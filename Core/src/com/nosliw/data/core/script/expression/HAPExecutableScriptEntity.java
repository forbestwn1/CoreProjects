package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public class HAPExecutableScriptEntity extends HAPExecutableImpEntityInfo implements HAPExecutableScript{

	private String m_id;
	
	private String m_scriptType;
	
	private List<HAPExecutableScript> m_segs;

	public HAPExecutableScriptEntity(String scriptType, String id) {
		this.m_segs = new ArrayList<HAPExecutableScript>();
		this.m_scriptType = scriptType;
		this.m_id = id;
	}
	
	@Override
	public String getScriptType() {  return this.m_scriptType;  }

	@Override
	public String getId() {   return this.m_id;  }

	public void addSegment(HAPExecutableScript segment) {    this.m_segs.add(segment);   }
	public void addSegments(List<HAPExecutableScript> segments) {   this.m_segs.addAll(segments);    }
	
	public List<HAPExecutableScript> getSegments(){    return this.m_segs;     }
}
