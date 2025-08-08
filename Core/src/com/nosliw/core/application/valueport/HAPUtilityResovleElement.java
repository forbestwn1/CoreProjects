package com.nosliw.core.application.valueport;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.common.structure.HAPStructureImp;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.structure.reference.HAPUtilityResolveReference;

public class HAPUtilityResovleElement {

	public static HAPResultReferenceResolve analyzeElementReferenceValuePort(HAPReferenceElement reference, HAPValuePort valuePort, HAPConfigureResolveElementReference resolveConfigure, HAPDomainValueStructure valueStructureDomain) {
		HAPResultReferenceResolve resolve  = resolveReference(reference, valuePort, resolveConfigure, valueStructureDomain);
		if(resolve!=null) {
			resolve.valueStructureDomain = valueStructureDomain;
			resolve.valuePortId = reference.getValuePortId().getValuePortId();
			resolve.valuePortSide = reference.getValuePortId().getValuePortSide();
			resolve.elementPath = reference.getElementPath();
		}
		return resolve;
	}
	
	private static HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPValuePort valuePort, HAPConfigureResolveElementReference configure, HAPDomainValueStructure valueStructureDomain) {
		List<HAPInfoValueStructureReference> candiateValueStructures = new ArrayList<HAPInfoValueStructureReference>(); 
		List<String> candiateIds = discoverCandidateValueStructure(elementReference.getValueStructureReference(), valuePort, configure, valueStructureDomain);
		for(String valueStructureId : candiateIds) {
			candiateValueStructures.add(new HAPInfoValueStructureReference(valueStructureId, valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId)));
		}
		HAPResultReferenceResolve out = analyzeElementReference(elementReference.getElementPath(), candiateValueStructures, configure);
		
		if(out==null) {
			//extension
			if(configure==null||configure.isExtension()) {
//				String valueStructureForExtensionId = this.discoverCandidateValueStructure(configure==null?null:configure.getValueStructureForExtension(), configure).get(0);
//				out = extendValueStructure(valueStructureForExtensionId, elementReference.getElementPath(), new HAPElementStructureUnknown(), configure);
			}
			else {
				throw new RuntimeException();
			}
		}
		
		return out;
	}
	
	private static List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPValuePort valuePort, HAPConfigureResolveElementReference configure, HAPDomainValueStructure valueStructureDomain) {
		List<String> out = new ArrayList<String>();
		
		for(String valueStructureId : valuePort.getValueStructureIds()) {
			boolean isValid = true;

			HAPStructureImp valueStructureDefInfo = valueStructureDomain.getStructureDefinitionByRuntimeId(valueStructureId);

			//check runtime id
			if(isValid) {
				String valueStructueDefId = valueStructureCriteria==null? null : valueStructureCriteria.getId();
				if(valueStructueDefId!=null) {
					if(!valueStructueDefId.equals(valueStructureId)){
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
				String id = valueStructureId;
				out.add(id);
			}
		}
		return out;
	}
	
	
	//find best resolved element from structure 
	private static HAPResultReferenceResolve analyzeElementReference(String elementPath, List<HAPInfoValueStructureReference> targetStructures, HAPConfigureResolveElementReference resolveConfigure){
		if(targetStructures==null) {
			return null;
		}

		if(resolveConfigure==null) {
			resolveConfigure = new HAPConfigureResolveElementReference();
		}
		
		List<HAPResultReferenceResolve> resolveCandidates = new ArrayList<HAPResultReferenceResolve>();
		for(HAPInfoValueStructureReference valueStructureInfo : targetStructures) {
			HAPResultReferenceResolve resolved = HAPUtilityResolveReference.analyzeElementReference(elementPath, valueStructureInfo.getStructureDefinition(), resolveConfigure);
			if(resolved!=null) {
				resolved.structureId = valueStructureInfo.getValueStructureId();
				resolveCandidates.add(resolved);
				if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(resolveConfigure.searchMode)) {
					break;
				}
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPResultReferenceResolve out = null;
		int length = 99999;
		for(HAPResultReferenceResolve candidate : resolveCandidates) {
			HAPPath remainingPath = candidate.elementInfoSolid.remainPath;
			if(remainingPath.isEmpty()) {
				//all path solved
				out = candidate;
				break;
			}
			else {
				//some remaining path unsolved, find the shortest one 
				if(remainingPath.getLength()<length) {
					length = remainingPath.getLength();
					out = candidate;
				}
			}
		}
		return out;
	}
}

class HAPInfoValueStructureReference {

	private String m_id;
	
	private HAPStructureImp m_structure;
	
	public HAPInfoValueStructureReference(String id, HAPStructureImp structure) {
		this.m_id = id;
		this.m_structure = structure;
	}
	
	public String getValueStructureId() {    return this.m_id;      }
	
	public HAPStructureImp getStructureDefinition() {     return this.m_structure;       }

}

