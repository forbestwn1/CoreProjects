package com.nosliw.data.core.imp.runtime.js;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.HAPJSLibraryId;
import com.nosliw.data.core.runtime.js.HAPResourceDataLibrary;
import com.nosliw.data.core.runtime.js.HAPResourceIdLibrary;

public class HAPResourceManagerJSLibrary implements HAPResourceManager{

	private String m_baseFolder;
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		
		for(HAPResourceId resourceId : resourcesId){
			HAPResourceIdLibrary resourceLibraryId = new HAPResourceIdLibrary(resourceId);
			HAPJSLibraryId libraryId =  resourceLibraryId.getLibraryId();

			StringBuffer libraryContent = new StringBuffer();
			List<File> files = this.getLibraryFileName(libraryId);
			for(File file : files){
				String fileContent = HAPFileUtility.readFile(file);
				libraryContent.append(fileContent);
			}
			HAPResourceDataLibrary libraryResourceData = new HAPResourceDataLibrary(libraryContent.toString());
			HAPResource resource = new HAPResource(resourceId, libraryResourceData, null);
			out.add(resource);
		}
		return out;
	}
	
	
	private List<File> getLibraryFileName(HAPJSLibraryId libraryId){
		String folder = m_baseFolder + "/" + libraryId.getName() + "/" + libraryId.getVersion();
		Set<File> files = HAPFileUtility.getAllFiles(folder);
		List<File> out = new ArrayList<File>(files);
		Collections.sort(out);
		return out;
	}
}
