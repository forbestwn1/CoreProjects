package com.nosliw.common.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPAttributeConstant;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * class to store information for operation result,
 * so it have the result code, result message, result data, extra information (parms)
 * success or fail:
 * 		if result code less or equal than SERVICECODE_SUCCESS, we consider the operation success
 * 		if result code more or eqal than SERVICECODE_FAIL, we consier the operation fail
 */
public class HAPServiceData implements HAPSerializable{

	//result code
	private int m_code = HAPConstant.SERVICECODE_SUCCESS;
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
	
	public boolean isSuccess(){return this.m_code<=HAPConstant.SERVICECODE_SUCCESS;}
	public boolean isFail(){return this.m_code>=HAPConstant.SERVICECODE_FAILURE;}
	
	public static HAPServiceData createSuccessData(){return HAPServiceData.createSuccessData(null);}
	
	public static HAPServiceData createSuccessData(Object data){
		HAPServiceData out = new HAPServiceData();
		out.m_code = HAPConstant.SERVICECODE_SUCCESS;
		out.m_data = data;
		return out;
	}

	public static HAPServiceData createFailureData(){return HAPServiceData.createServiceData(HAPConstant.SERVICECODE_FAILURE, null, "");}
	public static HAPServiceData createFailureData(Object data, String message){return HAPServiceData.createServiceData(HAPConstant.SERVICECODE_FAILURE, data, message);}
	
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
		jsonMap.put(HAPAttributeConstant.SERVICEDATA_CODE, String.valueOf(this.getCode()));
		jsonTypeMap.put(HAPAttributeConstant.SERVICEDATA_CODE, Integer.class);
		
		//message
		jsonMap.put(HAPAttributeConstant.SERVICEDATA_MESSAGE, this.getMessage());

		//data
		Object data = this.getData();
		String dataString = null;
		if(data!=null){
			if(data instanceof HAPSerializable){
				dataString = ((HAPSerializable)data).toStringValue(format);
			}
			else if(data instanceof String){
				dataString = (String)data;
			}
			else if(data instanceof List){
				dataString = HAPJsonUtility.buildJson((List)data, format);
			}
			else if(data instanceof Set){
				dataString = HAPJsonUtility.buildJson((Set)data, format);
			}
			else if(data instanceof Map){
				dataString = HAPJsonUtility.buildJson((Map)data, format);
			}
			else if(data.getClass().isArray()){
				dataString = HAPJsonUtility.buildJson((Object[])data, format);
			}
		}
		jsonMap.put(HAPAttributeConstant.SERVICEDATA_DATA, dataString);
		
		//parms
		jsonMap.put(HAPAttributeConstant.SERVICEDATA_METADATA, HAPJsonUtility.buildMapJson(this.m_metaDatas));
		
		return HAPJsonUtility.buildMapJson(jsonMap, jsonTypeMap);
	}
	
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));
	}
}
