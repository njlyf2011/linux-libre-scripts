package com.sun.image.codec.jpeg;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.IOException;

public interface JPEGImageEncoder {
    public JPEGEncodeParam getDefaultJPEGEncodeParam(BufferedImage bi);
    public void encode(BufferedImage bi, JPEGEncodeParam param)
	throws IOException;
    public void encode(BufferedImage bi)
	throws IOException;
    public OutputStream getOutputStream();
}
