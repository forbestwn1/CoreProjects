package com.nosliw.data.core.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinitionWithContext;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPManagerExpression {

	private HAPManagerResourceDefinition m_resourceDefManager;

	private HAPRequirementContextProcessor m_contextProcessRequirement;

	public HAPManagerExpression(
			HAPManagerResourceDefinition resourceDefManager,
			HAPDataTypeHelper dataTypeHelper,
			HAPRuntime runtime,
			HAPManagerServiceDefinition serviceDefinitionManager
			) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_contextProcessRequirement = new HAPRequirementContextProcessor(this.m_resourceDefManager, dataTypeHelper, runtime, this, serviceDefinitionManager, null);
	}
	
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

	public HAPExecutableExpression getExpression(HAPResourceId expressionId, HAPContextExpression context) {
		if(context==null)  context = HAPContextExpression.createContext(this.m_resourceDefManager);
		HAPExecutableExpression out = HAPProcessorExpression.process(
				expressionId.toStringValue(HAPSerializationFormat.LITERATE), 
				context.getResourceDefinition(expressionId), 
				null, 
				null, 
				this, 
				null, 
				m_contextProcessRequirement,
				new HAPProcessTracker());
		return out;
	}
	
	
	
}
