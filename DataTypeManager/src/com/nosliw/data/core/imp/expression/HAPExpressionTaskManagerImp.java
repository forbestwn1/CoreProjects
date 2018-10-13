package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.task111.HAPDefinitionTask;
import com.nosliw.data.core.task111.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task111.HAPManagerTask;

public class HAPExpressionTaskManagerImp{ 
//extends HAPManagerTask{

//	//used to generate id
//	private int m_idIndex;
//	
//	//processor
//	private HAPExpressionDefinitionProcessorImp m_expressionProcessor;
//	
//	public HAPExpressionTaskManagerImp(HAPExpressionParser expressionParser, HAPDataTypeHelper dataTypeHelper){
//		this.m_expressionProcessor = new HAPExpressionDefinitionProcessorImp(expressionParser, dataTypeHelper);
//		this.init();
//	}
//	
//	private void init(){
//		HAPValueInfoManager.getInstance().importFromClassFolder(this.getClass());
//		this.m_idIndex = 1;
//	}
//
//
//	@Override
//	public HAPExpression processExpression(String id, String expressionName, HAPDefinitionTaskSuite suite, Map<String, HAPVariableInfo> variableCriterias){
//		String expId = id;
//		if(expId==null) expId = expressionName + "_no" + this.m_idIndex++;
//		HAPDefinitionTask expDef = suite.getTaskDefinition(expressionName); 
//		HAPExpression expression = this.m_expressionProcessor.process(expId, expDef, suite.getAllTaskDefinitions(), suite.getConstants(), variableCriterias, this.getContext(suite.getConfigure()));
//		return expression;
//	}
//	
//	@Override
//	public HAPExpression processExpression(String id, String expressionName, String suiteName, Map<String, HAPVariableInfo> variableCriterias){
//		String expId = id;
//		if(expId==null) expId = expressionName + "_no" + this.m_idIndex++;
//		HAPDefinitionTaskSuite suite = this.getTaskDefinitionSuite(suiteName);
//		HAPDefinitionTask expDef = suite.getTaskDefinition(expressionName); 
//		HAPExpression expression = this.m_expressionProcessor.process(expId, expDef, suite.getAllTaskDefinitions(), suite.getConstants(), variableCriterias, this.getContext(suite.getConfigure()));
//		return expression;
//	}
//
//
//	@Override
//	public HAPExpression processExpression(String id, HAPDefinitionTask expressionDefinition,	HAPDefinitionTaskSuite suite, Map<String, HAPVariableInfo> variableCriterias, Map<String, String> context) {
//		String expId = id;
//		if(expId==null) expId = expressionDefinition.getName() + "_no" + this.m_idIndex++;
//		Map<String, String> cfgContext = new LinkedHashMap<String, String>();
//		if(suite.getConfigure()!=null)		cfgContext.putAll(suite.getConfigure());
//		if(context!=null)  cfgContext.putAll(context);
//		HAPExpression expression = this.m_expressionProcessor.process(expId, expressionDefinition, suite.getAllTaskDefinitions(), suite.getConstants(), variableCriterias, this.getContext(cfgContext));
//		return expression;
//	}
//	
//	@Override
//	public HAPExpression processExpression(String id, HAPDefinitionTask expressionDefinition, Map<String, HAPData> contextConstants, Map<String, HAPVariableInfo> variableCriterias, Map<String, String> context) {
//		String expId = id;
//		if(expId==null) expId = ""+this.m_idIndex++;
//		HAPExpression expression = this.m_expressionProcessor.processExpressionDefinition(expId, expressionDefinition, null, contextConstants, variableCriterias, this.getContext(context));
//		return expression;
//	}
//
//	private HAPProcessExpressionDefinitionContext getContext(Map<String, String> contextValue){
//		return  new HAPProcessExpressionDefinitionContextImp(contextValue);
//	}
}
