package com.nosliw.data.core.interactive;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

@HAPEntityWithAttribute
public class HAPOutputInteractive extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String REFERENCE = "reference";

	@HAPAttribute
	public static String DATA = "data";

	//data type
	private HAPDataTypeCriteria m_criteria;
	
	//path to relative node
	private HAPReferenceElementInValueContext m_reference;

	private HAPData m_constantData;
	
	public HAPOutputInteractive() {}

	public HAPOutputInteractive(HAPDataTypeCriteria criteria) {
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria() {   return this.m_criteria; }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }

	public HAPData getConstantData() {   return this.m_constantData;     }
	
	public HAPReferenceElementInValueContext getReferenceInfo() {   return this.m_reference;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null)	jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		if(this.m_reference!=null)  jsonMap.put(REFERENCE, this.m_reference.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_constantData!=null)  jsonMap.put(DATA, this.m_constantData.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.buildEntityInfoByJson(jsonValue);
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
			this.m_reference = new HAPReferenceElementInValueContext();
			this.m_reference.buildObject(jsonValue.opt(REFERENCE), HAPSerializationFormat.JSON);
			Object dataObj = jsonValue.opt(DATA);
			if(dataObj!=null) 		HAPUtilityData.buildDataWrapperFromObject(dataObj);
		}
		return true;
	}
	
	public HAPOutputInteractive cloneInteractiveOutput() {
		HAPOutputInteractive out = new HAPOutputInteractive();
		this.cloneToEntityInfo(out);
		out.m_criteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_criteria);
		out.m_reference = this.m_reference;
		if(this.m_constantData!=null) out.m_constantData = this.m_constantData.cloneData();
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}

}
