package com.nosliw.core.application.common.valueport;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public class HAPValuePortNew {

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	List<String> getValueStructureIds();

	HAPInfoValuePort m_valuePortInfo;
	
}
