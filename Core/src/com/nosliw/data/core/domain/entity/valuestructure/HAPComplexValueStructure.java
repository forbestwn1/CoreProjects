package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPComplexValueStructure extends HAPDefinitionEntityInDomainSimple{

	private int m_idIndex;
	
	private List<HAPPartComplexValueStructure> m_parts;
	
	private HAPGeneratorId m_idGenerator;
	 
	public HAPComplexValueStructure() {
		this.m_parts = new ArrayList<HAPPartComplexValueStructure>();
	}
	
	public List<HAPPartComplexValueStructure> getParts(){   return this.m_parts;  }
	
	public List<HAPPartComplexValueStructure> getPart(String name) {
		List<HAPPartComplexValueStructure> out = new ArrayList<HAPPartComplexValueStructure>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(HAPValueStructure valueStructure, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPPartComplexValueStructureSimple part = new HAPPartComplexValueStructureSimple(valueStructure.cloneValueStructure(), partInfo);
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPPartComplexValueStructure> children, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPPartComplexValueStructureGroupWithEntity part = new HAPPartComplexValueStructureGroupWithEntity(partInfo);
		for(HAPPartComplexValueStructure child : children) {
			part.addChild(child.cloneComplexValueStructurePart());
		}
		this.addPart(part);
	}
	
	public void addPart(HAPPartComplexValueStructure part) {
		this.m_parts.add(part);
		HAPUtilityComplexValueStructure.sortParts(m_parts);
	}
	
	public void copyPart(HAPPartComplexValueStructure part) {
		this.m_parts.add(part);
		HAPUtilityComplexValueStructure.sortParts(m_parts);
	}
	
	public HAPComplexValueStructure cloneValueStructureComplex() {
		HAPComplexValueStructure out = new HAPComplexValueStructure();
		for(HAPPartComplexValueStructure part : this.m_parts) {
			this.m_parts.add(part.cloneComplexValueStructurePart());
		}
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPPartComplexValueStructure part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	private String generateId(HAPInfoPartValueStructure partInfo) {
		return partInfo.getName() + "_" + this.m_idIndex++;
	}

}
