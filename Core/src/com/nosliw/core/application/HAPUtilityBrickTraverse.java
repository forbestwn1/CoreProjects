package com.nosliw.core.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityBrickTraverse {

	public static void traverseTreeWithLocalBrick(HAPBundle bundle, String rootName, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(
			bundle, 
			rootName,
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
	
	public static void traverseTree(HAPBundle bundle, String rootName, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		Set<HAPPath> validPath = new HashSet<HAPPath>();
		
		traversePreTree(bundle, new HAPPath(HAPUtilityBundle.buildBranchPathSegment(rootName)), processor, brickMan, data, validPath);
		
		traversePostTree(bundle, new HAPPath(HAPUtilityBundle.buildBranchPathSegment(rootName)), processor, brickMan, data, validPath);
	}
	
	private static void traversePreTree(HAPBundle bundle, HAPPath path, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data, Set<HAPPath> validPath) {
		validPath.add(path);
		if(processor.processBrickNode(bundle, path, data)) {
			HAPBrick leafBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
			
			if(leafBrick!=null) {
				//only process child for brick
				List<HAPAttributeInBrick> attrsExe = leafBrick.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traversePreTree(bundle, attrPath, processor, brickMan, data, validPath);
				}
			}
		}
	}

	private static void traversePostTree(HAPBundle bundle, HAPPath path, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data, Set<HAPPath> validPath) {
		if(validPath.contains(path)) {
			processor.postProcessBrickNode(bundle, path, data);
			HAPBrick leafBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
			
			if(leafBrick!=null) {
				//only process child for brick
				List<HAPAttributeInBrick> attrsExe = leafBrick.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traversePostTree(bundle, attrPath, processor, brickMan, data, validPath);
				}
			}
		}
	}

}


//public static void trasversExecutableEntityTreeUpward(HAPBrick entity, HAPProcessorEntityExecutableUpward processor, Object object) {
//HAPPath path = new HAPPath();
//boolean result =  processor.process(entity, null, object);
//while(result) {
//	HAPExecutableEntity parent = entity.getParent();
//	if(parent==null) {
//		break;
//	} else {
//		result = processor.process(parent, path.appendSegment(HAPConstantShared.NAME_PARENT), processContext, object);
//	}
//}
//}

//traverse only entity leaves that marked as auto process
//public static void traverseExecutableTreeAutoProcessed(HAPWrapperBrickRoot rootEntityInfo, HAPHandlerDownward processor, HAPManualContextProcess processContext) {
//traverseExecutableEntity(
//		rootEntityInfo, 
//	new HAPHandlerBrickWrapper(processor) {
//		@Override
//		protected boolean isValidAttribute(HAPAttributeInBrick attr) {
//			HAPManualAttribute attrDef = (HAPManualAttribute)HAPUtilityDefinitionBrick.getDefTreeNodeFromExeTreeNode(attr, processContext.getCurrentBundle());
//			
//			HAPUtilityDefinitionBrick.isAttributeAutoProcess(attrDef, null)
//			
//			if(attr.isAttributeAutoProcess()) {
//				return true;
//			}
//			return false;
//		}
//	}, 
//	processContext);
//}

