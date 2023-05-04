package com.acme;

import com.sun.image.codec.jpeg.*;

public class ImageEncoderTest {

    public static void main(String[] args) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        JPEGImageEncoder imageEncoder = JPEGCodec.createJPEGEncoder(outputStream);
        imageEncoder.encode(bufferedImage);


}