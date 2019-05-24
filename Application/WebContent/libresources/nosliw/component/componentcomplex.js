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
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentComplex = function(configure, envInterface){

	var loc_configure = node_createConfigure(configure);
	
	var loc_state = node_createState();
	var loc_parts = [];
	var loc_interface = _.extend({}, envInterface);

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

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
	};

	var loc_updateView = function(view, request){
		for(var i=loc_parts.length-1; i>0; i--){
			var updated = loc_parts[i].updateView(view, request);
			if(updated!=undefined)  view = updated;  
			else break;
		}
	};
	
	var loc_unregisterPartListener = function(){	
		loc_getCurrentFacad().unregisterEventListener(loc_eventListener);	
		loc_getCurrentFacad().unregisterValueChangeEventListener(loc_eventListener);	
	};

	var loc_registerPartListener = function(){
		loc_getCurrentFacad().registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});

		loc_getCurrentFacad().registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	};

	var loc_out = {
		
		addComponent : function(component){
			loc_parts.push(component);
			loc_registerPartListener();
		},
		
		addDecorations : function(componentDecorationInfos){
			for(var i in componentDecorationInfos){  loc_out.addDecoration(componentDecorationInfos[i]);	}
		},

		addDecoration : function(componentDecorationInfo){
			var current = loc_getCurrentFacad();
			loc_unregisterPartListener();
			var decName = componentDecorationInfo.name;
			var decoration = node_createComponentDecoration(decName, current, componentDecorationInfo.coreFun, loc_interface, loc_configure.getConfigureData(decName), loc_state);
			loc_parts.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_interface, decoration.getInterface());
			loc_registerPartListener();
		},
		
		getInterface : function(){  return loc_interface;   },
		
		getComponent : function(){   return loc_getComponent();    },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_getCurrentFacad().getExecuteCommandRequest(command, parms, handlers, request);	},
		
		getAllStateData : function(){   return loc_state.getAllState();   },
		clearState : function(){   loc_state.clear();   },	
		setAllStateData : function(stateData){  loc_state.setAllState(stateData)  },
		
		updateView : function(view, request){  loc_updateView(view, request);  },
		
		getPreDisplayInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getPreDisplayInitRequest", handlers, request);  },
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
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentComplex", node_createComponentComplex); 


})(packageObj);
