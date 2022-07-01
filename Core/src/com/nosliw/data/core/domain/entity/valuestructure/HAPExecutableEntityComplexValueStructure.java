package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
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

	public void addPartSimple(List<HAPWrapperValueStructureExecutable> valueStructureExeWrappers, HAPInfoPartValueStructure partInfo) {
		HAPPartComplexValueStructureSimple part = new HAPPartComplexValueStructureSimple(partInfo);
		for(HAPWrapperValueStructureExecutable wrapper : valueStructureExeWrappers) {
			part.addValueStructure(wrapper);
		}
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
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPPartComplexValueStructure part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPJsonUtility.buildArrayJson(partArrayJson.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		List<String> jsonArray = new ArrayList<String>();
		for(HAPInfoPartSimple partInfo : HAPUtilityComplexValueStructure.getAllSimpleParts(this)) {
			jsonArray.add(partInfo.toExpandedString(valueStructureDomain));
		}
		jsonMap.put(PART, HAPJsonUtility.buildArrayJson(jsonArray.toArray(new String[0])));
		
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		List<String> valueStructureIds = new ArrayList<String>();
		List<HAPInfoPartSimple> simpleParts = HAPUtilityComplexValueStructure.getAllSimpleParts(this);
		for(HAPInfoPartSimple simplePart : simpleParts) {
			List<HAPWrapperValueStructureExecutable> valueStructures = simplePart.getSimpleValueStructurePart().getValueStructures();
			for(HAPWrapperValueStructureExecutable valueStructure : valueStructures) {
				valueStructureIds.add(valueStructure.getValueStructureRuntimeId());
			}
		}
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildArrayJson(valueStructureIds.toArray(new String[0])));
	}
}
