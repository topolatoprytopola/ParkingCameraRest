package Main;

import Methods.DetectMovement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        DetectMovement detectMovement = new DetectMovement(60,300,150,360);
        Thread t = new Thread(detectMovement);
        t.start();
        SpringApplication.run(Main.class, args);
    }
}
