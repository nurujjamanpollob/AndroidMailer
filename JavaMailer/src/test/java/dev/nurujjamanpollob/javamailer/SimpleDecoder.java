package dev.nurujjamanpollob.javamailer;

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
public class SimpleDecoder extends SecurityPlugin {

    private final String encodedPassword;


    public SimpleDecoder(String encodedString) throws SecurityDriverException {
        super(encodedString);
        this.encodedPassword = encodedString;
    }

    /**
     * TODO: Developer Implementation part
     * Override this method to customize the default implementation of {@link SecurityPlugin#getDecodedString()}
     * By default, it usages java.util.Base64 package to decode encoded String.
     * If current device SDK is lower than Android Oreo, using super implementation can cause {@link ClassNotFoundException}.
     *
     * This example overrides the default implementation of {@link SecurityPlugin#getDecodedString()}, and uses android.util.Base64 to decode encoded String.
     *
     * @return String decoded from {@link android.util.Base64#decode(String, int)}.
     */
    @Override
    public String getDecodedString() {
        return new String(Base64.decode(encodedPassword, Base64.DEFAULT));
    }
}
