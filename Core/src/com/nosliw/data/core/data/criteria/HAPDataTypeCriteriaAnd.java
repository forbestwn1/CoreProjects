package com.nosliw.data.core.data.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;

public class HAPDataTypeCriteriaAnd extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaAnd(List<HAPDataTypeCriteria> ele){
		super(ele);
	}

	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_AND;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		int i = 0;
		for(HAPDataTypeCriteria ele : this.getChildren()){
			if(i==0){
				out = ele.getValidDataTypeId(dataTypeHelper);
			}
			else{
				out = Sets.intersection(out, ele.getValidDataTypeId(dataTypeHelper));
			}
		}
		return out;
	}

//	@Override
//	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
//		Set<HAPDataTypeId> ids = this.getValidDataTypeId(dataTypeHelper);
//		Set<HAPDataTypeId> norIds = dataTypeHelper.normalize(ids);
//		return this.buildCriteriaByIds(norIds);
//	}
	
	@Override
	protected String buildLiterate(){
		StringBuffer out = new StringBuffer();
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.START_AND));
		int i = 0;
		for(HAPDataTypeCriteria childCriteria : this.getChildren()){
			if(i!=0)   out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.COMMAR));
			out.append(HAPSerializeManager.getInstance().toStringValue(childCriteria, HAPSerializationFormat.LITERATE));
			i++;
		}
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.END_AND));
		return out.toString(); 
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		List<HAPDataTypeCriteria> elements = this.getChildren();
		HAPDataTypeCriteria out = null;
		for(int i=0; i<elements.size(); i++){
			if(out==null){
				out = elements.get(i);
			}
			else{
				out = dataTypeHelper.and(out, elements.get(i));
			}
		}
		return out.getValidDataTypeCriteriaId(dataTypeHelper);
	}

}
