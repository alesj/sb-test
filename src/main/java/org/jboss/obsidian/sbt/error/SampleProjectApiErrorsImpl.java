package org.jboss.obsidian.sbt.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import com.nike.backstopper.apierror.ApiError;
import com.nike.backstopper.apierror.projectspecificinfo.ProjectSpecificErrorCodeRange;
import com.nike.backstopper.apierror.projectspecificinfo.ProjectSpecificErrorCodeRangeIntegerImpl;
import com.nike.backstopper.apierror.sample.SampleProjectApiErrorsBase;

/**
 * @author Nike-Inc Backstopper samples
 */
@Singleton
public class SampleProjectApiErrorsImpl extends SampleProjectApiErrorsBase {

	private static final List<ApiError> projectSpecificApiErrors =
		new ArrayList<>(Arrays.<ApiError>asList(SampleProjectApiError.values()));

	// Set the valid range of non-core error codes for this project to be 99100-99200.
	private static final ProjectSpecificErrorCodeRange errorCodeRange = new ProjectSpecificErrorCodeRangeIntegerImpl(
		99100, 99200, "SAMPLE_PROJECT_API_ERRORS"
	);

	@Override
	protected List<ApiError> getProjectSpecificApiErrors() {
		return projectSpecificApiErrors;
	}

	@Override
	protected ProjectSpecificErrorCodeRange getProjectSpecificErrorCodeRange() {
		return errorCodeRange;
	}

}
