package com.nosliw.uiresource.page.tag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;

@HAPEntityWithAttribute
public class HAPUITageQueryData extends HAPSerializableImp{

	@HAPAttribute
	final public static String DATATYPECRITERIA = "dataTypeCriteria";

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	public HAPUITageQueryData(HAPDataTypeCriteria dataTypeCriteria) {
		this.m_dataTypeCriteria = dataTypeCriteria;
	}
	
	public HAPDataTypeCriteria getDataTypeCriterai() {   return this.m_dataTypeCriteria;  }
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {   this.m_dataTypeCriteria = dataTypeCriteria;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPECRITERIA, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_dataTypeCriteria = HAPUtilityCriteria.parseCriteria(jsonObj.getString(DATATYPECRITERIA));
		return true;  
	}	
}
