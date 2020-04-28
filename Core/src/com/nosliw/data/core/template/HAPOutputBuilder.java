package com.nosliw.data.core.template;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPOutputBuilder {

	private HAPResourceDefinition m_resourceDef;
	
	private Set<HAPParmDefinition> m_parmsInfo;
	
	public HAPOutputBuilder() {
		this.m_parmsInfo = new HashSet<HAPParmDefinition>();
	}
	
	public void setResourceDefinition(HAPResourceDefinition resourceDef) {    this.m_resourceDef = resourceDef;     }
	public HAPResourceDefinition getResourceDefinition() {    return this.m_resourceDef;    }
	
	public void addParmInfo(HAPParmDefinition parmDef) {   this.m_parmsInfo.add(parmDef);    }
	
}
