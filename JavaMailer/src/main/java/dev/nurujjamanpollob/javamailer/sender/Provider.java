package dev.nurujjamanpollob.javamailer.sender;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Provider {

    private final String mailSMTPHostAddress;
    private final String mailSMTPPortAddress;
    private final String socketFactoryClassName;
    private final Boolean isUseAuth;
    private final String socketFactoryPortAddress;
    private final Map<String, String> configurations = new HashMap<>();
    private Boolean isUseTLS = false;


    public Provider(
            String mailSMTPHostAddress,
            String mailSMTPPortAddress,
            String socketFactoryPortAddress,
            String javaSocketFactoryClassName,
            Boolean isUseAuth
    ) {
        this.mailSMTPHostAddress = mailSMTPHostAddress;
        this.mailSMTPPortAddress = mailSMTPPortAddress;
        this.socketFactoryClassName = javaSocketFactoryClassName;
        this.isUseAuth = isUseAuth;
        this.socketFactoryPortAddress = socketFactoryPortAddress;

        // Put all configurations
        configurations.put("mail.smtp.host", getMailSMTPHostAddress());
        configurations.put("mail.smtp.socketFactory.port", getSocketFactoryPortAddress());
        configurations.put("mail.smtp.socketFactory.class", getSocketFactoryClassName());
        configurations.put("mail.smtp.auth", String.valueOf(getUseAuth()));
        configurations.put("mail.smtp.port", getMailSMTPPortAddress());

    }

    public Provider(
            String mailSMTPHostAddress,
            String mailSMTPPortAddress,
            String socketFactoryPortAddress,
            String javaSocketFactoryClassName,
            Boolean isUseAuth,
            Boolean isUseTLS) {
        this.mailSMTPHostAddress = mailSMTPHostAddress;
        this.mailSMTPPortAddress = mailSMTPPortAddress;
        this.socketFactoryClassName = javaSocketFactoryClassName;
        this.isUseAuth = isUseAuth;
        this.socketFactoryPortAddress = socketFactoryPortAddress;
        this.isUseTLS = isUseTLS;

        // Put all configurations
        configurations.put("mail.smtp.host", getMailSMTPHostAddress());
        configurations.put("mail.smtp.socketFactory.port", getSocketFactoryPortAddress());
        configurations.put("mail.smtp.socketFactory.class", getSocketFactoryClassName());
        configurations.put("mail.smtp.auth", String.valueOf(getUseAuth()));
        configurations.put("mail.smtp.port", getMailSMTPPortAddress());

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

    public void putConfiguration(String propertyKey, String propertyValue){
        configurations.put(propertyKey, propertyValue);
    }

    public Boolean getIsUseTLS() {
        return isUseTLS;
    }

    public Map<String, String> getAllConfigurations(){

        return configurations;
    }

    public Object getValueForAConfiguration(String propertyKey){

        for (Map.Entry<String, String> entry : configurations.entrySet()){
            String key = entry.getKey();
            if(Objects.equals(propertyKey, key)){
                return entry.getValue();
            }
        }

        // The requested key is not exists so return null
        return null;
    }

}
