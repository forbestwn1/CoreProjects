package com.nosliw.entity.persistent.xmlfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPXMLUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntity;
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
import com.nosliw.entity.utils.HAPEntityUtility;

/*
 * Helper class to read entity from xml file
 */
public class HAPXMLEntityImporter {
	
	private static final String TAG_ELEMENT = "element";

	private static final String ATTRIBUTE_Id = "id";
	private static final String ATTRIBUTE_REFID = "refid";
	private static final String ATTRIBUTE_NEXTID = "nextid";

	/*
	 * entity type from tag name
	 */
	public static HAPEntityWraper[] readEntities(InputStream[] streams)
	{
		List<HAPEntityWraper> out = new ArrayList<HAPEntityWraper>();
		DocumentBuilder DOMbuilder = null;
		DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
		try {
			DOMbuilder = DOMfactory.newDocumentBuilder();
		} catch (Exception e) {    
			e.printStackTrace();
		}

		for(InputStream stream : streams){
			Document doc = null;
			try {
				doc = DOMbuilder.parse(stream);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			HAPEntityWraper[] entities = readEntities(doc);
			for(HAPEntityWraper entity : entities)  out.add(entity);
		}
		return out.toArray(new HAPEntityWraper[0]);
	}	
	
	/*
	 * read entity from inputstream, given the type info
	 */
	public static HAPEntityWraper[] readEntities(InputStream stream, String type)
	{
		HAPEntityWraper[] out = new HAPEntityWraper[0];
		try {
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			
			Document doc = DOMbuilder.parse(stream);
			out = readEntities(doc, type);
		} catch (Exception e) {    
			e.printStackTrace();
		}
		return out;
	}	
	
	/*
	 * read entity from document, get type info from tag name
	 */
	public static HAPEntityWraper[] readEntities(Document doc)
	{
		List<HAPEntityWraper> out = new ArrayList<HAPEntityWraper>();
		Element[] eles = HAPXMLUtility.getChildElements(doc.getDocumentElement());
		for(Element ele : eles){
			String type = ele.getTagName();
			HAPEntityWraper wraper = importEntityDataWraper(ele, type);
			out.add(wraper);
		}
		return out.toArray(new HAPEntityWraper[0]);
	}	
	
	/*
	 * read entity from document, given the type name
	 */
	public static HAPEntityWraper[] readEntities(Document doc, String type)
	{
		List<HAPEntityWraper> out = new ArrayList<HAPEntityWraper>();
		
		Element[] eles = HAPXMLUtility.getMultiChildElementByName(doc.getDocumentElement(), type);
		for(Element ele : eles){
			HAPEntityWraper wraper = importEntityDataWraper(ele, type);
			out.add(wraper);
		}
		return out.toArray(new HAPEntityWraper[0]);
	}	

	private static HAPEntityWraper importEntityDataWraper(Element element, String type){
		HAPEntityData entityData = importEntityData(element, type);
		HAPEntityWraper valueWraper = new HAPEntityWraper(entityData, getDataTypeManager(), getEntityDefinitionManager()); 

		//set entity id
		String id = element.getAttribute(ATTRIBUTE_Id);
		if(HAPBasicUtility.isStringNotEmpty(id)){
			valueWraper.setID(new HAPEntityID("", type, id));
			valueWraper.setId(id);
		}
		return valueWraper;
	}
	
	/*
	 * create complex entity from element
	 */
	private static HAPEntityData importEntityData(Element element, String type)
	{
		HAPEntityDefinitionCritical entityInfo = getEntityDefinitionManager().getEntityDefinition(type);
		
		//get critical value from node
		String criticalValue = getCriticalValue(element, entityInfo);
		
		//create wraper with entity with empty attributes
		HAPEntity entityDataType = HAPEntityDataUtility.getEntityDataType(type, getDataTypeManager());
		HAPEntityData out = entityDataType.newEmptyEntity(criticalValue);
		
		//set attribute value
		importEntityAttributes(element, out);
		
		return out;
	}
	
	
	/*
	 * read all attributes into entity from element
	 */
	private static void importEntityAttributes(Element element, HAPEntityData entity)
	{
		Map<String, HAPAttributeDefinition> attrs = entity.getAttributeDefinitions();
		for(String attrName : attrs.keySet()){
			HAPAttributeDefinition attrDef = attrs.get(attrName);
			//for each attribute defined in entity
			importEntityAttribute(element, attrDef, entity);
		}
	}
	
	/*
	 * import attribute value into entity according to entityElement and attribute
	 * return wraper
	 * if cannot find 
	 */
	private static void importEntityAttribute(Element entityEle, HAPAttributeDefinition attrDef, HAPEntityData parent)
	{
		String attrName = attrDef.getName();
		HAPDataTypeDefInfo attrType = attrDef.getDataTypeDefinitionInfo();

		HAPDataWraper attrWraper = parent.getAttributeValueWraper(attrDef.getName());
		
		if(HAPEntityDataTypeUtility.isAtomType(attrType)){ 
			String value = entityEle.getAttribute(attrName);
			if(value!=null){
				attrWraper.parse(value);
			}
		}
		else if(HAPEntityDataTypeUtility.isContainerType(attrType)){
			HAPEntityContainerAttributeWraper containerWraper = (HAPEntityContainerAttributeWraper)attrWraper;
			Element containerEle = HAPXMLUtility.getFirstChildElementByName(entityEle, attrName);
			if(containerEle != null){
				//get container element
				Element[] eles = HAPXMLUtility.getMultiChildElementByName(containerEle, TAG_ELEMENT);
				for(Element ele : eles){
					importContainerElement(ele, (HAPEntityContainerAttributeWraper)attrWraper, (HAPAttributeDefinitionContainer)attrDef);
				}
			}
		}
		else if(HAPEntityDataTypeUtility.isEntityType(attrType))
		{
			//our defined entity type
			Element attributeEle = HAPXMLUtility.getFirstChildElementByName(entityEle, attrName);
			if(attributeEle != null){
				HAPEntityData entity = importEntityData(attributeEle, attrType.getType());
				attrWraper.setData(entity);
			}
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(attrType)){
			//our defined entity type
			Element attributeEle = HAPXMLUtility.getFirstChildElementByName(entityEle, attrName);
			if(attributeEle != null){
				HAPReference refDataType = (HAPReference)getDataTypeManager().getDataType(attrType.getDataTypeInfo());
				String refId = attributeEle.getAttribute(ATTRIBUTE_REFID);
				if(refId.indexOf("\\..")==-1){
					HAPEntityID id = new HAPEntityID("", refId);
					if(HAPBasicUtility.isStringEmpty(id.getUserContext()))  id.setUserContext("");
					attrWraper.setData(refDataType.createReferenceData(id));
				}
				else{
					attrWraper.setData(refDataType.createReferenceData(refId));
				}
			}
		}
	}	
	
	private static HAPDataWraper importContainerElement(Element ele, 
														HAPEntityContainerAttributeWraper containerWraper, 
														HAPAttributeDefinitionContainer attrDef
														)
	{
		HAPDataWraper out = null;
		String eleId = ele.getAttribute(ATTRIBUTE_Id);
		HAPDataTypeDefInfo cType = attrDef.getChildDataTypeDefinitionInfo();
		if(HAPEntityDataTypeUtility.isAtomType(cType)){
			String content = ele.getTextContent();
			HAPData data = getDataTypeManager().parseString(content, cType.getCategary(), cType.getType());
			out = containerWraper.getContainerData().addElementData(data, eleId);
		}
		else if(HAPEntityDataTypeUtility.isContainerType(cType)){
			//not appliable for container, because container cannot be element of another container
		}
		else if(HAPEntityDataTypeUtility.isEntityType(cType)){
			HAPEntityData entity = importEntityData(ele, cType.getType());
			out = containerWraper.getContainerData().addElementData(entity, eleId);
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(cType)){
			String refId = ele.getAttribute(ATTRIBUTE_REFID);
			HAPReferenceWraperData data = null;
			if(HAPBasicUtility.isStringNotEmpty(refId)){
				HAPReference refDataType = (HAPReference)getDataTypeManager().getDataType(cType.getDataTypeInfo());
				if(refId.indexOf("\\..")==-1){
					HAPEntityID id = new HAPEntityID("", refId);
					if(HAPBasicUtility.isStringEmpty(id.getUserContext()))  id.setUserContext("");
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
	
	private static String getCriticalValue(Element element, HAPEntityDefinitionCritical entityInfo)
	{
		String criticalValue = null;
		HAPAttributeDefinition criticalAttributeDef = entityInfo.getCriticalAttributeDefinition();
		if(criticalAttributeDef != null){
			criticalValue = element.getAttribute(criticalAttributeDef.getName());
		}
		return criticalValue;
	}
	
	private static HAPDataTypeManager getDataTypeManager(){return null;}
	private static HAPEntityDefinitionManager getEntityDefinitionManager(){return null;}
}	

