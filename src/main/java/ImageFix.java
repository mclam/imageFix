import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageFix {
    public static void main(String[] args) throws IOException {
        BufferedImage srcImage = ImageIO.read(new File("/Users/mclam/git/imageFix/src/main/resources/00023.jpeg"));

        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        System.out.println(width + "x" + height);

        var parts = IntStream.range(0, 20).boxed().collect(Collectors.toList());//Arrays.asList(2, 1, 3);
        Collections.reverse(parts);
        System.out.println("parts: " + parts);

        // decompose source image equally
        var images = new ArrayList<BufferedImage>();
        for (var i = 0; i < parts.size(); i++) {
            BufferedImage subImage = srcImage.getSubimage(0, height * i / parts.size(), width, height / parts.size());
//            ImageIO.write(subImage, "jpg", new File("00023-" + i + ".jpg"));
            images.add(subImage);
        }

        // combine decomposed images by order in parts
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = combined.getGraphics();
        for(var i = 0; i < parts.size(); i++) {
            graphics.drawImage(images.get(parts.get(i)), 0, height * i / parts.size(), null);
        }
        graphics.dispose();

        // output combined image
        File outputFile = new File("00023.jpg");
        ImageIO.write(combined, "jpg", outputFile);

//        OutputStream fileOutputStream = new FileOutputStream(outputFile);
//        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
//        imageWriter.setOutput(ImageIO.createImageOutputStream(fileOutputStream));
//        imageWriter.write(null, new IIOImage(combined, null, null), null);
    }
}
