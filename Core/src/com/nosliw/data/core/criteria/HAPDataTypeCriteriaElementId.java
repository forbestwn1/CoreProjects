package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementId  extends HAPDataTypeCriteriaImp{

	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";
	
	private HAPDataTypeId m_dataTypeId;

	public HAPDataTypeCriteriaElementId(HAPDataTypeId dataTypeId){
		this.m_dataTypeId = dataTypeId;
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;  }
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEID;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper){
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		out.add(m_dataTypeId);
		return out;
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {		return this;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPEID, HAPSerializeManager.getInstance().toStringValue(m_dataTypeId, HAPSerializationFormat.LITERATE));
	}
}
