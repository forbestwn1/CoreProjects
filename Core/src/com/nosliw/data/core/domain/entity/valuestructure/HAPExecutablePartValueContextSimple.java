package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPExecutablePartValueContextSimple extends HAPExecutablePartValueContext{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPWrapperExecutableValueStructure> m_valueStructures;
	
	public HAPExecutablePartValueContextSimple(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_valueStructures = new ArrayList<HAPWrapperExecutableValueStructure>();
	}

	public HAPExecutablePartValueContextSimple() {
		this.m_valueStructures = new ArrayList<HAPWrapperExecutableValueStructure>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public List<HAPWrapperExecutableValueStructure> getValueStructures(){    return this.m_valueStructures;    }
	public void addValueStructure(HAPWrapperExecutableValueStructure valueStructure) {   
		if(valueStructure!=null) this.m_valueStructures.add(valueStructure);   
	}
	
	public HAPExecutablePartValueContextSimple cloneValueContextPartSimple(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		HAPExecutablePartValueContextSimple out = new HAPExecutablePartValueContextSimple(this.getPartInfo());
		this.cloneToPartValueContext(out);

		if(mode.equals(HAPConstantShared.INHERITMODE_NONE))  return out;

		for(HAPWrapperExecutableValueStructure valueStructure : this.m_valueStructures) {
			if(groupTypeCandidates==null||groupTypeCandidates.length==0||Arrays.asList(groupTypeCandidates).contains(valueStructure.getGroupType())) {
				HAPWrapperExecutableValueStructure cloned = null;
				if(mode.equals(HAPConstantShared.INHERITMODE_RUNTIME)) {
					cloned = valueStructure.cloneValueStructureWrapper();
				}
				else if(mode.equals(HAPConstantShared.INHERITMODE_DEFINITION)) {
					cloned = valueStructure.cloneValueStructureWrapper();
					cloned.setValueStructureRuntimeId(valueStructureDomain.cloneRuntime(valueStructure.getValueStructureRuntimeId()));
				}
				else if(mode.equals(HAPConstantShared.INHERITMODE_REFER)) {
					cloned = valueStructure.cloneValueStructureWrapper();
					cloned.setValueStructureRuntimeId(valueStructureDomain.createRuntimeByRelativeRef(valueStructure.getValueStructureRuntimeId()));
				}
				out.addValueStructure(cloned);
			}
		}
		return out;
	}

	@Override
	public HAPExecutablePartValueContext cloneValueContextPart(HAPDomainValueStructure valueStructureDomain, String mode, String[] groupTypeCandidates) {
		return this.cloneValueContextPartSimple(valueStructureDomain, mode, groupTypeCandidates);  
	}
	
	@Override
	public HAPExecutablePartValueContext cloneValueContextPart() {
		HAPExecutablePartValueContextSimple out = new HAPExecutablePartValueContextSimple();
		this.cloneToPartValueContext(out);
		for(HAPWrapperExecutableValueStructure valueStructureWrapper : this.m_valueStructures) {
			out.m_valueStructures.add(valueStructureWrapper.cloneValueStructureWrapper());
		}
		return out;
	}

	@Override
	public boolean isEmpty() {	return this.m_valueStructures.isEmpty();	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPWrapperExecutableValueStructure valueStructure : this.m_valueStructures) {
			valueStructureJsonArray.add(valueStructure.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		List<String> arrayJson = new ArrayList<String>();
		for(HAPWrapperExecutableValueStructure valueStructure : this.m_valueStructures) {
			arrayJson.add(valueStructure.toExpandedString(valueStructureDomain));
		}
		return HAPUtilityJson.buildArrayJson(arrayJson.toArray(new String[0]));
	}
}
