package utils;

import javax.swing.*;
import java.awt.*;

public class ImageUtils{
    public static Image createImageIconFromName(String fileName) {
        return new ImageIcon(ImageUtils.class.getResource("/images/" + fileName + ".png")).getImage();
    }
}