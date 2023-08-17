package com.nosliw.data.core.domain.entity.uipage;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPDefinitionEntityComplexUITag extends HAPDefinitionEntityComplexWithUIUnit{

	@HAPAttribute
	public static final String TYPE = "type";
	@HAPAttribute
	public static final String BASE = "base";
	@HAPAttribute
	public static final String SCRIPT = "script";
	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";
	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";
	@HAPAttribute
	public static final String VALUESTRUCTUREEXE = "valueStructureExe";
	@HAPAttribute
	public static final String REQUIRES = "requires";
	@HAPAttribute
	public static final String EVENT = "event";
	

	
	public void setTagName(String tagName) {}

	public void setUIId(String uiId) {}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
