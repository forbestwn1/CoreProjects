package com.nosliw.core.application.common.datadefinition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;

public class HAPDataDefinitionWritableWithInit extends HAPDataDefinitionWritable{

	@HAPAttribute
	public static String INITDATA = "initData";

	private HAPData m_initData;

	public HAPDataDefinitionWritableWithInit() {}
	
	public HAPData getInitData() {    return this.m_initData;     }
	public void setInitData(HAPData initData) {    this.m_initData = initData;      }

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataDefinitionWritableWithInit){
			HAPDataDefinitionWritableWithInit dataInfo = (HAPDataDefinitionWritableWithInit)obj;
			if(super.equals(dataInfo) && HAPUtilityBasic.isEquals(this.getInitData(), dataInfo.getInitData()))
			{
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_initData!=null) {
			jsonMap.put(INITDATA, this.m_initData.toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		super.buildObject(value, format);
		if(value instanceof String) {
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			
			Object initDataObj = jsonValue.opt(INITDATA);
			if(initDataObj!=null) {
				this.m_initData = HAPUtilityData.buildDataWrapperFromObject(initDataObj);
			}
		}
		return true;
	}
	
	protected void cloneToDataDefinitionWritableWithInit(HAPDataDefinitionWritableWithInit out) {
		this.cloneToDataDefinitionWritable(out);
		if(this.m_initData!=null) {
			out.m_initData = this.m_initData.cloneData();
		}
	}
	
	public HAPDataDefinitionWritableWithInit cloneDataDefinitionWritableWithInit() {
		HAPDataDefinitionWritableWithInit out = new HAPDataDefinitionWritableWithInit();
		this.cloneToDataDefinitionWritableWithInit(out);
		return out;
	}
	
}
