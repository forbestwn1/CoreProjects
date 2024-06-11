package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociationForExpression  extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociationForExpression(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, HAPManualAdapterDataAssociationForExpression.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickManual, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualAdapterDataAssociationForExpression entity = (HAPManualAdapterDataAssociationForExpression)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualAdapterDataAssociationForExpression.DEFINITION);
		HAPManualDataAssociationForExpression dataAssociationForExpression = new HAPManualDataAssociationForExpression();
		dataAssociationForExpression.buildObject(daJsonObj, HAPSerializationFormat.JSON);
		
		entity.setDataAssciation(dataAssociationForExpression);
	}
}
