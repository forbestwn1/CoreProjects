//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_getComponentLifecycleInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createBaseAppDecoration = function(gate){

	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponent();
	var loc_uiAppDef = loc_uiApp.prv_app.appDef;
	var loc_configureData = loc_gate.getConfigureData();

	var loc_out = {
			
		getInitRequest : function(handlers, request){
			
		},

		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var allModules = loc_uiApp.getAllModuleInfo();
			_.each(allModules, function(moduleInfo){
				node_getComponentLifecycleInterface(moduleInfo.module).active();
			});
			return out;
		},
			
		getInterface : function(){
			
		},
		
		registerEventListener(){
			
		}
			
	};
	
	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createBaseAppDecoration", node_createBaseAppDecoration); 

})(packageObj);
