package com.nosliw.core.resource.dynamic;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.core.resource.HAPResourceDefinition1;

public class HAPOutputBuilder {

	private HAPResourceDefinition1 m_resourceDef;
	
	private Set<HAPParmDefinition> m_parmsInfo;
	
	public HAPOutputBuilder() {
		this.m_parmsInfo = new HashSet<HAPParmDefinition>();
	}
	
	public void setResourceDefinition(HAPResourceDefinition1 resourceDef) {    this.m_resourceDef = resourceDef;     }
	public HAPResourceDefinition1 getResourceDefinition() {    return this.m_resourceDef;    }
	
	public void addParmInfo(HAPParmDefinition parmDef) {   this.m_parmsInfo.add(parmDef);    }
	public Set<HAPParmDefinition> getParmsInfo(){  return this.m_parmsInfo;   }
}
