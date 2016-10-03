package com.nosliw.data;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.info.HAPDataOperationInfo;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.data.info.HAPDataTypeInfoWithVersion;
import com.nosliw.data.utils.HAPDataErrorUtility;

/*
 * store a set of operation information that defined locally for this data type
 */
public class HAPDataTypeOperations {
	//data type for operation
	private HAPDataTypeInfoWithVersion m_dataTypeInfo;
	//operation information
	private Map<String, HAPDataOperationInfo> m_operationInfos;
	//operation script <opName <scriptName, script>> 
	private Map<String, Map<String, HAPScriptOperationInfo>> m_operationScriptInfos;
	//object to provide method to implements the operation for java 
	private Object m_operationObj;
	
	private HAPDataType m_dataType;
	
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataTypeOperations(HAPDataTypeInfoWithVersion dataTypeInfo, HAPDataTypeManager dataTypeMan){
		this.m_operationInfos = new LinkedHashMap<String, HAPDataOperationInfo>();
		this.m_operationScriptInfos = new LinkedHashMap<String, Map<String, HAPScriptOperationInfo>>();
		this.m_dataTypeInfo = dataTypeInfo;
		this.m_dataTypeMan = dataTypeMan;
	}

	/****************************** Operation ********************************/
	
	/*
	 * try to perform operation
	 * input:
	 * 		data:  array of input data 
	 * return : 
	 *     1. success
	 *     2. CONS_ERRORCODE_DATAOPERATION_NOTDEFINED    the operation does not define in this set
	 *     3. CONS_ERRORCODE_DATAOPERATION_NOTEXIST    the operation is defined, but no method is provided
	 */
	public HAPServiceData operate(String operation, HAPData[] data, HAPOperationContext opContext){
		HAPDataOperationInfo opInfo = this.getOperationInfoByName(operation);
		if(opInfo==null) return HAPDataErrorUtility.createDataOperationNotDefinedError(this.m_dataTypeInfo, operation, null);  
		
		Class[] paramTypes = new Class[]{HAPData[].class};
		try {
			Method m = this.getOperationObject().getClass().getDeclaredMethod(operation, new Class[]{HAPData[].class, HAPOperationContext.class});
			Object r = m.invoke(this.getOperationObject(), data, opContext);
			return HAPServiceData.createSuccessData(r);
		} catch (Exception e) {
			e.printStackTrace();
			return HAPDataErrorUtility.createDataOperationNotExistError(this.m_dataTypeInfo, operation, e); 
		}
	}
	
	/*
	 * try to get operation script 
	 * return : 
	 *     1. success
	 *     2. CONS_ERRORCODE_DATAOPERATION_NOTDEFINED    the operation does not define in this set
	 *     3. CONS_ERRORCODE_DATAOPERATION_NOTEXIST    the operation is defined, but no script is provided
	 */
	public HAPServiceData getOperateScript(String operation, String format){
		HAPServiceData scriptInfoServiceData = this.getOperateScriptInfo(operation, format);
		if(scriptInfoServiceData.isFail())  return scriptInfoServiceData;
		else{
			HAPScriptOperationInfo scriptOpInfo = (HAPScriptOperationInfo)scriptInfoServiceData.getData();
			String script = scriptOpInfo.getScript();
			if(script==null)  return HAPDataErrorUtility.createDataOperationNotDefinedError(this.m_dataTypeInfo, operation, null);
			else return HAPServiceData.createSuccessData(script);
		}
	}

	public HAPServiceData getOperationDependentDataType(String operation){
		HAPServiceData scriptInfoServiceData = this.getOperateScriptInfo(operation, HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT);
		if(scriptInfoServiceData.isFail())  return scriptInfoServiceData;
		else{
			HAPScriptOperationInfo scriptOpInfo = (HAPScriptOperationInfo)scriptInfoServiceData.getData();
			Set<HAPDataTypeInfo> dependentDataTypeInfos = scriptOpInfo.getDependentDataTypeInfos();
			return HAPServiceData.createSuccessData(dependentDataTypeInfos);
		}
	}
	
	public HAPServiceData getOperateScriptInfo(String operation, String format){
		HAPDataOperationInfo opInfo = this.getOperationInfoByName(operation);
		if(opInfo==null) return HAPDataErrorUtility.createDataOperationNotDefinedError(this.m_dataTypeInfo, operation, null);  

		Map<String, HAPScriptOperationInfo> opScriptInfos = this.m_operationScriptInfos.get(operation);
		if(opScriptInfos==null)    return HAPDataErrorUtility.createDataOperationNotDefinedError(this.m_dataTypeInfo, operation, null);
		HAPScriptOperationInfo scriptInfo = opScriptInfos.get(format);
		if(scriptInfo==null)  return HAPDataErrorUtility.createDataOperationNotDefinedError(this.m_dataTypeInfo, operation, null);
		else return HAPServiceData.createSuccessData(scriptInfo);
	}
	
	/****************************** Manipulate ********************************/
	
	public void addDataOperationInfo(HAPDataOperationInfo info){	this.m_operationInfos.put(info.getName(), info);	}
	
	public void addOperationScript(String operation, String scriptName, String script, Set<HAPDataTypeInfo> dependentDataTypeInfos){
		Map<String, HAPScriptOperationInfo> opScriptInfos = this.m_operationScriptInfos.get(operation);
		if(opScriptInfos==null){
			opScriptInfos = new LinkedHashMap<String, HAPScriptOperationInfo>();
			this.m_operationScriptInfos.put(operation, opScriptInfos);
		}
		opScriptInfos.put(scriptName, new HAPScriptOperationInfo(script, dependentDataTypeInfos));
		
		//set dependent data type info to data operation info
		HAPDataOperationInfo dataOperationInfo = this.getOperationInfoByName(operation);
		dataOperationInfo.setDependentDataTypeInfos(dependentDataTypeInfos, scriptName);
	}

	public void setOperationObject(Object o){this.m_operationObj=o;}
	
	
	/****************************** Basice Information ********************************/
	
	public HAPDataTypeInfoWithVersion getDataTypeInfo(){return this.m_dataTypeInfo;}
	
	public HAPDataType getDataType(){
		if(this.m_dataType==null){
			this.m_dataType = this.getDataTypeManager().getDataType(this.m_dataTypeInfo);
		}
		return this.m_dataType;
	}
	
	public Map<String, HAPDataOperationInfo> getOperationInfos(){return this.m_operationInfos;}
	
	public HAPDataOperationInfo getOperationInfoByName(String name){return this.m_operationInfos.get(name);}
	
	public Object getOperationObject(){  return this.m_operationObj;}
	
	
	protected HAPDataTypeManager getDataTypeManager(){	return this.m_dataTypeMan;	}
}
