package com.nosliw.data.core.resource;

import java.io.File;

import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPInfoResourceLocation {

	//for resource in a folder, the base path
	private HAPLocalReferenceBase m_basePath;
	
	//core file
	private File m_file;
	
	public HAPInfoResourceLocation(File file, HAPLocalReferenceBase basePath) {
		this.m_file = file;
		this.m_basePath = basePath;
	}
	
	public HAPLocalReferenceBase getBasePath() {    return this.m_basePath;    }
	
	public File getFiile() {   return this.m_file;    }
	
}
