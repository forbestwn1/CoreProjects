package com.nosliw.data.core.event;

import com.nosliw.common.exception.HAPServiceData;

public interface HAPPollTaskResultHandler {

	void onSuccess(HAPPollTaskResult result);

	void onError(HAPServiceData serviceData);
}
