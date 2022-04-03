package dev.nurujjamanpollob.androidmailer.overrides;

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
            @DecodeWith(decoder = DemoDecoder.class)
            @NonNull String password,
            @NonNull String mailSubject,
            @NonNull String mailMessage,
            @NonNull Provider serviceProviderConfiguration
    ) throws Exception {
        super(fromAddress, toAddress, password, mailSubject, mailMessage, serviceProviderConfiguration);
    }
}
