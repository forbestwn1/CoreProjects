package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;

public class HAPPluginEntityProcessorComplexUITag extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexUITag() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPExecutableEntityComplexUIContent.class);
	}

	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {	
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> entityPair = this.getEntityPair(complexEntityExecutableId, processContext);
		HAPDefinitionEntityComplexUITag uiTagDef = (HAPDefinitionEntityComplexUITag)entityPair.getLeft();
		HAPExecutableEntityComplexUITag uiTagExe = (HAPExecutableEntityComplexUITag)entityPair.getRight();
		
		uiTagExe.setUIId(uiTagDef.getUIId());
		
		uiTagExe.setTagName(uiTagDef.getTagName());
		
		Map<String, String> attrs = uiTagDef.getTagAttributes();
		for(String attrName : attrs.keySet()) {
			uiTagExe.addTagAttribute(attrName, attrs.get(attrName));
		}
	}
	
}
