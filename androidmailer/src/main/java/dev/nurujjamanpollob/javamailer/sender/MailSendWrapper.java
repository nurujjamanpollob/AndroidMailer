/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022 Nurujjaman Pollob, All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * If you have contributed in codebase,
 * and want to add your name or copyright in a particular class or method,
 * you must follow this following pattern:
 * <code>
 *     // For a new method created by you,
 *     //like this example method with name fooMethod()
 *     //then use following format:
 *
 *     >>>
 *     @author $Name and $CurrentYear.
 *     $Documentation here.
 *     $Notes
 *     public boolean fooMethod(){}
 *     <<<
 *
 *     // For an existing method
 *
 *     >>>
 *     $Current Method Documentation(Update if needed)
 *
 *     Updated by $YourName
 *     $Update summery
 *     $Notes(If any)
 *     <<<
 *
 *     // For a new class of file, that is not created by anyone else
 *     >>>
 *     Copyright (c) $CurrentYear $Name, All right reserved.
 *
 *     $Copyright Text.
 *     $Notes(If Any)
 *     <<<
 *
 *     // For a existing class, if you want to add your own copyright for your work.
 *
 *     >>>
 *     $Current Copyright text
 *
 *     $YourCopyrightText
 *     <<<
 *
 *     Done! Clean code!!
 * </code>
 */

package dev.nurujjamanpollob.javamailer.sender;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import dev.nurujjamanpollob.javamailer.backgroundtaskexecutor.NJPollobCustomAsyncTask;
import dev.nurujjamanpollob.javamailer.entity.Attachment;
import dev.nurujjamanpollob.javamailer.security.SecurityDriver;
import dev.nurujjamanpollob.javamailer.security.SecurityPlugin;
import dev.nurujjamanpollob.javamailer.security.utility.SecurityPluginUtility;

/**
 * This class is designed to simplify mail sending experience in Android Application
 * It launches a background thread to send Email background.
 * If you do update application UI from Background thread,
 * Be sure to use: YourActivity.this.runOnUiThread(Runnable task); To update application UI,
 * it is highly recommended.
 */
public class MailSendWrapper extends NJPollobCustomAsyncTask<Void, String> {

    private final String fromAddress;
    private final String toAddress;
    private final String password;
    private final String mailSubject;
    private final String mailMessage;
    private MessageSendListener listener;
    private final Provider serviceProviderConfiguration;
    private String errorMessage;
    private boolean sendFileWithAttachment;
    private Attachment[] attachments;

    /**
     * Constructor Parameter to Configure MailSendWrapper with basic parameters.
     * Supports decoding support of all String parameters that annotated with @DecodeWith
     * @param fromAddress set the email from address field,
     *                    for example if you are sending email by nurujjamanpollob@androiddev.io,
     *                    you should pass this value as argument.
     * @param toAddress set the target address, the recipient address.
     * @param password the SMTP host or POP3 host account password
     * @param mailSubject set the email subject.
     * @param mailMessage sets the email body message.
     * @param serviceProviderConfiguration argument for Provider class, that contains SMTP or POP3 server configuration to send the email.
     *                                     For more information,
     * @see Provider class for more information
     */
    public MailSendWrapper(
            @NonNull
            String fromAddress,
            @NonNull
            String toAddress,
            @NonNull
            String password,
            @NonNull
            String mailSubject,
            @NonNull
            String mailMessage,
            @NonNull
            Provider serviceProviderConfiguration
    ) throws Exception {

        this.fromAddress = checkIfAnnotatedForDecoding(fromAddress, 0);
        this.toAddress = checkIfAnnotatedForDecoding(toAddress, 1);
        this.password = checkIfAnnotatedForDecoding(password, 2);
        this.mailSubject = checkIfAnnotatedForDecoding(mailSubject, 3);
        this.mailMessage = checkIfAnnotatedForDecoding(mailMessage, 4);
        this.serviceProviderConfiguration = serviceProviderConfiguration;
    }

    /**
     * Method to check if a passed constructor parameter at given index has been annotated with @DecodeWith(decoder = Class<? extends SecurityPlugin>)
     * If annotated, decode the encoded String at initialization time, and initialize the decoded String.
     * @param inputString the passed String from parameter
     * @param parameterIndex parameter index to check
     * @return decoded String if the parameter is annotated with @DecodeWith
     * @throws Exception if there are constructor index,
     * parameter index, Security driver SecurityPlugin class has no correct number of parameter, or decoding method invocation failed.
     */
    private String checkIfAnnotatedForDecoding(String inputString, int parameterIndex) throws Exception {

        SecurityPluginUtility securityPluginUtility = new SecurityPluginUtility(0, super.getClass(), parameterIndex);

        if(securityPluginUtility.isConstructorParameterIsAnnotatedWithAnnotationClass()){

            Class<? extends SecurityPlugin> securityPluginClass = securityPluginUtility.getDecoderClassForSecurityDriver();

            return new SecurityDriver(inputString, securityPluginClass).getDecodedString();
        }

        return inputString;
    }

