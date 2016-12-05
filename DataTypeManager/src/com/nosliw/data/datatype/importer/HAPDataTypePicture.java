package com.nosliw.data.datatype.importer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPDataTypePicture {

	private HAPDataTypeImp m_mainDataType;
	
	private Map<String, HAPDataTypePictureNodeImp> m_dataTypeElements;
	
	public HAPDataTypePicture(HAPDataTypeImp mainDataType){
		this.m_mainDataType = mainDataType;
		this.m_dataTypeElements = new LinkedHashMap<String, HAPDataTypePictureNodeImp>();
	}
	
	public HAPDataTypeImp getMainDataType(){
		return this.m_mainDataType;
	}
	
	public Set<HAPDataTypePictureNodeImp> getDataTypeNodes(){
		Set<HAPDataTypePictureNodeImp> out = new HashSet<HAPDataTypePictureNodeImp>();
		out.addAll(this.m_dataTypeElements.values());
		return out;
	}
	
	public HAPDataTypePictureNodeImp getNode(String dataTypeId){
		return this.m_dataTypeElements.get(dataTypeId);
	}

	public void addNode(HAPDataTypePictureNodeImp node){
		this.m_dataTypeElements.put(((HAPDataTypeImp)node.getDataType()).getId(), node);
	}
	
}
