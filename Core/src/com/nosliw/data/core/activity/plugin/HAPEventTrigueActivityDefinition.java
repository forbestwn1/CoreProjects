package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.activity.HAPDefinitionResultActivity;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;

public class HAPEventTrigueActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String EVENTNAME = "eventName";

	private String m_eventName;
	
	private HAPDefinitionWrapperValueStructure m_valueStructure;
	
	public HAPEventTrigueActivityDefinition(String type) {
		super(type);
	}

	public String getEventName() {    return this.m_eventName;      }
	
	@Override
	public HAPDefinitionWrapperValueStructure getInputValueStructureWrapper() {  return this.m_valueStructure;   }

	@Override
	protected void buildConfigureByJson(JSONObject configurJsonObj) {
		this.m_eventName = (String)configurJsonObj.opt(EVENTNAME);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTNAME, this.m_eventName);
	}
	
	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPEventTrigueActivityDefinition out = new HAPEventTrigueActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_eventName = this.m_eventName;
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPDefinitionEntityInDomainComplex complexEntity,
			HAPSerializationFormat format) {
		this.buildObject(obj, format);
		this.m_valueStructure = complexEntity.getValueStructureWrapper();
		this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
		HAPDefinitionResultActivity result = new HAPDefinitionResultActivity();
		result.setOutputDataAssociation(new HAPDefinitionDataAssociationNone());
		this.addResult(HAPConstantShared.SERVICE_RESULT_SUCCESS, result);
	}

}
