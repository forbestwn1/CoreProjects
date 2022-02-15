package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.domain.entity.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;

public class HAPExpressionActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	private HAPDefinitionScriptEntity m_script;
	
	private HAPWrapperValueStructure m_valueStructure;
	
	public HAPExpressionActivityDefinition(String type) {
		super(type);
	}
	
	public HAPDefinitionScriptEntity getScript(){  return this.m_script;    }

	@Override
	public HAPWrapperValueStructure getInputValueStructureWrapper() {  return this.m_valueStructure;   }

	@Override
	protected void buildConfigureByJson(JSONObject configurJsonObj) {
		this.m_script = new HAPDefinitionScriptEntity();
		this.m_script.buildObject(configurJsonObj, HAPSerializationFormat.JSON);
		this.m_script.setId(null);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SCRIPT, HAPJsonUtility.buildJson(this.m_script, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPExpressionActivityDefinition out = new HAPExpressionActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_script = this.m_script.cloneScriptEntityDefinition();
		return out;
	}

	@Override
	public void parseActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity, HAPSerializationFormat format) {
		this.buildObject(obj, format);
		this.m_valueStructure = complexEntity.getValueStructureWrapper();
		this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
	}

}
