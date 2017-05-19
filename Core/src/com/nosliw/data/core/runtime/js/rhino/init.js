/**
 * 
 */
nosliw.initModules();

var runtimeRhino = nosliw.getNodeData("runtime.rhino.createRuntime")();

runtimeRhino.interfaceObjectLifecycle.init();

nosliw.generateId = runtimeRhino.getIdService();

nosliw.runtime = runtimeRhino;
