package com.nosliw.data.core.system;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPSystemFolderUtility {

	public static String getApplicationLibFolder(){ return HAPSystemUtility.getApplicationResourceLibFolder(); }
	public static String getTagDefinitionFolder(){  return getApplicationLibFolder() + "uitag/";   }
	public static String getActivityPluginFolder(){  return getApplicationLibFolder() + "activity/";   }
	public static String getUIModuleDecorationFolder(){  return getApplicationLibFolder() + "uimoduledecoration/";  }
	public static String getUIAppConfigureFolder(){  return getApplicationLibFolder() + "uiappconfigure/";  }

	public static String getJSLibraryFolder() {   return HAPSystemFolderUtility.getJSFolder() + "libresources/";   }
	public static String getApplicationDataFolder(){  return HAPSystemUtility.getApplicationResourceDataFolder();  }
	public static String getProcessFolder(){  return getApplicationDataFolder() + "process/";   }

	public static String getJSFolder(){  return HAPSystemUtility.getJSFolder();  }
	public static String getNosliwJSFolder(String lib){  return getJSFolder()+"libresources/nosliw/"+lib+"/";  }
	public static String getServiceInterfaceFolder(){  return getApplicationDataFolder() + "serviceinterface/";  }
	public static String getUIPageFolder(){  return getApplicationDataFolder() + "uiresource/";  }
	public static String getUIModuleFolder(){  return getApplicationDataFolder() + "uimodule/";  }
	public static String getMiniAppFolder(){  return getApplicationDataFolder() + "miniapp/";  }
	public static String getCronJobFolder(){  return getApplicationDataFolder() + "cronjob/";  }
	public static String getStoryFolder(){  return getApplicationDataFolder() + "story/";  }
	public static String getTemplateFolder(){  return getApplicationDataFolder() + "template/";  }
	public static String getExpressionFolder(){  return getApplicationDataFolder() + "expression/";  }
	public static String getScriptGroupFolder(){  return getApplicationDataFolder() + "script/";  }
	public static String getCodeTableFolder(){  return getApplicationDataFolder() + "codetable/";  }

	public static String getEntityTextFolder(String entityType) {    return getApplicationDataFolder() + entityType + "/";     }
	public static String getManualEntityBaseFolder() {    return getApplicationDataFolder();     }
	
	public static String getResourceFolder(String resourceType) {    return getApplicationDataFolder() + resourceType + "/";     }
	
	public static String getResourceTempFileFolder(){  return HAPSystemUtility.getJSTempFolder() + "resources/";  }
	public static String getJSLibrayrTempFolder() {    return  HAPSystemUtility.getJSTempFolder() + "libs/";  }
	
	public static String getTempFolder(){		return HAPSystemUtility.getTempFolder();	}

	public static String getBundleExportFolder(){  return HAPSystemFolderUtility.getTempFolder()+"bundleexport/";  }
	public static String getExecutablePackageExportFolder(){  return HAPSystemFolderUtility.getTempFolder()+"executablepackageexport/";  }
	public static String getScriptExportFolder(){  return HAPSystemFolderUtility.getTempFolder()+"scriptexport/scripts/";  }
	public static String getCurrentScriptExportFolder(){  return HAPSystemFolderUtility.getScriptExportFolder() + HAPRuntimeEnvironment.id + "/";  }
	public static String getTaskLogFolder(){  return HAPSystemFolderUtility.getTempFolder()+"tasklog/";  }
	public static String getCronJobInstanceFolder(){  return HAPSystemFolderUtility.getTempFolder()+"cronjob/";  }
	public static String getDynamicResourceExportFolder(){  return HAPSystemFolderUtility.getTempFolder()+"dynamicresourceexport/";  }
	public static String getCurrentDynamicResourceExportFolder(){  return HAPSystemFolderUtility.getDynamicResourceExportFolder() + HAPRuntimeEnvironment.id + "/";  }
	
	public static String getStoryDesignFolder() {	return HAPSystemUtility.getAppDataFolder()+"/storydesign/";}

}