    /**
     * This method executes when a background thread is starting
     */
    @MainThread
    @Override
    protected void preExecute() {
        super.preExecute();

        // If listener is not null, inform the listener
        if(listener != null){
            listener.whileSendingEmail();
        }
    }

    /**
     * Method to send email with passed parameters, from background thread
      */
    @WorkerThread
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
            message.setSubject(mailSubject , "UTF8");

            // Create Mime MultiPart instance and attach message body
            MimeMultipart mimeMultipart = new MimeMultipart();

            // Create multipart body
            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setDataHandler(new DataHandler(mailMessage, "text/html"));

            // Attach to multipart
            mimeMultipart.addBodyPart(messageBody);

            // if flag indicates that we need to append file as attachment
            // And Attachment array is not null
            if (sendFileWithAttachment && attachments != null && attachments.length > 0){

                // Lets iterate over The array and add every attachment to the Mime Multi Part
                for(Attachment attachment : attachments){
                    // Create attachment part
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    // assign file byte with mime type
                    attachmentPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment.getAttachmentByte(), attachment.getAttachmentMimeType())));
                    // Set attachment file name
                    attachmentPart.setFileName(attachment.getAttachmentName());
                    // attach to multipart
                    mimeMultipart.addBodyPart(attachmentPart);
                }

            }

            // attach Multipart to Mime Message
            message.setContent(mimeMultipart);
            //send message
            Transport.send(message);

            return toAddress;


        }
        // Email send exception
        catch (MessagingException messagingException){
            // Get error message and update to variable
            this.errorMessage = messagingException.getMessage();

            // Return null means there is error
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
        // put all the properties from provider to properties object
        props.putAll(provider.getAllConfigurations());

        /* For improvement of security, using TLS is requires
         If the basic configuration for TLS is not found on Provider.getAllConfigurations
         This library will add some TLS based configs, anyway this can be disabled by passing Provider(Boolean isUseTLS) false
         By default, it is false
         */
        if (provider.getIsUseTLS()){

            // Lets check for TLS configuration
            // If the some parameter is missing, add missing configuration for working TLS
            String tlsEnable = "mail.smtp.starttls.enable";
            String tlsRequires = "mail.smtp.starttls.required";
            String tlsSSLProtocols = "mail.smtp.ssl.protocols";

            if(provider.getValueForAConfiguration(tlsEnable) == null){
                props.put(tlsEnable, "true");
            }
            if (provider.getValueForAConfiguration(tlsRequires) == null){
                props.put(tlsRequires, "true");
            }

            if(provider.getValueForAConfiguration(tlsSSLProtocols) == null){
                props.put(tlsSSLProtocols, "TLSv1.2");
            }
        }


        return props;

    }

    /**
     * This method executes in the background thread
     * @return null if the email send to client is unsuccessful, else the recipient address is returned.
     */
    @WorkerThread
    @Override
    protected String doBackgroundTask() {
       return sendEmailToClient();

    }

    /**
     * This method executed when background thread finishes background task
     * @param output the output from background thread
     */
    @MainThread
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

    /**
     * Listener interface to listen on MailSendWrapper Event and provide different callbacks
     */
    public interface MessageSendListener{

        /**
         * This method will be invoked when the email sending task starts
         */
        default void whileSendingEmail(){}

        /**
         * This method will be invoked when a email to recipient address is successfully delivered
         * @param toRecipientAddress the String containing recipient email address
         */
        default void onEmailSent(String toRecipientAddress){}

        /**
         * This method will be invoked when the email sending process has been failed.
         * @param errorMessage the String containing error description
         */
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

        sendFileWithAttachment = false;
        // Run background thread
        this.runThread();

    }


    /**
     * Method to send a email to recipient email address with Multiple Attachment
     */
    public void sendEmailWithAttachment(Attachment[] attachments){
        sendFileWithAttachment = true;
        // Assign variable values
        this.attachments = attachments;
        // Run background thread
        this.runThread();


    }

    /**
     * Method to send a email to recipient email address with a Single Attachment
     */
    public void setSendFileWithAttachment(Attachment attachment){

        sendFileWithAttachment = true;
        this.attachments = new Attachment[1];
        attachments[0] = attachment;
        // Run background thread
        this.runThread();
    }

    /**
     * Override ToString method to print the class details
     * @return String containing class details
     */

    @NonNull
    @Override
    public String toString() {
        return "MailSendWrapper{" +
                "fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", password='" + password + '\'' +
                ", mailSubject='" + mailSubject + '\'' +
                ", mailMessage='" + mailMessage + '\'' +
                ", listener=" + listener +
                ", serviceProviderConfiguration=" + serviceProviderConfiguration +
                ", errorMessage='" + errorMessage + '\'' +
                ", sendFileWithAttachment=" + sendFileWithAttachment +
                ", attachments=" + Arrays.toString(attachments) +
                '}';
    }




}
