package com.nosliw.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperand;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * this is path operation
 * it is one type of data operation
 */
public class HAPOperandAttribute extends HAPOperandDataOperaion{

	private String m_attribute;
	
	public HAPOperandAttribute(HAPOperand baseData, String attribute, HAPDataTypeManager dataTypeMan){
		super(baseData, HAPConstant.DATAOPERATION_GETCHILD, new HAPOperand[]{new HAPOperandConstant(attribute, dataTypeMan)}, dataTypeMan);
		this.m_attribute = attribute;
	}

	public String getAttribute(){return this.m_attribute;}
	
	@Override
	public int getOperandType() {	return HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION;	}
	
	@Override
	public boolean isScriptRunnable(String script)
	{
		//check base data
		if(!this.getBaseData().isScriptRunnable(script))   return false;
		return true;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap){
		super.buildJsonMap(jsonMap, jsonDataTypeMap);
		jsonMap.put(HAPAttributeConstant.OPERAND_OPERATION_ATTRIBUTE, this.m_attribute);
	}
	
	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos){
		//try to get out data type info based on base data type and path info
		//it is through the operation defined in base data : getChildDatatype
		if(this.getBaseDataType()!=null){
			HAPServiceData serviceData = this.getBaseDataType().operate(HAPConstant.DATAOPERATION_GETCHILDDATATYPE, new HAPData[]{HAPDataTypeManager.STRING.createDataByValue(m_attribute)}, null);
			this.setOutDataTypeInfo(HAPDataTypeInfo.parseDataTypeInfo(serviceData.getData().toString()));
		}
		else{
			this.setOutDataTypeInfo(null);
		}
		super.preProcess(varsInfo, dataTypeInfos);
	}
}
