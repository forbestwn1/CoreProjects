package com.nosliw.entity.persistent.mango;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data.datatype.HAPDataTypeInfo;
import com.nosliw.entity.data.HAPAtomWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;

public class HAPEntityExportUtil {

	public static Document exportEntity(HAPEntityWraper entity){
		Document doc = new Document();
		
		doc.append("_id", new ObjectId(entity.getID().toString()));
		doc.append("id", entity.getId());
		doc.append("ID", entity.getID().toString());
		
		Document dataDoc = new Document();
		
		HAPEntityData entityData = entity.getEntityData();
		String[] attrs = entityData.getAttributes();
		if(attrs == null)  return doc;
		
		for(String attr : attrs)
		{
			if(entityData.getAttributeValueWraper(attr).isEmpty())  continue;
			
			HAPAttributeDefinition attrDef = entityData.getAttributeDefinition(attr);
			HAPDataTypeDefInfo attrType = attrDef.getDataTypeDefinitionInfo();
			String attrName = attrDef.getName();
			if(HAPEntityDataTypeUtility.isAtomType(attrType)){
				HAPAtomWraper atomData = (HAPAtomWraper)entityData.getAttributeValueWraper(attrName);
				Document attrDoc = createAtomDocument(atomData);
				dataDoc.append(attrName, attrDoc);
			}
			else if(HAPEntityDataTypeUtility.isContainerType(attrType)){
				HAPEntityContainerAttributeWraper containerValueWraper = (HAPEntityContainerAttributeWraper)entityData.getAttributeValueWraper(attrName);
				Document attrDoc = createContainerDocument(containerValueWraper);
				dataDoc.append(attrName, attrDoc);
				
			}	
			else if(HAPEntityDataTypeUtility.isEntityType(attrType))
			{
				HAPEntityWraper entityAttrWraper = (HAPEntityWraper)entityData.getAttributeValueWraper(attrName);
				if(!entityAttrWraper.isEmpty()){
					Document attrDoc = exportEntity(entityAttrWraper);
					dataDoc.append(attrName, attrDoc);
				}
			}
			else if(HAPEntityDataTypeUtility.isReferenceType(attrType)){
				HAPReferenceWraper refData = (HAPReferenceWraper)entityData.getAttributeValueWraper(attrName);
				Document attrDoc = createReferenceDocument(refData);
				dataDoc.append(attrName, attrDoc);
			}
		}
		
		doc.append("data", dataDoc);
		
		return doc;
	}

	private static Document createContainerDocument(HAPEntityContainerAttributeWraper containerValueWraper){
		Document doc = new Document();
		
		List<Document> elementArray = new ArrayList<Document>();
		Iterator<HAPDataWraper> eleIterator = containerValueWraper.iterate();
		while(eleIterator.hasNext()){
			HAPDataWraper value = eleIterator.next();

			Document eleDoc = new Document();
			eleDoc.append("id", value.getId());
			
			if(HAPEntityDataTypeUtility.isAtomType(value)){
				//atom
				eleDoc.append("data", createAtomDocument((HAPAtomWraper)value));
			}
			else if(HAPEntityDataTypeUtility.isEntityType(value)){
				eleDoc.append("data", exportEntity((HAPEntityWraper)value));
			}
			else if(HAPEntityDataTypeUtility.isReferenceType(value)){
				eleDoc.append("data", createReferenceDocument((HAPReferenceWraper)value));
			}
			elementArray.add(eleDoc);
		}

		doc.append("data", elementArray);
		return doc;
	}
	
	private static Document createReferenceDocument(HAPReferenceWraper referenceAttrWraper){
		Document doc = new Document();

		if(!referenceAttrWraper.isEmpty()){
			doc.append("type", referenceAttrWraper.getReferenceType());
			doc.append("refid", referenceAttrWraper.getReferenceData().toStringValue(HAPSerializationFormat.JSON));
		}
		
		return doc;
	}
	
	private static Document createAtomDocument(HAPAtomWraper atomData){
		Document doc = new Document();
		
		HAPDataTypeInfo dataTypeInfo = atomData.getData().getDataType().getDataTypeInfo();
		Document attrDataTypeDoc = new Document();
		attrDataTypeDoc.append("categary", dataTypeInfo.getCategary());
		attrDataTypeDoc.append("type", dataTypeInfo.getType());
		doc.append("dataType", attrDataTypeDoc);
		
		doc.append("data", atomData.getData().toStringValue(HAPSerializationFormat.JSON));
		return doc;
	}
}

