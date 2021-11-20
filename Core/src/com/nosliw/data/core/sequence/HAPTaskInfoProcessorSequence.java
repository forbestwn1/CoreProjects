package com.nosliw.data.core.sequence;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPProcessorTask;

public class HAPTaskInfoProcessorSequence implements HAPProcessorTask{

	private HAPManagerTask m_taskMan;

	public HAPTaskInfoProcessorSequence(HAPManagerTask taskMan) {
		this.m_taskMan = taskMan;
	}
	
	@Override
	public HAPExecutableTask process(
			HAPDefinitionTask taskDefinition, 
			String id,
			HAPContextProcessor processContext, 
			HAPWrapperValueStructure valueStructureWrapper,
			HAPProcessTracker processTracker) {
		HAPDefinitionSequence sequenceDef = (HAPDefinitionSequence)taskDefinition;
		HAPExecutableSequence out = new HAPExecutableSequence();
		taskDefinition.cloneToEntityInfo(out);
		int i = 0;
		for(HAPDefinitionTask step : sequenceDef.getSteps()) {
			 out.addStep(m_taskMan.getTaskInfo(step.getTaskType()).getProcessor().process(step, id+"_"+i, processContext, valueStructureWrapper, processTracker));
			i++;
		}
		return out;
	}

}
