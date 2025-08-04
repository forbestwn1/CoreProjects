package com.nosliw.core.application.division.manual.core.definition;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.a.HAPManualUtilityBrick;

public class HAPManualDefinitionUtilityBrickTraverse {

	
	public static void traverseBrickTreeLeaves(HAPManualDefinitionWrapperBrickRoot rootBrickWrapper, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, Object data) {
		traverseBrickTreeLeaves(rootBrickWrapper.getBrick(), null, processor, data);
	}

	public static void traverseBrickTreeLeavesOfBrickComplex(HAPManualDefinitionBrick rootBrick, HAPPath path, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, HAPManualManagerBrick manualBrickMan, Object data) {
		traverseBrickTreeLeavesOfBrick(rootBrick, path, new HAPProcessorBrickWrapper(processor) {
			@Override
			protected boolean isValidAttribute(HAPManualDefinitionAttributeInBrick attr) {
				return HAPManualUtilityBrick.isBrickComplex(((HAPManualDefinitionWrapperValueBrick)attr.getValueWrapper()).getBrickTypeId(), manualBrickMan);
			}

			@Override
			protected boolean ifContinueWithInvalideAttribute(HAPManualDefinitionAttributeInBrick attr) {	return true;	}
		}, data);
	}

	public static void traverseBrickTreeLeavesOfBrick(HAPManualDefinitionBrick rootBrick, HAPPath path, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, Object data) {
		traverseBrickTreeLeaves(rootBrick, path, new HAPProcessorBrickWrapper(processor) {
			@Override
			protected boolean isValidAttribute(HAPManualDefinitionAttributeInBrick attr) {
				return HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK.equals(attr.getValueWrapper().getValueType());
			}
		}, data);
	}
	
	public static void traverseBrickTreeLeaves(HAPManualDefinitionBrick rootBrick, HAPPath path, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}

		if(processor.processBrickNode(rootBrick, path, data)) {
			HAPManualDefinitionBrick leafBrick = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path);
			
			if(leafBrick!=null) {
				//if related value is not brick, then stop processing children attribute
				List<HAPManualDefinitionAttributeInBrick> attrs = leafBrick.getAllAttributes();
				for(HAPManualDefinitionAttributeInBrick attr : attrs) {
					HAPPath attrPath = path.appendSegment(attr.getName());
					traverseBrickTreeLeaves(rootBrick, attrPath, processor, data);
				}
			}
		}
		processor.postProcessBrickNode(rootBrick, path, data);
	}
}

abstract class HAPProcessorBrickWrapper extends HAPManualDefinitionProcessorBrickNodeDownwardWithPath{

	private HAPManualDefinitionProcessorBrickNodeDownwardWithPath m_processor;
	
	public HAPProcessorBrickWrapper(HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPManualDefinitionAttributeInBrick attr);
	
	protected boolean ifContinueWithInvalideAttribute(HAPManualDefinitionAttributeInBrick attr) {	return false;	}
	
	@Override
	public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrick, path, data);
		}
		else {
			HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrick, path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrick, path, data);
			}
			return ifContinueWithInvalideAttribute(attr);
		}
	}

	@Override
	public void postProcessBrickNode(HAPManualDefinitionBrick rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrickWrapper, path); 
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}
