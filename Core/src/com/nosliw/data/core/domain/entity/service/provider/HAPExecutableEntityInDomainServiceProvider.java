package com.nosliw.data.core.domain.entity.service.provider;

import java.util.Map;

import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveResult;
import com.nosliw.data.core.interactive.HAPExecutableEntityInteractive;

public class HAPExecutableEntityInDomainServiceProvider extends HAPExecutableEntityInteractive{

	public static final String ATTR_SERVICEID = "serviceId";

	public void setServiceId(String serviceId) {	this.setAttributeValueObject(ATTR_SERVICEID, serviceId);	}

	public String getServiceId() {	return (String)this.getAttributeValue(ATTR_SERVICEID);	}
	 
	@Override
	public HAPContainerValuePorts getValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPDefinitionInteractive interactiveDef = this.getInteractive();
		String entityId = this.getId();
		
		//parm
		out.addValuePort(new HAPValuePortServiceRequest(entityId, interactiveDef.getRequestParms()));
		
		//result
		Map<String, HAPDefinitionInteractiveResult> results = interactiveDef.getResults();
		for(String resultName : results.keySet()) {
			out.addValuePort(new HAPValuePortServiceResult(entityId, results.get(resultName)));
		}
		return out;
	}
}
