package com.nosliw.core.application.division.manual.core.process;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.a.HAPManualUtilityProcessor;
import com.nosliw.core.application.division.manual.core.a.HAPManualUtilityValueContextProcessor;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityAttachment;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValueReferenceResource;

public class HAPManualProcessBrick {

	public static HAPWrapperBrickRoot processRootBrick(HAPManualDefinitionWrapperBrickRoot brickDefWrapper, HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickManager) {
		HAPManualDefinitionBrick brickDef = brickDefWrapper.getBrick();
		
		//build parent and 
		
		
		//build path from root
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(brickDefWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, brickDefWrapper);
		

		//normalize division infor in referred resource id
		normalizeDivisionInReferredResource(brickDefWrapper, brickManager);
		
		//build attachment
		HAPManualDefinitionUtilityAttachment.processAttachment(brickDef, null, processContext);

		//process constant
		HAPManualUtilityScriptExpressionConstant.discoverScriptExpressionConstantInBrick(brickDef, this);
		Map<String, Map<String, Object>> scriptExpressionResults = HAPManualUtilityScriptExpressionConstant.calculateScriptExpressionConstants(brickDef, m_runtimeEnv, this);
		HAPManualUtilityScriptExpressionConstant.solidateScriptExpressionConstantInBrick(brickDef, scriptExpressionResults, this);
		
		//build executable tree
		HAPManualWrapperBrickRoot out = new HAPManualWrapperBrickRoot(HAPManualUtilityProcessor.buildExecutableTree(brickDef, processContext, this));
		out.setName(processContext.getRootBrickName());
		out.setDefinition(brickDefWrapper);
		processContext.getCurrentBundle().addRootBrickWrapper(out);
		
		//brick init
		HAPManualUtilityProcessor.initBricks(processContext, this, m_runtimeEnv);

		//init
		HAPManualUtilityProcessor.processComplexBrickInit(processContext);

		HAPManualUtilityProcessor.processComplexBrickNormalizeBrickPath(processContext);
		
		//complex entity, build value context domain, create extension value structure if needed
//		HAPManualUtilityValueContextProcessor.processValueContext(out.getBrickWrapper(), processContext, this, this.m_runtimeEnv);

		//build value context in complex block
		HAPManualUtilityValueContextProcessor.buildValueContext(processContext, this, this.m_runtimeEnv);
		
		//build other value port
		HAPManualUtilityProcessor.processOtherValuePortBuild(processContext);
		
		//generate extra value structure for variable extension
		HAPManualUtilityValueContextProcessor.buildExtensionValueStructure(processContext, this, this.m_runtimeEnv);
		
		//
		HAPManualUtilityValueContextProcessor.processInheritageAndRelativeElement(null, processContext);
		
		
		//variable resolve (variable extension)-----impact data container
		HAPManualUtilityProcessor.processComplexVariableResolve(processContext);
	
		//build var criteria infor in var info container according to value port def
		HAPManualUtilityProcessor.processComplexVariableInfoResolve(processContext);
		
		//variable criteria discovery ---- impact data container and value structure in context domain
		HAPManualUtilityProcessor.processComplexValueContextDiscovery(processContext);
		
		//update value port element according to var info container after discovery
//		HAPManualUtilityProcessor.processComplexValuePortUpdate(processContext);
		
		//process entity
		HAPManualUtilityProcessor.processBrick(processContext, brickManager);
		
		//process adapter
		HAPManualUtilityProcessor.processAdapterInAttribute(processContext);
		
		HAPManualUtilityProcessor.cleanupEmptyValueStructure(processContext);
		
		return out;
	}
	
	private static void normalizeDivisionInReferredResource(HAPManualDefinitionWrapperBrickRoot brickWrapper, HAPManagerApplicationBrick brickManager) {
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(brickWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				if(path==null||path.isEmpty()) {
					return true;
				}
				
				HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrick, path);
				HAPManualDefinitionWrapperValue valueWrapper = attr.getValueWrapper();
				if(valueWrapper.getValueType().equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)){
					HAPManualDefinitionWrapperValueReferenceResource resourceIdValueWrapper = (HAPManualDefinitionWrapperValueReferenceResource)valueWrapper;
					resourceIdValueWrapper.setResourceId(brickManager.normalizeResourceIdWithDivision(resourceIdValueWrapper.getResourceId(), HAPConstantShared.BRICK_DIVISION_MANUAL));
				}
				return true;
			}
			
		}, HAPConstantShared.BRICK_DIVISION_MANUAL);
	}

}
