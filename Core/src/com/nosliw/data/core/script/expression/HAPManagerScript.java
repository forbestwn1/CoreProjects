package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.expression.HAPContextExpression;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPProcessorExpression;
import com.nosliw.data.core.expression.HAPResourceDefinitionExpression;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;


public class HAPManagerScript {
	
	private HAPManagerExpression m_expressionManager;
	
	private HAPManagerResourceDefinition m_resourceDefManager;

	private HAPRequirementContextProcessor m_contextProcessRequirement;

	private HAPManagerExpression m_expressionMan;
	
	public HAPManagerScript(
			HAPManagerExpression expressionMan,
			HAPManagerResourceDefinition resourceDefManager,
			HAPDataTypeHelper dataTypeHelper,
			HAPRuntime runtime,
			HAPManagerServiceDefinition serviceDefinitionManager
			) {
		this.m_expressionMan = expressionMan;
		this.m_resourceDefManager = resourceDefManager;
		this.m_contextProcessRequirement = new HAPRequirementContextProcessor(this.m_resourceDefManager, dataTypeHelper, runtime, m_expressionManager, serviceDefinitionManager, null);
	}
	
	public HAPResourceDefinitionScriptGroup getScriptDefinition(HAPResourceId resourceId, HAPAttachmentContainer parentAttachment) {
		return (HAPResourceDefinitionScriptGroup)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(resourceId, parentAttachment);
	}

	public HAPExecutableScriptGroup getScript(HAPResourceId resourceId, Map<String, String> configure) {
		if(context==null)  context = HAPContextExpression.createContext(this.m_resourceDefManager);
		HAPEntityWithResourceContext resourceDefWithContext = context.getResourceDefinition(expressionId);
		
		if(configure==null) {
			//build configure from definition info
			HAPResourceDefinitionExpression expressionDef = (HAPResourceDefinitionExpression)resourceDefWithContext.getEntity();
			configure = new LinkedHashMap<String, String>();
			for(String n : expressionDef.getInfo().getNames()) {
				configure.put(n, (String)expressionDef.getInfo().getValue(n)); 
			}
		}
		
		HAPExecutableExpression out = HAPProcessorExpression.process(
				expressionId.toStringValue(HAPSerializationFormat.LITERATE), 
				resourceDefWithContext, 
				null, 
				null, 
				this, 
				configure, 
				m_contextProcessRequirement,
				new HAPProcessTracker());
		return out;
	}
}
