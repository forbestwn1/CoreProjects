package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;

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
		if(this.m_children!=null)		jsonMap.put(CHILD, HAPJsonUtility.buildJson(m_children, HAPSerializationFormat.JSON));
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
	
	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureNode solid = new HAPElementStructureNode();
		super.toStructureElement(solid);
		for(String name : this.getChildren().keySet()){
			String solidName = HAPUtilityScriptExpression.solidateLiterate(name, constants, runtimeEnv);
			HAPElementStructure child = this.getChildren().get(name);
			HAPElementStructure solidChild = (HAPElementStructure)child.solidateConstantScript(constants, runtimeEnv);
			solid.addChild(solidName, solidChild);
		}
		return solid;
	}
}
