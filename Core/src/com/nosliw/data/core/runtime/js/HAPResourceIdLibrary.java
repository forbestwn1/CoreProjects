package com.nosliw.data.core.runtime.js;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdLibrary extends HAPResourceId{

	private HAPJSLibraryId m_jsLibraryId; 
	
	public HAPResourceIdLibrary(){}
	
	public HAPResourceIdLibrary(String idLiterate, String alias) {
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY, idLiterate, alias);
	}

	public HAPResourceIdLibrary(HAPJSLibraryId operationId, String alias){
		super(HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY, null, alias);
		this.setLibraryId(operationId);
	}
	
	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_jsLibraryId = new HAPJSLibraryId(id);
	}

	public HAPJSLibraryId getLibraryId(){  return this.m_jsLibraryId;	}
	protected void setLibraryId(HAPJSLibraryId libraryId){
		this.m_jsLibraryId = libraryId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(libraryId, HAPSerializationFormat.LITERATE); 
	}

	public HAPResourceIdLibrary clone(){
		HAPResourceIdLibrary out = new HAPResourceIdLibrary();
		out.cloneFrom(this);
		return out;
	}

	protected void cloneFrom(HAPResourceIdLibrary resourceId){
		super.cloneFrom(resourceId);
		this.m_jsLibraryId = resourceId.m_jsLibraryId;
	}
	
}
