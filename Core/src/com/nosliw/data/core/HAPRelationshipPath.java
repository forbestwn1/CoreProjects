package com.nosliw.data.core;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public class HAPRelationshipPath extends HAPSerializableImp{

	protected List<HAPRelationshipPathSegment> m_segments = null;
	
	public HAPRelationshipPath(){
		this.m_segments = new ArrayList<HAPRelationshipPathSegment>();
	}

	public List<HAPRelationshipPathSegment> getSegments(){
		return this.m_segments;
	}
	
	public void addSegment(HAPRelationshipPathSegment segment){
		this.m_segments.add(segment);
	}

	public void insert(HAPRelationshipPathSegment segment){
		this.m_segments.add(0, segment);
	}

	public void append(HAPRelationshipPathSegment segment){
		this.m_segments.add(segment);
	}
	
	public void setPath(HAPRelationshipPath path){
		this.m_segments.clear();
		this.m_segments.addAll(path.getSegments());
	}

	@Override
	protected String buildLiterate(){  
		return HAPSerializeManager.getInstance().toStringValue(m_segments, HAPSerializationFormat.LITERATE); 
	}
}
