package com.nosliw.core.application.common.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

//branch in value structure tree
public class HAPElementStructureNode extends HAPElementStructureLeafVariable{

	@HAPAttribute
	public static final String CHILD = "child";
	
	//child node
	private Map<String, HAPElementStructure> m_children;
	
	public HAPElementStructureNode(){
		this.m_children = new LinkedHashMap<String, HAPElementStructure>();
	}
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE;	}

	public Map<String, HAPElementStructure> getChildren(){	return this.m_children;	}
	public void setChildren(Map<String, HAPElementStructure> children) {    this.m_children = children;    }
	
	public void addChild(String name, HAPElementStructure nodeBranch){		this.m_children.put(name, nodeBranch);	}

	@Override
	public void processed() {
		super.processed();
		for(HAPElementStructure child : this.m_children.values()) {
			child.processed();
		}
	}
	
	@Override
	public HAPElementStructure getChild(String childName) {   return this.m_children.get(childName);  }
	
//	@Override
//	public HAPContextDefinitionElement getSolidContextDefinitionElement() {
//		HAPContextDefinitionNode out = new HAPContextDefinitionNode();
//		super.toContextDefinitionElement(out);
//		for(String childName : this.m_children.keySet()) {
//			out.addChild(childName, this.m_children.get(childName).getSolidContextDefinitionElement());
//		}
//		return out;
//	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_children!=null) {
			jsonMap.put(CHILD, HAPUtilityJson.buildJson(m_children, HAPSerializationFormat.JSON));
		}
	}

	@Override
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureNode out = new HAPElementStructureNode();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		HAPElementStructureNode that = (HAPElementStructureNode)out;
		for(String name : this.m_children.keySet()) {
			that.addChild(name, this.m_children.get(name).cloneStructureElement());
		}
	}
}
