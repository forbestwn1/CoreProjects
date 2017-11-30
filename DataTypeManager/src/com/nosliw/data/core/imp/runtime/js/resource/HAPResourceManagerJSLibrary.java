package com.nosliw.data.core.imp.runtime.js.resource;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.js.resource.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.resource.HAPResourceIdJSLibrary;

public class HAPResourceManagerJSLibrary extends HAPResourceManagerImp{

	private String m_baseFolder = HAPFileUtility.getJSFolder() + "libresources/";

	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdJSLibrary resourceLibraryId = new HAPResourceIdJSLibrary(resourceId);
		HAPJSLibraryId libraryId =  resourceLibraryId.getLibraryId();

		List<File> files = this.getLibraryFileName(libraryId);
		if(files==null || files.size()==0)   return null;
		
		List<URI> uris = new ArrayList<URI>();
		for(File file : files){
			uris.add(file.toURI());
		}
		HAPResourceDataJSLibrary libraryResourceData = new HAPResourceDataJSLibrary(uris);
		HAPResource resource = new HAPResource(resourceId, libraryResourceData, null);
		return resource;
	}
	
	private List<File> getLibraryFileName(HAPJSLibraryId libraryId){
		String path = libraryId.getName().replace(".", "/");
		String folder = m_baseFolder + path + (HAPBasicUtility.isStringEmpty(libraryId.getVersion()) ? "" : "/" + libraryId.getVersion());
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		List<File> out = new ArrayList<File>(files);
		//make file sorted by name
		Collections.sort(out);
		return out;
	}

}
