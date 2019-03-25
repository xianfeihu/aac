package com.yz.aac.common.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static boolean isImage(InputStream inputStream) {
        boolean result = false;
        if (null != inputStream) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(inputStream);
            } catch (IOException ignored) {
            }
            result = (img != null && img.getWidth() > 0 && img.getHeight() > 0);
        }
        return result;
    }

}
