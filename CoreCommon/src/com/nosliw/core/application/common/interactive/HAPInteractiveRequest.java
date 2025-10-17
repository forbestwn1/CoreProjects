package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionParm;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPInteractiveRequest extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	private List<HAPDefinitionParm> m_requestParms;

	public HAPInteractiveRequest() {
		this.m_requestParms = new ArrayList<HAPDefinitionParm>();
	}
	
	public HAPInteractiveRequest(List<HAPDefinitionParm> requestParms) {
		this();
		this.m_requestParms = requestParms;
		this.initValuePort();
	}

	private void initValuePort() {
	}
	
	public List<HAPDefinitionParm> getRequestParms() {   return this.m_requestParms;  }
	public void addRequestParm(HAPDefinitionParm parm) {   this.m_requestParms.add(parm);    }
	
	
	public static HAPInteractiveRequest parse(JSONArray parmsArray, HAPManagerDataRule dataRuleMan) {
		HAPInteractiveRequest out = new HAPInteractiveRequest();
		for(int i=0; i<parmsArray.length(); i++) {
			JSONObject parmJson = parmsArray.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(parmJson)){
				HAPDefinitionParm parm = HAPDefinitionParm.buildParmFromObject(parmJson, dataRuleMan);
				out.addRequestParm(parm);
			}
		}
		out.initValuePort();
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARM, HAPManagerSerialize.getInstance().toStringValue(this.getRequestParms(), HAPSerializationFormat.JSON));
	}
}

