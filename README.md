# AndroidMailer

## A Java Android library to send email

This project is mainly use <b>javax.mail</b> and <b>javax.activation</b> packages to configure mail plugin and send email to target email address.

Also, this project uses <b>android.os</b> and <b>java.util.concurrent</b> to utilize effiecient background work processing.


## Project Configuration

You need to add following code in your project level build.gradle file:

<pre>
<code>  	
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
		
    }

}
</code>
</pre>

<br />

<br />

Note:Newer gradle android project prefer settings.gradle repository configuration over project level build.gradle configuration. In that case, you should add following code in your settings.gradle file:

<pre>
<code>  
pluginManagement {
	repositories {
        ...
        maven { url 'https://jitpack.io' }
              
    }
}
</code>
</pre>

<br />
<br />


After that, you should add following code in your app module's <b>build.gradle</b> file:

<pre>
<code> 	
dependencies {
	implementation 'com.github.nurujjamanpollob:AndroidMailer:2.0'
}
</code>
</pre>

This library needs internet permission to send data from phone to remote email server. So in your app module <b>AndroidManifest.xml</b> file, add this following code:

	<uses-permission android:name="android.permission.INTERNET"/>


So, as far, your project configuration is done. Let's dive into implementation part!

## Implementaion

It's very easy to use this library to send email, and listen on email sending event, you can configure this library in your own way, and this library is designed to help save your time.

First, you need to import following packages and classes:

<pre>
<code>
import dev.nurujjamanpollob.javamailer.entity.Attachment;
import dev.nurujjamanpollob.javamailer.sender.MailSendWrapper;
import dev.nurujjamanpollob.javamailer.sender.Provider;
import dev.nurujjamanpollob.javamailer.sender.Providers;
</code>
</pre>

<br />

Then, send email like this, in this example, we gonna send email with or without attachment file. This logic flow is depending on if the Attachment instance is null or not null.


<pre>
<code>

private final String MAIL_SENDER_SEND_FROM_ADDRESS = "fromaddress@domain.com";
private final String MAIL_HOST = "mail.domain.com";
private final String MAIL_PASSWORD = "mailbosspasswordhere";
private final String smtpPortAddress = "465";
private final String socketFactoryPortAddress = "465";
private String receiverMailAdd = "receiver@domain.com";
private String subject = "Mail subject goes here";
private String message = "Mail message body goes here, supports HTML markup";

// to create Attachment instance from android intent Uri, keep following
private Attachment attachment = new Attachment(byte[] fileByte, String fileNameIncludingExtension, String fileMimeType);
    
    
    // Create service provider configuration
    Provider serviceProviderConfig = new Provider(
             MAIL_HOST, // SMTP Host name
             smtpPortAddress, // SMTP Host port number
             socketFactoryPortAddress, // java mail socket factory address, should be same as smtp port
             Providers.getSecureSocketFactoryName(), // Java secure socket factory name
             isUseAuth, // use auth or no flag
             isUseTls // Use TLS to securely transfer mail flag
	    );


            /*
            Basic mail credentials is provided, if you need to provide additional mail properties,
            use: serviceProviderConfig.putConfiguration(String propertyKey, String propertyValue);
            This can also be used to Override current mail service configuration
             */

     // send email to server using wrapper
     MailSendWrapper mailSendWrapper = new MailSendWrapper(
            MAIL_SENDER_SEND_FROM_ADDRESS, // Set from address
            receiverMailAdd, // Receiver mail address
            MAIL_PASSWORD, // Mail box password for authorization purposes
            subject, // Mail subject
            message, // Mail body, supports HTML markup
            serviceProviderConfig // Service provider configuration
	   );

      // Listen to event
     mailSendWrapper.setMailSendEventListener(new MailSendWrapper.MessageSendListener() {
     
     	  // Invokes when the background thread started running
          @Override
           public void whileSendingEmail() {

              Toast.makeText(MainActivity.this, "Sending Mail...", Toast.LENGTH_SHORT).show();
           }

	   // Invokes when the mail sender plugin finishes sending email
           @Override
           public void onEmailSent(String toRecipientAddress) {
	   
           // reset attachment
           attachment = null;
           Toast.makeText(MainActivity.this, "Mail sent to " + toRecipientAddress, Toast.LENGTH_SHORT).show();
	   
           }

	  // Invokes when mail sending failes
          @Override
          public void onEmailSendFailed(String errorMessage) {
	  
          // reset attachment
          attachment = null;
          Toast.makeText(MainActivity.this, "Mail send Exception " + errorMessage, Toast.LENGTH_SHORT).show();
          
	  }
	  
	  
     });



     // Send email to client
     // When attachment is not null
     // Call mailSendWrapper.setSendFileWithAttachment(attachment); to send the attached attachment
     if(attachment != null && attachment.isAttachmentNotNull()){
          mailSendWrapper.setSendFileWithAttachment(attachment);
     }
     // When attachment is null
     else {
           mailSendWrapper.doSendEmailToFollowingClient();
     }
	    
