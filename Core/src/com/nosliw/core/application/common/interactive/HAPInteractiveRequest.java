package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInteractiveRequest extends HAPSerializableImp{

	@HAPAttribute
	public static String PARM = "parm";
	
	private List<HAPRequestParmInInteractive> m_requestParms;

	public HAPInteractiveRequest() {
		this.m_requestParms = new ArrayList<HAPRequestParmInInteractive>();
	}
	
	public HAPInteractiveRequest(List<HAPRequestParmInInteractive> requestParms) {
		this();
		this.m_requestParms = requestParms;
		this.initValuePort();
	}

	private void initValuePort() {
	}
	
	public List<HAPRequestParmInInteractive> getRequestParms() {   return this.m_requestParms;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONArray parmsArray = (JSONArray)json;
		for(int i=0; i<parmsArray.length(); i++) {
			HAPRequestParmInInteractive parm = HAPRequestParmInInteractive.buildParmFromObject(parmsArray.get(i));
			m_requestParms.add(parm);
		}
		this.initValuePort();
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARM, HAPManagerSerialize.getInstance().toStringValue(this.getRequestParms(), HAPSerializationFormat.JSON));
	}
}

