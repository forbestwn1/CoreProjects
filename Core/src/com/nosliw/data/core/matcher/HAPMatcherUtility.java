package com.nosliw.data.core.matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipImp;
import com.nosliw.data.core.HAPRelationshipPathSegment;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;

public class HAPMatcherUtility {

	public static HAPMatchers reversMatchers(HAPMatchers matchers) {
		HAPMatchers out = new HAPMatchers();
		
		Map<HAPDataTypeId, HAPMatcher> matcherMap = matchers.getMatchers();
		for(HAPDataTypeId dataTypeId : matcherMap.keySet()){
			HAPMatcher originalMatcher = matcherMap.get(dataTypeId);
			
			HAPDataTypeId matcherDataTypeId = originalMatcher.getRelationship().getTarget();
			
			HAPRelationshipImp relationship = new HAPRelationshipImp(originalMatcher.getRelationship().getTarget(), originalMatcher.getRelationship().getSource(), originalMatcher.getRelationship().getPath().reverse(originalMatcher.getRelationship().getSource(), originalMatcher.getRelationship().getTarget()));
			
			HAPMatcher matcher = new HAPMatcher(matcherDataTypeId, relationship, true);
			
			Map<String, HAPMatchers> subMatchers = originalMatcher.getSubMatchers();
			for(String name : subMatchers.keySet()) {
				matcher.addSubMatchers(name, HAPMatcherUtility.reversMatchers(matchers));
			}
			out.addMatcher(matcher);
		}
		return out;
	}
	
	public static List<HAPResourceId> getMatchersResourceId(HAPMatchers matchers) {
		List<HAPResourceId> out = new ArrayList<HAPResourceId>();
		Set<HAPDataTypeConverter> converters = getConverterResourceIdFromRelationship(matchers.discoverRelationships());
		for(HAPDataTypeConverter converter : converters){
			out.add(new HAPResourceIdConverter(converter));
		}
		return out;
	}
	
	public static Set<HAPDataTypeConverter> getConverterResourceIdFromRelationship(HAPRelationship relationship){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		
		List<HAPRelationshipPathSegment> segments = relationship.getPath().getSegments();
		if(segments!=null && segments.size()>=1){
			HAPDataTypeId baseDataType = relationship.getSource();
			for(HAPRelationshipPathSegment segment : segments){
				out.add(new HAPDataTypeConverter(baseDataType));
				
				String segmentType = segment.getType();
				switch(segmentType){
				case HAPConstant.DATATYPE_PATHSEGMENT_PARENT:
					baseDataType = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), segment.getId(), HAPSerializationFormat.LITERATE);
					break;
				case HAPConstant.DATATYPE_PATHSEGMENT_LINKED:
					baseDataType = new HAPDataTypeId(baseDataType.getName(), segment.getId());
					break;
				}
			}
		}
		return out;
	}

	public static Set<HAPDataTypeConverter> getConverterResourceIdFromRelationship(Set<HAPRelationship> relationships){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
		for(HAPRelationship relationship : relationships){
			out.addAll(getConverterResourceIdFromRelationship(relationship));
		}
		return out;
	}

}
