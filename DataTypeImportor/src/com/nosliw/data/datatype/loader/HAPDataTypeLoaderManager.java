package com.nosliw.data.datatype.loader;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.strvalue.valueinfo.HAPStringableEntityImporterXML;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.HAPDataTypeProvider;
import com.nosliw.data.imp.HAPDataTypeManagerImp;

public class HAPDataTypeLoaderManager {
	private HAPDataTypeManagerImp m_dataTypeMan;
	
	public HAPDataTypeLoaderManager(HAPDataTypeManagerImp dataTypeMan){
		this.m_dataTypeMan = dataTypeMan;
	}
	
	public void loadAll(){
		Set<String> valueInfos = new HashSet<String>();
		valueInfos.add("datatypedefinition.xml");
		valueInfos.add("datatypeinfo.xml");
		valueInfos.add("datatypeversion.xml");
		HAPValueInfoManager.getInstance().importFromXML(HAPDataTypeLoaderManager.class, valueInfos);
		
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				loadDataType(cls);
			}

			@Override
			protected boolean isValid(Class cls) {
				Class[] interfaces = cls.getInterfaces();
				for(Class inf : interfaces){
					if(inf.equals(HAPDataTypeProvider.class)){
						return true;
					}
				}
				return false;
			}
		}.process(null);
	}

	public void loadDataType(Class cls){
		InputStream stream = cls.getResourceAsStream("datatype.xml");
		HAPDataTypeImp dataType = (HAPDataTypeImp)HAPStringableEntityImporterXML.readRootEntity(stream, "data.datatypedef");
		dataType.resolveByConfigure(null);
		this.m_dataTypeMan.registerDataType(dataType);
	}
	
	public static void main(String[] args){
		HAPDataTypeLoaderManager man = new HAPDataTypeLoaderManager(new HAPDataTypeManagerImp());
		man.loadAll();
	}
}
