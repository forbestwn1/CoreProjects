package com.nosliw.core.data.criteria;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDataTypeCriteriaRange extends HAPDataTypeCriteriaImp implements HAPDataTypeCriteriaWithSubCriteria{

	@HAPAttribute
	public static String DATATYPEFROM = "dataTypeFrom";

	@HAPAttribute
	public static String DATATYPETO = "dataTypeTo";
	
	//general data type
	private HAPDataTypeId m_from;
	
	//more specific data type
	private HAPDataTypeId m_to;

	private HAPDataTypeSubCriteriaGroup m_subCriteriaGroup;
	
	public HAPDataTypeCriteriaRange(HAPDataTypeId from, HAPDataTypeId to, HAPDataTypeSubCriteriaGroup subCriteriaGroup){
		this.m_from = from;
		this.m_to = to;
		this.m_subCriteriaGroup = subCriteriaGroup;
	}
	
	public HAPDataTypeId getFromDataTypeId(){  return this.m_from;  }
	public HAPDataTypeId getToDataTypeId(){  return this.m_to;  }
	
	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_DATATYPERANGE;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		return dataTypeHelper.getAllDataTypeInRange(m_from, m_to);
	}

	@Override
	public HAPDataTypeSubCriteriaGroup getSubCriteria() {
		return this.m_subCriteriaGroup;
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		Set<HAPDataTypeId> dataTypeIds = this.getValidDataTypeId(dataTypeHelper);
		Set<HAPDataTypeCriteriaId> out = new HashSet<HAPDataTypeCriteriaId>();
		for(HAPDataTypeId dataTypeId : dataTypeIds){
			out.add(new HAPDataTypeCriteriaId(dataTypeId, this.m_subCriteriaGroup));
		}
		return out;
	}

//	@Override
//	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
//		HAPDataTypeCriteria out = null;
//		if(this.m_from!=null){
//			HAPDataTypeId dataTypeId = this.m_from;
//			out = new HAPDataTypeCriteriaElementId(dataTypeId, this.m_elementDataTypeCriteria);
//		}
//		else if(this.m_to!=null){
//			List<HAPDataTypeId> dataTypeIds = new ArrayList(dataTypeHelper.getRootDataTypeId(m_to));
//			if(dataTypeIds.size()==1)		out = new HAPDataTypeCriteriaElementId(dataTypeIds.get(0), this.m_elementDataTypeCriteria);
//			else out = new HAPDataTypeCriteriaElementIds(new HashSet(dataTypeIds));
//		}
//		else{
//			out = HAPDataTypeCriteriaAny.getCriteria();
//		}
//		return out;
//	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.START_RANGE));
		if(this.m_from!=null)		out.append(HAPManagerSerialize.getInstance().toStringValue(this.m_from, HAPSerializationFormat.LITERATE));
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.RANGE));
		if(this.m_to!=null)		out.append(HAPManagerSerialize.getInstance().toStringValue(this.m_to, HAPSerializationFormat.LITERATE));
		if(this.m_subCriteriaGroup!=null){
			out.append(HAPManagerSerialize.getInstance().toStringValue(m_subCriteriaGroup, HAPSerializationFormat.LITERATE));
		}
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.END_RANGE));
		return out.toString(); 
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATATYPEFROM, HAPManagerSerialize.getInstance().toStringValue(this.m_from, HAPSerializationFormat.LITERATE));
		jsonMap.put(DATATYPETO, HAPManagerSerialize.getInstance().toStringValue(this.m_to, HAPSerializationFormat.LITERATE));
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaRange){
			HAPDataTypeCriteriaRange criteria = (HAPDataTypeCriteriaRange)obj;
			boolean out1 = HAPUtilityBasic.isEquals(this.m_from, criteria.m_from) && HAPUtilityBasic.isEquals(this.m_to, criteria.m_to);
			if(out1){
				out = HAPUtilityBasic.isEquals(this.m_subCriteriaGroup, criteria.m_subCriteriaGroup);
			}
		}
		return out;
	}

}
