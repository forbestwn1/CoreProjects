package com.nosliw.data.core.complex.valuestructure;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPPartComplexValueStructureSimple extends HAPPartComplexValueStructure{

	//id for runtime, if two part have the same 
	private String m_runtimeId;

	private HAPValueStructure m_valueStructure;
	
	public HAPPartComplexValueStructureSimple(HAPValueStructure valueStructure, HAPInfoPartValueStructure partInfo) {
		super(partInfo);
		this.m_valueStructure = valueStructure;
	}
	
	public HAPPartComplexValueStructureSimple() {}
	
	@Override
	public String getPartType() {    return HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE;    }

	public String getRuntimeId() {     return this.m_runtimeId;     }
	
	public HAPValueStructure getValueStructure() {    return this.m_valueStructure;    }
	
	public String getValueStructureDefinitionId() {   return this.m_valueStructureDefinitionId;    }
	public HAPValueStructure setValueStructureDefinitionId(String id) {     
		this.m_valueStructureDefinitionId = id;
		HAPValueStructure out = this.m_valueStructure;
		this.m_valueStructure = null;
		return out;
	}

	public HAPPartComplexValueStructureSimple cloneValueStructureComplexPartSimple() {
		HAPPartComplexValueStructureSimple out = new HAPPartComplexValueStructureSimple();
		this.cloneToEntityInfo(out);
		out.m_valueStructure = this.m_valueStructure.cloneValueStructure();
		return out;
	}

	@Override
	public HAPPartComplexValueStructure cloneComplexValueStructurePart() { return this.cloneValueStructureComplexPartSimple();  }
	
}
