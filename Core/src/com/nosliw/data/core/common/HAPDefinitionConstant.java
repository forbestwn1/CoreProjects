package com.nosliw.data.core.common;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;

public class HAPDefinitionConstant extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String DATA = "data";

	private HAPData m_data;
	
	public HAPData getData() {    return this.m_data;    }
	public void setData(HAPData data) {  this.m_data = data;    }

	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		HAPDataUtility.buildDataWrapperFromObject(jsonObj.get(DATA));
		return true;  
	}

}
