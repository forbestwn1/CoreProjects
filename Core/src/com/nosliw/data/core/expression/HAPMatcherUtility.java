package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationshipImp;

public class HAPMatcherUtility {

	public static HAPMatchers reversMatchers(HAPMatchers matchers) {
		HAPMatchers out = new HAPMatchers();
		
		Map<HAPDataTypeId, HAPMatcher> matcherMap = matchers.getMatchers();
		for(HAPDataTypeId dataTypeId : matcherMap.keySet()){
			HAPMatcher originalMatcher = matcherMap.get(dataTypeId);
			
			HAPDataTypeId matcherDataTypeId = originalMatcher.getRelationship().getTarget();
			
			HAPRelationshipImp relationship = new HAPRelationshipImp(originalMatcher.getRelationship().getTarget(), originalMatcher.getRelationship().getSource(), originalMatcher.getRelationship().getPath().reverse());
			
			HAPMatcher matcher = new HAPMatcher(matcherDataTypeId, relationship, true);
			
			Map<String, HAPMatchers> subMatchers = originalMatcher.getSubMatchers();
			for(String name : subMatchers.keySet()) {
				matcher.addSubMatchers(name, HAPMatcherUtility.reversMatchers(matchers));
			}
			out.addMatcher(matcher);
		}
		return out;
	}
}
