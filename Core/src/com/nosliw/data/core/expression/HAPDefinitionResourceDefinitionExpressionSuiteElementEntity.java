package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;

public class HAPDefinitionResourceDefinitionExpressionSuiteElementEntity extends HAPResourceDefinitionContainerElementEntityImpComponent implements HAPDefinitionExpressionGroup{

	@HAPAttribute
	public static String ELEMENT = "element";

	private Map<String, HAPDefinitionExpression> m_element;
	
	public HAPDefinitionResourceDefinitionExpressionSuiteElementEntity() {
		this.m_element = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	@Override
	public Map<String, HAPDefinitionExpression> getExpressions(){   return this.m_element;   }

	public void addExpression(HAPDefinitionExpression expression) {
		String name = expression.getName();
		if(HAPBasicUtility.isStringEmpty(name)) name = HAPConstant.NAME_DEFAULT;
		this.m_element.put(name, expression);    
	}
	
	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out);
		for(String name : this.m_element.keySet()) {
			out.m_element.put(name, this.m_element.get(name).cloneDefinitionExpressionSuiteElementEntityExpression());
		}
		return out;
	}

}
