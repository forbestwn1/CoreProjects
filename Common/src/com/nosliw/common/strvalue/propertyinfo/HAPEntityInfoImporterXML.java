package com.nosliw.common.strvalue.propertyinfo;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPEntityInfoImporterXML {

	public static HAPValueInfoEntity importFromXML(InputStream xmlStream){
		HAPValueInfoEntity out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = (HAPValueInfoEntity)readValueInfoFromElement(rootEle, HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
	 
	private static HAPValueInfo readValueInfoFromElement(Element valueInfoEle, String valueInfoType){
		HAPValueInfo valueInfo = null;
		
		String propertyType = valueInfoType;
		if(propertyType==null)		propertyType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.ATTR_TYPE); 
		
		if(HAPConstant.CONS_STRINGALBE_VALUEINFO_LIST.equals(propertyType)){
			valueInfo = new HAPValueInfoList(); 
			updateBasicProperty(valueInfoEle, valueInfo);
			readContainerChildValueInfo(valueInfoEle, valueInfo);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_MAP.equals(propertyType)){
			valueInfo = new HAPValueInfoMap();
			updateBasicProperty(valueInfoEle, valueInfo);
			readContainerChildValueInfo(valueInfoEle, valueInfo);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITY.equals(propertyType)){
			valueInfo = readEntityValueInfo(valueInfoEle);
		}
		else if(HAPConstant.CONS_STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyType)){
			valueInfo = new HAPValueInfoEntityOptions();
			updateBasicProperty(valueInfoEle, valueInfo);
			HAPStringableValueEntity optionsInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntityOptions.ATTR_OPTIONS, HAPConstant.CONS_STRINGABLE_VALUECATEGARY_ENTITY);
			Element[] optionEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfoEntityOptions.ATTR_OPTIONS);
			for(Element optionEle : optionEles){
				HAPValueInfo optionPropertyInfo = readValueInfoFromElement(optionEle, null);
				optionsInfoEntity.addChild(optionPropertyInfo.getBasicAncestorValueString(HAPValueInfoEntityOptions.ATTR_VALUE), optionPropertyInfo);
			}
		}
		else{
			valueInfo = new HAPValueInfoBasic();
			updateBasicProperty(valueInfoEle, valueInfo);
		}
		return valueInfo;
	}
	
	private static HAPValueInfoEntity readEntityValueInfo(Element entityEle){
		HAPValueInfoEntity valueInfo = new HAPValueInfoEntity();
		updateBasicProperty(entityEle, valueInfo);
		HAPStringableValueEntity propertyInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.ATTR_PROPERTIES, HAPConstant.CONS_STRINGABLE_VALUECATEGARY_ENTITY);
		Element[] childPropertyEles = HAPXMLUtility.getMultiChildElementByName(entityEle, HAPValueInfoEntity.ATTR_PROPERTIES);
		for(Element childPropertyEle : childPropertyEles){
			HAPValueInfo childPropertyInfo = readValueInfoFromElement(childPropertyEle, null);
			propertyInfoEntity.addChild(childPropertyInfo.getName(), childPropertyInfo);
		}
		return valueInfo;
	}
	
	private static HAPValueInfo readContainerChildValueInfo(Element valueInfoEle, HAPValueInfo containerValueInfo){
		Element childEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, HAPValueInfoList.ATTR_CHILD);
		HAPValueInfo childPropertyInfo = readValueInfoFromElement(childEle, null);
		containerValueInfo.addChild(HAPValueInfoList.ATTR_CHILD, childPropertyInfo);
		return childPropertyInfo;
	}
	
	private static void updateBasicProperty(Element element, HAPStringableValueEntity entity){
		Map<String, String> propertyAttrs = HAPXMLUtility.getAllAttributes(element);
		entity.updateBasicChildrens(propertyAttrs);
	}
}
