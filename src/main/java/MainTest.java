import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainTest implements Runnable {
    final int INTERVAL = 500; // ms
    CanvasFrame canvas = new CanvasFrame("Web Cam");

    public MainTest() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MainTest gs = new MainTest();
        Thread th = new Thread(gs);
        th.start();
    }

    public void run() {
        FrameGrabber grabber = new VideoInputFrameGrabber(0);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage img;
        try {
            grabber.start();
            while (true) {
                Frame frame = grabber.grab();
                img = converter.convert(frame);
                canvas.showImage(converter.convert(img));

                if (img != null) {
                    LuminanceSource source = new BufferedImageLuminanceSource(img);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);

                        if (result != null) {
                            System.out.println(result.getText());
                        }
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        System.out.println(sw.toString());
                    }
                }

                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

