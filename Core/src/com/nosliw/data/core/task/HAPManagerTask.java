package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.task.expression.HAPManagerTaskExpression;

public class HAPManagerTask {
	
	private HAPRuntime m_runtime;
	
	private static Map<String, HAPManagerTaskSpecific> m_taskManagers = new LinkedHashMap<String, HAPManagerTaskSpecific>();
	
	//all expression definition suites
	private Map<String, HAPDefinitionTaskSuite> m_taskDefinitionSuites;

	private Map<String, HAPExecutableTask> m_executables; 
	
	//used to generate id
	private int m_idIndex;
	
	public HAPManagerTask(HAPRuntime runtime){
		this.m_runtime = runtime;
		this.init();
	}

	private void init(){
		
		HAPValueInfoManager.getInstance().importFromClassFolder(this.getClass());
		
		this.m_executables = new LinkedHashMap<String, HAPExecutableTask>();
		this.m_taskDefinitionSuites = new LinkedHashMap<String, HAPDefinitionTaskSuite>();
		this.m_idIndex = 1;
		
		this.registerTaskManager(HAPConstant.DATATASK_TYPE_EXPRESSION, new HAPManagerTaskExpression(this, this.m_runtime));
	}

	
	public HAPData executeTask(String taskName, String suite, Map<String, HAPData> parms, HAPLogTask logger) {
		return this.executeTask(taskName, this.getTaskDefinitionSuite(suite), parms, logger);
	}
	
	
	public HAPData executeTask(String taskName, HAPDefinitionTaskSuite suite, Map<String, HAPData> parms, HAPLogTask logger) {
		
		long start = System.currentTimeMillis();
		
		//compile task
		HAPProcessContext  processContext = new HAPProcessContext();

		String bufName = suite.getName()+"_" +suite.getTask(taskName).getName();
		HAPExecutableTask executableTask = this.m_executables.get(bufName);
		if(executableTask==null) {
			executableTask = this.compileTask(this.generateId(), suite.getTask(taskName), suite.getAllTasks(), suite.getVariables(), suite.getConstants(), null, suite.getConfigure(), processContext);
			if(HAPRuntime.isDemo) {
				this.m_executables.put(bufName, executableTask);
			}
		}
		
		//convert parms
		
		//execute task
		HAPTaskReferenceCache cache = new HAPTaskReferenceCache();
		HAPData out = this.executeTask(executableTask, parms, cache, logger);

		long end = System.currentTimeMillis();
		System.out.println(bufName + " " + (end-start));
		
		return out;
	}
	
	public HAPData executeTask(HAPExecutableTask executableTask, Map<String, HAPData> parms, HAPTaskReferenceCache cache, HAPLogTask logger) {
		
		HAPData out = m_taskManagers.get(executableTask.getType()).getTaskExecutor().execute(executableTask, parms, cache, logger);
		return out;
	}

	public HAPExecutableTask compileTask(
			String id,
			HAPDefinitionTask taskDefinition, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput,
			Map<String, String> configure,
			HAPProcessContext context) {
		HAPExecutableTask task = processTask(taskDefinition, null, null, contextTaskDefinitions, contextConstants, context);
		task.setId(id);
		
		Map<String, HAPVariableInfo> variableInfos = parentVariablesInfo;
		if(variableInfos==null) 	variableInfos = new LinkedHashMap<String, HAPVariableInfo>();
		task.discoverVariable(variableInfos, expectOutput, context);
		
		return task;
	}
	
	public HAPExecutableTask processTask(HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {
		HAPProcessorTask taskProcessor = m_taskManagers.get(taskDefinition.getType()).getTaskProcessor();
		HAPExecutableTask out = taskProcessor.process(taskDefinition, domain, variableMap, contextTaskDefinitions, contextConstants, context);
		return out;
	}
	
	
	public void registerTaskManager(String type, HAPManagerTaskSpecific manager) {		m_taskManagers.put(type, manager);	}
	public HAPManagerTaskSpecific getTaskManager(String type) {  return this.m_taskManagers.get(type);  }  
	
	private String generateId() {	return ++this.m_idIndex+"";  }
	
	
	public HAPDefinitionTaskSuite getTaskDefinitionSuite(String suiteName){		return this.m_taskDefinitionSuites.get(suiteName);	}
	
	public HAPDefinitionTask getTaskDefinition(String suite, String name) {		return this.getTaskDefinitionSuite(suite).getTask(name);	}

	public Set<String> getTaskDefinitionSuites() {		return this.m_taskDefinitionSuites.keySet();	}
	
	public void addTaskDefinitionSuite(HAPDefinitionTaskSuite expressionDefinitionSuite){
		this.m_taskDefinitionSuites.put(expressionDefinitionSuite.getName(), expressionDefinitionSuite);
	}
	
	
	
	
	
	
	
/*	
	
	
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
	
*/
	
/*	
	

	public HAPDefinitionTask newExpressionDefinition(String expression, String name,
			Map<String, HAPData> constants, Map<String, HAPDataTypeCriteria> variableCriterias) {
		HAPDefinitionTask expDefinition = new HAPTaskDefinitionSimple(expression, name, constants, null, null, null);
		expDefinition.setOperand(this.m_expressionProcessor.parseExpression(expression));
		return expDefinition;
	}

	public HAPDefinitionTaskSuite newTaskDefinitionSuite(String name) {
		return new HAPDefinitionTaskSuite();
	}
	*/
}
