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
	var node_contextUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleUIRequest = function(moduleUI, externalContext, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleUI", {"moduleUI":moduleUI, "externalContext":externalContext}), handlers, request);
	
	//build context
	var context = node_contextUtility.buildContext(moduleUI[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], externalContext);
	//generate page
	out.addRequest(nosliw.runtime.getUIPageService().getGenerateUIPageRequest(moduleUI[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGE], context, {
		success :function(requestInfo, page){
			return node_createModuleUI(moduleUI, context, page);
		}
	}));
	return out;
};
	
var node_createModuleUI = function(moduleUI, context, page){
	var loc_moduleUI = moduleUI;
	var loc_page = page;
	var loc_context = context;
	
	var loc_out = {
			
		//take command
		getExecuteCommandRequest : function(commandName, parms, handlers, request){
			return node_createServiceRequestInfoSimple(new node_ServiceInfo("executeCommand", {"commandName":commandName, "pamrs":parms}), 
					function(){
						loc_page.command(commandName, parms);
					}, 
			handlers, request);
		},
		
		executeCommand : function(commandName, parms){
			loc_page.command(commandName, parms);
		},
		
		//
		registerListener : function(handler){
			loc_page.registerEventListener(handler);
		},
		
		getEventHandler : function(eventName){   return loc_moduleUI[node_COMMONATRIBUTECONSTANT.DEFINITIONMODULEUI_EVENTHANDLER][eventName];   },
		
		getPage : function(){		return page;		},
		
		getName : function(){	return moduleUI[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];	}
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
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleUIRequest", node_createModuleUIRequest); 

})(packageObj);
