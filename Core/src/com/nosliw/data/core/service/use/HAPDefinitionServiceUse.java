package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;

public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	@HAPAttribute
	public static String RESULTMAPPING = "resultMapping";

	//parms path
	private HAPDefinitionDataAssociationGroup m_parmMapping;
	
	//result
	private Map<String, HAPDefinitionDataAssociationGroup> m_resultMapping;
	
	private HAPServiceInterface m_serviceInterface;
	
	private String m_provideServiceName;

	
	public HAPDefinitionServiceUse() {
		this.m_parmMapping = new HAPDefinitionDataAssociationGroup();
		this.m_resultMapping = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
	}

	public HAPDefinitionDataAssociationGroup getParms() {  return this.m_parmMapping;   }
	public void setParmMapping(HAPDefinitionDataAssociationGroup parms) {   this.m_parmMapping = parms;  }
	
	public Map<String, HAPDefinitionDataAssociationGroup> getResultMapping(){   return this.m_resultMapping;   }
	public void addResultMapping(String name, HAPDefinitionDataAssociationGroup result) {   this.m_resultMapping.put(name, result);   }
	
	public HAPServiceInterface getServiceInterface() {  return this.m_serviceInterface;  }
	public HAPServiceParm getProviderServiceParm(String parmName) {  return this.m_serviceInterface.getParm(parmName); }

	public void cloneBasicTo(HAPDefinitionServiceUse command) {
		this.cloneToEntityInfo(command);
	}
	
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
