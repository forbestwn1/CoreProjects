package com.nosliw.data.core.imp.runtime.js.resource;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.resource.HAPResourceIdJSLibrary;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginResourceManagerJSLibrary implements HAPPluginResourceManager{

	public HAPPluginResourceManagerJSLibrary() {	}
	
	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdJSLibrary resourceLibraryId = new HAPResourceIdJSLibrary(simpleResourceId);
		HAPJSLibraryId libraryId =  resourceLibraryId.getLibraryId();

		List<File> files = this.getLibraryFileName(libraryId);
		if(files==null || files.size()==0) {
			return null;
		}
		
		List<URI> uris = new ArrayList<URI>();
		for(File file : files){
			uris.add(file.toURI());
		}
		return new HAPResourceDataJSLibrary(uris);
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
