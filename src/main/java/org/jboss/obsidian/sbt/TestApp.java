package org.jboss.obsidian.sbt;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.ThreadLocalActiveSpanSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@SpringBootApplication
public class TestApp {

    @Value("${zipkin.host:localhost}")
    String zipkinHost;

    @Value("${zipkin.port:9411}")
    String zipkinPort;

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

    //@Bean
    public Tracer createMockTracer() {
        return new MockTracer(new ThreadLocalActiveSpanSource(), MockTracer.Propagator.TEXT_MAP) {
            @Override
            protected void onSpanFinished(MockSpan mockSpan) {
                System.out.println(mockSpan);
            }
        };
    }

    @Bean
    public Tracer createZipkinTracer() {
        // Configure a reporter, which controls how often spans are sent
        //   (the dependency is io.zipkin.reporter:zipkin-sender-okhttp3)
        String url = String.format("http://%s:%s/api/v1/spans", zipkinHost, zipkinPort);
        Sender sender = OkHttpSender.create(url);
        AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();

        // Now, create a Brave tracing component with the service name you want to see in Zipkin.
        //   (the dependency is io.zipkin.brave:brave)
        brave.Tracing braveTracing = Tracing.newBuilder()
            .localServiceName("sb-test")
            .reporter(reporter)
            .build();

        // use this to create an OpenTracing Tracer
        return BraveTracer.create(braveTracing);
    }
}
