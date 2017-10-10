package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;

public class HAPContextNode {

	@HAPAttribute
	public static final String DEFINITION  = "definition";

	@HAPAttribute
	public static final String CHILDREN  = "children";
	
	private Map<String, HAPContextNode> m_children;
	
	//context definition of that node (criteria)
	private HAPContextNodeDefinition m_definition;
	
	public HAPContextNode(){
		this.m_children = new LinkedHashMap<String, HAPContextNode>();
	}
	
	public void setDefinition(HAPContextNodeDefinition definition){	this.m_definition = definition;	}
	
	public HAPContextNodeDefinition getDefinition(){   return this.m_definition;  }
	
	public Map<String, HAPContextNode> getChildren(){	return this.m_children;	}
	
	public void addChild(String name, HAPContextNode nodeBranch){		this.m_children.put(name, nodeBranch);	}
	
}
