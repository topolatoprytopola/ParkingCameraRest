package Main;

import Camera.Capture;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.opencv.core.Core;
import org.opencv.core.Mat;

@RestController
public class MainController {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    @GetMapping("/")
    @ResponseBody
    public String index(@RequestParam String id) {
        Integer intid;
        try {
             intid = Integer.valueOf(id);
        } catch(NumberFormatException e) {
            return "Invalid input!";
        }

        Mat frame = new Capture().capture(intid);
        if (frame == null)
        {
            return "Invalid input!";
        }

        return String.valueOf(frame);
    }
}
