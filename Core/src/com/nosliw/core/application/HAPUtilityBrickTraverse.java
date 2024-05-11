package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityBrickTraverse {

	public static void trasversExecutableEntityTreeUpward(HAPBrick entity, HAPProcessorEntityExecutableUpward processor, Object object) {
//		HAPPath path = new HAPPath();
//		boolean result =  processor.process(entity, null, object);
//		while(result) {
//			HAPExecutableEntity parent = entity.getParent();
//			if(parent==null) {
//				break;
//			} else {
//				result = processor.process(parent, path.appendSegment(HAPConstantShared.NAME_PARENT), processContext, object);
//			}
//		}
	}
	
	//traverse only entity leaves that marked as auto process
//	public static void traverseExecutableTreeAutoProcessed(HAPWrapperBrickRoot rootEntityInfo, HAPHandlerDownward processor, HAPManualContextProcess processContext) {
//		traverseExecutableEntity(
//				rootEntityInfo, 
//			new HAPHandlerBrickWrapper(processor) {
//				@Override
//				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
//					HAPManualAttribute attrDef = (HAPManualAttribute)HAPUtilityDefinitionBrick.getDefTreeNodeFromExeTreeNode(attr, processContext.getCurrentBundle());
//					
//					HAPUtilityDefinitionBrick.isAttributeAutoProcess(attrDef, null)
//					
//					if(attr.isAttributeAutoProcess()) {
//						return true;
//					}
//					return false;
//				}
//			}, 
//			processContext);
//	}
	
	//traverse only leaves that is local complex entity
	public static void traverseTreeLocalComplexBrick(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTreeComplexBrick(
				rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					if(attr.getValueWrapper() instanceof HAPWithBrick) {
						return true;
					}
					return false;
				}
			}, 
			brickMan,
			data);
	}
	
	
	//traverse only leafs that is complex entity
	public static void traverseTreeComplexBrick(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(
			rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPWrapperValue attrValueInfo = attr.getValueWrapper();
					if(attrValueInfo instanceof HAPWithBrick) {
						return HAPUtilityBrick.isBrickComplex(((HAPWithBrick)attrValueInfo).getBrick().getBrickType(), brickMan);
					}
					return false;
				}
			}, 
			brickMan,
			data);
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseTree(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseExecutableTree(
			rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					if(attr.getValueWrapper() instanceof HAPWithBrick) {
						return true;
					}
					return false;
				}
			}, 
			brickMan,
			data);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseExecutableTree(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseExecutableTreeLeaves(rootBrickWrapper, null, processor, brickMan, data);
	}
	
	private static void traverseExecutableTreeLeaves(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		if(path==null) {
			path = new HAPPath();
		}
		
		if(processor.processBrickNode(rootBrickWrapper, path, data)) {
			HAPBrick leafBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
			
			if(leafBrick!=null) {
				List<HAPAttributeInBrick> attrsExe = leafBrick.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traverseExecutableTreeLeaves(rootBrickWrapper, attrPath, processor, brickMan, data);
				}
			}
		}
		
		processor.postProcessBrickNode(rootBrickWrapper, path, data);
	}
}

abstract class HAPHandlerBrickWrapper extends HAPHandlerDownward{

	private HAPHandlerDownward m_processor;
	
	public HAPHandlerBrickWrapper(HAPHandlerDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPAttributeInBrick attr);
	
	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}
