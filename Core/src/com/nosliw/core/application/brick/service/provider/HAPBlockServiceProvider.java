package com.nosliw.core.application.brick.service.provider;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveInterface;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

@HAPEntityWithAttribute
public class HAPBlockServiceProvider extends HAPBrickBlockSimple{

	@HAPAttribute
	public static final String SERVICEID = "serviceId";

	public void setServiceId(String serviceId) {	this.setAttributeValueWithValue(SERVICEID, serviceId);	}
	public String getServiceId() {	return (String)this.getAttributeValueOfValue(SERVICEID);	}
	 
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
