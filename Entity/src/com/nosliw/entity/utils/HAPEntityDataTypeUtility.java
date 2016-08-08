package com.nosliw.entity.utils;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.info.HAPDataTypeInfo;
import com.nosliw.entity.data.HAPAtomWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityContainerAttribute;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReference;
import com.nosliw.entity.data.HAPReferenceWraper;

public class HAPEntityDataTypeUtility {

	public static boolean isAtomType(HAPDataTypeInfo type){	return HAPConstant.CONS_DATATYPE_CATEGARY_BLOCK.equals(type.getCategary()) || HAPConstant.CONS_DATATYPE_CATEGARY_SIMPLE.equals(type.getCategary());	}
	public static boolean isAtomType(HAPDataWraper value){	return value instanceof HAPAtomWraper; }

	public static boolean isContainerType(HAPDataTypeInfo type){return HAPConstant.CONS_DATATYPE_CATEGARY_CONTAINER.equals(type.getCategary());	}
	public static boolean isContainerType(HAPDataWraper value){	return (value instanceof HAPEntityContainerAttributeWraper);}

	public static boolean isEntityType(HAPDataTypeInfo type){return HAPConstant.CONS_DATATYPE_CATEGARY_ENTITY.equals(type.getCategary());}
	public static boolean isEntityType(HAPDataWraper value){return value instanceof HAPEntityWraper;}
	
	public static boolean isReferenceType(HAPDataTypeInfo type){return HAPConstant.CONS_DATATYPE_CATEGARY_REFERENCE.equals(type.getCategary());}
	public static boolean isReferenceType(HAPDataWraper value){	return value instanceof HAPReferenceWraper;	}
	

	public static boolean isCompositType(HAPDataTypeInfo type){return isContainerType(type) || isEntityType(type);}
	public static boolean isCompositType(HAPDataWraper value){return isEntityType(value) || isContainerType(value);}	
	
	public static boolean isSolidEntityType(HAPDataWraper value){	return value instanceof HAPEntityWraper;  }


	public static HAPEntityContainerAttribute getContainerAttributeDataType(HAPDataTypeManager dataTypeMan){
		return (HAPEntityContainerAttribute)dataTypeMan.getDataType(new HAPDataTypeInfo(HAPConstant.CONS_DATATYPE_CATEGARY_CONTAINER, HAPConstant.CONS_DATATYPE_TYPE_CONTAINER_ENTITYATTRIBUTE));
	}

	public static HAPReference getReferenceAttributeDataType(HAPDataTypeManager dataTypeMan){
		return (HAPReference)dataTypeMan.getDataType(new HAPDataTypeInfo(HAPConstant.CONS_DATATYPE_CATEGARY_REFERENCE, HAPConstant.CONS_DATATYPE_TYPE_REFERENCE_NORMAL));
	}
	
	/*
	 * get all referenced entity Id 
	 */
	public static Set<HAPEntityID> getAllChildReferenceEntityID(HAPEntityWraper entityWraper){
		Set<HAPEntityID> out = new HashSet<HAPEntityID>();
		
		if(entityWraper.getEntityData()==null)	return out;
		
		String[] atts = entityWraper.getEntityData().getAttributes();
		for(String attr : atts){
			HAPDataWraper attrWraper = entityWraper.getEntityData().getAttributeValueWraper(attr);
			if(HAPEntityDataTypeUtility.isReferenceType(attrWraper)){
				//for reference attribute
				if(!attrWraper.isEmpty()){
					if(((HAPReferenceWraper)attrWraper).getIDData()!=null){
						out.add(((HAPReferenceWraper)attrWraper).getIDData());
					}
				}
			}
			else if(HAPEntityDataTypeUtility.isContainerType(attrWraper)){
				//for container attribute, check element type
				HAPEntityContainerAttributeWraper containerAttr = (HAPEntityContainerAttributeWraper)attrWraper;
				if(HAPConstant.CONS_DATATYPE_CATEGARY_REFERENCE.equals(containerAttr.getChildDataTypeDefInfo().getDataTypeInfo().getCategary())){
					//for entity reference element 
					HAPDataWraper[] eleWrapers = containerAttr.getContainerData().getElementWrapers();
					for(HAPDataWraper eleWraper : eleWrapers){
						if(HAPEntityDataTypeUtility.isReferenceType(eleWraper)){
							if(!eleWraper.isEmpty()){
								if(((HAPReferenceWraper)eleWraper).getIDData()!=null){
									out.add(((HAPReferenceWraper)eleWraper).getIDData());
								}
							}
						}
					}
				}
				else if(HAPConstant.CONS_DATATYPE_CATEGARY_ENTITY.equals(containerAttr.getChildDataTypeDefInfo().getDataTypeInfo().getCategary())){
					//for entity element, get all child reference for entity element
					HAPDataWraper[] eleWrapers = containerAttr.getContainerData().getElementWrapers();
					for(HAPDataWraper eleWraper : eleWrapers){
						if(HAPEntityDataTypeUtility.isEntityType(eleWraper)){
							if(!eleWraper.isEmpty()){
								out.addAll(getAllChildReferenceEntityID((HAPEntityWraper)eleWraper));
							}
						}
					}
				}
			}
			else if(HAPEntityDataTypeUtility.isEntityType(attrWraper)){
				//for entity attribute 
				out.addAll(getAllChildReferenceEntityID((HAPEntityWraper)attrWraper));
			}
		}
		return out;
	}

}
