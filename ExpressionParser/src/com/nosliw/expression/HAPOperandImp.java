package com.nosliw.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperand;
import com.nosliw.expression.utils.HAPAttributeConstant;

public abstract class HAPOperandImp implements HAPOperand{
	//output data type info for operand 
	//this information may not needed during run time
	//but, this information help to jugdge if some operation is ready to run
	private HAPDataTypeInfo m_outDataTypeInfo;
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPOperandImp(HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}

	public HAPOperandImp(HAPDataTypeInfo outDataTypeInfo, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_outDataTypeInfo = outDataTypeInfo;
	}
	
	@Override
	public HAPDataTypeInfo getOutDataTypeInfo(){	return this.m_outDataTypeInfo;	}
	public void setOutDataTypeInfo(HAPDataTypeInfo dataTypeInfo){	this.m_outDataTypeInfo = dataTypeInfo;	}
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}

	/*
	 * sub class can add their json information
	 */
	abstract protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap);
	
	@Override
	public final String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.OPERAND_TYPE, String.valueOf(this.getOperandType()));
		jsonMap.put(HAPAttributeConstant.OPERAND_SCRIPTRUNNALBE, this.isScriptRunnable(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT)+"");
		
		if(this.m_outDataTypeInfo!=null)		jsonMap.put(HAPAttributeConstant.OPERAND_OUTDATATYPEINFO, this.m_outDataTypeInfo.toStringValue(HAPSerializationFormat.JSON));

		Map<String, Class> jsonDataTypeMap = new LinkedHashMap<String, Class>();
		
		this.buildJsonMap(jsonMap, jsonDataTypeMap);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));
	}
}
