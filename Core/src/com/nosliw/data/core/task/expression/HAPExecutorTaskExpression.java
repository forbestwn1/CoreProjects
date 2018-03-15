package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPExecutorTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPTaskReferenceCache;

public class HAPExecutorTaskExpression implements HAPExecutorTask{

	private HAPManagerTaskExpression m_expTaskManager;
	
	private HAPManagerTask m_taskManager;
	
	@Override
	public HAPData execute(HAPExecutableTask task, Map<String, HAPData> parms, HAPTaskReferenceCache cache) {
		HAPExecutableTaskExpression expTask = (HAPExecutableTaskExpression)task;
		
		Map<String, HAPData> executeParms = new LinkedHashMap<String, HAPData>();
		executeParms.putAll(parms);
		HAPData out = null;
		boolean exit = false;
		HAPExecutableStep step = expTask.getStep(0);
		while(!exit) {
			//execute referenced executable
			Map<String, HAPData> referencesData = new LinkedHashMap<String, HAPData>();
			for(String refName : step.getReferences()) {
				Map<String, HAPData> cacheSearchParms = new LinkedHashMap<String, HAPData>();
				for(String varName : step.getVariables()) 	cacheSearchParms.put(varName, parms.get(varName));
				HAPData cachedResult = cache.getResult(refName, cacheSearchParms);
				if(cachedResult==null) {
					//not find from cache, execute the referenced executable
					cachedResult = this.m_taskManager.executeTask(expTask.getReferencedExecute().get(refName), cacheSearchParms, cache);
					cache.addResult(refName, cachedResult, cacheSearchParms);
				}
				referencesData.put(refName, cachedResult);
			}
			
			HAPResultStep stepResult = m_expTaskManager.executeStep(step, executeParms, referencesData);
			if(stepResult.isExit()) {
				//exit
				out = stepResult.getData();
				exit = true;
			}
			else {
				//handle variable output
				String varName = stepResult.getVariableName();
				if(HAPBasicUtility.isStringNotEmpty(varName)) {
					parms.put(varName, stepResult.getData());
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
