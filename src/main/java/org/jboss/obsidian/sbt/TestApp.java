package org.jboss.obsidian.sbt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SpringBootApplication
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

/*
    @Bean
    public JmsTemplateAspect createJmsTemplateAspect() {
        return new JmsTemplateAspect();
    }
*/

    @Bean
    public JmsListenerAspect createJmsListenerAspect() {
        return new JmsListenerAspect();
    }
}
