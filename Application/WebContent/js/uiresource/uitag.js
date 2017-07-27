/**
 * 
 * base customer tag object, child tag just provide extendObj which implements its own method 
 * it is also constructor object for customer tag object  
 * 		id: 	id for this tag
 * 		uiTag:	tag object
 * 		uiResourceView: 	parent ui resource view
 * 		extendObj:			object for customer tag
 */
var nosliwCreateUITagCommonObject = function(id, uiTag, uiResourceView, requestInfo){
	
	//tag definition object
	var loc_uiTag = uiTag;
	
	//id of this tag object
	var loc_id = id;
	//parent resource view
	var loc_parentResourceView = uiResourceView;
	//all tag attributes
	var loc_attributes = {};

	//boundary element for this tag
	var loc_startEle = undefined;
	var loc_endEle = undefined;

	//context element infos for body resource
	var loc_contextElementInfosArray = undefined;
	//for body resource view object within this tag
	var loc_bodyResourceView = undefined;

	//all variables
	var loc_dataBindings = {};

	//store all data 
	var loc_data = {};
	
	var loc_eventSource = undefined;
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
		//overriden method
		loc_out.ovr_destroy();
		
		if(loc_dataBindingsGroup!=undefined)	loc_dataBindingsGroup.destroy();
		if(loc_bodyResourceView!=undefined)  loc_bodyResourceView.destroy();
		
		loc_id = undefined;
		loc_uiTag = undefined;
		loc_parentResourceView = undefined;
		loc_context = undefined;
		loc_attributes = undefined;
		
		loc_startEle = undefined;
		loc_endEle = undefined;
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(id, uiTag, uiResourceView, requestInfo){
		//init data bindings
		_.each(loc_uiTag[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_DATABINDINGS], function(dataBinding, name, list){
			var contextVar = dataBinding[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_DATABINDING_VARIABLE];
			loc_out.prv_addDataBinding(dataBinding[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_CONTEXTVARIABLE_NAME], nosliwCreateContextVariable(contextVar[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_CONTEXTVARIABLE_NAME], contextVar[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_CONTEXTVARIABLE_PATH]));
		});
		
		//init all attributes value
		_.each(loc_uiTag.attributes, function(attrValue, attribute, list){
			loc_attributes[attribute] = attrValue;
		});		
		
		//get wraper element
		loc_startEle = loc_parentResourceView.get$EleByUIId(loc_id+NOSLIWCOMMONCONSTANT.CONS_UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX);
		loc_endEle = loc_parentResourceView.get$EleByUIId(loc_id+NOSLIWCOMMONCONSTANT.CONS_UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX);
		
		//overriden method before view is attatched to dom
		loc_out.ovr_preInit(requestInfo);
		
		//overridden method to create init view
		var views = loc_out.ovr_initViews(requestInfo);
		//attach view to resourve view
		if(views!=undefined)  loc_startEle.after(views);	

		loc_eventSource = nosliwCreateRequestEventSource();
		
		//overridden method to do sth after view is attatched to dom
		loc_out.ovr_postInit(requestInfo);
	};

	var loc_out = {
		/***************************    Method for Overriden           *****************************/
		ovr_preInit : function(){},
		ovr_postInit : function(){},
		ovr_initViews : function(){},

		ovr_processAttribute : function(name, value){},

		
		
		ovr_getContextElementInfoArray : function(data){},
		
		ovr_destroy : function(){},
		ovr_clearupContext : function(){},
		ovr_updateContext : function(context){},
		
		
		ovr_handleDataEvent : function(dataBindingName, event, data, requestInfo){},
		ovr_getViewData : function(name){},
		ovr_handleBodyResourceViewEvent : function(event, data, requestInfo){
			this.prv_triggerEvent(event, data);
		},
		
		prv_getBodyResourceViewContext : function(){ return this.prv_getBodyResourceView().getContext(); },
		
		prv_getContextElementInfoArrayFromParent : function(){
			var out = [];
			var context = this.prv_getParentResourceView().getContext();
			_.each(context.getContext(), function(contextEle, name){
				out.push(nosliwCreateContextElementInfo(name, context, name));
			});
			return out;
		},
		
		prv_getAttribute : function(name, defaultValue){ 
			var out = loc_attributes[name];
			if(out==undefined)  out = defaultValue;
			return out;
		},	
		
		prv_getStartElement : function(){  return loc_startEle.get(); },
		prv_getEndElement : function(){  return loc_endEle.get(); },
		
		prv_getParentResourceView : function(){  return loc_parentResourceView},
		prv_getBodyResourceView : function(){ return loc_bodyResourceView; },
		prv_setBodyResourceView : function(view){ loc_bodyResourceView = view; },
		
		/*
		 * create ui resource view for resoruce
		 * 		id : id for ui resource view   if null, use this.loc_id
		 * 		resource : ui reosurce   if null, use loc_uiTag
		 */
		prv_createUIResourceView : function(id, data, resource, requestInfo){
			var res = resource;
			if(resource==undefined)  res = loc_uiTag;

			var id1 = id;
			if(id1==undefined)   id1 = loc_id;
			
			var resourceView = nosliwCreateUIResourceView(res, id1, loc_parentResourceView, this.ovr_getContextElementInfoArray(data), requestInfo);
			resourceView.setParentTagInfo(new NosliwParentTagInfo(this.getTagName(), this));
			return resourceView;
		},
		
		/*
		 * create ui resource view for resoruce, save result to bodyResourceView
		 * 		id : id for ui resource view   if null, use this.loc_id
		 * 		resource : ui reosurce   if null, use loc_uiTag
		 */
		prv_processBodyResourceView : function(id, data, requestInfo){
			loc_bodyResourceView = this.prv_createUIResourceView(id, data, loc_uiTag, requestInfo);
			return loc_bodyResourceView;
		},

		/*
		 * get name for this tag object
		 */
		prv_getName : function(){
			var name = this.getAttribute("name");
			if(name==undefined) name = this.prv_getTagId();
		},
		
		/*
		 * get tag id : 
		 */
		prv_getTagId : function(){	return loc_id;	},

		/*
		 * get root resource view : do not have parent resource view
		 */
		prv_getRootResourceView : function(){
			var out = loc_parentResourceView;
			var parent = out.getParentResourceView();
			while(parent!=undefined){
				out = parent;
				parent = out.getParentResourceView();
			}
			return out;
		},

		/*
		 * add new data binding
		 */
		prv_addDataBinding : function(name, contextVariable, requestInfo){
			var that = this;
			loc_dataBindings[name] = loc_out.prv_getParentResourceView().getContext().createVariable(nosliwCreateContextVariable(contextVariable), requestInfo);
			loc_dataBindings[name].registerDataChangeEvent(function(event, path, data, requestInfo){
				that.ovr_handleDataEvent(name, event, path, data, requestInfo);
			}, requestInfo);

			loc_dataBindings[name].registerLifecycleEvent(function(event, data, requestInfo){
				that.ovr_handleDataEvent(name, event, "", data, requestInfo);
			}, requestInfo);
		},
		
		/*
		 * get data binding object by name
		 */
		prv_getDataBinding : function(name){
			if(name==undefined)  return loc_dataBindings[NOSLIWCONSTANT.UITAG_DATABINDING_NAME_DEFAULT];
			return loc_dataBindings[name];
		},

		prv_getDataBindingData : function(name){
			return this.prv_getDataBinding(name).getData();
		},

		/*
		 * get data related with this tag view 
		 */
		prv_getViewData : function(name){
			return this.ovr_getViewData(name);
		},
		
		/*
		 * get requester object for request from this tag object
		 */
		prv_getRequester : function(){
			var info = new NosliwUITagRequesterInfo(loc_parentResourceView, this, {});
			return new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_UITAG, this.prv_getTagId(), info);
		},

		/*
		 * listener to any event from body resource view 
		 */
		prv_listenToBodyResourceViewEvent : function(){
			if(loc_bodyResourceView==undefined)  return;
			var that = this;
			nosliwEventUtility.registerEvent(this, loc_bodyResourceView, NOSLIWCONSTANT.EVENT_EVENTNAME_ALL, function(event, data, requestInfo){
				that.ovr_handleBodyResourceViewEvent(event, data, requestInfo);
			}, this);
		},

		/*
		 * tirgger event from tag inside
		 * 		event : event name
		 * 		data : data related with this event
		 */
		prv_triggerEvent : function(event, data, requestInfo){	loc_eventSource.triggerEvent(event, data, requestInfo);	},
		
		/*
		 * get erro handler information
		 */
		prv_getErrorHandler : function(){
			var value = this.getAttribute(HAPUIResourceConstant.UITAG_ATTRIBUTE_ERRORHANDLER);
			return value;
		},
		
		prv_startRequest : function(){
			var requestInfo = nosliwCreateRequestUITag({}, loc_dataBindings, {}, this.prv_getRequester());
			return requestInfo;
		},
		
		prv_submitRequest : function(requestInfo){
			nosliw.getRequestServiceManager().processRequest(requestInfo);
		},
		
		prv_createOperationRequestSet : function(dataBindingName, path, data){
			var parms = {
					dataBinding : dataBindingName,
					path : path,
					data : data,
			};
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_SET, parms);
		},		

		prv_createOperationRequestAddElement : function(dataBindingName, path, index, data){
			var parms = {
					dataBinding : dataBindingName,
					path : path,
					index : index,
					data : data,
			};
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT, parms);
		},		
		
		prv_createOperationRequestDeleteElement : function(dataBindingName, path, index){
			var parms = {
					dataBinding : dataBindingName,
					path : path,
					index : index,
			};
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT, parms);
		},		
		
		prv_createOperationRequestGroup : function(){
			var parms = {
			};
			return new NosliwServiceInfo(NOSLIWCONSTANT.WRAPPER_OPERATION_GROUP, parms);
		},		
		
		
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		/***************************    Method for external           *****************************/
		setAttribute : function(name, value){	
			loc_attributes[name] = value;
			this.ovr_processAttribute(name, value);
		},
		
		getAttribute : function(name){ return loc_attributes[name];},	

		getTagName : function(){return loc_uiTag.tagName;},
		
		registerEvent : function(eventName, eventHandler){	return loc_eventSource.registerEventHandler(eventHandler, this, eventName);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_UITAG);

	return loc_out;
};	
