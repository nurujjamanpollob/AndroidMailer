package dev.nurujjamanpollob.javamailer.sender;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dev.nurujjamanpollob.javamailer.backgroundtaskexecutor.NJPollobCustomAsyncTask;

public class MailSendWrapper extends NJPollobCustomAsyncTask<Void, String> {

    private final String fromAddress;
    private final String toAddress;
    private final String password;
    private final String mailSubject;
    private final String mailMessage;
    private MessageSendListener listener;
    private final Provider serviceProviderConfiguration;
    private String errorMessage;

    public MailSendWrapper(String fromAddress, String toAddress, String password, String mailSubject, String mailMessage, Provider serviceProviderConfiguration) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.password = password;
        this.mailSubject = mailSubject;
        this.mailMessage = mailMessage;
        this.serviceProviderConfiguration = serviceProviderConfiguration;
    }

    @Override
    protected void preExecute() {
        super.preExecute();

        // If listener is not null, inform the listener
        if(listener != null){
            listener.whileSendingEmail();
        }
    }

    // Method to send email with Provider Data
    private String sendEmailToClient() {

        //get Session
        Session session = Session.getInstance(getMailPropertiesFromProvider(serviceProviderConfiguration),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromAddress, password);
                    }
                });
        // Try to send email
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toAddress));
            message.setFrom(fromAddress);
            message.setSubject(mailSubject);
            message.setText(mailMessage);
            //send message
            Transport.send(message);



            return toAddress;


        }catch (MessagingException messagingException){

            this.errorMessage = messagingException.getMessage();
            return null;
        }


    }

    /**
     * Method to get Properties to configure Mail sender plugin
     * @param provider the Provider instance having all necessary parameter
     * @return Properties, that can be used by mail authenticator to send email to user email address
     */
    private Properties getMailPropertiesFromProvider(Provider provider){

        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", provider.getMailSMTPHostAddress());
        props.put("mail.smtp.socketFactory.port", provider.getSocketFactoryPortAddress());
        props.put("mail.smtp.socketFactory.class", provider.getSocketFactoryClassName());
        props.put("mail.smtp.auth", provider.getUseAuth());
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.port", provider.getMailSMTPPortAddress());

        return props;

    }

    @Override
    protected String doBackgroundTask() {
       return sendEmailToClient();

    }

    @Override
    protected void onTaskFinished(String output) {
        super.onTaskFinished(output);

        if(output != null){
            if(listener != null){
                listener.onEmailSent(toAddress);
            }
        }else {
            if(listener != null){
                listener.onEmailSendFailed(errorMessage);
            }

        }



    }

    public interface MessageSendListener{

        default void whileSendingEmail(){}
        default void onEmailSent(String totoRecipientAddress){}
        default void onEmailSendFailed(String errorMessage){}


    }

    /**
     * Method to set Listener for mail send event
     * @param eventListener the instance of MessageSendListener, that will receive callback
     */
    public void setMailSendEventListener(MessageSendListener eventListener){

        this.listener = eventListener;
    }

    /**
     * Method to send email to client using a Background Thread
     */
    public void doSendEmailToFollowingClient(){

        // Run background thread
        this.runThread();

    }





}
