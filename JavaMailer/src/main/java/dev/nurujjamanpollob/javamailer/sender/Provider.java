package dev.nurujjamanpollob.javamailer.sender;

public class Provider {

    private final String mailSMTPHostAddress;
    private final String mailSMTPPortAddress;
    private final String socketFactoryClassName;
    private final Boolean isUseAuth;
    private final String socketFactoryPortAddress;


    public Provider(String mailSMTPHostAddress, String mailSMTPPortAddress, String socketFactoryPortAddress, String javaSocketFactoryClassName, Boolean isUseAuth) {
        this.mailSMTPHostAddress = mailSMTPHostAddress;
        this.mailSMTPPortAddress = mailSMTPPortAddress;
        this.socketFactoryClassName = javaSocketFactoryClassName;
        this.isUseAuth = isUseAuth;
        this.socketFactoryPortAddress = socketFactoryPortAddress;


    }

    public String getSocketFactoryPortAddress() {
        return socketFactoryPortAddress;
    }

    public String getMailSMTPHostAddress() {
        return mailSMTPHostAddress;
    }

    public Boolean getUseAuth() {
        return isUseAuth;
    }

    public String getSocketFactoryClassName() {
        return socketFactoryClassName;
    }

    public String getMailSMTPPortAddress() {
        return mailSMTPPortAddress;
    }

}
