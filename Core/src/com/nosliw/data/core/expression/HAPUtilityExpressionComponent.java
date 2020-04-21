package com.nosliw.data.core.expression;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPUtilityDataComponent;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPDefinitionComplex;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.script.context.HAPContextStructure;

public class HAPUtilityExpressionComponent {

	public static HAPDefinitionExpressionSuiteImp buildExpressionSuiteFromComponent(HAPDefinitionComplex complexEntity) {
		return buildExpressionSuiteFromComponent(complexEntity, null);
	}
	
	public static HAPDefinitionExpressionSuiteImp buildExpressionSuiteFromComponent(HAPDefinitionComplex complexEntity, HAPContextStructure contextStructure) {
		HAPDefinitionExpressionSuiteImp out = new HAPDefinitionExpressionSuiteImp();
		
		//build context
		if(contextStructure==null)		complexEntity.cloneToDataContext(out);
		else   out.setContextStructure(contextStructure);
		
		//build constant
		for(HAPDefinitionConstant constantDef : HAPUtilityDataComponent.buildDataConstantDefinition(complexEntity.getAttachmentContainer())) {
			out.addConstantDefinition(constantDef);
		}
		
		//build expression definition
		buildExpressionSuiteFromAttachment(out, complexEntity.getAttachmentContainer());
		
		return out;
	}
	
	public static void buildExpressionSuiteFromAttachment(HAPDefinitionExpressionSuite suite, HAPAttachmentContainer attachmentContainer) {
		Map<String, HAPAttachment> expressionAtts = attachmentContainer.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION);
		for(String name : expressionAtts.keySet()) {
			HAPAttachment attachment = expressionAtts.get(name);
			suite.addEntityElement(buildExpressionGroup(attachment));
		}
	}

	private static HAPDefinitionExpressionGroup buildExpressionGroup(HAPAttachment attachment) {
		HAPDefinitionExpressionGroupImp out = new HAPDefinitionExpressionGroupImp();
		attachment.cloneToEntityInfo(out);
		if(HAPConstant.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			entityAttachment.cloneToEntityInfo(out);
			JSONObject attachmentEntityJsonObj = entityAttachment.getEntityJsonObj();
			HAPParserExpressionDefinition.parseExpressionDefinitionList(out, attachmentEntityJsonObj);
		}
		else if(HAPConstant.ATTACHMENT_TYPE_REFERENCE.equals(attachment.getType())) {
		}
		return out;
	}
}
