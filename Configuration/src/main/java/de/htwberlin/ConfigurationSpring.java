package de.htwberlin;

import de.htwberlin.service.GameUIController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSpring {
    private static ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("de.htwberlin");

    public static void main(String[] args) {
        context.getBean(GameUIController.class).run();
    }

}
