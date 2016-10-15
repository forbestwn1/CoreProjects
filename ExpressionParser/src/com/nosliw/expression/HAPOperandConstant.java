package com.nosliw.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * a Operand that is constant data in expression
 */
public class HAPOperandConstant extends HAPOperandAtom{

	//string format (json, json_datatype, literal)
	private String m_value;
	//data type
	private HAPData m_data;
	
	public HAPOperandConstant(String value, HAPDataTypeManager dataTypeMan){
		super(dataTypeMan);
		this.m_value = value;
		this.m_data = this.getDataTypeManager().parseString(m_value, null, null);
		this.setOutDataTypeInfo(HAPDataUtility.getDataTypeInfo(m_data));
	}

	public HAPOperandConstant(String value, HAPDataTypeInfo dataTypeInfo, HAPDataTypeManager dataTypeMan){
		super(dataTypeInfo, dataTypeMan);
		this.m_value = value;
		this.m_data = this.getDataTypeManager().parseString(m_value, null, null);
	}
	
	@Override
	public int getOperandType(){	return HAPConstant.EXPRESSION_OPERAND_CONSTANT;}

	@Override
	public HAPData execute(Map<String, HAPData> vars, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) {	return this.m_data;	}

	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos){
		dataTypeInfos.add(HAPDataUtility.getDataTypeInfo(this.m_data));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap){
		super.buildJsonMap(jsonMap, jsonDataTypeMap);
		jsonMap.put(HAPAttributeConstant.OPERAND_CONSTANT_DATA, this.m_data.toStringValue(HAPSerializationFormat.JSON_FULL));
	}
}
