package org.perzpy.mail;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 发送邮件实现类
 * @author cxb
 * @date 2010-9-1
 * @version 1.0
 */
public class SendMail {

	/**
	 * 简单的发邮件方式 邮件内容只有标题和邮件内容 支持多个用户批量发送
	 * 
	 * @param mailServer SMTP服务器
	 * @param userName 用户名
	 * @param passWord 密码
	 * @param to 收件人，为数组形式
	 * @param cc 抄送人，为数组形式，可以为NULL
	 * @param bcc 密送人，为数组形式，可以为NULL
	 * @param from 发送人
	 * @param subject 邮件标题，可以为NULL
	 * @param content  邮件内容，可以为NULL
	 * @throws Exception
	 */
	public boolean sendSimpleEmail(String mailServer, String userName, String passWord, String[] to, String[] cc,
			String[] bcc, String from, String subject, String content) throws Exception {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailServer);
			props.put("mail.smtp.localhost", mailServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.user", userName);
			props.put("mail.smtp.password", passWord);
			MyAuthenticator auth = new MyAuthenticator();
			PasswordAuthentication pa = auth.performCheck(userName, passWord);
			Session session = Session.getDefaultInstance(props, auth);
			MimeMessage message = new MimeMessage(session);
			//设置发送人
			Address[] addresses = new Address[1];
			addresses[0] = new InternetAddress(from);
			message.addFrom(addresses);
			//设置收件人
			Address[] addressTo = new Address[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
			message.addRecipients(Message.RecipientType.TO, addressTo);
			//设置抄送人
			if (cc != null) {
				Address[] addressCc = new Address[cc.length];
				for (int i = 0; i < cc.length; i++) {
					addressCc[i] = new InternetAddress(cc[i]);
				}
				message.addRecipients(Message.RecipientType.CC, addressCc);
			}
			//设置密送人
			if (bcc != null) {
				Address[] addressBcc = new Address[bcc.length];
				for (int i = 0; i < bcc.length; i++) {
					addressBcc[i] = new InternetAddress(bcc[i]);
				}
				message.addRecipients(Message.RecipientType.BCC, addressBcc);
			}
			//设置主题
			if (subject != null) {
				message.setSubject(subject, "UTF-8");
			}
			//设置内容
			if (content != null) {
				message.setText(content, "UTF-8");
			}
			//保存
			message.saveChanges();

			Transport transport = session.getTransport("smtp");
			transport.connect(mailServer, userName, passWord);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 发送带附件的邮件方式 邮件内容有标题和邮件内容和附件，附件可以是本地机器上的文本，也可以是web上的一个URL 文件，
	 * 当为web上的一个URL文件时，此方法可以将WEB中的URL文件先下载到本地，再发送给收入用户
	 * 
	 * @param mailServer SMTP服务器
	 * @param userName 用户名
	 * @param passWord 密码
	 * @param to 收件人，为数组形式
	 * @param cc 抄送人，为数组形式，可以为NULL
	 * @param bcc 密送人，为数组形式，可以为NULL
	 * @param from 发送人
	 * @param subject 邮件标题，可以为NULL
	 * @param content  邮件内容，可以为NULL
	 * @param multiPaths 附件
	 * @throws Exception
	 */
	public boolean sendMultiPartEmail(String mailServer, String userName, String passWord, String[] to, String[] cc,
			String[] bcc, String from, String subject, String content, String[] multiPaths) throws Exception {

		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", mailServer);
			props.put("mail.smtp.localhost", mailServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.user", userName);
			props.put("mail.smtp.password", passWord);
			MyAuthenticator auth = new MyAuthenticator();
			PasswordAuthentication pa = auth.performCheck(userName, passWord);
			Session session = Session.getDefaultInstance(props, auth);
			MimeMessage message = new MimeMessage(session);
			//设置发送人
			Address[] addresses = new Address[1];
			addresses[0] = new InternetAddress(from);
			message.addFrom(addresses);
			//设置收件人
			Address[] addressTo = new Address[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
			message.addRecipients(Message.RecipientType.TO, addressTo);

			//设置抄送人
			if (cc != null) {
				Address[] addressCc = new Address[cc.length];
				for (int i = 0; i < cc.length; i++) {
					addressCc[i] = new InternetAddress(cc[i]);
				}
				message.addRecipients(Message.RecipientType.CC, addressCc);
			}
			//设置密送人
			if (bcc != null) {
				Address[] addressBcc = new Address[bcc.length];
				for (int i = 0; i < bcc.length; i++) {
					addressBcc[i] = new InternetAddress(bcc[i]);
				}
				message.addRecipients(Message.RecipientType.BCC, addressBcc);
			}
			//设置主题
			if (subject != null) {
				message.setSubject(subject, "UTF-8");
			}
			//设置附件
			BodyPart bodyPart = new MimeBodyPart();
			if (content != null) {
				bodyPart.setContent(content, "text/html;charset=UTF-8");
			}
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(bodyPart);
			for (int i = 0; i < multiPaths.length; i++) {
				bodyPart = new MimeBodyPart();
				DataSource source = null;
				String path = multiPaths[i];
				String fileName = path.substring(path.lastIndexOf(File.separator) + 1, path.length());
				if (path.indexOf("http") == -1) {
					source = new FileDataSource(path);
				} else {
					source = new URLDataSource(new URL(path));
				}
				bodyPart.setDataHandler(new DataHandler(source));
				bodyPart.setFileName(fileName);
				multiPart.addBodyPart(bodyPart);
			}

			message.setContent(multiPart);

			//保存
			message.saveChanges();

			Transport transport = session.getTransport("smtp");
			transport.connect(mailServer, userName, passWord);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public class MyAuthenticator extends Authenticator {
		String username = null;
		String password = null;

		public MyAuthenticator() {
		}

		public PasswordAuthentication performCheck(String user, String pass) {
			username = user;
			password = pass;
			return getPasswordAuthentication();
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	public static void main(String args[]) {
		SendMail test = new SendMail();
		try {
			System.out.println(test.sendSimpleEmail("smtp.163.com", "xxx@163.com", "pe3",
					new String[] { "xxx@gmail.com" }, null, null, "xxx@163.com", "你是我的唯一",
					"fdsfsdfdsfsdf"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
