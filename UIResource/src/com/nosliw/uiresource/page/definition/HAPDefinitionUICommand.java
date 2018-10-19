package com.nosliw.uiresource.page.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPDefinitionUICommand  extends HAPEntityInfoImp{
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String RESULT = "result";

	//context
	private HAPContext m_parms;
	
	private Map<String, HAPContext> m_results;
	
	public HAPDefinitionUICommand() {
		this.m_parms = new HAPContext();
		this.m_results = new LinkedHashMap<String, HAPContext>();
	}

	public HAPContext getParms() {  return this.m_parms;   }
	public void setParms(HAPContext parms) {   this.m_parms = parms;  }
	
	public Map<String, HAPContext> getResults(){   return this.m_results;   }
	public void addResult(String name, HAPContext result) {   this.m_results.put(name, result);   }
	
	public void cloneBasicTo(HAPDefinitionUICommand command) {
		this.cloneToEntityInfo(command);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PARM, this.m_parms.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(RESULT, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		HAPParserContext.parseContext(jsonObj.optJSONObject(PARM), this.m_parms);
		
		JSONObject resultJson = jsonObj.optJSONObject(RESULT);
		if(resultJson!=null) {
			for(Object key : resultJson.keySet()) {
				HAPContext resultEle = new HAPContext();
				HAPParserContext.parseContext(jsonObj.optJSONObject((String)key), resultEle);
				this.m_results.put((String)key, resultEle);
			}
		}
		
		return true;  
	}
}
