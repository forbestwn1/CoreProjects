//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSimple;
	var node_contextUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIPage = function(uiView){
	
	var loc_decorations = [];
	loc_decorations.push(uiView);
	
	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	
	var loc_viewEventListener;

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiView){
		loc_viewEventListener = loc_getCurrent().registerEventListener(function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});
	};	
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
	
	};	
	
	var loc_getCurrent = function(){	return loc_decorations[loc_decorations.length-1];	};
	
	var loc_out = {
		getUIView :function(){ return loc_decorations[0];  },
			
		addDecoration : function(decoration){  
			decoration.setParent(loc_getCurrent());
			loc_decorations.push(decoration);
		},	
			
		//append this views to some element as child
		appendTo : function(ele){  loc_getCurrent().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_getCurrent().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_getCurrent().detachViews();		},

		registerEventListener : function(handler, thisContext){	return loc_eventSource.registerListener(undefined, undefined, handler, thisContext);},

		getExecuteCommandRequest : function(command, parms, handlers, requestInfo){
			if(command==node_CONSTANT.PAGE_COMMAND_REFRESH){
				return loc_getCurrent().getUpdateContextRequest(parms, handlers, requestInfo);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
					return loc_getCurrent().command(command, parms, requestInfo);	
				}, handlers, requestInfo);
			}
		},
		
		executeExecuteCommandRequest : function(command, data, handlers, requestInfo){
			node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(command, data, handlers, requestInfo));
		},
		
		getGetContextValueRequest : function(handlers, requestInfo){
			return node_contextUtility.getGetContextValueRequest(loc_getCurrent().getContext(), handlers, requestInfo);
		}
	};

	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIRESOURCEVIEW);

	node_getLifecycleInterface(loc_out).init(uiView);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIPage", node_createUIPage); 

})(packageObj);
