package com.nosliw.data.core.domain.entity.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.common.HAPUtilityWithValueStructure;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPManagerResourceComplexEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceEntityExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceEntityExpressionSuite;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

public class HAPManagerExpression extends HAPManagerResourceComplexEntity{
	
	private HAPParserExpression m_expressionParser;

	public HAPManagerExpression(
			HAPParserExpression expressionParser,
			HAPRuntimeEnvironment runtimeEnv
			) {
		super(runtimeEnv);
		this.m_expressionParser = expressionParser;
	}
	
	public HAPParserExpression getExpressionParser() {   return this.m_expressionParser;    }
	

	
	
	
	
	
	
	public HAPResourceEntityExpressionSuite getExpressionSuiteDefinition(HAPResourceId suiteId, HAPDefinitionEntityContainerAttachment parentAttachment) {
		HAPResourceEntityExpressionSuite suiteDef = (HAPResourceEntityExpressionSuite)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(suiteId, parentAttachment);
		return suiteDef;
	}

	public HAPResourceEntityExpressionGroup getExpressionDefinition(HAPResourceId expressionId, HAPDefinitionEntityContainerAttachment parentAttachment) {
		HAPResourceEntityExpressionGroup expressionDef = (HAPResourceEntityExpressionGroup)getResourceDefinitionManager().getAdjustedComplextResourceDefinition(expressionId, parentAttachment);
		return expressionDef;
	}
	
	public HAPExecutableExpressionGroup getExpression(String expression, Map<String, HAPDataTypeCriteria> varCriteria) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionEntityExpressionGroup expressionGroupDef = new HAPDefinitionEntityExpressionGroup();
		HAPValueStructureDefinitionFlat valueStructure = new HAPValueStructureDefinitionFlat();
		if(varCriteria!=null) {
			for(String varName : varCriteria.keySet()) {
				valueStructure.addRoot(varName, new HAPElementStructureLeafData(varCriteria.get(varName)));
			}
		}
		HAPUtilityWithValueStructure.setValueStructure(expressionGroupDef, valueStructure);
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPExecutableExpressionGroup out = HAPPluginEntityDefinitionInDomainExpressionGroup.process(null, expressionGroupDef, new HAPContextProcessor(null, this.m_runtimeEnv), null, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}

	public HAPExecutableExpressionGroup getExpression(String expression) {
		HAPDefinitionExpression expressionDef = new HAPDefinitionExpression(expression);
		HAPDefinitionEntityExpressionGroup expressionGroupDef = new HAPDefinitionEntityExpressionGroup();
		HAPUtilityWithValueStructure.setValueStructure(expressionGroupDef, new HAPValueStructureDefinitionFlat());
		expressionGroupDef.addExpression(expressionDef);
		
		HAPProcessTracker processTracker = new HAPProcessTracker();
		HAPExecutableExpressionGroup out = HAPPluginEntityDefinitionInDomainExpressionGroup.process(null, expressionGroupDef, new HAPContextProcessor(null, this.m_runtimeEnv), null, HAPUtilityExpressionProcessConfigure.setDontDiscovery(null), this.m_runtimeEnv, processTracker);
		return out;
	}
	
	private HAPManagerResourceDefinition getResourceDefinitionManager() {     return this.m_runtimeEnv.getResourceDefinitionManager();   }
}
