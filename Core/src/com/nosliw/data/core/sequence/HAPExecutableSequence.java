package com.nosliw.data.core.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.task.HAPExecutableTask;

@HAPEntityWithAttribute
public class HAPExecutableSequence extends HAPExecutableTask{

	@HAPAttribute
	public static String STEP = "step";

	private List<HAPExecutableTask> m_steps;
	
	public HAPExecutableSequence() {
		super(HAPConstantShared.TASK_TYPE_SEQUENCE);
		this.m_steps = new ArrayList<HAPExecutableTask>();
	}
	
	public void addStep(HAPExecutableTask step) {   this.m_steps.add(step);    }
	public List<HAPExecutableTask> getSteps(){    return this.m_steps;      }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STEP, HAPJsonUtility.buildJson(this.m_steps, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);

		List<String> arrayJson = new ArrayList<String>();
		for(HAPExecutableTask step : this.m_steps) {
			arrayJson.add(step.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(STEP, HAPJsonUtility.buildArrayJson(arrayJson.toArray(new String[0])));
	}	

}
