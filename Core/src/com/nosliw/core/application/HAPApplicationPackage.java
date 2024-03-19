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
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutableImp;

//a package is all information for a resource runtime, it include resource id and all resource bundles required
@HAPEntityWithAttribute
public class HAPApplicationPackage extends HAPExecutableImp{

	@HAPAttribute
	public static final String MAINENTITYREF = "mainEntityRef";
	@HAPAttribute
	public static final String DEPENDENCY = "dependency";
	
	//complex entity resource dependency
	private Set<HAPResourceIdSimple> m_dependency;
	
	//main global entity
	private HAPInfoResourceIdNormalize m_mainEntityRef;
	
	public HAPApplicationPackage() {
		this.m_dependency = new HashSet<HAPResourceIdSimple>();
	}
	
	public Set<HAPResourceIdSimple> getDependency() {     return this.m_dependency;      }
	public void addDependency(HAPResourceIdSimple resourceId) {   this.m_dependency.add(resourceId);   }
	
	public HAPInfoResourceIdNormalize getMainEntityId() {    return this.m_mainEntityRef;     }
	
	public void setMainEntityId(HAPInfoResourceIdNormalize mainEntityId) {    this.m_mainEntityRef = mainEntityId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MAINENTITYREF, this.m_mainEntityRef.toStringValue(HAPSerializationFormat.JSON));
		List<String> dependencyArray = new ArrayList<String>();
		for(HAPResourceIdSimple resourceId : this.m_dependency) {
			dependencyArray.add(resourceId.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(DEPENDENCY, HAPUtilityJson.buildArrayJson(dependencyArray.toArray(new String[0])));
	}

}
