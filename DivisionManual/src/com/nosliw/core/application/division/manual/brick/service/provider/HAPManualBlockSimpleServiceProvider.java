package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.core.application.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.application.brick.service.provider.HAPKeyService;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockSimpleServiceProvider extends HAPManualBrickImp implements HAPBlockServiceProvider{

	@Override
	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEID);	}

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEID, serviceKey);	}

	@Override
	public HAPInteractiveTask getTaskInteractive(){  return (HAPInteractiveTask)this.getAttributeValueOfValue(TASKINTERACTIVE);   }
	
	public void setTaskInteractive(HAPInteractiveTask taskInteractive){  this.setAttributeValueWithValue(TASKINTERACTIVE, taskInteractive); }
	
}
