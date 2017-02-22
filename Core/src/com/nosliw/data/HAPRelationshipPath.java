package com.nosliw.data;

import java.util.ArrayList;
import java.util.List;

public class HAPRelationshipPath {

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
	
	public void setPath(HAPRelationshipPath path){
		this.m_segments.clear();
		this.m_segments.addAll(path.getSegments());
	}
	
}
