package com.nosliw.data.core.domain.entity.test.simple.testsimple1;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.script.HAPDefinitionEntityScript;

public class HAPProcessorTestSimple1 {

	public static HAPExecutableTestSimple1 process(HAPIdEntityInDomain entityDefId, HAPContextProcessor processContext) {
		HAPDomainEntityDefinitionGlobal globalDomain = processContext.getCurrentDefinitionDomain();
		
		HAPDefinitionEntityTestSimple1 testSimple1Def = (HAPDefinitionEntityTestSimple1)globalDomain.getEntityInfoDefinition(entityDefId).getEntity();
		
		HAPIdEntityInDomain scriptEntityId = testSimple1Def.getNormalAttributeWithId(HAPDefinitionEntityTestSimple1.ATTR_SCRIPT).getValue().getEntityIdRef();
		HAPDefinitionEntityScript scriptDef = (HAPDefinitionEntityScript)globalDomain.getEntityInfoDefinition(scriptEntityId).getEntity();
		
		HAPExecutableTestSimple1 out = new HAPExecutableTestSimple1(scriptDef.getScript(), testSimple1Def.getParms());
		return out;
	}
}
