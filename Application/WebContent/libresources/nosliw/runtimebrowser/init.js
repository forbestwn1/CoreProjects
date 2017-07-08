/**
 * 
 */
nosliw.runtimeName = "browser";

nosliw.initModules();

var runtimeRhino = nosliw.getNodeData("runtime.createRuntime")(nosliw.runtimeName);

nosliw.runtime = runtimeRhino;


runtimeRhino.interfaceObjectLifecycle.init();

nosliw.generateId = runtimeRhino.getIdService().generateId;

