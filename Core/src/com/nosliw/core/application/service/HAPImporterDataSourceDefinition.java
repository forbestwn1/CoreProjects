package com.nosliw.core.application.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;

public class HAPImporterDataSourceDefinition {

	public static List<HAPBrickServiceProfile> loadDataSourceDefinition() {
		List<HAPBrickServiceProfile> out = new ArrayList<HAPBrickServiceProfile>();

		/*
		for(String serviceClasse : serviceClasses) {
			Class cls;
			try {
				cls = Class.forName(serviceClasse);
				List<HAPBrickServiceProfile> dataSourceDefs = loadDataSourceDefinition(cls);
				for(HAPBrickServiceProfile dataSourceDef : dataSourceDefs) {
					out.add(dataSourceDef);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		*/
		
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				List<HAPBrickServiceProfile> dataSourceDefs = loadDataSourceDefinition(cls);
				out.addAll(dataSourceDefs);
			}

			@Override
			protected boolean isValid(Class cls) {
				Class[] interfaces = cls.getInterfaces();
				for(Class inf : interfaces){
					if(inf.equals(HAPProviderService.class)){
						return true;
					}
				}
				return false;
			}
		}.process(null);
	
		return out;
	}
	
	private static List<HAPBrickServiceProfile> loadDataSourceDefinition(Class cls){
		List<HAPBrickServiceProfile> out = new ArrayList<HAPBrickServiceProfile>();
		try{
			InputStream inputStream = cls.getResourceAsStream("service.ds");
			if(inputStream!=null) {
				String content = HAPUtilityFile.readFile(inputStream);
				JSONArray serviceDefArray = new JSONArray(content);
				for(int i=0; i<serviceDefArray.length(); i++) {
					JSONObject serviceDefJson = serviceDefArray.getJSONObject(i);
					if(HAPUtilityEntityInfo.isEnabled(serviceDefJson)) {
						HAPBrickServiceProfile serviceDef = new HAPBrickServiceProfile();
						serviceDef.buildObject(serviceDefJson, HAPSerializationFormat.JSON);
						out.add(serviceDef);
						System.out.println(serviceDef.getStaticInfo().getId());
					}
				}
			}
		}
		catch(Throwable e){
			e.printStackTrace();
		}
		return out;
	}

}
