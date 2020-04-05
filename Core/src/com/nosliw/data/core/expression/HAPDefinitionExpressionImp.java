package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.component.HAPWithAttachmentImp;

public class HAPDefinitionExpressionImp extends HAPWithAttachmentImp implements HAPDefinitionExpression{

	private Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> m_elements;
	
	public HAPDefinitionExpressionImp() {
		this.m_elements = new LinkedHashMap<String, HAPDefinitionExpressionSuiteElementEntityExpression>();
	}
	
	@Override
	public Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> getExpressionElements() {  return this.m_elements;  }  

	public void addExpressionElement(HAPDefinitionExpressionSuiteElementEntityExpression element) {   this.m_elements.put(element.getName(), element);   }
	
}
