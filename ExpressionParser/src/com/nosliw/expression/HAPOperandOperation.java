package com.nosliw.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataOperationInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperand;
import com.nosliw.expression.utils.HAPAttributeConstant;

/*
 * parent class for all the operand related with data operation
 * 		data operation
 * 		data type operation
 * 		path operation
 */
public abstract class HAPOperandOperation extends HAPOperandImp{

	//operation name
	protected String m_operation;
	//operation parms
	protected HAPOperand[] m_parms;

	//data type that operation defined on
	protected HAPDataTypeInfo m_baseDataTypeInfo;

	//calculated out information
	protected HAPDataType m_baseDataType;
	protected HAPDataOperationInfo m_operationInfo;
	
	public HAPOperandOperation(String operation, HAPOperand[] parms, HAPDataTypeManager dataTypeMan) {
		super(dataTypeMan);
		this.m_operation = operation;
		this.m_parms = parms;
	}

	public String getOperationName(){	return this.m_operation;	}
	
	public HAPOperand[] getParameters(){return this.m_parms;}
	
	/*
	 * check if operation is avaialbe for particular script
	 * return null:  no or unknown
	 */
	protected boolean isOperationScriptAvailable(String format){
		HAPDataType baseDataType = this.getBaseDataType();
		//chekc if base datatype information is available 
		if(baseDataType==null || m_operationInfo==null)   return false;

		return baseDataType.isScriptAvailable(this.getOperationName(), format);
	}

	protected boolean isOperationScriptAvailableLocally(String format){
		HAPDataType baseDataType = this.getBaseDataType();
		//chekc if base datatype information is available 
		if(baseDataType==null || m_operationInfo==null)   return false;

		return baseDataType.isScriptAvailableLocally(this.getOperationName(), format);
	}
	
	/*
	 * set base data type
	 * at the same time, calculate out other information : 
	 * 		base data type
	 * 		operation infor
	 * 		out data type info
	 */
	protected void setBaseDataTypeInfo(HAPDataTypeInfo dataTypeInfo){
		this.m_baseDataTypeInfo = dataTypeInfo;
		this.m_baseDataType = this.getDataTypeManager().getDataType(m_baseDataTypeInfo);
		this.m_operationInfo = this.m_baseDataType.getOperationInfoByName(m_operation);
		this.setOutDataTypeInfo(this.m_operationInfo.getOutDataTypeInfo());
	}

	protected HAPDataTypeInfo getBasedDataTypeInfo(){return this.m_baseDataTypeInfo;}
	protected HAPDataType getBaseDataType(){return this.m_baseDataType;}
	protected HAPDataOperationInfo getOperationInfo(){return this.m_operationInfo;}
	
	@Override
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos){
		//parms data type info
		for(HAPOperand parm : this.getParameters()){
			parm.preProcess(varsInfo, dataTypeInfos);
		}
		
		//base data type info
		if(this.m_baseDataTypeInfo!=null)   dataTypeInfos.add(this.m_baseDataTypeInfo);
		if(this.m_operationInfo!=null){
			for(HAPDataTypeInfo parmDataTypeInfo : this.m_operationInfo.getInDataTypeInfos())   dataTypeInfos.add(parmDataTypeInfo);
			//out data type info
			HAPDataTypeInfo outDataTypeInfo = this.m_operationInfo.getOutDataTypeInfo();
			if(outDataTypeInfo!=null){
				dataTypeInfos.add(outDataTypeInfo);
			}
			
			//operation dependent data type
			Set<HAPDataTypeInfo> dependentDataTypeInfos = this.m_baseDataType.getOperationDependentDataTypes(m_operation);
			dataTypeInfos.addAll(dependentDataTypeInfos);
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonDataTypeMap){
		jsonMap.put(HAPAttributeConstant.OPERAND_OPERATION_OPERATION, this.m_operation);
		if(this.m_baseDataTypeInfo!=null){
			jsonMap.put(HAPAttributeConstant.OPERAND_OPERATION_BASEDATATYPEINFO, this.m_baseDataTypeInfo.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(HAPAttributeConstant.OPERAND_OPERATION_PARAMETERS, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
	}
}
