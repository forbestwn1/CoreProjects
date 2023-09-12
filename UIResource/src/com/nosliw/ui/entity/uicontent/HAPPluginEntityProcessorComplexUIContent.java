package com.nosliw.ui.entity.uicontent;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexUIContent extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexUIContent() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, HAPExecutableEntityComplexUIContent.class);
	}

	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {	
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> entityPair = this.getEntityPair(complexEntityExecutableId, processContext);
		HAPDefinitionEntityComplexUIContent uiContentDef = (HAPDefinitionEntityComplexUIContent)entityPair.getLeft();
		HAPExecutableEntityComplexUIContent uiContentExe = (HAPExecutableEntityComplexUIContent)entityPair.getRight();
		uiContentExe.setContent(uiContentDef.getContent());
	}

}
