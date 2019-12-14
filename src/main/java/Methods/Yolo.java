package Methods;

import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Yolo implements Runnable {
    int freespaces;

    public int getFreespaces() {
        return freespaces;
    }

    public void setFreespaces(int freespaces) {
        this.freespaces = freespaces;
    }

    private static List<String> getOutputNames(Net net) {
        List<String> names = new ArrayList<>();

        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layersNames = net.getLayerNames();

        outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));//unfold and create R-CNN layers from the loaded YOLO model//
        return names;
    }
    public Yolo(int freespaces)
    {
        this.freespaces = freespaces;
    }
    @Override
    public void run() {
        int cars;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String modelWeights = "D:\\yolov3.weights";
        String modelConfiguration = "D:\\yolov3.cfg.txt";
        VideoCapture cap = new VideoCapture("http://live.uci.agh.edu.pl/video/stream3.cgi");
        Mat frame = new Mat();
        JFrame jframe = new JFrame("Video");
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setSize(600, 600);
        jframe.setVisible(true);
        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);
        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = getOutputNames(net);
        while (true) {
            if (cap.read(frame)) {
                cars=0;
                Size sz = new Size(288,288);
                Mat blob = Dnn.blobFromImage(frame, 0.00392, sz, new Scalar(0), true, false);
                net.setInput(blob);
                net.forward(result, outBlobNames);
                float confThreshold = 0.1f;
                for (int i = 0; i < result.size(); ++i)
                {
                    Mat level = result.get(i);
                    for (int j = 0; j < level.rows(); ++j)
                    {
                        Mat row = level.row(j);
                        Mat scores = row.colRange(5, level.cols());
                        Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                        float confidence = (float)mm.maxVal;
                        Point classIdPoint = mm.maxLoc;
                        if (confidence > confThreshold)
                        {
                            if((int)classIdPoint.x == 2 || (int)classIdPoint.x == 5 || (int)classIdPoint.x == 7   ) {   //check if car,bus or truck
                                cars++;
                            }
                        }
                    }
                }
                this.freespaces = this.freespaces - cars;
            }
        }
    }
}
