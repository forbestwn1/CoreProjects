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
			
			if(loopStep.getBreakOperand()!=null) {
				HAPData exitData = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getBreakOperand(), parms, referencedData, m_runtime);
				if(((Boolean)exitData.getValue()).booleanValue())  break;
			}
		}
		
		if(eleOut!=null)  return HAPResultStep.createNextStepResult(eleOut, loopStep.getOutputVariable());
		else return HAPResultStep.createNullResult();
	}

//	@Override
	protected HAPResultStep executeStep1(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms,
		Map<String, HAPData> referencedData, HAPLogStep stepLog) {
		HAPExecutableStepLoop loopStep = (HAPExecutableStepLoop)step;
		
		//get container data
		HAPData containerData = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getContainerOperand(), parms, referencedData, m_runtime);
		
		Object[] aa = new Object[2];
		aa[1] = Integer.valueOf(0);
		HAPData eleOut = null;
		
		JSONArray elesJson =  (JSONArray)containerData.getValue();
		for(int i=0; i<elesJson.length(); i++) {
			this.bbb(elesJson, i, parms, loopStep, aa, stepLog);
		}
		
		while(((Integer)aa[1]).intValue()<elesJson.length()) {
			System.out.println(((Integer)aa[1]).intValue()+"  " + elesJson.length());
		}
		
		if(aa[0]!=null)  return HAPResultStep.createNextStepResult((HAPData)aa[0], loopStep.getOutputVariable());
		else return HAPResultStep.createNullResult();
	}

	private synchronized void incrementIndex(Object[] aa) {
		aa[1] = ((Integer)aa[1]) + 1;
	}
	
	private void bbb(JSONArray elesJson, int i, Map<String, HAPData> parms, HAPExecutableStepLoop loopStep, Object[] aa, HAPLogStep stepLog) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject eleJson = (JSONObject)elesJson.get(i);
				HAPData eleData = HAPDataUtility.buildDataWrapperFromJson(eleJson);

				Map<String, HAPData> eleParms = new LinkedHashMap<String, HAPData>();
				eleParms.putAll(parms);
				eleParms.put(loopStep.getElementVariable(), eleData);
				if(aa[0]!=null)   eleParms.put(loopStep.getOutputVariable(), (HAPData)aa[0]);
			
				HAPLogTask taskLog = new HAPLogTask();
				aa[0] = m_taskManager.executeTask(loopStep.getExecuteTask(), eleParms, new HAPTaskReferenceCache(), taskLog);
				stepLog.addChild(taskLog);
				incrementIndex(aa);
			}
			
		}).start();
	}
	
}
