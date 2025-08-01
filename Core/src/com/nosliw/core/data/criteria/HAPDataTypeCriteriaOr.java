package com.nosliw.core.data.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.HAPDataTypeId;

public class HAPDataTypeCriteriaOr extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> eles){
		super(eles);
	}

	@Override
	public String getType() {		return HAPConstantShared.DATATYPECRITERIA_TYPE_OR;	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		Set<HAPDataTypeId> out = new HashSet<HAPDataTypeId>();
		for(HAPDataTypeCriteria ele : this.getChildren()){
			out.addAll(ele.getValidDataTypeId(dataTypeHelper));
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
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.START_OR));
		int i = 0;
		for(HAPDataTypeCriteria childCriteria : this.getChildren()){
			if(i!=0)   out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.COMMAR));
			out.append(HAPManagerSerialize.getInstance().toStringValue(childCriteria, HAPSerializationFormat.LITERATE));
			i++;
		}
		out.append(HAPParserCriteria.getInstance().getToken(HAPParserCriteria.END_OR));
		return out.toString(); 
	}

	@Override
	public Set<HAPDataTypeCriteriaId> getValidDataTypeCriteriaId(HAPDataTypeHelper dataTypeHelper) {
		List<HAPDataTypeCriteria> elements = this.getChildren();
		Set<HAPDataTypeCriteriaId> out = new HashSet<HAPDataTypeCriteriaId>();
		for(int i=0; i<elements.size(); i++){
			out.addAll(elements.get(i).getValidDataTypeCriteriaId(dataTypeHelper));
		}
		return out;
	}
}
