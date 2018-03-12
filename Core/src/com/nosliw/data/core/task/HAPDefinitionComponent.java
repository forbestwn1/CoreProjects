package com.nosliw.data.core.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;


public class HAPDefinitionComponent extends HAPSerializableImp {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String VARIABLES = "variables";

	@HAPAttribute
	public static String CONSTANTS = "constants";

	@HAPAttribute
	public static String CHILDREN = "children";
	
	private String m_name;
	
	private String m_description;
	
	private Map<String, HAPVariableInfo> m_variables;
	
	private Map<String, HAPData> m_constants;
	
	private List<HAPDefinitionComponent> m_children;
	
	
	public HAPDefinitionComponent(){
		this.m_constants = new LinkedHashMap<String, HAPData>(); 
		this.m_variables = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_children = new ArrayList<HAPDefinitionComponent>();
	}

	public String getName(){  return this.m_name;  }
	
	public String getDescription() {    return this.m_description;  }
	
	//get global constants in suite. these constants is visible to all expression definition in suite
	public Map<String, HAPData> getConstants(){  return this.m_constants;  }

	//variables definition
	public Map<String, HAPVariableInfo> getVariables(){  return this.m_variables;  }
	public void setVariables(Map<String, HAPVariableInfo> varCriterias){  this.m_variables = varCriterias;  }

	public List<HAPDefinitionComponent> getChildren(){   return this.m_children;   }
	public void addChild(HAPDefinitionComponent child) {   this.m_children.add(child);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject jsonObj = (JSONObject)json;
			this.m_name = jsonObj.optString(NAME);
			this.m_description = jsonObj.optString(DESCRIPTION);
			{
				JSONObject constantsObj = jsonObj.optJSONObject(CONSTANTS);
				this.m_constants = HAPDataUtility.buildDataWrapperMapFromJson(constantsObj);
			}
			
			JSONObject varsObj = jsonObj.optJSONObject(VARIABLES);
			Iterator<String> its = varsObj.keys();
			while(its.hasNext()){
				String name = its.next();
				String criteriaStr = jsonObj.optString(name);
				HAPDataTypeCriteria criteria = HAPCriteriaUtility.parseCriteria(criteriaStr);
				this.m_variables.put(name, new HAPVariableInfo(criteria));
			}
			
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
		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(this.m_children, HAPSerializationFormat.JSON));
	}
}
