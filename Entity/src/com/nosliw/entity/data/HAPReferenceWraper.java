package com.nosliw.entity.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;

/*
 * wraper that have entity id as data
 * for reference, define the entity's group, instead of type
 * therefore, this wraper can contain all the entity type for that group
 */
public class HAPReferenceWraper extends HAPDataWraper{

	/*********************** Init / Clear up ***************************/
	HAPReferenceWraper(HAPDataTypeDefInfo dataTypeDefInfo, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(dataTypeDefInfo, dataTypeMan, entityDefMan);
		this.setEmpty();
	}

	HAPReferenceWraper(HAPEntityID id, HAPDataTypeDefInfo dataTypeDefInfo, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(dataTypeDefInfo, dataTypeMan, entityDefMan);
		this.setData(new HAPReferenceWraperData(id, dataTypeMan.getDataType(dataTypeDefInfo.getDataTypeInfo())));
	}

	HAPReferenceWraper(String path, HAPDataTypeDefInfo dataTypeDefInfo, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan){
		super(dataTypeDefInfo, dataTypeMan, entityDefMan);
		this.setData(new HAPReferenceWraperData(path, dataTypeMan.getDataType(dataTypeDefInfo.getDataTypeInfo())));
	}
	
	@Override
	public void initAttributeData() {
		this.setData(null);
	}
	
	//*************************** Clear Up
	@Override
	void clearUPData(Map<String, Object> scope) {
		this.setData(null);
	}

	@Override
	void breakExternalLink(){
		if(!this.isEmpty()){
			this.getCurrentTransaction().breakEntityReference(this.getReferencePath(), this.getIDData());
		}
	}
	
	
	/*********************** Manage Data  ***************************/
	public int getReferenceType(){
		if(this.isEmpty())  return -1;
		HAPReferenceWraperData referenceData = (HAPReferenceWraperData)this.getData();
		return referenceData.getReferenceType();
	}
	
	public HAPEntityID getIDData(){
		HAPEntityID out = null;
		switch(this.getReferenceType()){
		case HAPReference.REFERENCE_ID:
			HAPReferenceWraperData referenceData = (HAPReferenceWraperData)this.getData();
			out = referenceData.getReferenceID();
			break;
		case HAPReference.REFERENCE_PATH:
			HAPEntityWraper entityWraper = this.getReferenceWraper();
			if(entityWraper!=null)		out = entityWraper.getID();
			break;
		default:
			break;
		}
		return out;
	}

	public String getPathData(){
		if(this.isEmpty())  return null;
		HAPReferenceWraperData referenceData = (HAPReferenceWraperData)this.getData();
		return referenceData.getReferencePath();
	}
	
	public HAPEntityWraper getReferenceWraper(){
		HAPEntityWraper out = null;
		switch(this.getReferenceType()){
		case HAPReference.REFERENCE_ID:
			HAPEntityID refID = this.getIDData();
			out = (HAPEntityWraper)this.getEntityByID(refID).getData();
			break;
		case HAPReference.REFERENCE_PATH:
			String path = this.getPathData();
			int k = path.indexOf("\\..");
			HAPDataWraper dataWraper = this;
			while(k!=-1){
				out = dataWraper.getParentEntity().getWraper();
				if(path.length()<=3){
					path = null;
					k = -1;
				}
				else{
					path = path.substring(3);
					k = path.indexOf("\\..");
				}
				dataWraper = out;
			}

			if(path!=null){
				k = path.indexOf("\\.");
				if(k!=-1){
					path = path.substring(2);
					out = (HAPEntityWraper)out.getChildWraperByPath(path);
				}
			}
			
			break;
		default:
			break;
		}
		return out;
	}
	
	@Override
	protected HAPDataWraper getChildWraper(String attribute) {
		HAPEntityWraper wraper = this.getReferenceWraper();
		return wraper.getChildWraper(attribute);
	}

	@Override
	protected Set<HAPDataWraper> getGenericChildWraper(String pathEle){
		HAPEntityWraper wraper = this.getReferenceWraper();
		return wraper.getGenericChildWraper(pathEle);
	}
	
	public HAPReferenceWraperData getReferenceData(){return (HAPReferenceWraperData)this.getData();	}
	
	protected void setReferenceID(HAPEntityID ID){
		this.setData(new HAPReferenceWraperData(ID, HAPEntityDataTypeUtility.getReferenceAttributeDataType(this.getDataTypeManager())));
	}
	
	@Override
	public boolean isEmpty(){return this.getData()==null;}
	
	//*************************** Operation
	@Override
	public void prepareReverseOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){
		HAPEntityID entityID = this.getIDData();
		operation.setExtra(entityID);
	}
	
	@Override
	protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps) {
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_REFERENCE_SET:
			HAPEntityID ID = operation.getRefEntityID();
			this.setReferenceID(ID);
			break;
//		case ENTITYOPERATION_ATTR_REFERENCE_CLEAR:
//			this.setEmpty();
//			break;
		}
		return HAPServiceData.createSuccessData(this.getData());
	}

	@Override
	protected void externalOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_REFERENCE_SET:
			HAPReferenceInfoAbsolute refPath = this.getReferencePath(); 

			HAPEntityID entityID1 = (HAPEntityID)operation.getExtra();
			if(entityID1!=null){
				this.getCurrentTransaction().breakEntityReference(refPath, entityID1);
			}
			
			HAPEntityID entityID = (HAPEntityID)operation.getData();
			if(entityID!=null){
				this.getCurrentTransaction().buildEntityReference(refPath, entityID);
			}

			break;
//		case ENTITYOPERATION_ATTR_REFERENCE_CLEAR:
//			break;
		}
	}
}
