package com.nosliw.app.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.app.utils.HAPApplicationUtility;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUIResourceProcessor;

public class HAPApplicationUIResourceManager extends HAPUIResourceManager{

	//when is debug mode, parse ui resource everytime, not get from buffer, so that, everytime ui resource is changed, the change can be reflected
	private String m_mode;
	
	private HAPUIResource readUIResource(String resourceFile){
		HAPUIResource out = null;
		HAPUIResourceProcessor processor = new HAPUIResourceProcessor(this.getConfiguration(), this.getDataTypeManager(), this);
		try {
			out = processor.readUIResource(resourceFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public HAPApplicationUIResourceManager(HAPConfigure configuration, HAPDataTypeManager dataTypeMan) {
		super(configuration, dataTypeMan);

		this.m_mode = configuration.getStringValue("mode");
		if("debug".equals(this.m_mode)){
			//debug mode, load based on demand
			
		}
		else{
			//normal mode, load everything during init
			//load all the ui resource file
			HAPConfigure config = configuration.getConfigurableValue("loader");
			for(String name : config.getConfigureItems()){
				HAPConfigure loaderConfig = config.getConfigurableValue(name);
				if(name.equals("file")){
					List<String> resourceFiles = new ArrayList<String>();
					
					Set<String> loaderConfigNames = loaderConfig.getConfigureItems();
					if(loaderConfigNames.contains("path")){
						//single 
						resourceFiles.addAll(HAPApplicationUtility.getFileNames(loaderConfig));
					}
					else{
						//multiple
						for(String n : loaderConfigNames){
							resourceFiles.addAll(HAPApplicationUtility.getFileNames(loaderConfig.getConfigurableValue(n)));
						}
					}
					
					for(String resourceFile : resourceFiles){
						this.addUIResource(this.readUIResource(resourceFile));
					}
				}
			}
		}
	}
	
	public HAPUIResource getUIResource(String name){
		if("debug".equals(this.m_mode)){
			String path = this.getConfiguration().getStringValue("loader.file.path");
			String fileName = HAPFileUtility.buildFullFileName(path, name, "res");
			return this.readUIResource(fileName);
		}
		else{
			return super.getUIResource(name);
		}
	}

	/*
	 * return the script string (an json structure containing block and expression) for ui resource
	 */
	public String getUIResourceScript(String name){
		if("debug".equals(this.m_mode)){
			String path = this.getConfiguration().getStringValue("loader.file.path");
			String fileName = HAPFileUtility.buildFullFileName(path, name, "res");
			HAPUIResource uiResource = this.readUIResource(fileName);
			return this.readUIResourceScript(name);
		}
		else{
			return super.getUIResourceScript(name);
		}
	}
	
}
