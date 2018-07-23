package com.nosliw.data.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

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

	public Map<String, HAPContextNodeRoot> getElements(){  return this.m_elements;  }
	public HAPContextNodeRoot getElement(String name) {  return this.m_elements.get(name);   }
	
	public void hardMergeWith(HAPContext context){
		Map<String, HAPContextNodeRoot> eles = context.getElements();
		for(String rootName : eles.keySet()){
			this.m_elements.put(rootName, eles.get(rootName));
		}
	}

	public HAPContextNode getChild(HAPContextPath path){
		Object[] out = this.discoverChild(path);
		return (HAPContextNode)out[0];
	}

	//discover child node according to path
	//may not find exact match child node according to path
	//   return[0]   closest child node
	//   return[1]   remaining path
	public Object[] discoverChild(HAPContextPath path){
		Object[] out = new Object[2];
		String remainingPath = null;
		HAPContextNode outNode = (HAPContextNode)this.m_elements.get(path.getRootElementName());
		if(outNode!=null){
			String[] pathSegs = path.getPathSegments();
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
					remainingPath = remainingPath + "." + pathSeg;
				}
			}
		}
		out[0] = outNode;
		out[1] = remainingPath;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String rootName : this.m_elements.keySet()){
			jsonMap.put(rootName, this.m_elements.get(rootName).toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
