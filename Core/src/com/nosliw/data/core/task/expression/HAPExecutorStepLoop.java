package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPTaskReferenceCache;

public class HAPExecutorStepLoop implements HAPExecutorStep{

	private HAPRuntime m_runtime;

	private HAPManagerTask m_taskManager;
	
	public HAPExecutorStepLoop(HAPRuntime runtime, HAPManagerTask taskManager) {
		this.m_runtime = runtime;
		this.m_taskManager = taskManager;
	}
	
	@Override
	public HAPResultStep execute(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms,
			Map<String, HAPData> referencedData) {

		HAPExecutableStepLoop loopStep = (HAPExecutableStepLoop)step;
		
		//get container data
		HAPData containerData = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getContainerOperand(), parms, referencedData, m_runtime);
		
		//loop through element
		Set<String> eleRefs = HAPOperandUtility.discoverReferences(loopStep.getExecuteOperand());
		
		HAPData eleOut = null;
		
		JSONArray elesJson =  (JSONArray)containerData.getValue();
		for(int i=0; i<elesJson.length(); i++) {
			JSONObject eleJson = (JSONObject)elesJson.get(i);
			HAPData eleData = HAPDataUtility.buildDataWrapperFromJson(eleJson);

			Map<String, HAPData> eleParms = new LinkedHashMap<String, HAPData>();
			eleParms.putAll(parms);
			eleParms.put(loopStep.getElementVariable(), eleData);
			if(eleOut!=null)   eleParms.put(loopStep.getOutputVariable(), eleOut);
			
			Map<String, HAPData> eleRefDatas = new LinkedHashMap<String, HAPData>(); 
			eleRefDatas.putAll(referencedData);
			for(String eleRefName : eleRefs) {
				HAPData eleRefData = this.m_taskManager.executeTask(task.getReferencedExecute().get(eleRefName), eleParms, new HAPTaskReferenceCache());
				eleRefDatas.put(eleRefName, eleRefData);
			}
			
			eleOut = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getExecuteOperand(), eleParms, eleRefDatas, m_runtime);
		}
		
		if(eleOut!=null)  return HAPResultStep.createNextStepResult(eleOut, loopStep.getOutputVariable());
		else return HAPResultStep.createNullResult();
	}
}
