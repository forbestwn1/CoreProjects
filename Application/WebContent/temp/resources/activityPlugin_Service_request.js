
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"Service_request",
"type":"activityPlugin"
},
"children":[],
"dependency":{},
"info":{}
}, {"script":
function (nosliw, env) {
    var loc_env = env;
    var loc_expressionService = nosliw.runtime.getExpressionService();
    var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
    var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
    var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
    var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
    var node_IOTaskResult = nosliw.getNodeData("iotask.entity.IOTaskResult");
    var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
    var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
    var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
    var loc_out = {getExecuteActivityRequest: function (activity, input, env, handlers, request) {
        var service = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SERVICE];
        var provider = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_PROVIDER];
        var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {"serviceName": service}), handlers, request);
        out.addRequest(nosliw.runtime.getDataService().getExecuteEmbededDataServiceByProviderRequest(provider, service, input, {success: function (request, taskResult) {
            var activityOutput = taskResult.resultValue.getData();
            return new node_IOTaskResult(node_COMMONCONSTANT.ACTIVITY_RESULT_SUCCESS, activityOutput);
        }}));
        return out;
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

