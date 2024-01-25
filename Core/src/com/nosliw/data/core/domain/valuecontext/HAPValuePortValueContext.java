package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPInfoValueStructureDefinition;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.valueport.HAPInfoValuePort;
import com.nosliw.data.core.domain.valueport.HAPReferenceValueStructure;
import com.nosliw.data.core.domain.valueport.HAPValuePortImp;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public class HAPValuePortValueContext extends HAPValuePortImp{

	private HAPExecutableEntityValueContext m_valueContext;
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPValuePortValueContext(HAPExecutableEntityComplex complexEntityExe, HAPDomainValueStructure valueStructureDomain) {
		this(complexEntityExe, valueStructureDomain, false);
	}

	public HAPValuePortValueContext(HAPExecutableEntityComplex complexEntityExe, HAPDomainValueStructure valueStructureDomain, boolean isDefault) {
		super(HAPUtilityValuePort.createValuePortIdValueContext(complexEntityExe), new HAPInfoValuePort(), isDefault);
		this.m_valueContext = complexEntityExe.getValueContext();
		this.m_valueStructureDomain = valueStructureDomain;
	}

	@Override
	public List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria) {
		List<HAPInfoValueStructureReference> out = new ArrayList<HAPInfoValueStructureReference>();
		
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructuresSorted(m_valueContext);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			HAPWrapperExecutableValueStructure wraper = valueStructureInfo.getValueStructure();
			boolean isValid = true;

			HAPInfoValueStructureDefinition valueStructureDefInfo = m_valueStructureDomain.getValueStructureDefInfoByRuntimeId(wraper.getValueStructureRuntimeId());

			//check runtime id
			if(isValid) {
				String valueStructueDefId = valueStructureCriteria==null? null : valueStructureCriteria.getId();
				if(valueStructueDefId!=null) {
					if(!valueStructueDefId.equals(wraper.getValueStructureRuntimeId())){
						isValid = false;
					}
				}
			}
			
			//check group type
//			if(isValid) {
//				if(m_groupTypes!=null&&!m_groupTypes.isEmpty()) {
//					if(!m_groupTypes.contains(wraper.getGroupType())) {
//						isValid = false;
//					}
//				}
//			}

			//check name
			if(isValid) {
				String valueStructureName = valueStructureCriteria==null? null : valueStructureCriteria.getName();
				if(valueStructureName!=null) {
					if(!valueStructureDefInfo.getExtraInfo().getName().equals(valueStructureName)){
						isValid = false;
					}
				}
			}
			
			if(isValid) {
				String id = wraper.getValueStructureRuntimeId();
				out.add(new HAPInfoValueStructureReference(id, m_valueStructureDomain.getValueStructureDefinitionByRuntimeId(id)));
			}
		}
		return out;
	}

	@Override
	public HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId) {
		return this.m_valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureId);
	}

}
