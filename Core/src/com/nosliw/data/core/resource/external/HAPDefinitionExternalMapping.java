package com.nosliw.data.core.resource.external;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

//define what external resource depend on
@HAPEntityWithAttribute
public class HAPDefinitionExternalMapping extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, Map<String, HAPDefinitionExternalMappingEle>> m_element;
	
	public HAPDefinitionExternalMapping() {
		this.m_element = new LinkedHashMap<>();
	}
	
	public void addElement(String type, HAPDefinitionExternalMappingEle ele) {
		Map<String, HAPDefinitionExternalMappingEle> byName = this.m_element.get(type);
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPDefinitionExternalMappingEle>();
			this.m_element.put(type, byName);
		}
		byName.put(ele.getName(), ele);
	}

	//merge with parent
	public void merge(HAPDefinitionExternalMapping parent) {
		for(String type : parent.m_element.keySet()) {
			Map<String, HAPDefinitionExternalMappingEle> byName = parent.m_element.get(type);
			for(String name : byName.keySet()) {
				HAPDefinitionExternalMappingEle parentEle = byName.get(name);
				HAPDefinitionExternalMappingEle thisEle = this.getElement(type, name);
				if(thisEle==null)   this.addElement(type, parentEle.clone());      //element not exist, create one 
				else if(thisEle.getId()==null)  thisEle.setId(parentEle.getId());  //element not have resource id info, then use from parent
				else {
					if(HAPExternalMappingUtility.isOverridenByParent(thisEle)) {   //if configurable, then parent override child
						this.addElement(type, parentEle.clone());
					}
				}
			}
		}
	}
	
	private HAPDefinitionExternalMappingEle getElement(String type, String name) {
		HAPDefinitionExternalMappingEle out = null;
		Map<String, HAPDefinitionExternalMappingEle> byName = this.m_element.get(type);
		if(byName!=null) {
			out = byName.get(name);
		}
		return out;
	}
	
}
