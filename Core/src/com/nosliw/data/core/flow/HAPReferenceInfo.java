package com.nosliw.data.core.flow;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.script.context.HAPContext;

/**
 * The information used to define reference to another task
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public class HAPReferenceInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String REFERENCE = "reference";
	
	@HAPAttribute
	public static String VARIABLESMAP = "variablesMap";
	
	private String m_reference;
	
	//mapping information : from 
	private HAPContext m_variableMap;
	
	public HAPReferenceInfo(){
		this.m_variableMap = new HAPContext();
	}

	public HAPReferenceInfo(String ref){
		this();
		this.m_reference = ref;
	}
	
	public String getReference(){  return this.m_reference;	}
	
	public HAPContext getVariablesMap(){  return this.m_variableMap;  }
	public void addVariableMap(String name, String mapped) {
//		this.m_variableMap.put(name, mapped);	
	}
	
	public void setVariableMap(Map<String, String> varsMap) {
//		this.m_variableMap.clear();
//		this.m_variableMap.putAll(varsMap);
	}
	
	public HAPReferenceInfo clone() {
		HAPReferenceInfo out = new HAPReferenceInfo();
//		out.m_reference = this.m_reference;
//		out.m_variableMap.putAll(this.m_variableMap);
		return out;
	}

	public void upateVariableName(HAPUpdateVariable updateVar) {
//		Map<String, String> varMap = new LinkedHashMap<String, String>();
//		for(String name : this.m_variableMap.keySet()) {
//			varMap.put(updateVar.getUpdatedVariable(name), this.m_variableMap.get(name));
//		}
//		this.m_variableMap.clear();
//		this.m_variableMap.putAll(varMap);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
//		JSONObject jsonObj = (JSONObject)json;
//		this.m_reference = jsonObj.optString(REFERENCE);
//		if(HAPBasicUtility.isStringEmpty(this.m_reference))   this.m_reference = null;
//		
//		JSONObject mapsObj = jsonObj.getJSONObject(VARIABLESMAP);
//		Iterator<String> its = mapsObj.keys();
//		while(its.hasNext()){
//			String name = its.next();
//			String map = mapsObj.optString(name);
//			this.m_variableMap.put(name, map);
//		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCE, this.m_reference);
		jsonMap.put(VARIABLESMAP, HAPJsonUtility.buildJson(this.m_variableMap, HAPSerializationFormat.JSON));
	}
}
