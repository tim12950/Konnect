package common;

import java.sql.Connection;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import loginAndRegister.RegisterBean;
import loginAndRegister.RegisterDAO;

public class Mail {
	
	public static void sendMail(String recip, String id, String code, RegisterBean RBean, Connection k) {
		
		Transport transport = null;
		
		try {
			RegisterDAO.insertIntoUsers(RBean, k, id, code); 
			
			String from = "konnect@tutanota.com";
			String to = recip;
			String body = "Please verify your email address by clicking the link below:\n\nhttp://env0.mxrnmhthx4.us-west-2.elasticbeanstalk.com/verify/" + code + "\n\nIf you did not sign up for Konnect, please ignore this email.\n\nBest,\nThe Konnect Team.";
			String subject = "Konnect Account Verification";
			
			String smtp_username = "*";
			String smtp_pass = "*";
			String host = "email-smtp.us-west-2.amazonaws.com";
			int port = 25;
			
			Properties props = System.getProperties();
	    	props.put("mail.transport.protocol", "smtps");
	    	props.put("mail.smtp.port", port);
	    	
	    	props.put("mail.smtp.auth", "true");
	    	props.put("mail.smtp.starttls.enable", "true");
	    	props.put("mail.smtp.starttls.required", "true");
	    	
	    	Session session = Session.getDefaultInstance(props);
	    	
	    	MimeMessage msg = new MimeMessage(session);
	    	
	    	msg.setFrom(new InternetAddress(from));
		    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		    msg.setSubject(subject);
		    msg.setContent(body,"text/plain");
		        
		    transport = session.getTransport();
		        
		    transport.connect(host, smtp_username, smtp_pass);
		    transport.sendMessage(msg, msg.getAllRecipients());
		    transport.close();
		    
		    RBean.setEverythingOK();
		}
		catch (Exception e) {
			//sqlexception or messagingexception or other; everythingok = false;
		}
		finally {
			try {
				transport.close();
			} catch (Exception e) {
				//do nothing; if exception not thrown in topmost try block, everything's ok except transport not closed;
				//if exception is thrown in topmost try block, user knows he'll have to try again
			}
		}
	}

}
