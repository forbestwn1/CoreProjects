package com.nosliw.core.xxx.application.common.structure;

import java.util.List;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

public class HAPInfoVariable {

	private HAPInfoCriteria m_criteriaInfo;
	
	private HAPComplexPath m_idPath;
	
	private List<HAPInfoAlias> m_rootAliases;
	
	public HAPInfoVariable(HAPInfoCriteria criteriaInfo, HAPComplexPath idPath, List<HAPInfoAlias> rootAliases) {
		this.m_criteriaInfo = criteriaInfo;
		this.m_idPath = idPath;
		this.m_rootAliases = rootAliases;
	}

	public List<HAPInfoAlias> getRootAliases(){   return this.m_rootAliases;     }
	
	public String getPrincipleRootAlias(){   return this.m_rootAliases.iterator().next().getName();    }
	
	public HAPInfoCriteria getCriteriaInfo() {   return this.m_criteriaInfo;    }
	
	public HAPComplexPath getIdPath() {   return this.m_idPath;    }
	
	public HAPPath getSubPath() {  return this.m_idPath.getPath();    }
}
