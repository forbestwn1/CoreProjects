package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPDefinitionResultActivityNormal;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.service.use.HAPDefinitionMappingService;

public class HAPServiceActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	private String m_provider;
	
	private HAPDefinitionMappingService m_serviceMapping;

	public HAPServiceActivityDefinition(String type) {
		super(type);
		this.m_serviceMapping = new HAPDefinitionMappingService();
	}

	public String getProvider() {   return this.m_provider;  }

	public HAPDefinitionMappingService getServiceMapping() {   return this.m_serviceMapping;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_provider = jsonObj.optString(PROVIDER);
		this.m_serviceMapping.getParms().buildObject(jsonObj.optJSONObject(PARMMAPPING), HAPSerializationFormat.JSON);

		Map<String, HAPDefinitionResultActivityNormal> results = this.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionResultActivityNormal result = results.get(resultName);
			HAPDefinitionDataAssociation dataAssociation = result.getOutputDataAssociation();
			this.m_serviceMapping.addResultMapping(resultName, dataAssociation.cloneDataAssocation());
			
			result.setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
/*			
			//build straight data association
			HAPContext association = dataAssociation.getAssociation();
			for(String eleName :association.getElementNames()) {
				HAPContextDefinitionLeafRelative ele = new HAPContextDefinitionLeafRelative();
				ele.setPath(eleName);
				association.addElement(eleName, ele);
			}
*/			
		}

		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
	}
}
