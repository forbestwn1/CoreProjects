package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPContextDefinitionNode extends HAPContextDefinitionLeafVariable{

	@HAPAttribute
	public static final String CHILD = "child";
	
	//child node
	private Map<String, HAPContextDefinitionElement> m_children;
	
	public HAPContextDefinitionNode(){
		this.m_children = new LinkedHashMap<String, HAPContextDefinitionElement>();
	}
	
	@Override
	public String getType() {	return HAPConstant.CONTEXT_ELEMENTTYPE_NODE;	}

	public Map<String, HAPContextDefinitionElement> getChildren(){	return this.m_children;	}
	
	public void addChild(String name, HAPContextDefinitionElement nodeBranch){		this.m_children.put(name, nodeBranch);	}

	@Override
	public HAPContextDefinitionElement getChild(String childName) {   return this.m_children.get(childName);  }
	
	@Override
	public HAPContextDefinitionElement getSolidContextDefinitionElement() {
		HAPContextDefinitionNode out = new HAPContextDefinitionNode();
		super.toContextDefinitionElement(out);
		for(String childName : this.m_children.keySet()) {
			this.addChild(childName, this.m_children.get(childName).getSolidContextDefinitionElement());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_children!=null)		jsonMap.put(CHILD, HAPJsonUtility.buildJson(m_children, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		HAPContextDefinitionNode out = new HAPContextDefinitionNode();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
		HAPContextDefinitionNode that = (HAPContextDefinitionNode)out;
		for(String name : this.m_children.keySet()) {
			that.addChild(name, this.m_children.get(name).cloneContextDefinitionElement());
		}
	}
	
	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextDefinitionNode solid = new HAPContextDefinitionNode();
		super.toContextDefinitionElement(solid);
		for(String name : this.getChildren().keySet()){
			String solidName = HAPProcessorContextSolidate.getSolidName(name, constants, contextProcessorEnv);
			HAPContextDefinitionElement child = this.getChildren().get(name);
			HAPContextDefinitionElement solidChild = child.toSolidContextDefinitionElement(constants, contextProcessorEnv);
			solid.addChild(solidName, solidChild);
		}
		return solid;
	}
}
