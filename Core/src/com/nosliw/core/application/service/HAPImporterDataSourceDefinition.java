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
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.division.manual.executable.HAPUtilitySerializeJson;

public class HAPImporterDataSourceDefinition {

	public static List<HAPInfoService> loadDataSourceDefinition(HAPManagerApplicationBrick brickMan) {
		List<HAPInfoService> out = new ArrayList<HAPInfoService>();

		/*
		for(String serviceClasse : serviceClasses) {
			Class cls;
			try {
				cls = Class.forName(serviceClasse);
				List<HAPBlockData> dataSourceDefs = loadDataSourceDefinition(cls);
				for(HAPBlockData dataSourceDef : dataSourceDefs) {
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
				List<HAPInfoService> dataSourceDefs = loadDataSourceDefinition(cls, brickMan);
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
	
	private static List<HAPInfoService> loadDataSourceDefinition(Class cls, HAPManagerApplicationBrick brickMan){
		List<HAPInfoService> out = new ArrayList<HAPInfoService>();
		try{
			InputStream inputStream = cls.getResourceAsStream("service.ds");
			if(inputStream!=null) {
				String content = HAPUtilityFile.readFile(inputStream);
				JSONArray serviceDefArray = new JSONArray(content);
				for(int i=0; i<serviceDefArray.length(); i++) {
					JSONObject serviceDefJson = serviceDefArray.getJSONObject(i);
					if(HAPUtilityEntityInfo.isEnabled(serviceDefJson)) {
						JSONObject profileJsonObj = serviceDefJson.optJSONObject(HAPInfoService.PROFILE);
						if(HAPUtilityEntityInfo.isEnabled(profileJsonObj)) {
							HAPBlockServiceProfile serviceProfile = (HAPBlockServiceProfile)HAPUtilitySerializeJson.buildBrick(profileJsonObj, HAPEnumBrickType.SERVICEPROFILE_100, brickMan);
							
							JSONObject runtimeJsonObj = serviceDefJson.optJSONObject(HAPInfoService.RUNTIME);
							HAPInfoServiceRuntime serviceRuntime = new HAPInfoServiceRuntime();
							serviceRuntime.buildObject(runtimeJsonObj, HAPSerializationFormat.JSON);
							
							out.add(new HAPInfoService(serviceProfile, serviceRuntime));
						}
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
