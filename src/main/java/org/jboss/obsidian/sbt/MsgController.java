package org.jboss.obsidian.sbt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapInjector;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@RestController
public class MsgController {
    @Autowired
    JmsTemplate template;

    @Autowired
    Tracer tracer;

    @Autowired
    MessagingSpanTextMapInjector msgInjector;

    @RequestMapping("/msg")
    public Response msg(@RequestParam(value="text", defaultValue="Test msg!") String text) {
        template.convertAndSend("fooQueue", text, message -> {
            msgInjector.inject(tracer.getCurrentSpan(), MessageSpanTextMapAdapter.convert(message));
            return message;
        });
        return new Response(String.format("Sending OK [%s] ...", text));
    }
}
