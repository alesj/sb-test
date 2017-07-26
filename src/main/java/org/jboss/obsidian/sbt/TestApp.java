package org.jboss.obsidian.sbt;

import io.opentracing.Tracer;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.ThreadLocalActiveSpanSource;
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

    @Bean
    public Tracer createTracer() {
        return new MockTracer(new ThreadLocalActiveSpanSource(), MockTracer.Propagator.TEXT_MAP) {
            @Override
            protected void onSpanFinished(MockSpan mockSpan) {
                System.out.println(mockSpan);
            }
        };
    }
}
