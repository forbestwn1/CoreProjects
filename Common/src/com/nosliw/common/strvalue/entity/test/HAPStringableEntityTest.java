package com.nosliw.common.strvalue.entity.test;

import java.io.InputStream;

import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.strvalue.propertyinfo.HAPEntityInfoImporterXML;
import com.nosliw.common.strvalue.propertyinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.propertyinfo.HAPStringableEntityImporter;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityTest {
	
	public static void main(String[] args){
		InputStream infoInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entitydef.xml");
		
		HAPValueInfoEntity entityInfo = HAPEntityInfoImporterXML.importFromXML(infoInputStream);
		entityInfo.resolveByPattern(null);
		System.out.println(entityInfo.toString());
		
		InputStream entityInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entity.xml");
		HAPStringableValueEntity entity = HAPStringableEntityImporter.importStringableEntity(entityInputStream, entityInfo);
		entity.resolveByPattern(null);
		System.out.println(entity.toString());
	}
	
	
}
