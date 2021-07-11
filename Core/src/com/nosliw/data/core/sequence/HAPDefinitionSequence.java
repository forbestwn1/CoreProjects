package com.nosliw.data.core.sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.task.HAPDefinitionTask;

@HAPEntityWithAttribute
public class HAPDefinitionSequence extends HAPEntityInfoWritableImp implements HAPDefinitionTask{

	@HAPAttribute
	public static String STEP = "step";

	private List<HAPDefinitionTask> m_steps;
	
	public HAPDefinitionSequence() {
		this.m_steps = new ArrayList<HAPDefinitionTask>();
	}

	@Override
	public String getTaskType() {   return HAPConstantShared.TASK_TYPE_SEQUENCE;  }

	public void addStep(HAPDefinitionTask step) {   this.m_steps.add(step);    }
	
	public List<HAPDefinitionTask> getSteps(){    return this.m_steps;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public HAPDefinitionTask cloneTaskDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
}
