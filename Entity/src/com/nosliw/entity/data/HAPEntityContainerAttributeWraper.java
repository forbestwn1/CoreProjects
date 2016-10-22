package com.nosliw.entity.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinitionAtom;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPAttributeDefinitionContainer;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.utils.HAPNamingUtility;

/*
 * relationship between container and element
 * 		the parent of element : the entity
 * 		the listener of element : the container 
 * the possible event between container and element
 * 		operation : atom element
 * 		entity change: entity update
 * 		reference : 
 * 				clear up
 * 				entity update
 * 
 */
public class HAPEntityContainerAttributeWraper extends HAPDataWraper{

	/*
	 * operations supported by ContainerValueWraper
	 * OPERATION_ADDELEMENT: 
	 * 		add element to container
	 * OPERATION_REMOVEELEMENT:
	 * 		remove element from container
	 * OPERATION_MODIFYELEMENT
	 * 		the content of element is changed
	 */

	HAPEntityContainerAttributeWraper(HAPDataTypeDefInfo dataTypeDefInfo, HAPDataTypeManager dataTypeManager, HAPEntityDefinitionManager entityDefMan) {
		super(dataTypeDefInfo, dataTypeManager, entityDefMan);
		this.setData(new HAPEntityContainerAttributeWraperData(this, dataTypeManager.getDataType(dataTypeDefInfo.getDataTypeInfo()), this.getEntityDefinitionManager()));
	}
	
	@Override
	protected void initAttributeData(){}
	
	//*************************** Clear Up
	@Override
	void clearUPData(Map<String, Object> scope){
		this.getContainerData().clearUp(scope);
	}
	
	//*************************** Basic Info
	public int getSize() {	return this.getContainerData().getSize();	}

	public Iterator<HAPDataWraper> iterate(){
		return this.getContainerData().iterate();
	}
	
	public HAPDataWraper getElement(String id){return this.getContainerData().getElementWraper(id);}
	
	public HAPEntityContainerAttributeWraperData getContainerData(){	return (HAPEntityContainerAttributeWraperData)this.getData(); }

	@Override
	public boolean isEmpty(){return this.getSize()==0;}
	
	public HAPDataTypeDefInfo getChildDataTypeDefInfo(){
		HAPAttributeDefinitionContainer attrDef = (HAPAttributeDefinitionContainer)this.getAttributeDefinition();
		return attrDef.getChildDataTypeDefinitionInfo();
	}

	@Override
	protected HAPDataWraper getChildWraper(String attribute){
		HAPDataWraper out = null;
		
		String keyword = HAPNamingConversionUtility.getKeyword(attribute);
		if(keyword==null){
			out = this.getElement(attribute);	
		}
		else if(HAPConstant.ATTRIBUTE_PATH_EACH.equals(keyword)){
			HAPWraperContainerWraper wraper = new HAPWraperContainerWraper(this.getChildDataTypeDefInfo(), this.getDataTypeManager(), this.getEntityDefinitionManager());
			HAPWraperContainerData wraperContainerData = wraper.getWraperContainerData();
			Iterator<HAPDataWraper> its = this.iterate();
			while(its.hasNext()){
				wraperContainerData.addWraper(its.next());
			}
			out = wraper;
		}
		return out;
	}
	
	@Override
	public void setEmpty(){	this.getContainerData().removeAllElementDatas();}
	
	@Override
	protected Set<HAPDataWraper> getGenericChildWraper(String pathEle){
		Set<HAPDataWraper> out = new HashSet<HAPDataWraper>();
		if("*".equals(pathEle)){
			for(HAPDataWraper wraper : this.getContainerData().getElementWrapers()){
				out.add(wraper);
			}
		}
		else{
			out.add(this.getContainerData().getElementWraper(pathEle));
		}
		return out;
	}

