package com.nosliw.data.core.domain.entity.expression.data;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocalComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.operand.HAPInterfaceProcessOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPParserExpressionDefinition {

	public static HAPDefinitionExpressionData parseExpressionDefinition(Object obj, HAPContextParser parserContext, HAPParserExpression expressionParser, HAPManagerResourceDefinition resourceDefMan) {
		HAPDefinitionExpressionData out = null;
		if(obj instanceof String) {
			out = new HAPDefinitionExpressionData((String)obj);
		}
		else if(obj instanceof JSONObject) {
			if(HAPUtilityEntityInfo.isEnabled((JSONObject)obj)) {
				out = new HAPDefinitionExpressionData();
				out.buildObject(obj, HAPSerializationFormat.JSON);
			}
		}
		if(out!=null)   out.setOperand(expressionParser.parseExpression(out.getExpression()));
		
		return out;
	}

	public static void processReferenceInExpression(HAPIdEntityInDomain expressionEntityId, HAPDefinitionEntityExpressionData expressionEntity, HAPContextParser parserContext, HAPManagerResourceDefinition resourceDefMan) {
		for(HAPDefinitionExpressionData expressionDef : expressionEntity.getAllExpressionItems()) {
			HAPUtilityOperand.processAllOperand(expressionDef.getOperand(), null, new HAPInterfaceProcessOperand(){
				@Override
				public boolean processOperand(HAPWrapperOperand operand, Object data) {
					String opType = operand.getOperand().getType();
					if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE)){
						HAPOperandReference referenceOperand = (HAPOperandReference)operand.getOperand();
						HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, referenceOperand.getReference());
						HAPIdEntityInDomain refExpId = HAPUtilityParserEntity.parseReferenceResource(resourceId, parserContext, resourceDefMan);
						
						HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();
						parentInfo.setParentId(expressionEntityId);
						parentInfo.getParentRelationConfigure().getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_DEFINITION);
						
						((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(refExpId, parentInfo);
						
						String refAttrName = expressionEntity.addReferencedExpressionAttribute(refExpId);
						referenceOperand.setReferenceExpressionAttributeName(refAttrName);
					}
					return true;
				}
			});
		}
	}
}
