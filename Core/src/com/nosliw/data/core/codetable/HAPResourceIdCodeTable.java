package com.nosliw.data.core.codetable;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPResourceIdCodeTable extends HAPResourceIdSimple{

	private HAPCodeTableId m_codeTableId; 
	
	public HAPResourceIdCodeTable(){	super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CODETABLE);	}

	public HAPResourceIdCodeTable(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdCodeTable(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdCodeTable(HAPCodeTableId codeTableId){
		this();
		init(null, null);
		this.m_codeTableId = codeTableId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(codeTableId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public void setId(String id){
		super.setId(id);
		this.m_codeTableId = new HAPCodeTableId(id);
	}

	public HAPCodeTableId getCodeTableId(){  return this.m_codeTableId;	}
	
	@Override
	public HAPResourceIdCodeTable clone(){
		HAPResourceIdCodeTable out = new HAPResourceIdCodeTable();
		out.cloneFrom(this);
		return out;
	}

}
