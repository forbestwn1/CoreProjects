function nosliwCreateContextElementEntity(wraper, path){
	var loc_ID = wraper.ID;

	var loc_setRealAttributePartListener = function(cpi, i, actionChange, actionNewEle){
		var contextPathInfo = cpi;
		var index = i;
		var results = contextPathInfo.results;
		var part = results[index];

		_.extend(part, Backbone.Events);
		if(part.data!=undefined)	_.extend(part.data, Backbone.Events);

		if(part.ID==undefined){
			
		}
		else if(index==results.length-1){
			part.listenTo(part.data, EVENT_DATA_CHANGE+':'+part.path, actionChange); 
			part.listenTo(part.data, EVENT_ELEMENT_ADD+':'+part.path, actionNewEle); 
		}
		else{
			part.listenTo(part.data, EVENT_DATA_CHANGE+':'+part.path, function(){
				var path = '';
				for(var j=results.length-1; j>index; j--){
					var result = results[j];
					result.stopListening();
					results.pop();
				}
				loc_buildRealAttribute(contextPathInfo, index);
				for(var j=index+1; j<results.length; j++){
					loc_setRealAttributePartListener(contextPathInfo, index, actionChange, actionNewEle);
				}
			}); 
		}
	};

	var loc_buildRealAttribute = function(contextPathInfo, index){
		var parts = contextPathInfo.results;
		
		for(var i=index+1; i<parts.length; i++){
			parts.pop();
		}
		
		var part = parts[index];
		var path = part.expectPath;
		part.path = part.expectPath;
		
		if(isStringEmpty(path))  return;
		
		if(part.ID!=undefined && part.data==undefined)	part.data = NosliwEntityManager.getEntity(part.ID);
		var data = part.data;
		
		var attribute;
		var categary;
		var nextPart = {};
		var start = 0;
		var end = path.indexOf('.', start);
		while(end!=-1){
			attribute = path.substring(start, end);
			start = end+1;
			data = getAttributeValue(data, attribute);
			categary = getWraperDataCategary(data);
			if(categary=='reference'){
				part.path = path.substring(0, end);
				
				if(getWraperData(data)!=undefined){
					nextPart.ID = getWraperData(data);
					nextPart.expectPath = path.substring(end+1, path.length);
					parts.push(nextPart);
					loc_buildRealAttribute(contextPathInfo, parts.length-1);
				}
				else{
					nextPart.expectPath = path.substring(end+1, path.length);
					parts.push(nextPart);
				}
				break;
			}
			end = path.indexOf('.', start);
		}
	};

	var loc_getRealAttribute = function(contextPathInfo){
		
		contextPathInfo.results = [];
		
		var initPart = {
				'ID' : contextPathInfo.ID,
				'data' : contextPathInfo.data,
				'path' : contextPathInfo.path,
				'expectPath' : contextPathInfo.path, 
		};
		
		contextPathInfo.results.push(initPart);
		loc_buildRealAttribute(contextPathInfo, 0);
	};

	
	var loc_out = {
		ovr_getType : function(){return NOSLIWCOMMONCONSTANT.DATATYPE_CATEGARY_ENTITY;},
		
		ovr_registerEvent : function(path, handler, thisContext){
			var contextValue = context[contextPath.name];
			var fullPath = getAbsolutePathOfContextData(context, contextPath); 
			var contextPathEventInfo = {
				'contextType' : contextValue.type,
				'ID' : contextValue.ID,
				'data' : contextValue.data,
				'path' : fullPath,
				'expectPath' : fullPath, 
			};
			
			loc_getRealAttribute(contextPathEventInfo);
			
			var results = contextPathEventInfo.results;
			for(var i=0; i<results.length; i++){
				setRealAttributePartListener(contextPathEventInfo, i, function(eventData, requestInfo){
					action.call(this, EVENT_DATA_CHANGE, contextPath, eventData, requestInfo);
				},
				function(eventData, requestInfo){
					action.call(this, EVENT_ELEMENT_ADD, contextPath, eventData, requestInfo);
				});
			}
			return contextPathEventInfo;
		},
		
		ovr_unregisterEvent : function(eventObj){
			var results = eventObj.results;
			for(var i=0; i<results.length; i++){
				var result = results[i];
				result.stopListening();
				results.pop();
			}
			eventObj.ID = undefined;
			eventObj.data = undefined;
		},
		
		ovr_getWraperByPath : function(path){
			var wraper = loc_out.getWraper();
			
			if(isStringEmpty(path))  return wraper;
			
			var attribute;
			var parts = path.split('.');
			for(var i in parts){
				attribute = parts[i];
				wraper = getWraperAttributeValue(wraper, attribute);
				if(wraper==undefined)  break;
				if(isWraperAtomType(wraper)) break;
			}
			
			if(wraper==undefined)  return undefined; 
			if(i==parts.length-1)  return wraper;
			
			var data = wraper.data;
			var objPath = '';
			for(; i<parts.length; i++){
				if(objPath!=''){
					objPath = objPath + '.' + paths[i];
				}
				else{
					objPath = paths[i];
				}
			}
			var objValueWraper = createObjectWraper(getProperty(data, objPath)); 
			return objValueWraper;
		},
		
		ovr_request : function(reqInfo){
			
		},
		
		ovr_handleEachElement : function(handler, thatContext){
			var containerPath = cascadePath(contextValue.path, contextPath.path);
			var containerWraper = getEntityAttributeWraperByPath(contextValue.data, containerPath);
			var containerData = containerWraper.data;
			for (var key in containerData) {
			    if (containerData.hasOwnProperty(key)) {
			    	var eleWraper = containerData[key];
			    	
					var categary = getWraperDataCategary(eleWraper);
					if(categary=='reference'){
						eleWraper =  NosliwEntityManager.getEntity(eleWraper.data);
				    	var contextEle = createContextElement(eleWraper); 
				    	handler.call(that, key, eleWraper, contextEle);
					}
					else{
				    	var contextEle = createContextElement(contextValue.data, cascadePath(containerPath, key)); 
				    	handler.call(that, key, eleWraper, contextEle);
					}
			    }
			}	
		},
		
	};
	
	return loc_out;
};

function requestSetEntityContextPathValue(context, contextPath, value, reqInfo){
	var eleData = context[contextPath.name];
	var aPath = getAbsolutePathOfContextData(context, contextPath);
	var req = {
		ID : eleData.ID,
		path : aPath,
		value : value,
	};
	var serviceData = NosliwOperationManager.requestSetAttributeAtom(req, reqInfo);
	return serviceData;
}
