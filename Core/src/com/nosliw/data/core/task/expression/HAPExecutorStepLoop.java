package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;
import com.nosliw.data.core.task.HAPLogTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPTaskReferenceCache;

public class HAPExecutorStepLoop extends HAPExecutorStepImp{

	private HAPRuntime m_runtime;

	private HAPManagerTask m_taskManager;
	
	public HAPExecutorStepLoop(HAPRuntime runtime, HAPManagerTask taskManager) {
		this.m_runtime = runtime;
		this.m_taskManager = taskManager;
	}
	
	@Override
	protected HAPResultStep executeStep(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms,
		Map<String, HAPData> referencedData, HAPLogStep stepLog) {
		HAPExecutableStepLoop loopStep = (HAPExecutableStepLoop)step;
		
		//get container data
		HAPData containerData = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getContainerOperand(), parms, referencedData, m_runtime);
		
		HAPData eleOut = null;
		
		JSONArray elesJson =  (JSONArray)containerData.getValue();
		for(int i=0; i<elesJson.length(); i++) {
			JSONObject eleJson = (JSONObject)elesJson.get(i);
			HAPData eleData = HAPDataUtility.buildDataWrapperFromJson(eleJson);

			Map<String, HAPData> eleParms = new LinkedHashMap<String, HAPData>();
			eleParms.putAll(parms);
			eleParms.put(loopStep.getElementVariable(), eleData);
			if(eleOut!=null)   eleParms.put(loopStep.getOutputVariable(), eleOut);
		
			HAPLogTask taskLog = new HAPLogTask();
			eleOut = this.m_taskManager.executeTask(loopStep.getExecuteTask(), eleParms, new HAPTaskReferenceCache(), taskLog);
			stepLog.addChild(taskLog);
		}
		
		if(eleOut!=null)  return HAPResultStep.createNextStepResult(eleOut, loopStep.getOutputVariable());
		else return HAPResultStep.createNullResult();
	}
}
