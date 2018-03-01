package com.nosliw.data.core.task;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

public class HAPDefinitionTaskSuiteForTest extends HAPDefinitionTaskSuite{

	@HAPAttribute
	public static String VARIABLESDATA = "variablesData";
	
	private Map<String, HAPData> m_variableData;
	
	public Map<String, HAPData> getVariableData(){	return this.m_variableData;	}

	public HAPDefinitionTaskSuiteForTest(){
		this.m_variableData = new LinkedHashMap<String, HAPData>();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		try{
			JSONObject jsonObj = (JSONObject)json;
			{
				JSONObject varsDataObjJson = jsonObj.optJSONObject(VARIABLESDATA);
				if(varsDataObjJson!=null){
					Iterator<String> its = varsDataObjJson.keys();
					while(its.hasNext()){
						String name = its.next();
						Object varDataObj = jsonObj.opt(name);
						HAPData data = HAPDataUtility.buildDataWrapperFromObject(varDataObj);
						this.m_variableData.put(name, data);
					}
				}
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
		jsonMap.put(VARIABLESDATA, HAPJsonUtility.buildJson(this.m_variableData, HAPSerializationFormat.JSON));
	}
	
}
