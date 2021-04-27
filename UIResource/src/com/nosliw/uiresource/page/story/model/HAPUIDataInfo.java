package com.nosliw.uiresource.page.story.model;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.structure.HAPPathStructure;

@HAPEntityWithAttribute
public class HAPUIDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATATYPE = "dataType";

	@HAPAttribute
	public static final String CONTEXTPATH = "contextPath";

	private HAPVariableDataInfo m_dataType;
	
	private HAPPathStructure m_contextPath;
	
	public HAPVariableDataInfo getDataType() {	return this.m_dataType;	}
	public void setDataType(HAPVariableDataInfo dataTypeCriteria) {    this.m_dataType = dataTypeCriteria;      }

	public HAPPathStructure getContextPath() {   return this.m_contextPath;   }
	public void setContextPath(HAPPathStructure contextPath) {    this.m_contextPath = contextPath;    }
	
	public HAPUIDataInfo cloneUIDataInfo() {
		HAPUIDataInfo out = new HAPUIDataInfo();
		out.m_dataType = this.m_dataType.cloneVariableDataInfo();
		out.m_contextPath = this.m_contextPath.clone();
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
		JSONObject contextPathObj = jsonObj.optJSONObject(CONTEXTPATH);
		if(contextPathObj!=null) {
			this.m_contextPath = new HAPPathStructure();
			this.m_contextPath.buildObject(contextPathObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPE, this.m_dataType.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXTPATH, this.m_contextPath.toStringValue(HAPSerializationFormat.JSON));
	}
}
