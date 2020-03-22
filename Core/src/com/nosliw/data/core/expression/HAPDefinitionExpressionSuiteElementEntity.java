package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPDefinitionExpressionSuiteElementEntity extends HAPResourceDefinitionContainerElementEntityImpComponent{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String REFERENCEMAPPING = "referenceMapping";

	private HAPOperandWrapper m_operand;

	private Map<String, HAPDefinitionReference> m_references;
	
	public HAPDefinitionExpressionSuiteElementEntity() {
		this.m_references = new LinkedHashMap<String, HAPDefinitionReference>();
	}
	
	public HAPOperandWrapper getOperand() {    return this.m_operand;   }
	
	public void setOperand(HAPOperandWrapper operand) {   this.m_operand = operand;    }
	public void setOperand(HAPOperand operand) {   this.m_operand = new HAPOperandWrapper(operand);    }
	
	public void addReference(HAPDefinitionReference reference) {  this.m_references.put(reference.getName(), reference);   }
	public Map<String, HAPDefinitionReference> getReference(){   return this.m_references;    }
	public HAPDefinitionReference getReference(String name) {   return this.m_references.get(name);    }
	
	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPDefinitionExpressionSuiteElementEntity out = new HAPDefinitionExpressionSuiteElementEntity();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out);
		out.m_operand = this.m_operand.cloneWrapper();
		for(String name : this.m_references.keySet()) {
			out.m_references.put(name, this.m_references.get(name).cloneReferenceDefinition());
		}
		return out;
	}

}
