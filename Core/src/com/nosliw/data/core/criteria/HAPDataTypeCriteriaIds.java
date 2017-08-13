package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaIds extends HAPDataTypeCriteriaImp{

	@HAPAttribute
	public static String IDSCRITERIA = "idsCriteria";

	private Set<HAPDataTypeCriteriaId> m_idCriterias;
	private Set<HAPDataTypeId> m_ids;
	
	public HAPDataTypeCriteriaIds(Set<HAPDataTypeCriteriaId> eles){
		this.m_idCriterias = new HashSet<HAPDataTypeCriteriaId>();
		this.m_ids = new HashSet<HAPDataTypeId>();
		this.m_idCriterias.addAll(eles);
		for(HAPDataTypeCriteriaId criteria : eles){
			this.m_ids.add(criteria.getDataTypeId());
		}
	}
	
	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_DATATYPEIDS;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {		return this.m_ids;	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {		return this.m_idCriterias;	}

	public HAPDataTypeCriteriaOr toOrCriteria(){
		List<HAPDataTypeCriteria> criterias = new ArrayList<HAPDataTypeCriteria>();
		for(HAPDataTypeCriteriaId id : this.m_idCriterias){
			criterias.add(id);
		}
		
		HAPDataTypeCriteriaOr out = new HAPDataTypeCriteriaOr(criterias);
		return out;
	}

//	@Override
//	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
//		Set<HAPDataTypeId> normalizedIds = dataTypeHelper.normalize(m_ids);
//		return this.buildCriteriaByIds(normalizedIds);
//	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(IDSCRITERIA, HAPJsonUtility.buildJson(this.m_idCriterias, HAPSerializationFormat.JSON));
	}

	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_IDS));
		int i = 0;
		for(HAPDataTypeCriteriaId idCriteria : this.m_idCriterias){
			if(i!=0)   out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.COMMAR));
			out.append(HAPSerializeManager.getInstance().toStringValue(idCriteria, HAPSerializationFormat.LITERATE));
			i++;
		}
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_IDS));

		return out.toString(); 
	}

}
