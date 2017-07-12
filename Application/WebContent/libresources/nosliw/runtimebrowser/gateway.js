var gateway = {
		/**
		 * Callback method used to request to discover resources into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered resource info)
		 */
		requestDiscoverResources : function(objResourceIds, callBackFunction){
			
		},
		
		/**
		 * Callback method used to request to discover resources and load into runtime env
		 * @param objResourcesInfo: a list of resource id 
		 * @param callBackFunction (discovered and loaded resource info)
		 */
		requestDiscoverAndLoadResources : function(objResourceIds, callBackFunction){
			
		},
		
		/**
		 * Callback method used to request to load resources into runtime env
		 * @param objResourcesInfo: a list of resource info 
		 * @param callBackFunction (nothing)
		 */
		requestLoadResources : function(objResourcesInfo, callBackFunction){
			var scriptType = requestInfo.getParmData('type');
			var scriptInfo = encodeURI(requestInfo.getParmData('info'));

			var url = "http://localhost:8080/Application/loadResource?resources="+resources;

			  var script = document.createElement('script');
			  script.setAttribute('src', url);
			  script.setAttribute('type', 'text/javascript');

			  script.onload = callBackFunction;
			  script.onreadystatechange = callBackFunction;
			  document.getElementsByTagName("head")[0].appendChild(script);
		},
		
		/**
		 * Callback method used to return expression result to runtime env
		 * @param expressionId: expression id executed
		 * @param result  the data result
		 */
		notifyExpressionExecuteResult : function(taskId, result){
			
		},
		
		/**
		 * Call back method used when all the resources are loaded, so that can execute expression
		 * @param taskId
		 */
		notifyResourcesLoaded : function(taskId){
			
		}
};
