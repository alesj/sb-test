package org.jboss.obsidian.sbt;

import java.util.logging.Logger;

import javax.jms.Message;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Component
public class MsgListener {
    private static final Logger log = Logger.getLogger(MsgListener.class.getName());

    @JmsListener(destination = "fooQueue") // TODO -- to @Aspect
    public void processMessage(Message msg) throws Exception {
        log.info("Received msg: " + msg.toString());
    }
}