package com.nosliw.core.application.division.manual.definition;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPManualDefinitionUtilityBrickTraverse {

	
	public static void traverseEntityTreeLeaves(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, Object data) {
		traverseEntityTreeLeaves(rootBrickWrapper, null, processor, data);
	}
	
	private static void traverseEntityTreeLeaves(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}

		if(processor.processBrickNode(rootBrickWrapper, path, data)) {
			HAPManualDefinitionBrick leafBrick = HAPManualDefinitionUtilityBrick.getDescdentBrickDefinition(rootBrickWrapper, path);
			
			if(leafBrick!=null) {
				//if related value is not brick, then stop processing children attribute
				List<HAPManualDefinitionAttributeInBrick> attrs = leafBrick.getAllAttributes();
				for(HAPManualDefinitionAttributeInBrick attr : attrs) {
					HAPPath attrPath = path.appendSegment(attr.getName());
					traverseEntityTreeLeaves(rootBrickWrapper, attrPath, processor, data);
				}
			}
		}
		processor.postProcessBrickNode(rootBrickWrapper, path, data);
	}
}

abstract class HAPProcessorEntityWrapper extends HAPManualDefinitionProcessorBrickNodeDownwardWithPath{

	private HAPManualDefinitionProcessorBrickNodeDownwardWithPath m_processor;
	
	public HAPProcessorEntityWrapper(HAPManualDefinitionProcessorBrickNodeDownwardWithPath processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPManualDefinitionAttributeInBrick attr);
	
	@Override
	public boolean processBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}
