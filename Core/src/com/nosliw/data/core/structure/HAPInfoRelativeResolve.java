package com.nosliw.data.core.structure;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;

public class HAPInfoRelativeResolve {

	//resolved structure runtime id
	private String m_structureId;
	//path after resolve (root id + path)
	private HAPComplexPath m_path;
	//unsolved path part
	private HAPPath m_remainPath;
	//final element, solid (maybe logic element which embeded in real element)
	private HAPElementStructure m_solidElement;

	public HAPInfoRelativeResolve(String structureId, HAPComplexPath path, HAPPath remainPath, HAPElementStructure element) {
		this.m_structureId = structureId;
		this.m_path = path;
		this.m_remainPath = remainPath;
		this.m_solidElement = element;
	}

	public String getResolvedStructureId() {   return this.m_structureId;    }
	
	public HAPComplexPath getResolvedElementPath() {    return this.m_path;    }
	
	public HAPPath getUnresolvedElementPath() {    return this.m_remainPath;    }
	
	public HAPElementStructure getSolidElement() {    return this.m_solidElement;    }
	
}
