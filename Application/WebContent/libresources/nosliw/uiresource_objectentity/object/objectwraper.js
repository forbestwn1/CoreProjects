function nosliwCreateObjectWraper(){
	
	var loc_getOperationObject = function(obj){
		var opObject = obj;
		if(nosliwTypedObjectUtility.getObjectType(obj)==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA){
			var dataCategary = obj.dataTypeInfo.categary;
			if(dataCategary==NOSLIWCOMMONCONSTANT.CONS_DATATYPE_CATEGARY_OBJECT){
				//if operation data is object, then use value
				opObject = obj.value;
			}
		}
		return opObject;
	};
	
	var loc_out = {		
			/*
			 * 
			 */
			pri_triggerOperationEvent : function(event, path, opData, request){
				var rootWrapper = this.getRootWrapper();
				var rootPath = rootWrapper.getPath();
				if(rootPath==undefined)  rootPath = "";
				var fullPath = nosliwNamingConversionUtility.cascadePath(this.getFullPath(), path);
				if(rootPath==fullPath){
					//on current
					rootWrapper.pri_trigueDataOperationEvent(event, fullPath, opData, request);
				}
				else{
					//on child
					rootWrapper.pri_triggerForwardEvent(event, fullPath, opData, request);
				}
			},

			/*
			 * calculate current object
			 */
			ovr_calObject : function(){
				var baseObject = undefined;
				if(this.pri_isDataBased()==false){
					baseObject = this.pri_getParent().getObject();
				}
				else{
					baseObject = this.pri_getRootData().value;			
				}
				this.pri_setObject(nosliwObjectUtility.getObjectAttributeByPath(baseObject, this.pri_getPath()));
				loc_validData = true;
			},
			
			getChildData : function(path){
				var object = nosliwObjectUtility.getObjectAttributeByPath(this.getObject(), path);
				var data = nosliwDataUtility.createDataByObject(object);
				return data;
			},
			
			requestDataOperation : function(service, request){
				var command = service.command;
				var serviceData = service.parms;
				var path = serviceData.path;
				
				var rootData = this.getRootData();
				var fullPath = nosliwNamingConversionUtility.cascadePath(this.getFullPath(), path);

				var opObject = rootData;
				var opPath = nosliwNamingConversionUtility.cascadePath("value", fullPath);
				if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_SET){
					var opData = loc_getOperationObject(serviceData.data);
					//change value
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_SET, opData);
					//trigue event
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_SET, path, opData, request);
				}
				else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					var opData = loc_getOperationObject(serviceData.data);
					var index = serviceData.index;
					var operationData = {
						data : opData,
						index : index,
					};
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT, operationData);
					//trigue event
					if(path==undefined)  path="";
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_ADDELEMENT, path, operationData, request);
				}
				else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					var index = serviceData.index; 
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT, index);
					//trigue event
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_DELETE, nosliwNamingConversionUtility.cascadePath(path, index), {}, request);
				}
			},

			handleEachElement : function(handler, thatContext){	
				var containerData = this.getData();
				_.each(containerData.value, function(data, key, list){
					var child = this.createChildWrapper(key);
			    	handler.call(thatContext, child, key);
				}, this);
			},
			
			getWrapperType : function(){	return NOSLIWCONSTANT.WRAPPER_TYPE_OBJECT;		},
	};
	
	return loc_out;
};
