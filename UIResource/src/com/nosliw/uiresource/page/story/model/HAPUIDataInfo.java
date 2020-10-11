package com.nosliw.uiresource.page.story.model;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public class HAPUIDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATATYPE = "dataType";

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPDataTypeCriteria getDataTypeCriteria() {	return this.m_dataTypeCriteria;	}
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {    this.m_dataTypeCriteria = dataTypeCriteria;      }

	public HAPUIDataInfo cloneUIDataInfo() {
		HAPUIDataInfo out = new HAPUIDataInfo();
		out.m_dataTypeCriteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_dataTypeCriteria);
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String dataTypeCriteria = (String)jsonObj.opt(DATATYPE);
		this.m_dataTypeCriteria = HAPCriteriaUtility.parseCriteria(dataTypeCriteria);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPE, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
