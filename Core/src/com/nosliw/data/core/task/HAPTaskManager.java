package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionParser;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.task.expression.HAPExecuteTaskExpression;

public class HAPTaskManager {
	
	private static Map<String, HAPProcessorTask> m_taskProcessors;
	
	//all expression definition suites
	private Map<String, HAPDefinitionTaskSuite> m_taskDefinitionSuites;

	//used to generate id
	private int m_idIndex;
	
	public HAPTaskManager(){
		this.init();
	}
	
	public static void registerTaskProcessor(String type, HAPProcessorTask processor) {
		m_taskProcessors.put(type, processor);
	}
	
	private void init(){
		HAPValueInfoManager.getInstance().importFromClassFolder(this.getClass());
		
		this.m_taskDefinitionSuites = new LinkedHashMap<String, HAPDefinitionTaskSuite>();
		this.m_idIndex = 1;
	}

	
	public HAPExecutable compileTask(
			String id,
			HAPDefinitionTask taskDefinition, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPVariableInfo> contextVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPProcessTaskContext context) {
		HAPExecuteTaskExpression task = (HAPExecuteTaskExpression)processTask(taskDefinition, null, null, contextTaskDefinitions, contextConstants, context);
		task.setId(id);
		task.discover(contextVariablesInfo, context);
		return task;
	}
	
	public static HAPExecutable processTask(HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessTaskContext context) {
		
		HAPProcessorTask taskProcessor = m_taskProcessors.get(taskDefinition.getType());
		HAPExecutable out = taskProcessor.process(taskDefinition, domain, variableMap, contextTaskDefinitions, contextConstants, context);
		return out;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public HAPDefinitionTaskSuite getTaskDefinitionSuite(String suiteName){		return this.m_taskDefinitionSuites.get(suiteName);	}
	
	public Set<String> getTaskDefinitionSuites() {		return this.m_taskDefinitionSuites.keySet();	}
	
	public void addTaskDefinitionSuite(HAPDefinitionTaskSuite expressionDefinitionSuite){
		//parse expression in suite
        Map<String, HAPDefinitionTask> expDefs = expressionDefinitionSuite.getAllTaskDefinitions();
        for(String name : expDefs.keySet()){
        	HAPDefinitionTask expDef = expDefs.get(name);
        	HAPOperand operand = this.m_expressionProcessor.parseExpression(expDef.getExpression());
        	expDef.setOperand(operand);
        }

        //add to expression manager
		HAPExpressionDefinitionSuiteImp suite = (HAPExpressionDefinitionSuiteImp)this.getTaskDefinitionSuite(expressionDefinitionSuite.getName());
		if(suite==null){
			this.m_taskDefinitionSuites.put(expressionDefinitionSuite.getName(), (HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
		else{
			suite.merge((HAPExpressionDefinitionSuiteImp)expressionDefinitionSuite);
		}
	}
	
	public HAPDefinitionTask getTaskDefinition(String suite, String name) {		return this.getTaskDefinitionSuite(suite).getTask(name);	}

	public HAPDefinitionTask newExpressionDefinition(String expression, String name,
			Map<String, HAPData> constants, Map<String, HAPDataTypeCriteria> variableCriterias) {
		HAPDefinitionTask expDefinition = new HAPTaskDefinitionSimple(expression, name, constants, null, null, null);
		expDefinition.setOperand(this.m_expressionProcessor.parseExpression(expression));
		return expDefinition;
	}

	public HAPDefinitionTaskSuite newTaskDefinitionSuite(String name) {
		return new HAPDefinitionTaskSuite();
	}
}
