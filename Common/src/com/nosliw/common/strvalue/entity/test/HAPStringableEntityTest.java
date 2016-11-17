package com.nosliw.common.strvalue.entity.test;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoImporterXML;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPStringableEntityTest {
	
	public static void main(String[] args){
		Set<String> files = new HashSet<String>();
		files.add("entitydef.xml");
		files.add("reference.xml");
		files.add("parent.xml");
		HAPValueInfoManager.getInstance().importFromXML(HAPStringableEntityTest.class, files);
		
		InputStream entityInputStream = HAPFileUtility.getInputStreamOnClassPath(HAPStringableEntityTest.class, "entity.xml");
		HAPStringableValueEntity entity = HAPStringableEntityImporterXML.readRootEntity(entityInputStream);
		entity.resolveByPattern(null);
		System.out.println(entity.toString());
	}
}
