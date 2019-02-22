package com.nosliw.uiresource.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociationGroup;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppModule  extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String ROLE = "role";

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static final String OUTPUTMAPPING = "outputMapping";
	
	private String m_role;
	
	private String m_module;
	
	private HAPDefinitionDataAssociationGroup m_inputMapping;
	
	private HAPDefinitionDataAssociationGroup m_outputMapping;
	
	public HAPDefinitionMiniAppModule() {
		this.m_inputMapping = new HAPDefinitionDataAssociationGroup();
		this.m_outputMapping = new HAPDefinitionDataAssociationGroup();
	}
	
	public String getModule() {   return this.m_module;   }
	public HAPDefinitionDataAssociationGroup getInputMapping() {   return this.m_inputMapping;   }
	public HAPDefinitionDataAssociationGroup getOutputMapping() {   return this.m_outputMapping;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROLE, this.m_role);
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, this.m_outputMapping.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_role = (String)jsonObj.opt(ROLE);
		this.m_module = (String)jsonObj.opt(MODULE);
		this.m_inputMapping.buildObject(jsonObj.optJSONObject(INPUTMAPPING), HAPSerializationFormat.JSON);
		this.m_outputMapping.buildObject(jsonObj.optJSONObject(OUTPUTMAPPING), HAPSerializationFormat.JSON);
		return true;
	}
}
