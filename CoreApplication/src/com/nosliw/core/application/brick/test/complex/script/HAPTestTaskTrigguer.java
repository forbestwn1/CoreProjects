package com.nosliw.core.application.brick.test.complex.script;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.task.HAPInfoTrigguerTask;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPTestTaskTrigguer extends HAPSerializableImp{

	@HAPAttribute
	public static final String TRIGGUERINFO = "trigguerInfo";
	
	@HAPAttribute
	public static final String TESTDATA = "testData";
	
	private HAPInfoTrigguerTask m_taskTrigguerInfo;
	
	private Object m_testData;
	
	public HAPTestTaskTrigguer() {}
	
	public HAPInfoTrigguerTask getTaskTrigguerInfo() {   return this.m_taskTrigguerInfo;    }
	public void setTaskTrigguerInfo(HAPInfoTrigguerTask taskInfo) {   this.m_taskTrigguerInfo = taskInfo;    }
	
	public Object getTestData() {    return this.m_testData;     }
	public void setTaskData(Object testData) {   this.m_testData = testData;      }
	
	public static HAPTestTaskTrigguer parsTestTaskTrigguer(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPTestTaskTrigguer out = new HAPTestTaskTrigguer();
		out.setTaskTrigguerInfo(HAPInfoTrigguerTask.parseInfoTrigguerTask(jsonObj.optJSONObject(TRIGGUERINFO), dataRuleMan));
		out.setTaskData(jsonObj.opt(TESTDATA));
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TRIGGUERINFO, this.m_taskTrigguerInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TESTDATA, HAPManagerSerialize.getInstance().toStringValue(m_testData, HAPSerializationFormat.JSON));
	}
}
