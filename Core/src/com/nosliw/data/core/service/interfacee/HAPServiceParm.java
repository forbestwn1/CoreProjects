package com.nosliw.data.core.service.interfacee;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPUtilityData;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public class HAPServiceParm extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";
	
	@HAPAttribute
	public static String DEFAULT = "default";

	private HAPDataTypeCriteria m_criteria;
	
	private HAPData m_default;
	
	public HAPServiceParm(){	}

	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;   }
	
	public HAPData getDefault(){   return this.m_default;   }
	
	public void setCriteria(HAPDataTypeCriteria criteria) {  this.m_criteria = criteria;   }
	
	public void setDefault(HAPData data) { this.m_default = data;   }
	
	public HAPServiceParm cloneServiceParm() {
		HAPServiceParm out = new HAPServiceParm();
		this.cloneToEntityInfo(out);
		if(this.m_criteria!=null)   out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		if(this.m_default!=null)  out.m_default = this.m_default.cloneData();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);

			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria(objJson.getString(CRITERIA));
			
			JSONObject defaultJson = objJson.optJSONObject(DEFAULT);
			if(defaultJson!=null)	this.m_default = HAPUtilityData.buildDataWrapperFromJson(defaultJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CRITERIA, this.m_criteria.toStringValue(HAPSerializationFormat.LITERATE));
		if(this.m_default!=null)  jsonMap.put(DEFAULT, this.m_default.toString());
	}
}
