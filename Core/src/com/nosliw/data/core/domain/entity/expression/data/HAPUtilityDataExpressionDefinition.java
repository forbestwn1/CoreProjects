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

public class HAPUtilityDataExpressionDefinition {

	public static HAPDefinitionExpressionData parseExpressionDefinition(Object obj, HAPParserDataExpression expressionParser) {
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

	public static HAPDefinitionExpressionData parseExpressionDefinitionScript(String script, HAPParserDataExpression expressionParser) {
		HAPDefinitionExpressionData out = new HAPDefinitionExpressionData(script);
		out.setOperand(expressionParser.parseExpression(out.getExpression()));
		return out;
	}

	
	public static void processReferenceInExpression(HAPIdEntityInDomain expressionEntityId, HAPContextParser parserContext, HAPManagerResourceDefinition resourceDefMan) {
		HAPDefinitionEntityExpressionData expressionEntity = (HAPDefinitionEntityExpressionData)parserContext.getGlobalDomain().getEntityInfoDefinition(expressionEntityId).getEntity();
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
	
	
	public static void buildParentRelation(HAPIdEntityInDomain parentEntityId, HAPIdEntityInDomain childEntityId, String valueContextInheritMode, HAPContextParser parserContext) {
		HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();
		parentInfo.setParentId(parentEntityId);
		parentInfo.getParentRelationConfigure().getValueStructureRelationMode().getInheritProcessorConfigure().setMode(valueContextInheritMode);
		
		((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(childEntityId, parentInfo);
	}
	
}
