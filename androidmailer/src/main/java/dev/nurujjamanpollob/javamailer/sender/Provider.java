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

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.nurujjamanpollob.javamailer.security.SecurityDriver;
import dev.nurujjamanpollob.javamailer.security.utility.SecurityPluginUtility;

/**
 * Class to store and retrieve mail service based configurations
 */
public class Provider {

    private final String mailSMTPHostAddress;
    private final String mailSMTPPortAddress;
    private final String socketFactoryClassName;
    private final String isUseAuth;
    private final String socketFactoryPortAddress;
    private final Map<String, String> configurations = new HashMap<>();
    private String isUseTLS = "false";


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
    ) throws Exception {
        this.mailSMTPHostAddress = checkAndDecode(0,0, mailSMTPHostAddress);
        this.mailSMTPPortAddress = checkAndDecode(0, 1, mailSMTPPortAddress);
        this.socketFactoryClassName = checkAndDecode(0, 2, javaSocketFactoryClassName);
        this.isUseAuth = checkAndDecode(0,3, isUseAuth.toString());
        this.socketFactoryPortAddress = checkAndDecode(0, 4, socketFactoryPortAddress);

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
            Boolean isUseTLS) throws Exception {
        this.mailSMTPHostAddress = checkAndDecode(1, 0, mailSMTPHostAddress);
        this.mailSMTPPortAddress = checkAndDecode(1, 1, mailSMTPPortAddress);
        this.socketFactoryClassName = checkAndDecode(1, 2, javaSocketFactoryClassName);
        this.isUseAuth = checkAndDecode(1, 3, isUseAuth.toString());
        this.socketFactoryPortAddress = checkAndDecode(1, 4, socketFactoryPortAddress);
        this.isUseTLS = checkAndDecode(1, 5, isUseTLS.toString());

        // Put all configurations
        configurations.put("mail.smtp.host", getMailSMTPHostAddress());
        configurations.put("mail.smtp.socketFactory.port", getSocketFactoryPortAddress());
        configurations.put("mail.smtp.socketFactory.class", getSocketFactoryClassName());
        configurations.put("mail.smtp.auth", String.valueOf(getUseAuth()));
        configurations.put("mail.smtp.port", getMailSMTPPortAddress());

    }



    /**
     * Suppress default constructor for non-instantiable for no args constructor
     */
    private Provider() {
        this.mailSMTPHostAddress = null;
        this.mailSMTPPortAddress = null;
        this.socketFactoryClassName = null;
        this.isUseAuth = String.valueOf(false);
        this.socketFactoryPortAddress = null;
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
        return isUseAuth.equals("true");
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
        return isUseTLS.equals("true");
    }

    /**
     * Method to get configuration set map for configure library plugin
     * @return Key value pair in array.
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

    /**
     * Method to check for if a parameter is annotated with {@link dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith}
     * If so, use the appropriate decoder library and claas to decode the value.
     * @throws Exception if the decoder library found errors during decoding, for detailed information, check the exception message.
     */

    private String checkAndDecode(int constructorIndex, int parameterIndex, String baseValue) throws Exception {

        // Create instance of SecurityPluginUtility class
        SecurityPluginUtility securityPluginUtility = new SecurityPluginUtility(constructorIndex, super.getClass(), parameterIndex);

        // Check if annotation is present
        if(securityPluginUtility.isConstructorParameterIsAnnotatedWithAnnotationClass()){

            // Return the decoded value
            return new SecurityDriver(baseValue, securityPluginUtility.getDecoderClassForSecurityDriver()).getDecodedString();
        }

        // It seems the field is not annotated for decoding, so returning the base value
        return baseValue;
    }


    @NonNull
    @Override
    public String toString() {
        return "Provider{" +
                "mailSMTPHostAddress='" + mailSMTPHostAddress + '\'' +
                ", mailSMTPPortAddress='" + mailSMTPPortAddress + '\'' +
                ", socketFactoryClassName='" + socketFactoryClassName + '\'' +
                ", isUseAuth='" + isUseAuth + '\'' +
                ", socketFactoryPortAddress='" + socketFactoryPortAddress + '\'' +
                ", configurations=" + configurations +
                ", isUseTLS='" + isUseTLS + '\'' +
                '}';
    }
}
