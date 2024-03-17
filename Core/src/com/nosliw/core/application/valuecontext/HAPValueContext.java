package com.nosliw.core.application.valuecontext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.entity.valuestructure.HAPUtilityValueContext;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPValueContext extends HAPExecutableImp{

	@HAPAttribute
	public static String PART = "part";

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static String VALUESTRUCTURERUNTIMEIDBYNAME = "valueStructureRuntimeIdByName";

	@HAPAttribute
	public static String VALUESTRUCTURERUNTIMENAMEBYID = "valueStructureRuntimeNameById";

	@HAPAttribute
	public static String ISBORDER = "isBorder";

	private List<HAPPartInValueContext> m_parts;
	
	private Map<String, String> m_valueStructureRuntimeIdByName;
	
	private Map<String, String> m_valueStructureRuntimeNameById;
	
	public HAPValueContext() {
		this.m_parts = new ArrayList<HAPPartInValueContext>();
		this.m_valueStructureRuntimeIdByName = new LinkedHashMap<String, String>();
		this.m_valueStructureRuntimeNameById = new LinkedHashMap<String, String>();
	}
	
	public List<HAPPartInValueContext> getParts(){   return this.m_parts;  }
	
	public List<HAPPartInValueContext> getPart(String name) {
		List<HAPPartInValueContext> out = new ArrayList<HAPPartInValueContext>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(List<HAPWrapperExecutableValueStructure> valueStructureExeWrappers, HAPInfoPartInValueContext partInfo, HAPDomainValueStructure valueStructureDomain) {
		HAPPartInValueContextSimple part = new HAPPartInValueContextSimple(partInfo);
		for(HAPWrapperExecutableValueStructure wrapper : valueStructureExeWrappers) {
			part.addValueStructure(wrapper);
			
			//build id by name
			String name = valueStructureDomain.getValueStructureRuntimeInfo(wrapper.getValueStructureRuntimeId()).getName();
			if(name!=null) {
				this.m_valueStructureRuntimeIdByName.put(name, wrapper.getValueStructureRuntimeId());
				this.m_valueStructureRuntimeNameById.put(wrapper.getValueStructureRuntimeId(), name);
			}
		}
		this.addPart(part);
	}
	
	public void addPartGroup(List<HAPPartInValueContext> children, HAPInfoPartInValueContext partInfo) {
		HAPPartInValueContextGroupWithEntity part = new HAPPartInValueContextGroupWithEntity(partInfo);
		for(HAPPartInValueContext child : children) {
			part.addChild(child.cloneValueContextPart());
		}
		this.addPart(part);
	}
	
	private void addPart(HAPPartInValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public void copyPart(HAPPartInValueContext part) {
		this.m_parts.add(part);
		HAPUtilityValueContext.sortParts(m_parts);
	}
	
	public HAPValueContext cloneValueStructureComplex() {
		HAPValueContext out = new HAPValueContext();
		for(HAPPartInValueContext part : this.m_parts) {
			this.m_parts.add(part.cloneValueContextPart());
		}
		out.m_valueStructureRuntimeIdByName.putAll(this.m_valueStructureRuntimeIdByName);
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPPartInValueContext part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPPartInValueContext part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partArrayJson.toArray(new String[0])));
		jsonMap.put(VALUESTRUCTURERUNTIMEIDBYNAME, HAPUtilityJson.buildMapJson(m_valueStructureRuntimeIdByName));
		jsonMap.put(VALUESTRUCTURERUNTIMENAMEBYID, HAPUtilityJson.buildMapJson(m_valueStructureRuntimeNameById));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		
		List<String> valueStructureIds = new ArrayList<String>();
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructuresSorted(this);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			valueStructureIds.add(valueStructureInfo.getValueStructure().getValueStructureRuntimeId());
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureIds.toArray(new String[0])));
	}
}
