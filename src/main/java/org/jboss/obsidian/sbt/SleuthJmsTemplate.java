package org.jboss.obsidian.sbt;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.SimpleJmsHeaderMapper;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Component
public class SleuthJmsTemplate extends JmsTemplate {
    private Tracer tracer;

    @Autowired
    public SleuthJmsTemplate(ConnectionFactory connectionFactory, Tracer tracer) {
        super(connectionFactory);
        this.tracer = tracer;
    }

    @Override
    protected void doSend(MessageProducer producer, Message message) throws JMSException {
        MessageSpanTextMapAdapter.MessagingTextMap carrier = MessageSpanTextMapAdapter.convert(message);
        ActiveSpan span = tracer.activeSpan();
        tracer.inject(span.context(), Format.Builtin.TEXT_MAP, carrier);
        SimpleJmsHeaderMapper mapper = new SimpleJmsHeaderMapper();
        mapper.fromHeaders(carrier.getMessageHeaders(), message);
        super.doSend(producer, message);
    }
}
