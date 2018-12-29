package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.runtime.HAPResourceDependent;

public class HAPEndActivityExecutable extends HAPExecutableActivity{

	private String m_outputName;
	
	public HAPEndActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}
	
	public void setOutputName(String outputName) {
		this.m_outputName = outputName;
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency() {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(HAPConstant.ACTIVITY_TYPE_END)));
		return out;
	}

}
