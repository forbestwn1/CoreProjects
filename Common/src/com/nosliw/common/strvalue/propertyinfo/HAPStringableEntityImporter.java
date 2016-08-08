package com.nosliw.common.strvalue.propertyinfo;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueBasic;
import com.nosliw.common.strvalue.basic.HAPStringableValueComplex;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.basic.HAPStringableValueList;
import com.nosliw.common.strvalue.basic.HAPStringableValueMap;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPStringableEntityImporter {

	public static HAPStringableValueEntity importStringableEntity(InputStream xmlStream, HAPValueInfoEntity entityInfo){
		HAPStringableValueEntity out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = processEntityValue(rootEle, entityInfo);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
 
	private static HAPStringableValue readPropertyValueOfEntity(Element propertyContainerEle, HAPValueInfo propertyInfo){
		String propertyName = propertyInfo.getName();
		
		HAPStringableValue out = null;
		try{
			String propertyCategary = propertyInfo.getCategary();
			if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(propertyCategary)){
				Element listEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processListValue(listEle, (HAPValueInfoList)propertyInfo);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(propertyCategary)){
				Element mapEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processMapValue(mapEle, (HAPValueInfoMap)propertyInfo);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(propertyCategary)){
				Element entityEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processEntityValue(entityEle, (HAPValueInfoEntity)propertyInfo);
			}
			else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary)){
				out = processEntityOptionsValue(propertyContainerEle, (HAPValueInfoEntityOptions)propertyInfo);
			}
			else{
				String value = HAPXMLUtility.getAttributeValue(propertyContainerEle, propertyName); 
				out = processBasicValue(value, (HAPValueInfoBasic)propertyInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	private static HAPStringableValueBasic processBasicValue(String strValue, HAPValueInfoBasic valueInfo){
		HAPStringableValueBasic out = null;
		String type = valueInfo.getBasicAncestorValueString(HAPValueInfo.ATTR_TYPE);
		String defaultValue = valueInfo.getBasicAncestorValueString(HAPValueInfoBasic.ATTR_DEFAULTVALUE);
		out = new HAPStringableValueBasic(strValue, type, defaultValue);
		if(out.isEmpty())  out = null;
		return out;
	}

	private static HAPStringableValueComplex processComplexValue(Element element, HAPValueInfo valueInfo){
		HAPStringableValueComplex out = null;
		String categary = valueInfo.getCategary();
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			out = processEntityValue(element, (HAPValueInfoEntity)valueInfo);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(categary)){
			out = processListValue(element, (HAPValueInfoList)valueInfo);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(categary)){
			out = processMapValue(element, (HAPValueInfoMap)valueInfo);
		}
		return out;
	}

	private static HAPStringableValue processEntityOptionsValue(Element containerEle, HAPValueInfoEntityOptions valueInfo){
		String propertyName = valueInfo.getName();

		String optionsKey = valueInfo.getBasicAncestorValueString(HAPValueInfoEntityOptions.ATTR_KEY);
		String keyValue = HAPXMLUtility.getAttributeValue(containerEle, optionsKey);
		HAPStringableValueEntity optionsValueInfo = (HAPStringableValueEntity)valueInfo.getChild(HAPValueInfoEntityOptions.ATTR_OPTIONS);
		HAPValueInfo optionValueInfo = (HAPValueInfo)optionsValueInfo.getChild(keyValue);
		String categary = optionValueInfo.getCategary();
		HAPStringableValue out = null;
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(categary)){
			String value = HAPXMLUtility.getAttributeValue(containerEle, propertyName); 
			out = processBasicValue(value, (HAPValueInfoBasic)optionValueInfo);
		}
		else{
			Element optionEle = HAPXMLUtility.getFirstChildElementByName(containerEle, propertyName);
			out = processComplexValue(optionEle, optionValueInfo);
		}
		return out;
	}
	
	private static HAPStringableValueMap processMapValue(Element mapEle, HAPValueInfoMap valueInfo){
		HAPStringableValueMap map = new HAPStringableValueMap();
		HAPValueInfo childInfo = (HAPValueInfo)valueInfo.getAncestorByPath(HAPValueInfoMap.ATTR_CHILD);
		String childCategary = childInfo.getCategary();

		String mapKey = valueInfo.getBasicAncestorValueString(HAPValueInfoMap.ATTR_KEY);
		Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(mapEle, "element");
		for(Element eleEle : eleEles){
			HAPStringableValue mapElementValue = null;
			String keyValue = HAPXMLUtility.getAttributeValue(eleEle, mapKey);
			if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
				String basicValue = eleEle.getTextContent();
				mapElementValue = processBasicValue(basicValue, (HAPValueInfoBasic)childInfo);
			}		
			else{
				mapElementValue = processComplexValue(eleEle, childInfo);
			}
			map.addElement(keyValue, mapElementValue);
		}
		return map;
	}
	
	private static HAPStringableValueList processListValue(Element listEle, HAPValueInfoList valueInfo){
		HAPStringableValueList list = new HAPStringableValueList();
		
		HAPValueInfo childInfo = (HAPValueInfo)valueInfo.getAncestorByPath(HAPValueInfoList.ATTR_CHILD);
		String childCategary = childInfo.getCategary();
		
		Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(listEle, "element");
		for(Element eleEle : eleEles){
			HAPStringableValue listElementValue = null;
			if(HAPConstant.CONS_STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
				String basicValue = eleEle.getTextContent();
				listElementValue = processBasicValue(basicValue, (HAPValueInfoBasic)childInfo);
			}
			else{
				listElementValue = processComplexValue(eleEle, childInfo);
			}
			if(listElementValue!=null)  list.addElement(listElementValue); 
		}
		return list;
	}
	
	private static HAPStringableValueEntity processEntityValue(Element entityEle, HAPValueInfoEntity valueInfo){
		HAPStringableValueEntity out = null;
		try{
			String className = valueInfo.getBasicAncestorValueString(HAPValueInfoEntity.ATTR_CLASSNAME);
			HAPStringableValueEntity entityPropertiesInfo = (HAPStringableValueEntity)valueInfo.getAncestorByPath(HAPValueInfoEntity.ATTR_PROPERTIES);
			boolean isMandatory = valueInfo.getBasicAncestorValueBoolean(HAPValueInfoEntity.ATTR_MANDATORY);
			
			HAPStringableValueEntity entity = (HAPStringableValueEntity)Class.forName(className).newInstance();
			
			for(String property : entityPropertiesInfo.getProperties()){
				HAPStringableValue entityProperty = readPropertyValueOfEntity(entityEle, (HAPValueInfo)entityPropertiesInfo.getAncestorByPath(property));
				entity.addChild(property, entityProperty);
			}
			if(!isMandatory && entity.isEmpty())   out = null;
			else out = entity;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
}
