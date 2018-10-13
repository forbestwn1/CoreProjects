package com.nosliw.data.core.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDefinitionServiceOutput extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String ITEM = "item";
	
	private String m_name;
	
	private String m_description;
	
	private Map<String, HAPDefinitionServiceOutputItem> m_items;
	
	
	public String getName(){   return this.m_name;   }

	public String getDescription(){   return this.m_description;   }
	
	public HAPDefinitionServiceOutput(){
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_name = objJson.optString(NAME);
			this.m_description = objJson.optString(DESCRIPTION);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
