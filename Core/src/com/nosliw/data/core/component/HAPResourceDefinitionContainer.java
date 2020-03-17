package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public abstract class HAPResourceDefinitionContainer extends HAPResourceDefinitionComplexImp{

	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, HAPResourceDefinitionContainerElement> m_elements;

	public HAPResourceDefinitionContainer() {
		this.m_elements = new LinkedHashMap<String, HAPResourceDefinitionContainerElement>();
	}
	
	public HAPResourceDefinitionContainerElement getElement(String name) {     return this.m_elements.get(name);     }
	
	public Map<String, HAPResourceDefinitionContainerElement> getAllElements(){    return this.m_elements;    }
	
	public void addElement(String id, HAPResourceDefinitionContainerElement ele) {
		String type = ele.getType();
		if(type.equals(HAPResourceDefinitionContainerElement.TYPE_ENTITY)) {
			HAPResourceDefinitionContainerElementEntity entityEle = (HAPResourceDefinitionContainerElementEntity)ele;
			entityEle.getAttachmentContainer().merge(this.getAttachmentContainer(), HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT);
		}
		this.m_elements.put(id, ele);  
	}

	public abstract HAPResourceDefinition getElementResourceDefinition(String eleName);
	
	public abstract HAPResourceDefinitionContainer cloneResourceDefinitionContainer();
	
	public void cloneToResourceDefinitionContainer(HAPResourceDefinitionContainer to) {
		this.cloneToComplexResourceDefinition(to);
		for(String name : this.m_elements.keySet()) {
			to.m_elements.put(name, this.m_elements.get(name).cloneResourceDefinitionContainerElement());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}

}
