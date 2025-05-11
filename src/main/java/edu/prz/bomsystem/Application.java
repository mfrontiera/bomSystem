package edu.prz.bomsystem;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "edu.prz.bomsystem")
@Theme(value = "bomsystem")
public class Application implements AppShellConfigurator {

    //private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(Application.class, args);
    }

}
