package com.nosliw.uiresource.page.story.model;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;

@HAPEntityWithAttribute
public class HAPUIDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATATYPE = "dataType";

	@HAPAttribute
	public static final String CONTEXTPATH = "contextPath";

	private HAPVariableDataInfo m_dataType;
	
	private HAPComplexPath m_contextPath;
	
	public HAPVariableDataInfo getDataType() {	return this.m_dataType;	}
	public void setDataType(HAPVariableDataInfo dataTypeCriteria) {    this.m_dataType = dataTypeCriteria;      }

	public HAPComplexPath getContextPath() {   return this.m_contextPath;   }
	public void setContextPath(HAPComplexPath contextPath) {    this.m_contextPath = contextPath;    }
	
	public HAPUIDataInfo cloneUIDataInfo() {
		HAPUIDataInfo out = new HAPUIDataInfo();
		out.m_dataType = this.m_dataType.cloneVariableDataInfo();
		out.m_contextPath = this.m_contextPath.cloneComplexPath();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject dataTypeObj = jsonObj.optJSONObject(DATATYPE);
		if(dataTypeObj!=null) {
			this.m_dataType = new HAPVariableDataInfo();
			this.m_dataType.buildObject(dataTypeObj, HAPSerializationFormat.JSON);
		}
		String contextPathStr = (String)jsonObj.opt(CONTEXTPATH);
		if(contextPathStr!=null) {
			this.m_contextPath = new HAPComplexPath(contextPathStr);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXTPATH, this.m_contextPath.toStringValue(HAPSerializationFormat.LITERATE));
	}
}
