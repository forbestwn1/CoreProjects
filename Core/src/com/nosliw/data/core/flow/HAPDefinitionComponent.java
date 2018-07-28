package com.nosliw.data.core.flow;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.context.HAPContextGroup;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

public class HAPDefinitionComponent extends HAPSerializableImp {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String VARIABLES = "variables";

	@HAPAttribute
	public static String CONSTANTS = "constants";

	
	private String m_name;
	
	private String m_description;
	
	private HAPContextGroup m_variables;
	
	private Map<String, HAPData> m_constants;
	
	
	public HAPDefinitionComponent(){
		this.m_constants = new LinkedHashMap<String, HAPData>(); 
		this.m_variables = new HAPContextGroup();
	}

	public String getName(){  return this.m_name;  }
	public void setName(String name) {
		this.m_name = name;   
	}
	
	public String getDescription() {    return this.m_description;  }
	
	//get global constants in suite. these constants is visible to all expression definition in suite
	public Map<String, HAPData> getConstants(){  return this.m_constants;  }
	public void addConstant(String name, HAPData constant) {   this.m_constants.put(name, constant);   }
	
	public HAPContextGroup getVariables() {   return this.m_variables; }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject jsonObj = (JSONObject)json;
			this.m_name = jsonObj.optString(NAME);
			this.m_description = jsonObj.optString(DESCRIPTION);
			this.m_constants = HAPDataUtility.buildDataWrapperMapFromJson(jsonObj.optJSONObject(CONSTANTS));
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
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_constants, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLES, HAPJsonUtility.buildJson(this.m_variables, HAPSerializationFormat.JSON));
	}
}
