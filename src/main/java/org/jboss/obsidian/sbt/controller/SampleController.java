package org.jboss.obsidian.sbt.controller;

import static java.util.Collections.singletonList;
import static org.jboss.obsidian.sbt.controller.SampleController.SAMPLE_PATH;

import java.util.Arrays;
import java.util.UUID;

import javax.validation.Valid;

import com.nike.backstopper.exception.ApiException;
import com.nike.backstopper.service.ClientDataValidationService;
import com.nike.internal.util.Pair;
import org.jboss.obsidian.sbt.error.SampleProjectApiError;
import org.jboss.obsidian.sbt.model.RgbColor;
import org.jboss.obsidian.sbt.model.SampleModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nike-Inc Backstopper samples
 */
@Controller
@RequestMapping(SAMPLE_PATH)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SampleController {

	public static final String SAMPLE_PATH = "/sample";
	public static final String CORE_ERROR_WRAPPER_ENDPOINT_SUBPATH = "/coreErrorWrapper";
	public static final String WITH_REQUIRED_QUERY_PARAM_SUBPATH = "/withRequiredQueryParam";
	public static final String TRIGGER_UNHANDLED_ERROR_SUBPATH = "/triggerUnhandledError";

	public static int nextRangeInt(int lowerBound, int upperBound) {
		return (int) Math.round(Math.random() * upperBound) + lowerBound;
	}

	public static RgbColor nextRandomColor() {
		return RgbColor.values()[nextRangeInt(0, 2)];
	}

	@GetMapping(produces = "application/json")
	@ResponseBody
	public SampleModel getSampleModel() {
		return new SampleModel(
			UUID.randomUUID().toString(), String.valueOf(nextRangeInt(0, 42)), nextRandomColor().name(), false
		);
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public SampleModel postSampleModel(@Valid @RequestBody SampleModel model) {
		if (Boolean.TRUE.equals(model.throw_manual_error)) {
			throw ApiException.newBuilder()
				.withExceptionMessage("Manual error throw was requested")
				.withApiErrors(SampleProjectApiError.MANUALLY_THROWN_ERROR)
				.withExtraDetailsForLogging(Pair.of("rgb_color_value", model.rgb_color))
				.withExtraResponseHeaders(
					Pair.of("rgbColorValue", singletonList(model.rgb_color)),
					Pair.of("otherExtraMultivalueHeader", Arrays.asList("foo", "bar"))
				)
				.build();
		}

		return model;
	}

	@GetMapping(path = CORE_ERROR_WRAPPER_ENDPOINT_SUBPATH)
	public void failWithCoreErrorWrapper() {
		throw ApiException.newBuilder()
			.withExceptionMessage("Throwing error due to 'reasons'")
			.withApiErrors(SampleProjectApiError.SOME_MEANINGFUL_ERROR_NAME)
			.build();
	}

	@GetMapping(path = WITH_REQUIRED_QUERY_PARAM_SUBPATH, produces = "text/plain")
	@ResponseBody
	public String withRequiredQueryParam(@RequestParam(name = "requiredQueryParamValue") int someRequiredQueryParam) {
		return "You passed in " + someRequiredQueryParam + " for the required query param value";
	}

	@GetMapping(path = TRIGGER_UNHANDLED_ERROR_SUBPATH)
	public void triggerUnhandledError() {
		throw new RuntimeException("This should be handled by SpringUnhandledExceptionHandler.");
	}
}
