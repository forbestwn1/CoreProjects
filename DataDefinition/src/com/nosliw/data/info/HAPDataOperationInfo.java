package com.nosliw.data.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.utils.HAPAttributeConstant;

/*
 * store all the information related with operation,
 * these information is independent of implementation language
 * 		operation Name
 * 		operation Parms data type
 * 		operation Out data type
 *      convertPath :  the path used to convert data type to original data type
 *      original data type: sometime, data operation is not defined on data type itself, it may inherate from older version or parent data type
 * 		operation Description
 * for ins and out attribute, data type version information is not appliable 
 * we consider two operation is equal when only when they have the same name. 
 */
public class HAPDataOperationInfo implements HAPStringable{

	//operation name
	private String m_name;
	//operation Out information
	private HAPDataTypeInfo m_outDataTypeInfo;
	//operation In information, multiple value
	private List<HAPDataTypeInfo> m_inDataTypeInfos;
	//operation description
	private String m_description;

	private HAPDataType m_dataType;
	
	//path to describe how to convert the parms with data type to original data type when doing the operation, the path example:   version|123.parent.parent.version|456
	private String m_convertPath;
	//where the operation orignally come from (when data operation is not defined on current data type, then then it may inherent from older version or parent data type)
	private HAPDataOperationInfo m_originalDataOperationInfo;
	
	//all dependent data type infos
	private Map<String, Set<HAPDataTypeInfo>> m_dependentDataTypeInfo;
	
	public HAPDataOperationInfo(HAPDataType dataType, HAPDataOperationInfo operationInfo, String path){
		this(dataType, operationInfo.getName(), operationInfo.getInDataTypeInfos(), operationInfo.getOutDataTypeInfo(), operationInfo.getDescription());
		this.m_convertPath = path;
		this.m_originalDataOperationInfo = operationInfo;
		this.m_dependentDataTypeInfo = new LinkedHashMap<String, Set<HAPDataTypeInfo>>();
	}
	
	public HAPDataOperationInfo(HAPDataType dataType, String name, List<HAPDataTypeInfo> inDataTypeInfos, HAPDataTypeInfo outDataTypeInfo, String description){
		this.m_dataType = dataType;
		this.m_name = name;
		this.m_inDataTypeInfos = inDataTypeInfos;
		this.m_outDataTypeInfo = outDataTypeInfo;
		this.m_description = description;
		this.m_dependentDataTypeInfo = new LinkedHashMap<String, Set<HAPDataTypeInfo>>();
	}
	
	public HAPDataType getDataType(){ return this.m_dataType; }
	public String getName(){return this.m_name;}
	public HAPDataTypeInfo getOutDataTypeInfo(){return this.m_outDataTypeInfo;}
	public void setOutDataTypeInfo(HAPDataTypeInfo dataTypeInfo){this.m_outDataTypeInfo=dataTypeInfo;}
	public List<HAPDataTypeInfo> getInDataTypeInfos(){return this.m_inDataTypeInfos;}
	public String getDescription(){return this.m_description;}
	public String getConvertPath(){return this.m_convertPath;}
	public HAPDataOperationInfo getOriginalDataOperationInfo(){
		if(HAPBasicUtility.isStringEmpty(m_convertPath)) return this;
		else	return this.m_originalDataOperationInfo; 
	}
	public Set<HAPDataTypeInfo> getDependentDataTypeInfos(String scriptName){
		if(HAPBasicUtility.isStringEmpty(m_convertPath)){
			//no convert
			Set<HAPDataTypeInfo> dependents = this.m_dependentDataTypeInfo.get(scriptName);
			if(dependents==null)   return new HashSet<HAPDataTypeInfo>();
			else return dependents;
		}
		else{
			//get from original 
			return this.m_originalDataOperationInfo.getDependentDataTypeInfos(scriptName);
		}
	}
	public void setDependentDataTypeInfos(Set<HAPDataTypeInfo> infos, String scriptName){this.m_dependentDataTypeInfo.put(scriptName, infos);}
	
	public int getInNumber(){return this.m_inDataTypeInfos.size();}

	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_NAME, this.m_name);
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_DESCRIPTION, this.m_description);
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_CONVERTPATH, this.m_convertPath);
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_OUT, this.m_outDataTypeInfo.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_DEPENDENTDATATYPES, HAPJsonUtility.getSetObjectJson(this.getDependentDataTypeInfos(HAPConstant.OPERATIONDEF_SCRIPT_JAVASCRIPT)));
		
		List<String> inJsons = new ArrayList<String>();
		for(HAPDataTypeInfo info : this.m_inDataTypeInfos){
			inJsons.add(info.toStringValue(format));
		}
		jsonMap.put(HAPAttributeConstant.DATAOPERATIONINFO_INS, HAPJsonUtility.getArrayJson(inJsons.toArray(new String[0])));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.SERIALIZATION_JSON));
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof HAPDataOperationInfo){
			HAPDataOperationInfo info = (HAPDataOperationInfo)o;
			if(!HAPBasicUtility.isEquals(this.getName(), info.getName()))  return false;
			return true;	
		}
		else return false;
	}
}
