package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPValueContext extends HAPSerializable{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	List<String> getValueStructureIds();
	
}
