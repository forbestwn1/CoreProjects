package com.nosliw.uiresource.tag;

import java.util.Map;

public class HAPUITagDefinition {

	private String m_name;
	
	//javascript
	private String m_script;

	//attribute definition
	private Map<String, HAPUITagDefinitionAttribute> m_attributes;
	
	//whether interit the context from parent
	private boolean m_inheritContext;
	
	
	//scriptExpression : criteria
	Map<String, HAPUITagDefinitionContextElment> m_contexts;
	
	public HAPUITagDefinition(String name, String script){
		this.m_name = name;
		this.m_script = script;
	}
	
	public String getName(){return this.m_name;}
	
	public String getScript(){return this.m_script;}
	
	public boolean isInheritContext(){  return this.m_inheritContext;  }
	
	public Map<String, HAPUITagDefinitionContextElment> getContextDefinitions(){
		return this.m_contexts;
	}

}
