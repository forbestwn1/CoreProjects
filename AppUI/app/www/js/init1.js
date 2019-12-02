/**
 * 
 */
var init = function(rootNode, env, configureName, version, dataInput, callBackFunction){

	var baseServer = env.nosliw_address+':'+env.nosliw_port+'/'+env.nosliw_context+'/';
	
	var loc_librarys = [
		"js/application/0_package_service.js",
		"js/application/utility.js",
		"js/application/service.js",
		"js/application/loginservice.js",
		"js/application/application.js",
		"js/application/module/userapps/userapps.js",
		"js/application/module/userapps/group.js",
		"js/application/module/userapps/miniapp.js",
		"js/application/module/userapps/userinfo.js",
		"js/application/module/userapps/usermodule.js",
		"js/application/module/miniapp/miniappmodule.js",
		"js/application/module/appgroup/appgroupmodule.js",
	];
	
	//
	loc_librarys.push("js/application/configure/"+configureName+"/configure.js");
	
	var loc_framework7App = new Framework7({
		  // App root element
		  root: $("#appDiv").get(),
		  name: 'My App',
		  id: 'com.myapp.test',
		  panel: {
			    swipe: 'both',
		  },				
	});
	
	//load lib utility function
	var loadLibrary = function(libs, callBackFunction){
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
	};

	//when nosliw active
	$(document).on("nosliwActive", function(){
		nosliw.runtime.getErrorManager().logErrorIfHasAny();
		
		//load mini libs
		loadLibrary(loc_librarys, function(){
			loc_framework7App.preloader.hide();
			
			$(document).on("miniappActive", function(){
				nosliw.miniapp.executeLoginRequest(undefined);
			});
			
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var requestProcessor = nosliw.runtime.getRequestProcessor();
			requestProcessor.registerEventListener(undefined, function(eventName, eventData){
				if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_START){
					loc_framework7App.preloader.show();
				}
				else if(eventName==node_CONSTANT.EVENT_REQUESTPROCESS_DONE){
					loc_framework7App.preloader.hide();
					
					if(eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_ERROR || eventData.result.type==node_CONSTANT.REQUEST_FINISHTYPE_EXCEPTION){
						var toastBottom = loc_framework7App.toast.create({
						  text: eventData.result.data.message,
						  closeTimeout: 2000,
						});
						toastBottom.open();
					}
				}
			});
			
			
			//create miniapp
			var minapp = nosliw.getNodeData("miniapp.createApplication")();
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

			nosliw.miniapp = minapp;
			nosliw.createNode("miniapp", minapp);
			var out = node_createServiceRequestInfoSequence(undefined, {
				success : function(requestInfo, data){
			  		$(document).trigger("miniappActive");
				}
			});

			var minappConfigure = nosliw.getNodeData("miniapp.configure");
			minappConfigure.addData("framework7App", loc_framework7App);
			minappConfigure.addDataSet(dataInput);
			var miniappInitRequest = minapp.interfaceObjectLifecycle.initRequest(rootNode, minappConfigure, undefined, out);
			out.addRequest(miniappInitRequest);
			nosliw.getNodeData("request.requestServiceProcessor").processRequest(out);
		});
	});

	loc_framework7App.preloader.show();
	
	//nosliw init first
	var libs = [
		baseServer+"libresources/external/Underscore/1.9.1/underscore.js",
		baseServer+"libresources/external/Backbone/1.3.3/backbone.js",
		baseServer+"libresources/nosliw/core/nosliw.js",
		baseServer+"libresources/nosliw/runtimebrowserinit/utility.js",
		baseServer+"libresources/nosliw/runtimebrowserinit/init.js",
	];
	if(env.library!=undefined){
		for(var i in env.library){
			libs.push(baseServer+env.library[i]);
		}
	}
	loadLibrary(libs, function(){
		nosliw.init({
			serverBase:baseServer,
			version : version
		});
	});
};
