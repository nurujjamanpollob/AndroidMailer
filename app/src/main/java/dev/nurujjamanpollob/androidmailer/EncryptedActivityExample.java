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
 *
 *
 */

package dev.nurujjamanpollob.androidmailer;

import static dev.nurujjamanpollob.androidmailer.StaticVars.MAIL_HOST;
import static dev.nurujjamanpollob.androidmailer.StaticVars.MAIL_PASSWORD;
import static dev.nurujjamanpollob.androidmailer.StaticVars.MAIL_SENDER_SEND_FROM_ADDRESS;
import static dev.nurujjamanpollob.androidmailer.StaticVars.isUseAuth;
import static dev.nurujjamanpollob.androidmailer.StaticVars.isUseTls;
import static dev.nurujjamanpollob.androidmailer.StaticVars.smtpPortAddress;
import static dev.nurujjamanpollob.androidmailer.StaticVars.socketFactoryPortAddress;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import dev.nurujjamanpollob.androidmailer.overrides.MailWrapperSecure;
import dev.nurujjamanpollob.androidmailer.overrides.ProviderSecure;
import dev.nurujjamanpollob.javamailer.entity.Attachment;
import dev.nurujjamanpollob.javamailer.sender.MailSendWrapper;
import dev.nurujjamanpollob.javamailer.sender.Providers;
import dev.nurujjamanpollob.javamailer.utility.AndroidUriToAttachmentUtility;


/**
 * This example used to show, how {@link dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith} can help decode all the encrypted parameter.
 * For more information see:
 *
 * @see MailWrapperSecure
 * @see ProviderSecure
 * @see dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith
 */
public class EncryptedActivityExample extends Activity {

    private final int pickFileRequestCode = 100;
    private Attachment attachment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set content View
        setContentView(R.layout.activity_encrypted);

        // Content Views
        EditText receiveMailField = findViewById(R.id.email_receiver_field_encrypted);
        EditText emailSubjectField = findViewById(R.id.email_subject_field_encrypted);
        EditText emailTextContentField = findViewById(R.id.email_message_field_encrypted);
        ImageView pickAttachment = findViewById(R.id.pick_file_encrypted);
        Button submitButton = findViewById(R.id.email_submit_button_encrypted);
        Button generalActivityButton = findViewById(R.id.general_activity_button);


        // Set click listener for pick attachment button
        pickAttachment.setOnClickListener(view -> pickFileFromSystem());

        // Set click listener for submit button
        submitButton.setOnClickListener(view -> {
            String receiverMailAdd = receiveMailField.getText().toString();
            String subject = emailSubjectField.getText().toString();
            String message = emailTextContentField.getText().toString();

            // Call sendMail method to send email
            try {
                sendEmailUsingJavaMailer(receiverMailAdd, subject, message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        // Set click listener for general activity button
        generalActivityButton.setOnClickListener(view -> launchGeneralActivity());


    }

    /**
     * Method to send email using JavaMailer, and usages a custom example of MailSendWrapper and Provider classes.
     *
     * @param receiverMailAdd the receiver mail address
     * @param subject         the email subject
     * @param message         the email message, HTML Markup is allowed
     */
    private void sendEmailUsingJavaMailer(String receiverMailAdd, String subject, String message) throws Exception {


        // Create service provider configuration
        ProviderSecure serviceProviderConfig = new ProviderSecure(
                MAIL_HOST,
                smtpPortAddress,
                socketFactoryPortAddress,
                Providers.getSecureSocketFactoryName(),
                isUseAuth,
                isUseTls);

        // Print service provider configuration
        printProviderConfigs(serviceProviderConfig);

            /*
            Basic mail credentials is provided, if you need to provide additional mail properties,
            use: serviceProviderConfig.putConfiguration(String propertyKey, String propertyValue);
            This can also be used to Override current mail service configuration
             */
        // send email to server using wrapper
        MailWrapperSecure mailSendWrapper = new MailWrapperSecure(
                MAIL_SENDER_SEND_FROM_ADDRESS, // from address field
                receiverMailAdd, // receiver mail address
                MAIL_PASSWORD, // mailbox password
                subject,
                message,
                serviceProviderConfig);

        // Print Wrapper configuration
        printMailWrapperConfigs(mailSendWrapper);

        // Set Listener for mail send event
        mailSendWrapper.setMailSendEventListener(new MailSendWrapper.MessageSendListener() {
            @Override
            public void whileSendingEmail() {

                Toast.makeText(EncryptedActivityExample.this, "Sending Mail...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmailSent(String totoRecipientAddress) {
                // reset attachment
                attachment = null;
                Toast.makeText(EncryptedActivityExample.this, "Mail sent to " + totoRecipientAddress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmailSendFailed(String errorMessage) {
                // reset attachment
                attachment = null;
                Toast.makeText(EncryptedActivityExample.this, "Mail send Exception " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


        // Fire mail sender to send to client
        // When attachment is not null
        // Call mailSendWrapper.setSendFileWithAttachment(attachment);
        if (attachment != null && attachment.isAttachmentNotNull()) {
            mailSendWrapper.setSendFileWithAttachment(attachment);
        }
        // When attachment is null
        else {
            mailSendWrapper.doSendEmailToFollowingClient();
        }
    }

    /**
     * Pick file from system
     */
    private void pickFileFromSystem() {

        Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooserIntent.setType("*/*");
        fileChooserIntent = Intent.createChooser(fileChooserIntent, "Choose Attachment");

        // Start Chooser
        startActivityForResult(fileChooserIntent, pickFileRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pickFileRequestCode) {
            if (resultCode == RESULT_OK) {

                // get content URI
                Uri contentUri = data != null ? data.getData() : null;
                // Get attachment from URI
                attachment = new AndroidUriToAttachmentUtility(contentUri, EncryptedActivityExample.this).getAttachmentInstance();

            } else {
                attachment = null;
            }
        }
    }

    /**
     * method to launch general activity
     */
    private void launchGeneralActivity() {
        Intent intent = new Intent(EncryptedActivityExample.this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * Print Mail Wrapper Secure configs - Remove if necessary
     */
    private void printMailWrapperConfigs(MailWrapperSecure wrapper) {

        System.out.println("Mail Send Wrapper configs are: " + wrapper);
    }

    /**
     * Print Service Provider configs - Remove if necessary
     */
    private void printProviderConfigs(ProviderSecure provider) {

        System.out.println("Provider configs are: " + provider);
    }
}
