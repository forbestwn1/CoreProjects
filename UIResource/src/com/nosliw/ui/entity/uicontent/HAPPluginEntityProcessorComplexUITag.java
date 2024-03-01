package com.nosliw.ui.entity.uicontent;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPPluginEntityProcessorComplexImp;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;

public class HAPPluginEntityProcessorComplexUITag extends HAPPluginEntityProcessorComplexImp{

	public HAPPluginEntityProcessorComplexUITag() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPExecutableEntityComplexUITag.class);
	}

	@Override
	public void extendConstantValue(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPDefinitionEntityComplexUITag uiTagDef = (HAPDefinitionEntityComplexUITag)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityComplexUITag uiTagExe = (HAPExecutableEntityComplexUITag)complexEntityExecutable;
		
		HAPExecutableEntityExpressionScriptGroup plainScriptExpressionGroupEntity = uiTagExe.getPlainScriptExpressionGroupEntity(processContext);
		Map<String, String> attrs = uiTagDef.getTagAttributes();
		for(String attrName : attrs.keySet()) {
			plainScriptExpressionGroupEntity.addValueConstant(HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE+attrName, attrs.get(attrName)); 
		}
	}
	
	@Override
	public void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {	
		HAPDefinitionEntityComplexUITag uiTagDef = (HAPDefinitionEntityComplexUITag)this.getEntityDefinition(complexEntityExecutable, processContext);
		HAPExecutableEntityComplexUITag uiTagExe = (HAPExecutableEntityComplexUITag)complexEntityExecutable;
	
		//build name to variable id mapping
		HAPDomainValueStructure valueStructureDomain = processContext.getCurrentValueStructureDomain();
		Set<String> valueStructureIds = HAPUtilityValueContext.getSelfValueStructures(uiTagExe.getValueContext());
		for(String valueStructureId : valueStructureIds) {
			HAPDefinitionEntityValueStructure valueStructureDef = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureId);
			for(String rootName : valueStructureDef.getRootNames()) {
				uiTagExe.addVariableByName(rootName, new HAPIdVariable(new HAPIdRootElement(null, valueStructureId, rootName), null));
			}
		}
		
		uiTagExe.setScriptResourceId(uiTagDef.getScriptResourceId());
		
		uiTagExe.setAttributeDefinition(uiTagDef.getAttributeDefinition());
		
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
