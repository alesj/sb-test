package org.jboss.obsidian.sbt;

import javax.jms.Message;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapExtractor;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Aspect
public class JmsListenerAspect {
    @Autowired
    Tracer tracer;

    @Autowired
    MessagingSpanTextMapExtractor msgExtractor;

    @Around("@annotation(org.springframework.jms.annotation.JmsListener)")
    public Object aroundListenerMethod(final ProceedingJoinPoint pjp) throws Throwable {
        Message msg = (Message) pjp.getArgs()[0];
        Span span = createSpan(msg, "jms:" + msg.getJMSDestination());
        try {
            return pjp.proceed();
        } finally {
            closeSpans(span);
        }
    }

    private Span createSpan(Message message, String name) {
        SpanTextMap carrier = MessageSpanTextMapAdapter.convert(message);
        Span parent = msgExtractor.joinTrace(carrier);
        Span result;
        if (parent != null) {
            result = tracer.createSpan(name, parent);
            if (parent.isRemote()) {
                result.logEvent(Span.SERVER_RECV);
            }
        } else {
            result = tracer.createSpan(name);
            result.logEvent(Span.SERVER_RECV);
        }
        return result;
    }

    private void closeSpans(Span span) {
        if (span != null) {
            tracer.close(span);
        }
    }
}