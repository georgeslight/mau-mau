package de.htwberlin;

import de.htwberlin.impl.service.GameUIController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "de.htwberlin")
public class ConfigurationSpring {
    private static final ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationSpring.class);

    public static void main(String[] args) {
        context.getBean(GameUIController.class).run();
    }
}
