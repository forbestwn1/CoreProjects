package com.nosliw.core.xxx.application1;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.valueport.HAPInfoValueStructure;

@HAPEntityWithAttribute
public interface HAPValueContext extends HAPSerializable{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	List<String> getValueStructureIds();

	List<HAPInfoValueStructure> getValueStructuresSorted();
	
}
