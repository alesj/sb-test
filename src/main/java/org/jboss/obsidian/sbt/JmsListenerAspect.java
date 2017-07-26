package org.jboss.obsidian.sbt;

import javax.jms.Message;

import io.opentracing.ActiveSpan;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Aspect
public class JmsListenerAspect {
    @Autowired
    Tracer tracer;

    @Around("@annotation(org.springframework.jms.annotation.JmsListener)")
    public Object aroundListenerMethod(final ProceedingJoinPoint pjp) throws Throwable {
        Message msg = (Message) pjp.getArgs()[0];
        try (ActiveSpan span = createSpan(msg, "jms:" + msg.getJMSDestination())) {
            return pjp.proceed();
        }
    }

    private ActiveSpan createSpan(Message message, String name) {
        TextMap carrier = MessageSpanTextMapAdapter.convert(message);
        SpanContext parent = tracer.extract(Format.Builtin.TEXT_MAP, carrier);
        ActiveSpan result = tracer.buildSpan(name).asChildOf(parent).startActive();
        result.log("SERVER_RECV");
        return result;
    }
}