package com.nosliw.data.core.codetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;

@HAPEntityWithAttribute
public class HAPCodeTable extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATASET = "dataSet";

	private List<HAPData> m_dataSet;
	
	public HAPCodeTable() {
		this.m_dataSet = new ArrayList<HAPData>();
	}
	
	public List<HAPData> getDataSet(){    return this.m_dataSet;    }
	
	public void addData(HAPData data) {    this.m_dataSet.add(data);     }
	public void setDataSet(List<HAPData> dataSet) {     
		this.m_dataSet.clear();
		this.m_dataSet.addAll(dataSet);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATASET, HAPJsonUtility.buildJson(this.m_dataSet, HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		JSONArray dataSetArray = valueObj.optJSONArray(DATASET);
		if(dataSetArray!=null) {
			for(int i=0; i<dataSetArray.length(); i++) {
				HAPData dataEle = HAPUtilityData.buildDataWrapperFromObject(dataSetArray.opt(i));
				this.addData(dataEle);
			}
		}
		return true;
	}
}
