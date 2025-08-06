{
	"name": "ModuleFlightArriveInfo",
	"description": "",
	"info": {
		"setting": "application"
	},
	"context": {
		"group": {
			"public": {
				"element": {
					"flightNumber": {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.string;1.0.0",
							"value": "A4H2M1"
						}
					},
					"date" : {
						"definition": {
							"criteria": "test.date;1.0.0"
						},
						"defaultValue" : {
							"dataTypeId": "test.date;1.0.0",
							"value": "2019"
						}
					},
					"flightInfo": {
						"definition": {
							"criteria": "test.map;1.0.0%%||status:test.string;1.0.0,company:test.string;1.0.0,flight:test.string;1.0.0||%%"
						},
						"defaultValue": {
							"dataTypeId": "test.map;1.0.0",
							"value": {
								"status": {
									"dataTypeId": "test.string;1.0.0",
									"value": "arrive"
								},
								"company": {
									"dataTypeId": "test.string;1.0.0",
									"value": "Air Canada"
								},
								"flight": {
									"dataTypeId": "test.string;1.0.0",
									"value": "ABC123"
								}
							}
						}
					}
				}
			},
			"protect": {
				"element": {}
			}
		}
	},
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"process" : "nosliw_INIT_ACTIVE"
		}
	], 
	"ui": [
		{
			"name": "info",
			"type": "info",
			"page": "infoPage",
			"status" : "disabled111",
			"inputMapping": {
				"element": {
					"flightInfo": {
						"definition": {
							"path": "flightInfo"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"outputMapping": {
				"element": {
					"flightInfo": {
						"definition": {
							"path": "flightInfo"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			}
		}
	],
	"attachment": {
		"uiResource" : [
			{
				"name": "infoPage",
				"id": "Page_FlightArrive_Info"
			},
		],
		"service" : [
			{
				"name": "getPearsonFlightArrivalService",
				"id" : "pearsonFlightArrivalService"
			},	
		],
		"process": [
			{
				"name" : "nosliw_INIT_ACTIVE",
				"entity" : {
					"activity": [{
							"id": "startActivityId",
							"name": "startActivity",
							"type": "start",
							"flow": {
								"target": "retrieveInfo"
							}
						},
						{
							"id": "retrieveInfo",
							"name": "retrieveInfo",
							"type": "Service_request",
							"provider": "getPearsonFlightArrivalService",
							"inputMapping" : {
								"element" : {
									"flightNumber" : {
										"definition" : {
											"path" : "flightNumber"
										}
									},
									"date" : {
										"definition" : {
											"path" : "date"
										}
									}
								}
							},
							"result": [{
								"name": "success",
								"flow": {
									"target": "presentInfoUI"
								},
								"output": {
									"element": {
										"flightInfo": {
											"definition": {
												"path": "outputInService"
											}
										}
									}
								}
							}]
						},
						{
							"id": "presentInfoUI",
							"name": "presentInfoUI",
							"type": "UI_presentUI",
							"setting" : {
								"updateData" : false
							},
							"ui": "infoPage",
							"result": [{
								"name": "success",
								"flow": {
									"target": "refreshInfoPage"
								}
							}]
						},
						{
							"id": "refreshInfoPage",
							"name": "refreshInfoPage",
							"type": "UI_executeCommand",
							"partId": "ui.infoPage",
							"command": "nosliw_update_data",
							"inputMapping": {
								"element": {
									"flightInfo": {
										"definition": { 
											"path": "flightInfo"
										}
									}
								}
							},
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
				},
			}
		],
	},
}
