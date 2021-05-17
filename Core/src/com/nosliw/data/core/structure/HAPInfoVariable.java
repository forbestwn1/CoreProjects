package com.nosliw.data.core.structure;

import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

public class HAPInfoVariable {

	private HAPInfoCriteria m_criteriaInfo;
	
	private HAPComplexPath m_idPath;
	
	private Set<String> m_rootAliases;
	
	public HAPInfoVariable(HAPInfoCriteria criteriaInfo, HAPComplexPath idPath, Set<String> rootAliases) {
		this.m_criteriaInfo = criteriaInfo;
		this.m_idPath = idPath;
		this.m_rootAliases = rootAliases;
	}

	public Set<String> getRootAliases(){   return this.m_rootAliases;     }
	
	public String getPrincipleRootAlias(){   return this.m_rootAliases.iterator().next();    }
	
	public HAPInfoCriteria getCriteriaInfo() {   return this.m_criteriaInfo;    }
	
	public HAPComplexPath getIdPath() {   return this.m_idPath;    }
	
	public HAPPath getSubPath() {  return this.m_idPath.getPath();    }
}
