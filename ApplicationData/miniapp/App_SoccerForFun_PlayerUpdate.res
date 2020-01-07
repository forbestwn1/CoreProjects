{
	"name" : "App_SoccerForFun_PlayerUpdate",
	"id" : "App_SoccerForFun_PlayerUpdate",
	"attachment": {
		"uiModule" : [
			{	
				"name" : "application",
				"id" : "Module_SoccerForFun_PlayerUpdate"
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
							"player": {
								"definition": {
									"path": "player.name",
									"parent" : "applicationData_playerInfo"
								},
							},
						}
					}
				],
			}
		],
		"process": {
		}
	}],

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
					}
				}
			}
		}			
	},
	"applicationData" : {
		"playerInfo" : {
			"element": {
				"player" : {
					"definition" : {
						"path" : "player"
					},
					"defaultValue": {
					}
				}
			},
			"info" : {
				"categary" : "group",
			}
		}
	},
}
