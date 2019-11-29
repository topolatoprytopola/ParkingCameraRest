package Camera;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Capture {
    public Mat capture(Integer id)
    {
        VideoCapture camera;
        Mat frame = new Mat();
        switch (id) {
            case 1:
                camera = new VideoCapture("http://live.uci.agh.edu.pl/video/stream3.cgi");
                break;
            case 2:
                camera = new VideoCapture("http://live.uci.agh.edu.pl/video/stream1.cgi");
                break;
            default:
                return null;
        }
        camera.read(frame);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
        return frame;
    }
}
