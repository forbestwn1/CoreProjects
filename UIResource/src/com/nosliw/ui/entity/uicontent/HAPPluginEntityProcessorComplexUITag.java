package com.nosliw.ui.entity.uicontent;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableEntityExpressionScriptGroup;

public class HAPPluginEntityProcessorComplexUITag extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexUITag() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPExecutableEntityComplexUITag.class);
	}

	@Override
	public void extendConstantValue(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> entityPair = this.getEntityPair(complexEntityExecutableId, processContext);
		HAPDefinitionEntityComplexUITag uiTagDef = (HAPDefinitionEntityComplexUITag)entityPair.getLeft();
		HAPExecutableEntityComplexUITag uiTagExe = (HAPExecutableEntityComplexUITag)entityPair.getRight();
		
		HAPExecutableEntityExpressionScriptGroup plainScriptExpressionGroupEntity = uiTagExe.getPlainScriptExpressionGroupEntity(processContext);
		Map<String, String> attrs = uiTagDef.getTagAttributes();
		for(String attrName : attrs.keySet()) {
			plainScriptExpressionGroupEntity.addValueConstant(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE+attrName, attrs.get(attrName)); 
		}
	}
	
	@Override
	public void processEntity(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {	
		Pair<HAPDefinitionEntityInDomainComplex,HAPExecutableEntityComplex> entityPair = this.getEntityPair(complexEntityExecutableId, processContext);
		HAPDefinitionEntityComplexUITag uiTagDef = (HAPDefinitionEntityComplexUITag)entityPair.getLeft();
		HAPExecutableEntityComplexUITag uiTagExe = (HAPExecutableEntityComplexUITag)entityPair.getRight();
	
		uiTagExe.setBaseName(uiTagDef.getBaseName());
		
		uiTagExe.setUIId(uiTagDef.getUIId());
		
		uiTagExe.setTagId(uiTagDef.getTagId());
		
		Map<String, String> attrs = uiTagDef.getTagAttributes();
		for(String attrName : attrs.keySet()) {
			uiTagExe.addTagAttribute(attrName, attrs.get(attrName));
			uiTagExe.addValueConstant(attrName, attrs.get(attrName));
		}
	}
	
}
