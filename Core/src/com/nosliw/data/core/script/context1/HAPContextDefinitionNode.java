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
public class HAPContextDefinitionNode extends HAPSerializableImp{

	@HAPAttribute
	public static final String CHILDREN  = "children";
	
	//child node
	private Map<String, HAPContextDefinitionElement> m_children;
	
	public HAPContextDefinitionNode(){
		this.m_children = new LinkedHashMap<String, HAPContextDefinitionElement>();
	}
	
	public Map<String, HAPContextDefinitionElement> getChildren(){	return this.m_children;	}
	
	public void addChild(String name, HAPContextDefinitionElement nodeBranch){		this.m_children.put(name, nodeBranch);	}
	
	public void toContextNode(HAPContextDefinitionElement node) {
		for(String childName : this.m_children.keySet()) {
			HAPContextDefinitionNode newNode = new HAPContextDefinitionNode();
			this.m_children.get(childName).toContextNode(newNode);
			node.m_children.put(childName, newNode);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.m_children!=null)		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(m_children, HAPSerializationFormat.JSON));
	}
}
