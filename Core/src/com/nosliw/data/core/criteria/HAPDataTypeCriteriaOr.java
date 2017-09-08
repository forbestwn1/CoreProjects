package com.nosliw.data.core.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPDataTypeCriteriaOr extends HAPDataTypeCriteriaComplex{

	public HAPDataTypeCriteriaOr(List<HAPDataTypeCriteria> eles){
		super(eles);
	}

	@Override
	public String getType() {		return HAPConstant.DATATYPECRITERIA_TYPE_OR;	}

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
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.START_OR));
		int i = 0;
		for(HAPDataTypeCriteria childCriteria : this.getChildren()){
			if(i!=0)   out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.COMMAR));
			out.append(HAPSerializeManager.getInstance().toStringValue(childCriteria, HAPSerializationFormat.LITERATE));
			i++;
		}
		out.append(HAPCriteriaParser.getInstance().getToken(HAPCriteriaParser.END_OR));
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
