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

    @JmsListener(destination = "fooQueue") // TODO -- to @Aspect
    public void processMessage(Message msg) throws Exception {
        Span span = createSpan(msg, "jms:" + msg.getJMSDestination());
        try {
            log.info("Received msg: " + msg.toString());
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