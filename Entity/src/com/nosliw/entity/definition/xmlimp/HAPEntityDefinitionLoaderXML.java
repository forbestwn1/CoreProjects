package com.nosliw.entity.definition.xmlimp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.common.utils.HAPXMLUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeDefInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.definition.HAPEntityDefinitionCritical;
import com.nosliw.entity.definition.HAPEntityDefinitionLoader;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinition;
import com.nosliw.entity.options.HAPOptionsDefinitionDynamicAttribute;
import com.nosliw.entity.options.HAPOptionsDefinitionDynamic;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.options.HAPOptionsDefinitionStatic;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.expression.HAPExpressionInfo;

/*
 * name : basic type
 * .name : same package
 * ..name : common package
 */

public class HAPEntityDefinitionLoaderXML extends HAPEntityDefinitionLoader{
	

	public static final String PACKAGE_COMMON = "common";
	
	private Document[] m_docs = null;
	public HAPEntityDefinitionLoaderXML(String name, Document[] doc, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan)
	{
		super(dataTypeMan, entityDefMan, optionsMan);
		this.m_docs = doc;
	}

	public HAPEntityDefinitionLoaderXML(String name, InputStream[] inputs, HAPDataTypeManager dataTypeMan, HAPEntityDefinitionManager entityDefMan, HAPOptionsDefinitionManager optionsMan)
	{
		super(dataTypeMan, entityDefMan, optionsMan);
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			List<Document> docs = new ArrayList<Document>();
			for(InputStream input : inputs)
			{
				if(input != null)
				{
					Document entityDoc = DOMbuilder.parse(input);
					docs.add(entityDoc);
				}
			}
			this.m_docs = docs.toArray(new Document[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public Set<HAPEntityDefinitionCritical> loadEntityDefinition() {
		Set<HAPEntityDefinitionCritical> out = new HashSet<HAPEntityDefinitionCritical>();
		for(Document doc : this.m_docs){
			Set<HAPEntityDefinitionCritical> entities = this.readEntities(doc);
			out.addAll(entities);
		}
		return out;
	}		
	
	private Set<HAPEntityDefinitionCritical> readEntities(Document doc)
	{
		Set<HAPEntityDefinitionCritical> out = new HashSet<HAPEntityDefinitionCritical>();
		
		//read meta data information
		NodeList metadata = doc.getElementsByTagName(HAPEntityDefinitionLoaderXmlUtility.TAG_METADATA);
		HAPEntityDefinitionMeta meta = null;
		if(metadata.getLength()>0)	meta = new HAPEntityDefinitionMeta((Element)metadata.item(0));
		else meta = new HAPEntityDefinitionMeta();
		
		//read entity definition
		Element[] elements = HAPXMLUtility.getMultiChildElementByName(doc.getDocumentElement(), HAPEntityDefinitionLoaderXmlUtility.TAG_ENTITY);
		for(Element element : elements){
			HAPEntityDefinitionCritical entityDef = readEntityDefinition(element, meta);
			entityDef.afterLoad();
			//add new entity definition here, rather than waiting for loder loading all entity then add them to entity definition manager
			//it is because some entity definition may need other entity definition infor, therefore, add them to entity def manager as soon as possible
			this.getEntityDefinitionManager().addEntityDefinition(entityDef);
			out.add(entityDef);
		}
		return out;
	}

	/*
	 * for read real entity
	 */
	HAPEntityDefinitionCritical readEntityDefinition(Element element, HAPEntityDefinitionMeta metadata)
	{
		HAPEntityDefinitionSegment entityDefBasic = readEntityDefinitionBasic(null, element, this.getEntityDefinitionManager().getDefaultClassName(), metadata);
		HAPEntityDefinitionCritical entityDef = new HAPEntityDefinitionCritical(entityDefBasic);

		//read attribute
		Element dataEle = HAPXMLUtility.getFirstChildElementByName(element, HAPEntityDefinitionLoaderXmlUtility.TAG_DATA);
		readAttributes(dataEle, entityDef, metadata);
		return entityDef;
	}
	
	/*
	 * for read critical attributes
	 */
	HAPEntityDefinitionSegment readCriticalEntityDefinition(String name, Element element, HAPEntityDefinitionMeta metadata, String backupClassName)
	{
		HAPEntityDefinitionSegment entityDef = readEntityDefinitionBasic(name, element, backupClassName, metadata);
		readAttributes(element, entityDef, metadata);
		return entityDef;
	}
	
	/*
	 * read element and create entity definition basic object
	 * entityName: if null, read from element
	 * backupBaseClass:  if not defined in element, use this one
	 */
	private HAPEntityDefinitionSegment readEntityDefinitionBasic(String entityName, Element element, String backupBaseClass, HAPEntityDefinitionMeta metadata){
		String fullName = entityName;
		if(HAPBasicUtility.isStringEmpty(entityName)){
			String name = element.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_NAME);
			//get full entity name based on metadata
			fullName = HAPEntityDefinitionLoaderXmlUtility.getEntityFullName(name, metadata);
		}
		
		//get group infor, if no group infor, then its group name is its entity name
		Set<String> groups = new HashSet<String>();
		String group = element.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_GROUP);
		if(HAPBasicUtility.isStringEmpty(group)){
			//not defined, use entity name as group name
			groups.add(entityName);
		}
		else{
			HAPSegmentParser groupSegs = new HAPSegmentParser(group, HAPConstant.SEPERATOR_ELEMENT);
			while(groupSegs.hasNext()){
				groups.add(groupSegs.next());
			}
		}
		
		//class name
		String className = element.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_CLASS);
		className = HAPEntityDefinitionLoaderXmlUtility.getFullClassName(className, metadata, backupBaseClass);
		
		HAPEntityDefinitionSegment entityDef = new HAPEntityDefinitionSegment(fullName, className, groups, this.getEntityDefinitionManager());
		return entityDef;
	}
	
