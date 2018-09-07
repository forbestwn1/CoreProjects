package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	private Map<String, HAPContextNodeRoot> m_elements;
	
	public HAPContext(){
		this.empty();
	}
	
	public void empty() {this.m_elements = new LinkedHashMap<String, HAPContextNodeRoot>();}
	
	public void addElement(String name, HAPContextNodeRoot rootEle){
		this.m_elements.put(name, rootEle);
	}

	public Set<String> getElementNames(){  return this.m_elements.keySet();   }
	public Map<String, HAPContextNodeRoot> getElements(){  return this.m_elements;  }
	public HAPContextNodeRoot getElement(String name) {  return this.m_elements.get(name);   }
	
	public void hardMergeWith(HAPContext context){
		Map<String, HAPContextNodeRoot> eles = context.getElements();
		for(String rootName : eles.keySet()){
			this.m_elements.put(rootName, eles.get(rootName));
		}
	}

	public HAPContextNode getChild(String rootName, String path){
		Object[] out = this.discoverChild(rootName, path);
		return (HAPContextNode)out[0];
	}

	public Map<String, Object> getConstantValue(){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		for(String name : this.m_elements.keySet()) {
			HAPContextNodeRoot rootNode = this.getElement(name);
			if(rootNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT)) {
				HAPContextNodeRootConstant constantRootNode = (HAPContextNodeRootConstant)rootNode;
				Object value = constantRootNode.getDataValue();
				if(value==null)   value = constantRootNode.getValue();
				out.put(name, value);
			}
		}
		return out;
	}
	
	public HAPContext getVariableContext() {
		HAPContext out = new HAPContext();
		for(String name : this.m_elements.keySet()) {
			HAPContextNodeRoot rootNode = this.getElement(name);
			if(rootNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE) || rootNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE)) {
				out.addElement(name, rootNode.cloneContextNodeRoot());
			}
		}
		return out;
	}
	
	//discover child node according to path
	//may not find exact match child node according to path
	//   return[0]   closest child node
	//   return[1]   remaining path
	public Object[] discoverChild(String rootName, String path){
		Object[] out = new Object[2];
		String remainingPath = null;
		HAPContextNode outNode = (HAPContextNode)this.m_elements.get(rootName);
		if(outNode!=null){
			String[] pathSegs = HAPNamingConversionUtility.parseComponentPaths(path);
			for(String pathSeg : pathSegs){
				if(remainingPath==null) {
					HAPContextNode node = outNode.getChildren().get(pathSeg);
					if(node==null) {
						remainingPath = pathSeg;
					}
					else {
						outNode = node;
					}
				}
				else {
					remainingPath = HAPNamingConversionUtility.cascadePath(remainingPath, pathSeg);
				}
			}
		}
		out[0] = outNode;
		out[1] = remainingPath;
		return out;
	}
	
	public HAPContext clone() {
		HAPContext out = new HAPContext();
		for(String name : this.m_elements.keySet()) {
			out.addElement(name, this.m_elements.get(name).cloneContextNodeRoot());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String rootName : this.m_elements.keySet()){
			jsonMap.put(rootName, this.m_elements.get(rootName).toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
