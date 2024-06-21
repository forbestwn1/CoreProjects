package com.nosliw.core.application.common.interactive;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePortGroup;
import com.nosliw.core.application.common.valueport.HAPWrapperValuePort;

@HAPEntityWithAttribute
public class HAPInteractiveExpression extends HAPSerializableImp implements HAPInteractive, HAPWithValuePortGroup{
	
	private HAPInteractiveRequest m_request;
	private HAPInteractiveResultExpression m_result;
	
	public HAPInteractiveExpression(List<HAPRequestParmInInteractive> requestParms, HAPResultElementInInteractiveExpression result) {
		this.m_request = new HAPInteractiveRequest(requestParms);
		this.m_result = new HAPInteractiveResultExpression(result);
	}

	public List<HAPRequestParmInInteractive> getRequestParms() {   return this.m_request.getRequestParms();  }

	public HAPResultElementInInteractiveExpression getResult() {   return this.m_result.getResult();  }
	
/*	
	public HAPIdElement resolveNameFromInternalRequest(String name) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildInternalElementReference(name, HAPConstantShared.IO_DIRECTION_OUT, this); 
		HAPIdElement eleId = HAPUtilityStructureElementReference.resolveElementReferenceInternal(ref, null, this);
		return eleId;
	}

	public HAPIdElement resolveNameFromInternalResponse(String name) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildInternalElementReference(name, HAPConstantShared.IO_DIRECTION_IN, this); 
		HAPIdElement eleId = HAPUtilityStructureElementReference.resolveElementReferenceInternal(ref, null, this);
		return eleId;
	}

	public HAPIdElement resolveNameFromExternalRequest(String name) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildExternalElementReference(name, HAPConstantShared.IO_DIRECTION_IN, this); 
		HAPIdElement eleId = HAPUtilityStructureElementReference.resolveElementReferenceExternal(ref, null, this);
		return eleId;
	}

	public HAPIdElement resolveNameFromExternalResponse(String name) {
		HAPReferenceElement ref = HAPUtilityStructureElementReference.buildExternalElementReference(name, HAPConstantShared.IO_DIRECTION_OUT, this); 
		HAPIdElement eleId = HAPUtilityStructureElementReference.resolveElementReferenceExternal(ref, null, this);
		return eleId;
	}
*/
	
	@Override
	public HAPGroupValuePorts getExternalValuePortGroup() {
		HAPGroupValuePorts group = new HAPGroupValuePorts();

		//request
		HAPWrapperValuePort requestValuePortWrapper = new HAPWrapperValuePort(this.m_request.getExternalValuePort());
		requestValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		group.addValuePort(requestValuePortWrapper, true); 
		
		//result
		HAPWrapperValuePort resultValuePortWrapper = new HAPWrapperValuePort(this.m_result.getExternalValuePort());
		resultValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		group.addValuePort(resultValuePortWrapper, true);

		return group;	
	}
	
	@Override
	public HAPGroupValuePorts getInternalValuePortGroup() {
		HAPGroupValuePorts group = new HAPGroupValuePorts();
		
		//request
		HAPWrapperValuePort requestValuePortWrapper = new HAPWrapperValuePort(this.m_request.getInternalValuePort());
		requestValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		group.addValuePort(requestValuePortWrapper, true);
		
		//result
		HAPWrapperValuePort resultValuePortWrapper = new HAPWrapperValuePort(this.m_result.getInternalValuePort());
		resultValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT);
		group.addValuePort(resultValuePortWrapper, true);
		
		return group;	
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(REQUEST, HAPManagerSerialize.getInstance().toStringValue(this.m_request, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPManagerSerialize.getInstance().toStringValue(this.m_result, HAPSerializationFormat.JSON));
	}

}
