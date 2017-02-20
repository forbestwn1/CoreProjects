package com.nosliw.common.strvalue.valueinfo;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.common.strvalue.HAPStringableValueUtility;
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
		String valueInfoType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.TYPE);
		if(valueInfoType==null)      valueInfoType = HAPConstant.STRINGALBE_VALUEINFO_ENTITY;

		HAPValueInfo out = readValueInfoFromElement(valueInfoEle, valueInfoType);
		String name = out.getName();
		if(HAPBasicUtility.isStringEmpty(name)){
			name = valueInfoEle.getTagName();
			out.setName(name);
		}
		return out;
	}
	
	private static HAPValueInfo readValueInfoFromElement(Element valueInfoEle, String valueInfoType1){
		HAPValueInfo valueInfo = null;
		
		String reference = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfoReference.REFERENCE);
		if(reference!=null){
			//for property reference to another property
			valueInfo = HAPValueInfoReference.build(); 
			HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
			//read override properties
			HAPStringableValueList overrideList = (HAPStringableValueList)valueInfo.updateComplexChild(HAPValueInfoReference.OVERRIDE, HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST);
			Element[] overrideEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfoEntity.OVERRIDE);
			for(Element overrideEle : overrideEles){
				overrideList.addChild(readAttributeValues(overrideEle));
			}
		}
		else{
			String valueInfoType = valueInfoType1;
			if(valueInfoType==null)  valueInfoType = HAPXMLUtility.getAttributeValue(valueInfoEle, HAPValueInfo.TYPE);
			
			if(HAPConstant.STRINGALBE_VALUEINFO_LIST.equals(valueInfoType)){
				valueInfo = HAPValueInfoList.build(); 
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
				readContainerChildValueInfo(valueInfoEle, valueInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MAP.equals(valueInfoType)){
				valueInfo = HAPValueInfoMap.build();
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
				readContainerChildValueInfo(valueInfoEle, valueInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITY.equals(valueInfoType)){
				valueInfo = readEntityValueInfo(valueInfoEle);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_OBJECT.equals(valueInfoType)){
				valueInfo = HAPValueInfoObject.build();
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_ENTITYOPTIONS.equals(valueInfoType)){
				valueInfo = HAPValueInfoEntityOptions.build();
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
				HAPStringableValueMap optionsInfoMap = (HAPStringableValueMap)valueInfo.updateComplexChild(HAPValueInfoEntityOptions.OPTIONS, HAPConstant.STRINGABLE_VALUESTRUCTURE_MAP);
				Element[] optionEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfoEntityOptions.OPTIONS);
				for(Element optionEle : optionEles){
					HAPValueInfo optionPropertyInfo = readValueInfoFromElement(optionEle, null);
					optionsInfoMap.updateChild(optionPropertyInfo.getAtomicAncestorValueString(HAPValueInfoEntityOptions.VALUE), optionPropertyInfo);
				}
			}
			else if(HAPConstant.STRINGALBE_VALUEINFO_MODE.equals(valueInfoType)){
				valueInfo = HAPValueInfoMode.build();
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
				HAPStringableValueEntity elesAttrs = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.PROPERTIES, HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY);
				String[] eleTypes = {
									HAPConstant.STRINGALBE_VALUEINFO_ATOMIC, 
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
				valueInfo = HAPValueInfoAtomic.build();
				HAPStringableValueUtility.updateBasicProperty(valueInfoEle, valueInfo);
			}
			
		}

		//read sql info
		Element[] sqlInfoEles = null;
		Element sqlInfosEle = null;
		sqlInfoEles = HAPXMLUtility.getMultiChildElementByName(valueInfoEle, HAPValueInfo.DBCOLUMNLINFO);
		if(sqlInfoEles!=null && sqlInfoEles.length==0){
			sqlInfosEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, HAPValueInfo.DBCOLUMNINFOS);
			if(sqlInfosEle!=null){
				sqlInfoEles = HAPXMLUtility.getMultiChildElementByName(sqlInfosEle, HAPValueInfo.DBCOLUMNLINFO);
			}
		}
		
		if(sqlInfoEles!=null && sqlInfoEles.length>0){
			HAPDBColumnsInfo columnsInfo = new HAPDBColumnsInfo(); 
			if(sqlInfosEle!=null)		HAPStringableValueUtility.updateBasicProperty(sqlInfosEle, columnsInfo);
			
			for(Element sqlInfoEle : sqlInfoEles){
				HAPDBColumnInfo sqlInfo = new HAPDBColumnInfo();
				HAPStringableValueUtility.updateBasicProperty(sqlInfoEle, sqlInfo);
				sqlInfo.setProperty(valueInfo.getName());
				columnsInfo.addDbColumnInfo(sqlInfo);
			}
			valueInfo.setDBColumnsInfo(columnsInfo);
		}
		
		return valueInfo;
	}
	
	private static HAPValueInfoEntity readEntityValueInfo(Element entityEle){
		HAPValueInfoEntity valueInfo = HAPValueInfoEntity.build();
		HAPStringableValueUtility.updateBasicProperty(entityEle, valueInfo);
		HAPStringableValueEntity propertyInfoEntity = (HAPStringableValueEntity)valueInfo.updateComplexChild(HAPValueInfoEntity.PROPERTIES, HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY);
		Element[] childPropertyEles = HAPXMLUtility.getMultiChildElementByName(entityEle, HAPValueInfoEntity.PROPERTIES);
		for(Element childPropertyEle : childPropertyEles){
			HAPValueInfo childPropertyInfo = readValueInfoFromElement(childPropertyEle, null);
			propertyInfoEntity.updateChild(childPropertyInfo.getName(), childPropertyInfo);
		}
		
		//read override properties
		HAPStringableValueList overrideList = (HAPStringableValueList)valueInfo.updateComplexChild(HAPValueInfoEntity.OVERRIDE, HAPConstant.STRINGABLE_VALUESTRUCTURE_LIST);
		Element[] overrideEles = HAPXMLUtility.getMultiChildElementByName(entityEle, HAPValueInfoEntity.OVERRIDE);
		for(Element overrideEle : overrideEles){
			overrideList.addChild(readAttributeValues(overrideEle));
		}
		return valueInfo;
	}
	
	private static HAPValueInfo readContainerChildValueInfo(Element valueInfoEle, HAPValueInfo containerValueInfo){
		Element childEle = HAPXMLUtility.getFirstChildElementByName(valueInfoEle, HAPValueInfoList.CHILD);
		HAPValueInfo childPropertyInfo = readValueInfoFromElement(childEle, null);
		containerValueInfo.updateChild(HAPValueInfoList.CHILD, childPropertyInfo);
		return childPropertyInfo;
	}
	
	private static HAPAttributeValues readAttributeValues(Element attrValuesEle){
		String path = attrValuesEle.getAttribute(HAPAttributeValues.PATH);
		HAPAttributeValues out = new HAPAttributeValues(path);
		
		String attrsValue = attrValuesEle.getAttribute(HAPAttributeValues.ATTRIBUTES); 
		Map<String, String> attrs = HAPNamingConversionUtility.parsePropertyValuePairs(attrsValue);
		
		for(String attr : attrs.keySet()){
			out.addAttributeValue(new HAPAttributeValue(attr, attrs.get(attr)));
		}
		
		return out;
	}
	
}
