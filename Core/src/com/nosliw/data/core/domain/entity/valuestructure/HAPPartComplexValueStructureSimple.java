package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;

public class HAPPartComplexValueStructureSimple extends HAPPartComplexValueStructure{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private List<HAPWrapperValueStructureExecutable> m_valueStructures;
	
	public HAPPartComplexValueStructureSimple(HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_valueStructures = new ArrayList<HAPWrapperValueStructureExecutable>();
	}

	public HAPPartComplexValueStructureSimple() {
		this.m_valueStructures = new ArrayList<HAPWrapperValueStructureExecutable>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public List<HAPWrapperValueStructureExecutable> getValueStructures(){    return this.m_valueStructures;    }
	public void addValueStructure(HAPWrapperValueStructureExecutable valueStructure) {   this.m_valueStructures.add(valueStructure);   }
	
	public HAPPartComplexValueStructureSimple cloneValueStructureComplexPartSimple(HAPDomainValueStructure valueStructureDomain, String mode) {
		HAPPartComplexValueStructureSimple out = new HAPPartComplexValueStructureSimple(this.getPartInfo());
		this.cloneToEntityInfo(out);
		for(HAPWrapperValueStructureExecutable valueStructure : this.m_valueStructures) {
			HAPWrapperValueStructureExecutable cloned = null;
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
			out.m_valueStructures.add(cloned);
		}
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart(HAPDomainValueStructure valueStructureDomain, String mode) {
		return this.cloneValueStructureComplexPartSimple(valueStructureDomain, mode);  
	}
	
	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() {
		HAPPartComplexValueStructureSimple out = new HAPPartComplexValueStructureSimple(this.getPartInfo().cloneValueStructurePartInfo());
		for(HAPWrapperValueStructureExecutable valueStructureWrapper : this.m_valueStructures) {
			out.m_valueStructures.add(valueStructureWrapper.cloneValueStructureWrapper());
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPWrapperValueStructureExecutable valueStructure : this.m_valueStructures) {
			valueStructureJsonArray.add(valueStructure.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(VALUESTRUCTURE, HAPJsonUtility.buildArrayJson(valueStructureJsonArray.toArray(new String[0])));
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		List<String> arrayJson = new ArrayList<String>();
		for(HAPWrapperValueStructureExecutable valueStructure : this.m_valueStructures) {
			arrayJson.add(valueStructure.toExpandedString(valueStructureDomain));
		}
		return HAPJsonUtility.buildArrayJson(arrayJson.toArray(new String[0]));
	}
}
