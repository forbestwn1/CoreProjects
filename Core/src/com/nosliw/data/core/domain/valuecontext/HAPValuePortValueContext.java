package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort;
import com.nosliw.core.application.valuecontext.HAPInfoValueStructure;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.core.application.valuestructure.HAPInfoValueStructureDefinition;
import com.nosliw.core.application.valuestructure.HAPInfoValueStructureRuntime;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public class HAPValuePortValueContext extends HAPValuePortImp{

	private HAPValueContext m_valueContext;
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPValuePortValueContext(HAPBrickBlockComplex complexEntityExe, HAPDomainValueStructure valueStructureDomain) {
		this(complexEntityExe, valueStructureDomain, false);
	}

	public HAPValuePortValueContext(HAPBrickBlockComplex complexEntityExe, HAPDomainValueStructure valueStructureDomain, boolean isDefault) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT), isDefault);
		this.m_valueContext = complexEntityExe.getValueContext();
		this.m_valueStructureDomain = valueStructureDomain;
	}
 
	@Override
	public List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure) {
		List<String> out = new ArrayList<String>();
		
		List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructuresSorted(m_valueContext);
		for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			HAPInfoValueStructure wraper = valueStructureInfo.getValueStructure();
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
//			if(isValid) {
//				String valueStructureName = valueStructureCriteria==null? null : valueStructureCriteria.getName();
//				if(valueStructureName!=null) {
//					if(!valueStructureDefInfo.getExtraInfo().getName().equals(valueStructureName)){
//						isValid = false;
//					}
//				}
//			}
			
			if(isValid) {
				String id = wraper.getValueStructureRuntimeId();
				out.add(id);
			}
		}
		return out;
	}

	@Override
	public HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort out = new HAPValueStructureInValuePort();
		HAPInfoValueStructureDefinition valueStructureDefInfo = this.m_valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureId);
		
		HAPInfoValueStructureRuntime valueStrcutreRuntimeInfo = m_valueStructureDomain.getValueStructureRuntimeInfo(valueStructureId);
		valueStrcutreRuntimeInfo.cloneToEntityInfo(out);
		
		for(HAPRootInValueStructure r : valueStructureDefInfo.getRoots()) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(r.getDefinition());
			r.cloneToEntityInfo(root);
			out.addRoot(root);
		}
		
		return out;
	}

}
