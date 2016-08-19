package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPValueInfoImporterXML {

	public static HAPValueInfo importFromXML(InputStream xmlStream, HAPValueInfoManager valueInfoMan){
		HAPValueInfo out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = readRootValueInfoFromElement(rootEle, valueInfoMan);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
	 
	private static HAPValueInfo readRootValueInfoFromElement(Element valueInfoEle, HAPValueInfoManager valueInfoMan){
		//by default, root value info type is entity
		String propertyType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.ENTITY_PROPERTY_TYPE);
		if(propertyType==null)      propertyType = HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY;

		HAPValueInfo out = readValueInfoFromElement(valueInfoEle, propertyType, valueInfoMan);
		String name = out.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = valueInfoEle.getTagName();
			out.setName(name);
		}
		return out;
	}
	
	private static HAPValueInfo readValueInfoFromElement(Element valueInfoEle, String valueInfoType, HAPValueInfoManager valueInfoMan){
		HAPValueInfo valueInfo = null;
		
		String reference = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfoReference.ENTITY_PROPERTY_REFERENCE);
		if(reference!=null){
			//for property reference to another property
			valueInfo = HAPValueInfoReference.build(valueInfoMan); 
			updateBasicProperty(valueInfoEle, valueInfo, valueInfoMan);
		}
		else{
			String propertyType = valueInfoType;
			if(propertyType==null)  propertyType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.ENTITY_PROPERTY_TYPE);
			
			if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(propertyType)){
				valueInfo = HAPValueInfoList.build(); 
				updateBasicProperty(valueInfoEle, valueInfo, valueInfoMan);
				readContainerChildValueInfo(valueInfoEle, valueInfo, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(propertyType)){
				valueInfo = HAPValueInfoMap.build();
				updateBasicProperty(valueInfoEle, valueInfo, valueInfoMan);
				readContainerChildValueInfo(valueInfoEle, valueInfo, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(propertyType)){
				valueInfo = readEntityValueInfo(valueInfoEle, valueInfoMan);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyType)){
				valueInfo = HAPValueInfoEntityOptions.build();
				updateBasicProperty(valueInfoEle, valueInfo, valueInfoMan);
				HAPStringableValueEntity optionsInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS, HAPConstant.CONS_STRINGABLE_VALUECATEGARY_ENTITY);
				Element[] optionEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS);
				for(Element optionEle : optionEles){
					HAPValueInfo optionPropertyInfo = readValueInfoFromElement(optionEle, null, valueInfoMan);
					optionsInfoEntity.updateChild(optionPropertyInfo.getBasicAncestorValueString(HAPValueInfoEntityOptions.ENTITY_PROPERTY_VALUE), optionPropertyInfo);
				}
			}
			else{
				valueInfo = HAPValueInfoBasic.build();
				updateBasicProperty(valueInfoEle, valueInfo, valueInfoMan);
			}
		}
		
		return valueInfo;
	}
	
	private static HAPValueInfoEntity readEntityValueInfo(Element entityEle, HAPValueInfoManager valueInfoMan){
		HAPValueInfoEntity valueInfo = HAPValueInfoEntity.build(valueInfoMan);
		updateBasicProperty(entityEle, valueInfo, valueInfoMan);
		HAPStringableValueEntity propertyInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES, HAPConstant.CONS_STRINGABLE_VALUECATEGARY_ENTITY);
		Element[] childPropertyEles = HAPXMLUtility.getMultiChildElementByName(entityEle, HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES);
		for(Element childPropertyEle : childPropertyEles){
			HAPValueInfo childPropertyInfo = readValueInfoFromElement(childPropertyEle, null, valueInfoMan);
			propertyInfoEntity.updateChild(childPropertyInfo.getName(), childPropertyInfo);
		}
		return valueInfo;
	}
	
	private static HAPValueInfo readContainerChildValueInfo(Element valueInfoEle, HAPValueInfo containerValueInfo, HAPValueInfoManager valueInfoMan){
		Element childEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, HAPValueInfoList.ENTITY_PROPERTY_CHILD);
		HAPValueInfo childPropertyInfo = readValueInfoFromElement(childEle, null, valueInfoMan);
		containerValueInfo.updateChild(HAPValueInfoList.ENTITY_PROPERTY_CHILD, childPropertyInfo);
		return childPropertyInfo;
	}
	
	private static void updateBasicProperty(Element element, HAPStringableValueEntity entity, HAPValueInfoManager valueInfoMan){
		Map<String, String> propertyAttrs = HAPXMLUtility.getAllAttributes(element);
		entity.updateBasicChildrens(propertyAttrs);
	}
}
