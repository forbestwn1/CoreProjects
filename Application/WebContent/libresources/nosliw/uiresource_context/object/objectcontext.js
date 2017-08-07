/**
 * 
 */

function nosliwCreateContextElementObject(wraper, path){

	var loc_wraper = wraper;
	var loc_data = loc_wraper.data;
	var loc_path = cascadePath(loc_wraper.path, path);
	loc_wraper.path = undefined;				
	
	
	loc_requestSetObjectContextElePathValue(contextEle, subPath, value, reqInfo){
		var wraper = contextEle.data;
		var object = contextEle.data.data;
		var path = cascadePath(contextEle.path, subPath);
	 
		setProperty(object, path, value);

		var event = EVENT_DATA_CHANGE+':'+path;
		
		if(isAtomData(object)==true){
			wraper.trigger(event, value, reqInfo);
		}
		else{
			object.trigger(event, value, reqInfo);
		}
		
		return NosliwErrorManager.createSuccessServiceData();
	};


	loc_registerObjectContextEventBySubPath(i, currentPath, pathSegments, contextEle, contextPathEventInfo, action, contextPath){
		contextPathEventInfo.listenTo(contextEle.data.data, EVENT_DATA_CHANGE+':'+currentPath, function(value, requestInfo){
			var eventData = value;
			var k = i;
			if(_.isString(k))  k = parseInt(k);  
			var index = k + 1;
			while(index<pathSegments.length){
				eventData = eventData[pathSegments[index]];
				if(eventData==undefined)   break;
				index++;
			}
			action.call(this, EVENT_DATA_CHANGE, contextPath, eventData, requestInfo);
		}); 
	};

	var loc_out = {			
			ovr_init : function(){
			},
			
			ovr_registerEvent : function(path, handler, thisContext){
				var contextPathEventInfo = {};
				
				var path = getAbsolutePathOfContextData(context, contextPath); 
				var setEvent = EVENT_DATA_CHANGE+':'+path;
				var addEleEvent = EVENT_DATA_CHANGE+':'+EVENT_ELEMENT_ADD+':'+path;
				var removeEleEvent = EVENT_DATA_CHANGE+':'+EVENT_ELEMENT_REMOVE+':'+path;

				var contextEle = context[contextPath.name];
				
				if(isAtomData(contextEle.data.data)==true){
					contextPathEventInfo.listenTo(contextEle.data, setEvent, function(eventData, requestInfo){
						action.call(this, EVENT_DATA_CHANGE, contextPath, eventData, requestInfo);
					}); 
				}
				else{
					if(path!=undefined && path!=''){
						path = path + '';
						var pathSegments = path.split(".");
						var currentPath = '';
						for(var i in pathSegments){
							if(i==0)  currentPath = pathSegments[i];
							else currentPath = currentPath + '.' + pathSegments[i];
							registerObjectContextEventBySubPath(i, currentPath, pathSegments, contextEle, contextPathEventInfo, action, contextPath);
						}
					}
					
//					contextPathEventInfo.listenTo(contextEle.data.data, setEvent, function(eventData, requestInfo){
//						action.call(this, EVENT_DATA_CHANGE, contextPath, eventData, requestInfo);
//					}); 

					contextPathEventInfo.listenTo(contextEle.data.data, addEleEvent, function(eventData, requestInfo){
						action.call(this, EVENT_ELEMENT_ADD, contextPath, eventData, requestInfo);
					}); 

					contextPathEventInfo.listenTo(contextEle.data.data, removeEleEvent, function(eventData, requestInfo){
						action.call(this, EVENT_ELEMENT_REMOVE, contextPath, eventData, requestInfo);
					}); 
					
				}

				return contextPathEventInfo;
			},
			
			ovr_unregisterEvent : function(eventObj){
				eventObj.stopListening();
			},
			
			ovr_getWraperByPath : function(path){
				return createObjectWraper(getObjectAttributeByPath(contextEle.data.data, path));
			},
			
			ovr_request : function(reqInfo){
				
			},
			
			ovr_handleEachElement : function(handler, thatContext){
				var containerPath = cascadePath(contextValue.path, contextPath.path);
				var containerData = getObjectAttributeByPath(contextValue.data.data, containerPath);
				
				_.each(containerData, function(data, key, list){
					var contextEle = createContextElementObject(contextValue.data, cascadePath(containerPath, key));
			    	handler.call(that, key, data, contextEle);
				}, this);
			},
	
	};
	
	return loc_out;
};
	


function requestSetObjectContextPathValue(context, contextPath, value, reqInfo){
	var wraper = context[contextPath.name].data;
	var object = context[contextPath.name].data.data;
	var path = getAbsolutePathOfContextData(context, contextPath);

	setProperty(object, path, value);

	var event = EVENT_DATA_CHANGE+':'+path;
	
	if(isAtomData(object)==true){
		wraper.trigger(event, value, reqInfo);
	}
	else{
		object.trigger(event, value, reqInfo);
	}
	
	return NosliwErrorManager.createSuccessServiceData();;
}

