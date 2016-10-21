package com.nosliw.data;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;

public interface HAPJavaDataRuntimeManager {

	public HAPServiceData executeDataOperation(HAPDataTypeInfo dataTypeInfo, String operation, Map<String, HAPData> parms, HAPOperationContext opContext, String env);
	
	public HAPServiceData executeExpression(HAPExpression expression, HAPOperationContext opContext, String env);
	
	
	
	
	
	
	public void buildOperation();
	
	//object that defined all the operations info
	public HAPDataTypeOperations getDataTypeOperationsObject();
	
	//*****************************************  Data Operation

	//for java, run operate
	public HAPServiceData operate(String operation, Map<String, HAPData> parms, HAPOperationContext opContext);
	public HAPServiceData localOperate(String operation, Map<String, HAPData> parms, HAPOperationContext opContext);
	//create a new data instance 
	public HAPServiceData newData(Map<String, HAPData> parms, HAPOperationContext opContext);
	//create a new data instance by using the name new method
	public HAPServiceData newData(String name, Map<String, HAPData> parms, HAPOperationContext opContext);

	
	public boolean isScriptAvailable(String operation, String format);
	public boolean isScriptAvailableLocally(String operation, String format);
	public String buildLocalOperationScript(String scriptName);
	//get dependent data type for operation
	public Set<HAPDataTypeInfo> getOperationDependentDataTypes(String operation);
	

}
