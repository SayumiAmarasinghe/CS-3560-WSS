package wss.gui.util;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

public final class ImageLoader {
    private ImageLoader() {
    }

    // OS-independent method to load images 
    public static ImageIcon loadIcon(String fileName) {
        return loadIcon(fileName, -1, -1);
    }

    // OS-independent method to load and scale images at specified pixels
    public static ImageIcon loadIcon(String fileName, int maxWidth, int maxHeight) {
        String resourcePath = "/wss/gui/assets/" + fileName;
        URL resource = ImageLoader.class.getResource(resourcePath);
        if (resource != null) {
            return scaleIcon(new ImageIcon(resource), maxWidth, maxHeight);
        }

        File sourceFile = new File("src/wss/gui/assets/" + fileName);
        if (sourceFile.exists()) {
            return scaleIcon(new ImageIcon(sourceFile.getPath()), maxWidth, maxHeight);
        }

        throw new IllegalArgumentException("Image not found: " + fileName);
    }

    private static ImageIcon scaleIcon(ImageIcon icon, int maxWidth, int maxHeight) {
        if (maxWidth <= 0 || maxHeight <= 0) {
            return icon;
        }

        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        if (originalWidth <= 0 || originalHeight <= 0) {
            return icon;
        }

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double scaleRatio = Math.min(widthRatio, heightRatio);

        if (scaleRatio >= 1.0) {
            return icon;
        }

        int scaledWidth = Math.max(1, (int) Math.round(originalWidth * scaleRatio));
        int scaledHeight = Math.max(1, (int) Math.round(originalHeight * scaleRatio));
        Image scaledImage = icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}