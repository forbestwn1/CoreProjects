package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdJSLibrary extends HAPResourceId{

	private HAPJSLibraryId m_jsLibraryId; 
	
	public HAPResourceIdJSLibrary(){}
	
	public HAPResourceIdJSLibrary(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdJSLibrary(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, idLiterate, null);
	}

	public HAPResourceIdJSLibrary(HAPJSLibraryId libraryId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, null, null);
		this.setLibraryId(libraryId);
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

	@Override
	public HAPResourceIdJSLibrary clone(){
		HAPResourceIdJSLibrary out = new HAPResourceIdJSLibrary();
		out.cloneFrom(this);
		return out;
	}

}
