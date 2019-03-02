package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;

@HAPEntityWithAttribute
public class HAPDefinitionMappingService extends HAPSerializableImp{

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";

	//parms path
	private HAPDefinitionDataAssociation m_parmMapping;
	
	//result
	private Map<String, HAPDefinitionDataAssociation> m_resultMapping;
	
	public HAPDefinitionMappingService() {
		this.m_parmMapping = new HAPDefinitionDataAssociation();
		this.m_resultMapping = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
		init();
	}

	private void init() {
		HAPUtilityContext.setContextGroupInheritModeNone(this.m_parmMapping.getInfo());
		for(String result : this.m_resultMapping.keySet()) {
			HAPUtilityContext.setContextGroupInheritModeNone(this.m_resultMapping.get(result).getInfo());
		}
	}
	
	public HAPDefinitionDataAssociation getParms() {  return this.m_parmMapping;   }
	public void setParmMapping(HAPDefinitionDataAssociation parms) {   
		this.m_parmMapping = parms;  
		HAPUtilityContext.setContextGroupInheritModeNone(this.m_parmMapping.getInfo());
	}
	
	public Map<String, HAPDefinitionDataAssociation> getResultMapping(){   return this.m_resultMapping;   }
	public void addResultMapping(String name, HAPDefinitionDataAssociation result) {   
		this.m_resultMapping.put(name, result);   
		HAPUtilityContext.setContextGroupInheritModeNone(result.getInfo());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARMMAPPING, this.m_parmMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULTMAPPING, HAPJsonUtility.buildJson(m_resultMapping, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_parmMapping = new HAPDefinitionDataAssociation();
		this.m_parmMapping.buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);

		JSONObject resultJson = jsonObj.optJSONObject(RESULTMAPPING);
		if(resultJson!=null) {
			for(Object key : resultJson.keySet()) {
				HAPDefinitionDataAssociation resultMapping = new HAPDefinitionDataAssociation();
				resultMapping.buildObject(resultJson.optJSONObject((String)key), HAPSerializationFormat.JSON);
				this.m_resultMapping.put((String)key, resultMapping);
			}
		}
		this.init();
		return true;  
	}

}
