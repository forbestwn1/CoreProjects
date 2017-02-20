package com.nosliw.data.datatype.importer;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPDataTypeRelationship;

public class HAPDataTypePictureImp extends HAPDataTypeImp implements HAPDataTypePicture{

	public static String NODES = "nodes";
	
	public HAPDataTypePictureImp(HAPDataTypeImp mainDataType){
		super(mainDataType);
		this.updateChild(NODES, new HAPStringableValueMap<HAPDataTypePictureNodeImp>());
	}
	
	@Override
	public HAPDataType getSourceDataType(){		return this;	}
	
	@Override
	public Set<? extends HAPDataTypeRelationship> getRelationships(){
		Set<HAPDataTypePictureNodeImp> out = this.getNodesMap().getValues(); 
		return out;
	}
	
	@Override
	public HAPDataTypePictureNodeImp getRelationship(HAPDataTypeId dataTypeInfo){
		return (HAPDataTypePictureNodeImp)this.getNodesMap().getChild(((HAPDataTypeIdImp)dataTypeInfo).getId());
	}

	public void addNode(HAPDataTypePictureNodeImp node){
		this.getNodesMap().updateChild(((HAPDataTypeImp)node.getTargetDataType()).getId(), node);
	}

	private HAPStringableValueMap<HAPDataTypePictureNodeImp> getNodesMap(){		return (HAPStringableValueMap<HAPDataTypePictureNodeImp>)this.getChild(NODES);	}
	
}