</code>
</pre>



Easy to configure huh?

Anyway, to send Attachment or Attachment array, you need a instance of <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/entity/Attachment.java">Attachment.Java</a>

Whichs needs a <b>byte[]</b>, <b>File Name with extension </b>, <b> File Mime Type </b> as a constructor parameter, So the libarary plugin can process your attachment.

Now, in this example, I gonna show you how to use Android File Picker to pick a file, and use <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/utility/AndroidUriToAttachmentUtility.java">AndroidUriToAttachmentUtility.java</a> to process the Android File Intent Uri to get a <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/entity/Attachment.java">Attachment</a> instance.


This class: <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/utility/AndroidUriToAttachmentUtility.java">AndroidUriToAttachmentUtility.java</a> requires Uri from intent and Your Activity instance to get all necessary parameters.

The detailed documentation will be come soon if I get enough response.

Okay, let's dive in.


<b> Navigate to your activity file and add following import: </b>

<pre>
<code> 
import android.net.Uri;
import dev.nurujjamanpollob.javamailer.utility.AndroidUriToAttachmentUtility;
</code>
</pre>

<br />

Then add this field:

<pre>
<code> 
private final int pickFileRequestCode = 11223344;
private Attachment attachment;
</code>
</pre>

Then, add this method, firing this method will launch a file picker dialog to choose file from system.

<pre>
<code>
 /**
     * Method to pick file from system
     */
    private void pickFileFromSystem(){

        Intent fileChooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooserIntent.setType("*/*");
        fileChooserIntent = Intent.createChooser(fileChooserIntent, "Choose Attachment");

        // Start Chooser
        startActivityForResult(fileChooserIntent, pickFileRequestCode);

    }
</code>
</pre>

Now Override <pre><code>onActivityresult(requestCode, resultCode, data)</code></pre> in your activity and add following code, 

So the implementation will be like this: 

<pre>
<code>
 @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pickFileRequestCode){
            if(resultCode == RESULT_OK){

                // get content URI
                Uri contentUri = data != null ? data.getData() : null;
                // Get attachment from URI
                attachment = new AndroidUriToAttachmentUtility(contentUri, MainActivity.this).getAttachmentInstance();

            }else {
                attachment = null;
            }
        }
    }
</code>
</pre>


Isn't easy to implement, huh?

Lets enjoy fully working email sending functionality from your app without worry about performance, crashes, compatibity.

For your help, I have a sample app module, that has contains full implementation of how to use this library. You can check that module to learn in more depth.

For your quick jump, click here to see the sample activity file that implemented this following code: <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/app/src/main/java/dev/nurujjamanpollob/androidmailer/MainActivity.java">MainActivity.java</a>



## Interactive Demo

https://raw.githubusercontent.com/nurujjamanpollob/AndroidMailer/master/interactive-demo.mov

## Update 2.0 - Added String Decoding support though developer implementation

To make your code more readable and organized, and to better security support, I have added support to decode the all String parameter of <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/sender/MailSendWrapper.java">MailSendWrapper</a> class with a developer provided decoding logic flow.

To achieve this, You need to Create a new class which extends <b>SecurityPlugin</b> class, as example here : <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/app/src/main/java/dev/nurujjamanpollob/androidmailer/decoderunit/DemoDecoder.java">DemoDecoder.java</a> and Override getDecodedPassword() method to implement decoding logic flow.

<b>Code here: </b>

<pre>
<code> 
import android.util.Base64;

import dev.nurujjamanpollob.javamailer.security.SecurityPlugin;
import dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith;

/**
 * Class to demonstrate The String decoding strategy,
 * that is up to developer,
 * and developer is responsible for implement the decoding logic flow.
 * Highly customizable security plugin support for AndroidMailer library.
 * In this example, I gonna use android.util.Base64 to decode encoded String.
 * You can use any logic flow, and a guaranteed execution is confirmed.
 * Override getDecodedPassword() to implement decoding logic flow.
 */
public class DemoDecoder extends SecurityPlugin {

   private final String encodedPassword;

    /**
     * Constructor Parameter to accept encoded String
     * @param encodedPass the encoded String.
     */
    public DemoDecoder(String encodedPass) {
        // super call is requires for SecurityPlugin class
        super(encodedPass);
        // get encoded string from parameter and store in field for implement decoding logic flow.
        this.encodedPassword = encodedPass;

    }

