package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPGroupValuePorts extends HAPEntityInfoImp{

	private Map<String, HAPWrapperValuePort> m_valuePortByName;
	
	private String m_inDefaultName;

	private String m_outDefaultName;
	
	private String m_bothDefaultName;

	private int m_idIndex = 0;
	
	public HAPGroupValuePorts() {
		this.m_valuePortByName = new LinkedHashMap<String, HAPWrapperValuePort>();
	}
	
	public void addValuePort(HAPWrapperValuePort valuePort, boolean isDefault) {
		String name = valuePort.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.m_idIndex + "";
			this.m_idIndex++;
			valuePort.setName(name);
		}
		this.m_valuePortByName.put(name, valuePort);
		if(isDefault) {
			String ioDirection = valuePort.getValuePort().getValuePortInfo().getIODirection();
			if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_IN)) {
				this.m_inDefaultName = name;
			}
			else if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_OUT)) {
				this.m_outDefaultName = name;
			}
			else if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_BOTH)) {
				this.m_bothDefaultName = name;
			}
		}
	}
	
	public Set<HAPWrapperValuePort> getValuePorts(){
		return new HashSet<HAPWrapperValuePort>(this.m_valuePortByName.values());
	}

	public HAPWrapperValuePort getValuePort(String name) {
		return this.m_valuePortByName.get(name);
	}
	
	public HAPWrapperValuePort getValuePort(String name, String ioDirection) {
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.getDefaultValuePortName(ioDirection);
		}
		return this.getValuePort(name);
	}
	
	public String getDefaultValuePortName(String ioDirection) {
		if(ioDirection==null) {
			ioDirection = HAPConstantShared.IO_DIRECTION_BOTH;
		}
		String out = null;
		//find specific first
		if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_IN)) {
			out = this.m_inDefaultName;
		}
		else if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_OUT)) {
			out = this.m_outDefaultName;
		}
		else if(ioDirection.equals(HAPConstantShared.IO_DIRECTION_BOTH)) {
			out = this.m_bothDefaultName;
		}
		
		//if no specific, then fall back to both
		if(out==null) {
			out = this.m_bothDefaultName;
		}
		return out;
	}
}
