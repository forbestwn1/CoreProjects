package com.nosliw.data.core.matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPRelationship;
import com.nosliw.data.core.data.HAPRelationshipImp;
import com.nosliw.data.core.data.HAPRelationshipPathSegment;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;

public class HAPMatcherUtility {

	public static HAPMatchers cascadeMatchers(HAPMatchers matchers1, HAPMatchers matchers2) {
		if(matchers1==null)   return matchers2;
		if(matchers2==null)   return matchers1;
		 
		HAPMatchers out = new HAPMatchers();
		Map<HAPDataTypeId, HAPMatcher> ms1 = matchers1.getMatchers();
		Map<HAPDataTypeId, HAPMatcher> ms2 = matchers1.getMatchers();
		for(HAPDataTypeId dataTypeSource : ms1.keySet()) {
			HAPMatcher m1 = ms1.get(dataTypeSource);
			HAPMatcher m2 = ms2.get(m1.getRelationship().getTarget());
			if(m2!=null) {
				HAPMatcher outMatcher = new HAPMatcher(dataTypeSource, HAPUtilityData.cascadeRelationship(m1.getRelationship(), m2.getRelationship()));
				//sub matchers
				Map<String, HAPMatchers> subMatchers1 = m1.getSubMatchers();
				Map<String, HAPMatchers> subMatchers2 = m2.getSubMatchers();
				for(String sub1 : subMatchers1.keySet()) {
					HAPMatchers subMs1 = subMatchers1.get(sub1);
					HAPMatchers subMs2 = subMatchers2.get(sub1);
					if(subMs2!=null) {
						HAPMatchers outSubMatchers = cascadeMatchers(subMs1, subMs1);
						outMatcher.addSubMatchers(sub1, outSubMatchers);
					}
				}
				out.addMatcher(outMatcher);
			}
		}
		return out;
	}
	
	public static HAPMatchers reversMatchers(HAPMatchers matchers) {
		if(matchers==null)  return null;
		HAPMatchers out = new HAPMatchers();
		
		Map<HAPDataTypeId, HAPMatcher> matcherMap = matchers.getMatchers();
		for(HAPDataTypeId dataTypeId : matcherMap.keySet()){
			HAPMatcher originalMatcher = matcherMap.get(dataTypeId);
			
			HAPDataTypeId matcherDataTypeId = originalMatcher.getRelationship().getTarget();
			
			HAPRelationshipImp relationship = new HAPRelationshipImp(originalMatcher.getRelationship().getTarget(), originalMatcher.getRelationship().getSource(), originalMatcher.getRelationship().getPath().reverse(originalMatcher.getRelationship().getSource(), originalMatcher.getRelationship().getTarget()));
			
			HAPMatcher matcher = new HAPMatcher(matcherDataTypeId, relationship, !originalMatcher.isReverse());
			
			Map<String, HAPMatchers> subMatchers = originalMatcher.getSubMatchers();
			for(String name : subMatchers.keySet()) {
				matcher.addSubMatchers(name, HAPMatcherUtility.reversMatchers(matchers));
			}
			out.addMatcher(matcher);
		}
		return out;
	}
	
	public static List<HAPResourceIdSimple> getMatchersResourceId(HAPMatchers matchers) {
		List<HAPResourceIdSimple> out = new ArrayList<HAPResourceIdSimple>();
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
				case HAPConstantShared.DATATYPE_PATHSEGMENT_PARENT:
					baseDataType = (HAPDataTypeId)HAPManagerSerialize.getInstance().buildObject(HAPDataTypeId.class.getName(), segment.getId(), HAPSerializationFormat.LITERATE);
					break;
				case HAPConstantShared.DATATYPE_PATHSEGMENT_LINKED:
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