    /**
     * TODO: Developer Implementation part
     * Override this method to customize the default implementation of super.getDecodedPassword()
     * By default, it usages java.util.Base64 package to decode encoded String.
     * If current device SDK is lower than Android Oreo, using super implementation can cause ClassNotFoundException.
     * @return String decoded from android.util.Base64.decode(encodedString, flag).
     */
    @Override
    public String getDecodedPassword() {

            // Use default flag to decode from android.util.Base64
            byte[] stringBytes = Base64.decode(encodedPassword, Base64.DEFAULT);
            // return decoded String
            return new String(stringBytes);
    }
}

</code>
</pre>



After that, you need to Create a new Class that extends <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/sender/MailSendWrapper.java">MailSendWrapper</a> class and in Constructor parameter, You need to annotate a Parameter with <b>@DecodeWith(decoder = DemoDecoder.class)</b> to have decoding support before send email to client, and you must pass this parameter to <b>super</b> constructor to get working support.
	
<b>The example here: </b>

<pre>
<code> 
import androidx.annotation.NonNull;

import dev.nurujjamanpollob.androidmailer.decoderunit.DemoDecoder;
import dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith;
import dev.nurujjamanpollob.javamailer.sender.MailSendWrapper;
import dev.nurujjamanpollob.javamailer.sender.Provider;

/**
 * A simple class to demonstrate how the @DecodeWith(decoder = Decoder.class) is works with MailSendWrapper class
 * and decode the target parameter value with Decoder class before send email to client to ensure better security.
 * All super parameter with String has support for @DecodeWith annotation
 */
public class MailWrapperSecure extends MailSendWrapper {
    /**
     * Constructor Parameter to Configure MailSendWrapper with basic parameters.
     * Supports decoding support of all String parameters that annotated with @DecodeWith
     *
     * @param fromAddress                  set the email from address field,
     *                                     for example if you are sending email by nurujjamanpollob@androiddev.io,
     *                                     you should pass this value as argument.
     * @param toAddress                    set the target address, the recipient address.
     * @param password                     the SMTP host or POP3 host account password
     * @param mailSubject                  set the email subject.
     * @param mailMessage                  sets the email body message.
     * @param serviceProviderConfiguration argument for Provider class, that contains SMTP or POP3 server configuration to send the email.
     *                                     For more information,
     * @see Provider class for more information
     */
    public MailWrapperSecure(
            @NonNull String fromAddress,
            @NonNull String toAddress,
            @DecodeWith(decoder = DemoDecoder.class) // decoding password from implementation of DemoDecoder class
            @NonNull String password,
            @NonNull String mailSubject,
            @NonNull String mailMessage,
            @NonNull Provider serviceProviderConfiguration
    ) throws Exception {
        super(fromAddress, toAddress, password, mailSubject, mailMessage, serviceProviderConfiguration);
    }
}

</code>
</pre>



In this example, I have used only <b>decoder</b> in password field only, anyway, you are free to use <b>@DecodeWith</b> in <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/app/src/main/java/dev/nurujjamanpollob/androidmailer/decoderunit/DemoDecoder.java">DemoDecoder.java</a> class, and it's all String parameter has been supported.


Note: <b>@DecodeWith</b> only works with <b>MailSendWrapper</b> class and it's all String parameter. other scope will be covered in <b>2.1</b> release.

	
For now, I never added any <b>encryption/decryption library</b>. Please use your preferred library to get support.

## @DecodeWith Coverage

<table> 
	
<tr>
<th> <h4> Class <a href="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/JavaMailer/src/main/java/dev/nurujjamanpollob/javamailer/sender/MailSendWrapper.java">MailSendWrapper</a></th>
<th> public MailSendWrapper(@NonNull String fromAddress, @NonNull String toAddress, @NonNull String password, @NonNull String mailSubject, @NonNull String mailMessage, @NonNull Provider serviceProviderConfiguration) throws Exception</th>	
</tr>
	
<tr>
<td> String fromAddress </td>
<td> ☑️ </td>
</tr>

<tr>
<td> String toAddress </td>
<td> ☑️ </td>
</tr>
<tr>
<td> String password </td>
<td> ☑️ </td>
</tr>
<tr>
<td> mailSubject </td>
<td> ☑️ </td>
</tr>
	
<tr>
<td> mailMessage </td>
<td> ☑ </td>
</tr>
	
<tr>
<td> serviceProviderConfiguration </td>
<td> ❌ </td>
</tr>

</table>

## Documentation

Will be covered in my free time. Anyway, some of documentation available inside project files. Feel free to check it.

## Message

If this library helps you, please give a star 🌟. Contributions are always welcome.
