package com.nosliw.data.core.domain.entity.test.complex.script;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPUtilitySerialize;
import com.nosliw.data.core.data.HAPDataTypeId;

public class HAPDefinitionVariableExpected extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";
	
	@HAPAttribute
	public static String GROUP = "group";
	
	private String m_variableName;
	
	private HAPDataTypeId m_dataTypeId;
	
	private String m_group;

	
	public String getVariableName() {     return this.m_variableName;      }
	
	public HAPDataTypeId getDataTypeId() {   return this.m_dataTypeId;      }
	
	public String getGroup() {      return this.m_group;     }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_variableName = jsonObj.getString(NAME);
		this.m_dataTypeId = (HAPDataTypeId)HAPUtilitySerialize.buildObject(HAPDataTypeId.class, jsonObj.opt(DATATYPEID));
		this.m_group = (String)jsonObj.opt(GROUP);
		return true;  
	}
}
