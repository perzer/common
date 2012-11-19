package org.perzpy.mail;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
/**
 * @author hackerzhou
 * @version 2010-8-12 09:06:31
 */
public class MailUtil {
    private Session session = null;
    private Properties properties = System.getProperties();
    private Authenticator authenticator = null;
    private HashMap<String, String> mailAttachment = new HashMap<String, String>();
 
    public static void main(String[] args) {
        String[] to = { "***@***.com" };
        String[] cc = { "***@***.com" };
        String content = "<html><head></head><body><img src=\"cid:Sunset.jpg\"/></body></html>";
        String subject = "Test HTML";
        MailUtil util = new MailUtil("***.***.com", false, null, null, false,
                null);
        util.addAttachment("D:\\test.jpg");
        util.send("***@***.com", to, cc, subject, content);
    }
 
    /**
     * No SMTP auth and no SSL
     */
    public MailUtil(String smtpHost) {
        this(smtpHost, false, null, null, false, null);
    }
 
    /**
     * SMTP auth with SSL
     */
    public MailUtil(String smtpHost, final String username,
            final String password, String sslPort) {
        this(smtpHost, true, username, password, true, sslPort);
    }
 
    /**
     * SMTP auth without SSL
     */
    public MailUtil(String smtpHost, final String username,
            final String password) {
        this(smtpHost, true, username, password, false, null);
    }
 
    /**
     * All in one setting
     */
    public MailUtil(String smtpHost, boolean needAuth, final String username,
            final String password, boolean isSSL, String sslPort) {
        setSMTPHost(smtpHost);
        if (isSSL) {
            enableSSL(sslPort);
        }
        if (needAuth) {
            enableAuth(username, password);
        }
        session = getSession(needAuth);
    }
 
    /**
     * Add attachment to email.
     */
    public void addAttachment(String filePath) {
        if (isStringEmpty(filePath)) {
            throw new RuntimeException("[Error] Attachment filepath is empty!");
        }
        mailAttachment.put(filePath, new File(filePath).getName());
    }
 
    /**
     * Send email from specified from-address to specified to-addresses /
     * cc-addresses with given subject and content.
     * <p>
     * If already call addAttachment, the method will try to include them into
     * email body
     */
    public void send(String fromAddress, String[] toEmailAddresses,
            String[] ccEmailAddresses, String subject, String content) {
        MimeMessage message = new MimeMessage(session);
        MimeMultipart multipart = new MimeMultipart();
        try {
            message.setSubject(subject);
            message.setRecipients(Message.RecipientType.TO,
                    emailToInternetAddressArray(toEmailAddresses));
            message.setRecipients(Message.RecipientType.CC,
                    emailToInternetAddressArray(ccEmailAddresses));
            message.addFrom(InternetAddress.parse(fromAddress));
            BodyPart mainBody = new MimeBodyPart();
            mainBody.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(mainBody);
            for (Entry<String, String> e : mailAttachment.entrySet()) {
                BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler(new DataHandler(new FileDataSource(e
                        .getKey())));
                bodyPart.setFileName(e.getValue());
                bodyPart.setHeader("Content-ID", e.getValue());
                multipart.addBodyPart(bodyPart);
            }
            message.setContent(multipart);
            message.saveChanges();
            Transport.send(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
 
    private void setSMTPHost(String smtpHost) {
        if (smtpHost == null) {
            throw new RuntimeException("[Error] SMTP Host is empty!");
        }
        properties.setProperty("mail.smtp.host", smtpHost);
    }
 
    private Session getSession(boolean needAuth) {
        mailAttachment.clear();
        return needAuth ? session = Session.getInstance(properties,
                authenticator) : Session.getInstance(properties);
    }
 
    private void enableAuth(final String username, final String password) {
        if (username == null || password == null) {
            throw new RuntimeException("[Error] Username or password is empty!");
        }
        properties.put("mail.smtp.auth", "true");
        authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }
 
    private void enableSSL(String sslPort) {
        if (isStringEmpty(sslPort)) {
            throw new RuntimeException("[Error] SSL port is empty!");
        }
        properties.setProperty("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", sslPort);
        properties.setProperty("mail.smtp.socketFactory.port", sslPort);
    }
 
    private boolean isStringEmpty(String s) {
        return s == null || s.length() == 0;
    }
 
    private InternetAddress[] emailToInternetAddressArray(String[] email)
            throws AddressException {
        if (email == null) {
            return new InternetAddress[0];
        }
        InternetAddress[] addresses = new InternetAddress[email.length];
        for (int i = 0; i < email.length; i++) {
            addresses[i] = new InternetAddress(email[i]);
        }
        return addresses;
    }
}