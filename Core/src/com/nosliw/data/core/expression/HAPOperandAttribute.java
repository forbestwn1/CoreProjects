package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPConverters;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperandAttribute extends HAPOperandImp{

	public static final String ATTRIBUTE = "attribute";
	
	public static final String BASEDATA = "baseData";
	
	private String m_attribute;
	
	private HAPOperand m_base;
	
	public HAPOperandAttribute(HAPOperand base, String attribute){
		super(HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION);
		this.setBase(base);
		this.m_attribute = attribute;
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION;}

	public void setBase(HAPOperand base){
		this.addChildOperand(base);
		this.m_base = base;  
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(BASEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON_FULL));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(BASEDATA, HAPSerializeManager.getInstance().toStringValue(this.m_base, HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPOperand> getChildren() {
		List<HAPOperand> out = new ArrayList<HAPOperand>();
		out.add(m_base);
		return out;
	}

	@Override
	public HAPConverters discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessVariablesContext context,
			HAPDataTypeHelper dataTypeHelper) {
		return null;
	}

	@Override
	public HAPDataTypeCriteria getDataTypeCriteria() {
		return null;
	}
}
