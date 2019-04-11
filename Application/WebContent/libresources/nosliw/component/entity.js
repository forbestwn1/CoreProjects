//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createState = function(){
	var loc_state = {};
	
	var loc_out = {
		getState : function(component){
			var out = loc_state[component];
			if(out==undefined){
				out = {};
				loc_state[component] = out;
			}
			return out;
		},
		
		getStateValue : function(component, name){
			return loc_out.getState(component)[name];
		},
		
		setStateValue : function(component, name, value){
			loc_out.getState(component)[name] = value;
		}
	};
	return loc_out;
};
	
var node_createConfigure = function(configure){
	
	var loc_configure = configure;
	
	var loc_out = {
		
		getConfigure : function(component){
			var out = {};
			_.extend(out, loc_configure.global, loc_configure.components==undefined?undefined : loc_configure.components[component]);
			return out;
		}
	};
	
	return loc_out;
};

	
var node_commandResult = function(requestResult, commandInfo){
	this.requestResult = requestResult;
	this.commandInfo = commandInfo;
};

var node_commandRequestInfo = function(name, parms, handlers, request){
	this.name = name;
	this.parms = parms;
	this.handlers = handlers;
	this.request = request;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createState", node_createState); 
packageObj.createChildNode("createConfigure", node_createConfigure); 
packageObj.createChildNode("commandResult", node_commandResult); 
packageObj.createChildNode("commandRequestInfo", node_commandRequestInfo); 

})(packageObj);
