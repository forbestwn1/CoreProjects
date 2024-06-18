package com.nosliw.data.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPOperandAttribute extends HAPOperandImp{

	public static final String ATTRIBUTE = "attribute";
	
	public static final String BASEDATA = "baseData";
	
	private String m_attribute;
	
	private HAPWrapperOperand m_base;
	
	private HAPOperandAttribute(){}
	
	public HAPOperandAttribute(HAPOperand base, String attribute){
		super(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION);
		this.setBase(base);
		this.m_attribute = attribute;
	}
	
	public String getAttribute(){   return this.m_attribute;   }
	
	public HAPWrapperOperand getBase() {  return this.m_base;  }
	
	public void setBase(HAPOperand base){	this.m_base = this.createOperandWrapper(base);	}
	
	@Override
	public List<HAPWrapperOperand> getChildren(){
		List<HAPWrapperOperand> out = new ArrayList<HAPWrapperOperand>();
		if(this.m_base!=null)	out.add(this.m_base);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(BASEDATA, HAPManagerSerialize.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPMatchers discover(
			HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
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
