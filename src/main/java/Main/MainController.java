package Main;

import Methods.DetectMovement;
import Methods.PixelDetect;
import Methods.Yolo;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    private DetectMovement detectMovement;
    private PixelDetect pixelDetect;
    private Yolo yolo;
    private DetectMovement detectMovement2;
    private PixelDetect pixelDetect2;
    private Yolo yolo2;
    private List<Cameras> cameras = new ArrayList<>();
    private List<Methods> methods;
    @PostConstruct
    public void init()
    {
        String energetyka = "http://live.uci.agh.edu.pl/video/stream3.cgi";
        String alejka = "http://live.uci.agh.edu.pl/video/stream1.cgi";
        this.detectMovement = new DetectMovement(60,300,150,360, new Point(100,265),new Point(400,265),7000,energetyka,true,40);
        Thread t = new Thread(detectMovement);
        t.start();
        this.pixelDetect = new PixelDetect(addtestpoints(1),addlist(1),energetyka);
        Thread t2 = new Thread(pixelDetect);
        t2.start();
        this.yolo = new Yolo(30,this.addpointyolo(),energetyka);
        Thread t3 = new Thread(yolo);
        t3.start();
        cameras.add(new Cameras("Centrum Energetyki","1"));
        this.detectMovement2 = new DetectMovement(100,200,50,170, new Point(95,50),new Point(150,165),3500,alejka,false,40);
        Thread t4 = new Thread(detectMovement2);
        t4.start();
//        this.pixelDetect2 = new PixelDetect(addtestpoints(2),addlist(2),alejka);
//        Thread t5 = new Thread(pixelDetect2);
//        t5.start();
        this.yolo2 = new Yolo(30,this.addpointyolo(),alejka);
        Thread t6 = new Thread(yolo2);
        t6.start();
        cameras.add(new Cameras("Alejka","2"));
    }
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    @GetMapping("/")
    @ResponseBody
    public List<Methods> index(@RequestParam String id) {
        Integer intid;
        try {
             intid = Integer.valueOf(id);
        } catch(NumberFormatException e) {
            return null;
        }
        this.methods = new ArrayList<>();
        switch (intid)
        {
            case 1:
                this.methods.add(new Methods("Detect Movement", this.detectMovement.freespaces()));
                this.methods.add(new Methods("Pixel Detect", this.pixelDetect.freespaces()));
                this.methods.add(new Methods("Yolo", this.yolo.freespaces()));
                return this.methods;
            case 2:
                this.methods.add(new Methods("Detect Movement", this.detectMovement2.freespaces()));
                //this.methods.add(new Methods("Pixel Detect", this.pixelDetect2.freespaces()));
                this.methods.add(new Methods("Yolo", this.yolo2.freespaces()));
                return this.methods;
        }


        return null;
    }
    @RequestMapping(value = "/getCameras", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cameras> cameralist() {
        return this.cameras;
    }
    private List<Point> addpointyolo() {
        List<Point> fieldpoints = new ArrayList<>();
        fieldpoints.add(new Point(1,43));
        fieldpoints.add(new Point(452,89));
        fieldpoints.add(new Point(430,260));
        fieldpoints.add(new Point(1,190));
        return fieldpoints;
    }
    private List<List<Point>> addlist(int id) {
        List<List<Point>> listofpoints = new ArrayList<>();
        if(id == 1) {
            List<Point> points = new ArrayList<>();
            points.add(new Point(16, 63));
            points.add(new Point(25, 64));
            points.add(new Point(10, 72));
            points.add(new Point(1, 70));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(25, 64));
            points.add(new Point(40, 65));
            points.add(new Point(18, 76));
            points.add(new Point(10, 72));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(40, 65));
            points.add(new Point(54, 66));
            points.add(new Point(26, 79));
            points.add(new Point(18, 76));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(54, 66));
            points.add(new Point(63, 67));
            points.add(new Point(39, 80));
            points.add(new Point(27, 79));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(63, 67));
            points.add(new Point(82, 69));
            points.add(new Point(55, 82));
            points.add(new Point(39, 80));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(82, 69));
            points.add(new Point(93, 69));
            points.add(new Point(66, 84));
            points.add(new Point(55, 82));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(93, 69));
            points.add(new Point(115, 69));
            points.add(new Point(85, 85));
            points.add(new Point(66, 84));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(115, 69));
            points.add(new Point(135, 70));
            points.add(new Point(102, 87));
            points.add(new Point(85, 85));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(135, 70));
            points.add(new Point(153, 71));
            points.add(new Point(121, 88));
            points.add(new Point(101, 87));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(153, 71));
            points.add(new Point(172, 73));
            points.add(new Point(141, 90));
            points.add(new Point(121, 88));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(172, 73));
            points.add(new Point(190, 75));
            points.add(new Point(161, 92));
            points.add(new Point(141, 90));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(190, 75));
            points.add(new Point(213, 77));
            points.add(new Point(184, 94));
            points.add(new Point(161, 92));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(213, 77));
            points.add(new Point(236, 77));
            points.add(new Point(208, 96));
            points.add(new Point(184, 94));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(236, 77));
            points.add(new Point(258, 81));
            points.add(new Point(229, 99));
            points.add(new Point(208, 96));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(258, 81));
            points.add(new Point(284, 83));
            points.add(new Point(258, 103));
            points.add(new Point(229, 99));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(284, 83));
            points.add(new Point(310, 87));
            points.add(new Point(284, 106));
            points.add(new Point(258, 103));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(310, 87));
            points.add(new Point(336, 89));
            points.add(new Point(313, 110));
            points.add(new Point(284, 106));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(368, 96));
            points.add(new Point(396, 100));
            points.add(new Point(381, 119));
            points.add(new Point(351, 114));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(396, 100));
            points.add(new Point(425, 104));
            points.add(new Point(412, 123));
            points.add(new Point(381, 119));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(425, 104));
            points.add(new Point(452, 109));
            points.add(new Point(444, 128));
            points.add(new Point(412, 123));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(22, 104));
            points.add(new Point(38, 106));
            points.add(new Point(1, 126));
            points.add(new Point(1, 116));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(38, 106));
            points.add(new Point(56, 109));
            points.add(new Point(11, 133));
            points.add(new Point(1, 128));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(56, 109));
            points.add(new Point(74, 111));
            points.add(new Point(27, 136));
            points.add(new Point(11, 133));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(74, 111));
            points.add(new Point(93, 114));
            points.add(new Point(47, 140));
            points.add(new Point(27, 136));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(93, 114));
            points.add(new Point(113, 117));
            points.add(new Point(68, 144));
            points.add(new Point(47, 140));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(113, 117));
            points.add(new Point(137, 121));
            points.add(new Point(91, 149));
            points.add(new Point(68, 144));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(161, 124));
            points.add(new Point(137, 121));
            points.add(new Point(91, 149));
            points.add(new Point(117, 153));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(161, 124));
            points.add(new Point(187, 128));
            points.add(new Point(144, 158));
            points.add(new Point(117, 153));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(187, 128));
            points.add(new Point(215, 132));
            points.add(new Point(172, 163));
            points.add(new Point(144, 158));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(320, 148));
            points.add(new Point(354, 153));
            points.add(new Point(325, 191));
            points.add(new Point(287, 183));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(354, 153));
            points.add(new Point(392, 159));
            points.add(new Point(387, 168));
            points.add(new Point(343, 170));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(392, 159));
            points.add(new Point(429, 165));
            points.add(new Point(416, 191));
            points.add(new Point(387, 167));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(1, 130));
            points.add(new Point(28, 136));
            points.add(new Point(1, 166));
            points.add(new Point(1, 138));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(28, 136));
            points.add(new Point(48, 140));
            points.add(new Point(23, 165));
            points.add(new Point(1, 166));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(71, 145));
            points.add(new Point(92, 149));
            points.add(new Point(42, 179));
            points.add(new Point(18, 171));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(92, 149));
            points.add(new Point(117, 155));
            points.add(new Point(68, 185));
            points.add(new Point(42, 179));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(117, 155));
            points.add(new Point(144, 160));
            points.add(new Point(94, 193));
            points.add(new Point(68, 185));
            listofpoints.add(points);
            points = new ArrayList<>();
            points.add(new Point(144, 160));
            points.add(new Point(173, 165));
            points.add(new Point(125, 200));
            points.add(new Point(94, 193));
            listofpoints.add(points);
        }
        else if(id == 2)
        {

        }
        return listofpoints;
    }
    public static List<Point> addtestpoints(int id) {
        List<Point> points = new ArrayList<>();
        if (id ==1)
        {
        points.add(new Point(400,140));
        points.add(new Point(212,230));
        points.add(new Point(255,136));
        points.add(new Point(71,92));
        }
        else if (id == 2)
        {
            points.add(new Point(100,80));
            points.add(new Point(110,180));
            points.add(new Point(140,250));
            points.add(new Point(170,185));
        }
        return points;
    }
}
