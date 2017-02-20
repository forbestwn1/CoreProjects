package com.nosliw.common.strvalue.entity.test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityTest {
	
	public static void main(String[] args){
		HAPValueInfoManager.getInstance().importFromXML(HAPStringableEntityTest.class, 
				new String[]{"entitydef.xml", "reference.xml", "parent.xml"});
		
		InputStream entityInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entity.xml");
		HAPStringableValueEntity entity = HAPStringableEntityImporterXML.readRootEntity(entityInputStream);
		entity.resolveByPattern(null);
		System.out.println(entity.toString());
	}
}
