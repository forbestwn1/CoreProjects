package com.nosliw.data.core.imp.datasource;

import java.io.InputStream;

import org.json.JSONObject;

import com.nosliw.common.clss.HAPClassFilter;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.datasource.HAPDataSourceDefinition;
import com.nosliw.data.core.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.data.core.datasource.HAPDataSourceProvider;

public class HAPDataSourceDefinitionManagerImp extends HAPDataSourceDefinitionManager{

	public HAPDataSourceDefinitionManagerImp(){
		this.loadAllDataSourceDefinition();
	}
	
	private void loadAllDataSourceDefinition(){
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				HAPDataSourceDefinition def = loadDataSourceDefinition(cls);
				if(def!=null)	registerDataSourceDefinition(def.getName(), def);
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
	}

	private HAPDataSourceDefinition loadDataSourceDefinition(Class cls){
		try{
			InputStream inputStream = cls.getResourceAsStream("dataresource.ds");
			String content = HAPFileUtility.readFile(inputStream);
			HAPDataSourceDefinition out = new HAPDataSourceDefinition();
			out.buildObject(new JSONObject(content), HAPSerializationFormat.JSON);
			return out;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
