package com.nosliw.data.core.handler;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public class HAPExecutableHandler extends HAPExecutableImpEntityInfo{

	private List<HAPExecutableHandlerStep> m_steps;
	
	public HAPExecutableHandler(HAPHandler handlerDef) {
		this.m_steps = new ArrayList<HAPExecutableHandlerStep>();
	}
	
	public void addStep(HAPExecutableHandlerStep step) {   this.m_steps.add(step);    }
	
}
