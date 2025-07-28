package com.nosliw.data.core.datasource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONObject;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPDataSourceDefinitionImporter {

	
	public static List<HAPDefinition> loadDataSourceDefinition() {
		List<HAPDefinition> out = new ArrayList<HAPDefinition>();
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				HAPDefinition dataSourceDef = loadDataSourceDefinition(cls);
				if(dataSourceDef!=null)		out.add(dataSourceDef);
//				try {
//					Path path = HAPFileUtility.getClassFolderPath(cls);
//					Stream<Path> stream = Files.list(path);
//					Iterator<Path> it = stream.iterator();
//					while(it.hasNext()) {
//						try {
//							Path filePath = it.next();
//							if(filePath.toUri().toString().endsWith(".ds")) {
//								String defContent = HAPFileUtility.readFile(Files.newInputStream(filePath));
//								HAPDefinition dataSourceDef = new HAPDefinition();
//								dataSourceDef.buildObjectByJson(new JSONObject(defContent));
//								out.add(dataSourceDef);
//							}
//						}
//						catch(Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				catch(Exception e) {
//					e.printStackTrace();
//				}
			}

			@Override
			protected boolean isValid(Class cls) {
				Class[] interfaces = cls.getInterfaces();
				for(Class inf : interfaces){
					if(inf.equals(HAPDataSourceProvider.class)){
						return true;
					}
				}
				return false;
			}
		}.process(null);
		
		return out;
	}
	
	private static HAPDefinition loadDataSourceDefinition(Class cls){
		HAPDefinition out = null;
		try{
			InputStream inputStream = cls.getResourceAsStream("datasource.ds");
			if(inputStream!=null) {
				String content = HAPFileUtility.readFile(inputStream);
				out = new HAPDefinition();
				out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

}
