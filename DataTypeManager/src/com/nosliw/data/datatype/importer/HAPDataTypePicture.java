package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPDataTypePicture {

	private HAPDataTypeImp m_mainDataType;
	
	private Map<String, HAPDataTypePictureElement> m_dataTypeElements;
	
	public HAPDataTypePicture(HAPDataTypeImp mainDataType){
		this.m_mainDataType = mainDataType;
		this.m_dataTypeElements = new LinkedHashMap<String, HAPDataTypePictureElement>();
	}
	
	public HAPDataTypeImp getMainDataType(){
		return this.m_mainDataType;
	}
	
	public Set<HAPDataTypePictureElement> getDataTypeElements(){
		Set<HAPDataTypePictureElement> out = new HashSet<HAPDataTypePictureElement>();
		out.addAll(this.m_dataTypeElements.values());
		return out;
	}
	
	public HAPDataTypePictureElement getElement(String dataTypeId){
		return this.m_dataTypeElements.get(dataTypeId);
	}

	public void addElement(HAPDataTypePictureElement ele){
		this.m_dataTypeElements.put(ele.getDataType().getId(), ele);
	}
	
}
