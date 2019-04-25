//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	
//*******************************************   Start Node Definition  **************************************

var node_ModuleInfo = function(role, module, version){
	this.role = role;
	this.module = module;
	this.version = version;
	this.inputMapping = {};
	this.outputMapping = {};
};
	
var node_createApp = function(appDef, ioInput){
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
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {

		prv_app : {
			appDef : appDef,
			
			modulesByRole : {},
			currentModuleByRole : {},
			
			ioContext : node_createIODataSet(),
		},
			
		getIOContext : function(){  return loc_out.prv_app.ioContext;  },
		
		getPart : function(partId){		return loc_partMatchers.match(partId);	},
		
		addModule : function(role, module, version){
			var modules = loc_out.prv_app.modulesByRole[role];
			if(modules==undefined){
				modules = [];
				loc_out.prv_app.modulesByRole[role] = modules;
			}
			var out = new node_ModuleInfo(role, module, version);
			modules.push(out);
			loc_out.prv_app.currentModuleByRole[role] = modules.length-1;
			
			module.registerEventListener(loc_eventListener, loc_moduleEventProcessor, out);
			
			return out;
		},
		
		getCurrentModuleInfo : function(role){
			return loc_out.prv_app.modulesByRole[role][loc_out.prv_app.currentModuleByRole[role]];
		},
		
		getModuleInfo : function(role, version){
			if(version==undefined)  return this.getCurrentModuleInfo(role);
			var modules = loc_out.prv_app.modulesByRole[role];
			for(var i in modules){
				if(modules[i].version==version)  return modules[i];
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

//Register Node by Name
packageObj.createChildNode("node_createApp", node_createApp); 

})(packageObj);
