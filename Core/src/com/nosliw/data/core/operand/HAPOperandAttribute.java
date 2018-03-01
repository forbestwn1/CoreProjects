package com.nosliw.data.core.operand;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPOperandAttribute extends HAPOperandImp{

	public static final String ATTRIBUTE = "attribute";
	
	public static final String BASEDATA = "baseData";
	
	private String m_attribute;
	
	private HAPOperandWrapper m_base;
	
	private HAPOperandAttribute(){}
	
	public HAPOperandAttribute(HAPOperand base, String attribute){
		super(HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION);
		this.setBase(base);
		this.m_attribute = attribute;
	}
	
	public String getAttribute(){   return this.m_attribute;   }
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION;}

	public void setBase(HAPOperand base){
		this.m_base = this.createOperandWrapper(base);
		this.addChildOperand(this.m_base);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(BASEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessExpressionDefinitionContext context,
			HAPDataTypeHelper dataTypeHelper) {
		return null;
	}

	@Override
	public HAPDataTypeCriteria getOutputCriteria() {
		return null;
	}

	@Override
	public HAPOperand cloneOperand() {
		HAPOperandAttribute out = new HAPOperandAttribute();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandAttribute operand){
		super.cloneTo(operand);
		operand.m_attribute = this.m_attribute;
		if(this.m_base!=null)	operand.m_base = this.m_base.cloneWrapper();
	}
}
