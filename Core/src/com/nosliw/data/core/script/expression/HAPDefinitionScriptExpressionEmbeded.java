package com.nosliw.data.core.script.expression;

//script expression definition
//it is used in embeded
public class HAPDefinitionScriptExpressionEmbeded {

	//definition literate
	private String m_definition;

	public HAPDefinitionScriptExpressionEmbeded(String definition) {
		this.m_definition = definition;
	}
	
	public String getDefinition() {   return this.m_definition;  }
}
