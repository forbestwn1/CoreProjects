
if(typeof nosliw!='undefined' && nosliw.runtime!=undefined && nosliw.runtime.getResourceService()!=undefined) nosliw.runtime.getResourceService().importResource({"id":{"id":"loop",
"type":"uiTag"
},
"children":[],
"dependency":{},
"info":{}
}, {"name":"loop",
"context":{"group":{"public":{"element":{}
},
"protected":{"element":{"<%=&(nosliwattribute_index)&%>":{"name":"",
"description":"",
"info":{"instantiate":"manual"
},
"definition":{"type":"data",
"processed":"false",
"criteria":{"status":"close",
"criteria":"test.integer;1.0.0",
"info":{}
}
}
},
"<%=&(nosliwattribute_element)&%>":{"name":"",
"description":"",
"info":{"instantiate":"manual"
},
"definition":{"type":"relative",
"processed":"false",
"path":{"rootEleName":"<%=&(nosliwattribute_data)&%>",
"path":"element"
},
"parent":"default"
}
}
}
},
"internal":{"element":{}
},
"private":{"element":{"internal_data":{"name":"",
"description":"",
"info":{},
"definition":{"type":"relative",
"processed":"false",
"path":{"rootEleName":"<%=&(nosliwattribute_data)&%>"
},
"parent":"default"
}
}
}
}
},
"info":{"inherit":"true"
}
},
"attributes":{"data":{"name":"data",
"description":""
},
"element":{"name":"element",
"description":""
},
"index":{"name":"index",
"description":""
}
},
"event":[],
"script":
function (env) {
    var node_namingConvensionUtility = nosliw.getNodeData("common.namingconvension.namingConvensionUtility");
    var node_createContextVariable = nosliw.getNodeData("uidata.context.createContextVariable");
    var node_uiDataOperationServiceUtility = nosliw.getNodeData("uidata.uidataoperation.uiDataOperationServiceUtility");
    var node_dataUtility = nosliw.getNodeData("uidata.data.utility");
    var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
    var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
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
    var loc_getUpdateViewRequest = function (handlers, requestInfo) {
        var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
        out.addRequest(node_createServiceRequestInfoSimple(undefined, function (requestInfo) {
            _.each(loc_childResourceViews, function (resourceView, id) {
                resourceView.destroy(requestInfo);
            });
            loc_childResourceViews = [];
        }));
        out.addRequest(loc_handleEachElementProcessor.getLoopRequest({success: function (requestInfo, eles) {
            var addEleRequest = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
            _.each(eles, function (ele, index) {
                addEleRequest.addRequest(loc_getAddEleRequest(ele.elementVar, ele.indexVar, index));
            });
            return addEleRequest;
        }}));
        return out;
    };
    var loc_getAddEleRequest = function (eleVar, indexVar, index, handlers, requestInfo) {
        var eleContext = loc_env.createExtendedContext([loc_env.createContextElementInfo(loc_env.getAttributeValue("element"), eleVar), loc_env.createContextElementInfo(loc_env.getAttributeValue("index"), indexVar)], requestInfo);
        var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
        out.addRequest(loc_env.getCreateUIViewWithIdRequest(loc_env.getId() + "." + loc_generateId(), eleContext, {success: function (requestInfo, resourceView) {
            if (index == 0) {
                resourceView.insertAfter(loc_env.getStartElement());
            } else {
                resourceView.insertAfter(loc_childResourceViews[index - 1].getEndElement());
            }
            loc_childResourceViews.splice(index, 0, resourceView);
            loc_childVaraibles.splice(index, 0, eleVar);
        }}));
        return out;
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
                node_requestServiceProcessor.processRequest(loc_getUpdateViewRequest(undefined, requestInfo));
            } else {
                if (event == "EACHELEMENTCONTAINER_EVENT_NEWELEMENT") {
                    var req = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
                    req.addRequest(eventData.indexVar.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {success: function (request, data) {
                        return loc_getAddEleRequest(eventData.elementVar, eventData.indexVar, data.value.getValue());
                    }}));
                    node_requestServiceProcessor.processRequest(req);
                } else {
                    if (event == "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT") {
                        eventData.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {success: function (request, data) {
                            loc_out.prv_deleteEle(node_dataUtility.getValueOfData(data), request);
                        }}, requestInfo);
                    }
                }
            }
        });
        return loc_getUpdateViewRequest(undefined, requestInfo);
    }, destroy: function (request) {
        loc_handleEachElementProcessor.destroy(request);
    }};
    return loc_out;
}

}, {"loadPattern":"file"
});

