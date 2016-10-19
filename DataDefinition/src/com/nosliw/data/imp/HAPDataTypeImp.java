package com.nosliw.data.imp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.bool.HAPBooleanOperation;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.datatype.HAPDataTypeInfoWithVersion;
import com.nosliw.data.utils.HAPAttributeConstant;
import com.nosliw.data.utils.HAPDataErrorUtility;
import com.nosliw.data.utils.HAPDataOperationUtility;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.data1.HAPDataOperationInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPDataTypeOperationInfos;
import com.nosliw.data1.HAPDataTypeOperations;
import com.nosliw.data1.HAPOperationContext;

/*
 * data type definition implemetation
 * this implementation support parent data type (like extends in class definition)
 * all the operations for this data type are provided through HAPDataTypeOperations sections
 * when do operation, following the steps:
 * 		seach in HAPDataTypeOperations list one by one, and operate on the first one found
 * 		if not found in HAPDataTypeOperations list, then try to operate on parent data type
 * 		in order to operate on parent data type, the data on current data type must convert to parent data type
 */

public abstract class HAPDataTypeImp implements HAPDataType{
	
	private HAPDataTypeInfoWithVersion m_dataTypeInfo;
	private String m_description;

	//parent data type
	private HAPDataTypeImp m_parentDataType;
	private HAPDataTypeInfoWithVersion m_parentDataTypeInfo;

	//operation sections, defined locally
	private HAPDataTypeOperations m_operations;

	//older and newer version DataType
	private HAPDataTypeImp m_olderDataType;
	private HAPDataTypeImp m_newerDataType;
	
	//we can configure the behavior of data type
	private HAPConfigure m_configures;

	//store all the operation information for this data type(local, available, new)
	private HAPDataTypeOperationInfos m_operationInfos;
	
	private HAPDataTypeManager m_dataTypeMan;

	protected HAPDataTypeImp(HAPDataTypeInfoWithVersion dataTypeInfo,        
							HAPDataType olderDataType, 		
							HAPDataTypeInfoWithVersion parentDataTypeInfo, 
							HAPConfigure configures,
							String description,
							HAPDataTypeManager dataTypeMan){
		super();
		this.m_dataTypeInfo = dataTypeInfo;
		this.m_parentDataTypeInfo = parentDataTypeInfo;
		this.m_dataTypeMan = dataTypeMan;
		
		this.m_configures = configures;
		this.m_description = description;

		this.setOlderDataType((HAPDataTypeImp)olderDataType);
		
		this.initDataType();
	}

	/*
	 * init data type object based on the configure information
	 */
	protected void initDataType(){}

//	@Override
//	public void buildOperation(){}

	/****************************** Operation Related ********************************/
	@Override
	public HAPServiceData localOperate(String operation, HAPData[] parms, HAPOperationContext opContext){
		return this.getDataTypeOperationsObject().operate(operation, parms, opContext);
	}

	/*
	 * do data operation globally
	 * 		operation : operation name
	 * 		parms: parms for the operation
	 */
	@Override
	public HAPServiceData operate(String operation, HAPData[] parms, HAPOperationContext opContext){
		return HAPDataOperationUtility.dataOperate(this, operation, parms, opContext);
	}
	
	@Override
	public HAPDataOperationInfo getNewDataOperation(HAPDataTypeInfo[] dataTypeInfos){	
		return this.getDataTypeOperationInfos().getNewDataOperation(dataTypeInfos);	
	}
	
	@Override
	public HAPServiceData newData(HAPData[] data, HAPOperationContext opContext){
		List<HAPDataTypeInfo> parmTypes = new ArrayList<HAPDataTypeInfo>();
		if(data!=null){
			for(HAPData d : data)	parmTypes.add(HAPDataUtility.getDataTypeInfo(d));
		}
		HAPDataOperationInfo newDataOp = this.getNewDataOperation(parmTypes.toArray(new HAPDataTypeInfo[0]));
		
		HAPServiceData out = null;
		if(newDataOp!=null){
			out = this.localOperate(newDataOp.getName(), data, opContext);
		}
		else{
			out = HAPDataErrorUtility.createNewDataOperationNotDefinedError(this.getDataTypeInfo(), data, null);
		}
		return out;
	}

