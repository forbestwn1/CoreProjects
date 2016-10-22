package com.nosliw.entity.persistent.mango;

import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntity;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReference;
import com.nosliw.entity.data.HAPReferenceWraperData;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPAttributeDefinitionContainer;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityDataUtility;

public class HAPEntityImportUtil {

	public static HAPEntityWraper importEntity(Document doc, String type, HAPEntityDefinitionManager entityDefMan, HAPDataTypeManager dataTypeMan){
		HAPEntityData entityData = importEntityData(doc, type, entityDefMan, dataTypeMan);
		HAPEntityWraper valueWraper = new HAPEntityWraper(entityData, dataTypeMan, entityDefMan); 

		//set entity id
		String id = doc.getString("id"); 
		if(HAPBasicUtility.isStringNotEmpty(id)){
			valueWraper.setID(new HAPEntityID("", type, id));
			valueWraper.setId(id);
		}
		return valueWraper;
	}
	
	/*
	 * create complex entity from element
	 */
	private static HAPEntityData importEntityData(Document entityDoc, String type, HAPEntityDefinitionManager entityDefMan, HAPDataTypeManager dataTypeMan)
	{
		HAPEntityDefinitionCritical entityInfo = entityDefMan.getEntityDefinition(type);
		
		//get critical value from node
		String criticalValue = getCriticalValue(entityDoc, entityInfo);
		
		//create wraper with entity with empty attributes
		HAPEntity entityDataType = HAPEntityDataUtility.getEntityDataType(type, dataTypeMan);
		HAPEntityData out = entityDataType.newEmptyEntity(criticalValue);
		
		//set attribute value
		importEntityAttributes(entityDoc, out, entityDefMan, dataTypeMan);
		
		return out;
	}
	
	/*
	 * read all attributes into entity from element
	 */
	private static void importEntityAttributes(Document entityDoc, HAPEntityData entity, HAPEntityDefinitionManager entityDefMan, HAPDataTypeManager dataTypeMan)
	{
		Map<String, HAPAttributeDefinition> attrDefs = entity.getAttributeDefinitions();
		for(String attrName : attrDefs.keySet()){
			HAPAttributeDefinition attrDef = attrDefs.get(attrName);
			//for each attribute defined in entity
			importEntityAttribute(entityDoc, attrDef, entity, entityDefMan, dataTypeMan);
		}
	}
	
	/*
	 * import attribute value into entity according to entityElement and attribute
	 * return wraper
	 * if cannot find 
	 */
	private static void importEntityAttribute(Document entityDoc, HAPAttributeDefinition attrDef, HAPEntityData parent, HAPEntityDefinitionManager entityDefMan, HAPDataTypeManager dataTypeMan)
	{
		String attrName = attrDef.getName();
		HAPDataTypeDefInfo attrType = attrDef.getDataTypeDefinitionInfo();

		HAPDataWraper attrWraper = parent.getAttributeValueWraper(attrDef.getName());
		
		Document attrDoc = (Document)entityDoc.get("data."+attrName);
		
		if(attrDoc!=null){
			if(HAPEntityDataTypeUtility.isAtomType(attrType)){ 
				String value = attrDoc.getString("data");
				if(value!=null){
					attrWraper.parse(value);
				}
			}
			else if(HAPEntityDataTypeUtility.isContainerType(attrType)){
				HAPEntityContainerAttributeWraper containerWraper = (HAPEntityContainerAttributeWraper)attrWraper;
				if(attrDoc!=null){
					//get container element
					List<Document> eleList = (List<Document>)attrDoc.get("data");
					for(Document eleDoc : eleList){
						importContainerElement(eleDoc, (HAPEntityContainerAttributeWraper)attrWraper, (HAPAttributeDefinitionContainer)attrDef, entityDefMan, dataTypeMan);
					}
				}
			}
			else if(HAPEntityDataTypeUtility.isEntityType(attrType))
			{
				//our defined entity type
				if(attrDoc!=null){
					HAPEntityData entity = importEntityData(attrDoc, attrType.getType(), entityDefMan, dataTypeMan);
					attrWraper.setData(entity);
				}
			}
			else if(HAPEntityDataTypeUtility.isReferenceType(attrType)){
				//our defined entity type
				HAPReference refDataType = (HAPReference)dataTypeMan.getDataType(attrType.getDataTypeInfo());
				String refId = attrDoc.getString("refid");
				if(refId.indexOf("\\..")==-1){
					HAPEntityID id = new HAPEntityID("", refId);
					attrWraper.setData(refDataType.createReferenceData(id));
				}
				else{
					attrWraper.setData(refDataType.createReferenceData(refId));
				}
			}
		}
	}	
	
	private static HAPDataWraper importContainerElement(Document eleDoc, 
														HAPEntityContainerAttributeWraper containerWraper, 
														HAPAttributeDefinitionContainer attrDef,
														HAPEntityDefinitionManager entityDefMan, HAPDataTypeManager dataTypeMan
														)
	{
		HAPDataWraper out = null;
		String eleId = eleDoc.getString("id"); 
		HAPDataTypeDefInfo cType = attrDef.getChildDataTypeDefinitionInfo();
		if(HAPEntityDataTypeUtility.isAtomType(cType)){
			String content = eleDoc.getString("data.data"); 
			HAPData data = dataTypeMan.parseString(content, cType.getCategary(), cType.getType());
			out = containerWraper.getContainerData().addElementData(data, eleId);
		}
		else if(HAPEntityDataTypeUtility.isContainerType(cType)){
			//not appliable for container, because container cannot be element of another container
		}
		else if(HAPEntityDataTypeUtility.isEntityType(cType)){
			HAPEntityData entity = importEntityData(eleDoc, cType.getType(), entityDefMan, dataTypeMan);
			out = containerWraper.getContainerData().addElementData(entity, eleId);
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(cType)){
			String refId = eleDoc.getString("refid"); 
			HAPReferenceWraperData data = null;
			if(HAPBasicUtility.isStringNotEmpty(refId)){
				HAPReference refDataType = (HAPReference)dataTypeMan.getDataType(cType.getDataTypeInfo());
				if(refId.indexOf("\\..")==-1){
					HAPEntityID id = new HAPEntityID("", refId);
					data = refDataType.createReferenceData(id);
				}			
				else{
					data = refDataType.createReferenceData(refId);
				}
			}
			out = containerWraper.getContainerData().addElementData(data, eleId);
		}

		//set ele ID
		if(eleId!=null)  out.setId(eleId);		
		
		return out;
	}

	
	private static String getCriticalValue(Document entityDoc, HAPEntityDefinitionCritical entityInfo)
	{
		String criticalValue = null;
		HAPAttributeDefinition criticalAttributeDef = entityInfo.getCriticalAttributeDefinition();
		if(criticalAttributeDef != null){
			criticalValue = entityDoc.getString("data."+criticalAttributeDef.getName()+".data"); 
		}
		return criticalValue;
	}
	
}
