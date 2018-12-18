package com.nosliw.data.core.process.activity;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPPluginActivity;
import com.nosliw.data.core.process.HAPProcessorActivity;

public class HAPPluginActivityExpression implements HAPPluginActivity{

	private HAPProcessorActivityExpression m_processor;
	
	@Override
	public HAPProcessorActivity getActivityProcessor() {		return this.m_processor;	}

	@Override
	public HAPDefinitionActivity buildActivityDefinition(Object obj) {
		HAPDefinitionActivityExpression out = new HAPDefinitionActivityExpression();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}

}
