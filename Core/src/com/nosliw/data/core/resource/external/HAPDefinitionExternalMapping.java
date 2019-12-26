package com.nosliw.data.core.resource.external;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSupplement;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

//define what external resource depend on
@HAPEntityWithAttribute
public class HAPDefinitionExternalMapping extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, Map<String, HAPDefinitionExternalMappingEle>> m_element;
	
	public HAPDefinitionExternalMapping() {
		this.m_element = new LinkedHashMap<>();
	}

	public HAPDefinitionExternalMapping(HAPResourceIdSupplement resourceIdSupplement) {
		this();
		if(resourceIdSupplement!=null) {
			Map<String, Map<String, HAPResourceId>> resourceIds = resourceIdSupplement.getAllSupplymentResourceId();
			for(String type : resourceIds.keySet()) {
				Map<String, HAPResourceId> byName = resourceIds.get(type);
				for(String name : byName.keySet()) {
					HAPDefinitionExternalMappingEle ele = new HAPDefinitionExternalMappingEle(type);
					ele.setName(name);
					ele.setId(byName.get(name));
					this.addElement(type, ele);
				}
			}
		}
	}
	
	public Map<String, HAPDefinitionExternalMappingEle> getMappingByType(String type){
		return this.m_element.get(type);
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
	public void merge(HAPDefinitionExternalMapping parent, String mode) {
		if(mode==null)   mode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE))  return;
		
		for(String type : parent.m_element.keySet()) {
			Map<String, HAPDefinitionExternalMappingEle> byName = parent.m_element.get(type);
			for(String name : byName.keySet()) {
				HAPDefinitionExternalMappingEle parentEle = byName.get(name);
				HAPDefinitionExternalMappingEle thisEle = this.getElement(type, name);
				if(thisEle==null) {
					//element not exist, create one
					HAPDefinitionExternalMappingEle newEle = parentEle.clone();
					HAPExternalMappingUtility.setOverridenByParent(newEle);
					this.addElement(type, newEle);
				}
				else if(thisEle.getId()==null) {
					//element not have resource id info, then use from parent
					HAPExternalMappingUtility.setOverridenByParent(thisEle);
					thisEle.setId(parentEle.getId());  
				}
				else {
					if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT)&&HAPExternalMappingUtility.isOverridenByParentMode(thisEle)) {
						//if configurable, then parent override child
						HAPDefinitionExternalMappingEle newEle = parentEle.clone();
						this.addElement(type, newEle);
					}
				}
			}
		}
	}

	public HAPResourceIdSupplement toResourceIdSupplement(){
		Map<String, Map<String, HAPResourceId>> resourceIds = new LinkedHashMap<String, Map<String, HAPResourceId>>();
		for(String type : this.m_element.keySet()) {
			Map<String, HAPDefinitionExternalMappingEle> byName = this.m_element.get(type);
			Map<String, HAPResourceId> byNameOut = new LinkedHashMap<>();
			for(String name : byName.keySet()) {
				byNameOut.put(name, byName.get(name).getId());
			}
			resourceIds.put(type, byNameOut);
		}
		return HAPResourceIdSupplement.newInstance(resourceIds);
	}
	
	public HAPDefinitionExternalMappingEle getElement(String type, String name) {
		HAPDefinitionExternalMappingEle out = null;
		Map<String, HAPDefinitionExternalMappingEle> byName = this.m_element.get(type);
		if(byName!=null) {
			out = byName.get(name);
		}
		return out;
	}
}
