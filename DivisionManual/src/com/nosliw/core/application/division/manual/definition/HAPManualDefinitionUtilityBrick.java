package com.nosliw.core.application.division.manual.definition;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;

public class HAPManualDefinitionUtilityBrick {

	public static HAPIdBrickType getBrickType(HAPManualDefinitionWrapperValue attrValueWrapper) {
		HAPIdBrickType out = null;
		String attrValueWrapperType = attrValueWrapper.getValueType();
		if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
			HAPManualDefinitionWrapperValueBrick brickWrapper = (HAPManualDefinitionWrapperValueBrick)attrValueWrapper;
			out = brickWrapper.getBrickTypeId();
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
			HAPManualDefinitionWrapperValueReferenceResource resourceWrapper = (HAPManualDefinitionWrapperValueReferenceResource)attrValueWrapper;
			out = HAPUtilityBrickId.getBrickTypeIdFromResourceTypeId(resourceWrapper.getResourceId().getResourceTypeId());
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICKREFERENCE)) {
			HAPManualDefinitionWrapperValueReferenceBrick brickRefWrapper = (HAPManualDefinitionWrapperValueReferenceBrick)attrValueWrapper;
			out = brickRefWrapper.getBrickTypeId();
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE)) {
			HAPManualDefinitionWrapperValueReferenceAttachment attachmentRefWrapper = (HAPManualDefinitionWrapperValueReferenceAttachment)attrValueWrapper;
			out = attachmentRefWrapper.getBrickTypeId();
		}
		
		return out;
	}
	
	public static Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPPath path, HAPBundle bundle){
		HAPManualDefinitionWrapperBrick rootEntityDefInfo = (HAPManualDefinitionWrapperBrick)bundle.getExtraData();
		HAPManualDefinitionBrick entityDef = getDescdentBrickDefinition(rootEntityDefInfo, path);
		HAPManualBrick entityExe = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle.getBrickWrapper(), path);
		return Pair.of(entityDef, entityExe);
	}
	
	public static HAPTreeNode getDescdentTreeNode(HAPManualDefinitionWrapperBrick rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getBrick(), path);
		}
		return out;
	}
	
	public static HAPManualDefinitionBrick getDescdentBrickDefinition(HAPManualDefinitionWrapperBrick rootEntityInfo, HAPPath path) {
		return getDescendantBrick(rootEntityInfo.getBrick(), path);
	}
	
	public static HAPManualDefinitionAttributeInBrick getDescendantAttribute(HAPManualDefinitionBrick brickManual, HAPPath path) {
		HAPManualDefinitionAttributeInBrick out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = brickManual.getAttribute(attribute);
			} else {
				HAPManualDefinitionWrapperValue attrValueInfo = out.getValueWrapper();
				if(attrValueInfo instanceof HAPManualWithBrick) {
					out = ((HAPManualWithBrick)attrValueInfo).getBrick().getAttribute(attribute);
				}
				else if(attrValueInfo.getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	private static HAPManualDefinitionBrick getDescendantBrick(HAPManualDefinitionBrick entityDef, HAPPath path) {
		HAPManualDefinitionBrick out = null;
		if(path==null||path.isEmpty()) {
			out = entityDef;
		} else {
			HAPManualDefinitionWrapperValue attrValueInfo = getDescendantAttribute(entityDef, path).getValueWrapper();
			if(attrValueInfo instanceof HAPManualWithBrick) {
				out = ((HAPManualWithBrick)attrValueInfo).getBrick();
			}
		}
		return out;
	}

	
	public static HAPTreeNode getDefTreeNodeFromExeTreeNode(HAPTreeNodeBrick treeNodeExe, HAPBundle bundle) {
		HAPManualDefinitionWrapperBrick rootEntityDefInfo = (HAPManualDefinitionWrapperBrick)bundle.getExtraData();
		return HAPManualDefinitionUtilityBrick.getDescdentTreeNode(rootEntityDefInfo, treeNodeExe.getTreeNodeInfo().getPathFromRoot());
	}

	public static HAPManualDefinitionBrick getEntityDefinitionFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualDefinitionWrapperBrick rootEntityDefInfo = (HAPManualDefinitionWrapperBrick)bundle.getExtraData();
		return HAPManualDefinitionUtilityBrick.getDescdentBrickDefinition(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}

	public static boolean isAdapterAutoProcess(HAPManualDefinitionAttributeInBrick attr, HAPManagerApplicationBrick entityMan) {
		return true;
	}

	public static HAPManualDefinitionBrickRelation getEntityRelation(HAPManualDefinitionAttributeInBrick attr, String relationType) {
		for(HAPManualDefinitionBrickRelation relation : attr.getRelations()) {
			if(relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		if(brickTypeId.getBrickType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			return false;
		} else {
			return HAPManualUtilityBrick.isBrickComplex(brickTypeId, manualBrickMan);
		}
	}
	
}
