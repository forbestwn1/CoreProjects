package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPManualPartInValueContextSimple extends HAPManualPartInValueContext{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPManualInfoValueStructure> m_valueStructures;
	
	public HAPManualPartInValueContextSimple(HAPManualInfoPartInValueContext partInfo) {
		super(partInfo);
		this.m_valueStructures = new ArrayList<HAPManualInfoValueStructure>();
	}

	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public List<HAPManualInfoValueStructure> getValueStructures(){    return this.m_valueStructures;    }
	public void addValueStructure(HAPManualInfoValueStructure valueStructure) {   
		if(valueStructure!=null) {
			this.m_valueStructures.add(valueStructure);
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

		for(HAPManualInfoValueStructure valueStructure : this.m_valueStructures) {
			if(groupTypeCandidates==null||groupTypeCandidates.length==0||Arrays.asList(groupTypeCandidates).contains(valueStructure.getGroupType())) {
				HAPManualInfoValueStructure cloned = null;
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
		for(int i=0; i<m_valueStructures.size(); i++) {
			if(valueStrucutreIds.contains(this.m_valueStructures.get(i).getValueStructureRuntimeId())) {
				this.m_valueStructures.remove(i);
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return this.m_valueStructures.isEmpty();
	}

	@Override
	public boolean isEmptyOfValueStructure(HAPDomainValueStructure valueStructureDomain) {
		if(!this.m_valueStructures.isEmpty() && valueStructureDomain==null) {
			return false;
		}
		
		for(HAPManualInfoValueStructure vsInfo : this.m_valueStructures) {
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
		for(HAPManualInfoValueStructure valueStructureWrapper : this.m_valueStructures) {
			out.m_valueStructures.add(valueStructureWrapper.cloneValueStructureWrapper());
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPManualInfoValueStructure valueStructure : this.m_valueStructures) {
			valueStructureJsonArray.add(valueStructure.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		List<String> arrayJson = new ArrayList<String>();
		for(HAPManualInfoValueStructure valueStructure : this.m_valueStructures) {
			arrayJson.add(valueStructure.toExpandedString(valueStructureDomain));
		}
		return HAPUtilityJson.buildArrayJson(arrayJson.toArray(new String[0]));
	}
}
