package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionGroup;
import com.nosliw.data.core.expression.resource.HAPResourceDefinitionExpressionSuite;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;


public class HAPManagerExpression {
	
	private HAPParserExpression m_expressionParser;

	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManagerExpression(
			HAPParserExpression expressionParser,
			HAPRuntimeEnvironment runtimeEnv
			) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_expressionParser = expressionParser;
	}
	
	public HAPParserExpression getExpressionParser() {   return this.m_expressionParser;    }
	
	public HAPResourceDefinitionExpressionSuite getExpressionSuiteDefinition(HAPResourceId suiteId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionExpressionSuite suiteDef = (HAPResourceDefinitionExpressionSuite)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}

	public HAPResourceDefinitionExpressionGroup getExpressionDefinition(HAPResourceId expressionId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionExpressionGroup expressionDef = (HAPResourceDefinitionExpressionGroup)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(expressionId, parentAttachment);
		return expressionDef;
	}
	
	public HAPEntityWithResourceContext getExpressionDefinitionWithContext(HAPResourceId processId, HAPContainerAttachment parentAttachment) {
		HAPResourceDefinitionExpressionGroup expressionDef = this.getExpressionDefinition(processId, parentAttachment);
		HAPEntityWithResourceContext out = new HAPEntityWithResourceContext(expressionDef, HAPContextResourceExpressionGroup.createContext(expressionDef.getSuite(), this.getResourceDefinitionManager()));
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(HAPResourceId expressionId, HAPContextResourceExpressionGroup context, Map<String, String> configure) {
		HAPEntityWithResourceContext resourceDefWithContext = null;
		if(context==null) {
			HAPResourceDefinitionExpressionGroup exressionGroupResourceDef = (HAPResourceDefinitionExpressionGroup)this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(expressionId);
			HAPDefinitionExpressionSuiteImp suite = HAPUtilityExpressionComponent.buildExpressionSuiteFromComponent(exressionGroupResourceDef, this.m_runtimeEnv);
			context = HAPContextResourceExpressionGroup.createContext(suite, this.getResourceDefinitionManager());
			resourceDefWithContext = new HAPEntityWithResourceContext(exressionGroupResourceDef, context);
		}
		else {
			resourceDefWithContext = context.getResourceDefinition(expressionId);
		}
		
		if(configure==null) {
			//build configure from definition info
			HAPResourceDefinitionExpressionGroup expressionDef = (HAPResourceDefinitionExpressionGroup)resourceDefWithContext.getEntity();
			configure = new LinkedHashMap<String, String>();
			for(String n : expressionDef.getInfo().getNames()) {
				configure.put(n, (String)expressionDef.getInfo().getValue(n)); 
			}
		}
		
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(
				expressionId.toStringValue(HAPSerializationFormat.LITERATE), 
				resourceDefWithContext, 
				null, 
				null, 
				this, 
				configure, 
				this.m_runtimeEnv,
				new HAPProcessTracker());
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(String expression, Map<String, HAPDataTypeCriteria> varCriteria) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		HAPContext context = new HAPContext();
		if(varCriteria!=null) {
			for(String varName : varCriteria.keySet()) {
				context.addElement(varName, new HAPContextDefinitionLeafData(varCriteria.get(varName)));
			}
		}
		expressionGroupDef.setContextStructure(context);
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPEntityWithResourceContext resourceWithContext = new HAPEntityWithResourceContext(expressionGroupDef, HAPContextResourceExpressionGroup.createContext(null, getResourceDefinitionManager()));
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(null, resourceWithContext, null, null, this, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(String expression) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		expressionGroupDef.setContextStructure(new HAPContext());
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPEntityWithResourceContext resourceWithContext = new HAPEntityWithResourceContext(expressionGroupDef, HAPContextResourceExpressionGroup.createContext(null, getResourceDefinitionManager()));
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(null, resourceWithContext, null, null, this, HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }
}
