package com.nosliw.core.xxx.application.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.xxx.application.common.structure.HAPElementStructureUnknown;

public abstract class HAPValuePortImp implements HAPValuePort1111{

	private HAPInfoValuePort m_valuePortInfo;
	
	public HAPValuePortImp(HAPInfoValuePort valuePortInfo) {
		this.m_valuePortInfo = valuePortInfo;
	}

	@Override
	public HAPInfoValuePort getValuePortInfo() {    return this.m_valuePortInfo;     }

	
	@Override
	public HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure) {
		List<HAPInfoValueStructureReference> candiateValueStructures = new ArrayList<HAPInfoValueStructureReference>(); 
		List<String> candiateIds = this.discoverCandidateValueStructure(elementReference.getValueStructureReference(), configure);
		for(String valueStructureId : candiateIds) {
			candiateValueStructures.add(new HAPInfoValueStructureReference(valueStructureId, this.getValueStructureDefintion(valueStructureId)));
		}
		HAPResultReferenceResolve out = this.analyzeElementReference(elementReference.getElementPath(), candiateValueStructures, configure);
		
		if(out==null) {
			//extension
			if(configure==null||configure.isExtension()) {
				String valueStructureForExtensionId = this.discoverCandidateValueStructure(configure==null?null:configure.getValueStructureForExtension(), configure).get(0);
				out = extendValueStructure(valueStructureForExtensionId, elementReference.getElementPath(), new HAPElementStructureUnknown(), configure);
			}
			else {
				throw new RuntimeException();
			}
		}
		
		return out;
	}
	
	protected abstract List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure);

	protected abstract HAPResultReferenceResolve extendValueStructure(String valueStructureInValuePort, String elementPath, HAPElementStructure structureEle, HAPConfigureResolveElementReference configure);
	
	//find best resolved element from structure 
	protected HAPResultReferenceResolve analyzeElementReference(String elementPath, List<HAPInfoValueStructureReference> targetStructures, HAPConfigureResolveElementReference resolveConfigure){
		if(targetStructures==null) {
			return null;
		}

		if(resolveConfigure==null) {
			resolveConfigure = new HAPConfigureResolveElementReference();
		}
		
		List<HAPResultReferenceResolve> resolveCandidates = new ArrayList<HAPResultReferenceResolve>();
		for(HAPInfoValueStructureReference valueStructureInfo : targetStructures) {
			HAPValueStructureInValuePort11111 valueStructure = valueStructureInfo.getStructureDefinition();
			HAPComplexPath complexPath = new HAPComplexPath(elementPath);
			String rootName = complexPath.getRoot();
			String path = complexPath.getPathStr();
			
			HAPRootStructureInValuePort root = valueStructure.getRootByName(rootName);
			if(root!=null) {
				HAPResultReferenceResolve resolved = new HAPResultReferenceResolve(); 
				resolved.structureId = valueStructureInfo.getValueStructureId();
				resolved.rootName = rootName;
				resolved.elementPath = path;
				resolved.fullPath = elementPath;

				resolved.elementInfoSolid = HAPUtilityElement.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
				if(resolved.elementInfoSolid!=null) {
					resolved.elementInfoOriginal = HAPUtilityElement.resolveDescendant(root.getDefinition(), path);
					
					Set<String> elementTypes = resolveConfigure.candidateElementTypes;
					if(elementTypes==null || elementTypes.contains(resolved.elementInfoSolid.resolvedElement.getType())) {
						resolveCandidates.add(resolved);
						if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(resolveConfigure.searchMode)) {
							break;
						}
					}
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
