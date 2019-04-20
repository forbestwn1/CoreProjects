//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createState;
	var node_createConfigure;
	var node_createComponentDecoration;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentComplex = function(configure){

	var loc_configure = node_createConfigure(configure);
	
	var loc_state = node_createState();
	var loc_parts = [];
	var loc_interface = {};
	
	var loc_getCurrentFacad = function(){   return loc_parts[loc_parts.length-1];  };
	
	var loc_getComponent = function(){  return  loc_parts[0]; };

	var loc_getLifeCycleRequest = function(requestFunName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ComponentComplexLifycycle", {}), handlers, request);
		//start module
		_.each(loc_parts, function(part, i){
			if(part[requestFunName]!=undefined){
				out.addRequest(part[requestFunName]());
			}
		});
		return out;
	}
	
	
	var loc_out = {
		
		addComponent : function(component){
			loc_parts.push(component);
		},
		
		addDecorations : function(componentDecorationInfos){
			for(var i in componentDecorationInfos){  loc_out.addDecoration(componentDecorationInfos[i]);	}
		},

		addDecoration : function(componentDecorationInfo){
			var decName = componentDecorationInfo.name;
			var decoration = node_createComponentDecoration(decName, loc_getCurrentFacad(), componentDecorationInfo.coreFun, loc_interface, loc_configure.getConfigureData(decName), loc_state);
			loc_parts.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_interface, decoration.getInterface());
		},
		
		getInterface : function(){  return loc_interface;   },
		
		getComponent : function(){   return loc_getComponent();    },
		
		getAllStateData : function(){   return loc_state.getAllState();   },
		clearState : function(){   loc_state.clear();   },	
		setAllStateData : function(stateData){  loc_state.setAllState(stateData.state)  },
		
		getInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getInitRequest", handlers, request);  },
		getStartRequest : function(handlers, request){  return loc_getLifeCycleRequest("getStartRequest", handlers, request);  },
		getResumeRequest : function(handlers, request){  return loc_getLifeCycleRequest("getResumeRequest", handlers, request);  },
		getDeactiveRequest : function(handlers, request){  return loc_getLifeCycleRequest("getDeactiveRequest", handlers, request);  },
		getSuspendRequest : function(handlers, request){  return loc_getLifeCycleRequest("getSuspendRequest", handlers, request);  },

	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.createState", function(){node_createState = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentDecoration", function(){node_createComponentDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createComponentComplex", node_createComponentComplex); 


})(packageObj);
