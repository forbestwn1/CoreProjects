package com.nosliw.data.core.service.interfacee;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.criteria.HAPCriteriaParser;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public class HAPServiceOutput extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";
	
	private HAPDataTypeCriteria m_criteria;
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;   }
	
	public void setCriteria(HAPDataTypeCriteria criteria) {   this.m_criteria = criteria;   }
	
	public HAPServiceOutput(){
	}

	public HAPServiceOutput cloneServiceOutput() {
		HAPServiceOutput out = new HAPServiceOutput();
		this.cloneToEntityInfo(out);
		if(this.m_criteria!=null)  out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria(objJson.getString(CRITERIA));
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
	}

}
