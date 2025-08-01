package com.nosliw.core.resource.imp.jslibrary;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceIdSimple;

public class HAPResourceIdJSLibrary extends HAPResourceIdSimple{

	private HAPJSLibraryId m_jsLibraryId; 
	
	public HAPResourceIdJSLibrary(){   super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY, "1.0.0");      }
	
	public HAPResourceIdJSLibrary(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdJSLibrary(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdJSLibrary(HAPJSLibraryId libraryId){
		this();
		init(null, null);
		this.setLibraryId(libraryId);
	}
	
	@Override
	public void setId(String id){
		super.setId(id);
		this.m_jsLibraryId = new HAPJSLibraryId(id);
	}

	public HAPJSLibraryId getLibraryId(){  return this.m_jsLibraryId;	}
	protected void setLibraryId(HAPJSLibraryId libraryId){
		this.m_jsLibraryId = libraryId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(libraryId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public HAPResourceIdJSLibrary clone(){
		HAPResourceIdJSLibrary out = new HAPResourceIdJSLibrary();
		out.cloneFrom(this);
		return out;
	}

}
