package dev.nurujjamanpollob.javamailer.sender;

public class Providers {


    /**
     * Method to get ready to go configuration for GMAIl email sender
     * @return the Provider instance for Gmail SMTP Sender
     */

    @Deprecated
    public static Provider useGmailProvider(){

        String gmailHostName = "smtp.gmail.com";
        String gmailSmtpPort = "25";
        String socketFactoryPort = "25";
        String socketFactoryClassName = "javax.net.ssl.SSLSocketFactory";

        return new Provider(gmailHostName,gmailSmtpPort,socketFactoryPort,socketFactoryClassName, true);
    }

    public static String getSecureSocketFactoryName(){

        return "javax.net.ssl.SSLSocketFactory";
    }
}
