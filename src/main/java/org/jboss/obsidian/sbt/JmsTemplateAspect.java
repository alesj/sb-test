package org.jboss.obsidian.sbt;

import javax.jms.Message;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapInjector;
import org.springframework.jms.support.SimpleJmsHeaderMapper;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Aspect
public class JmsTemplateAspect {
    @Autowired
    Tracer tracer;

    @Autowired
    MessagingSpanTextMapInjector msgInjector;

    /*
        @Pointcut("execution (* org.springframework.jms.core.JmsTemplate.doSend(..)) && args(.., message)")
        private void sendAdvice(Message message) {}

        @Before("sendAdvice(message)")
        public void doSend(Message message) throws Throwable {
            MessageSpanTextMapAdapter.MessagingTextMap carrier = MessageSpanTextMapAdapter.convert(message);
            msgInjector.inject(tracer.getCurrentSpan(), carrier);
            SimpleJmsHeaderMapper mapper = new SimpleJmsHeaderMapper();
            mapper.fromHeaders(carrier.getMessageHeaders(), message);
        }
    */
    @Around("execution (* org.springframework.jms.core.JmsTemplate.doSend(..))")
    public Object doSend(ProceedingJoinPoint pjp) throws Throwable {
        Object arg1 = pjp.getArgs()[1];
        if (arg1 instanceof Message) {
            Message message = (Message) arg1;
            MessageSpanTextMapAdapter.MessagingTextMap carrier = MessageSpanTextMapAdapter.convert(message);
            msgInjector.inject(tracer.getCurrentSpan(), carrier);
            SimpleJmsHeaderMapper mapper = new SimpleJmsHeaderMapper();
            mapper.fromHeaders(carrier.getMessageHeaders(), message);
        }
        return pjp.proceed();
    }
}
