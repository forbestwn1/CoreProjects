package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Node in context definition
 * One node either 
 * 		has data type  -- a leaf
 *   or has children -- a branch
 * cannot be both
 */
@HAPEntityWithAttribute
public class HAPContextDefinitionLeafAbsolute extends HAPSerializableImp{

	@HAPAttribute
	public static final String DEFINITION  = "definition";

	@HAPAttribute
	public static final String CHILDREN  = "children";
	
	//child node
	private Map<String, HAPContextDefinitionLeafAbsolute> m_children;
	
	//context definition of that node (criteria)
	private HAPContextNodeCriteria m_definition;
	
	public HAPContextDefinitionLeafAbsolute(){
		this.m_children = new LinkedHashMap<String, HAPContextDefinitionLeafAbsolute>();
	}
	
	public void setDefinition(HAPContextNodeCriteria definition){	this.m_definition = definition;	}
	
	public HAPContextNodeCriteria getDefinition(){   return this.m_definition;  }
	
	public Map<String, HAPContextDefinitionLeafAbsolute> getChildren(){	return this.m_children;	}
	
	public void addChild(String name, HAPContextDefinitionLeafAbsolute nodeBranch){		this.m_children.put(name, nodeBranch);	}
	
	public void toContextNode(HAPContextDefinitionLeafAbsolute node) {
		if(this.m_definition!=null)		node.m_definition = this.m_definition.clone();
		for(String childName : this.m_children.keySet()) {
			HAPContextDefinitionLeafAbsolute newNode = new HAPContextDefinitionLeafAbsolute();
			this.m_children.get(childName).toContextNode(newNode);
			node.m_children.put(childName, newNode);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.m_children!=null)		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(m_children, HAPSerializationFormat.JSON));
		if(this.m_definition!=null)  	jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));
	}
}
