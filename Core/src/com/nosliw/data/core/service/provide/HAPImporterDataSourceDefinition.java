package com.nosliw.data.core.service.provide;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPImporterDataSourceDefinition {

	private static String[] serviceClasses = {
//			"com.nosliw.service.school.HAPServiceImp",
//			"com.nosliw.service.realtor.HAPServiceImp",
//			"com.nosliw.service.soccer.HAPServiceGetLineup",
//			"com.nosliw.service.soccer.HAPServiceUpdateLineup",
//			"com.nosliw.service.test.process.HAPServiceProviderImp",
//			"com.nosliw.service.pearsonflight.HAPServiceImp",
//			"com.nosliw.service.email.HAPServiceImp",
			"com.nosliw.service.test.template1.HAPServiceImp",
			"com.nosliw.service.test.template2.HAPServiceImp",
//			"com.nosliw.service.test.template.school.HAPServiceImp",
	};
	
	public static List<HAPDefinitionService> loadDataSourceDefinition() {
		List<HAPDefinitionService> out = new ArrayList<HAPDefinitionService>();

		for(String serviceClasse : serviceClasses) {
			Class cls;
			try {
				cls = Class.forName(serviceClasse);
				List<HAPDefinitionService> dataSourceDefs = loadDataSourceDefinition(cls);
				for(HAPDefinitionService dataSourceDef : dataSourceDefs) {
					out.add(dataSourceDef);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
/*		
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				HAPDefinitionService dataSourceDef = loadDataSourceDefinition(cls);
				if(dataSourceDef!=null)		out.add(dataSourceDef);
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
*/		
		return out;
	}
	
	private static List<HAPDefinitionService> loadDataSourceDefinition(Class cls){
		List<HAPDefinitionService> out = new ArrayList<HAPDefinitionService>();
		try{
			InputStream inputStream = cls.getResourceAsStream("service.ds");
			if(inputStream!=null) {
				String content = HAPFileUtility.readFile(inputStream);
				JSONArray serviceDefArray = new JSONArray(content);
				for(int i=0; i<serviceDefArray.length(); i++) {
					HAPDefinitionService serviceDef = new HAPDefinitionService();
					serviceDef.buildObject(serviceDefArray.get(i), HAPSerializationFormat.JSON);
					out.add(serviceDef);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
