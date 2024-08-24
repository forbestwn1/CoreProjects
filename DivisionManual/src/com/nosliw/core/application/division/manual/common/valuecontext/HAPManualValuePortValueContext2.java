package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort11111;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.core.application.valuestructure.HAPDefinitionStructure;
import com.nosliw.core.application.valuestructure.HAPInfoValueStructureRuntime;

public class HAPManualValuePortValueContext2 extends HAPValuePort{

	private HAPManualValueContext m_valueContext;
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPManualValuePortValueContext2(HAPManualBrick complexEntityExe, HAPDomainValueStructure valueStructureDomain) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.IO_DIRECTION_BOTH));
		this.m_valueContext = complexEntityExe.getManualValueContext();
		this.m_valueStructureDomain = valueStructureDomain;
	}
 
	@Override
	public List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure) {
		List<String> out = new ArrayList<String>();
		
		List<HAPManualInfoValueStructureSorting> valueStructureInfos = HAPManualUtilityValueContext.getAllValueStructuresSorted(m_valueContext);
		for(HAPManualInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
			HAPManualInfoValueStructure wraper = valueStructureInfo.getValueStructure();
			boolean isValid = true;

			HAPDefinitionStructure valueStructureDefInfo = m_valueStructureDomain.getStructureDefinitionByRuntimeId(wraper.getValueStructureRuntimeId());

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
	public HAPValueStructureInValuePort11111 getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort11111 out = new HAPValueStructureInValuePort11111();
		HAPDefinitionStructure valueStructureDefInfo = this.m_valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId);
		
		HAPInfoValueStructureRuntime valueStrcutreRuntimeInfo = m_valueStructureDomain.getValueStructureRuntimeInfo(valueStructureId);
		valueStrcutreRuntimeInfo.cloneToEntityInfo(out);
		
		for(HAPRootInStructure r : valueStructureDefInfo.getRoots()) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(r.getDefinition());
			r.cloneToEntityInfo(root);
			out.addRoot(root);
		}
		
		return out;
	}

	@Override
	public void updateElement(HAPIdElement elementId, HAPElementStructure structureElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HAPResultReferenceResolve extendValueStructure(String valueStructureInValuePort, String elementPath,
			HAPElementStructure structureEle, HAPConfigureResolveElementReference configure) {
		// TODO Auto-generated method stub
		return null;
	}

}
