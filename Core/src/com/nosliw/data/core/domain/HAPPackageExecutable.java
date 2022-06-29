package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

//executable package
@HAPEntityWithAttribute
public class HAPPackageExecutable extends HAPExecutableImp{

	@HAPAttribute
	public static final String MAINENTITYID = "mainEntityId";
	@HAPAttribute
	public static final String DEPENDENCY = "dependency";
	
	//complex entity resource dependency
	private Set<HAPResourceIdSimple> m_dependency;
	
	//main global entity
	private HAPIdComplexEntityInGlobal m_mainEntityId;
	
	public HAPPackageExecutable() {
		this.m_dependency = new HashSet<HAPResourceIdSimple>();
	}
	
	public Set<HAPResourceIdSimple> getDependency() {     return this.m_dependency;      }
	public void addDependency(HAPResourceIdSimple resourceId) {   this.m_dependency.add(resourceId);   }
	
	public HAPIdComplexEntityInGlobal getMainEntityId() {    return this.m_mainEntityId;     }
	
	public void setMainEntityId(HAPIdComplexEntityInGlobal mainEntityId) {    this.m_mainEntityId = mainEntityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MAINENTITYID, this.m_mainEntityId.toStringValue(HAPSerializationFormat.JSON));
		List<String> dependencyArray = new ArrayList<String>();
		for(HAPResourceIdSimple resourceId : this.m_dependency) {
			dependencyArray.add(resourceId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(DEPENDENCY, HAPJsonUtility.buildArrayJson(dependencyArray.toArray(new String[0])));
	}
}
