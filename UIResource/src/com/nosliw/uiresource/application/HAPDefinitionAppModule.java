package com.nosliw.uiresource.application;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPUtilityDataAssociation;

@HAPEntityWithAttribute
public class HAPDefinitionAppModule  extends HAPEntityInfoWritableImp{

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
	
	private Map<String, HAPDefinitionDataAssociation> m_outputMapping;
	
	private HAPDefinitionDataAssociation m_inputMapping;
	
	public HAPDefinitionAppModule() {
		this.m_outputMapping = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
		this.m_inputMapping = new HAPDefinitionDataAssociation();
	}
	
	public String getModule() {   return this.m_module;   }
	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public Map<String, HAPDefinitionDataAssociation> getOutputMapping() {   return this.m_outputMapping;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROLE, this.m_role);
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_role = (String)jsonObj.opt(ROLE);
		this.m_module = (String)jsonObj.opt(MODULE);
		this.m_outputMapping = HAPUtilityDataAssociation.buildDataAssociation(jsonObj.optJSONObject(OUTPUTMAPPING));
		this.m_inputMapping.buildObject(jsonObj.optJSONObject(INPUTMAPPING), HAPSerializationFormat.JSON);
		return true;
	}
}
