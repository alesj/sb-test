package org.jboss.obsidian.sbt.config;

import javax.validation.Validation;
import javax.validation.Validator;

import com.nike.backstopper.apierror.projectspecificinfo.ProjectApiErrors;
import org.jboss.obsidian.sbt.error.SampleProjectApiErrorsImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Nike-Inc Backstopper samples
 */
@Configuration
@ComponentScan(basePackages = "com.nike.backstopper")
public class SampleWebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public ProjectApiErrors getProjectApiErrors() {
		return new SampleProjectApiErrorsImpl();
	}

	@Bean
	public Validator getJsr303Validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
}
