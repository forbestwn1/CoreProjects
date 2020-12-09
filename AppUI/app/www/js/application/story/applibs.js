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
			"utilitystory.js",
			"utilitystoryui.js",
			"utilitydesign.js",
			"utilitystorychange.js",
			"componentuitagdata.js"
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
	else if(moduleName=='overview'){
		libsInfo.push({
			basePath : moduleFoler,
			libs : [
				"0_package.js",
				"module.js",
				"connection.js",
				"uidataconnection.js",
				"storynode.js",
				"grouplayer.js",
				"layoutbasic.js",
				"layoutmain.js",
				"utilitystoryoverview.js",
			]
		});
	}
	else if(moduleName=='ui'){
		libsInfo.push({
			basePath : moduleFoler,
			libs : [
				"0_package.js",
				"module.js",
				"uinode.js",
			]
		});
	}
	else if(moduleName=='builder'){
		libsInfo.push({
			basePath : moduleFoler,
			libs : [
				"0_package.js",
				"entity.js",
				"module.js",
				"questionstep.js",
				"questiongroup.js",
				"questionitem.js",
				"questionitemservice.js",
				"questionitemswitch.js",
				"questionitemconstant.js",
				"questionitemuidata.js",
				"utility.js",
			]
		});
	}
}

