
nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"loop",
"context":{"public":{},
"protected":{"<%=&(element)&%>":{"children":{},
"type":"relative",
"info":{"info":{}
},
"path":{"rootEleName":"<%=&(data)&%>",
"path":"element"
},
"isToParent":false
}
},
"internal":{},
"private":{"internal_data":{"children":{},
"type":"relative",
"info":{"info":{}
},
"path":{"rootEleName":"<%=&(data)&%>"
},
"isToParent":false
}
},
"info":{"inherit":"true"
}
},
"attributes":{},
"script":
function (env) {
    var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
    var loc_env = env;
    var loc_containerVariable;
    var loc_childResourceViews = [];
    var loc_childVaraibles = [];
    var loc_id = 0;
    var loc_handleEachElementProcessor;
    var loc_generateId = function () {
        loc_id++;
        return loc_id + "";
    };
    var loc_getElementContextVariable = function (key) {
        var out = node_createContextVariable(loc_dataContextEleName);
        out.path = node_namingConvensionUtility.cascadePath(out.path, key + "");
        return out;
    };
    var loc_updateView = function (requestInfo) {
        _.each(loc_childResourceViews, function (resourceView, id) {
            resourceView.destroy(requestInfo);
        });
        loc_childResourceViews = [];
        var index = 0;
        loc_handleEachElementProcessor.executeLoopRequest(function (eleVar, indexVar) {
            loc_addEle(eleVar, indexVar, index, requestInfo);
            index++;
        }, {}, requestInfo);
    };
    var loc_addEle = function (eleVar, indexVar, index, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfo(loc_env.getAttributeValue("element"), eleVar), loc_env.createContextElementInfo(loc_env.getAttributeValue("index"), indexVar)], requestInfo);
        var resourceView = loc_env.createUIResourceViewWithId(loc_env.getId() + "." + loc_generateId(), eleContext, requestInfo);
        if (index == 0) {
            resourceView.insertAfter(loc_env.getStartElement());
        } else {
            resourceView.insertAfter(loc_childResourceViews[index - 1].getEndElement());
        }
        loc_childResourceViews.splice(index, 0, resourceView);
    };
    var loc_out = {prv_deleteEle: function (index, requestInfo) {
        var view = loc_childResourceViews[index];
        view.detachViews();
        view.destroy(requestInfo);
        loc_childResourceViews.splice(index, 1);
        loc_childVaraibles.splice(index, 1);
    }, postInit: function (requestInfo) {
        loc_handleEachElementProcessor = loc_env.createHandleEachElementProcessor("internal_data", "");
        loc_handleEachElementProcessor.registerEventListener(undefined, function (event, eventData, requestInfo) {
            if (event == "EACHELEMENTCONTAINER_EVENT_RESET") {
                loc_updateView(requestInfo);
            } else {
                if (event == "EACHELEMENTCONTAINER_EVENT_NEWELEMENT") {
                    eventData.indexVar.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {success: function (request, data) {
                        loc_addEle(eventData.elementVar, eventData.indexVar, data.value.getValue(), request);
                    }}, requestInfo);
                } else {
                    if (event == "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT") {
                        eventData.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {success: function (request, data) {
                            loc_out.prv_deleteEle(data.value.getValue(), request);
                        }});
                    }
                }
            }
        });
        loc_updateView(requestInfo);
    }, destroy: function (request) {
        loc_handleEachElementProcessor.destroy(request);
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

