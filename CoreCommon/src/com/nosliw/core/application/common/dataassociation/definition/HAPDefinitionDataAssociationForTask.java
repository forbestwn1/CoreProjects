package com.nosliw.core.application.common.dataassociation.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

public class HAPDefinitionDataAssociationForTask extends HAPSerializableImp{

	public static String OUT = "out";

	public static String IN = "in";

	private HAPDefinitionDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private Map<String, HAPDefinitionDataAssociation> m_outDataAssociation;

	public HAPDefinitionDataAssociationForTask() {
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
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OUT, HAPUtilityJson.buildJson(this.m_outDataAssociation, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
	}

	protected void cloneToTaskDataMappingDefinition(HAPDefinitionDataAssociationForTask def) {
		if(this.m_inDataAssociation!=null) {
			def.m_inDataAssociation = this.m_inDataAssociation.cloneDataAssocation();
		}
		for(String name : this.m_outDataAssociation.keySet()) {
			def.m_outDataAssociation.put(name, this.m_outDataAssociation.get(name).cloneDataAssocation());
		}
	}
	
	@Override
	public HAPDefinitionDataAssociationForTask clone(){
		HAPDefinitionDataAssociationForTask out = new HAPDefinitionDataAssociationForTask();
		this.cloneToTaskDataMappingDefinition(out);
		return out;
	}
}
