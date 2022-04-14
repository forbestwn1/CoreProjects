package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPPartComplexValueStructureSimple extends HAPPartComplexValueStructure{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	//id for runtime, if two part have the same 
	private String m_runtimeId;

	private List<HAPValueStructureWrapper> m_valueStructures;
	
	public HAPPartComplexValueStructureSimple(HAPValueStructureWrapper valueStructure, HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_valueStructures = new ArrayList<HAPValueStructureWrapper>();
		this.addValueStructure(valueStructure);
	}
	
	public HAPPartComplexValueStructureSimple() {
		this.m_valueStructures = new ArrayList<HAPValueStructureWrapper>();
	}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public void addValueStructure(HAPValueStructureWrapper valueStructure) {   this.m_valueStructures.add(valueStructure);   }
	
	
	public String getRuntimeId() {     return this.m_runtimeId;     }
	
	public HAPValueStructure getValueStructure() {    return this.m_valueStructures;    }
	
	public String getValueStructureDefinitionId() {   return this.m_valueStructureDefinitionId;    }
	public HAPValueStructure setValueStructureDefinitionId(String id) {     
		this.m_valueStructureDefinitionId = id;
		HAPValueStructure out = this.m_valueStructures;
		this.m_valueStructures = null;
		return out;
	}

	public HAPPartComplexValueStructureSimple cloneValueStructureComplexPartSimple() {
		HAPPartComplexValueStructureSimple out = new HAPPartComplexValueStructureSimple();
		this.cloneToEntityInfo(out);
		out.m_valueStructures = this.m_valueStructures.cloneValueStructure();
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() { return this.cloneValueStructureComplexPartSimple();  }

	@Override
	public String toExpandedJsonString(HAPDomainEntityDefinition entityDefDomain) {
		List<String> valueStructureJsonArray = new ArrayList<String>();
		for(HAPValueStructureWrapper valueStructure : this.m_valueStructures) {
			valueStructureJsonArray.add(valueStructure.toExpandedJsonString(entityDefDomain));
		}
		return HAPJsonUtility.buildArrayJson(valueStructureJsonArray.toArray(new String[0]));
	}

}
