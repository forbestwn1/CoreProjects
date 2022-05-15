package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPExecutableEntityComplexValueStructure extends HAPSerializableImp{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX;

	public static String PART = "part";

	private int m_idIndex;
	
	private List<HAPPartComplexValueStructure> m_parts;
	
	public HAPExecutableEntityComplexValueStructure() {
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

	public void addPartSimple(HAPWrapperValueStructureExecutable valueStructureExeWrapper, HAPInfoPartValueStructure partInfo) {
		HAPPartComplexValueStructureSimple part = new HAPPartComplexValueStructureSimple(partInfo);
		part.addValueStructure(valueStructureExeWrapper);
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPPartComplexValueStructure> children, HAPInfoPartValueStructure partInfo) {
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
	
	public HAPExecutableEntityComplexValueStructure cloneValueStructureComplex() {
		HAPExecutableEntityComplexValueStructure out = new HAPExecutableEntityComplexValueStructure();
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

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPPartComplexValueStructure part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPJsonUtility.buildArrayJson(partArrayJson.toArray(new String[0])));
	}

}
