package com.nosliw.data.core.complex;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructureGroupWithEntity;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructureSimple;
import com.nosliw.data.core.complex.valuestructure.HAPProcessorValueStructureInComponent;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.component.HAPHandlerComplexEntity;
import com.nosliw.data.core.resource.HAPEntityResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.structure.HAPProcessorElementConstant;
import com.nosliw.data.core.structure.HAPProcessorElementRule;
import com.nosliw.data.core.structure.HAPProcessorElementSolidateConstantScript;
import com.nosliw.data.core.structure.HAPProcessorStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

/*
 * domain represent a collection of value structure, complex entity and their relationship
 * different domains does not share 
 */
public class HAPDomainDefinitionComplex{

	//value structure domain (value structure definition, runtime id)
	private HAPDomainValueStructure m_valueStructureDomain;
	
	//all complex entity by id
	private Map<String, HAPInfoComplexEntityInDomain> m_complexEntity;
	
	//complex entity tree 
	private Map<String, Set<String>> m_childrensByParent;
	private Map<String, String> m_parentByChild;
	//set of entity that don't have parent
	private Set<String> m_rootEntity;

	//domain entity by resource id so that domain entity can be reused for same resource id 
	private Map<HAPResourceId, HAPResourceDefinition> m_complexEntityIdByResourceId;
	
	//all other simple resource entity
	private Map<String, HAPEntityResourceDefinition> m_entity;
	
	//id generator
	private HAPGeneratorId m_idGenerator;

	public HAPDomainDefinitionComplex(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = new HAPDomainValueStructure(this.m_idGenerator);
		this.m_complexEntity = new LinkedHashMap<String, HAPInfoComplexEntityInDomain>();
		this.m_childrensByParent = new LinkedHashMap<String, Set<String>>();
		this.m_parentByChild = new LinkedHashMap<String, String>();
		this.m_rootEntity = new HashSet<String>();
		this.m_complexEntityIdByResourceId = new LinkedHashMap<HAPResourceId, HAPResourceDefinition>();
		this.m_entity = new LinkedHashMap<String, HAPEntityResourceDefinition>();
	}

	public HAPIdEntityInDomain addComplexEntity(HAPInfoComplexEntityInDomain complexEntityInfo) {
		HAPDefinitionEntityComplex complexEntity = complexEntityInfo.getComplexEntity().cloneComplexEntityDefinition();
		
		String id = this.m_idGenerator.generateId();
	
		//add entity
		complexEntity.setId(id);
		this.m_complexEntity.put(id, complexEntityInfo);

		//replace alias with id for this entity as parent
		updateParentAlias(complexEntityInfo.getAlias(), id);
		
		//discover value structure definition
		for(HAPPartComplexValueStructure part : complexEntity.getValueStructureComplex().getParts()) {
			extractSimpleValueStructure(part);
		}

		return new HAPIdEntityInDomain(id);
	}
	

	
	public HAPDefinitionEntityComplex getEntity(HAPIdEntityInDomain entityId) {	return this.m_complexEntity.get(entityId).getComplexEntity();	}

	private void process() {
		//process info in child
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPInfoParentEntity parentInfo) {
				HAPUtilityComplexEntity.processInfo(complexEntity, parentEntity, parentInfo.getInfoRelationMode());
			}
		});
		
		//process attachment in child
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPInfoParentEntity parentInfo) {
				HAPUtilityComplexEntity.processAttachment(complexEntity, parentEntity, parentInfo.getAttachmentRelationMode());
			}
		});

		// process structure expand reference
		HAPProcessorValueStructureInComponent.expandReference(valueStructureInComponent, processContext);
		
		// process structure static (constant)
		HAPProcessorElementConstant.process(structure, parent, attachmentContainer, configure, runtimeEnv);
		
		//solidate name in context  
		structure = HAPProcessorElementSolidateConstantScript.process(structure, runtimeEnv);
		
		//process data rule
		HAPProcessorElementRule.process(structure, runtimeEnv);

		//process structure relative
		HAPProcessorStructure.processRelative(structure, parent, dependency, errors, configure, runtimeEnv);
		
		//process structure inheritance
		this.traverseAllEntity(new HAPHandlerComplexEntity() {
			@Override
			public void process(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity,
					HAPInfoParentEntity parentInfo) {
				HAPUtilityComplexEntity.processValueStructureInheritance(complexEntity, parentEntity, parentInfo.getValueStructureRelationMode());
			}
		});
		
		
		
		// process structure (constant, inheritance)
		
		
	}
	
	
	
	public String addValueStructure(HAPValueStructure valueStructure) {
		String id = this.generateId();
		this.addValueStructure(id, valueStructure);
		return id;
	}

	private void addValueStructure(String id, HAPValueStructure valueStructure) {
		HAPWrapperValueStructure wrapper = new HAPWrapperValueStructure(valueStructure);
		wrapper.setId(id);
		this.m_valueStructure.put(wrapper.getId(), wrapper);
	}
	
	//extract value structure from complex and add to pool
	private void extractSimpleValueStructure(HAPPartComplexValueStructure part) {
		//part id
		part.setId(this.generateId());
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPPartComplexValueStructureSimple simplePart = (HAPPartComplexValueStructureSimple)part;
			String valueStructureId = this.addValueStructure(simplePart.getValueStructure());
			simplePart.setValueStructureDefinitionId(valueStructureId);
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)) {
			HAPPartComplexValueStructureGroupWithEntity entityGroup = (HAPPartComplexValueStructureGroupWithEntity)part;
			for(HAPPartComplexValueStructure child : entityGroup.getChildren()) {
				extractSimpleValueStructure(child);
			}
		}
	}

	//build entity tree
	private void buildEntityTree() {
		for(String entityId : this.m_complexEntity.keySet()) {
			HAPInfoComplexEntityInDomain entityInfo = this.m_complexEntity.get(entityId);
			HAPInfoParentEntity parentInfo = entityInfo.getParentInfo();
			if(parentInfo!=null) {
				this.m_parentByChild.put(entityInfo.getComplexEntity().getId(), parentInfo.getParentId());
				Set<String> childrenIds = this.m_childrensByParent.get(parentInfo.getParentId());
				if(childrenIds==null) {
					childrenIds = new HashSet<String>();
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
	private void updateParentAlias(Set<String> alias, String parentId) {
		if(alias==null||alias.isEmpty())  return;
		
		for(String entityId : this.m_complexEntity.keySet()) {
			HAPInfoComplexEntityInDomain entityInfo = this.m_complexEntity.get(entityId);
			HAPInfoParentEntity parentInfo = entityInfo.getParentInfo();
			if(parentInfo!=null) {
				parentInfo.replaceAliasWithId(alias, parentId);
			}
		}
	}
	
	private void traverseAllEntity(HAPHandlerComplexEntity handler) {
		for(String entityId : this.m_rootEntity) {
			traverseEntity(entityId, handler);
		}
	}
	
	//i
	private void traverseEntity(String entityId, HAPHandlerComplexEntity handler) {
		Set<String> childrenId = this.m_childrensByParent.get(entityId);
		
		for(String childId : childrenId) {
			handler.process(this.getEntity(childId), this.getEntity(entityId), this.m_complexEntity.get(childId).getParentInfo());
		}
	}

}
