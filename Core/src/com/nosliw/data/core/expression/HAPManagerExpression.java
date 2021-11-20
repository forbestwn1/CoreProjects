package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPUtilityWithValueStructure;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.resource.HAPResourceEntityExpressionGroup;
import com.nosliw.data.core.expression.resource.HAPResourceEntityExpressionSuite;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;


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
	
	public HAPResourceEntityExpressionSuite getExpressionSuiteDefinition(HAPResourceId suiteId, HAPContainerAttachment parentAttachment) {
		HAPResourceEntityExpressionSuite suiteDef = (HAPResourceEntityExpressionSuite)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}

	public HAPResourceEntityExpressionGroup getExpressionDefinition(HAPResourceId expressionId, HAPContainerAttachment parentAttachment) {
		HAPResourceEntityExpressionGroup expressionDef = (HAPResourceEntityExpressionGroup)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(expressionId, parentAttachment);
		return expressionDef;
	}
	
	public HAPExecutableExpressionGroup getExpression(HAPResourceId expressionId, Map<String, String> configure) {
		HAPResourceEntityExpressionGroup exressionGroupResourceDef = (HAPResourceEntityExpressionGroup)this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(expressionId);
		HAPContextProcessor contextProcess = new HAPContextProcessor(exressionGroupResourceDef, this.m_runtimeEnv);
		
		if(configure==null) {
			//build configure from definition info
			configure = new LinkedHashMap<String, String>();
			for(String n : exressionGroupResourceDef.getInfo().getNames()) {
				configure.put(n, (String)exressionGroupResourceDef.getInfo().getValue(n)); 
			}
		}
		
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(
				expressionId.toStringValue(HAPSerializationFormat.LITERATE), 
				exressionGroupResourceDef,
				contextProcess, 
				null, 
				configure, 
				this.m_runtimeEnv,
				new HAPProcessTracker());
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(String expression, Map<String, HAPDataTypeCriteria> varCriteria) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		HAPValueStructureDefinitionFlat valueStructure = new HAPValueStructureDefinitionFlat();
		if(varCriteria!=null) {
			for(String varName : varCriteria.keySet()) {
				valueStructure.addRoot(varName, new HAPElementStructureLeafData(varCriteria.get(varName)));
			}
		}
		HAPUtilityWithValueStructure.setValueStructure(expressionGroupDef, valueStructure);
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(null, expressionGroupDef, new HAPContextProcessor(null, this.m_runtimeEnv), null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(String expression) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionExpressionGroupImp expressionGroupDef = new HAPDefinitionExpressionGroupImp();
		HAPUtilityWithValueStructure.setValueStructure(expressionGroupDef, new HAPValueStructureDefinitionFlat());
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPExecutableExpressionGroup out = HAPProcessorExpression.process(null, expressionGroupDef, new HAPContextProcessor(null, this.m_runtimeEnv), null, HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }
}
