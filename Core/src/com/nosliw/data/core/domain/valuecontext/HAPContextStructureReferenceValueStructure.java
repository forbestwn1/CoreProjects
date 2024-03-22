package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.core.application.common.structure.reference.HAPContextStructureReference;
import com.nosliw.core.application.common.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPInfoValueStructureDefinition;

public class HAPContextStructureReferenceValueStructure implements HAPContextStructureReference{

	private HAPExecutableEntityValueContext m_valueContext;
	private Set<String> m_groupTypes; 
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPContextStructureReferenceValueStructure(HAPExecutableEntityValueContext valueContext, Set<String> groupTypes, HAPDomainValueStructure valueStructureDomain) {
		this.m_valueContext = valueContext;
		this.m_groupTypes = groupTypes;
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
			if(isValid) {
				if(m_groupTypes!=null&&!m_groupTypes.isEmpty()) {
					if(!m_groupTypes.contains(wraper.getGroupType())) {
						isValid = false;
					}
				}
			}

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
