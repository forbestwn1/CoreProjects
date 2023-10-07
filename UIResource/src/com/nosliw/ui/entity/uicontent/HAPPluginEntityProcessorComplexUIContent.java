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
		
		//html
		uiContentExe.setHTML(uiContentDef.getHtml());

		//script block
		uiContentExe.setScriptBlock(uiContentDef.getScriptBlock());
		
		//script expression in content
		for(HAPUIEmbededScriptExpressionInContent uiEmbed : uiContentDef.getScriptExpressionInContents()) {
			uiContentExe.addScriptExpressionInContent(uiEmbed);
		}
		
		//script expression in normal tag
		for(HAPUIEmbededScriptExpressionInAttribute uiEmbed : uiContentDef.getScriptExpressionInNormalTagAttributes()) {
			uiContentExe.addScriptExpressionInNormalTagAttribute(uiEmbed);
		}

		//script expression in custom tag
		for(HAPUIEmbededScriptExpressionInAttribute uiEmbed : uiContentDef.getScriptExpressionInCustomTagAttributes()) {
			uiContentExe.addScriptExpressionInCustomTagAttribute(uiEmbed);
		}

		//event in normal tag
		for(HAPElementEvent event : uiContentDef.getNormalTagEvents()) {
			uiContentExe.addNormalTagEvent(event);
		}

		//event in custom tag
		for(HAPElementEvent event : uiContentDef.getCustomTagEvent()) {
			uiContentExe.addCustomTagEvent(event);;
		}
		
		
	}

}
