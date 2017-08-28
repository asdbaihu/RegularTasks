package hello;

import hello.Controller.BashScriptController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@Component
@SpringBootApplication
public class Application {

    @Autowired
    static BashScriptController bashScriptController;



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);






    }


}
