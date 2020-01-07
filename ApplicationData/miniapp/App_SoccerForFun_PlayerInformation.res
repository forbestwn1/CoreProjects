{
	"name" : "App_SoccerForFun_PlayerInformation",
	"id" : "App_SoccerForFun_PlayerInformation",
	"attachment": {
		"uiModule" : [
			{	
				"name" : "application",
				"id" : "Module_SoccerForFun_PlayerInformation"
			}		
		],
	},
	"entry": [{
		"name": "main",
		"module": [
			{
				"status": "disabled1111",
				"role": "application",
				"name": "application",
				"module": "application",
				"inputMapping": [
					{
						"element": {
							"playerInModule": {
								"definition": {
									"path": "player",
									"parent" : "applicationData_playerInfo"
								}
							},
						}
					}
				],
				"outputMapping": [
					{
						"name" : "persistance",
						"element": {
							"applicationData_playerInfo;player": {
								"definition": {
									"path": "playerInModule",
								}
							},
						}
					},
				],
				"eventHandler": {
					"savePlayerInformation": {
						"process": {
							"activity": [{
									"id": "startActivityId",
									"name": "startActivity",
									"type": "start",
									"flow": {
										"target": "savePlayerInformation"
									}
								},
								{
									"id": "savePlayerInformation",
									"name": "savePlayerInformation",
									"type": "UI_executeCommand",
									"partId": "module.application.outputMapping.persistance",
									"command": "execute",
									"result": [{
										"name": "success",
										"flow": {
											"target": "successEndId"
										}
									}]
								},
								{
									"id": "successEndId",
									"name": "successEnd",
									"type": "end"
								}
							]
						}
					}
				}
			}
		],
		"process": {

		}

	}],

	"applicationData" : {
		"playerInfo" : {
			"element": {
				"player" : {
					"definition" : {
						"path" : "player"
					},
					"defaultValue": {
						registered : {
							dataTypeId: "test.boolean;1.0.0",
							value: false
						},
						name : {
							dataTypeId: "test.string;1.0.0",
							value: ""
						},
						email : {
							dataTypeId: "test.string;1.0.0",
							value: ""
						},
					}
				}
			},
			"info" : {
				"categary" : "group",
			}
		}
	},

	"context" : {
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"child" : {
								"registered" : {criteria:"test.boolean;1.0.0"},
								"name" : {criteria:"test.string;1.0.0"},
								"email" : {criteria:"test.string;1.0.0"},
							}
						},
						"defaultValue": {
							registered : {
								dataTypeId: "test.boolean;1.0.0",
								value: false
							},
							name : {
								dataTypeId: "test.string;1.0.0",
								value: "Wilson"
							},
							email : {
								dataTypeId: "test.string;1.0.0",
								value: ""
							},
						}
					}
				}
			}
		}			
	}
}
