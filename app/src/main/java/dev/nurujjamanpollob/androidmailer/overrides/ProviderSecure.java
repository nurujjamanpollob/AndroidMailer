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
 *
 *
 */

package dev.nurujjamanpollob.androidmailer.overrides;

import dev.nurujjamanpollob.androidmailer.decoderunit.DemoDecoder;
import dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith;
import dev.nurujjamanpollob.javamailer.sender.Provider;

/**
 * This class Inherits {@link Provider} class and overrides constructors to add decoder support,
 * though a custom decoder class, named {@link DemoDecoder}.
 *
 * if you want to exclude a parameter from decoder, you should not mark this parameter with @DecodeWith annotation.
 *
 * This example usages {@link DemoDecoder} as example, and the decoder class is responsible for decoding the encoded String,
 * you can also create your own decoder class, and can use different decoder for per parameter.
 *
 * For more information, please refer to {@link Provider} {@link dev.nurujjamanpollob.javamailer.security.SecurityPlugin} class documentation.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ProviderSecure extends Provider {


    public ProviderSecure(
            @DecodeWith(decoder = DemoDecoder.class) String mailSMTPHostAddress,
            @DecodeWith(decoder = DemoDecoder.class) String mailSMTPPortAddress,
            @DecodeWith(decoder = DemoDecoder.class) String socketFactoryPortAddress,
            @DecodeWith(decoder = DemoDecoder.class) String javaSocketFactoryClassName,
            @DecodeWith(decoder = DemoDecoder.class) Boolean isUseAuth) throws Exception{

        super(mailSMTPHostAddress, mailSMTPPortAddress, socketFactoryPortAddress, javaSocketFactoryClassName, isUseAuth);
    }

    public ProviderSecure(
            @DecodeWith(decoder = DemoDecoder.class) String mailSMTPHostAddress,
            @DecodeWith(decoder = DemoDecoder.class) String mailSMTPPortAddress,
            @DecodeWith(decoder = DemoDecoder.class) String socketFactoryPortAddress,
            @DecodeWith(decoder = DemoDecoder.class) String javaSocketFactoryClassName,
            @DecodeWith(decoder = DemoDecoder.class) Boolean isUseAuth,
            @DecodeWith(decoder = DemoDecoder.class) Boolean isUseTLS) throws Exception{
        super(mailSMTPHostAddress, mailSMTPPortAddress, socketFactoryPortAddress, javaSocketFactoryClassName, isUseAuth, isUseTLS);
    }
}
