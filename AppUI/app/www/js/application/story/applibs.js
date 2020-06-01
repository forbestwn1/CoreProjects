var libsInfo = nosliwApplication.info.application.libs;
for(var i in nosliwApplication.info.application.modulesConfigure){
	var moduleConfigure = nosliwApplication.info.application.modulesConfigure[i];
	var moduleName = moduleConfigure.name;
	var moduleFoler = nosliwApplication.utility.getModuleFolder(moduleName);

	//lib for application
	libsInfo.push({
		basePath : nosliwApplication.info.application.appFolder,
		libs : [
			"entity.js",
			"utility.js",
		]
	});
	
	if(moduleName=='page'){
		libsInfo.push({
			basePath : moduleFoler,
			libs : [
				"0_package.js",
				"module.js",
				"uinode.js",
			]
		});
	}
}

