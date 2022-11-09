package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

@HAPEntityWithAttribute
public class HAPExecutableEntityComplexValueStructure extends HAPExecutableImp{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURECOMPLEX;

	@HAPAttribute
	public static String PART = "part";

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	private List<HAPExecutablePartComplexValueStructure> m_parts;
	
	public HAPExecutableEntityComplexValueStructure() {
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

	public void addPartSimple(List<HAPWrapperExecutableValueStructure> valueStructureExeWrappers, HAPInfoPartValueStructure partInfo) {
		HAPExecutablePartComplexValueStructureSimple part = new HAPExecutablePartComplexValueStructureSimple(partInfo);
		for(HAPWrapperExecutableValueStructure wrapper : valueStructureExeWrappers) {
			part.addValueStructure(wrapper);
		}
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPExecutablePartComplexValueStructure> children, HAPInfoPartValueStructure partInfo) {
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
	
	public HAPExecutableEntityComplexValueStructure cloneValueStructureComplex() {
		HAPExecutableEntityComplexValueStructure out = new HAPExecutableEntityComplexValueStructure();
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
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPExecutablePartComplexValueStructure part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partArrayJson.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		List<String> jsonArray = new ArrayList<String>();
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityComplexValueStructure.getAllValueStructures(this);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			jsonArray.add(valueStructureInfo.toExpandedString(valueStructureDomain));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(jsonArray.toArray(new String[0])));
		
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		List<String> valueStructureIds = new ArrayList<String>();
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityComplexValueStructure.getAllValueStructuresSorted(this);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			valueStructureIds.add(valueStructureInfo.getValueStructure().getValueStructureRuntimeId());
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureIds.toArray(new String[0])));
	}
}
