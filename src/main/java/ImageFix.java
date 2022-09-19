import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFix {
    public static void main(String[] args) throws IOException {
        BufferedImage srcImage = ImageIO.read(new File("/Users/mclam/git/imageFix/src/main/resources/00023.jpeg"));

        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        System.out.println(width + "x" + height);

        var parts = Arrays.asList(2, 1, 3);
        var images = new ArrayList<BufferedImage>();
        for (var i = 0; i < parts.size(); i++) {
            images.add(srcImage.getSubimage(0, height * i / parts.size(), width, height / parts.size()));
        }
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = combined.getGraphics();
        for(var i = 0; i < parts.size(); i++) {
            graphics.drawImage(images.get(parts.get(i) - 1), 0, height * i / parts.size(), null);
        }
        graphics.dispose();

        ImageIO.write(combined, "jpg", new File("/Users/mclam/git/imageFix/src/main/resources/00023.jpg"));
    }
}
