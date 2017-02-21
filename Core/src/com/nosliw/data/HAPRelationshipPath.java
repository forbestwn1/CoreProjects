package com.nosliw.data;

import java.util.ArrayList;
import java.util.List;

public class HAPRelationshipPath {

	private List<HAPRelationshipPathSegment> m_segments = null;
	
	public HAPRelationshipPath(){
		this.m_segments = new ArrayList<HAPRelationshipPathSegment>();
	}

	public List<HAPRelationshipPathSegment> getSegments(){
		return this.m_segments;
	}
	
	public void addSegment(HAPRelationshipPathSegment segment){
		this.m_segments.add(segment);
	}
	
}
