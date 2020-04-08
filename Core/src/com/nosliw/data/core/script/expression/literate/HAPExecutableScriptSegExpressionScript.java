package com.nosliw.data.core.script.expression.literate;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPExecutableScript;

public class HAPExecutableScriptSegExpressionScript implements HAPExecutableScript{

	private String m_id;
	
	private List<HAPExecutableScript> m_segs;

	public HAPExecutableScriptSegExpressionScript(String id) {
		this.m_segs = new ArrayList<HAPExecutableScript>();
		this.m_id = id;
	}
	
	@Override
	public String getId() {    return this.m_id;    }
	
	@Override
	public String getScriptType() {   return HAPConstant.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT;  }

	public void addSegment(HAPExecutableScript segment) {    this.m_segs.add(segment);   }
	public void addSegments(List<HAPExecutableScript> segments) {   this.m_segs.addAll(segments);    }
	
	public List<HAPExecutableScript> getSegments(){    return this.m_segs;     }
	
}
