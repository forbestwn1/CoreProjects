package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociationForExpression  extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociationForExpression(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, HAPManualDefinitionAdapterDataAssociationForExpression.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapterDataAssociationForExpression entity = (HAPManualDefinitionAdapterDataAssociationForExpression)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualDefinitionAdapterDataAssociationForExpression.DEFINITION);
		HAPManualDataAssociationForExpression dataAssociationForExpression = new HAPManualDataAssociationForExpression();
		dataAssociationForExpression.buildObject(daJsonObj, HAPSerializationFormat.JSON);
		
		entity.setDataAssciation(dataAssociationForExpression);
	}
}
