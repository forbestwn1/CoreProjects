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
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_createIODataSet;
	var node_appDataService;
	var node_createDataAssociation;
	
//*******************************************   Start Node Definition  **************************************

var node_ModuleInfo = function(module, version){
	this.module = module;
	this.version = version;
	this.inputMapping = {};
	this.outputMapping = {};
};
	
var node_createApp = function(appDef, ioContext, statelessData){
	
	var loc_compenentMatchers = node_createPatternMatcher([
		new node_Pattern(new RegExp("module\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).module;}),
		new node_Pattern(new RegExp("module\.(\\w+)\.outputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).outputMapping[result[2]]}),
		new node_Pattern(new RegExp("module\.(\\w+)\.inputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).inputMapping[result[2]]}),
	]);
	
	var loc_out = {

		prv_app : {
			appDef : appDef,
			
			modulesByRole : {},
			currentModuleByRole : {},
			
			ioContext : ioContext,

			stateData : {},
			
			statelessData : statelessData==undefined?{}:statelessData,
		},
			
		getComponent : function(componentId){
			return loc_compenentMatchers.match(componentId);
		},
		
		addModule : function(role, module, version){
			var modules = loc_out.prv_app.modulesByRole[role];
			if(modules==undefined){
				modules = [];
				loc_out.prv_app.modulesByRole[role] = modules;
			}
			var out = new node_ModuleInfo(module, version);
			modules.push(out);
			loc_out.prv_app.currentModuleByRole[role] = modules.length-1;
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
		
		getEventProcess : function(eventName){
			return loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_PROCESS][eventName];
		}
		
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
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.appDataService", function(){node_appDataService = this.getData();});

//Register Node by Name
packageObj.createChildNode("node_createApp", node_createApp); 

})(packageObj);
