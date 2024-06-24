package chatroom;

import chatroom.database.DatabaseHandler;
import chatroom.database.DatabaseQuery;
import chatroom.users.User;
import chatroom.users.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import util.Email;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class LoginController extends Thread{

	public Label signin_error;
	@FXML
    private TextField lo_email;

    @FXML
    private PasswordField lo_password;

    @FXML
    private Hyperlink lo_register_link;

    @FXML
    private Button lo_signin;

    @FXML
    private AnchorPane lo_view;

    @FXML
    private ImageView re_avatar;

    @FXML
    private TextField re_email;

    @FXML
    private Hyperlink re_login_link;

    @FXML
    private TextField re_name;

    @FXML
    private PasswordField re_password;

    @FXML
    private Button re_setavatar_btn;

    @FXML
    private Button re_signup;

    @FXML
    private AnchorPane re_view;
    
    @FXML
    private AnchorPane login_form;

	@FXML
	private Button verify_Cancel_btn;

	@FXML
	private Button verify_OK_btn;

	@FXML
	private TextField verify_OTP;

	@FXML
	private AnchorPane verify_view;
    
    private Alert alert;



	private Connection connect;
	private PreparedStatement prepare;
	private ResultSet result;
	public static String nameAccount ;

	public static String urlAvatar;

    public LoginController() throws SQLException, IOException {
    }

    public void loginBtn() throws Exception{
		DatabaseQuery databaseQuery = new DatabaseQuery(new DatabaseHandler());

		UserService userService = new UserService(databaseQuery);
		
		//check the email or password feild do write yet? if not, give notification error!

			if (lo_email.getText().isEmpty() || lo_password.getText().isEmpty()) {
				signin_error.setText("Please enter email and password!");
			} 
			else {
				signin_error.setText(null);
				String selectData = "SELECT name, email, avatar, password FROM users WHERE email = ? and password = ?";

				connect = JDBCUtil.getConnection();

				
					prepare = connect.prepareStatement(selectData);
					prepare.setString(1, lo_email.getText());
					prepare.setString(2, encodePassword(lo_password.getText()));

					result = prepare.executeQuery();

					// Nếu đăng nhập thành công sẽ chuyển sang giao diện chính của chương trình
					if (result.next()) {

						nameAccount = result.getString("name");
						urlAvatar = result.getString("avatar");
						String email = result.getString("email");
						System.out.println(result.getString("name"));
						
						data.name = result.getString("name");
						User user = userService.getUserByEmail(email);

						//Kết nối với giao diện chính khi đăng nhập thành công.

						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat.fxml"));

						ChatController controller = new ChatController();
						controller.setUser(user);
						loader.setController(controller);
						Parent root = loader.load();
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.setScene(scene);
						stage.show();
						lo_signin.getScene().getWindow().hide();
						JDBCUtil.closeConnect(connect);

					} else {
						signin_error.setText("Incorrect information!");
					}
				} 
	}

	public static String encodePassword(String password)
	{
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] encodeHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder hexString = new StringBuilder();
			for(byte b : encodeHash) {
				String hex = String.format("%02x", b);
				hexString.append(hex);
			}
			
			return hexString.toString();
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setAvatar() {

		FileChooser openFileChooser = new FileChooser();
		openFileChooser.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg", "*jpeg"));

		File file = openFileChooser.showOpenDialog(login_form.getScene().getWindow());

		if (file != null) {
			
			data.path = file.getAbsolutePath();

			re_avatar.setImage(new Image(file.toURI().toString(), 50, 50, false, true));
		}
	}

	private String generateOTP() {
		Random random = new Random();
		String otp = String.valueOf(random.nextInt(900000) + 100000); // Tạo số ngẫu nhiên từ 100000 đến 999999
		data.OTP = otp;
		return otp;
	}

	private boolean checkDuplicateCode(String code){
		boolean duplicate = false;
		//prepare = connect.prepareStatement("select ");
		//tt
		return false;
	}

	public void verifyOKbtn() throws SQLException {
		if(verify_OTP.getText().isEmpty()){
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Message");
			alert.setHeaderText(null);
			alert.setContentText("Please fill the OTP!");
			alert.showAndWait();
		}
		else{
			connect = JDBCUtil.getConnection();
			String updateData = "UPDATE users set verifyCode='' , status = 'verified' where email = ? limit 1";
			prepare = connect.prepareStatement(updateData);
			prepare.setString(1, data.email);

			if((Objects.equals(verify_OTP.getText(), data.OTP))){
				prepare.executeUpdate();
				alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Message");
				alert.setHeaderText(null);
				alert.setContentText("Successfully registered Account!");
				alert.showAndWait();
				verify_view.setVisible(false);
				resetData();
				JDBCUtil.closeConnect(connect);
			}
			else{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error OTP");
				alert.setHeaderText(null);
				alert.setContentText("OTP is INCORRECT!");
				alert.showAndWait();
			}
		}
	}

	public void verifyCancelbtn(ActionEvent event) throws SQLException {
		if(event.getSource()==verify_Cancel_btn){


			connect = JDBCUtil.getConnection();
			String deleteData = "DELETE FROM users WHERE email=? and status='unverified'";
			prepare = connect.prepareStatement(deleteData);
			prepare.setString(1, data.email);
			prepare.executeUpdate();
			JDBCUtil.closeConnect(connect);

			verify_view.setVisible(false);

		}
	}
    
    public void reBtn() throws Exception{

			if (re_email.getText().isEmpty() || re_password.getText().isEmpty() || re_name.getText().isEmpty() || data.path==null) {

				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields!");
				alert.showAndWait();

			}
				else {
				String reData = "INSERT INTO users(name, email, password, avatar, verifyCode, status)"
						+ "VALUES(?,?,?,?,?,?)";
				connect = JDBCUtil.getConnection();


					// Kiểm tra nếu email đã tồn tài.
					String checkEmail = "SELECT * FROM users WHERE email = '" + re_email.getText() + "' OR name = '"+ re_name.getText() +"'";

					prepare = connect.prepareStatement(checkEmail);
					result = prepare.executeQuery();
					
					
					String email_patern = "^[a-zA-Z0-9._%+-]+@(vku\\.udn\\.vn|gmail\\.com(\\.vn)?)$";

					if (result.next()) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText(re_email.getText() + " or " + re_name.getText()+ " is already taken");
						alert.showAndWait();
					}

					// Kiểm tra độ dài kí tự mật khâu đăng kí
					else if (re_password.getText().length() < 4) {
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The password must be at least 5 characters long!");
						alert.showAndWait();
					}
					
					
					else if(!Pattern.matches(email_patern, re_email.getText()))
					{
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Message");
						alert.setHeaderText(null);
						alert.setContentText("The email INVALID: example@gmail.com");
						alert.showAndWait();
					}
					else {

						prepare = connect.prepareStatement(reData);
						
						prepare.setString(1, re_name.getText());


						prepare.setString(2, re_email.getText());
						data.email = re_email.getText();

						prepare.setString(3, encodePassword(re_password.getText()));
						
						String path = data.path;

						path = path.replace("\\", "\\\\");


						prepare.setString(4, path);

						System.out.println(path);

						prepare.setString(5, String.valueOf(generateOTP()));

						prepare.setString(6,"unverified");

						prepare.executeUpdate();

						verify_view.setVisible(true);

						Email.sendMail();
						JDBCUtil.closeConnect(connect);

//						alert = new Alert(AlertType.INFORMATION);
//						alert.setTitle("Information Message");
//						alert.setHeaderText(null);
//						alert.setContentText("Successfully registered Account!");
//						alert.showAndWait();


					}

			}
	}

	public void closeConnect() throws SQLException {
		connect.close();
		prepare.close();
		result.close();
	}
	public void resetData(){
				re_email.setText("");
				re_password.setText("");
				re_name.setText("");
				re_avatar.setImage(null);	}

    
  public void switchForm(ActionEvent event) {
	  
	  if(event.getSource() == lo_register_link) {
		  lo_view.setVisible(false);
		  re_view.setVisible(true);
	  }
	  else if(event.getSource() == re_login_link) 
	  {
		  re_view.setVisible(false);
		  lo_view.setVisible(true);
		  
	  }
	
  }
  

}


