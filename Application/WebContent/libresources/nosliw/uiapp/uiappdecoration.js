//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

	
	
var node_createAppDecoration = function(app, appDef, appDataService, configure){
	
	var loc_appDefinition = appDef;
	
	var loc_applicationDataService = appDataService;
	
	var loc_configure = configure;
	
	var loc_app = app;
	
	var loc_out = {
		
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

//Register Node by Name
//packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);