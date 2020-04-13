package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.component.HAPResourceDefinitionComplex;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.expression.resource.HAPDefinitionResourceDefinitionExpressionSuiteElementEntity;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionSuite;

public class HAPUtilityExpressionComponent {

	public static HAPResourceDefinitionExpressionSuite buildExpressionSuiteFromComponent(HAPResourceDefinitionComplex component, HAPParserExpression expressionParser) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();
		component.cloneToComplexResourceDefinition(out);
		
		if(component instanceof HAPComponentContainerElement) {
			out = buildExpressionSuiteFromComponent(((HAPComponentContainerElement)component).getEntityElement(), expressionParser);
		}
		else if(component instanceof HAPComponentImp) {
			component.cloneToComplexResourceDefinition(out);
			buildExpressionSuiteFromAttachment(out, component.getAttachmentContainer(), expressionParser);
		}
		else {
			buildExpressionSuiteFromAttachment(out, component.getAttachmentContainer(), expressionParser);
		}
		
		return out;
	}
	
	public static void buildExpressionSuiteFromAttachment(HAPDefinitionExpressionSuite suite, HAPAttachmentContainer attachmentContainer, HAPParserExpression expressionParser) {
		Map<String, HAPAttachment> expressionAtts = attachmentContainer.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION);
		for(String name : expressionAtts.keySet()) {
			HAPAttachment attachment = expressionAtts.get(name);
			suite.addExpressionGroup(buildExpressionSuiteElementFromAttachment(attachment, expressionParser));
		}
		HAPUtilityExpression.normalizeReference(suite);
	}

	private static HAPDefinitionResourceDefinitionExpressionSuiteElementEntity buildExpressionSuiteElementFromAttachment(HAPAttachment attachment, HAPParserExpression expressionParser) {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = null;
		if(HAPConstant.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			out = HAPParserExpressionDefinition.parseExpressionSuiteElement(entityAttachment.getEntityJsonObj(), expressionParser);
			attachment.cloneToEntityInfo(out);
		}
		else if(HAPConstant.ATTACHMENT_TYPE_REFERENCE.equals(attachment.getType())) {
		}
		return out;
	}
}
