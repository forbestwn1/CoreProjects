package com.nosliw.core.application.common.dataassociation.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

public class HAPDefinitionDataAssociationForExpression extends HAPSerializableImp{

	public static String OUT = "out";

	public static String IN = "in";

	private HAPDefinitionDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private HAPDefinitionDataAssociation m_outDataAssociation;

	public HAPDefinitionDataAssociationForExpression() {
	}

	public HAPDefinitionDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPDefinitionDataAssociation inDataAssociation) {    this.m_inDataAssociation = inDataAssociation;    }
	
	public HAPDefinitionDataAssociation getOutDataAssociation(){    return this.m_outDataAssociation;     }
	public void setOutDataAssociation(HAPDefinitionDataAssociation outDataAssociation) {   this.m_outDataAssociation = outDataAssociation;  }   
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OUT, HAPUtilityJson.buildJson(this.m_outDataAssociation, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
	}

	protected void cloneToTaskDataMappingDefinition(HAPDefinitionDataAssociationForExpression def) {
		if(this.m_inDataAssociation!=null) {
			def.m_inDataAssociation = this.m_inDataAssociation.cloneDataAssocation();
		}
		if(this.m_outDataAssociation!=null) {
			def.m_outDataAssociation = this.m_outDataAssociation.cloneDataAssocation();
		}
	}
	
	@Override
	public HAPDefinitionDataAssociationForExpression clone(){
		HAPDefinitionDataAssociationForExpression out = new HAPDefinitionDataAssociationForExpression();
		this.cloneToTaskDataMappingDefinition(out);
		return out;
	}
}