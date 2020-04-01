package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;

public class HAPDefinitionExpressionSuiteElementEntity extends HAPResourceDefinitionContainerElementEntityImpComponent{

	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> m_element;
	
	public HAPDefinitionExpressionSuiteElementEntity() {
		this.m_element = new LinkedHashMap<String, HAPDefinitionExpressionSuiteElementEntityExpression>();
	}
	
	public Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> getExpressions(){   return this.m_element;   }
	public void addExpression(HAPDefinitionExpressionSuiteElementEntityExpression expression) {
		String name = expression.getName();
		if(HAPBasicUtility.isStringEmpty(name)) name = HAPConstant.NAME_DEFAULT;
		this.m_element.put(name, expression);    
	}
	
	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPDefinitionExpressionSuiteElementEntity out = new HAPDefinitionExpressionSuiteElementEntity();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out);
		for(String name : this.m_element.keySet()) {
			out.m_element.put(name, this.m_element.get(name).cloneDefinitionExpressionSuiteElementEntityExpression());
		}
		return out;
	}

}
