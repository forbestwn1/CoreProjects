package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionComplexImp;

public class HAPResourceDefinitionScriptGroup extends HAPResourceDefinitionComplexImp{

	@HAPAttribute
	public static String ELEMENT = "element";
	
	private List<HAPResourceDefinitionScriptElement> m_element;
	
	
	public HAPResourceDefinitionScriptGroup() {
		this.m_element = new ArrayList<HAPResourceDefinitionScriptElement>();
	}
	
	public List<HAPResourceDefinitionScriptElement> getElements(){    return this.m_element;    }
	public void addElement(HAPResourceDefinitionScriptElement element) {   this.m_element.add(element);    }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}

}
