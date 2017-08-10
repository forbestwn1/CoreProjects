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

	private Map<String, HAPDataTypeCriteria> m_elementDataTypeCriteria;
	
	public HAPDataTypeCriteriaElementRange(HAPDataTypeId from, HAPDataTypeId to, Map<String, HAPDataTypeCriteria> elementDataTypeCriteria){
		this.m_from = from;
		this.m_to = to;
		this.m_elementDataTypeCriteria = elementDataTypeCriteria;
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
			out = new HAPDataTypeCriteriaElementId(dataTypeId, this.m_elementDataTypeCriteria);
		}
		else if(this.m_to!=null){
			List<HAPDataTypeId> dataTypeIds = new ArrayList(dataTypeHelper.getRootDataTypeId(m_to));
			if(dataTypeIds.size()==1)		out = new HAPDataTypeCriteriaElementId(dataTypeIds.get(0), this.m_elementDataTypeCriteria);
			else out = new HAPDataTypeCriteriaElementIds(new HashSet(dataTypeIds));
		}
		else{
			out = HAPDataTypeCriteriaAny.getCriteria();
		}
		return out;
	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_RANGE));
		if(this.m_from!=null)		out.append(HAPSerializeManager.getInstance().toStringValue(this.m_from, HAPSerializationFormat.LITERATE));
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.RANGE));
		if(this.m_to!=null)		out.append(HAPSerializeManager.getInstance().toStringValue(this.m_to, HAPSerializationFormat.LITERATE));
		
		if(this.m_elementDataTypeCriteria!=null && !this.m_elementDataTypeCriteria.isEmpty()){
			out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_ELEMENT));

			int i = 0;
			for(String name : this.m_elementDataTypeCriteria.keySet()){
				if(i!=0)   out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.COMMAR));
				out.append(name);
				out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.ASSIGNMENT));
				out.append(HAPSerializeManager.getInstance().toStringValue(this.m_elementDataTypeCriteria.get(name), HAPSerializationFormat.LITERATE));
				i++;
			}
			
			out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_ELEMENT));
		}
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_RANGE));
		
		return out.toString(); 
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
			boolean out1 = HAPBasicUtility.isEquals(this.m_from, criteria.m_from) && HAPBasicUtility.isEquals(this.m_to, criteria.m_to);
			if(out1){
				out = HAPBasicUtility.isEqualMaps(this.m_elementDataTypeCriteria, criteria.m_elementDataTypeCriteria);
			}
		}
		return out;
	}
}
