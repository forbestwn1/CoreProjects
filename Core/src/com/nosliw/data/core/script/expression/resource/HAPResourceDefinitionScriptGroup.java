package com.nosliw.data.core.script.expression.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPContainerChildResource;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptGroup;

public class HAPResourceDefinitionScriptGroup extends HAPResourceDefinitionComplexImp implements HAPDefinitionScriptGroup{

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private Map<String, HAPDefinitionScriptEntity> m_element;
	
	public HAPResourceDefinitionScriptGroup() {
		this.m_element = new LinkedHashMap<String, HAPDefinitionScriptEntity>();
	}
	
	@Override
	public Set<HAPDefinitionScriptEntity> getEntityElements() {  return new HashSet<HAPDefinitionScriptEntity>(this.m_element.values());    }

	@Override
	public HAPDefinitionScriptEntity getEntityElement(String id) {  return this.m_element.get(id);  }  

	@Override
	public void addEntityElement(HAPDefinitionScriptEntity entityElement) {   this.m_element.put(entityElement.getId(), entityElement);  }

	@Override
	public HAPContainerChildResource getChildrenResource() {
		// TODO Auto-generated method stub
		return null;
	}

}
