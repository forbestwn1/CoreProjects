//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentComplex = function(){

	var loc_elements = [];

	var loc_interface = {};
	
	var loc_out = {
		
		addElement : function(element){
			
		},
		
		addDecorations : function(){
			for(var i in componentDecorationInfos){
				var componentDecorationInfo = componentDecorationInfos[i];
				var decoration = node_createComponentDecoration(componentDecorationInfo.name, loc_moduleComplex[i], componentDecorationInfo.coreFun, loc_processEnv, loc_configure, loc_state);
				loc_moduleComplex.push(decoration);
				if(decoration.getInterface!=undefined)	_.extend(loc_processEnv, decoration.getInterface());
			}

		},

		addDecoration : function(componentDecorationInfo){
			var decoration = node_createComponentDecoration(componentDecorationInfo.name, loc_moduleComplex[i], componentDecorationInfo.coreFun, loc_processEnv, loc_configure, loc_state);
			loc_moduleComplex.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_processEnv, decoration.getInterface());
		},
		
		getInterface : function(){  return loc_interface;   },
			
			
	};
	return loc_out;
};
	
var node_createState = function(){
	var loc_state = {};
	
	var loc_out = {
		
		getAllState : function(){   return loc_state;   },
		setAllState : function(state){  loc_state = state; },
		
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
		},
		
		clear : function(){
			loc_state = {};
		}
	};
	return loc_out;
};
	
var node_createConfigure = function(value){
	
	if(value!=undefined){
		var valueType = node_getObjectType(value);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE){
			return value;
		}
		else{
			var loc_configure = value;
		}
	}
	
	var loc_out = {
		
		getConfigure : function(component){
			var out = {};
			if(component!=undefined)	_.extend(out, loc_configure.global, loc_configure.components==undefined?undefined : loc_configure.components[component]);
			else  _.extend(out, loc_configure.global);
			return out;
		}
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE);

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
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

})(packageObj);
