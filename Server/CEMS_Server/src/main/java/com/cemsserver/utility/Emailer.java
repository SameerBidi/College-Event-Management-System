package com.cemsserver.utility;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer
{
	public static boolean SendEmail(String emailID, String subject, String body)
	{
		String from = "id";
		String pass = "pass";
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try
		{
			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailID));

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			return true;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
			return false;
		} 
	}
}
