package com.nosliw.core.application.division.manual.definition;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.HAPManualWithBrick;
import com.nosliw.core.application.division.manual.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.executable.HAPInfoTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;

public class HAPManualDefinitionUtilityBrick {

	public static HAPManualDefinitionWrapperBrickRoot getBrickRootDefinition(String rootBrickName, HAPBundle bundle) {
		return ((HAPManualWrapperBrickRoot)bundle.getRootBrickWrapper(rootBrickName)).getDefinition();
	}
	
	public static HAPTreeNode getDefTreeNodeFromExeTreeNode(HAPTreeNodeBrick treeNodeExe, HAPBundle bundle) {
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeExe.getTreeNodeInfo());
		HAPManualDefinitionWrapperBrickRoot rootEntityDefInfo = getBrickRootDefinition(pathInfo.getRoot(), bundle);
		return HAPManualDefinitionUtilityBrick.getDescdentTreeNode(rootEntityDefInfo, pathInfo.getPath());
	}

	public static HAPManualDefinitionBrick getDescendantBrickDefinition(HAPBundle bundle, HAPInfoTreeNode treeNodeInfo) {
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeInfo);
		HAPManualDefinitionWrapperBrickRoot rootBrickWrapper = getBrickRootDefinition(pathInfo.getRoot(), bundle);
		return getDescendantBrickDefinition(rootBrickWrapper.getBrick(), pathInfo.getPath());
	}

	public static Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPInfoTreeNode treeNodeInfo, HAPBundle bundle){
		HAPComplexPath pathInfo = HAPManualUtilityBrick.getBrickFullPathInfo(treeNodeInfo);
		return getBrickPair(pathInfo.getRoot(), pathInfo.getPath(), bundle);
	}
	
	public static Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(String rootName, HAPPath path, HAPBundle bundle){
		HAPManualDefinitionWrapperBrickRoot rootEntityDefInfo = getBrickRootDefinition(rootName, bundle);
		HAPManualDefinitionBrick entityDef = getDescendantBrickDefinition(rootEntityDefInfo, path);
		HAPManualBrick entityExe = (HAPManualBrick)HAPUtilityBrick.getDescdentBrickLocal(bundle.getRootBrickWrapper(rootName), path);
		return Pair.of(entityDef, entityExe);
	}
	
	private static Pair<HAPManualDefinitionWrapperBrickRoot, HAPPath> getDefinitionPathInfo(HAPBundle bundle, HAPPath path, String rootNameIfNotProvided){
		HAPComplexPath fullPathInfo = HAPUtilityBundle.getBrickFullPathInfo(path.toString(), rootNameIfNotProvided);
		return Pair.of(((HAPManualWrapperBrickRoot)bundle.getRootBrickWrapper(fullPathInfo.getRoot())).getDefinition(), fullPathInfo.getPath());
	}
	

	
	private static HAPManualDefinitionBrick getDescendantBrickDefinition(HAPManualDefinitionWrapperBrickRoot rootEntityInfo, HAPPath path) {
		return getDescendantBrickDefinition(rootEntityInfo.getBrick(), path);
	}
	
	
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
	
	private static HAPTreeNode getDescdentTreeNode(HAPManualDefinitionWrapperBrickRoot rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getBrick(), path);
		}
		return out;
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

	public static HAPManualDefinitionBrick getDescendantBrickDefinition(HAPManualDefinitionBrick entityDef, HAPPath path) {
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
