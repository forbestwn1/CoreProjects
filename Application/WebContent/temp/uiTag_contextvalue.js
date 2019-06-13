
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"contextvalue",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"contextvalue",
"context":{"group":{"public":{"element":{}
},
"protected":{"element":{}
},
"internal":{"element":{}
},
"private":{"element":{}
}
},
"info":{}
},
"attributes":{},
"event":[],
"script":
function (env) {
    var node_createServiceRequestInfoSet = nosliw.getNodeData("request.request.createServiceRequestInfoSet");
    var node_requestProcessor = nosliw.getNodeData("request.requestServiceProcessor");
    var node_createContextVariablesGroup = nosliw.getNodeData("uidata.context.createContextVariablesGroup");
    var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
    var node_dataUtility = nosliw.getNodeData("uidata.data.utility");
    var loc_env = env;
    var loc_view;
    var loc_viewData;
    var loc_viewVariableTree;
    var loc_contextVariableGroup = {};
    var loc_updateView = function (requestInfo) {
        var contextContent = {};
        var setRequest = node_createServiceRequestInfoSet({}, {success: function (requestInfo, result) {
            _.each(result.getResults(), function (contextData, name) {
                contextContent[name] = contextData != undefined ? node_dataUtility.getValueOfData(contextData) : "EMPTY VARIABLE";
            });
            loc_viewData.val(JSON.stringify(contextContent, null, 4));
        }}, requestInfo);
        var eleVars = loc_contextVariableGroup.getVariables();
        _.each(eleVars, function (eleVar, eleName) {
            setRequest.addRequest(eleName, loc_env.getDataOperationRequestGet(eleVar));
        });
        node_requestProcessor.processRequest(setRequest, false);
    };
    var loc_out = {preInit: function (requestInfo) {
        loc_contextVariableGroup = node_createContextVariablesGroup(loc_env.getContext(), undefined, function (request) {
            loc_updateView(request);
        });
        _.each(loc_env.getContext().getElementsName(), function (eleName, index) {
            loc_contextVariableGroup.addVariable(node_createContextVariableInfo(eleName));
        });
    }, initViews: function (requestInfo) {
        loc_view = $("<div/>");
        loc_viewData = $("<textarea rows=\"15\" cols=\"150\" id=\"aboutDescription\" style=\"resize: none;\" data-role=\"none\"></textarea>");
        loc_view.append(loc_viewData);
        return loc_view;
    }, postInit: function (requestInfo) {
        loc_updateView(requestInfo);
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

