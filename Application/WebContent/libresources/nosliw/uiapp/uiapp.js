//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createPatternMatcher;
	var node_Pattern;
	var node_createEventObject;
	var node_createIODataSet;
	var node_createServiceRequestInfoSequence;
	
//*******************************************   Start Node Definition  **************************************

var node_ModuleInfo = function(role){
	this.role = role;
	this.module = undefined;
	this.id = undefined;
	this.version = undefined;
	this.inputMapping = {};
	this.currentInputMapping = undefined;
	this.outputMapping = {};
};
	
var node_createApp = function(id, appDef, ioInput){
	var loc_ioInput = ioInput;
	
	var loc_partMatchers = node_createPatternMatcher([
		new node_Pattern(new RegExp("module\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).module;}),
		new node_Pattern(new RegExp("module\.(\\w+)\.outputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).outputMapping[result[2]]}),
		new node_Pattern(new RegExp("module\.(\\w+)\.inputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).inputMapping[result[2]]}),
	]);
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_moduleEventProcessor = function(eventName, eventData, request){
		loc_trigueEvent(eventName, {
			moduleInfo : this,
			data : eventData
		}, request);
	};
	
	var loc_updateIOContext = function(input){
		var data = loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_INITSCRIPT](input);
		loc_out.prv_app.ioContext.setData(undefined, data);
	};

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {

		prv_app : {
			id : id,
			version : "1.0.0",
			
			appDef : appDef,
			
			modulesByRole : {},
			currentModuleByRole : {},
			
			ioContext : node_createIODataSet(),
		},
			
		getId : function(){  return loc_out.prv_app.id;  },
		getVersion : function(){   return "1.0.0";   },
		
		getIOContext : function(){  return loc_out.prv_app.ioContext;  },
		
		getPart : function(partId){		return loc_partMatchers.match(partId);	},
		
		getProcess : function(name){  return loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_PROCESS][name];  },

		getInitIOContextRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(loc_ioInput!=undefined){
				out.addRequest(loc_ioInput.getGetDataValueRequest(undefined, {
					success : function(request, data){
						loc_updateIOContext(data);
					}
				}));
			}
			else{
				loc_updateIOContext();
			}
			return out;
		},
		
		addModuleInfo : function(moduleInfo){
			var role = moduleInfo.role;
			var module = moduleInfo.module;
			
			var modules = loc_out.prv_app.modulesByRole[role];
			if(modules==undefined){
				modules = [];
				loc_out.prv_app.modulesByRole[role] = modules;
			}
			modules.push(moduleInfo);
			loc_out.prv_app.currentModuleByRole[role] = modules.length-1;
			
			module.registerEventListener(loc_eventListener, loc_moduleEventProcessor, moduleInfo);
			return moduleInfo;
		},
		
//		addModule : function(role, module, version){
//			var out = new node_ModuleInfo(role, module, version);
//			return addModuleInfo(out);
//		},
		
		getCurrentModuleInfo : function(role){
			return loc_out.prv_app.modulesByRole[role][loc_out.prv_app.currentModuleByRole[role]];
		},
		
		getAllModuleInfo : function(){
			var out = [];
			_.each(loc_out.prv_app.modulesByRole, function(modulesByRole, role){
				_.each(modulesByRole, function(moduleInfo){
					out.push(moduleInfo);
				});
			});
			return out;
		},
		
		getModuleInfo : function(role, id){
			if(id==undefined)  return this.getCurrentModuleInfo(role);
			var modules = loc_out.prv_app.modulesByRole[role];
			for(var i in modules){
				if(modules[i].id==id)  return modules[i];
			}
		},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
		
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.createPatternMatcher", function(){node_createPatternMatcher = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.Pattern", function(){node_Pattern = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createApp", node_createApp); 
packageObj.createChildNode("ModuleInfo", node_ModuleInfo); 

})(packageObj);
