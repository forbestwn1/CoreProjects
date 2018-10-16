package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTS = "elements";

	@HAPAttribute
	public static final String INFO = "info";
	
	private Map<String, HAPContextNodeRoot> m_elements;
	
	private HAPInfo m_info;
	
	public HAPContext(){
		this.empty();
	}
	
	public void empty() {
		this.m_elements = new LinkedHashMap<String, HAPContextNodeRoot>();
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPInfo getInfo() {   return this.m_info;   }
	
	public Set<String> getElementNames(){  return this.m_elements.keySet();   }
	public Map<String, HAPContextNodeRoot> getElements(){  return this.m_elements;  }
	public HAPContextNodeRoot getElement(String name) {  return this.m_elements.get(name);   }
	public void addElement(String name, HAPContextNodeRoot rootEle){	this.m_elements.put(name, rootEle);	}
	
	public void hardMergeWith(HAPContext context){
		Map<String, HAPContextNodeRoot> eles = context.getElements();
		for(String rootName : eles.keySet()){
			this.m_elements.put(rootName, eles.get(rootName));
		}
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
	//   return[0]   base node
	//   return[1]   closest child node
	//   return[2]   remaining path
	public void discoverChild(String rootName, String path, HAPInfoRelativeContextResolve resolved){
		HAPContextNodeRoot rootNodeNode = this.m_elements.get(rootName);
		resolved.rootNode = rootNodeNode;
		if(rootNodeNode!=null) {
			switch(rootNodeNode.getType()) {
			case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
			case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
				HAPContextNode outNode = (HAPContextNode)rootNodeNode;
				String remainingPath = null;
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
				resolved.referedNode = outNode;
				resolved.remainPath = remainingPath;
				break;
			case HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT:
				resolved.referedNode = null;
				resolved.remainPath = path;
				break;
			}
		}
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
