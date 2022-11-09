package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPDefinitionEntityComplexValueStructure3 extends HAPDefinitionEntityInDomainSimple{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX;

	public static final String PART = "part";
	
	private int m_idIndex;
	
	private List<HAPExecutablePartComplexValueStructure> m_parts;
	
	private HAPGeneratorId m_idGenerator;
	 
	public HAPDefinitionEntityComplexValueStructure3() {
		this.m_parts = new ArrayList<HAPExecutablePartComplexValueStructure>();
	}
	
	public List<HAPExecutablePartComplexValueStructure> getParts(){   return this.m_parts;  }
	
	public List<HAPExecutablePartComplexValueStructure> getPart(String name) {
		List<HAPExecutablePartComplexValueStructure> out = new ArrayList<HAPExecutablePartComplexValueStructure>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(HAPValueStructure valueStructure, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPExecutablePartComplexValueStructureSimple part = new HAPExecutablePartComplexValueStructureSimple(valueStructure.cloneValueStructure(), partInfo);
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPExecutablePartComplexValueStructure> children, HAPInfoPartValueStructure partInfo) {
		partInfo.setId(this.generateId(partInfo));
		HAPExecutablePartComplexValueStructureGroupWithEntity part = new HAPExecutablePartComplexValueStructureGroupWithEntity(partInfo);
		for(HAPExecutablePartComplexValueStructure child : children) {
			part.addChild(child.cloneComplexValueStructurePart());
		}
		this.addPart(part);
	}
	
	public void addPart(HAPExecutablePartComplexValueStructure part) {
		this.m_parts.add(part);
		HAPUtilityComplexValueStructure.sortParts(m_parts);
	}
	
	public void copyPart(HAPExecutablePartComplexValueStructure part) {
		this.m_parts.add(part);
		HAPUtilityComplexValueStructure.sortParts(m_parts);
	}
	
	public HAPDefinitionEntityComplexValueStructure3 cloneValueStructureComplex() {
		HAPDefinitionEntityComplexValueStructure3 out = new HAPDefinitionEntityComplexValueStructure3();
		for(HAPExecutablePartComplexValueStructure part : this.m_parts) {
			this.m_parts.add(part.cloneComplexValueStructurePart());
		}
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPExecutablePartComplexValueStructure part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	private String generateId(HAPInfoPartValueStructure partInfo) {
		return partInfo.getName() + "_" + this.m_idIndex++;
	}

	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityDefinitionLocal entityDefDomain){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> partJsonArray = new ArrayList<String>();
		for(HAPExecutablePartComplexValueStructure part : this.m_parts) {
			partJsonArray.add(part.toExpandedJsonString(entityDefDomain));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partJsonArray.toArray(new String[0])));
	}

}
