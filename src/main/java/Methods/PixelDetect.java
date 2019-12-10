package Methods;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.opencv.core.CvType.CV_8UC3;

public class PixelDetect implements Runnable {
    List<Point> testpoints;
    List<List<Point>> listofpoints;
    public PixelDetect(List<Point> testpoints,List<List<Point>> listofpoints)
    {
        this.testpoints = testpoints;
        this.listofpoints = listofpoints;
    }
public void run() {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    VideoCapture camera = new VideoCapture("http://live.uci.agh.edu.pl/video/stream3.cgi");
    if (!camera.isOpened()) {
        System.out.println("Error! Camera can't be opened!");
        return;
    }
    Mat frame = new Mat();
    MatOfPoint mop;
    int freespaces;
    List<Mat> masks;
    while(camera.read(frame))
    {
        Imgproc.GaussianBlur(frame, frame, new Size(9,9),0);
        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_BGR2HSV);
        masks = new ArrayList<>();
        freespaces = 0;
        for (List<Point> fieldpoints: listofpoints
        ){
            Mat mask = new Mat(frame.size(),CV_8UC3);
            mop = new MatOfPoint();
            mop.fromList(fieldpoints);
            Imgproc.fillConvexPoly(mask, mop, new Scalar(255, 255, 255));
            masks.add(mask);
        }
        double[] numArrayh = new double[testpoints.size()*10*10];
        double[] numArrays = new double[testpoints.size()*10*10];
        double[] numArrayv = new double[testpoints.size()*10*10];
        int k = 0;
        for (Point point:testpoints)
        {
            for(int a = (int)point.y - 5; a<(int)point.y+5; a++) {
                for(int b = (int)point.x - 5; b<(int)point.x+5; b++) {
                    double[] test = frame.get(a,b);
                    numArrayh[k] = frame.get(a, b)[0];
                    numArrays[k] = frame.get(a, b)[1];
                    numArrayv[k] = frame.get(a, b)[2];
                    k++;
                }
            }
        }
        Arrays.sort(numArrayh);
        double medianh;
        if (numArrayh.length % 2 == 0)
            medianh = (numArrayh[numArrayh.length/2] + numArrayh[numArrayh.length/2 - 1])/2;
        else
            medianh = numArrayh[numArrayh.length/2];
        double medians;
        if (numArrays.length % 2 == 0)
            medians = (numArrays[numArrays.length/2] + numArrays[numArrays.length/2 - 1])/2;
        else
            medians = numArrays[numArrays.length/2];
        double medianv;
        if (numArrays.length % 2 == 0)
            medianv = (numArrayv[numArrayv.length/2] + numArrayv[numArrayv.length/2 - 1])/2;
        else
            medianv = numArrayv[numArrayv.length/2];
        Core.inRange(frame, new Scalar(medianh - 25, medians - 30, medianv - 30), new Scalar(medianh + 25, medians + 30, medianv + 30), frame);
        for (Mat mask:masks
        ) {
            double test1 = 0;
            double test2 = 0;
            for(int i = 0 ; i < mask.height() ; i++) {
                for (int j = 0; j < mask.width(); j++) {
                    if (mask.get(i,j)[0] == 255.0)
                    {
                        double[] testd = frame.get(i,j);
                        if(frame.get(i,j)[0] == 0) {
                            test2++;
                        }
                        test1++;
                    }
                }
            }
            double test = test2/test1*100;
            if(test < 70)
            {
                freespaces++;
            }
        }
        System.out.println(freespaces);
    }
        }
}
