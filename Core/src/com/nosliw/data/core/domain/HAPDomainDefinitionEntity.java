package com.nosliw.data.core.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPUtilityComplexEntity;
import com.nosliw.data.core.component.HAPHandlerComplexEntity;
import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.domain.entity.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPProcessorValueStructureInComponent;
import com.nosliw.data.core.structure.HAPProcessorElementConstant;
import com.nosliw.data.core.structure.HAPProcessorElementRule;
import com.nosliw.data.core.structure.HAPProcessorElementSolidateConstantScript;
import com.nosliw.data.core.structure.HAPProcessorStructure;

/*
 * domain represent a collection of value structure, complex entity and their relationship
 * different domains does not share 
 */
public class HAPDomainDefinitionEntity{
	//all other simple resource entity
	private Map<HAPIdEntityInDomain, HAPInfoDefinitionEntityInDomain> m_entity;
	
	//id generator
	private HAPGeneratorId m_idGenerator;
	
	//set of entity that don't have parent
	private Set<HAPIdEntityInDomain> m_rootEntity;
	
	
	//all complex entity by id
//	private Map<HAPIdEntityInDomain, HAPInfoDefinitionEntityInDomainComplex> m_complexEntity;
	
	//complex entity tree 
//	private Map<HAPIdEntityInDomain, Set<HAPIdEntityInDomain>> m_childrensByParent;
//	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_parentByChild;

	//domain entity by resource id so that domain entity can be reused for same resource id 
//	private Map<HAPResourceId, HAPResourceDefinition> m_complexEntityIdByResourceId;
	

	public HAPDomainDefinitionEntity(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_rootEntity = new HashSet<HAPIdEntityInDomain>();
		this.m_entity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoDefinitionEntityInDomain>();
	}

	public HAPIdEntityInDomain addEntity(HAPDefinitionEntityInDomain entity, HAPInfoDefinitionEntityInDomain entityInfo) {
		HAPInfoDefinitionEntityInDomain out = null;
		if(entity.isComplexEntity()) {
			out = new HAPInfoDefinitionEntityInDomainComplex();
		}
		else {
			out = new HAPInfoDefinitionEntityInDomain();
		}
		entityInfo.cloneToInfoDefinitionEntityInDomain(out);
		out.setEntityId(new HAPIdEntityInDomain(this.generateId(), entity.getEntityType()));
		this.m_entity.put(out.getEntityId(), out);
		return out.getEntityId();
	}

	public HAPIdEntityInDomain addEntity(HAPDefinitionEntityInDomain entity, HAPLocalReferenceBase basePath) {
		HAPInfoDefinitionEntityInDomain entityInfo = new HAPInfoDefinitionEntityInDomain();
		entityInfo.setLocalBaseReference(basePath);
		return this.addEntity(entity, entityInfo);
	}

	public HAPInfoDefinitionEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {		return this.m_entity.get(entityId); 	}
	

	
	private String generateId() {    return this.m_idGenerator.generateId();    } 
	
	
	
	//complex entity tree root
	public Set<HAPIdEntityInDomain> getRootComplexEntity(){     }
	
	public HAPInfoDefinitionEntityInDomainComplex getComplexEntityInfo(HAPIdEntityInDomain entityId) {
		
	}
	
	public Set<HAPInfoComplexEntityDefinition> getAllComplexEntities() {
	}
	
	//add complex entity to domain, also add value structure
	public HAPIdEntityInDomain addComplexEntity(HAPInfoComplexEntityDefinition complexEntityInfo, HAPComplexValueStructure valueStructureComplex) {
		HAPDefinitionEntityComplex complexEntity = complexEntityInfo.getComplexEntity().cloneComplexEntityDefinition();
		
		HAPIdEntityInDomain out = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), complexEntity.getEntityType());
		
		//value structure
		String valueStructureComplexId = this.m_valueStructureDomain.addValueStructureComplex(valueStructureComplex);
		complexEntity.setValueStructureComplexId(valueStructureComplexId);
		
		//add entity
		complexEntity.setId(out.getEntityId());
		this.m_complexEntity.put(out, complexEntityInfo);

		//replace alias with id for this entity as parent
		updateParentAlias(complexEntityInfo.getAlias(), out);
		
		return out;
	}
	