	@Override
	public HAPServiceData newData(String name, HAPData[] data, HAPOperationContext opContext){	return this.localOperate(name, data, opContext);	}
	
	@Override
	public Set<HAPDataTypeInfo> getOperationDependentDataTypes(String operation){
		HAPServiceData out = this.getDataTypeOperationsObject().getOperationDependentDataType(operation);
		if(out.isSuccess())  return (Set<HAPDataTypeInfo>)out.getData();
		else return new HashSet<HAPDataTypeInfo>();
	}

	@Override
	public Map<String, HAPDataOperationInfo> getOperationInfos(){	return this.getDataTypeOperationInfos().getOperationInfos();	}
	
	@Override
	public HAPDataOperationInfo getOperationInfoByName(String name){	return this.getOperationInfos().get(name);	}

	@Override
	public Map<String, HAPDataOperationInfo> getLocalOperationInfos(){ return this.getDataTypeOperationInfos().getLocalOperationInfos();  }
	
	@Override
	public HAPDataOperationInfo getLocalOperationInfoByName(String name){ return this.getLocalOperationInfos().get(name); }

	@Override
	public Set<HAPDataOperationInfo> getNewDataOperations(){  return this.getDataTypeOperationInfos().getNewDataOperations(); }

	public void setDataTypeOperations(HAPDataTypeOperations operations){this.m_operations = operations;}

	private HAPDataTypeOperationInfos getDataTypeOperationInfos(){
		if(this.m_operationInfos==null){
			this.m_operationInfos = new HAPDataTypeOperationInfos(this, this.getDataTypeManager());
		}
		return this.m_operationInfos;
	}
	
	@Override
	public HAPDataTypeOperations getDataTypeOperationsObject(){return this.m_operations;}

	/****************************** Script ********************************/
	@Override
	public boolean isScriptAvailable(String operation, String format){
		HAPDataOperationInfo dataOpInfo = this.getOperationInfoByName(operation);
		if(dataOpInfo==null)   return false;
		HAPDataOperationInfo originalDataOpInfo = dataOpInfo.getOriginalDataOperationInfo();
		String script = ((HAPDataTypeImp)originalDataOpInfo.getDataType()).getOperateScript(operation, format);
		return script!=null;
	}

	@Override
	public boolean isScriptAvailableLocally(String operation, String format){
		return null != this.getOperateScript(operation, format);
	}
	
	private String getOperateScript(String operation, String format){
		HAPServiceData out = this.getDataTypeOperationsObject().getOperateScript(operation, format);
		if(out.isSuccess())  return (String)out.getData();
		else return null;
	}
	

	
	/****************************** Version ********************************/
	@Override
	public HAPDataType getOlderDataType(){	return this.m_olderDataType;	}
	
	@Override
	public HAPDataType getNewerDataType(){  return this.m_newerDataType;	}

	void setOlderDataType(HAPDataTypeImp dataType){
		this.m_olderDataType = dataType;
		if(dataType!=null)		dataType.m_newerDataType = this;
	}
	
	/*
	 * get Data type with specified version number
	 */
	@Override
	public HAPDataTypeImp getDataTypeByVersion(int version){
		HAPDataTypeImp out = null;
		if(version==this.getVersion())   return this;
		if(version>this.getVersion()){
			out = (HAPDataTypeImp)this.getNewerDataType();
		}
		else if(version<this.getVersion()){
			out = (HAPDataTypeImp)this.getOlderDataType();
		}
		if(out==null)   return null;
		else  return out.getDataTypeByVersion(version);
	}

	/*
	 * Utility method to get version number of current data type
	 */
	private int getVersion(){ return this.getDataTypeInfo().getVersionNumber();	}
	
	
	/****************************** Configure ********************************/
	protected String getConfigure(String name){return this.m_configures.getConfigureValue(name).getStringValue();}
	
	
	/****************************** Basice Information ********************************/
	@Override
	public String getDescription(){return this.m_description;}
	
	@Override
	public HAPDataTypeInfoWithVersion getDataTypeInfo(){return this.m_dataTypeInfo;}

