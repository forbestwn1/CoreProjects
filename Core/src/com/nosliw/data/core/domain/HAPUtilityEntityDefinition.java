package com.nosliw.data.core.domain;

import java.lang.reflect.Field;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityEntityDefinition {

	public static HAPEmbededDefinitionWithId newEmbededDefinitionWithId(HAPIdEntityInDomain entityId, HAPManagerDomainEntityDefinition entityDefDomainMan) {
		return new HAPEmbededDefinitionWithId(entityId, entityDefDomainMan.isComplexEntity(entityId.getEntityType()));
	}
	
	public static HAPEmbededDefinitionWithId setEntitySimpleAttributeWithId(HAPDefinitionEntityInDomain entityDef, String attributeName, HAPIdEntityInDomain attrEntityId, HAPManagerDomainEntityDefinition entityDefDomainMan) {
		HAPEmbededDefinitionWithId embeded = new HAPEmbededDefinitionWithId(attrEntityId, entityDefDomainMan.isComplexEntity(attrEntityId.getEntityType()));
		entityDef.setSimpleAttribute(attributeName, embeded);
		return embeded;
	}
	
	//get entity type from class
	public static String getEntityTypeFromEntityClass(Class<? extends HAPDefinitionEntityInDomain> entityClass) {
		String out = null;
		try {
			Field f = entityClass.getField("ENTITY_TYPE");
			out = (String)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static HAPInfoEntityInDomainDefinition newEntityDefinitionInfoInDomain(String entityType, HAPManagerDomainEntityDefinition entityDefMan) {
		HAPInfoEntityInDomainDefinition out = new HAPInfoEntityInDomainDefinition(entityType);
		out.setIsComplexEntity(entityDefMan.isComplexEntity(entityType));
		return out;
	}

	public static HAPIdEntityInDomain getEntityDescent(HAPIdEntityInDomain entityId, HAPPath p, HAPDomainEntityDefinitionGlobal globalDomain) {
		if(p==null || p.isEmpty())  return entityId;
		HAPIdEntityInDomain currentEntityId = entityId;
		HAPInfoEntityInDomainDefinition currentEntityInfo = globalDomain.getEntityInfoDefinition(currentEntityId);
		HAPDefinitionEntityInDomain currentEntityDef = globalDomain.getEntityInfoDefinition(currentEntityId).getEntity();
		for(String seg : p.getPathSegments()) {
			currentEntityId = currentEntityDef.getChild(seg);
			currentEntityDef = globalDomain.getEntityInfoDefinition(currentEntityId).getEntity();
		}
		return currentEntityId;
	}

}
