/**
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
package org.jboss.sbtest.ui;

import org.jboss.sbtest.common.Greeting;
import org.jboss.sbtest.wingtips.Tracing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UiController {

	private UiProperties properties;
	private final RestTemplate client = Tracing.createRestTemplate();

	@Autowired
	public UiController(UiProperties properties) {
		this.properties = properties;
	}

	@RequestMapping("/go")
	public String go(@RequestParam(value = "name", defaultValue = "World") String name) {
		String url = String.format("http://%s/greeting?name=%s", properties.getHost(), name);
		ResponseEntity<Greeting> response = client.getForEntity(url, Greeting.class);
		return response.getBody().getContent();
	}
}
