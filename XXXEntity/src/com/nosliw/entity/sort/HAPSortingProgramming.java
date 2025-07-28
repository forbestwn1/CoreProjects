package com.nosliw.entity.sort;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;

public class HAPSortingProgramming extends HAPSortingInfo{

	private HAPComparable m_comparable;
	
	public HAPSortingProgramming(HAPComparable comparable, HAPDataTypeManager dataTypeMan) {
		super(HAPConstant.SORTING_TYPE_PROGRAMMING, dataTypeMan);
		this.m_comparable = comparable;
	}

	@Override
	public int compare(HAPWraper data1, HAPWraper data2) {
		return this.m_comparable.compare(data1, data2);
	}

}
