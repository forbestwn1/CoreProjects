package com.nosliw.core.application.division.manual.definition;

import java.io.File;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPManualDefinitionInfoBrickLocation {

	private HAPIdBrickType m_brickTypeId;
	
	//for resource in a folder, the base path
	private HAPPathLocationBase m_basePath;
	
	//core file
	private File m_file;
	
	private HAPSerializationFormat m_format;
	
	public HAPManualDefinitionInfoBrickLocation(HAPIdBrickType brickTypeId, File file, HAPSerializationFormat format, HAPPathLocationBase basePath) {
		this.m_brickTypeId = brickTypeId;
		this.m_file = file;
		this.m_format = format;
		if(this.m_format==null) {
			this.m_format = HAPSerializationFormat.JSON;
		}
		
		this.m_basePath = basePath;
	}
	
	public HAPIdBrickType getBrickTypeId() {     return this.m_brickTypeId;      }
	
	public HAPPathLocationBase getBasePath() {    return this.m_basePath;    }
	
	public File getFiile() {   return this.m_file;    }
	
	public HAPSerializationFormat getFormat() {   return this.m_format;     }
	
}
