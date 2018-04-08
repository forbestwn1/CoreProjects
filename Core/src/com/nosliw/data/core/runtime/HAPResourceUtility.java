package com.nosliw.data.core.runtime;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPResourceUtility {

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
