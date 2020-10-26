package com.nosliw.uiresource.page.story.model;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.script.context.HAPContextPath;

@HAPEntityWithAttribute
public class HAPUIDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATATYPE = "dataType";

	@HAPAttribute
	public static final String CONTEXTPATH = "contextPath";

	private HAPDataTypeCriteria m_dataTypeCriteria;
	
	private HAPContextPath m_contextPath;
	
	public HAPDataTypeCriteria getDataTypeCriteria() {	return this.m_dataTypeCriteria;	}
	public void setDataTypeCriteria(HAPDataTypeCriteria dataTypeCriteria) {    this.m_dataTypeCriteria = dataTypeCriteria;      }

	public HAPContextPath getContextPath() {   return this.m_contextPath;   }
	public void setContextPath(HAPContextPath contextPath) {    this.m_contextPath = contextPath;    }
	
	public HAPUIDataInfo cloneUIDataInfo() {
		HAPUIDataInfo out = new HAPUIDataInfo();
		out.m_dataTypeCriteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_dataTypeCriteria);
		out.m_contextPath = this.m_contextPath.clone();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String dataTypeCriteria = (String)jsonObj.opt(DATATYPE);
		this.m_dataTypeCriteria = HAPCriteriaUtility.parseCriteria(dataTypeCriteria);
		JSONObject contextPathObj = jsonObj.optJSONObject(CONTEXTPATH);
		if(contextPathObj!=null) {
			this.m_contextPath = new HAPContextPath();
			this.m_contextPath.buildObject(contextPathObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPE, this.m_dataTypeCriteria.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(CONTEXTPATH, this.m_contextPath.toStringValue(HAPSerializationFormat.JSON));
	}
}
