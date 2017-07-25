package org.jboss.obsidian.sbt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapInjector;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.SimpleJmsHeaderMapper;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Component
public class SleuthJmsTemplate extends JmsTemplate {
    private Tracer tracer;
    private MessagingSpanTextMapInjector msgInjector;

    @Autowired
    public SleuthJmsTemplate(ConnectionFactory connectionFactory, Tracer tracer, MessagingSpanTextMapInjector msgInjector) {
        super(connectionFactory);
        this.tracer = tracer;
        this.msgInjector = msgInjector;
    }

    @Override
    protected void doSend(MessageProducer producer, Message message) throws JMSException {
        MessageSpanTextMapAdapter.MessagingTextMap carrier = MessageSpanTextMapAdapter.convert(message);
        msgInjector.inject(tracer.getCurrentSpan(), carrier);
        SimpleJmsHeaderMapper mapper = new SimpleJmsHeaderMapper();
        mapper.fromHeaders(carrier.getMessageHeaders(), message);
        super.doSend(producer, message);
    }
}
