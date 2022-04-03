package dev.nurujjamanpollob.androidmailer.decoderunit;


import android.util.Base64;

import dev.nurujjamanpollob.javamailer.security.SecurityPlugin;
import dev.nurujjamanpollob.javamailer.security.annotation.DecodeWith;

/**
 * Class to demonstrate The String decoding strategy,
 * that is up to developer,
 * and developer is responsible for implement the decoding logic flow.
 * Highly customizable security plugin support for AndroidMailer library.
 * In this example, I gonna use android.util.Base64 to decode encoded String.
 * You can use any logic flow, and a guaranteed execution is confirmed.
 * Override getDecodedPassword() to implement decoding logic flow.
 */
public class DemoDecoder extends SecurityPlugin {

   private final String encodedPassword;

    /**
     * Constructor Parameter to accept encoded String
     * @param encodedPass the encoded String.
     */
    public DemoDecoder(String encodedPass) {
        // super call is requires for SecurityPlugin class
        super(encodedPass);
        // get encoded string from parameter and store in field for implement decoding logic flow.
        this.encodedPassword = encodedPass;

    }

    /**
     * TODO: Developer Implementation part
     * Override this method to customize the default implementation of super.getDecodedPassword()
     * By default, it usages java.util.Base64 package to decode encoded String.
     * If current device SDK is lower than Android Oreo, using super implementation can cause ClassNotFoundException.
     * @return String decoded from android.util.Base64.decode(encodedString, flag).
     */
    @Override
    public String getDecodedPassword() {

            // Use default flag to decode from android.util.Base64
            byte[] stringBytes = Base64.decode(encodedPassword, Base64.DEFAULT);
            // return decoded String
            return new String(stringBytes);
    }
}
