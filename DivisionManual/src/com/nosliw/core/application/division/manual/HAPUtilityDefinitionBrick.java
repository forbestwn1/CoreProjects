package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;

public class HAPUtilityDefinitionBrick {

	public static Pair<HAPManualBrick, HAPBrick> getEntityPair(HAPPath path, HAPBundle bundle){
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		HAPManualBrick entityDef = getDescdentEntityDefinition(rootEntityDefInfo, path);
		HAPBrick entityExe = HAPUtilityBrick.getDescdentBrick(bundle.getBrickWrapper(), path);
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
	
	public static HAPManualBrick getDescdentEntityDefinition(HAPManualWrapperBrick rootEntityInfo, HAPPath path) {
		return getDescendantEntity(rootEntityInfo.getBrick(), path);
	}
	
	public static HAPManualAttribute getDescendantAttribute(HAPManualBrick entityDef, HAPPath path) {
		HAPManualAttribute out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = entityDef.getAttribute(attribute);
			} else {
				HAPManualWrapperValueInAttribute attrValueInfo = out.getValueInfo();
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

	private static HAPManualBrick getDescendantEntity(HAPManualBrick entityDef, HAPPath path) {
		HAPManualBrick out = null;
		if(path==null||path.isEmpty()) {
			out = entityDef;
		} else {
			HAPManualWrapperValueInAttribute attrValueInfo = getDescendantAttribute(entityDef, path).getValueInfo();
			if(attrValueInfo instanceof HAPManualWithBrick) {
				out = ((HAPManualWithBrick)attrValueInfo).getBrick();
			}
		}
		return out;
	}

	
	public static HAPTreeNode getDefTreeNodeFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		return HAPUtilityDefinitionBrick.getDescdentTreeNode(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}

	public static HAPManualBrick getEntityDefinitionFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualWrapperBrick rootEntityDefInfo = (HAPManualWrapperBrick)bundle.getExtraData();
		return HAPUtilityDefinitionBrick.getDescdentEntityDefinition(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}
	
	public static boolean isAttributeAutoProcess(HAPManualAttribute attr, HAPManagerApplicationBrick entityMan) {
		HAPManualBrickRelationAutoProcess relation = (HAPManualBrickRelationAutoProcess)getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualWrapperValueInAttribute attrValueInfo = attr.getValueInfo();
		if(attrValueInfo instanceof HAPManualWithBrick) {
			boolean isComplex = HAPUtilityBrick.isBrickComplex(((HAPManualWithBrick)attrValueInfo).getBrickTypeId(), entityMan); 
			
			if(isComplex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public static HAPManualBrickRelation getEntityRelation(HAPManualAttribute attr, String relationType) {
		for(HAPManualBrickRelation relation : attr.getRelations()) {
			if(relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
}
