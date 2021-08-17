package com.nosliw.data.core.data.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPCriteriaParser;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.structure.reference.HAPInfoPathReference;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute(baseName="VARIABLEINFO")
public class HAPVariableInfo extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String DATAINFO = "dataInfo";

	@HAPAttribute
	public static String REFERENCE = "reference";

	@HAPAttribute
	public static String DEFAULTVALUE = "defaultValue";

	private HAPVariableDataInfo m_dataInfo;
	
	private HAPInfoPathReference m_reference;

	private HAPData m_defaultValue;
	
	public static HAPVariableInfo buildUndefinedVariableInfo() {
		return buildVariableInfo(null);
	}
	
	public static HAPVariableInfo buildVariableInfoFromObject(Object def) {
		HAPVariableInfo out = new HAPVariableInfo();
		out.buildObject(def, null);
		return out;
	}

	public static HAPVariableInfo buildVariableInfo(HAPDataTypeCriteria criteria) {
		return buildVariableInfo(null, criteria);
	}

	public static HAPVariableInfo buildVariableInfo(String name, HAPDataTypeCriteria criteria) {
		HAPVariableInfo out = new HAPVariableInfo();
		if(criteria!=null)		out.m_dataInfo = new HAPVariableDataInfo(criteria);
		if(name!=null)  out.setName(name);
		return out;
	}
	
	protected HAPVariableInfo() {
		this.m_dataInfo = new HAPVariableDataInfo();
	}
	
	public HAPInfoPathReference getReferenceInfo() {    return this.m_reference;     }
	
	public HAPVariableDataInfo getDataInfo(){		return this.m_dataInfo;	}
	
	public HAPDataTypeCriteria getCriteria() {  return this.m_dataInfo.getCriteria();    }
	public void setCriteria(HAPDataTypeCriteria criteria){		this.m_dataInfo.setCriteria(criteria);	}
	
	public HAPData getDefaultValue() {   return this.m_defaultValue;   }
	public void setDefaultValue(HAPData data) {    this.m_defaultValue = data;    }
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableInfo){
			HAPVariableInfo varInfo = (HAPVariableInfo)obj;
			if(HAPBasicUtility.isEquals(this.m_dataInfo, varInfo.m_dataInfo)){
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
		if(this.m_reference!=null)  jsonMap.put(REFERENCE, this.m_reference.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_dataInfo.setCriteria(HAPCriteriaParser.getInstance().parseCriteria((String)value));
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.buildEntityInfoByJson(jsonValue);
			
			Object dataInfoObj = jsonValue.opt(DATAINFO);
			this.m_dataInfo.buildObject(dataInfoObj, null);
			
			this.m_defaultValue = HAPUtilityData.buildDataWrapperFromObject(jsonValue.opt(DEFAULTVALUE));

			this.m_reference = new HAPInfoPathReference();
			this.m_reference.buildObject(jsonValue.opt(REFERENCE), HAPSerializationFormat.JSON);
		}
		return true;
	}
	
	public HAPVariableInfo cloneVariableInfo() {
		HAPVariableInfo out = new HAPVariableInfo();
		this.cloneToEntityInfo(out);
		this.cloneToVariableInfo(out);
		if(this.m_reference!=null)  out.m_reference = this.m_reference.cloneReferencePathInfo(); 
		return out;
	}
	
	public void cloneToVariableInfo(HAPVariableInfo varInfo) {
		varInfo.m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
		if(this.m_defaultValue!=null)   varInfo.m_defaultValue = this.m_defaultValue.cloneData();
		if(this.m_reference!=null)  varInfo.m_reference = this.m_reference.cloneReferencePathInfo(); 
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
