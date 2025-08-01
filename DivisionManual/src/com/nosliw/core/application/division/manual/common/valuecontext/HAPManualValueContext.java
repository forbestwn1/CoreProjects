package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.xxx.application1.HAPValueContext;
import com.nosliw.core.xxx.application1.valuestructure.HAPDomainValueStructure;

public class HAPManualValueContext extends HAPSerializableImp implements HAPValueContext{

	@HAPAttribute
	public static String PART = "part";

	@HAPAttribute
	public static String VALUESTRUCTURERUNTIMEIDBYNAME = "valueStructureRuntimeIdByName";

	@HAPAttribute
	public static String VALUESTRUCTURERUNTIMENAMEBYID = "valueStructureRuntimeNameById";

	@HAPAttribute
	public static String ISBORDER = "isBorder";

	private List<HAPManualPartInValueContext> m_parts;
	
	private Map<String, String> m_valueStructureRuntimeIdByName;
	
	private Map<String, String> m_valueStructureRuntimeNameById;
	
	public HAPManualValueContext() {
		this.m_parts = new ArrayList<HAPManualPartInValueContext>();
		this.m_valueStructureRuntimeIdByName = new LinkedHashMap<String, String>();
		this.m_valueStructureRuntimeNameById = new LinkedHashMap<String, String>();
	}
	
	@Override
	public List<String> getValueStructureIds() {
		List<String> valueStructureIds = new ArrayList<String>();
		List<HAPManualInfoValueStructureSorting> valueStructureInfos = HAPManualUtilityValueContext.getAllValueStructuresSorted(this);
		for(HAPManualInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			valueStructureIds.add(valueStructureInfo.getValueStructure().getValueStructureRuntimeId());
		}
		return valueStructureIds;
	}
	
	public void cleanValueStucture(Set<String> valueStrucutreIds) {
		for(int i=this.m_parts.size()-1; i>=0; i--) {
			HAPManualPartInValueContext part = this.m_parts.get(i);
			part.cleanValueStucture(valueStrucutreIds);
			if(part.isEmpty()) {
				this.m_parts.remove(i);
			}
		}
		
		for(String vsId : valueStrucutreIds) {
			this.m_valueStructureRuntimeNameById.remove(vsId);
		}
		
		for(String name : this.m_valueStructureRuntimeIdByName.keySet()) {
			if(valueStrucutreIds.contains(this.m_valueStructureRuntimeIdByName.get(name))) {
				this.m_valueStructureRuntimeIdByName.remove(name);
			}
		}
	}
	
	public boolean isEmpty(HAPDomainValueStructure valueStructureDomain) {
		for(HAPManualPartInValueContext part : this.m_parts) {
			if(!part.isEmptyOfValueStructure(valueStructureDomain)) {
				return false;
			}
		}
		return true;
	}
	
	public List<HAPManualPartInValueContext> getParts(){   return this.m_parts;  }
	
	public List<HAPManualPartInValueContext> getPart(String name) {
		List<HAPManualPartInValueContext> out = new ArrayList<HAPManualPartInValueContext>();
		for(int i : this.findPartByName(name)) {
			out.add(this.m_parts.get(i));
		}
		return out;
	}

	public void addPartSimple(List<HAPManualInfoValueStructure> valueStructureExeWrappers, HAPManualInfoPartInValueContext partInfo, HAPDomainValueStructure valueStructureDomain) {
		HAPManualPartInValueContextSimple part = new HAPManualPartInValueContextSimple(partInfo);
		for(HAPManualInfoValueStructure wrapper : valueStructureExeWrappers) {
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
	
	public void addPartGroup(List<HAPManualPartInValueContext> children, HAPManualInfoPartInValueContext partInfo) {
		HAPManualPartInValueContextGroupWithEntity part = new HAPManualPartInValueContextGroupWithEntity(partInfo);
		for(HAPManualPartInValueContext child : children) {
			part.addChild(child.cloneValueContextPart());
		}
		this.addPart(part);
	}
	
	private void addPart(HAPManualPartInValueContext part) {
		this.m_parts.add(part);
		HAPManualUtilityValueContext.sortParts(m_parts);
	}
	
	public void copyPart(HAPManualPartInValueContext part) {
		this.m_parts.add(part);
		HAPManualUtilityValueContext.sortParts(m_parts);
	}
	
	public HAPManualValueContext cloneValueStructureComplex() {
		HAPManualValueContext out = new HAPManualValueContext();
		for(HAPManualPartInValueContext part : this.m_parts) {
			this.m_parts.add(part.cloneValueContextPart());
		}
		out.m_valueStructureRuntimeIdByName.putAll(this.m_valueStructureRuntimeIdByName);
		return out;
	}
	
	private List<Integer> findPartByName(String name) {
		List<Integer> out = new ArrayList<Integer>();
		for(int i=0; i<this.m_parts.size(); i++) {
			HAPManualPartInValueContext part = this.m_parts.get(i);
			if(name.equals(part.getName())) {
				out.add(i);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> partArrayJson = new ArrayList<String>();
		for(HAPManualPartInValueContext part : this.m_parts) {
			partArrayJson.add(part.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partArrayJson.toArray(new String[0])));
		jsonMap.put(VALUESTRUCTURERUNTIMEIDBYNAME, HAPUtilityJson.buildMapJson(m_valueStructureRuntimeIdByName));
		jsonMap.put(VALUESTRUCTURERUNTIMENAMEBYID, HAPUtilityJson.buildMapJson(m_valueStructureRuntimeNameById));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(getValueStructureIds().toArray(new String[0])));
	}

}
