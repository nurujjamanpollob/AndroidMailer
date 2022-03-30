package dev.nurujjamanpollob.javamailer.sender;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class to store and retrieve mail service based configurations
 */
public class Provider {

    private final String mailSMTPHostAddress;
    private final String mailSMTPPortAddress;
    private final String socketFactoryClassName;
    private final Boolean isUseAuth;
    private final String socketFactoryPortAddress;
    private final Map<String, String> configurations = new HashMap<>();
    private Boolean isUseTLS = false;


    /**
     * Constructor parameter to store basic service configuration to authenticate with email server without TLS
     * @param mailSMTPHostAddress SMTP host address
     * @param mailSMTPPortAddress SMTP HOST port address
     * @param socketFactoryPortAddress socket factory port address, should be same as SMTP host port address, in some cases this may be different.
     * @param javaSocketFactoryClassName Java socket factory class name, for your quick configuration, you can use:
     *                                  <pre><code>Providers.getSecureSocketFactoryName();</code></pre>
     * @param isUseAuth use authorization, if requires
     *                  Note: If you add TLS based configuration without passing isUseTls true, it should add all tls based configuration.
     */
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

    /**
     * Constructor parameter to store basic service configuration to authenticate with email server with TLS
     * @param mailSMTPHostAddress SMTP host address
     * @param mailSMTPPortAddress SMTP HOST port address
     * @param socketFactoryPortAddress socket factory port address, should be same as SMTP host port address, in some cases this may be different.
     * @param javaSocketFactoryClassName Java socket factory class name, for your quick configuration, you can use:
     *                                  <pre><code>Providers.getSecureSocketFactoryName();</code></pre>
     * @param isUseAuth use authorization, if requires
     * @param isUseTLS to determine if you are intended to use TLS, and the advantage is if you do not pass TLS based configurations,
     *                 the library will use some basic TLS parameter to configure this library.
     */
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

    /**
     * @return passed socket factory address
     */
    public String getSocketFactoryPortAddress() {
        return socketFactoryPortAddress;
    }

    /**
     * @return passed SMTP Host Address
     */
    public String getMailSMTPHostAddress() {
        return mailSMTPHostAddress;
    }

    /**
     * @return passed isUseAuth parameter
     */
    public Boolean getUseAuth() {
        return isUseAuth;
    }

    /**
     * @return passed java socket factory class name
     */
    public String getSocketFactoryClassName() {
        return socketFactoryClassName;
    }

    /**
     * @return passed SMTP Port Address
     */
    public String getMailSMTPPortAddress() {
        return mailSMTPPortAddress;
    }

    /**
     * Method to put a key value pair as mail service configuration.
     * Can be used to override existing service configuration, if the library generated configuration is problematic
     * @param propertyKey service configuration key
     * @param propertyValue service configuration value
     */
    public void putConfiguration(String propertyKey, String propertyValue){
        configurations.put(propertyKey, propertyValue);
    }

    /**
     * @return passed isUseTls parameter, if no parameter is passed via constructor, the default value (false) is returned.
     */
    public Boolean getIsUseTLS() {
        return isUseTLS;
    }

    /**
     * Method to get configuration set map for configure library plugin
     * @return Key value pain in array.
     */
    public Map<String, String> getAllConfigurations(){

        return configurations;
    }

    /**
     * Method to get value from a given key in the configuration Map
     * @param propertyKey the property key to find a value by
     * @return null if the key has containing nil value or key is not exists in the configuration key,
     * else the key's value is returned.
     */
    public String getValueForAConfiguration(String propertyKey){

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
