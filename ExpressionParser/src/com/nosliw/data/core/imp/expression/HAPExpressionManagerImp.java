package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;

public class HAPExpressionManagerImp implements HAPExpressionManager{

	//all expression definition suites
	private Map<String, HAPExpressionDefinitionSuiteImp> m_expressionDefinitionSuites;

	//used to generate id
	private int m_idIndex;
	
	//processor
	private HAPExpressionDefinitionProcessorImp m_expressionProcessor;
	
	public HAPExpressionManagerImp(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
		this.m_expressionProcessor = new HAPExpressionDefinitionProcessorImp(expressionParser, dataTypeHelper);
		this.init();
	}
	
	private void init(){
		HAPValueInfoManager.getInstance().importFromClassFolder(this.getClass());
		
		this.m_expressionDefinitionSuites = new LinkedHashMap<String, HAPExpressionDefinitionSuiteImp>();
		this.m_idIndex = 1;
	}

	@Override
	public HAPExpressionDefinitionSuite getExpressionDefinitionSuite(String suiteName){		return this.m_expressionDefinitionSuites.get(suiteName);	}
	
	@Override
	public Set<String> getExpressionDefinitionSuites() {		return this.m_expressionDefinitionSuites.keySet();	}
	
	@Override
	public void addExpressionDefinitionSuite(HAPExpressionDefinitionSuite expressionDefinitionSuite){
		//parse expression in suite
        Map<String, HAPExpressionDefinition> expDefs = expressionDefinitionSuite.getAllExpressionDefinitions();
        for(String name : expDefs.keySet()){
        	HAPExpressionDefinition expDef = expDefs.get(name);
        	HAPOperand operand = this.m_expressionProcessor.parseExpression(expDef.getExpression());
        	expDef.setOperand(operand);
        }

        //add to expression manager
		HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)this.getExpressionDefinitionSuite(expressionDefinitionSuite.getName());
		if(suite==null){
			this.m_expressionDefinitionSuites.put(expressionDefinitionSuite.getName(), (HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
		else{
			suite.merge((HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
	}
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition(String suite, String name) {		return this.getExpressionDefinitionSuite(suite).getExpressionDefinition(name);	}

	@Override
	public HAPExpression processExpression(String id, HAPExpressionDefinitionSuite suite, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias){
		String expId = id;
		if(expId==null) expId = expressionName + "_no" + this.m_idIndex++;
		HAPExpressionDefinition expDef = suite.getExpressionDefinition(expressionName); 
		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(expId, expDef, suite.getAllExpressionDefinitions(), suite.getConstants(), variableCriterias, this.getContext(suite.getConfigure()));
		return expression;
	}
	
	@Override
	public HAPExpression processExpression(String id, String suiteName, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias){
		String expId = id;
		if(expId==null) expId = expressionName + "_no" + this.m_idIndex++;
		HAPExpressionDefinitionSuite suite = this.getExpressionDefinitionSuite(suiteName);
		HAPExpressionDefinition expDef = suite.getExpressionDefinition(expressionName); 
		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(expId, expDef, suite.getAllExpressionDefinitions(), suite.getConstants(), variableCriterias, this.getContext(suite.getConfigure()));
		return expression;
	}

	@Override
	public HAPExpression processExpression(String id, HAPExpressionDefinition expressionDefinition, Map<String, HAPData> contextConstants, Map<String, HAPDataTypeCriteria> variableCriterias, Map<String, String> context) {
		String expId = id;
		if(expId==null) expId = ""+this.m_idIndex++;
		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(expId, expressionDefinition, null, contextConstants, variableCriterias, this.getContext(context));
		return expression;
	}

	@Override
	public HAPExpressionDefinition newExpressionDefinition(String expression, String name,
			Map<String, HAPData> constants, Map<String, HAPDataTypeCriteria> variableCriterias) {
		HAPExpressionDefinition expDefinition = new HAPExpressionDefinitionSimple(expression, name, constants, null, null, null);
		expDefinition.setOperand(this.m_expressionProcessor.parseExpression(expression));
		return expDefinition;
	}

	@Override
	public HAPExpressionDefinitionSuite newExpressionDefinitionSuite(String name) {
		return new HAPExpressionDefinitionSuiteImp();
	}
	
	private HAPProcessExpressionDefinitionContext getContext(Map<String, String> contextValue){
		return  new HAPProcessExpressionDefinitionContextImp(contextValue);
	}
}
