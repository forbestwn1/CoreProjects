package com.nosliw.uiresource.page.definition;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpressionEmbeded;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;

public class HAPDefinitionUIEmbededScriptExpression {

	private String m_uiId;

	// string or HAPDefinitionEmbededScript
	private List<Object> m_elements;

	public HAPDefinitionUIEmbededScriptExpression(String uiId, HAPDefinitionScriptExpressionEmbeded scriptExpression) {
		this.m_uiId = uiId;
		this.m_elements = new ArrayList<Object>();
		this.m_elements.add(scriptExpression);
	}

	public HAPDefinitionUIEmbededScriptExpression(String uiId, String content) {
		this.m_uiId = uiId;
		this.m_elements = new ArrayList<Object>();
		this.m_elements.addAll(HAPUtilityScriptExpression.discoverEmbededScript(content));
	}
	
	public HAPDefinitionUIEmbededScriptExpression(String uiId, List<Object> elements) {
		this.m_uiId = uiId;
		this.m_elements = new ArrayList<Object>();
		this.m_elements.addAll(elements);
	}

	public String getUIId() {   return this.m_uiId;  }
	
	public List<Object> getElements(){  return this.m_elements;   }

	public boolean containsScriptExpression() {
		for(Object ele : m_elements) {
			if(ele instanceof HAPDefinitionScriptExpressionEmbeded)  return true;
		}
		return false;
	}
	
}
