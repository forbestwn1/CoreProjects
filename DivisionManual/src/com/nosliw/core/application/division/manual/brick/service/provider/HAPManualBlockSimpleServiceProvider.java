package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBlockInteractiveInterface;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.application.brick.service.provider.HAPKeyService;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;

public class HAPManualBlockSimpleServiceProvider extends HAPManualBrickImp implements HAPBlockServiceProvider{

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEID, serviceKey);	}
	@Override
	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEID);	}
	 
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrickId.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, this.getServiceKey().getServiceId()));
		HAPResourceIdEmbeded serviceInterfaceResourceId = new HAPResourceIdEmbeded(HAPEnumBrickType.SERVICEINTERFACE_100.getBrickType(), HAPEnumBrickType.SERVICEINTERFACE_100.getVersion(), serviceProfileResourceId, HAPBlockServiceProfile.INTERFACE);

		HAPBlockInteractiveInterface serviceInterfaceService = (HAPBlockInteractiveInterface)HAPUtilityResource.getResourceDataBrick(serviceInterfaceResourceId, getResourceManager(), this.getRuntimeEnvironment().getRuntime().getRuntimeInfo());
		return serviceInterfaceService.getExternalValuePorts();
	}
}
