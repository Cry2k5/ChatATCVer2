package util;

import chatroom.data;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;




public class Email {

    //email: vemailchatatc@gmail.com
    //pass: Anh2005!

        static final String from = "vemailchatatc@gmail.com";
        static final String password = "iencsgrjdsokjtiq";

        static final String to = data.email;

        public static void sendMail() {
            Properties props = new Properties();

            props.put("mail.smtp.host", "smtp.gmail.com");//SMTP HOST
            props.put("mail.smtp.port", "587"); //TLS 587 SSL 456
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            //Create Authenticator
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            };

            //Phien lam viec
            Session session = Session.getInstance(props, auth);

            //Gửi email;

            //Tao tin nhan
            MimeMessage msg = new MimeMessage(session);

            try {
                msg.addHeader("Content-type", "text/HTML; charset=UTF-8");

                //Nguoi gui
                msg.setFrom(from);

                //Nguoi nhan
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

                //Tieu de email
                msg.setSubject("YOUR OTP HERE!");

                //Quy dinh ngay gui
                //msg.setSentDate(new Date());

                //Quy dinh email nhan phan hoi
                //msg.setReplyTo(null);

                //noi dung
                msg.setText(data.OTP, "UTF-8");

                //Gui email
                Transport.send(msg);
            } catch (MessagingException e) {

                throw new RuntimeException(e);
            }
        }
    public static void main(String[] args) {

        // Tạo timer
        Timer timer = new Timer();

        // Đặt độ trễ (milliseconds)
        long delay = 5000; // Ví dụ: độ trễ 5 giây

        // Lên lịch gửi email sau khi độ trễ đã chờ
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMail();
            }
        }, delay);
    }



}