	//**********************  Operation
	@Override
	protected HAPServiceData doOperate(HAPEntityOperationInfo operation, List<HAPEntityOperationInfo> extraOps){
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_ELEMENT_DELETE:
		{
			String id = operation.getElementId();
			HAPDataWraper eleWraper = this.getContainerData().removeElementData(id);
			return HAPServiceData.createSuccessData(eleWraper);
		}
		case ENTITYOPERATION_ATTR_ELEMENT_NEW:
		{
			String id = operation.getElementId();
			String eleId = HAPEntityDataTypeUtility.getContainerAttributeDataType(this.getDataTypeManager()).newContainerElement(this, id);
			operation.setExtra(this.getChildWraper(eleId).toStringValue(HAPSerializationFormat.JSON));
			
			HAPDataTypeInfo childDataType = this.getChildDataTypeDefInfo();
			if(HAPEntityDataTypeUtility.isEntityType(childDataType)){
				HAPEntityWraper eleEntityWraper = (HAPEntityWraper)this.getElement(eleId);

				Map<String, String> parms = operation.getParms();
				//critical attr first
				Set<String> criticalAttrs = new HashSet<String>();
				for(String attr : parms.keySet()){
					String value = parms.get(attr);
					HAPDataWraper attrWraper = eleEntityWraper.getChildWraperByPath(attr);
					if(attrWraper!=null){
						HAPAttributeDefinition attrDef = attrWraper.getAttributeDefinition();
						HAPDataTypeDefInfo attrDataTypeInfo = attrDef.getDataTypeDefinitionInfo();
						if(HAPEntityDataTypeUtility.isAtomType(attrDataTypeInfo)){
							if(((HAPAttributeDefinitionAtom)attrDef).getIsCritical()){
								criticalAttrs.add(attr);
								HAPEntityOperationInfo opInfo = HAPEntityOperationFactory.createAttributeAtomSetOperationByString(eleEntityWraper.getID(), attr, value);
								this.getCurrentTransaction().operate(opInfo);
							}
						}
					}
				}
				
				//set parms value to attribute
				for(String attr : parms.keySet()){
					if(!parms.containsValue(attr)){
						HAPEntityOperationInfo opInfo = null;
						String value = parms.get(attr);
						
						HAPAttributeDefinition attrDef = eleEntityWraper.getChildWraperByPath(attr).getAttributeDefinition();
						HAPDataTypeDefInfo attrDataTypeInfo = attrDef.getDataTypeDefinitionInfo();
						String categary = attrDataTypeInfo.getCategary();
						if(HAPEntityDataTypeUtility.isAtomType(attrDataTypeInfo)){
							opInfo = HAPEntityOperationFactory.createAttributeAtomSetOperationByString(eleEntityWraper.getID(), attr, value);
						}
						else if(HAPEntityDataTypeUtility.isReferenceType(attrDataTypeInfo)){
							opInfo = HAPEntityOperationFactory.createAttributeReferenceSetOperation(eleEntityWraper.getID(), attr, new HAPEntityID(value));
						}
						extraOps.add(opInfo);
					}
				}
			}
			
			return HAPServiceData.createSuccessData(eleId);
		}
		}
		
		return HAPServiceData.createFailureData();
	}

	@Override
	public void prepareReverseOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_ELEMENT_DELETE:
		{
			String id = operation.getElementId();
			operation.setExtra(this.getElement(id).cloneWraper());
			break;
		}
		case ENTITYOPERATION_ATTR_ELEMENT_NEW:
		{
			operation.setExtra(serviceData.getData());
			break;
		}
		}
	}
	
	@Override
	protected void postOperation(HAPEntityOperationInfo operation, HAPServiceData serviceData){
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_ELEMENT_DELETE:
		{
			HAPDataWraper eleWraper = (HAPEntityWraper)serviceData.getData();
			eleWraper.clearUp(operation.getScope());
			break;
		}
		}
	}

	//******************************  Clone
	protected void cloneData(HAPData data){
		HAPEntityContainerAttributeWraperData containerData = (HAPEntityContainerAttributeWraperData)data;
		this.getContainerData().cloneFrom(containerData);
	}

	@Override
	void setParentEntity(HAPEntityData parentEntity, String attributePath, HAPAttributeDefinition attrDef){
		super.setParentEntity(parentEntity, attributePath, attrDef);
		HAPAttributeDefinitionContainer attrDefinition = (HAPAttributeDefinitionContainer)this.getAttributeDefinition();
		for(HAPDataWraper eleWraper : this.getContainerData().getElementWrapers()){
			eleWraper.setParentEntity(this.getParentEntity(), HAPNamingConversionUtility.buildPath(this.getParentEntityAttributePath(), eleWraper.getId()), attrDefinition.getChildAttributeDefinition());
		}
	}
}
