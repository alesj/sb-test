/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.sbtest.service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.jboss.sbtest.common.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	private static final Logger log = Logger.getLogger(GreetingController.class.getName());

	private final AtomicLong counter = new AtomicLong();
	private GreetingProperties properties;

	@Autowired
	public GreetingController(GreetingProperties properties) {
		this.properties = properties;
	}

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name") String name) {
		log.info(String.format("Received greeting request from %s (%s)", name, counter.incrementAndGet()));
		return new Greeting(counter.get(), String.format(properties.getMessage(), name));
	}
}
