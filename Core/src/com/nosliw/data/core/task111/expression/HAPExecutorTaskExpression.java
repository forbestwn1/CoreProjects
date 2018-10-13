package com.nosliw.data.core.task111.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task111.HAPExecutableTask;
import com.nosliw.data.core.task111.HAPExecutorTaskImp;
import com.nosliw.data.core.task111.HAPLogTask;
import com.nosliw.data.core.task111.HAPManagerTask;
import com.nosliw.data.core.task111.HAPTaskReferenceCache;

public class HAPExecutorTaskExpression extends HAPExecutorTaskImp{

	private HAPManagerTaskExpression m_expTaskManager;
	
	private HAPManagerTask m_taskManager;
	
	public HAPExecutorTaskExpression(HAPManagerTaskExpression expTaskManager, HAPManagerTask taskManager) {
		this.m_expTaskManager = expTaskManager;
		this.m_taskManager = taskManager;
	}
	
	@Override
	public HAPData executeTask(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache, HAPLogTask logger) {
		HAPExecutableTaskExpression expTask = (HAPExecutableTaskExpression)task;
		
		Map<String, HAPData> executeParms = new LinkedHashMap<String, HAPData>();
		executeParms.putAll(parms);
		HAPData out = null;
		boolean exit = false;
		HAPExecutableStep step = expTask.getStep(0);
		while(!exit) {
			//execute referenced executable
			Map<String, HAPData> referencesData = new LinkedHashMap<String, HAPData>();
			if(step.getReferences()!=null) {
				for(String refName : step.getReferences()) {
					Map<String, HAPData> cacheSearchParms = new LinkedHashMap<String, HAPData>();
					for(String varName : expTask.getReferencedExecute().get(refName).getVariables()) 	cacheSearchParms.put(varName, parms.get(varName));
					HAPData cachedResult = cache.getResult(refName, cacheSearchParms);
					if(cachedResult==null) {
						//not find from cache, execute the referenced executable
						HAPLogTask refTaskLog = new HAPLogTask();
						cachedResult = this.m_taskManager.executeTask(expTask.getReferencedExecute().get(refName), cacheSearchParms, cache, refTaskLog);
						logger.addChild(refTaskLog);
						cache.addResult(refName, cachedResult, cacheSearchParms);
					}
					referencesData.put(refName, cachedResult);
				}
			}
			
			HAPResultStep stepResult = m_expTaskManager.getStepManager(step.getType()).getStepExecutor().execute(step, expTask, executeParms, referencesData, logger);
			if(stepResult.isExit()) {
				//exit
				out = stepResult.getData();
				exit = true;
			}
			else {
				//handle variable output
				String varName = stepResult.getVariableName();
				if(HAPBasicUtility.isStringNotEmpty(varName)) {
					executeParms.put(varName, stepResult.getData());
				}
				//next step
				String nextStep = stepResult.getNext();
				if(HAPBasicUtility.isStringNotEmpty(nextStep)) {
					step = expTask.getStep(nextStep);
				}
				else {
					//otherwise, move to next in sequence
					step = expTask.getStep(step.getIndex()+1);
					if(step==null) {
						//it is the last one
						out = stepResult.getData();
						exit = true;
					}
				}
			}
		}
		return out;
	}
}
