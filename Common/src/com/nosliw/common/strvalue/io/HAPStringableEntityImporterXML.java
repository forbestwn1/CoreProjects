package com.nosliw.common.strvalue.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.strvalue.HAPStringableValueComplex;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.strvalue.HAPStringableValueObject;
import com.nosliw.common.strvalue.HAPStringableValueUtility;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoAtomic;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoContainer;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntityOptions;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoList;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoMap;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoObject;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPXMLUtility;

public class HAPStringableEntityImporterXML {

	private static String TAG_CONTAINERCHILD = "element";

	public static List<HAPStringableValueEntity> readMutipleEntitys(InputStream xmlStream){
		return readMutipleEntitys(xmlStream, null);
	}	
	
	public static List<HAPStringableValueEntity> readMutipleEntitys(InputStream xmlStream, String valueInfoName){
		List<HAPStringableValueEntity> out = new ArrayList<HAPStringableValueEntity>();
		
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			Element[] eles = HAPXMLUtility.getChildElements(rootEle);
			for(Element ele : eles){
				out.add(readRootEntity(ele, valueInfoName));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	public static HAPStringableValueEntity readRootEntity(InputStream xmlStream){
		return readRootEntity(xmlStream, null);
	}

	public static HAPStringableValueEntity readRootEntity(InputStream xmlStream, String valueInfoName){
		HAPStringableValueEntity out = null;
		try{
			DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder DOMbuilder = DOMfactory.newDocumentBuilder();
			Document doc = DOMbuilder.parse(xmlStream);

			Element rootEle = doc.getDocumentElement();
			out = readRootEntity(rootEle, valueInfoName);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
	
	public static HAPStringableValueEntity readRootEntity(Element entityEle){
		return readRootEntity(entityEle, null);
	}

	public static HAPStringableValueEntity readRootEntity(Element entityEle, String valueInfoName){
		String valueInfo = valueInfoName;
		if(HAPBasicUtility.isStringEmpty(valueInfoName)){
			valueInfo = entityEle.getTagName();
		}
		HAPStringableValueEntity out = processEntityValue(entityEle, (HAPValueInfoEntity)HAPValueInfoManager.getInstance().getValueInfo(valueInfo).getSolidValueInfo());
		return out;
	}
	
	private static HAPStringableValue readPropertyValueOfEntity(Element propertyContainerEle, HAPValueInfo propertyValueInfo){
		String propertyName = propertyValueInfo.getName();
		
		HAPStringableValue out = null;
		try{
			HAPValueInfo propertyInfo = propertyValueInfo.getSolidValueInfo();
			String propertyCategary = propertyInfo.getValueInfoType();
			if(HAPConstantShared.STRINGALBE_VALUEINFO_LIST.equals(propertyCategary)){
				Element listEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processListValue(listEle, (HAPValueInfoList)propertyInfo);
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_MAP.equals(propertyCategary)){
				Element mapEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processMapValue(mapEle, (HAPValueInfoMap)propertyInfo);
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(propertyCategary)){
				Element entityEle = HAPXMLUtility.getFirstChildElementByName(propertyContainerEle, propertyName);
				out = processEntityValue(entityEle, (HAPValueInfoEntity)propertyInfo);
			}
			else if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(propertyCategary)){
				out = processEntityOptionsValue(propertyContainerEle, (HAPValueInfoEntityOptions)propertyInfo);
			}
			else if(HAPConstantShared.STRINGABLE_VALUESTRUCTURE_OBJECT.equals(propertyCategary)){
				String value = HAPXMLUtility.getChildContent(propertyContainerEle, propertyName);
				if(value!=null)			out = processObjectValue(value, (HAPValueInfoObject)propertyInfo);
			}
			else{
				String value = HAPXMLUtility.getChildContent(propertyContainerEle, propertyName);
				out = processAtomicValue(value, (HAPValueInfoAtomic)propertyInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	private static HAPStringableValueObject processObjectValue(String strValue, HAPValueInfoObject objectValueInfo){
		HAPStringableValueObject out = objectValueInfo.buildValue(strValue);
		return out;
	}
	
	private static HAPStringableValueAtomic processAtomicValue(String strValue, HAPValueInfoAtomic atomicValueInfo){
		HAPStringableValueAtomic out = null;
		if(strValue!=null)		out = new HAPStringableValueAtomic(strValue, atomicValueInfo.getDataType(), atomicValueInfo.getSubDataType());		
		return out;
	}

	private static HAPStringableValueComplex processComplexValue(Element element, HAPValueInfo valueInfo){
		HAPStringableValueComplex out = null;
		String valueInfoType = valueInfo.getValueInfoType();
		if(HAPConstantShared.STRINGALBE_VALUEINFO_ENTITY.equals(valueInfoType)){
			out = processEntityValue(element, (HAPValueInfoEntity)valueInfo);
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_LIST.equals(valueInfoType)){
			out = processListValue(element, (HAPValueInfoList)valueInfo);
		}
		else if(HAPConstantShared.STRINGALBE_VALUEINFO_MAP.equals(valueInfoType)){
			out = processMapValue(element, (HAPValueInfoMap)valueInfo);
		}
		return out;
	}

	private static HAPStringableValue processEntityOptionsValue(Element containerEle, HAPValueInfoEntityOptions entityOptionsValueInfo){
		String propertyName = entityOptionsValueInfo.getName();

		String optionsKey = entityOptionsValueInfo.getAtomicAncestorValueString(HAPValueInfoEntityOptions.KEY);
		String keyValue = HAPXMLUtility.getAttributeValue(containerEle, optionsKey);
		HAPValueInfo optionValueInfo = entityOptionsValueInfo.getOptionsValueInfo(keyValue).getSolidValueInfo();
		String optionValueInfoType = optionValueInfo.getValueInfoType();
		HAPStringableValue out = null;
		if(HAPConstantShared.STRINGALBE_VALUEINFO_ATOMIC.equals(optionValueInfoType)){
			String value = HAPXMLUtility.getAttributeValue(containerEle, propertyName); 
			out = processAtomicValue(value, (HAPValueInfoAtomic)optionValueInfo);
		}
		else{
			Element optionEle = HAPXMLUtility.getFirstChildElementByName(containerEle, propertyName);
			out = processComplexValue(optionEle, optionValueInfo);
		}
		return out;
	}
	
	private static HAPStringableValueMap processMapValue(Element mapEle, HAPValueInfoMap mapValueInfo){
		HAPStringableValueMap map = (HAPStringableValueMap)mapValueInfo.newValue();
		
		if(mapEle!=null){
			HAPValueInfo childInfo = mapValueInfo.getChildValueInfo().getSolidValueInfo();
			String childValueInfoType = childInfo.getValueInfoType();

			String mapKey = mapValueInfo.getAtomicAncestorValueString(HAPValueInfoMap.KEY);

			String childElementTag = mapValueInfo.getAtomicAncestorValueString(HAPValueInfoContainer.ELEMENTTAG);
			if(HAPBasicUtility.isStringEmpty(childElementTag))  childElementTag = TAG_CONTAINERCHILD; 
			
			Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(mapEle, childElementTag);
			for(Element eleEle : eleEles){
				HAPStringableValue mapElementValue = null;
				String keyValue = HAPXMLUtility.getAttributeValue(eleEle, mapKey);
				if(HAPConstantShared.STRINGALBE_VALUEINFO_ATOMIC.equals(childValueInfoType)){
					String basicValue = eleEle.getTextContent();
					mapElementValue = processAtomicValue(basicValue, (HAPValueInfoAtomic)childInfo);
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
		HAPStringableValueList list = (HAPStringableValueList)listValueInfo.newValue();
		
		if(listEle!=null){
			HAPValueInfo childInfo = listValueInfo.getChildValueInfo().getSolidValueInfo();
			String childValueInfoType = childInfo.getValueInfoType();

			String childElementTag = listValueInfo.getAtomicAncestorValueString(HAPValueInfoContainer.ELEMENTTAG);
			if(HAPBasicUtility.isStringEmpty(childElementTag))  childElementTag = TAG_CONTAINERCHILD; 
			Element[] eleEles = HAPXMLUtility.getMultiChildElementByName(listEle, childElementTag);
			for(Element eleEle : eleEles){
				HAPStringableValue listElementValue = null;
				if(HAPConstantShared.STRINGALBE_VALUEINFO_ATOMIC.equals(childValueInfoType)){
					String basicValue = eleEle.getTextContent();
					listElementValue = processAtomicValue(basicValue, (HAPValueInfoAtomic)childInfo);
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
		if(entityEle==null)  return null;
		
		HAPStringableValueEntity out = null;
		try{
			HAPStringableValueEntity entity = (HAPStringableValueEntity)entityValueInfo.newValue();

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
