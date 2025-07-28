package com.nosliw.data.core.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskImp;

@HAPEntityWithAttribute
public class HAPDefinitionSequence extends HAPDefinitionTaskImp{

	@HAPAttribute
	public static String STEP = "step";

	private List<HAPDefinitionTask> m_steps;
	
	public HAPDefinitionSequence() {
		super(HAPConstantShared.TASK_TYPE_SEQUENCE);
		this.m_steps = new ArrayList<HAPDefinitionTask>();
	}

	public void addStep(HAPDefinitionTask step) {   
		if(step!=null)  this.m_steps.add(step);    
	}
	
	public List<HAPDefinitionTask> getSteps(){    return this.m_steps;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STEP, HAPUtilityJson.buildJson(this.m_steps, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}
	
	@Override
	public HAPDefinitionTask cloneTaskDefinition() {
		HAPDefinitionSequence out = new HAPDefinitionSequence();
		for(HAPDefinitionTask step : this.m_steps) {
			out.addStep(step.cloneTaskDefinition());
		}
		return out;
	}
}
