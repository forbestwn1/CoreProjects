package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public abstract class HAPResourceDefinitionContainer <T extends HAPResourceDefinitionContainerElement> extends HAPResourceDefinitionComplexImp {
 
	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, T> m_elements;

	public HAPResourceDefinitionContainer() {
		this.m_elements = new LinkedHashMap<String, T>();
	}
	
	public T getContainerElement(String id) {     return this.m_elements.get(id);     }
	
	public Set<T> getContainerElements(){    return new HashSet<T>(this.m_elements.values());    }
	
	public void addContainerElement(T ele) {
		String type = ele.getType(); 
		if(type.equals(HAPResourceDefinitionContainerElement.TYPE_ENTITY)) {
			HAPResourceDefinitionContainerElementEntity entityEle = (HAPResourceDefinitionContainerElementEntity)ele;
			entityEle.getAttachmentContainer().merge(this.getAttachmentContainer(), HAPConstant.INHERITMODE_PARENT);
		}
		this.m_elements.put(ele.getId(), ele);  
	}

	public abstract HAPResourceDefinition getElementResourceDefinition(String eleId);
	
	public abstract HAPResourceDefinitionContainer<T> cloneResourceDefinitionContainer();
	 
	public void cloneToResourceDefinitionContainer(HAPResourceDefinitionContainer<T> to) {
		this.cloneToComplexResourceDefinition(to);
		for(String id : this.m_elements.keySet()) {
			to.addContainerElement((T)this.m_elements.get(id).cloneResourceDefinitionContainerElement());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}
}
