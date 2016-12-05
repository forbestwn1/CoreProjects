package com.nosliw.data;

import java.util.List;

public interface HAPDataTypePictureNode {

	HAPDataType getDataType();
	
	List<HAPDataTypePathSegment> getPathSegments();
	
	void appendPathSegment(HAPDataTypePathSegment segment);
	
	HAPDataTypePictureNode extendPathSegment(HAPDataTypePathSegment segment);
	
}
