package org.jboss.obsidian.sbt;

import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/msg")
    public Response msg(@RequestParam(value="text", defaultValue="Test msg!") String text) {
        template.convertAndSend("fooQueue", text);
        return new Response(String.format("Sending OK [%s] ...", text));
    }
}
