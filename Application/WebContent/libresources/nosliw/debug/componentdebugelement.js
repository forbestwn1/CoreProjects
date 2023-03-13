//get/create package
var packageObj = library.getChildPackage();    


(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentManagementInterface;
	var node_createServiceRequestInfoSimple;
	var node_createIODataSet;
	var node_createDynamicIOData;
	var node_requestServiceProcessor;
	var node_createEventObject;
	var node_getComponentManagementInterface;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentDebugView = function(id){
	
	var loc_view = $('<div style="border-width:thick; border-style:solid; border-color:yellow">'+id+'</div>');
	var loc_outputValueView = $('<textarea rows="5" cols="150" style="resize: none;"></textarea>');
	var loc_wrapperView = $('<div style="border-style:dotted; border-color:red"></div>');
	loc_view.append(loc_outputValueView);
	loc_view.append(loc_wrapperView);
	
	var loc_content = "";

	var loc_println = function(content){
		loc_content = loc_content + "\n";
		loc_content = loc_content + content;
		loc_outputValueView.val(loc_content);
	};
	
	var loc_out = {

		getView : function(){   return loc_view.get();   },
			
		getWrapperView : function(){  return loc_wrapperView.get();   },

		logMethodCalled : function(functionName, args){
			loc_println("method called: " + functionName);
			loc_println("method args: " + JSON.stringify(args, null, 4));
		},

	};
	return loc_out;
};
	
	
//load component and init it with inputValue
var node_createComponentResetView = function(resetCallBack, resourceType, resourceId, inputValue, configureValue, runtimeContext){
	var loc_resetCallBack = resetCallBack;
	var loc_configureValue = configureValue;
	var loc_runtimeContext = runtimeContext;
	
	var loc_view = $('<div>Component Input: </div>');
	var loc_resourceTypeView = $('<textinput></textinput><br>');
	var loc_resourceIdView = $('<textinput></textinput><br>');
	var loc_configureValueView = $('<textinput></textinput><br>');
	var loc_inputValueView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	var loc_submitView = $('<button>Reset</button>');
	loc_view.append(loc_resourceTypeView);
	loc_view.append(loc_resourceIdView);
	loc_view.append(loc_configureValueView);
	loc_view.append(loc_inputValueView);
	loc_view.append(loc_submitView);

	var loc_getResourceType = function(){  return loc_resourceTypeView.val();  };
	var loc_getResourceId = function(){  return loc_resourceIdView.val();  };
	var loc_getConfigureValue = function(){  return loc_configureValue;  };
	var loc_getInputValue = function(){
		var content = loc_inputValueView.val();
		if(content=='')  return;
		return JSON.parse(content); 
	};
	var loc_getRuntimeContext = function(){  return loc_runtimeContext;   };
	
	var loc_init = function(resourceType, resourceId, inputValue, configureValue, runtimeContext){
		if(resourceType!=undefined)   	loc_out.setResourceType(resourceType);
		if(resourceId!=undefined)  		loc_out.setResourceId(resourceId);
		if(inputValue!=undefined)		loc_out.setInputValue(inputValue);
		if(configureValue!=undefined)		loc_out.setConfigureValue(configureValue);
		if(runtimeContext!=undefined)   loc_out.setRuntimeContext(runtimeContext);

		loc_submitView.on('click', function(){
			var request = loc_resetCallBack(loc_getResourceType(), loc_getResourceId(), loc_getInputValue(), loc_getConfigureValue(), loc_getRuntimeContext());
			node_requestServiceProcessor.processRequest(request);
		});
	};
	
	var loc_out = {

		getView : function(){  return loc_view;   },
		
		setResourceType : function(type){   loc_resourceTypeView.val(type);   },
		setResourceId : function(id){  loc_resourceIdView.val(id);   },
		setConfigureValue : function(configureValue){
			loc_configureValue = configureValue;
			loc_configureValueView.val(JSON.stringify(loc_configureValue));   
		},
		setRuntimeContext : function(runtimeContext){  locruntimeContext = runtimeContext;  },
		setInputValue : function(inputValue){  loc_inputValueView.val(JSON.stringify(inputValue));  },
	};
	
	loc_init(resourceType, resourceId, inputValue, configureValue, runtimeContext);
	
	return loc_out;
};

//display component context value
var node_createComponentDataView = function(){
	var loc_comInterface;
	
	var loc_view = $('<div>Component Data: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_listener = node_createEventObject();

	var loc_clearup = function(){
		if(loc_comInterface!=undefined){
			loc_comInterface.unregisterContextDataChangeEventListener(loc_listener);
			loc_comInterface = undefined;
		}
	};

	var loc_showDataSet = function(dataSet){	loc_textView.val(JSON.stringify(dataSet, null, 4));	};
	
	var loc_refresh = function(request){
		node_requestServiceProcessor.processRequest(loc_comInterface.getContextDataSetValueRequest({
			success : function(request, dataSet){
				loc_showDataSet(dataSet);
			}
		}, request));
	};
	
	var loc_setup = function(component, request){
//		loc_comInterface = node_getComponentManagementInterface(component);
//		loc_comInterface.getContextIODataSet().registerEventListener(undefined, function(eventName, eventData, request){
//			loc_refresh(request);
//		});

//		loc_comInterface = node_getComponentManagementInterface(component);
//		loc_comInterface.registerContextDataChangeEventListener(loc_listener, function(eventName, dataSet){
//			loc_showDataSet(dataSet);
//		});

//		loc_refresh(request);
	};
	
	var loc_out = {
			
		getView : function(){  return loc_view;   },
		
		setComponent : function(component, request){
			loc_clearup();
			loc_setup(component, request);
		},
		
		refresh : function(){
			loc_refresh();
		},
	};
	
	return loc_out;
};

//display component event
var node_createComponentEventView = function(){
	var loc_comInterface;
	var loc_view = $('<div>Component Event: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_clearup = function(){};
	
	var loc_setup = function(component){
		loc_comInterface = node_getComponentManagementInterface(component);
		loc_comInterface.registerEventListener(undefined, function(eventName, eventData, request){
			var content = loc_textView.val();
			content = content + "\n\n*****************************************\n\n";
			content = content + JSON.stringify({
				eventName : eventName,
				eventData : eventData
			}, null, 4);
			
			loc_textView.val(content);
		});
	};
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		setComponent : function(component){
			loc_clearup();
			loc_setup(component);
		}
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentManagementInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComponentDebugView", node_createComponentDebugView); 
packageObj.createChildNode("createComponentDataView", node_createComponentDataView); 
packageObj.createChildNode("createComponentEventView", node_createComponentEventView); 
packageObj.createChildNode("createComponentResetView", node_createComponentResetView); 

})(packageObj);
