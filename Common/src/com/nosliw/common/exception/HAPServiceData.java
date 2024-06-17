package com.nosliw.common.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;

/*
 * class to store information for operation result,
 * so it have the result code, result message, result data, extra information (parms)
 * success or fail:
 * 		if result code less or equal than SERVICECODE_SUCCESS, we consider the operation success
 * 		if result code more or eqal than SERVICECODE_FAIL, we consier the operation fail
 */
@HAPEntityWithAttribute
public class HAPServiceData extends HAPSerializableImp{

	@HAPAttribute
	public static final String SERVICEDATA_CODE = "code";
	@HAPAttribute
	public static final String SERVICEDATA_MESSAGE = "message";
	@HAPAttribute
	public static final String SERVICEDATA_DATA = "data";
	@HAPAttribute
	public static final String SERVICEDATA_METADATA = "metaData";
	
	//result code
	private int m_code = HAPConstantShared.SERVICECODE_SUCCESS;
	//result message
	private String m_message = null;
	//result data
	private Object m_data = null;
	//result extra value, not directed with result
	private Map<String, String> m_metaDatas;
	//exception
	private Exception m_exception;
	
	public HAPServiceData(){
		this.m_metaDatas = new LinkedHashMap<String, String>();
	}
	
	public int getCode(){return this.m_code;}
	public String getMessage(){return this.m_message;}
	public Object getData(){return this.m_data;}
	public void setData(Object data){this.m_data=data;}
	public Exception getException(){return this.m_exception;}
	public void setException(Exception ex){this.m_exception = ex;}
	
	public void setMetaData(String name, String value){this.m_metaDatas.put(name, value);}
	public String getMetaData(String name){return this.m_metaDatas.get(name);}
	
	public boolean isSuccess(){return this.m_code<=HAPConstantShared.SERVICECODE_SUCCESS;}
	public boolean isFail(){return this.m_code>=HAPConstantShared.SERVICECODE_ERROR;}
	
	public static HAPServiceData createSuccessData(){return HAPServiceData.createSuccessData(null);}
	
	public static HAPServiceData createSuccessData(Object data){
		HAPServiceData out = new HAPServiceData();
		out.m_code = HAPConstantShared.SERVICECODE_SUCCESS;
		out.m_data = data;
		return out;
	}

	public static HAPServiceData createFailureData(){
		return HAPServiceData.createServiceData(HAPConstantShared.SERVICECODE_ERROR, null, "");
	}
	public static HAPServiceData createFailureData(Object data, String message){
		return HAPServiceData.createServiceData(HAPConstantShared.SERVICECODE_ERROR, data, message);
	}
	
	public static HAPServiceData createServiceData(int code, Object data, String message){
		HAPServiceData out = new HAPServiceData();
		out.m_code = code;
		out.m_data = data;
		out.m_message = message;
		return out;
	}
	
	@Override
	public String toStringValue(HAPSerializationFormat format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> jsonTypeMap = new LinkedHashMap<String, Class<?>>();
		
		//code
		jsonMap.put(SERVICEDATA_CODE, String.valueOf(this.getCode()));
		jsonTypeMap.put(SERVICEDATA_CODE, Integer.class);
		
		//message
		jsonMap.put(SERVICEDATA_MESSAGE, this.getMessage());

		//data
		Object data = this.getData();
		String dataString = null;
		if(data!=null){
			
			dataString = HAPManagerSerialize.getInstance().toStringValue(data, format);
			
//			if(data instanceof HAPSerializable){
//				dataString = ((HAPSerializable)data).toStringValue(format);
//			}
//			else if(data instanceof String){
//				dataString = (String)data;
//			}
//			else if(data instanceof List){
//				dataString = HAPJsonUtility.buildJson((List)data, format);
//			}
//			else if(data instanceof Set){
//				dataString = HAPJsonUtility.buildJson((Set)data, format);
//			}
//			else if(data instanceof Map){
//				dataString = HAPJsonUtility.buildJson((Map)data, format);
//			}
//			else if(data.getClass().isArray()){
//				dataString = HAPJsonUtility.buildJson((Object[])data, format);
//			}
		}
		jsonMap.put(SERVICEDATA_DATA, dataString);
		
		//parms
		jsonMap.put(SERVICEDATA_METADATA, HAPUtilityJson.buildMapJson(this.m_metaDatas));
		
		return HAPUtilityJson.buildMapJson(jsonMap, jsonTypeMap);
	}
	
	@Override
	public String toString(){
		return HAPUtilityJson.formatJson(this.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		if(json instanceof JSONObject){
			JSONObject jsonObj = (JSONObject)json;
			this.m_code = jsonObj.optInt(SERVICEDATA_CODE);
			this.m_message = jsonObj.optString(SERVICEDATA_MESSAGE);
			this.m_data = jsonObj.opt(SERVICEDATA_DATA);
		}
		return true;  
	}

}
