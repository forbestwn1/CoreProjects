/**
 * 
 */
var init = function(rootNode, baseServer, configureName, callBackFunction){

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
	];
	
	//
	loc_librarys.push("js/application/configure/configure_"+configureName+".js");
	
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
			var url = libs[count];
			
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
		//load mini libs
		loadLibrary(loc_librarys, function(){
			loc_framework7App.preloader.hide();
			
			var node_CONSTANT = nosliw.getNodeData("constant.CONSTANT");
			var requestProcessor = nosliw.runtime.getRequestProcessor();
			requestProcessor.registerEventListener(undefined, function(eventName, requestId){
				if(eventName==node_CONSTANT.REQUESTPROCESS_EVENT_START){
					loc_framework7App.preloader.show();
				}
				else if(eventName==node_CONSTANT.REQUESTPROCESS_EVENT_DONE){
					loc_framework7App.preloader.hide();
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
			minappConfigure.getData().framework7App = loc_framework7App;
			var miniappInitRequest = minapp.interfaceObjectLifecycle.initRequest(rootNode, minappConfigure, undefined, out);
			out.addRequest(miniappInitRequest);
			nosliw.getNodeData("request.requestServiceProcessor").processRequest(out);
		});
	});

	loc_framework7App.preloader.show();
	
	//nosliw init first
	loadLibrary([
//		baseServer+"libresources/external/log4javascript/1.0.0/log4javascript.js",
		baseServer+"libresources/nosliw/runtimebrowserinit/init.js",
	], function(){
		nosliw.init(baseServer);
	});

};

