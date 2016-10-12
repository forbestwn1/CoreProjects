package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPValueInfoImporterXML {

	public static HAPValueInfo importFromXml(String fileName, Class resourceClass){
		HAPValueInfo out = null;
		try{
			InputStream xmlStream = HAPFileUtility.getInputStreamOnClassPath(resourceClass, fileName);
			out = importFromXML(xmlStream);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	public static HAPValueInfo importFromXML(InputStream xmlStream){
		HAPValueInfo out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = readRootValueInfoFromElement(rootEle);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	private static HAPValueInfo readRootValueInfoFromElement(Element valueInfoEle){
		//by default, root value info type is entity
		String propertyType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.TYPE);
		if(propertyType==null)      propertyType = HAPConstant.STRINGALBE_VALUEINFO_ENTITY;

		HAPValueInfo out = readValueInfoFromElement(valueInfoEle, propertyType);
		String name = out.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = valueInfoEle.getTagName();
			out.setName(name);
		}
		return out;
	}
	
	private static HAPValueInfo readValueInfoFromElement(Element valueInfoEle, String valueInfoType){
		HAPValueInfo valueInfo = null;
		
		String reference = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfoReference.ENTITY_PROPERTY_REFERENCE);
		if(reference!=null){
			//for property reference to another property
			valueInfo = HAPValueInfoReference.build(); 
			updateBasicProperty(valueInfoEle, valueInfo);
		}
		else{
			String propertyType = valueInfoType;
			if(propertyType==null)  propertyType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.TYPE);
			
			if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(propertyType)){
				valueInfo = HAPValueInfoList.build(); 
				updateBasicProperty(valueInfoEle, valueInfo);
				readContainerChildValueInfo(valueInfoEle, valueInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(propertyType)){
				valueInfo = HAPValueInfoMap.build();
				updateBasicProperty(valueInfoEle, valueInfo);
				readContainerChildValueInfo(valueInfoEle, valueInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(propertyType)){
				valueInfo = readEntityValueInfo(valueInfoEle);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyType)){
				valueInfo = HAPValueInfoEntityOptions.build();
				updateBasicProperty(valueInfoEle, valueInfo);
				HAPStringableValueMap optionsInfoMap = (HAPStringableValueMap)valueInfo.updateComplexChild(HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS, HAPConstant.STRINGABLE_VALUECATEGARY_MAP);
				Element[] optionEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfoEntityOptions.ENTITY_PROPERTY_OPTIONS);
				for(Element optionEle : optionEles){
					HAPValueInfo optionPropertyInfo = readValueInfoFromElement(optionEle, null);
					optionsInfoMap.updateChild(optionPropertyInfo.getBasicAncestorValueString(HAPValueInfoEntityOptions.ENTITY_PROPERTY_VALUE), optionPropertyInfo);
				}
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MODE.equals(propertyType)){
				valueInfo = HAPValueInfoMode.build();
				updateBasicProperty(valueInfoEle, valueInfo);
				HAPStringableValueEntity elesAttrs = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES, HAPConstant.STRINGABLE_VALUECATEGARY_ENTITY);
				String[] eleTypes = {
									HAPConstant.STRINGALBE_VALUEINFO_BASIC, 
									HAPConstant.STRINGALBE_VALUEINFO_ENTITY, 
									HAPConstant.STRINGALBE_VALUEINFO_MAP,
									HAPConstant.STRINGALBE_VALUEINFO_LIST,
									HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS,
				}; 
				for(String eleType : eleTypes){
					Element dataTypeEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, eleType);
					if(dataTypeEle!=null){
						HAPValueInfoEntity eleValueInfoEntity = (HAPValueInfoEntity)readValueInfoFromElement(dataTypeEle, HAPConstant.STRINGALBE_VALUEINFO_ENTITY);
						elesAttrs.updateChild(eleType, eleValueInfoEntity);
					}
				}
			}
			else{
				valueInfo = HAPValueInfoBasic.build();
				updateBasicProperty(valueInfoEle, valueInfo);
			}
		}
		
		return valueInfo;
	}
	
	private static HAPValueInfoEntity readEntityValueInfo(Element entityEle){
		HAPValueInfoEntity valueInfo = HAPValueInfoEntity.build();
		updateBasicProperty(entityEle, valueInfo);
		HAPStringableValueEntity propertyInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES, HAPConstant.STRINGABLE_VALUECATEGARY_ENTITY);
		Element[] childPropertyEles = HAPXMLUtility.getMultiChildElementByName(entityEle, HAPValueInfoEntity.ENTITY_PROPERTY_PROPERTIES);
		for(Element childPropertyEle : childPropertyEles){
			HAPValueInfo childPropertyInfo = readValueInfoFromElement(childPropertyEle, null);
			propertyInfoEntity.updateChild(childPropertyInfo.getName(), childPropertyInfo);
		}
		return valueInfo;
	}
	
	private static HAPValueInfo readContainerChildValueInfo(Element valueInfoEle, HAPValueInfo containerValueInfo){
		Element childEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, HAPValueInfoList.ENTITY_PROPERTY_CHILD);
		HAPValueInfo childPropertyInfo = readValueInfoFromElement(childEle, null);
		containerValueInfo.updateChild(HAPValueInfoList.ENTITY_PROPERTY_CHILD, childPropertyInfo);
		return childPropertyInfo;
	}
	
	private static void updateBasicProperty(Element element, HAPStringableValueEntity entity){
		Map<String, String> propertyAttrs = HAPXMLUtility.getAllAttributes(element);
		entity.updateBasicChildrens(propertyAttrs);
	}
}
