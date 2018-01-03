package com.nosliw.data.core.datasource.imp.secondhand;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDependentDataSource extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String PARMS = "parms";
	
	private String m_name;
	
	private Map<String, HAPDependentDataSourceParm> m_parms;

	public HAPDependentDataSource(){
		this.m_parms = new LinkedHashMap<String, HAPDependentDataSourceParm>();
	}
	
	public String getName(){
		return this.m_name;
	}
	
	public Map<String, HAPDependentDataSourceParm> getParms(){
		return this.m_parms;
	}

	public HAPDependentDataSourceParm getParm(String parmName){
		return this.m_parms.get(parmName);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.optString(NAME);
			
			JSONObject parmJsonObj = objJson.getJSONObject(PARMS);
			Iterator<String> parmsIts = parmJsonObj.keys();
			while(parmsIts.hasNext()){
				String parmName = parmsIts.next();
				Object parmValueJson = parmJsonObj.get(parmName);
				this.m_parms.put(parmName, new HAPDependentDataSourceParm(parmValueJson));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
