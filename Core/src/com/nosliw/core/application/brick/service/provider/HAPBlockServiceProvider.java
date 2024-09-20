package com.nosliw.core.application.brick.service.provider;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPWithInteractiveTask;

@HAPEntityWithAttribute
public interface HAPBlockServiceProvider extends HAPBrick, HAPWithInteractiveTask{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";

	HAPKeyService getServiceKey();

}
