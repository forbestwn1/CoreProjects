package com.nosliw.app.instance;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.app.utils.HAPApplicationUtility;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPEntityDefinitionLoader;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.definition.xmlimp.HAPEntityDefinitionLoaderXML;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;

public class HAPApplicationEntityDefinitionManager extends HAPEntityDefinitionManager{

	public HAPApplicationEntityDefinitionManager(HAPConfigure configuration,
			HAPDataTypeManager dataTypeMan, HAPOptionsDefinitionManager optionsMan) {
		super(configuration, dataTypeMan, optionsMan);
		
		HAPConfigure config = configuration.getConfigurableValue("loader");
		for(String name : config.getConfigureItems()){
			HAPConfigure loaderConfig = config.getConfigurableValue(name);
			if(name.equals("xml")){
				List<InputStream> xmlFiles = new ArrayList<InputStream>();
				
				Set<String> loaderConfigNames = loaderConfig.getConfigureItems();
				if(loaderConfigNames.contains("path")){
					//single 
					xmlFiles.addAll(HAPApplicationUtility.getFileInputStreams(loaderConfig));
				}
				else{
					//multiple
					for(String n : loaderConfigNames){
						xmlFiles.addAll(HAPApplicationUtility.getFileInputStreams(loaderConfig.getConfigurableValue(n)));
					}
				}
				
				HAPEntityDefinitionLoader loader = new HAPEntityDefinitionLoaderXML("", xmlFiles.toArray(new InputStream[0]), this.getDataTypeManager(), this, this.getOptonsManager());
				this.addEntityDefinitionLoader(loader);
			}
		}
		
	}
}

