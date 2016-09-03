package com.nosliw.entity.utils;

import java.util.Iterator;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPDataWraperTask;
import com.nosliw.entity.data.HAPEntity;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;

public class HAPEntityDataUtility {

	private static long m_nextID = System.currentTimeMillis(); 
	public static String getNextID(){
		String out = String.valueOf(m_nextID);
		m_nextID++;
		return out;
	}
	
	static public HAPEntity getEntityDataType(String type, HAPDataTypeManager dataTypeMan){
		return (HAPEntity)dataTypeMan.getDataType(new HAPDataTypeInfo(HAPConstant.DATATYPE_CATEGARY_ENTITY, type));
	}

	static public HAPEntityWraper getRootEntityParentWraper(HAPDataWraper wraper){
		HAPEntityData parentEntity = wraper.getParentEntity();
		if(parentEntity==null && HAPEntityDataTypeUtility.isEntityType(wraper))  return (HAPEntityWraper)wraper;
		HAPEntityWraper parentWraper = parentEntity.getWraper();
		if(parentWraper==null)  return null;
		else return getRootEntityParentWraper(parentWraper);
	}
	
	static public String getRootEntityAttributePath(HAPDataWraper wraper){
		HAPEntityData parentEntity = wraper.getParentEntity();
		if(parentEntity==null)  return null;
		String rootPath = getRootEntityAttributePath(parentEntity.getWraper());
		if(rootPath!=null)	return HAPNamingConversionUtility.cascadePath(rootPath, wraper.getParentEntityAttributePath()); 
		else return wraper.getParentEntityAttributePath();
	}

	/*
	static public HAPEntityWraper[] removeEntityReference(HAPEntityWraper entityWraper){
		List<HAPEntityWraper> out = new ArrayList<HAPEntityWraper>();
		HAPTransactionDataAccess dataAccess = entityWraper.getDataAccess();
		HAPReferencePath[] refPaths = entityWraper.getReferences();
		for(HAPReferencePath path : refPaths){
			HAPEntityWraper entityW = (HAPEntityWraper)dataAccess.getEntityByID(path.getEntityID()).getSuccessData();
			HAPDataWraper attrWraper = entityW.getChildPathWraper(path.getAttrPath());
			attrWraper.doOperateValue(HAPConstant.OPERATION_ATTR_ATOM_SET, null);
//			attrWraper.setEmpty();
			out.add(entityW);
		}
		return out.toArray(new HAPEntityWraper[0]);
	}
*/
	
	public static void iterateAllEntity(HAPEntityDataAccess dataAccess, Object data, HAPDataWraperTask task){
		Object out = data;
		Set<HAPEntityWraper> entitys = dataAccess.getAllTransitEntitys();
		for(HAPEntityWraper wraper : entitys){
			HAPEntityDataUtility.iterateEntityWraper(wraper, out, task);
		}
	}
	
	
	public static void iterateEntityAttributes(HAPEntityWraper entityWraper, Object data, HAPDataWraperTask task){
		Object out = data;
		HAPEntityData entity = entityWraper.getEntityData();
		if(entity!=null){
			iterateEntityAttributes(entity, out, task);
		}
	}

	public static void iterateEntityAttributes(HAPEntityData entity, Object data, HAPDataWraperTask task){
		Object out = data;
		String[] attributes = entity.getAttributes();
		for(int i=attributes.length-1; i>=0; i--){
			HAPDataWraper attrWraper = entity.getAttributeValueWraper(attributes[i]);
			task.process(attrWraper, out).getData();
		}
	}
	
	public static void iterateEntityWraper(HAPEntityWraper entityWraper, Object data, HAPDataWraperTask task){
		Object out = data;
		HAPEntityData entity = entityWraper.getEntityData();
		if(entityWraper.isEmpty())  return;
		HAPServiceData serviceData = task.process(entityWraper, out);
		if(serviceData!=null && serviceData.getData()!=null)		out = serviceData.getData();
		if(serviceData==null || serviceData.isSuccess()){
			String[] attributes = entity.getAttributes();
			for(String attr : attributes){
				HAPDataWraper attrWraper = entity.getAttributeValueWraper(attr);
				if(HAPEntityDataTypeUtility.isAtomType(attrWraper)){
					task.process(attrWraper, out);
				}				
				else if(HAPEntityDataTypeUtility.isContainerType(attrWraper)){
					serviceData = task.process(attrWraper, out);
					Object out1 = out;
					if(serviceData.getData()!=null)  out1 = serviceData.getData();
					if(serviceData==null || serviceData.isSuccess()){
						HAPEntityContainerAttributeWraper containerWraper = (HAPEntityContainerAttributeWraper)attrWraper;
						Iterator<HAPDataWraper> it = containerWraper.getContainerData().iterate();
						while(it.hasNext()){
							HAPDataWraper eleWraper = it.next();
							if(HAPEntityDataTypeUtility.isEntityType(eleWraper)){
								HAPEntityDataUtility.iterateEntityWraper(((HAPEntityWraper)eleWraper), out1, task);
							}
							else if(HAPEntityDataTypeUtility.isReferenceType(eleWraper)){
								task.process(eleWraper, out1);
							}
							else{
								task.process(eleWraper, out1);
							}
						}
					}
				}
				else if(HAPEntityDataTypeUtility.isEntityType(attrWraper)){
					HAPEntityWraper comWraper = (HAPEntityWraper)attrWraper;
					HAPEntityDataUtility.iterateEntityWraper(comWraper, out, task);
				}
				else if(HAPEntityDataTypeUtility.isReferenceType(attrWraper)){
					task.process(attrWraper, out);
				}
				else if(HAPEntityDataTypeUtility.isReferenceType(attrWraper)){
					task.process(attrWraper, out);
				}
			}			
		}
	}

	
	/*
	 * method used to merge from entity to to entity
	 * rule : 
	 * 		for attribute in from, if it also in to entity, 
	 */
//	public static void mergeEntity(HAPEntityData from, HAPEntityData to)
//	{
//		//clear all the attribute not exist in criticalEnity
//		//clear all the attribute from criticalEntity that exist in this entity
//		
//		for(String attr : from.getAttributes()){
//			HAPAttributeDefinition toAttr = to.getAttributeDefinition(attr);
//			if(toAttr==null){
//				HAPDataWraper fromAttrWraper = from.getAttributeValueWraper(attr);
//				from.removeAttribute(attr);
//			}
//			else{
//				to.removeAttribute(attr);
//			}
//		}
//		
//		//add new attribute to this
//		for(String attr : from.getAttributes()){
//			moveAttribute(attr, from, to);
//		}
//	}
	
	/*
	 * move attribute value from on entity to another entity
	 */
//	public static void moveAttribute(String attr, HAPEntityData from, HAPEntityData to){
//		HAPDataWraper fromWraper = from.dropAttribute(attr);
//		to.addAttributeValue(attr, fromWraper);
//	}
}
