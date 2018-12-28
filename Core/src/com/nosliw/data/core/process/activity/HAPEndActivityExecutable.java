package com.nosliw.data.core.process.activity;

import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;

public class HAPEndActivityExecutable extends HAPExecutableActivity{

	private String m_outputName;
	
	public HAPEndActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}
	
	public void setOutputName(String outputName) {
		this.m_outputName = outputName;
	}
}
