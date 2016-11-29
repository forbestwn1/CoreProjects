package com.nosliw.data.datatype.importer;

import java.util.ArrayList;
import java.util.List;

public class HAPDataTypePictureElement {

	private List<HAPDataTypePathSegment> m_pathSegs;

	private HAPDataTypeImp m_dataTypeImp;

	public HAPDataTypePictureElement(HAPDataTypeImp dataType){
		this.m_dataTypeImp = dataType;
		this.m_pathSegs = new ArrayList<HAPDataTypePathSegment>();
	}
	
	public HAPDataTypeImp getDataType(){
		return this.m_dataTypeImp;
	}
	
	public List<HAPDataTypePathSegment> getPathSegments(){
		return this.m_pathSegs;
	}
	
	public void appendPathSegment(HAPDataTypePathSegment segment){
		this.m_pathSegs.add(0, segment);
	}
	
	public HAPDataTypePictureElement extendPathSegment(HAPDataTypePathSegment segment){
		HAPDataTypePictureElement out = new HAPDataTypePictureElement(this.m_dataTypeImp);
		out.m_pathSegs.addAll(this.m_pathSegs);
		out.appendPathSegment(segment);
		return out;
	}
}
