package com.nosliw.data.core.complex;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceDefinition1;

public abstract class HAPDefinitionEntityContainer <T extends HAPElementInContainerEntityDefinition> extends HAPDefinitionEntityComplexImp{
 
	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, T> m_elements;

	public HAPDefinitionEntityContainer() {
		this.m_elements = new LinkedHashMap<String, T>();
	}
	
	public T getContainerElement(String id) {     return this.m_elements.get(id);     }
	
	public Set<T> getContainerElements(){    return new HashSet<T>(this.m_elements.values());    }
	
	public void addContainerElement(T ele) {
		String type = ele.getElementType(); 
		if(type.equals(HAPElementInContainerEntityDefinition.TYPE_ENTITY)) {
			HAPElementInContainerEntityDefinitionImpComplex entityEle = (HAPElementInContainerEntityDefinitionImpComplex)ele;
			entityEle.getAttachmentContainer().merge(this.getAttachmentContainer(), HAPConstant.INHERITMODE_PARENT);
		}
		this.m_elements.put(ele.getId(), ele);  
	}

	public void cloneToResourceDefinitionContainer(HAPDefinitionEntityContainer<T> to) {
		this.cloneToComplexResourceDefinition(to, true);
		for(String id : this.m_elements.keySet()) {
			to.addContainerElement((T)this.m_elements.get(id).cloneDefinitionEntityElementInContainer());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPUtilityJson.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}

	public abstract HAPResourceDefinition1 getElementResourceDefinition(String eleId);
	
	public abstract HAPDefinitionEntityContainer<T> cloneResourceDefinitionContainer();
	 
}
