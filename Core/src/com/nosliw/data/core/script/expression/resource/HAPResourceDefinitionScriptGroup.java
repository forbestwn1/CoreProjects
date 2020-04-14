package com.nosliw.data.core.script.expression.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;

public class HAPResourceDefinitionScriptGroup extends HAPResourceDefinitionComplexImp{

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private List<HAPDefinitionScriptEntity> m_element;
	
	public HAPResourceDefinitionScriptGroup() {
		this.m_element = new ArrayList<HAPDefinitionScriptEntity>();
	}
	
	public List<HAPDefinitionScriptEntity> getElements(){    return this.m_element;    }
	public void addElement(HAPDefinitionScriptEntity element) {   this.m_element.add(element);    }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}
}
