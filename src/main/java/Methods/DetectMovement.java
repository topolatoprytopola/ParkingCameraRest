package Methods;

import Camera.location;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractor;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
public class DetectMovement implements Runnable {
    int restrictionxleft;
    int restrictionxright;
    int restrictionytop;
    int restrictionybottom;
    public DetectMovement(int restrictionxleft,int restrictionxright,int restrictionytop,int restrictionybottom) {
        this.restrictionxleft = restrictionxleft;
        this.restrictionxright = restrictionxright;
        this.restrictionytop = restrictionytop;
        this.restrictionybottom = restrictionybottom;
    }
    public void run() {
        int line = 260;
        boolean ishorizontal = true;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int n = 0;
        List<location> locations = new ArrayList<>();
        List<location> tmplocations = new ArrayList<>();
        VideoCapture camera = new VideoCapture("http://live.uci.agh.edu.pl/video/stream3.cgi");
        if (!camera.isOpened()) {
            System.out.println("Error! Camera can't be opened!");
            return;
        }
        int j;
        int how_many = 40;
        Mat frame;
        BackgroundSubtractor backSub;
        Mat hierarchy = new Mat();
        int threshold = 600;
        int kernelSize = 11;
        while (true) {
            n++;
            j = 0;
            backSub = Video.createBackgroundSubtractorKNN();
            List<MatOfPoint> contours = new ArrayList<>();
            do {
                frame = new Mat();
                camera.read(frame);
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
                backSub.apply(frame, frame);
                j++;
            } while (j <= 26);
            Mat element2 = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(1, 1));
            Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_ERODE, element2);
            Imgproc.medianBlur(frame, frame, 3);
            Imgproc.morphologyEx(frame, frame, Imgproc.MORPH_DILATE, element2);
            Imgproc.Canny(frame, frame, threshold, threshold * 2);
            Mat element = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(2 * kernelSize + 1, 2 * kernelSize + 1),
                    new Point(kernelSize, kernelSize));
            Imgproc.dilate(frame, frame, element);
            Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
            Rect[] boundRect = new Rect[contours.size()];
            Point[] centers = new Point[contours.size()];
            float[][] radius = new float[contours.size()][1];
            for (int i = 0; i < contours.size(); i++) {
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
                boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
                centers[i] = new Point();
                Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
            }
            List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
            for (MatOfPoint2f poly : contoursPoly) {
                contoursPolyList.add(new MatOfPoint(poly.toArray()));
            }
            for (int i = 0; i < contours.size(); i++) {
                double centerx = (boundRect[i].tl().x + boundRect[i].br().x) / 2;
                double centery = (boundRect[i].tl().y + boundRect[i].br().y) / 2;
                double size = boundRect[i].width * boundRect[i].height;
                if (size > 7000) {
                    if (centerx > restrictionxleft && centerx < restrictionxright && centery < restrictionybottom && centery > restrictionytop) {
                        tmplocations.add(new location(centerx, centery));
                    }
                }
            }
            for (location lc : locations) {
                Iterator i = tmplocations.iterator();
                location tmplc;
                while (i.hasNext()) {
                    tmplc = (location) i.next();
                    if (Math.abs(lc.getY() - tmplc.getY()) < 80 && Math.abs(lc.getX() - tmplc.getX()) < 80) {
                        if (ishorizontal) {
                            if (lc.getY() > line && tmplc.getY() < line) {
                                if (how_many > 0) {
                                    how_many--;
                                }
                                i.remove();
                            } else if (lc.getY() < line && tmplc.getY() > line) {
                                how_many++;
                                i.remove();
                            }
                        } else {
                            if (lc.getX() > line && tmplc.getX() < line) {
                                if (how_many > 0) {
                                    how_many--;
                                }
                                i.remove();
                            } else if (lc.getX() < line && tmplc.getX() > line) {
                                how_many++;
                                i.remove();
                            }
                        }
                    }
                }
            }
            locations.clear();
            for (location tmplc : tmplocations
            ) {
                locations.add(tmplc);
            }
            tmplocations.clear();
            System.out.println(how_many);
        }
    }
}
