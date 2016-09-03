package com.nosliw.data.utils;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.info.HAPDataTypeInfo;

public class HAPDataErrorUtility {

	/*
	 * utility method to create error service data when one data operation is not defined for particular data type
	 */
	public static HAPServiceData createDataOperationNotDefinedError(HAPDataTypeInfo dataType, String operation, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_DATAOPERATION_NOTDEFINED, 
				operation, 
				"The data type "+ dataType.toString() +"'s operation " + operation + "does not defined");
		out.setException(ex);
		return out;
	}

	/*
	 * utility method to create error service data when one new data operation is not defined for particular data type
	 */
	public static HAPServiceData createNewDataOperationNotDefinedError(HAPDataTypeInfo dataType, HAPData[] parms, Exception ex){
		StringBuffer parmTypes = new StringBuffer();
		parmTypes.append("");
		for(int i=0; i<parms.length ; i++){
			HAPData parm = parms[i];
			if(i!=0)  parmTypes.append(", ");
			parmTypes.append(HAPDataUtility.getDataTypeInfo(parm).toString());
		}
		parmTypes.append("]");
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_NEWDATAOPERATION_NOTDEFINED, 
				dataType, 
				"The data type "+ dataType.toString() +"'s newData operation with parms " + parmTypes.toString() + "does not defined");
		out.setException(ex);
		return out;
	}
	
	/*
	 * utility method to create error service data when one data operation is not implemented for particular data type
	 */
	public static HAPServiceData createDataOperationNotExistError(HAPDataTypeInfo dataType, String operation, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_DATAOPERATION_NOTEXIST, 
				operation, 
				"The data type "+ dataType.toString() +"'s operation " + operation + "does not exists");
		out.setException(ex);
		return out;
	}

	/*
	 * utility method to create error service data when encounter wrong data type during expression, ...
	 */
	public static HAPServiceData createDataTypeError(HAPDataTypeInfo dataType, HAPDataTypeInfo expect, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_DATATYPE_WRONGTYPE, 
				dataType, 
				"Except data type : "+ expect.toString() +", however we have " + dataType.toString() + "!!!!!");
		out.setException(ex);
		return out;
	}

}
