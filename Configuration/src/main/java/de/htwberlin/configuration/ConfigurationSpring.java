package de.htwberlin.configuration;

import de.htwberlin.gameui.impl.GameUIController;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "de.htwberlin")
public class ConfigurationSpring {
    private static final ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationSpring.class);

    public static void main(String[] args) {
        try {
            context.getBean(GameUIController.class).run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
