package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementRange extends HAPDataTypeCriteriaImp{

	@HAPAttribute
	public static String DATATYPEFROM = "dataTypeFrom";

	@HAPAttribute
	public static String DATATYPETO = "dataTypeTo";
	
	//general data type
	private HAPDataTypeId m_from;
	
	//more specific data type
	private HAPDataTypeId m_to;

	public HAPDataTypeCriteriaElementRange(HAPDataTypeId from, HAPDataTypeId to){
		this.m_from = from;
		this.m_to = to;
	}
	
	public HAPDataTypeId getFromDataTypeId(){  return this.m_from;  }
	public HAPDataTypeId getToDataTypeId(){  return this.m_to;  }
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPERANGE;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		return dataTypeHelper.getAllDataTypeInRange(m_from, m_to);
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
		HAPDataTypeCriteria out = null;
		if(this.m_from!=null){
			HAPDataTypeId dataTypeId = this.m_from;
			out = new HAPDataTypeCriteriaElementId(dataTypeId);
		}
		else if(this.m_to!=null){
			List<HAPDataTypeId> dataTypeIds = new ArrayList(dataTypeHelper.getRootDataTypeId(m_to));
			if(dataTypeIds.size()==1)		out = new HAPDataTypeCriteriaElementId(dataTypeIds.get(0));
			else out = new HAPDataTypeCriteriaElementIds(new HashSet(dataTypeIds));
		}
		else{
			out = HAPDataTypeCriteriaAny.getCriteria();
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPEFROM, HAPSerializeManager.getInstance().toStringValue(this.m_from, HAPSerializationFormat.LITERATE));
		jsonMap.put(DATATYPETO, HAPSerializeManager.getInstance().toStringValue(this.m_to, HAPSerializationFormat.LITERATE));
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaElementRange){
			HAPDataTypeCriteriaElementRange criteria = (HAPDataTypeCriteriaElementRange)obj;
			out = HAPBasicUtility.isEquals(this.m_from, criteria.m_from) && HAPBasicUtility.isEquals(this.m_to, criteria.m_to);
		}
		return out;
	}
}
