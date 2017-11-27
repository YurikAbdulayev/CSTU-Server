package com.cstu.utils;

import com.cstu.domain.CstuUser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by yurii on 30.05.17.
 */
public class UserUtils {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static void sendMail(CstuUser user) {
        final String username = "cstu.ua@gmail.com";
        final String password = System.getenv("mailPass");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cstu.ua@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail() ));
            message.setSubject("Активація аккаунта");
//            message.setText("Для активації аккаунта натисніть на кнопку");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText( "Для активації аккаунта натисніть на кнопку", "utf-8" );

            Multipart multipart = new MimeMultipart( "alternative" );
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent( getButton(user.getId()), "text/html; charset=utf-8" );

            multipart.addBodyPart( htmlPart );
            message.setContent( multipart );

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getButton(Long userId) {
        return "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "  <tr>\n" +
                "    <td>\n" +
                "      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <tr>\n" +
                "          <td align=\"center\" style=\"-webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px;\" bgcolor=\"#e9703e\"><a href=\"https://cstu.herokuapp.com/api/verify/" + userId + "\" target=\"_blank\" style=\"font-size: 16px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; text-decoration: none; -webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; padding: 12px 18px; border: 1px solid #e9703e; display: inline-block;\">Активувати</a></td>\n" +
                "        </tr>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>";
    }
}
