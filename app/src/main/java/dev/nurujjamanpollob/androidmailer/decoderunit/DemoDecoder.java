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

package dev.nurujjamanpollob.androidmailer.decoderunit;


import android.util.Base64;

import dev.nurujjamanpollob.javamailer.security.SecurityPlugin;
import dev.nurujjamanpollob.javamailer.security.utility.SecurityDriverException;

/**
 * Class to demonstrate The String decoding strategy,
 * that is up to developer,
 * and developer is responsible for implement the decoding logic flow.
 * Highly customizable security plugin support for AndroidMailer library.
 * In this example, I gonna use android.util.Base64 to decode encoded String.
 * You can use any logic flow, and a guaranteed execution is confirmed.
 * Override {@link SecurityPlugin#getDecodedString()} to implement decoding logic flow.
 */
public class DemoDecoder extends SecurityPlugin {

   private final String encodedPassword;

    /**
     * Parameter to accept encoded String
     * @param encodedPass the encoded String.
     */
    public DemoDecoder(String encodedPass) throws SecurityDriverException {
        // super call is requires for SecurityPlugin class
        super(encodedPass);
        // get encoded string from parameter and store in field for implement decoding logic flow.
        this.encodedPassword = encodedPass;

    }

    /**
     * TODO: Developer Implementation part
     * Override this method to customize the default implementation of {@link SecurityPlugin#getDecodedString()}
     * By default, it usages java.util.Base64 package to decode encoded String.
     * If current device SDK is lower than Android Oreo, using super implementation can cause {@link ClassNotFoundException}.
     * @return String decoded from {@link android.util.Base64#decode(String, int)}.
     */
    @Override
    public String getDecodedString() {

        // Use default flag to decode from android.util.Base64
        byte[] stringBytes = Base64.decode(encodedPassword, Base64.DEFAULT);
        // return decoded String
        return new String(stringBytes);
    }
}
