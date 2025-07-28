package com.nosliw.core.application.brick.task.flow;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;

@HAPEntityWithAttribute
public class HAPTaskFlowDecisionJS extends HAPSerializableImp implements HAPTaskFlowDecision{

	@HAPAttribute
	public static final String SCRIPT = "script";

	private HAPJsonTypeScript m_script;
	
	@Override
	public String getType() {
		return HAPConstantShared.FLOW_DECISION_TYPE_JAVASCRIPT;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("script", this.m_script.toString());
		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPTaskFlowDecisionJS.class, "decisionjsfunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		
		jsonMap.put(SCRIPT, script);
		typeJsonMap.put(SCRIPT, this.m_script.getClass());
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_script = new HAPJsonTypeScript(HAPUtilityJson.unescape(jsonObj.getString(SCRIPT)));
		return true;  
	}
}
