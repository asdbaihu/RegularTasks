package hello.Controller;

import hello.Model.EmailRepository;
import hello.Model.ForSendingEmail;
import hello.Model.Sendemail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Component
public class SendEmailController {

   /* public SendEmailController(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                findAll();
            }
        }).start();
    }*/

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    ForSendingEmail forSendingEmail;

    volatile Iterable<Sendemail> sendEmails;

    private void findAll() {
        while (true) {
            try {
                sendEmails = emailRepository.findAll();

                sendEmails.forEach(sendEmail -> {
                    if (sendEmail.getNextexecution().
                            before(new Timestamp(System.currentTimeMillis()))) {
                        if (sendEmail.getFrequency() != null) {
                            saveNewNextExecution(sendEmail);
                        }
                        ThreadPoolTask.threadPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                sendEmail(sendEmail);
                                System.out.println(Thread.currentThread().getName() + " Email id = " + sendEmail.getId());
                            }
                        });

                    }
                });
            } catch (NullPointerException e) {
                System.out.println("No email files");
            }
        }
    }


    private void saveNewNextExecution(Sendemail sendEmail) {
        Sendemail sendEmailNew = new Sendemail();
        sendEmailNew = sendEmail;
        sendEmailNew.setNextexecution(new Timestamp(System.currentTimeMillis() + shouldConvertTimeBecauseJavaCannotByDepricatedWay(sendEmail.getFrequency())));
        emailRepository.delete(sendEmail.getId());
        emailRepository.save(sendEmailNew);
    }

    private long shouldConvertTimeBecauseJavaCannotByDepricatedWay(Time time) {
        long timeValue;
        timeValue = time.getHours() * 3600000 + time.getMinutes() * 60000 + time.getSeconds() * 1000;
        return timeValue;
    }

    private void sendEmailWithAttachments(String host, String port,
                                                final String userName, final String password, String toAddress,
                                                String subject, String message, String[] attachFiles)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.length > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);

    }

    private void sendEmail(Sendemail sendEmail){
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = forSendingEmail.getEmail();
        String password = forSendingEmail.getPassword();
        System.out.println(forSendingEmail.getEmail() + "  " + forSendingEmail.getPassword());

        // message info
        String mailTo = sendEmail.getScript();
        String subject = "Email with attachments from Anton for Infocom";
        String message = forSendingEmail.getText();

        // attachments
        String[] attachFiles = new String[1];
        attachFiles[0] = "D:\\anton\\AntonsTask.xls";


        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                    subject, message, attachFiles);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }


}
