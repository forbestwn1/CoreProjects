package com.nosliw.core.application.division.manual.common.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;

public class HAPManualDataAssociationForTask extends HAPSerializableImp{

	public static String OUT = "out";

	public static String IN = "in";

	private HAPDefinitionDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private Map<String, HAPDefinitionDataAssociation> m_outDataAssociation;

	public HAPManualDataAssociationForTask() {
		this.m_outDataAssociation = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
	}

	public HAPDefinitionDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPDefinitionDataAssociation inDataAssociation) {    this.m_inDataAssociation = inDataAssociation;    }
	
	public Map<String, HAPDefinitionDataAssociation> getOutDataAssociations(){    return this.m_outDataAssociation;     }
	public void setOutDataAssociations(Map<String, HAPDefinitionDataAssociation> outDataAssociations) {  
		if(outDataAssociations!=null) {
			this.m_outDataAssociation.putAll(outDataAssociations);
		}    
	}
	
	public void addOutDataAssociation(String name, HAPDefinitionDataAssociation dataAssociation) {  this.m_outDataAssociation.put(name, dataAssociation);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		this.buildMapping((JSONObject)json);
		return true;  
	}

	private void buildMapping(JSONObject jsonObj) {
		JSONObject inputMappingJson = jsonObj.optJSONObject(IN);
		if(inputMappingJson!=null) {
			this.m_inDataAssociation = HAPManualParserDataAssociation.buildDefinitionByJson(inputMappingJson);
			if(this.m_inDataAssociation!=null) {
				this.m_inDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM);
			}
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(OUT);
		if(outputMappingJson!=null) {
			for(Object key : outputMappingJson.keySet()) {
				HAPDefinitionDataAssociation dataAssociation = HAPManualParserDataAssociation.buildDefinitionByJson(outputMappingJson.optJSONObject((String)key));
				if(dataAssociation!=null) {
					dataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_UPSTREAM);
					this.addOutDataAssociation((String)key, dataAssociation);
				}
			}
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OUT, HAPUtilityJson.buildJson(this.m_outDataAssociation, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
	}

	protected void cloneToTaskDataMappingDefinition(HAPManualDataAssociationForTask def) {
		if(this.m_inDataAssociation!=null) {
			def.m_inDataAssociation = this.m_inDataAssociation.cloneDataAssocation();
		}
		for(String name : this.m_outDataAssociation.keySet()) {
			def.m_outDataAssociation.put(name, this.m_outDataAssociation.get(name).cloneDataAssocation());
		}
	}
	
	@Override
	public HAPManualDataAssociationForTask clone(){
		HAPManualDataAssociationForTask out = new HAPManualDataAssociationForTask();
		this.cloneToTaskDataMappingDefinition(out);
		return out;
	}
}
