package com.nosliw.data.core.process1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;

@HAPEntityWithAttribute
public class HAPDefinitionResultActivityBranch extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String FLOW = "flow";

	@HAPAttribute
	public static String DATA = "data";

	//next activity
	private HAPDefinitionSequenceFlow m_flow;
	
	private HAPData m_data;
	
	public HAPDefinitionSequenceFlow getFlow() {  return this.m_flow;  }
	
	public HAPData getData() {    return this.m_data;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			this.m_flow = new HAPDefinitionSequenceFlow();
			this.m_flow.buildObject(jsonObj.optJSONObject(FLOW), HAPSerializationFormat.JSON);
			
			JSONObject dataJsonObj = jsonObj.optJSONObject(DATA);
			if(dataJsonObj!=null)	this.m_data = HAPUtilityData.buildDataWrapperFromJson(dataJsonObj);
			
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
		jsonMap.put(FLOW, m_flow.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_data!=null)  jsonMap.put(DATA, m_data.toStringValue(HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionResultActivityBranch cloneBranchActivityResultDefinition() {
		HAPDefinitionResultActivityBranch out = new HAPDefinitionResultActivityBranch();
		this.cloneToEntityInfo(out);
		if(this.m_data!=null)  out.m_data = this.m_data.cloneData();
		out.m_flow = this.m_flow.cloneSequenceFlowDefinition();
		return out;
	}
}
