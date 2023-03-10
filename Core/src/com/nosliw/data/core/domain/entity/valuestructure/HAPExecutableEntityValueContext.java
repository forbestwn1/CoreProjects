package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

@HAPEntityWithAttribute
public class HAPExecutableEntityValueContext extends HAPExecutableImp{

	public static String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT;

	@HAPAttribute
	public static String PART = "part";

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	private List<HAPExecutablePartValueContext> m_parts;
	
	public HAPExecutableEntityValueContext() {
		this.m_parts = new ArrayList<HAPExecutablePartValueContext>();
	}
	
	public List<HAPExecutablePartValueContext> getParts(){   return this.m_parts;  }
	
	public List<HAPExecutablePartValueContext> getPart(String name) {
		List<HAPExecutablePartValueContext> out = new ArrayList<HAPExecutablePartValueContext>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(List<HAPWrapperExecutableValueStructure> valueStructureExeWrappers, HAPInfoPartValueStructure partInfo) {
		HAPExecutablePartValueContextSimple part = new HAPExecutablePartValueContextSimple(partInfo);
		for(HAPWrapperExecutableValueStructure wrapper : valueStructureExeWrappers) {
			part.addValueStructure(wrapper);
		}
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPExecutablePartValueContext> children, HAPInfoPartValueStructure partInfo) {
		HAPExecutablePartValueContextGroupWithEntity part = new HAPExecutablePartValueContextGroupWithEntity(partInfo);
		for(HAPExecutablePartValueContext child : children) {
			part.addChild(child.cloneComplexValueStructurePart());
		}
		this.addPart(part);
	}
	
	public void addPart(HAPExecutablePartValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public void copyPart(HAPExecutablePartValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public HAPExecutableEntityValueContext cloneValueStructureComplex() {
		HAPExecutableEntityValueContext out = new HAPExecutableEntityValueContext();
		for(HAPExecutablePartValueContext part : this.m_parts) {
			this.m_parts.add(part.cloneComplexValueStructurePart());
		}
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPExecutablePartValueContext part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPExecutablePartValueContext part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partArrayJson.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		List<String> jsonArray = new ArrayList<String>();
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(this);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			jsonArray.add(valueStructureInfo.toExpandedString(valueStructureDomain));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(jsonArray.toArray(new String[0])));
		
		return HAPUtilityJson.buildMapJson(jsonMap);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		List<String> valueStructureIds = new ArrayList<String>();
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructuresSorted(this);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			valueStructureIds.add(valueStructureInfo.getValueStructure().getValueStructureRuntimeId());
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureIds.toArray(new String[0])));
	}
}