	void readAttributes(Element element, HAPEntityDefinitionSegment entityDef, HAPEntityDefinitionMeta metadata)
	{
		Element[] elements = HAPXMLUtility.getMultiChildElementByName(element, HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE);
		for(Element ele : elements){
			HAPAttributeDefinition attrDef = readAttribute(ele, entityDef, metadata);
			entityDef.addAttributeDefinition(attrDef);
		}
	}

	HAPAttributeDefinition readAttribute(Element ele, HAPEntityDefinitionSegment entityDefinition, HAPEntityDefinitionMeta metadata)
	{
		HAPAttributeDefinition out = null;
		
		String type = ele.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_ATTR_DATATYPE);
		String categary = ele.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_ATTR_DATACATEGARY);
		HAPDataTypeDefInfo dataTypeInfo = HAPEntityDefinitionLoaderXmlUtility.getDataType(categary, type, metadata, this.getDataTypeManager());
		
		if(HAPEntityDataTypeUtility.isAtomType(dataTypeInfo)){
			out = new HAPAttributeDefinitionAtomXml(ele, entityDefinition, metadata, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		}
		else if(HAPEntityDataTypeUtility.isContainerType(dataTypeInfo))
		{
			out = new HAPContainerAttributeDefinitionXML(ele, entityDefinition, metadata, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
			
			//read container element information
			Element childEle = HAPXMLUtility.getFirstChildElementByName(ele, HAPEntityDefinitionLoaderXmlUtility.TAG_ELEMENT);
			HAPAttributeDefinition eleAttrDef = readAttribute(childEle, entityDefinition, metadata);
			eleAttrDef.setContainerElementAttr();
			((HAPContainerAttributeDefinitionXML)out).setChildAttributeDefinition(eleAttrDef);
		}
		else if(HAPEntityDataTypeUtility.isEntityType(dataTypeInfo))
		{
			out = new HAPEntityAttributeDefinitionXML(ele, entityDefinition, metadata, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		}
		else if(HAPEntityDataTypeUtility.isReferenceType(dataTypeInfo)){
			out = new HAPReferenceAttributeDefinitionXML(ele, entityDefinition, metadata, this.getDataTypeManager(), this.getEntityDefinitionManager(), this.getOptionsManager());
		}
		
		//read options
		HAPOptionsDefinition options = readOptions(ele, out, entityDefinition, metadata);
		if(options!=null)  out.setOptionsDefinition(options);
		
		return out;
	}
	
	HAPOptionsDefinition readOptions(Element ele, HAPAttributeDefinition attributeDef, HAPEntityDefinitionSegment entityDefinition, HAPEntityDefinitionMeta metadata){
		String categary = attributeDef.getDataTypeDefinitionInfo().getCategary();
		String type = attributeDef.getDataTypeDefinitionInfo().getType();
		
		HAPOptionsDefinition sourceOptions = null;

		//if no options name, use this options name based on naming conversion
		String optionsName = HAPEntityDefinitionLoaderXmlUtility.createOptionsName(entityDefinition, attributeDef);
		
		String staticOptionsName = ele.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_ATTR_OPTIONS);
		if(HAPBasicUtility.isStringNotEmpty(staticOptionsName)){
			//static options by reference to name
			sourceOptions = this.getOptionsManager().getOptionsDefinition(staticOptionsName);
		}
		else{
			Element optionsEle = HAPXMLUtility.getFirstChildElementByName(ele, HAPEntityDefinitionLoaderXmlUtility.TAG_OPTIONS);
			if(optionsEle != null){
				String baseOptionName = optionsEle.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_OPTIONS_BASE);
				String optionQuery = optionsEle.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_OPTIONS_QUERY);
				if(HAPBasicUtility.isStringNotEmpty(baseOptionName)){
					HAPOptionsDefinition baseOptions = this.getOptionsManager().getOptionsDefinition(baseOptionName);
					switch(baseOptions.getType()){
					case HAPConstant.OPTIONS_TYPE_STATIC:
						//static options
						sourceOptions = baseOptions;
						break;
					case HAPConstant.OPTIONS_TYPE_DYNAMIC:
						//dynamic options
						Element[] optionParmEles = HAPXMLUtility.getMultiChildElementByName(optionsEle, HAPEntityDefinitionLoaderXmlUtility.TAG_OPTIONS_PARM);
						Map<String, HAPExpressionInfo> optionsParms = new LinkedHashMap<String, HAPExpressionInfo>();
						for(Element optionsParmEle : optionParmEles){
							String parmName = optionsParmEle.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_OPTIONS_PARM_NAME);
							String parmValue = optionsParmEle.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_OPTIONS_PARM_VALUE);
							optionsParms.put(parmName, new HAPExpressionInfo(parmValue, null, null));
						}
						sourceOptions = new HAPOptionsDefinitionDynamicAttribute(optionsName, (HAPOptionsDefinitionDynamic)baseOptions, optionsParms, attributeDef, "", this.getDataTypeManager());
						break;
					}
				}
				else if(HAPBasicUtility.isStringNotEmpty(optionQuery)){
					//query options
					
				}
				else{
					//static options
					Element[] optionEles = HAPXMLUtility.getMultiChildElementByName(optionsEle, HAPEntityDefinitionLoaderXmlUtility.TAG_OPTION);
					List<HAPData> optionsData = new ArrayList<HAPData>();
					//if critical have "other" value, then no options should be created
					boolean isOption = true;
					for(Element optionEle : optionEles){
						String value = optionEle.getAttribute(HAPEntityDefinitionLoaderXmlUtility.TAG_ATTRIBUTE_OPTION_VALUE);
						if(((HAPAttributeDefinitionAtomXml)attributeDef).getIsCritical()){
							if(HAPBasicUtility.isStringNotEmpty(value)){
								HAPSegmentParser valueSegs = new HAPSegmentParser(value, HAPConstant.SEPERATOR_ELEMENT);
								while(valueSegs.hasNext()){
									String v = valueSegs.next();
									HAPEntityDefinitionSegment entityDef = this.readCriticalEntityDefinition(entityDefinition.getEntityName(), optionEle, metadata, entityDefinition.getBaseClassName());
									for(String attrName : entityDef.getAttributeNames()){
										HAPAttributeDefinition attrDef = entityDef.getAttributeDefinitionByName(attrName);
										((HAPAttributeDefinition)attrDef).setCriticalValue(v);
									}
									((HAPEntityDefinitionCritical)entityDefinition).addCriticalEntitySegmentValue(v, entityDef);
									optionsData.add(this.getDataTypeManager().parseString(v, categary, type));
								}
							}else{
								HAPEntityDefinitionSegment entityDef = this.readCriticalEntityDefinition(entityDefinition.getEntityName(), optionEle, metadata, entityDefinition.getBaseClassName()); 
								for(String attrName : entityDef.getAttributeNames()){
									HAPAttributeDefinition attrDef = entityDef.getAttributeDefinitionByName(attrName);
									((HAPAttributeDefinition)attrDef).setCriticalValue(HAPConstant.ENTITY_CRITICALVALUE_OTHER);
								}
								((HAPEntityDefinitionCritical)entityDefinition).setCriticalEntitySegmentOther(entityDef);
//								optionsData.add(HAPConstant.ENTITY_CRITICALVALUE_OTHER);
								isOption = false;
							}
						}
						else{
							//not critical attribute
							if(HAPBasicUtility.isStringNotEmpty(value)){
								HAPSegmentParser valueSegs = new HAPSegmentParser(value, HAPConstant.SEPERATOR_ELEMENT);
								while(valueSegs.hasNext()){
									String v = valueSegs.next();
									optionsData.add(this.getDataTypeManager().parseString(v, categary, type));
								}
							}
						}
					}
					if(isOption)	sourceOptions = new HAPOptionsDefinitionStatic(optionsName, attributeDef.getDataTypeDefinitionInfo(), optionsData, "", this.getDataTypeManager());
				}
			}
		}
		
		return sourceOptions;
	}
}
