package com.nosliw.data1;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.data.imp.HAPDataTypeImp;
import com.nosliw.data.utils.HAPDataOperationUtility;

/*
 * this class store information related with operation available to data type 
 */
public class HAPDataTypeOperationInfos {

	//store all newData operations (constructor)
	private Set<HAPDataOperationInfo> m_newDataOperations;
	//store all local data operations
	private Map<String, HAPDataOperationInfo> m_localOperations;
	//cache all available operation information including the operation path
	private Map<String, HAPDataOperationInfo> m_availableOperations;
	
	private HAPDataType m_dataType;
	private HAPDataTypeManager m_dataTypeMan;
	
	public HAPDataTypeOperationInfos(HAPDataType dataType, HAPDataTypeManager dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
		this.m_dataType = dataType;
		this.buildOperationInfos();
	}
	
	public Map<String, HAPDataOperationInfo> getOperationInfos(){
		if(this.m_availableOperations==null)	this.buildOperationInfos();
		return this.m_availableOperations;
	}
	
	public HAPDataOperationInfo getOperationInfoByName(String name){		return this.getOperationInfos().get(name);	}

	public Map<String, HAPDataOperationInfo> getLocalOperationInfos(){
		if(this.m_localOperations==null)	this.buildOperationInfos();
		return this.m_localOperations;
	}
	
	public HAPDataOperationInfo getLocalOperationInfoByName(String name){ return this.getLocalOperationInfos().get(name); }

	public Set<HAPDataOperationInfo> getNewDataOperations(){
		if(this.m_newDataOperations==null)  this.buildOperationInfos();
		return this.m_newDataOperations;
	}
	
	public HAPDataOperationInfo getNewDataOperation(HAPDataTypeInfo[] dataTypeInfos){
		HAPDataOperationInfo newDataOp = null;
		for(HAPDataOperationInfo opInfo : this.getNewDataOperations()){
			//check parm length
			if(opInfo.getInNumber()==dataTypeInfos.length){
				//check each data type
				List<HAPDataTypeInfo> dataTypeInfo = opInfo.getInDataTypeInfos();
				boolean sameParmType = true;
				for(int i=0; i<dataTypeInfos.length; i++){
					if(!dataTypeInfos[i].equals(dataTypeInfo.get(i))){
						sameParmType = false;
						break;
					}
				}
				if(sameParmType){
					newDataOp = opInfo;
					break;
				}
			}
		}
		return newDataOp;
	}
	
	/*
	 * build all the available operation infor for this data type and save them so that we don't need to build it next time when need operation
	 */
	private void buildOperationInfos(){
		this.m_availableOperations = new LinkedHashMap<String, HAPDataOperationInfo>();
		this.m_localOperations = new LinkedHashMap<String, HAPDataOperationInfo>();
		this.m_newDataOperations = new HashSet<HAPDataOperationInfo>();
		
		this.buildOperationInfosLocally();
		
		this.buildOperationInfosWithinVersion();
		
		this.buildOperationInfosForParent();
	}
	
	/*
	 * try to get operation info from local data type (current version def)
	 */
	private Map<String, HAPDataOperationInfo> buildOperationInfosLocally(){
		//locally
		HAPDataTypeOperations dataTypeOps = this.m_dataType.getDataTypeOperationsObject();
		
		if(dataTypeOps!=null){
			Map<String, HAPDataOperationInfo> ops = dataTypeOps.getOperationInfos();
			for(String name : ops.keySet()){
				if(HAPDataOperationUtility.isNewOperation(name)){
					//constructor operation
					HAPDataOperationInfo opInfo = ops.get(name);
					//set out put data type as the same data type
					opInfo.setOutDataTypeInfo(new HAPDataTypeInfo(this.m_dataType.getDataTypeInfo().getCategary(), this.m_dataType.getDataTypeInfo().getType()));
					this.getNewDataOperations().add(opInfo);
				}
				else{
					//normal operation
					this.getLocalOperationInfos().put(name, ops.get(name));
					if(!this.m_availableOperations.containsKey(name)){
						this.m_availableOperations.put(name, ops.get(name));
					}
				}
			}
		}
		return this.m_availableOperations;
	}
	
	/*
	 * build available operation info (basic info + convert path) from old version
	 */
	private Map<String, HAPDataOperationInfo> buildOperationInfosWithinVersion(){
		HAPDataTypeImp olderDataType = (HAPDataTypeImp)this.m_dataType.getOlderDataType();
		if(olderDataType==null)   return this.m_availableOperations;
		//older version
		Map<String, HAPDataOperationInfo> olderOpInfos = olderDataType.getOperationInfos();
		for(String name : olderOpInfos.keySet()){
			if(!this.m_availableOperations.containsKey(name)){
				String oldPath = HAPNamingConversionUtility.cascadePath(
						HAPNamingConversionUtility.cascadeDetail(HAPConstant.OPERATIONDEF_PATH_VERSION, olderDataType.getDataTypeInfo().getVersionNumber()+""),
						olderOpInfos.get(name).getConvertPath());
				this.m_availableOperations.put(name, new HAPDataOperationInfo(this.m_dataType, olderOpInfos.get(name), oldPath));
			}
		}
		return this.m_availableOperations;
	}
	
	/*
	 * build available operation info (basic info + convert path) from parent
	 */
	private Map<String, HAPDataOperationInfo> buildOperationInfosForParent(){
		HAPDataTypeImp parentDataType = (HAPDataTypeImp)this.m_dataTypeMan.getDataType(this.m_dataType.getParentDataTypeInfo());
		if(parentDataType==null)  return this.m_availableOperations;
		
		Map<String, HAPDataOperationInfo> parentOps = parentDataType.getOperationInfos();
		for(String name : parentOps.keySet()){
			if(!this.m_availableOperations.containsKey(name)){
				String parentPath = HAPNamingConversionUtility.cascadePath(HAPConstant.OPERATIONDEF_PATH_PARENT,
						parentOps.get(name).getConvertPath());
				this.m_availableOperations.put(name, new HAPDataOperationInfo(this.m_dataType, parentOps.get(name), parentPath));
			}
		}
		return this.m_availableOperations;
	}
	
}
