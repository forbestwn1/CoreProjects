package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

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
		this.m_resultMapping = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
	}

	public HAPDefinitionDataAssociation getParms() {  return this.m_parmMapping;   }
	public void setParmMapping(HAPDefinitionDataAssociation parms) {   
		this.m_parmMapping = parms;  
//		HAPUtilityContext.setContextGroupInheritModeNone(this.m_parmMapping.getInfo());
	}
	
	public Map<String, HAPDefinitionDataAssociation> getResultMapping(){   return this.m_resultMapping;   }
	public void addResultMapping(String name, HAPDefinitionDataAssociation result) {   
		this.m_resultMapping.put(name, result);   
//		HAPUtilityContext.setContextGroupInheritModeNone(result.getInfo());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARMMAPPING, this.m_parmMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULTMAPPING, HAPUtilityJson.buildJson(m_resultMapping, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_parmMapping = HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(PARMMAPPING)); 
		this.m_parmMapping.buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);

		JSONObject resultJson = jsonObj.optJSONObject(RESULTMAPPING);
		if(resultJson!=null) {
			for(Object key : resultJson.keySet()) {
				HAPDefinitionDataAssociation resultMapping = HAPParserDataAssociation.buildDefinitionByJson(resultJson.optJSONObject((String)key));
				this.m_resultMapping.put((String)key, resultMapping);
			}
		}
		return true;  
	}

}
