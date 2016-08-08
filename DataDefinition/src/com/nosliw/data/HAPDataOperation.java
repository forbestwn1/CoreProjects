package com.nosliw.data;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.info.HAPDataTypeInfo;

/*
 * utility class for data type operation object
 */
public class HAPDataOperation {

	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataOperation(HAPDataTypeManager man){
		this.m_dataTypeMan = man;
	}
	
	protected HAPDataType getDataType(String categary, String type){
		return this.m_dataTypeMan.getDataType(new HAPDataTypeInfo(categary, type));
	}

	protected HAPServiceData dataOperate(String categary, String type, String operation, HAPData[] parms){
		HAPServiceData serviceData = this.getDataType(categary, type).operate(operation, parms);
		return serviceData;
	}
}
