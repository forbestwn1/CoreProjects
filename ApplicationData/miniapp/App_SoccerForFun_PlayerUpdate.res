{
	"name" : "App_SoccerForFun_PlayerUpdate",
	"id" : "App_SoccerForFun_PlayerUpdate",
	"entry": [{
		"name": "main",
		"module": [
			{
				"status": "disabled1111",
				"role": "application",
				"name": "application",
				"module": "Module_SoccerForFun_PlayerUpdate",
				"inputMapping": [
					{
						"element": {
							"player": {
								"definition": {
									"path": "player",
								}
							},
						}
					}
				],
			}
		],
		"process": {

		}

	}],

	"pageInfo": [
	],

	"applicationData" : {
	},

	"context" : {
		"group" : {
			"public" : {
				"element" : {
					"player" : {
						"definition" : {
							"criteria":"test.string;1.0.0"
						},
						"defaultValue": {
							dataTypeId: "test.string;1.0.0",
							value: "peter"
						}
					},
				}
			}
		}			
	}
}
