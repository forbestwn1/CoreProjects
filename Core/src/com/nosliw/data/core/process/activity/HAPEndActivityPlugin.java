package com.nosliw.data.core.process.activity;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPPluginActivity;
import com.nosliw.data.core.process.HAPProcessorActivity;

public class HAPEndActivityPlugin implements HAPPluginActivity{

	private HAPExpressionActivityProcessor m_processor;
	
	@Override
	public String getType() {		return HAPConstant.ACTIVITY_TYPE_END;	}

	@Override
	public HAPProcessorActivity getActivityProcessor() {		return this.m_processor;	}

	@Override
	public HAPDefinitionActivity buildActivityDefinition(Object obj) {
		HAPExpressionActivityDefinition out = new HAPExpressionActivityDefinition();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}

	@Override
	public String getScript() {
		// TODO Auto-generated method stub
		return null;
	}

}
