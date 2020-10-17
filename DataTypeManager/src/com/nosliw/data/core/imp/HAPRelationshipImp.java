package com.nosliw.data.core.imp;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPRelationship;
import com.nosliw.data.core.data.HAPRelationshipPath;
import com.nosliw.data.core.data.HAPRelationshipPathSegment;

public class HAPRelationshipImp extends HAPStringableValueEntityWithID implements HAPRelationship{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String SOURCEDATATYPE = "sourceDataType";
	@HAPAttribute
	public static String TARGETDATATYPE = "targetDataType";
	@HAPAttribute
	public static String PATH = "path";
	
	//type of target, for instance, root, self, intermedia
	@HAPAttribute
	public static String TARGETTYPE = "targetType";
	
	public HAPRelationshipImp(){
		this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPath());
	}
	
	@Override
	public HAPDataTypeId getTarget() {		
		return this.getTargetDataType().getName();	
	}
	
	@Override
	public HAPDataTypeId getSource(){	return this.getSourceDataType().getName();	}

	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPath)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPath.class);	}

	public String getTargetType(){  return this.getAtomicAncestorValueString(TARGETTYPE);  }
	
	public HAPDataTypeImp getTargetDataType() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(TARGETDATATYPE);	}

	public HAPDataTypeImp getSourceDataType() {		return (HAPDataTypeImp)this.getEntityAncestorByPath(SOURCEDATATYPE);	}
	
	public void setSourceDataType(HAPDataTypeImp source){  this.updateChild(SOURCEDATATYPE, source);  }
	public void setTargetDataType(HAPDataTypeImp source){  this.updateChild(TARGETDATATYPE, source);  }
	public void setPath(HAPRelationshipPath path){		this.getPath().setPath(path);	}
	public void setTargetType(String targetType){   this.updateAtomicChildStrValue(TARGETTYPE, targetType);  }
	
	public HAPRelationshipImp extendPathSegmentSource(HAPRelationshipPathSegment segment, HAPDataTypeImp source){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setSourceDataType(source);
		out.getPath().insert(segment);
		return out;
	}

	public HAPRelationshipImp extendPathSegmentTarget(HAPRelationshipPathSegment segment, HAPDataTypeImp target){
		HAPRelationshipImp out = this.clone(HAPRelationshipImp.class);
		out.setTargetDataType(target);
		out.getPath().append(segment);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SOURCE, HAPSerializeManager.getInstance().toStringValue(this.getSource(), HAPSerializationFormat.LITERATE));
		jsonMap.put(TARGET, HAPSerializeManager.getInstance().toStringValue(this.getTarget(), HAPSerializationFormat.LITERATE));
		jsonMap.put(PATH, HAPSerializeManager.getInstance().toStringValue(this.getPath(), HAPSerializationFormat.LITERATE));
		jsonMap.put(TARGETTYPE, this.getTargetType());
	}
	
}
