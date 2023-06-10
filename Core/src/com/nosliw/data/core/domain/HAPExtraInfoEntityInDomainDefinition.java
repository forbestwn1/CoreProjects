package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPExtraInfoEntityInDomainDefinition extends HAPEntityInfoImp{

	//alias for this entity
	private Set<String> m_alias;
	
	//for id from external system
	private String m_globalId;

	public HAPExtraInfoEntityInDomainDefinition() {
		this.m_alias = new HashSet<String>();
	}

	public String getGlobalId() {    return this.m_globalId;    }
	public void setGlobalId(String globalId) {  this.m_globalId = globalId;   }
	
	public Set<String> getAlias(){    return this.m_alias;    }
	
	public HAPExtraInfoEntityInDomainDefinition cloneExtraInfo() {
		HAPExtraInfoEntityInDomainDefinition out = new HAPExtraInfoEntityInDomainDefinition();
		this.cloneToEntityInfo(out);
		out.m_globalId = this.m_globalId;
		out.m_alias.addAll(this.m_alias);
		return out;
	}
}
