package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;

public class HAPMatchers extends HAPSerializableImp{

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
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(HAPDataTypeId dataTypeId : this.m_matchers.keySet()){
			HAPMatcher matcher = this.m_matchers.get(dataTypeId);
			jsonMap.put(HAPSerializeManager.getInstance().toStringValue(dataTypeId, HAPSerializationFormat.LITERATE),
					HAPSerializeManager.getInstance().toStringValue(matcher, HAPSerializationFormat.JSON));
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
			if(!HAPBasicUtility.isEqualMaps(this.m_matchers, matchers.m_matchers))  return false;
			out = true;
		}
		
		return out;
	}
}
