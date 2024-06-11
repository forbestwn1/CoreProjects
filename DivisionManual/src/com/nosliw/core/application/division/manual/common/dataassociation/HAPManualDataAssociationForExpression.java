package com.nosliw.core.application.division.manual.common.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDataAssociationForExpression extends HAPSerializableImp{

	public static String OUT = "out";

	public static String IN = "in";

	private HAPManualDataAssociation m_inDataAssociation;
	
	//data association from process to external
	private HAPManualDataAssociation m_outDataAssociation;

	public HAPManualDataAssociationForExpression() {
	}

	public HAPManualDataAssociation getInDataAssociation() {   return this.m_inDataAssociation;   }
	public void setInDataAssociation(HAPManualDataAssociation inDataAssociation) {    this.m_inDataAssociation = inDataAssociation;    }
	
	public HAPManualDataAssociation getOutDataAssociation(){    return this.m_outDataAssociation;     }
	public void setOutDataAssociation(HAPManualDataAssociation outDataAssociation) {   this.m_outDataAssociation = outDataAssociation;  }   
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		this.buildMapping((JSONObject)json);
		return true;  
	}

	private void buildMapping(JSONObject jsonObj) {
		JSONObject inputMappingJson = jsonObj.optJSONObject(IN);
		if(inputMappingJson!=null) {
			this.m_inDataAssociation = HAPManualParserDataAssociation.buildDefinitionByJson(inputMappingJson);
			this.m_inDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM);
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(OUT);
		if(outputMappingJson!=null) {
			this.m_outDataAssociation = HAPManualParserDataAssociation.buildDefinitionByJson(outputMappingJson);
			this.m_outDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_UPSTREAM);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OUT, HAPUtilityJson.buildJson(this.m_outDataAssociation, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
	}

	protected void cloneToTaskDataMappingDefinition(HAPManualDataAssociationForExpression def) {
		if(this.m_inDataAssociation!=null) {
			def.m_inDataAssociation = this.m_inDataAssociation.cloneDataAssocation();
		}
		if(this.m_outDataAssociation!=null) {
			def.m_outDataAssociation = this.m_outDataAssociation.cloneDataAssocation();
		}
	}
	
	@Override
	public HAPManualDataAssociationForExpression clone(){
		HAPManualDataAssociationForExpression out = new HAPManualDataAssociationForExpression();
		this.cloneToTaskDataMappingDefinition(out);
		return out;
	}
}