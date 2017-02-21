package com.nosliw.data.datatype.importer;

import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPRelationshipPath;
import com.nosliw.data.HAPRelationshipPathSegment;

public class HAPRelationshipImp extends HAPDataTypeImp implements HAPRelationship{

	private String m_id;
	
	private HAPDataTypeIdImp m_sourceDataType; 
	
	private HAPRelationshipPathImp m_path;
	
	@Override
	public HAPDataType getTargetDataType() {		return this;	}

	@Override
	public HAPRelationshipPath getPath() {		return this.m_path;	}

	public HAPDataTypeIdImp getSource(){		return this.m_sourceDataType;	}

	public void setSource(HAPDataTypeIdImp source){  this.m_sourceDataType=source;  }
	public void setId(String id){  this.m_id = id;  }
	public void setPath(HAPRelationshipPathImp path){  this.m_path = path; }
	
	public HAPRelationshipImp extendPathSegment(HAPRelationshipPathSegment segment, HAPDataTypeIdImp sourceId){
		HAPRelationshipImp out = (HAPRelationshipImp)this.clone();
		out.m_sourceDataType = sourceId;
		out.m_path = this.m_path.clone();
		out.m_path.insert(segment);
		return out;
	}
}
