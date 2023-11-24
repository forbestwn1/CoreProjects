package com.nosliw.ui.entity.uicontent;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableUpward;

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
			
			String handlerName = event.getHandlerName();
			
		}

		//event in custom tag
		for(HAPElementEvent event : uiContentDef.getCustomTagEvent()) {
			uiContentExe.addCustomTagEvent(event);;
		}
		
	}
	
	private void locateTask(String taskName, HAPExecutableEntityComplexUIContent uiContentExe, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.trasversExecutableEntityTreeUpward(uiContentExe, new HAPProcessorEntityExecutableUpward() {

			@Override
			public boolean process(HAPExecutableEntity entity, HAPPath path, HAPContextProcessor processContext) {
				if(entity.getEntityType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT)){
					HAPExecutableEntityComplexUIContent uiContentEntity = (HAPExecutableEntityComplexUIContent)entity;
					uiContentEntity.getS
				}
				else if(entity.getEntityType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE)){
					return true;
				}
				return false;
			}
			
		}, processContext);
	}
	

}
