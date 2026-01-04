package com.nosliw.core.application.dynamic;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDynamicDefinitionCriteriaComplex extends HAPSerializableImp implements HAPDynamicDefinitionCriteria{

	@Override
	public String getCriteriaType() {   return HAPConstantShared.DYNAMICDEFINITION_CRITERIA_COMPLEX;   }


	
}
