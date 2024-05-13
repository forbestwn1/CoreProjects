package com.nosliw.data.core.imp.runtime.js.resource;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.resource.HAPResourceIdJSLibrary;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceManagerJSLibrary extends HAPResourceManagerImp{

	public HAPResourceManagerJSLibrary(HAPResourceManager rootResourceMan) {
		super(rootResourceMan);
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdJSLibrary resourceLibraryId = new HAPResourceIdJSLibrary((HAPResourceIdSimple)resourceId);
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
		String folder = HAPSystemFolderUtility.getJSLibraryFolder() + path + (HAPUtilityBasic.isStringEmpty(libraryId.getVersion()) ? "" : "/" + libraryId.getVersion());
		Set<File> files = HAPUtilityFile.getAllFiles(folder);
		List<File> out = new ArrayList<File>(files);
		//make file sorted by name
		Collections.sort(out);
		return out;
	}

}
