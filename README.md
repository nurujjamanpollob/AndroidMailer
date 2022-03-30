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
	implementation 'com.github.nurujjamanpollob:AndroidMailer:1.0'
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

<video width="320" height="240" controls>
  <source src="https://github.com/nurujjamanpollob/AndroidMailer/blob/master/interactive-demo.mov?raw=true" type="video/mp4">
</video>
