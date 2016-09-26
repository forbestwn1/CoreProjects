package com.nosliw.common.strvalue.entity.test;

import java.io.InputStream;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityTest {
	
	public static void main(String[] args){
		importValueInfoFromFile("entitydef.xml");
		importValueInfoFromFile("reference.xml");
		importValueInfoFromFile("parent.xml");
		
		InputStream entityInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entity.xml");
		HAPStringableValueEntity entity = HAPStringableEntityImporterXML.readRootEntity(entityInputStream);
		entity.resolveByPattern(null);
		System.out.println(entity.toString());
	}
	
	private static HAPValueInfo importValueInfoFromFile(String xmlFile){
		InputStream infoInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, xmlFile);
		HAPValueInfo valueInfo = HAPValueInfoImporterXML.importFromXML(infoInputStream);
		valueInfo.resolveByPattern(null);
//		HAPValueInfoManager.getInstance().registerValueInfo(valueInfo);
		System.out.println("********************************"+ valueInfo.getName() +"*******************************");
		System.out.println(valueInfo.toString());
		return valueInfo;
	}
	
}
