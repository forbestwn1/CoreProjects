package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityEntity;

public class HAPUtilityDefinitionEntity {

	public static Pair<HAPManualEntity, HAPBrick> getEntityPair(HAPPath path, HAPBundle bundle){
		HAPManualInfoEntity rootEntityDefInfo = (HAPManualInfoEntity)bundle.getExtraData();
		HAPManualEntity entityDef = getDescdentEntityDefinition(rootEntityDefInfo, path);
		HAPBrick entityExe = HAPUtilityEntity.getDescdentEntity(bundle.getEntityInfo(), path);
		return Pair.of(entityDef, entityExe);
	}
	
	public static HAPTreeNode getDescdentTreeNode(HAPManualInfoEntity rootEntityInfo, HAPPath path) {
		HAPTreeNode out = null;
		if(path==null || path.isEmpty()) {
			out = rootEntityInfo;
		}
		else {
			out = getDescendantAttribute(rootEntityInfo.getEntity(), path);
		}
		return out;
	}
	
	public static HAPManualEntity getDescdentEntityDefinition(HAPManualInfoEntity rootEntityInfo, HAPPath path) {
		return getDescendantEntity(rootEntityInfo.getEntity(), path);
	}
	
	public static HAPManualAttribute getDescendantAttribute(HAPManualEntity entityDef, HAPPath path) {
		HAPManualAttribute out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0) {
				out = entityDef.getAttribute(attribute);
			} else {
				HAPManualInfoAttributeValue attrValueInfo = out.getValueInfo();
				if(attrValueInfo instanceof HAPManualWithEntity) {
					out = ((HAPManualWithEntity)attrValueInfo).getEntity().getAttribute(attribute);
				}
				else if(attrValueInfo.getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
					throw new RuntimeException();
				}
			}
		}
		return out;
	}

	private static HAPManualEntity getDescendantEntity(HAPManualEntity entityDef, HAPPath path) {
		HAPManualEntity out = null;
		if(path==null||path.isEmpty()) {
			out = entityDef;
		} else {
			HAPManualInfoAttributeValue attrValueInfo = getDescendantAttribute(entityDef, path).getValueInfo();
			if(attrValueInfo instanceof HAPManualWithEntity) {
				out = ((HAPManualWithEntity)attrValueInfo).getEntity();
			}
		}
		return out;
	}

	
	public static HAPTreeNode getDefTreeNodeFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualInfoEntity rootEntityDefInfo = (HAPManualInfoEntity)bundle.getExtraData();
		return HAPUtilityDefinitionEntity.getDescdentTreeNode(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}

	public static HAPManualEntity getEntityDefinitionFromExeTreeNode(HAPTreeNode treeNodeExe, HAPBundle bundle) {
		HAPManualInfoEntity rootEntityDefInfo = (HAPManualInfoEntity)bundle.getExtraData();
		return HAPUtilityDefinitionEntity.getDescdentEntityDefinition(rootEntityDefInfo, treeNodeExe.getPathFromRoot());
	}
	
	public static boolean isAttributeAutoProcess(HAPManualAttribute attr, HAPManagerApplicationBrick entityMan) {
		HAPManualEntityRelationAutoProcess relation = (HAPManualEntityRelationAutoProcess)getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualInfoAttributeValue attrValueInfo = attr.getValueInfo();
		if(attrValueInfo instanceof HAPManualWithEntity) {
			boolean isComplex = HAPUtilityEntity.isEntityComplex(((HAPManualWithEntity)attrValueInfo).getEntityTypeId(), entityMan); 
			
			if(isComplex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public static HAPIdBrickType parseEntityTypeId(Object obj) {
		
	}
	
	public static HAPManualEntityRelation getEntityRelation(HAPManualAttribute attr, String relationType) {
		for(HAPManualEntityRelation relation : attr.getRelations()) {
			if(relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
}
