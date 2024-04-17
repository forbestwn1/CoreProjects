package com.nosliw.core.application.brick.service.provider;

import java.util.Map;

import com.nosliw.core.application.brick.interactive.interfacee.HAPDefinitionInteractive;
import com.nosliw.core.application.brick.interactive.interfacee.HAPResultInInteractiveInterface;
import com.nosliw.core.application.common.interactive.HAPBrickInteractive;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

public class HAPBrickServiceProvider extends HAPBrickInteractive{

	public static final String ATTR_SERVICEID = "serviceId";

	public void setServiceId(String serviceId) {	this.setAttributeValueObject(ATTR_SERVICEID, serviceId);	}

	public String getServiceId() {	return (String)this.getAttributeValueOfValue(ATTR_SERVICEID);	}
	 
	@Override
	public HAPContainerValuePorts getValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPDefinitionInteractive interactiveDef = this.getInteractive();
		String entityId = this.getId();
		
		//parm
		out.addValuePort(new HAPValuePortServiceRequest(entityId, interactiveDef.getRequestParms()));
		
		//result
		Map<String, HAPResultInInteractiveInterface> results = interactiveDef.getResults();
		for(String resultName : results.keySet()) {
			out.addValuePort(new HAPValuePortServiceResult(entityId, results.get(resultName)));
		}
		return out;
	}
}
