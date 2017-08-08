package com.nosliw.data.core.criteria;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

/**
 * The criteria that is the result of a expression
 * It is used when define operation output criteria, when operation output criteria depends on operation input 
 *
 */
public class HAPDataTypeCriteriaExpression implements HAPDataTypeCriteria{

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<HAPDataTypeId> getValidDataTypeId(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeCriteria normalize(HAPDataTypeHelper dataTypeHelper) {
		// TODO Auto-generated method stub
		return null;
	}

}
