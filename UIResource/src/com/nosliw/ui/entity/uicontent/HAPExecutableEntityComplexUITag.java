package com.nosliw.ui.entity.uicontent;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

public class HAPExecutableEntityComplexUITag extends HAPDefinitionEntityComplexWithUIContent{

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
	
	public static final String PARENTRELATIONCONFIGURE = "parentRelationConfigure";
	
	public static final String CHILDRELATIONCONFIGURE = "childRelationConfigure";

	public void setTagName(String tagName) {}

	public void setUIId(String uiId) {}

	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelationConfigure) {}
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {}

	public void setChildRelationConfigure(HAPConfigureParentRelationComplex childRelationConfigure) {}
	public HAPConfigureParentRelationComplex getChildRelationConfigure() {}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		// TODO Auto-generated method stub
		return null;
	}

}
