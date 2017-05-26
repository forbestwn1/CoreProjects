package com.nosliw.data.core.imp.runtime.js;

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
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdJSLibrary;

public class HAPResourceManagerJSLibrary implements HAPResourceManager{

	private String m_baseFolder = "C:/Users/ewaniwa/Desktop/MyWork/CoreProjects/Application/WebContent/libresources";
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		
		for(HAPResourceId resourceId : resourcesId){
			HAPResourceIdJSLibrary resourceLibraryId = new HAPResourceIdJSLibrary(resourceId);
			HAPJSLibraryId libraryId =  resourceLibraryId.getLibraryId();

			List<File> files = this.getLibraryFileName(libraryId);
			List<URI> uris = new ArrayList<URI>();
			for(File file : files){
				uris.add(file.toURI());
			}
			HAPResourceDataJSLibrary libraryResourceData = new HAPResourceDataJSLibrary(uris);
			HAPResource resource = new HAPResource(resourceId, libraryResourceData, null);
			out.add(resource);
		}
		return out;
	}
	
	
	private List<File> getLibraryFileName(HAPJSLibraryId libraryId){
		String path = libraryId.getName().replace(".", "/");
		String folder = m_baseFolder + "/" + path + (HAPBasicUtility.isStringEmpty(libraryId.getVersion()) ? "" : "/" + libraryId.getVersion());
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		List<File> out = new ArrayList<File>(files);
		//make file sorted by name
		Collections.sort(out);
		return out;
	}
}
