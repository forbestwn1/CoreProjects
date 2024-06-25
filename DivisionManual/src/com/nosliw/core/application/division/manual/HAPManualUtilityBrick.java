package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPTreeNodeBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;

public class HAPManualUtilityBrick {

	public static HAPIdBrickType getBrickType(HAPManualWrapperValue attrValueWrapper) {
		HAPIdBrickType out = null;
		String attrValueWrapperType = attrValueWrapper.getValueType();
		if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
			HAPManualWrapperValueBrick brickWrapper = (HAPManualWrapperValueBrick)attrValueWrapper;
			out = brickWrapper.getBrickTypeId();
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
			HAPManualWrapperValueReferenceResource resourceWrapper = (HAPManualWrapperValueReferenceResource)attrValueWrapper;
			out = HAPUtilityBrickId.getBrickTypeIdFromResourceTypeId(resourceWrapper.getResourceId().getResourceTypeId());
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICKREFERENCE)) {
			HAPManualWrapperValueReferenceBrick brickRefWrapper = (HAPManualWrapperValueReferenceBrick)attrValueWrapper;
			out = brickRefWrapper.getBrickTypeId();
		}
		else if(attrValueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_ATTACHMENTREFERENCE)) {
			HAPManualWrapperValueReferenceAttachment attachmentRefWrapper = (HAPManualWrapperValueReferenceAttachment)attrValueWrapper;
			out = attachmentRefWrapper.getBrickTypeId();
		}
		
		return out;
	}
	
	public static Pair<HAPManualBrick, HAPBrick> getBrickPair(HAPPath path, HAPBundle bundle){
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		HAPManualBrick entityDef = getDescdentBrickDefinition(rootEntityDefInfo, path);
		HAPBrick entityExe = HAPUtilityBrick.getDescdentBrickLocal(bundle.getBrickWrapper(), path);
		return Pair.of(entityDef, entityExe);
	}
	
	public static HAPTreeNode getDescdentTreeNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getBrick(), path);
		}
		return out;
	}
	
	public static HAPManualBrick getDescdentBrickDefinition(HAPManualWrapperBrick rootEntityInfo, HAPPath path) {
		return getDescendantBrick(rootEntityInfo.getBrick(), path);
	}
	
	public static HAPManualAttribute getDescendantAttribute(HAPManualBrick brickManual, HAPPath path) {
		HAPManualAttribute out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = brickManual.getAttribute(attribute);
			} else {
				HAPManualWrapperValue attrValueInfo = out.getValueWrapper();
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

	private static HAPManualBrick getDescendantBrick(HAPManualBrick entityDef, HAPPath path) {
		HAPManualBrick out = null;
		if(path==null||path.isEmpty()) {
			out = entityDef;
		} else {
			HAPManualWrapperValue attrValueInfo = getDescendantAttribute(entityDef, path).getValueWrapper();
			if(attrValueInfo instanceof HAPManualWithBrick) {
				out = ((HAPManualWithBrick)attrValueInfo).getBrick();
			}
		}
		return out;
	}

	
	public static HAPTreeNode getDefTreeNodeFromExeTreeNode(HAPTreeNodeBrick treeNodeExe, HAPBundle bundle) {
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		return HAPManualUtilityBrick.getDescdentTreeNode(rootEntityDefInfo, treeNodeExe.getTreeNodeInfo().getPathFromRoot());
	}

	public static HAPManualBrick getEntityDefinitionFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		return HAPManualUtilityBrick.getDescdentBrickDefinition(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}

	public static boolean isAdapterAutoProcess(HAPManualAttribute attr, HAPManagerApplicationBrick entityMan) {
		return true;
	}

	public static HAPManualBrickRelation getEntityRelation(HAPManualAttribute attr, String relationType) {
		for(HAPManualBrickRelation relation : attr.getRelations()) {
			if(relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManagerApplicationBrick entityMan) {
		if(brickTypeId.getBrickType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
			return false;
		} else {
			return HAPUtilityBrick.isBrickComplex(brickTypeId, entityMan);
		}
	}
	
}
