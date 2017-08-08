package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaElementId  extends HAPDataTypeCriteriaImp{

	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";
	
	private HAPDataTypeId m_dataTypeId;

	private Map<String, HAPDataTypeCriteria> m_elementDataTypeCriteria;
	
	public HAPDataTypeCriteriaElementId(HAPDataTypeId dataTypeId, Map<String, HAPDataTypeCriteria> elementDataTypeCriteria){
		this.m_dataTypeId = dataTypeId;
		this.m_elementDataTypeCriteria = elementDataTypeCriteria;
	}

	public HAPDataTypeId getDataTypeId(){  return this.m_dataTypeId;  }
	
	/**
	 * For some complex datatype (array, map), we need to describe the data type for child element
	 * For instance, we need data type criteria for element in array, attribute in map 
	 * In order to validate on data type or data type criteria, both parent and children data type criteria have to meet
	 * @return
	 */
	public Map<String, HAPDataTypeCriteria> getChildrenElementDataTypeCriteria(){	return this.m_elementDataTypeCriteria;	}
	
	
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
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaElementId){
			HAPDataTypeCriteriaElementId criteria = (HAPDataTypeCriteriaElementId)obj;
			out = HAPBasicUtility.isEquals(this.m_dataTypeId, criteria.m_dataTypeId);
		}
		return out;
	}
}
