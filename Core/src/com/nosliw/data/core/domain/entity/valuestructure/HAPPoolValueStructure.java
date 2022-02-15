package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

//contains all value structures
public class HAPPoolValueStructure {
	
	//value structure definitions by id
	private Map<String, HAPWrapperValueStructure> m_valueStructure;
	
	//all value structure complex by id
	private Map<String, HAPComplexValueStructure> m_valueStructureComplex;
	
	//value structure complex tree
	private Map<String, String> m_parents;
	private Map<String, Set<String>> m_children;
	
	private int m_idIndex;
	
	public HAPPoolValueStructure() {
		this.m_valueStructure = new LinkedHashMap<String, HAPWrapperValueStructure>();
		this.m_idIndex = 0;
	}
	
	public String addValueStructureComplex(HAPComplexValueStructure valueStructureComplex, String parentId) {
		String id = this.generateId();
		valueStructureComplex.setId(id);
		for(HAPPartComplexValueStructure part : valueStructureComplex.getParts()) {
			extractSimpleValueStructure(part);
		}
		this.m_valueStructureComplex.put(id, valueStructureComplex);
		//build tree
		if(parentId!=null) {
			this.m_parents.put(id, parentId);
			Set<String> childrenId = this.m_children.get(parentId);
			if(childrenId==null) {
				childrenId = new HashSet<String>();
				this.m_children.put(parentId, childrenId);
			}
			childrenId.add(id);
		}
		
		return id;
	}
	
	
	public HAPValueStructure getValueStructure(String id) {   return this.m_valueStructure.get(id).getValueStructure();    }
	
	public void setValueStructure(String id, HAPValueStructure valueStructure) {   
		HAPWrapperValueStructure wrapper = this.getValueStructureWrapper(id);
		if(wrapper==null) {
			this.addValueStructure(id, valueStructure);
		}
		else {
			wrapper.setValueStructure(valueStructure);
		}
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
	
	private HAPWrapperValueStructure getValueStructureWrapper(String id) {
		return this.m_valueStructure.get(id);
	}
	
	private String generateId() {
		this.m_idIndex++;
		return this.m_idIndex + "";
	}
	
	//extract value structure from complex and add to pool
	private void extractSimpleValueStructure(HAPPartComplexValueStructure part) {
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
	

}
