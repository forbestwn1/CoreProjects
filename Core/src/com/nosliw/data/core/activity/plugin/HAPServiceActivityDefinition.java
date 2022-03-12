package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.activity.HAPDefinitionResultActivity;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureGrouped;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;

public class HAPServiceActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String SERVICEUSE = "serviceUse";

	private HAPDefinitionServiceUse m_serviceUse;
	
	private String m_serviceUseName;
	
	private HAPValueStructureGrouped m_valueStructure;
	
	public HAPServiceActivityDefinition(String type) {
		super(type);
	}

	public HAPDefinitionServiceUse getServiceUse() {   return this.m_serviceUse;  }

	public String getServiceUseName() {    return this.m_serviceUseName;      }
	
	@Override
	public HAPValueStructureGrouped getInputValueStructureWrapper() {  return this.m_valueStructure;   }

	@Override
	protected void buildConfigureByJson(JSONObject configurJsonObj) {
		Object serviceUseObj = configurJsonObj.get(SERVICEUSE);
		if(serviceUseObj instanceof JSONObject) {
			this.m_serviceUse = new HAPDefinitionServiceUse();
			this.m_serviceUse.buildObject(serviceUseObj, HAPSerializationFormat.JSON);
		}
		else {
			this.m_serviceUseName = (String)serviceUseObj;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICEUSE, this.m_serviceUse.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPServiceActivityDefinition out = new HAPServiceActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_serviceUse = this.m_serviceUse;
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity,
			HAPSerializationFormat format) {
		this.buildObject(obj, format);
		this.m_valueStructure = complexEntity.getValueStructureWrapper();
		this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
		HAPDefinitionResultActivity result = new HAPDefinitionResultActivity();
		result.setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
		this.addResult(HAPConstantShared.SERVICE_RESULT_SUCCESS, result);
	}

}
