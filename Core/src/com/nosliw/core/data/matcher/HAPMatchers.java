package com.nosliw.core.data.matcher;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.HAPRelationship;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application1.HAPExecutableImp;

public class HAPMatchers extends HAPExecutableImp{

	private Map<HAPDataTypeId, HAPMatcher> m_matchers;
	
	public HAPMatchers(){
		this.m_matchers = new LinkedHashMap<HAPDataTypeId, HAPMatcher>();
	}

	public void addMatcher(HAPMatcher matcher){
		this.m_matchers.put(matcher.getDataTypeId(), matcher);
	}
	
	public Map<HAPDataTypeId, HAPMatcher> getMatchers(){
		return this.m_matchers;
	}
	
	public double getScore() {
		if(this.m_matchers.isEmpty()) {
			return 0;
		}
		double sum = 0;
		for(HAPMatcher matcher : this.m_matchers.values()) {
			sum = sum + matcher.getScore();
		}
		return sum / this.m_matchers.size();
	}
	
	public boolean isVoid(){
		boolean out = true;
		Iterator<HAPMatcher> it = this.m_matchers.values().iterator();
		while(it.hasNext()){
			HAPMatcher matcher = it.next();
			if(!matcher.isVoid()){
				out = false;
				break;
			}
		}
		return out;
	}
	
	/**
	 * Get all relationships invovled in this matchers 
	 */
	public Set<HAPRelationship> discoverRelationships(){
		Set<HAPRelationship> out = new HashSet<HAPRelationship>();
		for(HAPDataTypeId dataTypeId : this.m_matchers.keySet()){
			out.addAll(this.m_matchers.get(dataTypeId).discoverRelationships());
		}
		return out;
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		for(HAPResourceId resourceId : HAPMatcherUtility.getMatchersResourceId(this)) {
			dependency.add(new HAPResourceDependency(resourceId));
		}
	}

	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		for(Object key : jsonObj.keySet()) {
			HAPDataTypeId dataTypeId = new HAPDataTypeId();
			dataTypeId.buildObject(key, HAPSerializationFormat.LITERATE);
			HAPMatcher matcher = new HAPMatcher();
			matcher.buildObject(jsonObj.getJSONObject((String)key), HAPSerializationFormat.JSON);
		}
		return true;
	}
		
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(HAPDataTypeId dataTypeId : this.m_matchers.keySet()){
			HAPMatcher matcher = this.m_matchers.get(dataTypeId);
			jsonMap.put(HAPManagerSerialize.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE),
					HAPManagerSerialize.getInstance().toStringValue(matcher, HAPSerializationFormat.JSON));
		}
	}
	
	public HAPMatchers cloneMatchers(){
		HAPMatchers out = new HAPMatchers();
		for(HAPDataTypeId dataTypeId : this.m_matchers.keySet()){
			out.addMatcher(this.m_matchers.get(dataTypeId));
		}
		return out;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPMatchers) {
			HAPMatchers matchers = (HAPMatchers)obj;
			if(!HAPUtilityBasic.isEqualMaps(this.m_matchers, matchers.m_matchers)) {
				return false;
			}
			out = true;
		}
		
		return out;
	}
}
