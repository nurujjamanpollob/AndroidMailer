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
