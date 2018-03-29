package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;

public class HAPExecutorStepLoop implements HAPExecutorStep{

	private HAPRuntime m_runtime;

	public HAPExecutorStepLoop(HAPRuntime runtime) {
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPResultStep execute(HAPExecutableStep step, Map<String, HAPData> parms,
			Map<String, HAPData> referencedData) {

		HAPExecutableStepLoop loopStep = (HAPExecutableStepLoop)step;
		
		//get container data
		HAPData containerData = HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getContainerOperand(), parms, m_runtime);
		
		//loop through element
		JSONArray elesJson =  (JSONArray)containerData.getValue();
		for(int i=0; i<elesJson.length(); i++) {
			JSONObject eleJson = (JSONObject)elesJson.get(i);
			HAPData eleData = HAPDataUtility.buildDataWrapperFromJson(eleJson);
			Map<String, HAPData> eleParms = new LinkedHashMap<String, HAPData>(); 
			eleParms.putAll(parms);
			eleParms.put(loopStep.getElementVariable(), eleData);
			HAPRhinoRuntimeUtility.executeOperandSync(loopStep.getExecuteOperand(), eleParms, m_runtime);
		}
		
		HAPResultStep out = HAPResultStep.createNullResult();
		return out;
	}
}
