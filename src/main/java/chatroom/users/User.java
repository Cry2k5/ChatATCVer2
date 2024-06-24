package chatroom.users;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private Integer id;
    public SimpleStringProperty name;
    public SimpleStringProperty image;
    private String email;
    private String password;
    private Integer verifyCode;

    public User(Integer id, SimpleStringProperty name, SimpleStringProperty image, String email, String password, Integer verifyCode) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public User(String name, String image) {
        this.name = new SimpleStringProperty(name);
        this.image = new SimpleStringProperty(image);
    }

    public String getname() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setname(String name) {
        if (this.name == null) {
            this.name = new SimpleStringProperty();
        }
        this.name.set(name);
    }

    public String getimage() {
        return image.get();
    }

    public SimpleStringProperty imageProperty() {
        return image;
    }

    public void setimage(String image) {
        if (this.image == null) {
            this.image = new SimpleStringProperty();
        }
        this.image.set(image);

    }
    public User(){}

    public User(Integer id, SimpleStringProperty name, SimpleStringProperty image, String email, String password) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String name, String email, String password, Integer verifyCode) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
    }

    public User(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getVerifyCode() {
        return verifyCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerifyCode(Integer verifyCode) {
        this.verifyCode = verifyCode;
    }

}
