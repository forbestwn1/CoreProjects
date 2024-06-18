package com.nosliw.core.application.brick.service.provider;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBlockInteractiveInterface;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractive;
import com.nosliw.core.application.common.task.HAPTask;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;

@HAPEntityWithAttribute
public class HAPBlockServiceProvider extends HAPBrickBlockSimple implements HAPTask{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";

	public void setServiceKey(HAPKeyService serviceKey) {	this.setAttributeValueWithValue(SERVICEID, serviceKey);	}
	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getAttributeValueOfValue(SERVICEID);	}
	 
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrick.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, this.getServiceKey().getServiceId()));
		HAPResourceIdEmbeded serviceInterfaceResourceId = new HAPResourceIdEmbeded(HAPEnumBrickType.SERVICEINTERFACE_100.getBrickType(), HAPEnumBrickType.SERVICEINTERFACE_100.getVersion(), serviceProfileResourceId, HAPBlockServiceProfile.INTERFACE);

		HAPBlockInteractiveInterface serviceInterfaceService = (HAPBlockInteractiveInterface)HAPUtilityResource.getResourceDataBrick(serviceInterfaceResourceId, getResourceManager(), this.getRuntimeEnvironment().getRuntime().getRuntimeInfo());
		HAPGroupValuePorts valuePortGroup = HAPUtilityInteractive.buildExternalInteractiveTaskValuePortGroup(serviceInterfaceService);
		
		out.addValuePortGroup(valuePortGroup, true);
		
		return out;	
	}
}
