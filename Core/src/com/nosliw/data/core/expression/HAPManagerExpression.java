package com.nosliw.data.core.expression;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinitionWithContext;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManagerExpression {

	private HAPManagerResourceDefinition m_resourceDefManager;

	public HAPDefinitionExpressionSuite getExpressionSuiteDefinition(HAPResourceId suiteId, HAPAttachmentContainer parentAttachment) {
		HAPDefinitionExpressionSuite suiteDef = (HAPDefinitionExpressionSuite)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}

	public HAPDefinitionExpression getExpressionDefinition(HAPResourceId expressionId, HAPAttachmentContainer parentAttachment) {
		HAPDefinitionExpression expressionDef = (HAPDefinitionExpression)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(expressionId, parentAttachment);
		return expressionDef;
	}
	
	public HAPResourceDefinitionWithContext getExpressionDefinitionWithContext(HAPResourceId processId, HAPAttachmentContainer parentAttachment) {
		HAPDefinitionExpression expressionDef = this.getExpressionDefinition(processId, parentAttachment);
		HAPResourceDefinitionWithContext out = new HAPResourceDefinitionWithContext(expressionDef, HAPContextExpression.createContext(expressionDef.getSuite(), this.m_resourceDefManager));
		return out;
	}


}
