import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

public class PWCallback implements CallbackHandler {

    private static final byte[] key = {
            (byte)0x31, (byte)0xfd, (byte)0xcb, (byte)0xda,
            (byte)0xfb, (byte)0xcd, (byte)0x6b, (byte)0xa8,
            (byte)0xe6, (byte)0x19, (byte)0xa7, (byte)0xbf,
            (byte)0x51, (byte)0xf7, (byte)0xc7, (byte)0x3e,
            (byte)0x80, (byte)0xae, (byte)0x98, (byte)0x51,
            (byte)0xc8, (byte)0x51, (byte)0x34, (byte)0x04,
    };

    public void handle(Callback[] callbacks)
            throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
        /*
         * here call a function/method to lookup the password for
         * the given identifier (e.g. a user name or keystore alias)
         * e.g.: pc.setPassword(passStore.getPassword(pc.getIdentfifier))
         * for testing we supply a fixed name/fixed key here.
         */
                if (pc.getUsage() == WSPasswordCallback.KEY_NAME) {
                    pc.setKey(key);
                }
                else {
                    pc.setPassword("security");
                }
            } else {
                throw new UnsupportedCallbackException(
                        callbacks[i], "Unrecognized Callback");
            }
        }
    }
}