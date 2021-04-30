package com.nosliw.uiresource.page.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;

@HAPEntityWithAttribute
public class HAPDefinitionUICommand  extends HAPEntityInfoWritableImp{
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String RESULT = "result";

	//context
	private HAPStructureValueDefinitionFlat m_parms;
	
	private Map<String, HAPStructureValueDefinitionFlat> m_results;
	
	public HAPDefinitionUICommand() {
		this.m_parms = new HAPStructureValueDefinitionFlat();
		this.m_results = new LinkedHashMap<String, HAPStructureValueDefinitionFlat>();
	}

	public HAPStructureValueDefinitionFlat getParms() {  return this.m_parms;   }
	public void setParms(HAPStructureValueDefinitionFlat parms) {   this.m_parms = parms;  }
	
	public Map<String, HAPStructureValueDefinitionFlat> getResults(){   return this.m_results;   }
	public void addResult(String name, HAPStructureValueDefinitionFlat result) {   this.m_results.put(name, result);   }
	
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
				HAPStructureValueDefinitionFlat resultEle = new HAPStructureValueDefinitionFlat();
				HAPParserContext.parseContext(jsonObj.optJSONObject((String)key), resultEle);
				this.m_results.put((String)key, resultEle);
			}
		}
		
		return true;  
	}
}
