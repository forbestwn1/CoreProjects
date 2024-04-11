package com.nosliw.core.application.brick.interactive.interfacee;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute(baseName="VARIABLEINFO")
public class HAPRequestParmInInteractiveInterface extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String DATAINFO = "dataInfo";

	@HAPAttribute
	public static String DEFAULTVALUE = "defaultValue";

	private HAPVariableDataInfo m_dataInfo;
	
	private HAPData m_defaultValue;
	
	public static HAPRequestParmInInteractiveInterface buildUndefinedVariableInfo() {
		return buildParm(null);
	}
	
	public static HAPRequestParmInInteractiveInterface buildParmFromObject(Object def) {
		HAPRequestParmInInteractiveInterface out = new HAPRequestParmInInteractiveInterface();
		out.buildObject(def, null);
		return out;
	}

	public static HAPRequestParmInInteractiveInterface buildParm(HAPDataTypeCriteria criteria) {
		return buildParm(null, criteria);
	}

	public static HAPRequestParmInInteractiveInterface buildParm(String name, HAPDataTypeCriteria criteria) {
		HAPRequestParmInInteractiveInterface out = new HAPRequestParmInInteractiveInterface();
		if(criteria!=null)		out.m_dataInfo = new HAPVariableDataInfo(criteria);
		if(name!=null)  out.setName(name);
		return out;
	}
	
	protected HAPRequestParmInInteractiveInterface() {
		this.m_dataInfo = new HAPVariableDataInfo();
	}
	
	public HAPVariableDataInfo getDataInfo(){		return this.m_dataInfo;	}
	
	public HAPDataTypeCriteria getCriteria() {  return this.m_dataInfo.getCriteria();    }
	public void setCriteria(HAPDataTypeCriteria criteria){		this.m_dataInfo.setCriteria(criteria);	}
	
	public HAPData getDefaultValue() {   return this.m_defaultValue;   }
	public void setDefaultValue(HAPData data) {    this.m_defaultValue = data;    }
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPRequestParmInInteractiveInterface){
			HAPRequestParmInInteractiveInterface varInfo = (HAPRequestParmInInteractiveInterface)obj;
			if(HAPUtilityBasic.isEquals(this.m_dataInfo, varInfo.m_dataInfo)){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_dataInfo!=null)	jsonMap.put(DATAINFO, HAPSerializeManager.getInstance().toStringValue(this.m_dataInfo, HAPSerializationFormat.JSON));
		if(this.m_defaultValue!=null)  jsonMap.put(DEFAULTVALUE, this.m_defaultValue.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_dataInfo.setCriteria(HAPParserCriteria.getInstance().parseCriteria((String)value));
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.buildEntityInfoByJson(jsonValue);
			
			Object dataInfoObj = jsonValue.opt(DATAINFO);
			this.m_dataInfo.buildObject(dataInfoObj, null);
			
			this.m_defaultValue = HAPUtilityData.buildDataWrapperFromObject(jsonValue.opt(DEFAULTVALUE));
		}
		return true;
	}
	
	public HAPRequestParmInInteractiveInterface cloneVariableInfo() {
		HAPRequestParmInInteractiveInterface out = new HAPRequestParmInInteractiveInterface();
		this.cloneToEntityInfo(out);
		this.cloneToVariableInfo(out);
		return out;
	}
	
	public void cloneToVariableInfo(HAPRequestParmInInteractiveInterface varInfo) {
		varInfo.m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
		if(this.m_defaultValue!=null)   varInfo.m_defaultValue = this.m_defaultValue.cloneData();
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
