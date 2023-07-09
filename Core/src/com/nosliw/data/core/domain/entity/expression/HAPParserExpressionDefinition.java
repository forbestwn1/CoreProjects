package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;

public class HAPParserExpressionDefinition {

	public static HAPDefinitionExpression parseExpressionDefinition(Object obj, HAPContextParser parserContext, HAPParserExpression expressionParser, HAPManagerResourceDefinition resourceDefMan) {
		HAPDefinitionExpression out = null;
		if(obj instanceof String) {
			out = new HAPDefinitionExpression((String)obj);
		}
		else if(obj instanceof JSONObject) {
			if(HAPUtilityEntityInfo.isEnabled((JSONObject)obj)) {
				out = new HAPDefinitionExpression();
				out.buildObject(obj, HAPSerializationFormat.JSON);
			}
		}
		if(out!=null)   out.setOperand(expressionParser.parseExpression(out.getExpression()));
		
		return out;
	}

	public static void processReferenceInExpression(HAPDefinitionEntityExpression expressionEntity, HAPContextParser parserContext, HAPManagerResourceDefinition resourceDefMan) {
		for(HAPDefinitionExpression expressionDef : expressionEntity.getAllExpressionItems()) {
			HAPUtilityOperand.processAllOperand(expressionDef.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						HAPResourceId resourceId = HAPUtilityResourceId.buildResourceIdByLiterate(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, referenceOperand.getReference());
						HAPIdEntityInDomain refExpId = HAPUtilityParserEntity.parseReferenceResource(resourceId, parserContext, resourceDefMan);
						String refAttrName = expressionEntity.addReferencedExpressionAttribute(refExpId);
						referenceOperand.setReferenceExpressionAttributeName(refAttrName);
					}
					return true;
				}
			});
		}
	}
}
