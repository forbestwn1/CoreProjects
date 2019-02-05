package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPDefinitionDataAssociationGroup;

@HAPEntityWithAttribute
public class HAPDefinitionMappingService extends HAPSerializableImp{

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";

	//parms path
	private HAPDefinitionDataAssociationGroup m_parmMapping;
	
	//result
	private Map<String, HAPDefinitionDataAssociationGroup> m_resultMapping;
	
	public HAPDefinitionMappingService() {
		this.m_parmMapping = new HAPDefinitionDataAssociationGroup();
		this.m_resultMapping = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
	}

	public HAPDefinitionDataAssociationGroup getParms() {  return this.m_parmMapping;   }
	public void setParmMapping(HAPDefinitionDataAssociationGroup parms) {   this.m_parmMapping = parms;  }
	
	public Map<String, HAPDefinitionDataAssociationGroup> getResultMapping(){   return this.m_resultMapping;   }
	public void addResultMapping(String name, HAPDefinitionDataAssociationGroup result) {   this.m_resultMapping.put(name, result);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARMMAPPING, this.m_parmMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULTMAPPING, HAPJsonUtility.buildJson(RESULTMAPPING, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_parmMapping = new HAPDefinitionDataAssociationGroup();
		this.m_parmMapping.buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);

		JSONObject resultJson = jsonObj.optJSONObject(RESULTMAPPING);
		if(resultJson!=null) {
			for(Object key : resultJson.keySet()) {
				HAPDefinitionDataAssociationGroup resultMapping = new HAPDefinitionDataAssociationGroup();
				resultMapping.buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);
				this.m_resultMapping.put((String)key, resultMapping);
			}
		}
		
		return true;  
	}

}
