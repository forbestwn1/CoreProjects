//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIModuleRequest;
	var node_createState;
	var node_createComponentDecoration;
	var node_makeObjectWithComponentLifecycle;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleRuntimeRequest = function(uiModuleDef, ioInput, configure, componentDecorationInfos, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef}), handlers, request);
	out.addRequest(node_createUIModuleRequest(uiModuleDef, ioInput, undefined, {
		success : function(request, uiModule){
			var runtime = loc_createModuleRuntime(uiModule, configure, componentDecorationInfos);
			return runtime.prv_getInitRequest({
				success : function(request){
					return request.getData();
				}
			}).withData(runtime);
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModule, configure, componentDecorationInfos){
	
	var loc_moduleComplex = [];
	var loc_processEnv = {};
	var loc_state = node_createState();

	var loc_init = function(uiModule, configure, componentDecorationInfos){
		loc_moduleComplex.push(uiModule);
		
		for(var i in componentDecorationInfos){
			var componentDecorationInfo = componentDecorationInfos[i];
			var decoration = node_createComponentDecoration(componentDecorationInfo.name, loc_moduleComplex[i], componentDecorationInfo.coreFun, loc_processEnv, configure, loc_state);
			loc_moduleComplex.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_processEnv, decoration.getInterface());
		}
		
		loc_getCurrentModuleFacad().registerEventListener(undefined, function(eventName, eventData, request){});
	};

	var loc_getCurrentModuleFacad = function(){   return loc_moduleComplex[loc_moduleComplex.length-1];  };
	
	var loc_getModule = function(){  return  loc_moduleComplex[0]; };
	
	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessRequest(process, loc_getModule().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_getModule().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_START] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		_.each(loc_moduleComplex, function(part, i){
			if(part.getStartRequest!=undefined){
				out.addRequest(part.getStartRequest());
			}
		});
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("init"));
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]=
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_START_REVERSE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DeactiveUIModuleRuntime", {}), undefined, request);
		//start module
		_.each(loc_moduleComplex, function(part, i){
			if(part.getDeactiveRequest!=undefined){
				out.addRequest(part.getDeactiveRequest());
			}
		});
		loc_state.clear();
		return out;
	};	
	
	var loc_out = {
		
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			_.each(loc_moduleComplex, function(facad, i){
				if(facad.getInitRequest!=undefined){
					out.addRequest(facad.getInitRequest());
				}
			});
			return out;
		},
			
		getModule : function(){  return loc_getModule();  },

		registerEventListener : function(listener, handler, thisContext){	return loc_getCurrentModuleFacad().registerEventListener(listener, handler, thisContext);	},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_getCurrentModuleFacad().getExecuteCommandRequest(command, parms, handlers, request);	}
		
	};
	
	loc_init(uiModule, configure, componentDecorationInfos);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createState", function(){node_createState = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentDecoration", function(){node_createComponentDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
