package com.nosliw.core.application.common.dataassociation.definition;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPDefinitionParserDataAssociationForExpression {

	public static HAPDefinitionDataAssociationForExpression parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDefinitionDataAssociationForExpression out = new HAPDefinitionDataAssociationForExpression();
		
		JSONObject inputMappingJson = jsonObj.optJSONObject(HAPDefinitionDataAssociationForExpression.IN);
		if(inputMappingJson!=null) {
			HAPDefinitionDataAssociation inDataAssociation = HAPDefinitionParserDataAssociation.buildDefinitionByJson(inputMappingJson, dataRuleMan);
			inDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM);
			out.setInDataAssociation(inDataAssociation);
		}

		JSONObject outputMappingJson = jsonObj.optJSONObject(HAPDefinitionDataAssociationForExpression.OUT);
		if(outputMappingJson!=null) {
			HAPDefinitionDataAssociation outDataAssociation = HAPDefinitionParserDataAssociation.buildDefinitionByJson(outputMappingJson, dataRuleMan);
			outDataAssociation.setDirection(HAPConstantShared.DATAASSOCIATION_DIRECTION_UPSTREAM);
			out.setOutDataAssociation(outDataAssociation);
		}
		return out;
	}
	
}