	@Override
	public HAPData[] getDomainDatas(){return null;	}

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo(){return this.m_parentDataTypeInfo;}
	
	public HAPDataTypeImp getParentDataType(){
		if(this.m_parentDataTypeInfo==null)   return null;
		if(this.m_parentDataType==null){
			this.m_parentDataType = (HAPDataTypeImp)this.getDataTypeManager().getDataType(this.m_parentDataTypeInfo);
		}
		return this.m_parentDataType;
	}
	
	protected HAPDataTypeManager getDataTypeManager(){	return this.m_dataTypeMan;	}

	/****************************** Serialization ********************************/
	@Override
	public String buildLocalOperationScript(String scriptName){
		StringBuffer opFunctions = new StringBuffer();
		for(String name : this.getLocalOperationInfos().keySet()){
			String script = this.buildOperationFunctionScript(name, scriptName);
			if(script!=null)			opFunctions.append(script);
		}
		
		StringBuffer newFunctions = new StringBuffer();
		for(HAPDataOperationInfo dataOpInfo : this.getNewDataOperations()){
			String name = dataOpInfo.getName();
			String script = this.buildOperationFunctionScript(name, scriptName);
			if(script!=null)			newFunctions.append(script);
		}
		
		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataTypeImp.class, "DataTypeWithVersionOperations.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("version", this.getVersion()+"");
		parms.put("opFunctions", opFunctions.toString());
		parms.put("newFunctions", newFunctions.toString());
		return HAPStringTemplateUtil.getStringValue(templateStream, parms);
	}
	
	private String buildOperationFunctionScript(String name, String scriptName){
		String script = this.getOperateScript(name, scriptName);
		if(script==null)  return null;

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPDataTypeImp.class, "DataOperationFunction.txt");
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("functionScript", script);
		parms.put("functionName", name);
		String out = HAPStringTemplateUtil.getStringValue(templateStream, parms);
		return out;
	}
	
	@Override
	public String toDataStringValue(HAPData data, String format){return data.toStringValue(format);	}
	
	@Override
	public HAPData toData(Object value, String format){
		HAPData out = null;
		if(HAPSerializationFormat.TEXT.equals(format)){
			//literal
			//if operation CONS_DATAOPERATION_PARSELITERAL is defined
			HAPData[] parms = {HAPDataTypeManager.createStringData((String)value)};
			HAPServiceData serviceData = this.localOperate(HAPConstant.DATAOPERATION_PARSELITERAL, parms, null);
			if(serviceData.isSuccess())  return (HAPData)serviceData.getData();
			else{
				return this.parseLiteral((String)value);
			}
		}
		else if(HAPSerializationFormat.JSON.equals(format)){
			return this.parseJson(value);
		}
		return out;
	}

	@Override
	public HAPData parseLiteral(String text){ return null;	}
	@Override
	public HAPData parseJson(Object jsonObj){return null;}

	
	@Override
	public String toStringValue(String format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.DATATYPE_DATATYPEINFO, this.getDataTypeInfo().toStringValue(format));
		if(this.getParentDataType()!=null){
			jsonMap.put(HAPAttributeConstant.DATATYPE_PARENT, this.getParentDataTypeInfo().toStringValue(format));
		}

		if(this.getDataTypeOperationsObject()!=null){
			//all the available operations (name, input, output, convert path)
			Map<String, String> jsonOpInfoMap = new LinkedHashMap<String, String>();
			for(String name : this.getOperationInfos().keySet()){
				jsonOpInfoMap.put(name, this.getOperationInfos().get(name).toStringValue(format));
			}
			jsonMap.put(HAPAttributeConstant.DATATYPE_OPERATIONINFOS, HAPJsonUtility.buildMapJson(jsonOpInfoMap));
			
			List<String> jsonNewOpInfoList = new ArrayList<String>();
			for(HAPDataOperationInfo opInfo : this.getNewDataOperations()){
				jsonNewOpInfoList.add(opInfo.toStringValue(format));
			}
			jsonMap.put(HAPAttributeConstant.DATATYPE_NEWOPERATIONINFOS, HAPJsonUtility.buildJson(jsonNewOpInfoList, HAPSerializationFormat.JSON));
		}
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));
	}	
}
