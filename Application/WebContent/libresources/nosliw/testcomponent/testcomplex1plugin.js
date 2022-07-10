//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTestComplex1Plugin = function(){
	
	var loc_out = {

		createComponentCore : function(complexDef, configure){
			return loc_createTestComplex1ComponentCore();
		},
			
	};

	return loc_out;
};

var loc_createTestComplex1ComponentCore = function(complexDef, variableGroupId, bundleRuntime, configure){
	var loc_parentView;
	var loc_mainView;
	var loc_loggingView;
	
	var loc_logging = function(content){
		loc_loggingView.val(JSON.stringify(content, null, 4));
	};
	
	var loc_out = {

		//execute command
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getExecuteNosliwCommandRequest : function(commandName, parm, handlers, requestInfo){   this.getExecuteCommandRequest(node_basicUtility.buildNosliwFullName(commandName), parm, handlers, requestInfo);    },

		//get part by id
		getPart : function(partId){ },
		
		//get interface exposed
		getInterface : function(){ return {}; },

		//set state for the component core
		setState : function(state){   },

		//component runtime env
		getRuntimeEnv : function(){   return loc_runtimeEnv;    },
		setRuntimeEnv : function(runtimeEnv){   loc_runtimeEnv = runtimeEnv;     },
		
		//value by name
		getValue : function(name){},
		setValue : function(name, value){},
		
		//lifecycle handler
		getLifeCycleRequest : function(transitName, handlers, request){},
		setLifeCycleStatus : function(status){},
		
		//call back when core embeded into runtime during init phase
		getUpdateRuntimeRequest : function(runtimeEnv, handlers, request){},
		
		//call back to provide view (during init phase)
		getUpdateViewRequest : function(view, handlers, request){
			loc_parentView = $(view);
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_mainView = $('<div class="view view-main" style="height1:1200px;overflow-y1: scroll; "></div>');
				loc_loggingView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
				loc_mainView.append(loc_loggingView);
				loc_parentView.append(loc_mainView);
			}, handlers, request);
		},

		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			loc_parentView = $(runtimeContext.view);
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_mainView = $('<div class="view view-main" style="height1:1200px;overflow-y1: scroll; "></div>');
				loc_loggingView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
				loc_mainView.append(loc_loggingView);
				loc_parentView.append(loc_mainView);
			}, handlers, request);
		},

		registerEventListener : function(listener, handler, thisContext){  },
		unregisterEventListener : function(listener){ },

		registerValueChangeEventListener : function(listener, handler, thisContext){   },
		unregisterValueChangeEventListener : function(listener){ },
		
		startLifecycleTask : function(){},
		endLifecycleTask : function(){},
			
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTestComplex1Plugin", node_createTestComplex1Plugin); 

})(packageObj);
