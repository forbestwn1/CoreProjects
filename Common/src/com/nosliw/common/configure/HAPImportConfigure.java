package com.nosliw.common.configure;

public class HAPImportConfigure {

	private boolean m_isHard;
	
	private String m_basePath;
	
	private boolean m_useBaseConfigureWhenNotSpecified;
	
	private boolean m_isClone;
	
	public HAPImportConfigure(){
		this.m_isHard = true;
		this.m_basePath = null;
		this.m_useBaseConfigureWhenNotSpecified = false;
		this.m_isClone = false;
	}
	
	public boolean isHard(){  return this.m_isHard;  }
	public String getBasePath(){  return this.m_basePath; } 
	public boolean getUseBaseConfigureWhenNotSpecified() {  return this.m_useBaseConfigureWhenNotSpecified;  }
	public boolean isClone(){  return this.m_isClone;  }
	
	public HAPImportConfigure setIsClone(boolean isClone){
		this.m_isClone = isClone;
		return this;
	}
	
	public HAPImportConfigure setIsHard(boolean isHard){  
		this.m_isHard = isHard;
		return this;
	}
	
	public HAPImportConfigure setBasePath(String basePath){
		this.m_basePath = basePath;
		return this;
	}
	
	public HAPImportConfigure setUseBaseConfigureWhenNotSpecified(boolean in){
		this.m_useBaseConfigureWhenNotSpecified = in;
		return this;
	}
	
}
