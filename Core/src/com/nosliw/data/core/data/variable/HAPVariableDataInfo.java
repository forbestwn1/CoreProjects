package com.nosliw.data.core.data.variable;

import java.util.List;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public class HAPVariableDataInfo {

	//data type
	private HAPDataTypeCriteria m_criteria;

	//rules that apply constrain for the value
	private List<HAPDataRule> m_rules;
	
	
	private HAPData m_defaultValue;
	
}
