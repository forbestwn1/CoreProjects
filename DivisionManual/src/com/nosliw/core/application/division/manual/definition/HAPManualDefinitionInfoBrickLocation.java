package com.nosliw.core.application.division.manual.definition;

import java.io.File;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPManualDefinitionInfoBrickLocation {

	//for resource in a folder, the base path
	private HAPPathLocationBase m_basePath;
	
	//core file
	private File m_file;
	
	private HAPSerializationFormat m_format;
	
	public HAPManualDefinitionInfoBrickLocation(File file, HAPSerializationFormat format, HAPPathLocationBase basePath) {
		this.m_file = file;
		this.m_format = format;
		if(this.m_format==null)  this.m_format = HAPSerializationFormat.JSON;
		
		this.m_basePath = basePath;
	}
	
	public HAPPathLocationBase getBasePath() {    return this.m_basePath;    }
	
	public File getFiile() {   return this.m_file;    }
	
	public HAPSerializationFormat getFormat() {   return this.m_format;     }
	
}
