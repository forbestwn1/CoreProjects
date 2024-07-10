package com.nosliw.core.application.common.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute(baseName="VARIABLEINFO")
public class HAPVariableDefinition extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String DATAINFO = "dataInfo";

	@HAPAttribute
	public static String REFERENCE = "reference";

	@HAPAttribute
	public static String DEFAULTVALUE = "defaultValue";

	private HAPVariableDataInfo m_dataInfo;
	
	private HAPReferenceElement m_reference;

	private HAPData m_defaultValue;
	
	public static HAPVariableDefinition buildUndefinedVariableInfo() {
		return buildVariableInfo(null);
	}
	
	public static HAPVariableDefinition buildVariableInfoFromObject(Object def) {
		HAPVariableDefinition out = new HAPVariableDefinition();
		out.buildObject(def, null);
		return out;
	}

	public static HAPVariableDefinition buildVariableInfo(HAPDataTypeCriteria criteria) {
		return buildVariableInfo(null, criteria);
	}

	public static HAPVariableDefinition buildVariableInfo(String name, HAPDataTypeCriteria criteria) {
		HAPVariableDefinition out = new HAPVariableDefinition();
		if(criteria!=null)		out.m_dataInfo = new HAPVariableDataInfo(criteria);
		if(name!=null)  out.setName(name);
		return out;
	}
	
	protected HAPVariableDefinition() {
		this.m_dataInfo = new HAPVariableDataInfo();
	}
	
	public HAPReferenceElement getReferenceInfo() {    return this.m_reference;     }
	
	public HAPVariableDataInfo getDataInfo(){		return this.m_dataInfo;	}
	
	public HAPDataTypeCriteria getCriteria() {  return this.m_dataInfo.getCriteria();    }
	public void setCriteria(HAPDataTypeCriteria criteria){		this.m_dataInfo.setCriteria(criteria);	}
	
	public HAPData getDefaultValue() {   return this.m_defaultValue;   }
	public void setDefaultValue(HAPData data) {    this.m_defaultValue = data;    }
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableDefinition){
			HAPVariableDefinition varInfo = (HAPVariableDefinition)obj;
			if(HAPUtilityBasic.isEquals(this.m_dataInfo, varInfo.m_dataInfo)){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_dataInfo!=null)	jsonMap.put(DATAINFO, HAPManagerSerialize.getInstance().toStringValue(this.m_dataInfo, HAPSerializationFormat.JSON));
		if(this.m_defaultValue!=null)  jsonMap.put(DEFAULTVALUE, this.m_defaultValue.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_reference!=null)  jsonMap.put(REFERENCE, this.m_reference.toStringValue(HAPSerializationFormat.JSON));
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

			Object referenceObj = jsonValue.opt(REFERENCE);
			if(referenceObj!=null) {
				this.m_reference = new HAPReferenceElement();
				this.m_reference.buildObject(referenceObj, HAPSerializationFormat.JSON);
			}
		}
		return true;
	}
	
	public HAPVariableDefinition cloneVariableInfo() {
		HAPVariableDefinition out = new HAPVariableDefinition();
		this.cloneToEntityInfo(out);
		this.cloneToVariableInfo(out);
		if(this.m_reference!=null)  out.m_reference = this.m_reference.cloneReferenceInfo(); 
		return out;
	}
	
	public void cloneToVariableInfo(HAPVariableDefinition varInfo) {
		varInfo.m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
		if(this.m_defaultValue!=null)   varInfo.m_defaultValue = this.m_defaultValue.cloneData();
		if(this.m_reference!=null)  varInfo.m_reference = this.m_reference.cloneReferenceInfo(); 
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
