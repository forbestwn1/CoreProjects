package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementNode extends HAPElementLeafVariable{

	@HAPAttribute
	public static final String CHILD = "child";
	
	//child node
	private Map<String, HAPElement> m_children;
	
	public HAPElementNode(){
		this.m_children = new LinkedHashMap<String, HAPElement>();
	}
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE;	}

	public Map<String, HAPElement> getChildren(){	return this.m_children;	}
	
	public void addChild(String name, HAPElement nodeBranch){		this.m_children.put(name, nodeBranch);	}

	@Override
	public void processed() {
		super.processed();
		for(HAPElement child : this.m_children.values()) {
			child.processed();
		}
	}
	
	@Override
	public HAPElement getChild(String childName) {   return this.m_children.get(childName);  }
	
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
	public HAPElement cloneStructureElement() {
		HAPElementNode out = new HAPElementNode();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public void toStructureElement(HAPElement out) {
		super.toStructureElement(out);
		HAPElementNode that = (HAPElementNode)out;
		for(String name : this.m_children.keySet()) {
			that.addChild(name, this.m_children.get(name).cloneStructureElement());
		}
	}
	
	@Override
	public HAPElement toSolidStructureElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementNode solid = new HAPElementNode();
		super.toStructureElement(solid);
		for(String name : this.getChildren().keySet()){
			String solidName = HAPProcessorContextSolidate.getSolidName(name, constants, runtimeEnv);
			HAPElement child = this.getChildren().get(name);
			HAPElement solidChild = child.toSolidStructureElement(constants, runtimeEnv);
			solid.addChild(solidName, solidChild);
		}
		return solid;
	}
}
