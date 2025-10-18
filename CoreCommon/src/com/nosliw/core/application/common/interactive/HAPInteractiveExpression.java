package com.nosliw.core.application.common.interactive;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionParm;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionResult;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPInteractiveExpression extends HAPSerializableImp implements HAPInteractive{
	
	private HAPInteractiveRequest m_request;
	private HAPDefinitionResult m_result;

	public HAPInteractiveExpression() {
		this.m_request = new HAPInteractiveRequest();
		this.m_result = new HAPDefinitionResult();
	}
	
	public HAPInteractiveExpression(List<HAPDefinitionParm> requestParms, HAPDefinitionResult result) {
		this.m_request = new HAPInteractiveRequest(requestParms);
		this.m_result = result;
	}

	public void setRequest(HAPInteractiveRequest request) {    this.m_request = request;      }
	public List<HAPDefinitionParm> getRequestParms() {   return this.m_request.getRequestParms();  }

	public HAPDefinitionResult getResult() {   return this.m_result;  }
	
	public static HAPInteractiveExpression parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInteractiveExpression out = new HAPInteractiveExpression();
		
		JSONArray parmsArray = jsonObj.optJSONArray(REQUEST);
		if(parmsArray!=null) {
			out.setRequest(HAPInteractiveRequest.parse(parmsArray, dataRuleMan));
		}
		
		JSONObject resutltsObj = jsonObj.optJSONObject(RESULT);
		if(resutltsObj!=null) {
			out.m_result.buildObject(resutltsObj, HAPSerializationFormat.JSON);
		}
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(REQUEST, HAPManagerSerialize.getInstance().toStringValue(this.m_request, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPManagerSerialize.getInstance().toStringValue(this.m_result, HAPSerializationFormat.JSON));
	}

	
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
*/
	
}
