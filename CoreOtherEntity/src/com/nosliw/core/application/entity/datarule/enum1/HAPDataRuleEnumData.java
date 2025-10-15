package com.nosliw.core.application.entity.datarule.enum1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;

@HAPEntityWithAttribute
public class HAPDataRuleEnumData extends HAPDataRuleEnum {

	@HAPAttribute
	public static String DATASET = "dataSet";

	private List<HAPData> m_data;
	
	public HAPDataRuleEnumData() {
		this.m_data = new ArrayList<HAPData>();
	}

	public void addData(HAPData data) {   this.m_data.add(data);    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASET, HAPUtilityJson.buildJson(this.m_data, HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		JSONArray dataArray = valueObj.optJSONArray(DATASET);
		if(dataArray!=null) {
			for(int i=0; i<dataArray.length(); i++) {
				this.m_data.add(HAPUtilityData.buildDataWrapperFromObject(dataArray.get(i)));
			}
		}
		return true;
	}

}
