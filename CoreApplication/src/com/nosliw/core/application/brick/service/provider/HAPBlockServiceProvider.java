package com.nosliw.core.application.brick.service.provider;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockServiceProvider extends HAPBrick{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";

	HAPKeyService getServiceKey();

}
