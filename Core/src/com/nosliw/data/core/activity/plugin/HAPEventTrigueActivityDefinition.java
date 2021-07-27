package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.activity.HAPDefinitionResultActivity;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPEventTrigueActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String EVENTNAME = "eventName";

	private String m_eventName;
	
	private HAPWrapperValueStructure m_valueStructure;
	
	public HAPEventTrigueActivityDefinition(String type) {
		super(type);
	}

	public String getEventName() {    return this.m_eventName;      }
	
	@Override
	public HAPWrapperValueStructure getInputValueStructureWrapper() {  return this.m_valueStructure;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_eventName = (String)jsonObj.opt(EVENTNAME);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTNAME, this.m_eventName);
	}
	
	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPEventTrigueActivityDefinition out = new HAPEventTrigueActivityDefinition(this.getType());
		this.cloneToNormalActivityDefinition(out);
		out.m_eventName = this.m_eventName;
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity,
			HAPSerializationFormat format) {
		this.buildObject(obj, format);
		this.m_valueStructure = complexEntity.getValueStructureWrapper();
		this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
		HAPDefinitionResultActivity result = new HAPDefinitionResultActivity();
		result.setOutputDataAssociation(new HAPDefinitionDataAssociationNone());
		this.addResult(HAPConstantShared.SERVICE_RESULT_SUCCESS, result);
	}

	@Override
	public HAPDefinitionTask cloneTaskDefinition() {   return this.cloneActivityDefinition();  }
}
