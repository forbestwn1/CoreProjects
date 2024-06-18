package com.nosliw.data.core.domain.entity.expression.data1;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;
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
						
						String refAttrName = HAPUtilityEntityContainer.addComplexElementAttribute(expressionEntity.getReferencesContainerId(), refExpId, parserContext);
						referenceOperand.setReferenceExpressionAttributeName(refAttrName);
					}
					return true;
				}
			});
		}
	}
}
