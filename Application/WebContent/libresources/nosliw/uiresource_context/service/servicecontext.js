/**
 * 
 */

var NosliwServiceContext = function(extendObj, data){
	
	_.extend(this, extendObj);
	
	this.data = data;
	this.dataType = {
		categary : CONTEXT_TYPE_SERVICE,
	};
	this.init();
};

NosliwServiceContext.prototype = {
	init : function(){
	},	
		
	requestChange : function(requestInfo){
	},
	
	getData : function(context, path){
	},
	
	registerContextEvent : function(context, contextPath, action, code){
		
	},
};


var getServiceDataWraperByPath = function(context, contextPath){
	var contextEle = context[contextPath.name].data;
	return contextEle.getData(context, contextPath.path);
};

var requestOperateServiceContextPathValue = function(requestInfo){
	var entityOperations = [];
	var reqOperationInfos = requestInfo.getRequestOperations();
	
	var operationInfo = reqOperationInfos[0];
	var context = operationInfo.context;
	var contextPath = getPathInfo(operationInfo.contextPath);
	context[contextPath.name].data.requestChange(requestInfo);
};

var registerServiceContextEvent = function(context, contextPath, action, code){
	var contextEle = context[contextPath.name].data;
	return contextEle.registerContextEvent(context, contextPath, action, code);
};

var NosliwServiceOperationRequestManager = function(){
	
	var m_tasks = [];
	
	var m_manager = {
	};

	return m_manager;
}();
