package com.nosliw.core.application.brick.test.complex.script;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.task.HAPInfoTrigguerTask;

@HAPEntityWithAttribute
public class HAPTestTaskTrigguer extends HAPSerializableImp{

	@HAPAttribute
	public static final String TRIGGUERINFO = "trigguerInfo";
	
	@HAPAttribute
	public static final String TESTDATA = "testData";
	
	private HAPInfoTrigguerTask m_taskTrigguerInfo;
	
	private Object m_testData;
	
	public HAPInfoTrigguerTask getTaskTrigguerInfo() {   return this.m_taskTrigguerInfo;    }
	
	public Object getTestData() {    return this.m_testData;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_taskTrigguerInfo = new HAPInfoTrigguerTask();
		this.m_taskTrigguerInfo.buildObject(jsonObj.opt(TRIGGUERINFO), HAPSerializationFormat.JSON);

		this.m_testData = jsonObj.opt(TESTDATA);
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TRIGGUERINFO, this.m_taskTrigguerInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TESTDATA, HAPManagerSerialize.getInstance().toStringValue(m_testData, HAPSerializationFormat.JSON));
	}
}
