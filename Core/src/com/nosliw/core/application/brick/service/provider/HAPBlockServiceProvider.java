package com.nosliw.core.application.brick.service.provider;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractive;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

@HAPEntityWithAttribute
public class HAPBlockServiceProvider extends HAPBrickBlockSimple{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEID, serviceKey);	}
	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEID);	}
	 
	@Override
	public HAPContainerValuePorts getValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrick.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, this.getServiceKey().getServiceId()));
		HAPResourceIdEmbeded serviceInterfaceResourceId = new HAPResourceIdEmbeded(HAPEnumBrickType.SERVICEINTERFACE_100.getBrickType(), HAPEnumBrickType.SERVICEINTERFACE_100.getVersion(), serviceProfileResourceId, HAPBrickServiceProfile.INTERFACE);
		
		HAPResourceDataBrick brickResourceData = (HAPResourceDataBrick)this.getResourceManager().getResources(Lists.asList(serviceInterfaceResourceId, new HAPResourceId[0]), this.getRuntimeEnvironment().getRuntime().getRuntimeInfo()).getLoadedResources().get(0).getResourceData();
		HAPBrickServiceInterface serviceInterfaceService = (HAPBrickServiceInterface)brickResourceData.getBrick();
		HAPGroupValuePorts valuePortGroup = HAPUtilityInteractive.buildInteractiveValuePortGroup(serviceInterfaceService.getActiveInterface());
		
		out.addValuePortGroup(valuePortGroup, true);
		
		return out;	
	}
}
