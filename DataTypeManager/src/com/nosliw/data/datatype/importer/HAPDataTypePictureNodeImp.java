package com.nosliw.data.datatype.importer;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypePathSegment;
import com.nosliw.data.HAPDataTypeRelationship;

public class HAPDataTypePictureNodeImp extends HAPStringableValueEntity implements HAPDataTypeRelationship{

	private List<HAPDataTypePathSegment> m_pathSegs;

	private HAPDataTypeImp m_dataTypeImp;

	public HAPDataTypePictureNodeImp(HAPDataTypeImp dataType){
		this.m_dataTypeImp = dataType;
		this.m_pathSegs = new ArrayList<HAPDataTypePathSegment>();
	}
	
	@Override
	public HAPDataType getTargetDataType(){
		return this.m_dataTypeImp;
	}
	
	@Override
	public List<HAPDataTypePathSegment> getPath(){
		return this.m_pathSegs;
	}
	
	@Override
	public void appendPathSegment(HAPDataTypePathSegment segment){
		this.m_pathSegs.add(0, segment);
	}
	
	@Override
	public HAPDataTypePictureNodeImp extendPathSegment(HAPDataTypePathSegment segment){
		HAPDataTypePictureNodeImp out = new HAPDataTypePictureNodeImp(this.m_dataTypeImp);
		out.m_pathSegs.addAll(this.m_pathSegs);
		out.appendPathSegment(segment);
		return out;
	}
}
