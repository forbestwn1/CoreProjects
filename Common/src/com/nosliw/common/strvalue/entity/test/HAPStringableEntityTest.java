package com.nosliw.common.strvalue.entity.test;

import java.io.InputStream;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporter;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityTest {
	
	public static void main(String[] args){
		HAPValueInfoManager valueInfoMan = new HAPValueInfoManager();
		
		importValueInfoFromFile("entitydef.xml", valueInfoMan);
		importValueInfoFromFile("reference.xml", valueInfoMan);
		importValueInfoFromFile("parent.xml", valueInfoMan);
		
		InputStream entityInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entity.xml");
		HAPStringableValueEntity entity = HAPStringableEntityImporter.readRootEntity(entityInputStream, valueInfoMan);
		entity.resolveByPattern(null);
		System.out.println(entity.toString());
	}
	
	private static HAPValueInfo importValueInfoFromFile(String xmlFile, HAPValueInfoManager valueInfoMan){
		InputStream infoInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, xmlFile);
		HAPValueInfo valueInfo = HAPValueInfoImporterXML.importFromXML(infoInputStream, valueInfoMan);
		valueInfo.resolveByPattern(null);
		valueInfoMan.registerValueInfo(valueInfo);
		System.out.println("********************************"+ valueInfo.getName() +"*******************************");
		System.out.println(valueInfo.toString());
		return valueInfo;
	}
	
}
