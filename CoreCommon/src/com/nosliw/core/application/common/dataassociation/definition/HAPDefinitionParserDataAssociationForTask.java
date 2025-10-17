package com.nosliw.core.application.common.dataassociation.definition;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPDefinitionParserDataAssociationForTask {

	public static HAPDefinitionDataAssociationForTask parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDefinitionDataAssociationForTask out = new HAPDefinitionDataAssociationForTask();
		
		JSONObject inputMappingJson = jsonObj.optJSONObject(HAPDefinitionDataAssociationForTask.IN);
		if(inputMappingJson!=null) {
			HAPDefinitionDataAssociation inDataAssociation = HAPDefinitionParserDataAssociation.buildDefinitionByJson(inputMappingJson, dataRuleMan);
			if(inDataAssociation!=null) {
				inDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM);
				out.setInDataAssociation(inDataAssociation);
			}
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionDataAssociationForTask.OUT);
		if(outputMappingJson!=null) {
			for(Object key : outputMappingJson.keySet()) {
				HAPDefinitionDataAssociation dataAssociation = HAPDefinitionParserDataAssociation.buildDefinitionByJson(outputMappingJson.optJSONObject((String)key), dataRuleMan);
				if(dataAssociation!=null) {
					dataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_UPSTREAM);
					out.addOutDataAssociation((String)key, dataAssociation);
				}
			}
		}
		return out;
	}
	
}
