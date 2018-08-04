package com.nosliw.data.core.flow;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.context.HAPContextGroup;

public class HAPDefinitionComponent extends HAPSerializableImp {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATACONTEXT = "context";

	private String m_name;
	
	private String m_description;
	
	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_dataContext;
	
	public HAPDefinitionComponent(){
		this.m_dataContext = new HAPContextGroup();
	}

	public String getName(){  return this.m_name;  }
	public void setName(String name) {		this.m_name = name;	}
	
	public String getDescription() {    return this.m_description;  }
	
	public HAPContextGroup getDataContext() {   return this.m_dataContext; }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject jsonObj = (JSONObject)json;
			this.m_name = jsonObj.optString(NAME);
			this.m_description = jsonObj.optString(DESCRIPTION);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DESCRIPTION, m_description);
		jsonMap.put(DATACONTEXT, HAPJsonUtility.buildJson(this.m_dataContext, HAPSerializationFormat.JSON));
	}
}
