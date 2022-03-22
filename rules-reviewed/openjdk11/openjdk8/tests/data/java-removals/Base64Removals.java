package com.test.project;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Removals {
    public static void main(String[] args) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        BASE64Decoder base64Decoder = new BASE64Decoder();

        try {
            base64Decoder.decodeBuffer("inputString");
            base64Encoder.encodeBuffer(new byte[]{'a', 'b'});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
