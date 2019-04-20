//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppRuntimeRequest = function(id, appDef, appDecorationFac, configure, ioInput, handlers, request){
	loc_appDefinition(appDef, ioInput);
};
	
var node_createAppRuntime = function(id, app, configure, componentDecorationInfos){
	
	var loc_id = id;
	var loc_version = "1.0.0";
	var loc_appComplex = [];
	var loc_processEnv = {};
	var loc_state = node_createState();
	var loc_configure;
	var loc_applicationDataService;
	
	var loc_init = function(app, configure, componentDecorationInfos){
		loc_configure = configure;
		loc_appComplex.push(app);
		
		for(var i in componentDecorationInfos){
			var componentDecorationInfo = componentDecorationInfos[i];
			var decoration = node_createComponentDecoration(componentDecorationInfo.name, loc_appComplex[i], componentDecorationInfo.coreFun, loc_processEnv, loc_configure, loc_state);
			loc_appComplex.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_processEnv, decoration.getInterface());
		}
		
		loc_getCurrentModuleFacad().registerEventListener(undefined, function(eventName, eventData, request){});

	};	
	
	var loc_executeProcess = function(){
		
	};
	
	
	
	var loc_out = {
		
	};
	
	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});

//Register Node by Name
//packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);
