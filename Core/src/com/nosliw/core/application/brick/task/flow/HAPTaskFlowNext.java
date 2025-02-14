package com.nosliw.core.application.brick.task.flow;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPTaskFlowNext extends HAPSerializableImp{

	@HAPAttribute
	public static final String DECISION = "decision";

	@HAPAttribute
	public static final String TARGET = "target";

	private HAPTaskFlowDecision m_decision;

	private Map<String, HAPTaskFlowTarget> m_target;
	
}
