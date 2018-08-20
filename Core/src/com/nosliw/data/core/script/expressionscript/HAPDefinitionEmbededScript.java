package com.nosliw.data.core.script.expressionscript;

public class HAPDefinitionEmbededScript {

	//id of script expression
	private String m_id;
	
	//definition literate
	private String m_definition;

	public HAPDefinitionEmbededScript(String id, String definition) {
		this.m_id = id;
		this.m_definition = definition;
	}
	
	public String getId() {  return this.m_id;   }
	
	public String getDefinition() {   return this.m_definition;  }
}
