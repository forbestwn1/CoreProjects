package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPTaskFlowDecisionJS extends HAPSerializableImp implements HAPTaskFlowDecision{

	@HAPAttribute
	public static final String SCRIPT = "script";

	private HAPJsonTypeScript m_script;
	
	@Override
	public String getType() {
		return HAPConstantShared.FLOW_DECISION_TYPE_JAVASCRIPT;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		
		
		
		return true;  
	}

	
}
