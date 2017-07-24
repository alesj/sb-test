package org.jboss.obsidian.sbt;

import java.util.logging.Logger;

import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapExtractor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Component
public class MsgListener {
    private static final Logger log = Logger.getLogger(MsgListener.class.getName());

    @Autowired
    Tracer tracer;

    @Autowired
    MessagingSpanTextMapExtractor msgExtractor;

    @JmsListener(destination = "fooQueue")
    public void processMessage(Message msg) {
        SpanTextMap carrier = MessageSpanTextMapAdapter.convert(msg);
        Span span = msgExtractor.joinTrace(carrier);
        Span continuedSpan = tracer.continueSpan(span);
        try {
            log.info("Received msg: " + msg.toString());
        } finally {
            tracer.close(continuedSpan);
        }
    }
}