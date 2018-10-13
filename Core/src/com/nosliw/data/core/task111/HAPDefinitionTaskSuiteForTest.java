package com.nosliw.data.core.task111;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.HAPDataWrapper;

public class HAPDefinitionTaskSuiteForTest extends HAPDefinitionTaskSuite{

	@HAPAttribute
	public static String VARIABLESDATA = "variablesData";

	@HAPAttribute
	public static String RESULT = "result";

	private HAPDataWrapper m_result;
	
	private Map<String, HAPData> m_variableData;
	
	public Map<String, HAPData> getVariableData(){	return this.m_variableData;	}

	public HAPDataWrapper getResult() {   return this.m_result;   }
	
	public HAPDefinitionTaskSuiteForTest(HAPManagerTask taskManager){
		super(taskManager);
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
						Object varDataObj = varsDataObjJson.opt(name);
						HAPData data = HAPDataUtility.buildDataWrapperFromObject(varDataObj);
						this.m_variableData.put(name, data);
					}
				}
			}

			{
				JSONObject resultDataObjJson = jsonObj.optJSONObject(RESULT);
				if(resultDataObjJson!=null){
					this.m_result = HAPDataUtility.buildDataWrapperFromJson(resultDataObjJson);
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
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_result, HAPSerializationFormat.JSON));
	}
	
}
