package com.nosliw.uiresource.tag;

import java.util.Map;
import java.util.Set;

public class HAPUITagDefinition {

	private String m_name;
	
	//javascript
	private String m_script;

	//attribute definition
	private Map<String, HAPTagAttributeDefinition> m_attributes;
	
	//whether interit the context from parent
	private boolean m_inheritContext;
	
	
	//scriptExpression : criteria
	Set<HAPUITagContextElment> m_contexts;
	
	public HAPUITagDefinition(String name, String script){
		this.m_name = name;
		this.m_script = script;
	}
	
	public String getName(){return this.m_name;}
	
	public String getScript(){return this.m_script;}
	
	public boolean isInheritContext(){  return this.m_inheritContext;  }
	
	public Set<HAPUITagContextElment> getContextDefinitions(){
		return this.m_contexts;
	}

}
