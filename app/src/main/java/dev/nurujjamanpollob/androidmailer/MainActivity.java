package dev.nurujjamanpollob.androidmailer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.nurujjamanpollob.javamailer.sender.MailSendWrapper;
import dev.nurujjamanpollob.javamailer.sender.Provider;
import dev.nurujjamanpollob.javamailer.sender.Providers;


public class MainActivity extends AppCompatActivity {


    private final String MAIL_SENDER_SEND_FROM_ADDRESS = "founder@willtoeat.com";
    private final String MAIL_HOST = "mail.privateemail.com";
    private final String MAIL_PASSWORD = "$$0203040506$$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText receiveMailField = findViewById(R.id.email_receiver_filed);
        EditText emailSubjectField = findViewById(R.id.email_subject_filed);
        EditText emailTextContentField = findViewById(R.id.email_message_field);

        // Submit button Field
        Button submitButton = findViewById(R.id.email_submit_button);

        // set click listener
        submitButton.setOnClickListener(view -> {
            String receiverMailAdd = receiveMailField.getText().toString();
            String subject = emailSubjectField.getText().toString();
            String message = emailTextContentField.getText().toString();

            // Create service provider configuration
            Provider serviceProviderConfig = new Provider(
                    MAIL_HOST,
                    "465",
                    "465",
                    Providers.getSecureSocketFactoryName(),
                    true,
                    true);

            // send email to server
            MailSendWrapper mailSendWrapper = new MailSendWrapper(
                    MAIL_SENDER_SEND_FROM_ADDRESS,
                    receiverMailAdd,
                    MAIL_PASSWORD,
                    subject,
                    message,
                    serviceProviderConfig);

            // Listen to event
            mailSendWrapper.setMailSendEventListener(new MailSendWrapper.MessageSendListener() {
                @Override
                public void whileSendingEmail() {


                    System.out.println("Sending...");
                    Toast.makeText(MainActivity.this, "Sending Mail...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onEmailSent(String totoRecipientAddress) {

                    System.out.println("Sent");
                    Toast.makeText(MainActivity.this, "Mail sent to " + totoRecipientAddress, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onEmailSendFailed(String errorMessage) {


                    System.out.println(errorMessage);
                    Toast.makeText(MainActivity.this, "Mail send Exception " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });


            // Fire mail sender to send to client
            mailSendWrapper.doSendEmailToFollowingClient();
        });

    }
}