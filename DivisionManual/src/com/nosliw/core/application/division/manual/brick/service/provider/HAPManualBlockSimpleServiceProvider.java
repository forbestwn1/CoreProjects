package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.xxx.application1.brick.service.provider.HAPKeyService;

public class HAPManualBlockSimpleServiceProvider extends HAPManualBrickImp implements HAPBlockServiceProvider{

	@Override
	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEID);	}

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEID, serviceKey);	}

}
