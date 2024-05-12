package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPExecutableImp;

//a package is all information for a resource runtime, it include resource id and all resource bundles required
@HAPEntityWithAttribute
public class HAPApplicationPackage extends HAPExecutableImp{

	@HAPAttribute
	public static final String MAINRESOURCEID = "mainResourceId";
	@HAPAttribute
	public static final String DEPENDENCY = "dependency";
	
	//complex entity resource dependency
	private Set<HAPResourceId> m_dependency;
	
	//main global entity
	private HAPResourceId m_mainResourceId;
	
	public HAPApplicationPackage() {
		this.m_dependency = new HashSet<HAPResourceId>();
	}
	
	public Set<HAPResourceId> getDependency() {     return this.m_dependency;      }
	public void addDependency(HAPResourceId resourceId) {   this.m_dependency.add(resourceId);   }
	
	public HAPResourceId getMainResourceId() {    return this.m_mainResourceId;     }
	public void setMainResourceId(HAPResourceId resourceId) {    this.m_mainResourceId = resourceId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MAINRESOURCEID, this.m_mainResourceId.toStringValue(HAPSerializationFormat.JSON));
		List<String> dependencyArray = new ArrayList<String>();
		for(HAPResourceId resourceId : this.m_dependency) {
			dependencyArray.add(resourceId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(DEPENDENCY, HAPUtilityJson.buildArrayJson(dependencyArray.toArray(new String[0])));
	}

}
