//get/create package
var packageObj = library.getChildPackage("lifecycle");    

(function(packageObj){
	//get used node
	var node_NOSLIWCONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "lifecycle";
	
/*
 * utility functions to build lifecycle object
 */
var makeObjectWithLifecycle = function(baseObject, lifecycleCallback, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createResourceLifecycle(thisContext==undefined?baseObject:thisContext, lifecycleCallback));
};
	
/**
 * create resource lifecycle object which provide basic lifecycle method and status
 * all the thisContext for life cycle method is either loc_thisContext or this
 * the transition of status can be monitored by register event listener, so that when status transition is done, the listener will be informed
 */
var loc_createResourceLifecycle = function(thisContext, lifecycleCallback){
	//this context for lifycycle callback method
	var loc_thisContext = thisContext;
	
	//name for this lifecycle object, it can be used in logging
//	var loc_name = name;
	//status: start with START status
	var loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
	//wether lifecycle object is under transit
	var loc_inTransit = false;	
	//empty object that is used as event source 
	var loc_eventSource = {};
	//empty object that is used as event listener
	var loc_eventListener = {};
	
	//life cycle call back including all call back method
	var loc_lifecycleCallback = lifecycleCallback==undefined? {}:lifecycleCallback;
	
	/*
	 * get this context
	 */
	var loc_getThisContext = function(){
		return loc_thisContext;
	};

	/*
	 * method called when transition is finished
	 */
	var loc_finishStatusTransition = function(oldStatus, status){
		loc_inTransit = false;
		nosliwEventUtility.triggerEvent(loc_eventSource, node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, {
			oldStatus : oldStatus,
			newStatus : status,
		});
	};
	
	//method for init event
	var loc_onResourceInit = function(){
		//if resource has been inited, then just do nothing
		if(loc_status!=node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_START)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT];
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishInit is called
		if(initResult!=false)		loc_out.finishInit();
	};

	//method for init event
	var loc_onResourceDeactive = function(){
		//if resource is START OR DEAD, then just do nothing
		if(loc_status==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_START || loc_status==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDeactive is called
		if(initResult!=false)		loc_out.finishDeactive();
	};
	
	//method for destroy method
	var loc_onResourceDestroy = function(){
		if(loc_status==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY];
		
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDestroy is called
		if(initResult!=false)		loc_out.finishDestroy();
	};

	//method for suspend method
	var loc_onResourceSuspend = function(){
		if(loc_status!=node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishSuspend is called
		if(initResult!=false)		loc_out.finishSuspend();
	};

	//method for resume method
	var loc_onResourceResume = function(){
		if(loc_status!=node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishResume is called
		if(initResult!=false)		loc_out.finishResume();
	};
	
	var loc_out = {
		init : function(){
			loc_onResourceInit.apply(this, arguments);
		},
		
		finishInit : function(){
			var oldStatus = loc_status;
			loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		destroy : function(){
			loc_onResourceDestroy.apply(this, arguments);
		},
		
		finishDestroy : function(){
			var oldStatus = loc_status;
			loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		refresh : function(){
			var oldStatus = this.getResourceStatus();
			
			var listener = {};
			var that = this;
			this.registerEventListener(listener, function(event, status){
				if(status==oldStatus){
					that.unregisterEventListener(listener);
				}
				else{
					if(status==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_START){
						that.init();
					}
					else if(status==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
						if(oldStatus==node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
							that.suspend();
						}
					}
				}
			});
			
			loc_onResourceDeactive.apply(this, arguments);
		},
		
		finishDeactive : function(){
			var oldStatus = loc_status;
			loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		suspend : function(){
			loc_onResourceSuspend.apply(this, arguments);
		},
		
		finishSuspend : function(){
			var oldStatus = loc_status;
			loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		resume : function(){
			loc_onResourceResume.apply(this, arguments);
		},
		
		finishResume : function(){
			var oldStatus = loc_status;
			loc_status = node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		getResourceStatus : function(){return loc_status;},

		registerEventListener : function(listener, handler){
			node_eventUtility.registerEvent(listener, loc_eventSource, node_NOSLIWCONSTANT.EVENT_EVENTNAME_ALL, handler);
		},

		unregisterEventListener : function(listener){
			node_eventUtility.unregister(listener);
		},
		
		//callback method when this interface is connect to baseObject
		bindBaseObject : function(baseObject){
			//listener to status transit event by itself
			loc_out.registerEventListener(loc_eventListener, function(event, data){
				//logging ths status transit
				//only for module with name

				if(baseObject.interfaceObjectName!=null){
					var name = baseObject.interfaceObjectName.getObjectName();
					nosliwLogging.info(name, "status transit from " + data.oldStatus + " to " + data.newStatus);
				}
			});
		}
	};

	loc_constructor = function(){
	};
	
	loc_constructor();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("makeObjectWithLifecycle", makeObjectWithLifecycle); 

var module = {
	start : function(packageObj){
		node_NOSLIWCONSTANT = packageObj.getNodeData("constant.NOSLIWCONSTANT");
		node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
		node_getInterface = packageObj.getNodeData("common.interface.getInterfaceNode");
		node_eventUtility = packageObj.getNodeData("common.event.eventUtility");
	}
};
nosliw.registerModule(module, packageObj);

})(packageObj);
