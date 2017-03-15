package org.jboss.obsidian.sbt.controller;

import com.nike.backstopper.apierror.sample.SampleCoreApiError;
import com.nike.backstopper.exception.ApiException;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Nike-Inc Backstopper samples
 */
@Controller
@RequestMapping("/error")
public class SampleErrorController implements ErrorController {

	@RequestMapping
	@ResponseBody
	public void unknownError() {
		throw ApiException.newBuilder()
			.withApiErrors(SampleCoreApiError.UNHANDLED_FRAMEWORK_ERROR)
			.withExceptionMessage("Unknown container/framework error.").build();
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}
