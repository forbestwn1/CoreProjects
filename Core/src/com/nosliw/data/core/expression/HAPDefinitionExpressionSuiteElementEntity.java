package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;
import com.nosliw.data.core.operand.HAPOperand;

public class HAPDefinitionExpressionSuiteElementEntity extends HAPResourceDefinitionContainerElementEntityImpComponent{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPOperand m_operand;

	private Map<String, HAPDefinitionReference> m_references;
	
	public HAPDefinitionExpressionSuiteElementEntity() {
		this.m_references = new LinkedHashMap<String, HAPDefinitionReference>();
	}
	
	public HAPOperand getOperand() {    return this.m_operand;   }
	
	public void setOperand(HAPOperand operand) {   this.m_operand = operand;    }
	
	public void addReference(HAPDefinitionReference reference) {  this.m_references.put(reference.getName(), reference);   }
	public Map<String, HAPDefinitionReference> getReference(){   return this.m_references;    }
	public HAPDefinitionReference getReference(String name) {   return this.m_references.get(name);    }
}
