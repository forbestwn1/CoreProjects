package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPDomainValueStructure;

public class HAPManualPartInValueContextSimple extends HAPManualPartInValueContext{

	public static final String STRUCTURE = "structure";
	
	private List<HAPManualWrapperStructure> m_structureWrappers;
	
	public HAPManualPartInValueContextSimple(HAPManualInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_structureWrappers = new ArrayList<HAPManualWrapperStructure>();
	}

	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public List<HAPManualWrapperStructure> getValueStructures(){    return this.m_structureWrappers;    }
	public void addValueStructure(HAPManualWrapperStructure valueStructure) {   
		if(valueStructure!=null) {
			this.m_structureWrappers.add(valueStructure);
		}   
	}

/*	
	@Override
	public HAPManualPartInValueContext inheritValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		HAPManualPartInValueContextSimple out = new HAPManualPartInValueContextSimple(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);

		if(mode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			return out;
		}

		for(HAPManualWrapperStructure valueStructure : this.m_valueStructures) {
			if(groupTypeCandidates==null||groupTypeCandidates.length==0||Arrays.asList(groupTypeCandidates).contains(valueStructure.getGroupType())) {
				HAPManualWrapperStructure cloned = null;
				if(mode.equals(HAPConstantShared.INHERITMODE_RUNTIME)) {
					cloned = valueStructure.cloneValueStructureWrapper();
				}
				else if(mode.equals(HAPConstantShared.INHERITMODE_DEFINITION)) {
					cloned = valueStructure.cloneValueStructureWrapper();
					cloned.setValueStructureRuntimeId(valueStructureDomain.cloneRuntime(valueStructure.getValueStructureRuntimeId()));
				}
				else if(mode.equals(HAPConstantShared.INHERITMODE_REFER)) {
//					cloned = valueStructure.cloneValueStructureWrapper();
//					cloned.setValueStructureRuntimeId(valueStructureDomain.createRuntimeByRelativeRef(valueStructure.getValueStructureRuntimeId()));
				}
				out.addValueStructure(cloned);
			}
		}
		return out;
	}
*/	
	
	@Override
	public void cleanValueStucture(Set<String> valueStrucutreIds) {
		for(int i=0; i<m_structureWrappers.size(); i++) {
			if(valueStrucutreIds.contains(this.m_structureWrappers.get(i).getValueStructureRuntimeId())) {
				this.m_structureWrappers.remove(i);
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return this.m_structureWrappers.isEmpty();
	}

	@Override
	public boolean isEmptyOfValueStructure(HAPDomainValueStructure valueStructureDomain) {
		if(!this.m_structureWrappers.isEmpty() && valueStructureDomain==null) {
			return false;
		}
		
		for(HAPManualWrapperStructure vsInfo : this.m_structureWrappers) {
			boolean isEmpty = valueStructureDomain.getStructureDefinitionByRuntimeId(vsInfo.getValueStructureRuntimeId()).isEmpty();
			if(!isEmpty) {
				return false;
			}
		}
		return true;
	}

	@Override
	public HAPManualPartInValueContext cloneValueContextPart() {
		HAPManualPartInValueContextSimple out = new HAPManualPartInValueContextSimple(this.getPartInfo().cloneValueStructurePartInfo());
		this.cloneToPartValueContext(out);
		for(HAPManualWrapperStructure valueStructureWrapper : this.m_structureWrappers) {
			out.m_structureWrappers.add(valueStructureWrapper.cloneValueStructureWrapper());
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPManualWrapperStructure valueStructure : this.m_structureWrappers) {
			valueStructureJsonArray.add(valueStructure.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(STRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		List<String> arrayJson = new ArrayList<String>();
		for(HAPManualWrapperStructure valueStructure : this.m_structureWrappers) {
			arrayJson.add(valueStructure.toExpandedString(valueStructureDomain));
		}
		return HAPUtilityJson.buildArrayJson(arrayJson.toArray(new String[0]));
	}
}
