package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;

public abstract class HAPExecutableScriptWithSegmentImp implements HAPExecutableScriptWithSegment{

	private String m_id;
	
	private List<HAPExecutableScript> m_segs;

	public HAPExecutableScriptWithSegmentImp(String id) {
		this.m_segs = new ArrayList<HAPExecutableScript>();
		this.m_id = id;
	}
	
	@Override
	public String getId() {    return this.m_id;    }
	
	@Override
	public void addSegment(HAPExecutableScript segment) {    this.m_segs.add(segment);   }
	@Override
	public void addSegments(List<HAPExecutableScript> segments) {   this.m_segs.addAll(segments);    }
	
	@Override
	public List<HAPExecutableScript> getSegments(){    return this.m_segs;     }

}