//	public HAPDefinitionEntityComplex getEntity(HAPIdEntityInDomain entityId) {	return this.m_complexEntity.get(entityId).getComplexEntity();	}

	
	
	
	
	private void process() {
		//process info in child
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPConfigureParentRelationComplex parentInfo) {
				HAPUtilityComplexEntity.processInfo(complexEntity, parentEntity, parentInfo.getInfoRelationMode());
			}
		});
		
		//process attachment in child
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPConfigureParentRelationComplex parentInfo) {
				HAPUtilityComplexEntity.processAttachment(complexEntity, parentEntity, parentInfo.getAttachmentRelationMode());
			}
		});

		// process structure expand reference
		HAPProcessorValueStructureInComponent.expandReference(valueStructureInComponent, processContext);
		
		// process structure static (constant)
		HAPProcessorElementConstant.process(structure, parent, attachmentContainer, configure, runtimeEnv);
		
		//solidate name in context  
		structure = HAPProcessorElementSolidateConstantScript.processDefinition(structure, runtimeEnv);
		
		//process data rule
		HAPProcessorElementRule.processDefinition(structure, runtimeEnv);

		//process structure relative
		HAPProcessorStructure.processRelative(structure, parent, dependency, errors, configure, runtimeEnv);
		
		//process structure inheritance
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPConfigureParentRelationComplex parentInfo) {
				HAPUtilityComplexEntity.processValueStructureInheritance(complexEntity, parentEntity, parentInfo.getValueStructureRelationMode());
			}
		});
		
		
		
		// process structure (constant, inheritance)
		
		
	}
	
	
	
	//build entity tree
	private void buildEntityTree() {
		for(HAPIdEntityInDomain entityId : this.m_complexEntity.keySet()) {
			HAPInfoComplexEntityDefinition entityInfo = this.m_complexEntity.get(entityId);
			HAPConfigureParentRelationComplex parentInfo = entityInfo.getParentInfo();
			if(parentInfo!=null) {
				this.m_parentByChild.put(HAPUtilityComplexEntity.getEntityId(entityInfo.getComplexEntity()), parentInfo.getParentId());
				Set<HAPIdEntityInDomain> childrenIds = this.m_childrensByParent.get(parentInfo.getParentId());
				if(childrenIds==null) {
					childrenIds = new HashSet<HAPIdEntityInDomain>();
					this.m_childrensByParent.put(parentInfo.getParentId(), childrenIds);
				}
				childrenIds.add(entityId);
			}
			else {
				//no parent, root entity
				this.m_rootEntity.add(entityId);
			}
		}
	}
	
	//update parent alias with id
	private void updateParentAlias(Set<String> alias, HAPIdEntityInDomain parentId) {
		if(alias==null||alias.isEmpty())  return;
		
		for(HAPIdEntityInDomain entityId : this.m_complexEntity.keySet()) {
			HAPInfoComplexEntityDefinition entityInfo = this.m_complexEntity.get(entityId);
			HAPConfigureParentRelationComplex parentInfo = entityInfo.getParentInfo();
			if(parentInfo!=null) {
				parentInfo.replaceAliasWithId(alias, parentId);
			}
		}
	}
	
	private void traverseAllEntity(HAPHandlerComplexEntity handler) {
		for(HAPIdEntityInDomain entityId : this.m_rootEntity) {
			traverseEntity(entityId, handler);
		}
	}
	
	//i
	private void traverseEntity(HAPIdEntityInDomain entityId, HAPHandlerComplexEntity handler) {
		Set<HAPIdEntityInDomain> childrenId = this.m_childrensByParent.get(entityId);
		
		for(HAPIdEntityInDomain childId : childrenId) {
			handler.process(this.getEntity(childId), this.getEntity(entityId), this.m_complexEntity.get(childId).getParentInfo());
		}
	}

}
