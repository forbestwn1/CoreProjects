package com.nosliw.data;

import java.util.Set;

public interface HAPDataTypePicture {

	public HAPDataType getMainDataType();

	public Set<? extends HAPDataTypePictureNode> getDataTypeNodes();
	
	public HAPDataTypePictureNode getNode(HAPDataTypeInfo dataTypeInfo);
	
}
