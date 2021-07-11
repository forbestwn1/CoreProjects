package com.nosliw.data.core.sequence;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.task.HAPExecutableTask;

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

}
