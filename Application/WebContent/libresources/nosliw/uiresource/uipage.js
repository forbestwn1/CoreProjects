//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIPage = function(uiView){
	
	var loc_uiView = uiView;
	
	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();

	var loc_viewEventListener;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiView){
		loc_viewEventListener = loc_uiView.registerEventListener(function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});
	};	
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
	
	};	
	
	var loc_out = {

		//append this views to some element as child
		appendTo : function(ele){  loc_uiView.appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_uiView.insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_uiView.detachViews();		},

		registerEventListener : function(handler, thisContext){	return loc_eventSource.registerListener(undefined, undefined, handler, thisContext);},

		command : function(command, data, requestInfo){	 return loc_uiView.command(command, data, requestInfo);		}
		
	};

	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIRESOURCEVIEW);

	node_getLifecycleInterface(loc_out).init(uiView);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIPage", node_createUIPage); 

})(packageObj);