package com.nosliw.common.strvalue.valueinfo;

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
import com.nosliw.common.strvalue.basic.HAPStringableValueUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPStringableEntityImporterXML {

	private static String TAG_CONTAINERCHILD = "element";
	
	public static HAPStringableValueEntity readRootEntity(InputStream xmlStream){
		HAPStringableValueEntity out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = readRootEntity(rootEle);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
 
	public static HAPStringableValueEntity readRootEntity(Element entityEle){
		String valueInfoName = entityEle.getTagName();
		HAPStringableValueEntity out = processEntityValue(entityEle, (HAPValueInfoEntity)HAPValueInfoManager.getInstance().getValueInfo(valueInfoName).getSolidValueInfo());
		return out;
	}

	public static HAPStringableValueEntity readRootEntity(Element entityEle, String valueInfoName){
		HAPStringableValueEntity out = processEntityValue(entityEle, (HAPValueInfoEntity)HAPValueInfoManager.getInstance().getValueInfo(valueInfoName).getSolidValueInfo());
		return out;
	}
	
	private static HAPStringableValue readPropertyValueOfEntity(Element propertyContainerEle, HAPValueInfo propertyValueInfo){
		String propertyName = propertyValueInfo.getName();
		
		HAPStringableValue out = null;
		try{
			HAPValueInfo propertyInfo = propertyValueInfo.getSolidValueInfo();
			String propertyCategary = propertyInfo.getCategary();
			if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(propertyCategary)){
				Element listEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processListValue(listEle, (HAPValueInfoList)propertyInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(propertyCategary)){
				Element mapEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processMapValue(mapEle, (HAPValueInfoMap)propertyInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(propertyCategary)){
				Element entityEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processEntityValue(entityEle, (HAPValueInfoEntity)propertyInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary)){
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
	
	private static HAPStringableValueBasic processBasicValue(String strValue, HAPValueInfoBasic basicValueInfo){
		HAPStringableValueBasic out = null;
		if(strValue!=null)		out = new HAPStringableValueBasic(strValue, basicValueInfo.getValueDataType());		
		else		out = (HAPStringableValueBasic)basicValueInfo.buildDefault();		
		if(HAPStringableValueUtility.isStringableValueEmpty(out))  out = null;
		return out;
	}

	private static HAPStringableValueComplex processComplexValue(Element element, HAPValueInfo valueInfo){
		HAPStringableValueComplex out = null;
		String categary = valueInfo.getCategary();
		if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(categary)){
			out = processEntityValue(element, (HAPValueInfoEntity)valueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(categary)){
			out = processListValue(element, (HAPValueInfoList)valueInfo);
		}
		else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(categary)){
			out = processMapValue(element, (HAPValueInfoMap)valueInfo);
		}
		return out;
	}

	private static HAPStringableValue processEntityOptionsValue(Element containerEle, HAPValueInfoEntityOptions entityOptionsValueInfo){
		String propertyName = entityOptionsValueInfo.getName();

		String optionsKey = entityOptionsValueInfo.getBasicAncestorValueString(HAPValueInfoEntityOptions.ENTITY_PROPERTY_KEY);
		String keyValue = HAPXMLUtility.getAttributeValue(containerEle, optionsKey);
		HAPValueInfo optionValueInfo = entityOptionsValueInfo.getOptionsValueInfo(keyValue).getSolidValueInfo();
		String categary = optionValueInfo.getCategary();
		HAPStringableValue out = null;
		if(HAPConstant.STRINGALBE_VALUEINFO_BASIC.equals(categary)){
			String value = HAPXMLUtility.getAttributeValue(containerEle, propertyName); 
			out = processBasicValue(value, (HAPValueInfoBasic)optionValueInfo);
		}
		else{
			Element optionEle = HAPXMLUtility.getFirstChildElementByName(containerEle, propertyName);
			out = processComplexValue(optionEle, optionValueInfo);
		}
		return out;
	}
	
	private static HAPStringableValueMap processMapValue(Element mapEle, HAPValueInfoMap mapValueInfo){
		HAPStringableValueMap map = (HAPStringableValueMap)mapValueInfo.buildDefault();
		
		if(mapEle!=null){
			HAPValueInfo childInfo = mapValueInfo.getChildValueInfo().getSolidValueInfo();
			String childCategary = childInfo.getCategary();

			String mapKey = mapValueInfo.getBasicAncestorValueString(HAPValueInfoMap.ENTITY_PROPERTY_KEY);

			String childElementTag = mapValueInfo.getBasicAncestorValueString(HAPValueInfoContainer.ENTITY_PROPERTY_ELEMENTTAG);
			if(HAPBasicUtility.isStringEmpty(childElementTag))  childElementTag = TAG_CONTAINERCHILD; 
			
			Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(mapEle, childElementTag);
			for(Element eleEle : eleEles){
				HAPStringableValue mapElementValue = null;
				String keyValue = HAPXMLUtility.getAttributeValue(eleEle, mapKey);
				if(HAPConstant.STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
					String basicValue = eleEle.getTextContent();
					mapElementValue = processBasicValue(basicValue, (HAPValueInfoBasic)childInfo);
				}		
				else{
					mapElementValue = processComplexValue(eleEle, childInfo);
				}
				map.updateChild(keyValue, mapElementValue);
			}
		}
		
		return map;
	}
	
	private static HAPStringableValueList processListValue(Element listEle, HAPValueInfoList listValueInfo){
		HAPStringableValueList list = (HAPStringableValueList)listValueInfo.buildDefault();
		
		if(listEle!=null){
			HAPValueInfo childInfo = listValueInfo.getChildValueInfo().getSolidValueInfo();
			String childCategary = childInfo.getCategary();

			String childElementTag = listValueInfo.getBasicAncestorValueString(HAPValueInfoContainer.ENTITY_PROPERTY_ELEMENTTAG);
			if(HAPBasicUtility.isStringEmpty(childElementTag))  childElementTag = TAG_CONTAINERCHILD; 
			Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(listEle, childElementTag);
			for(Element eleEle : eleEles){
				HAPStringableValue listElementValue = null;
				if(HAPConstant.STRINGALBE_VALUEINFO_BASIC.equals(childCategary)){
					String basicValue = eleEle.getTextContent();
					listElementValue = processBasicValue(basicValue, (HAPValueInfoBasic)childInfo);
				}
				else{
					listElementValue = processComplexValue(eleEle, childInfo);
				}
				if(listElementValue!=null)  list.addChild(listElementValue); 
			}
		}
		return list;
	}

	private static HAPStringableValueEntity processEntityValue(Element entityEle, HAPValueInfoEntity entityValueInfo){
		HAPStringableValueEntity out = null;
		try{
			HAPStringableValueEntity entity = entityValueInfo.newEntity();

			if(entityEle!=null){
				for(String property : entityValueInfo.getEntityProperties()){
					HAPStringableValue entityProperty = readPropertyValueOfEntity(entityEle, entityValueInfo.getPropertyInfo(property));
					if(entityProperty!=null)			entity.updateChild(property, entityProperty);
				}
			}
			out = HAPValueInfoUtility.validateStringableValueEntity(entityValueInfo, entity);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
}
