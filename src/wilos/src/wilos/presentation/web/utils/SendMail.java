package wilos.presentation.web.utils;

/*
 Some SMTP servers require a username and password authentication before you
 can use their Server for Sending mail. This is most common with couple
 of ISP's who provide SMTP Address to Send Mail.

 This Program gives any example on how to do SMTP Authentication
 (User and Password verification)

 This is a free source code and is provided as it is without any warranties and
 it can be used in any your code for free.

 Author : Sudhir Ancha
 */

import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import wilos.resources.LocaleBean;

/*
 To use this program, change values for the following three constants,

 SMTP_HOST_NAME -- Has your SMTP Host Name
 SMTP_AUTH_USER -- Has your SMTP Authentication UserName
 SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

 Next change values for fields

 emailMsgTxt  -- Message Text for the Email
 emailSubjectTxt  -- Subject for email
 emailFromAddress -- Email Address whose name will appears as "from" address

 Next change value for "emailList".
 This String array has List of all Email Addresses to Email Email needs to be sent to.


 Next to run the program, execute it as follows,

 SendMailUsingAuthentication authProg = new SendMailUsingAuthentication();

 */

public class SendMail {

    //public static final String SERVER_SMTP = "smtp.numericable.fr";
    public static final String SERVER_SMTP = "smtp.cict.fr";
    
    public static void postMail(String recipients[], String subject,
	    String message, String from)
	    throws MessagingException{

	//Set the host smtp address
	Properties props = new Properties();
	
	props.put("mail.smtp.host", SERVER_SMTP);

	// create some properties and get the default Session
	Session session = Session.getDefaultInstance(props, null);

	// create a message
	Message msg = new MimeMessage(session);

	// set the from and to address
	InternetAddress addressFrom = new InternetAddress(from);
	msg.setFrom(addressFrom);

	InternetAddress[] addressTo = new InternetAddress[recipients.length];
	for (int i = 0; i < recipients.length; i++) {
	    addressTo[i] = new InternetAddress(recipients[i]);
	}
	msg.setRecipients(Message.RecipientType.TO, addressTo);

	// Setting the Subject and Content Type
	msg.setSubject(subject);
	msg.setContent(message, "text/html");
	Transport.send(msg);

    }

}
