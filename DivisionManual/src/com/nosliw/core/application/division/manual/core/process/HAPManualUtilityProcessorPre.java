package com.nosliw.core.application.division.manual.core.process;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityAttachment;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.runtime.HAPRuntimeManager;

public class HAPManualUtilityProcessorPre {

	public static void process(HAPManualDefinitionWrapperBrickRoot rootBrickDefWrapper, HAPRuntimeManager runtimeMan, HAPParserDataExpression dataExpressionParser, HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionBrick brickDef = rootBrickDefWrapper.getBrick();

		//build path in brick definition from root
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(rootBrickDefWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrickDefinition, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrickDefinition, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, rootBrickDefWrapper);

		
		//normalize division infor in referred resource id
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(rootBrickDefWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				if(path==null||path.isEmpty()) {
					return true;
				}
				
				HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrick, path);
				HAPManualDefinitionWrapperValue valueWrapper = attr.getValueWrapper();
				if(valueWrapper.getValueType().equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)){
					HAPManualDefinitionWrapperValueReferenceResource resourceIdValueWrapper = (HAPManualDefinitionWrapperValueReferenceResource)valueWrapper;
					resourceIdValueWrapper.setResourceId(processContext.getBrickManager().normalizeResourceIdWithDivision(resourceIdValueWrapper.getResourceId(), HAPConstantShared.BRICK_DIVISION_MANUAL));
				}
				return true;
			}
			
		}, null);
		
		//build attachment
		HAPManualDefinitionUtilityAttachment.processAttachment(brickDef, null, processContext);

		//process constant
		HAPManualUtilityScriptExpressionConstant.discoverScriptExpressionConstantInBrick(brickDef, processContext.getManualBrickManager());
		Map<String, Map<String, Object>> scriptExpressionResults = HAPManualUtilityScriptExpressionConstant.calculateScriptExpressionConstants(brickDef, processContext.getManualBrickManager(), runtimeMan, dataExpressionParser);
		HAPManualUtilityScriptExpressionConstant.solidateScriptExpressionConstantInBrick(brickDef, scriptExpressionResults, processContext.getManualBrickManager());
		
		//process rule
		
		
	}
	
}
