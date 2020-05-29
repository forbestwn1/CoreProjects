var libsInfo = nosliwApplication.info.application.libs;
for(var i in nosliwApplication.info.application.modulesConfigure){
	var moduleConfigure = nosliwApplication.info.application.modulesConfigure[i];
	var moduleName = moduleConfigure.name;
	var moduleFoler = nosliwApplication.utility.getModuleFolder();
	if(moduleName=='normal'){
		libsInfo.push({
			basePath : moduleFoler,
			libs : [
				"0_package.js",
				"module.js",
			]
		});
	}
}

