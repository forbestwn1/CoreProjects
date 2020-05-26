/**
 * 
 */
var nosliwApplication = {};
nosliwApplication.utility = {

	getApplicationObject : function(){   return nosliw.getNodeData("application.application");     },
		
	showPreloader : function(){		nosliwApplication.info.framework7.preloader.show();	},
	
	hidePreloader : function(){		nosliwApplication.info.framework7.preloader.hide();	},
	
	showError : function(eventData){
		var toastBottom = nosliwApplication.info.framework7.toast.create({
			  text: eventData.result.data.message,
			  closeTimeout: 2000,
			});
			toastBottom.open();
	},
	
	loadMultiRelativeLibrary : function(relativeLibsInfo, callBackFunction){
		var libs = [];
		for(var i in relativeLibsInfo){
			var libInfo = relativeLibsInfo[i];
			for(var j in libInfo.libs){
				libs.push(libInfo.basePath + libInfo.libs[j]);
			}
		}
		this.loadLibrary(libs, callBackFunction);
	}, 
		
	loadRelativeLibrary : function(relativeLibs, basePath, callBackFunction){
		var libs = [];
		for(var i in relativeLibs){
			libs.push(basePath+relativeLibs[i]);
		}
		this.loadLibrary(libs, callBackFunction);
	}, 
		
	//load lib utility function
	loadLibrary : function(libs, callBackFunction){
		if(libs==undefined || libs.length==0)  callBackFunction.call();
		
		var count = 0;
		var fileNumber = libs.length;
		
		var loadScriptInOrder = function(){
			var url = libs[count] + "?version="+version;    //attach version information to disable cache in browser 
			
			var script = document.createElement('script');
			script.setAttribute('src', url);
			script.setAttribute('defer', "defer");
			script.setAttribute('type', 'text/javascript');

			script.onload = callBack;
			document.getElementsByTagName("head")[0].appendChild(script);
		};
		
		var callBack = function(){
			count++;
			if(count==fileNumber){
				if(callBackFunction!=undefined)		callBackFunction.call();
			}
			else{
				loadScriptInOrder();
			}
		};

		loadScriptInOrder();
	},
	
	buildApplicationConfigure : function(parms){

		var loc_dataInput = parms;
		
		var loc_getParm = function(name, defaultValue){
			var value = loc_dataInput[name];
			if(value==undefined)  value = defaultValue;
			return value;
		};
		
		var loc_out = {
			
			//application configure (decoration)
			getApplicationConfigureName : function(){
				return loc_getParm("configure", "normal");
			},
			
			//environment information (address, port, context, library)
			getEnvironment : function(){
				var envName =  loc_getParm("env", "product");
				return loc_environments[envName];
			},
			
			getVersion : function(){ return loc_getParm("version", "0.0.0");     },
			
			getParms : function(){   return loc_dataInput;  },
		};
		return loc_out;
	}

};


nosliwApplication.lifecycle = {
	init : function(appName, configureName, rootNode, env, version, dataInput){
		
		nosliwApplication.info = {
			baseServer : env.nosliw_address+':'+env.nosliw_port+'/'+env.nosliw_context+'/',
			application : {
				name : appName,
				version : version,
				configureName : configureName,
				inputData : dataInput
			},
			framwork7 : new Framework7({
				  // App root element
				  root: rootNode,
				  name: appName,
				  id: 'com.myapp.test',
				  panel: {
					    swipe: 'both',
				  },				
			}) 
		};
		
		//when nosliw active
		$(document).on("nosliwActive", function(){
			nosliw.runtime.getErrorManager().logErrorIfHasAny();

			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var requestProcessor = nosliw.runtime.getRequestProcessor();
			requestProcessor.registerEventListener(undefined, function(eventName, eventData){
				if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_START){
					nosliwApplication.utility.showPreloader();
				}
				else if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_DONE){
					nosliwApplication.utility.hidePreloader();
					if(eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_ERROR || eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION){
						nosliwApplication.utility.showError();
					}
				}
			});
			
			
			var libsInfo = [];
			//common lib
			libsInfo.push({
				basePath : "common/",
				libs : [
					"0_package_common.js",
					"loginservice.js",
				]
			});
			
			//application libs
			var applicationPath = "js/application/"+ appName +"/";
			libsInfo.push({
				basePath : applicationPath,
				libs : [
					"0_package_application.js",
					"application.js",
				]
			});

			//load application libs
			nosliwApplication.utility.loadMultiRelativeLibrary(libsInfo, function(){
				var application = nosliwApplication.utility.getApplicationObject();
				var libInfos = [];
				
				var internalLibs = application.getInternalLibs();
				if(internalLibs!=undefined){
					libInfos.push({
						basePath : applicationPath,
						libs : internalLibs
					});
				}
				
				var externalLibs = application.getExternalLibs();
				if(externalLibs!=undefined){
					libInfos.push({
						basePath : "",
						libs : externalLibs
					});
				}
				
				nosliwApplication.utility.loadMultiRelativeLibrary(libsInfo, function(){
					nosliwApplication.utility.hidePreloader();
					var application = nosliwApplication.utility.getApplicationObject();
					application.start();
				});
			});
		});

		nosliwApplication.utility.showPreloader();
		
		//nosliw init first
		var nosliwLibs = [
			"libresources/external/Underscore/1.9.1/underscore.js",
			"libresources/external/Backbone/1.3.3/backbone.js",
			"libresources/nosliw/core/nosliw.js",
			"libresources/nosliw/runtimebrowserinit/utility.js",
			"libresources/nosliw/runtimebrowserinit/init.js",
		];
		
		if(env.library!=undefined){
			for(var i in env.library){
				nosliwLibs.push(env.library[i]);
			}
		}
		
		nosliwApplication.utility.loadRelativeLibrary(nosliwLibs, nosliwApplication.info.baseServer, function(){
			nosliw.init({
				serverBase:nosliwApplication.info.baseServer,
				version : nosliwApplication.info.application.version,
				logging : env.logging,
			});
		});
	},

};
