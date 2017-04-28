package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPJsonUtility;

public abstract class HAPDataTypeCriteriaComplex extends HAPDataTypeCriteriaImp{

	@HAPAttribute
	public static String ELEMENTS = "elements";

	private List<HAPDataTypeCriteria> m_eles;
	
	public HAPDataTypeCriteriaComplex(List<HAPDataTypeCriteria> eles) {
		this.m_eles = new ArrayList<HAPDataTypeCriteria>();
		this.m_eles.addAll(eles);
	}

	public List<HAPDataTypeCriteria> getElements(){  return this.m_eles;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENTS, HAPJsonUtility.buildJson(m_eles, HAPSerializationFormat.JSON));
	}

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaComplex){
			HAPDataTypeCriteriaComplex criteria = (HAPDataTypeCriteriaComplex)obj;
			if(criteria.getType().equals(this.getType())){
				if(this.m_eles.size()==criteria.m_eles.size()){
					out = true;
					for(int i=0; i<this.m_eles.size(); i++){
						if(!HAPBasicUtility.isEquals(this.m_eles.get(i), criteria.m_eles.get(i))){
							return false;
						}
					}
				}
			}
		}
		return out;
	}
}
