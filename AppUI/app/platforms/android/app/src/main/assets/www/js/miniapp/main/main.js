//get/create package
var packageObj = library.getChildPackage("module.main");    

(function(packageObj){
	//get used node
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createMiniAppService;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "mainModule";

var node_createModuleMain = function(){

	var loc_template;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){

		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadTemplate", {}));
		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
			$.get("js/miniapp/main/main.html")
			  .done((source) => {
				  loc_template = Handlebars.compile(source);
				  $(document.body).append(loc_template());
//					$( "body>[data-role='panel']" ).panel();
				  requestInfo.executeSuccessHandler(source, out);
			});
		}, this));
		
		return out;
	};

	var loc_out = {

		
	};
	
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;

};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.service.createMiniAppService", function(){node_createMiniAppService = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleMain", node_createModuleMain); 

})(packageObj);
