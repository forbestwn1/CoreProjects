package com.nosliw.core.data.criteria;

import java.util.List;

public abstract class HAPDataTypeCriteriaComplex extends HAPDataTypeCriteriaImp{

	public HAPDataTypeCriteriaComplex(List<HAPDataTypeCriteria> eles) {
		this.addChildren(eles);
	}
}
