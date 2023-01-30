import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ImageFix {

    static void processPath(File path) throws IOException {
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null && files.length > 0) {
                for (File file : Arrays.stream(files).filter(
                        file -> !file.getName().endsWith(".jpg") && !file.getName().startsWith(".")
                ).collect(Collectors.toList())) {
                    processPath(file);
                }
            }
        } else {
            System.out.println("fix " + path.getCanonicalPath());
            BufferedImage srcImage = ImageIO.read(path);

            int width = srcImage.getWidth();
            int height = srcImage.getHeight();

//            System.out.println(width + "x" + height);

            var parts = IntStream.range(0, Integer.parseInt(path.getParentFile().getName())).boxed().collect(Collectors.toList());//Arrays.asList(2, 1, 3);
            Collections.reverse(parts);
//            System.out.println("parts: " + parts);

            int step = (height - height % parts.size()) / parts.size();

            // decompose source image equally
            var images = new ArrayList<BufferedImage>();
            for (var i = 0; i < parts.size(); i++) {
                int h = step + (i == parts.size() - 1 ? height % parts.size() : 0);
                BufferedImage subImage = srcImage.getSubimage(0, step * i, width, h);
//                ImageIO.write(subImage, "jpg", new File(path.getPath().replaceAll("\\.jpeg", "-" + i + ".jpg")));
                images.add(subImage);
            }

            // combine decomposed images by order in parts
            BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = combined.getGraphics();
            for(var i = 0; i < parts.size(); i++) {
                int y = step * i + (i > 0 ? height % parts.size() : 0);
                graphics.drawImage(images.get(parts.get(i)), 0, y, null);
            }
            graphics.dispose();

            // output combined image
            File outputFile = new File("src/main/resources/images/fix/" + path.getName().replaceAll("\\.jpeg", ".jpg"));
            ImageIO.write(combined, "jpg", outputFile);
        }
    }

    public static void main(String[] args) throws IOException {
        String dir = args.length > 0 ? args[0] : "src/main/resources/images";
        processPath(new File(dir));
    }
}
