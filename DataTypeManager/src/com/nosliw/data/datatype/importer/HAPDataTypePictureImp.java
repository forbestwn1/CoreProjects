package com.nosliw.data.datatype.importer;

import java.util.Set;

import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypePicture;
import com.nosliw.data.HAPDataTypePictureNode;

public class HAPDataTypePictureImp extends HAPDataTypeImp implements HAPDataTypePicture{

	public static String NODES = "nodes";
	
	public HAPDataTypePictureImp(HAPDataTypeImp mainDataType){
		super(mainDataType);
		this.updateChild(NODES, new HAPStringableValueMap<HAPDataTypePictureNodeImp>());
	}
	
	@Override
	public HAPDataType getMainDataType(){		return this;	}
	
	@Override
	public Set<? extends HAPDataTypePictureNode> getDataTypeNodes(){
		Set<HAPDataTypePictureNodeImp> out = this.getNodesMap().getValues(); 
		return out;
	}
	
	@Override
	public HAPDataTypePictureNodeImp getNode(HAPDataTypeInfo dataTypeInfo){
		return (HAPDataTypePictureNodeImp)this.getNodesMap().getChild(((HAPDataTypeInfoImp)dataTypeInfo).getId());
	}

	public void addNode(HAPDataTypePictureNodeImp node){
		this.getNodesMap().updateChild(((HAPDataTypeImp)node.getDataType()).getId(), node);
	}

	private HAPStringableValueMap<HAPDataTypePictureNodeImp> getNodesMap(){		return (HAPStringableValueMap<HAPDataTypePictureNodeImp>)this.getChild(NODES);	}
	
}
