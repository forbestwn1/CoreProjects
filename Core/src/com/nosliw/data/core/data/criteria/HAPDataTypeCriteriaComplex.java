package com.nosliw.data.core.data.criteria;

import java.util.List;

public abstract class HAPDataTypeCriteriaComplex extends HAPDataTypeCriteriaImp{

	public HAPDataTypeCriteriaComplex(List<HAPDataTypeCriteria> eles) {
		this.addChildren(eles);
	}
}
