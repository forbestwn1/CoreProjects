package com.nosliw.data.core;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.literate.HAPLiterateManager;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;

public class HAPRelationshipPath extends HAPSerializableImp{

	protected List<HAPRelationshipPathSegment> m_segments = null;
	
	public HAPRelationshipPath(){
		this.m_segments = new ArrayList<HAPRelationshipPathSegment>();
	}

	public HAPRelationshipPath reverse() {
		HAPRelationshipPath out = new HAPRelationshipPath();
		for(HAPRelationshipPathSegment seg : this.m_segments) {
			out.m_segments.add(0, seg);
		}
		return out;
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
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		this.m_segments = (List<HAPRelationshipPathSegment>)HAPLiterateManager.getInstance().stringToValue((String)value, HAPConstant.STRINGABLE_ATOMICVALUETYPE_ARRAY, HAPRelationshipPathSegment.class.getName());
		return true;
	} 

	public HAPRelationshipPath clone(){
		HAPRelationshipPath out = new HAPRelationshipPath();
		out.m_segments.addAll(this.m_segments);
		return out;
	}
	
}
