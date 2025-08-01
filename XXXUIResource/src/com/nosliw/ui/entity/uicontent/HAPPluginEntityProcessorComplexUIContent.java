package com.nosliw.ui.entity.uicontent;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.xxx.application1.division.manual.HAPPluginProcessorBrickDefinitionComplexImp;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableUpward;
import com.nosliw.data.core.domain.entity.container.HAPExecutableEntityContainerComplex;
import com.nosliw.data.core.domain.entity.script.task.HAPExecutableEntityScriptTaskGroup;
import com.nosliw.data.core.domain.entity.task.HAPInfoTask;

public class HAPPluginEntityProcessorComplexUIContent extends HAPPluginProcessorBrickDefinitionComplexImp{

	public HAPPluginEntityProcessorComplexUIContent() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, HAPExecutableEntityComplexUIContent.class);
	}

	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {	
		HAPDefinitionEntityComplexUIContent uiContentDef = (HAPDefinitionEntityComplexUIContent)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityComplexUIContent uiContentExe = (HAPExecutableEntityComplexUIContent)complexEntityExecutable;
		
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

	}
	
	@Override
	public void postProcessEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPDefinitionEntityComplexUIContent uiContentDef = (HAPDefinitionEntityComplexUIContent)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityComplexUIContent uiContentExe = (HAPExecutableEntityComplexUIContent)complexEntityExecutable;
		
		//event in normal tag
		for(HAPElementEvent event : uiContentDef.getNormalTagEvents()) {
			uiContentExe.addNormalTagEvent(event);
			event.setTaskInfo(this.locateTask(event.getHandlerName(), uiContentExe, processContext));
		}

		//event in custom tag
		for(HAPElementEvent event : uiContentDef.getCustomTagEvent()) {
			uiContentExe.addCustomTagEvent(event);;
		}
	}

	private HAPInfoTask locateTask(String taskName, HAPExecutableEntityComplexUIContent uiContentExe, HAPContextProcessor processContext) {
		HAPInfoTask out = null;
		List<HAPInfoTask> results = new ArrayList<HAPInfoTask>();
		HAPUtilityEntityExecutable.trasversExecutableEntityTreeUpward(uiContentExe, new HAPProcessorEntityExecutableUpward() {
			@Override
			public boolean process(HAPExecutableEntity entity, HAPPath path, HAPContextProcessor processContext, Object obj) {
				if(entity.getEntityType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT)){
					HAPExecutableEntityComplexUIContent uiContentEntity = (HAPExecutableEntityComplexUIContent)entity;
					HAPExecutableEntityContainerComplex containerEntity = (HAPExecutableEntityContainerComplex)uiContentEntity.getAttributeValueEntity(HAPExecutableEntityComplexUIContent.SCRIPT);
					for(HAPAttributeEntityExecutable attrExe: containerEntity.getNormalAttributes()) {
						HAPExecutableEntityScriptTaskGroup scriptTaskGroup = (HAPExecutableEntityScriptTaskGroup)attrExe.getValue().getValue();
						if(scriptTaskGroup.getDefinitionByName(taskName)!=null) {
							List<HAPInfoTask> results = (List<HAPInfoTask>)obj;
							results.add(new HAPInfoTask(taskName, scriptTaskGroup.getPathFromRoot(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP));
							return false;
						}
					}
				}
				else if(entity.getEntityType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE)){
					return true;
				}
				return false;
			}
		}, processContext, results);
		if(results.size()>0)  out = results.get(0);
		return out;
	}
}
