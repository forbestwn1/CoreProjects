{
	"name" : "App_FlightArrival",
	"id" : "App_FlightArrival",
	"attachment": {
		"uiResource" : [
			{
				"name": "infoPage",
				"id": "Page_FlightArrive_Info"
			},
			{
				"name": "settingPage",
				"id": "Page_FlightArrive_Query"
			}
		],
		"uiModule" : [
			{	
				"name" : "Setting",
				"id" : "ModuleFlightArriveSetting"
			},		
			{	
				"name" : "Info",
				"id" : "ModuleFlightArriveInfo"
			}		
		],
		"service" : [
			{
				"name": "getPearsonFlightArrivalService",
				"id" : "pearsonFlightArrivalService"
			},	
		],
		"eventTask" : [
			{	
				"name" : "flightarrive",
				"id" : "flightarrive"
			}		
		],
	},
	"entry": [{
		"name": "main",
		"module": [{
				"status": "disabled111",
				"role": "setting",
				"name": "setting",
				"module": "Setting",
				"info": {},
				"inputMapping": [
					{
						"element": {
							"flightNumber": {
								"definition": {
									"path": "flightNumber",
									"parent" : "applicationData_setting"
								}
							},
							"date": {
								"definition": {
									"path": "date",
									"parent" : "applicationData_setting"
								}
							}
						}
					}
				],
				"outputMapping": [
					{
						"name" : "persistance",
						"element": {
							"applicationData_setting;flightNumber": {
								"definition": {
									"path": "flightNumber",
								}
							},
							"applicationData_setting;date": {
								"definition": {
									"path": "date",
								}
							}
						}
					},
					{
						"name" : "syncWithApp",
						"element": {
							"flightNumber": {
								"definition": {
									"path": "flightNumber",
								}
							},
							"date": {
								"definition": {
									"path": "date",
								}
							}
						}
					}
				],
				"eventHandler" : {
				}
			},
			{
				"status": "disabled111",
				"role": "application",
				"name": "info",
				"module": "Info",
				"inputMapping": [
					{
						"element": {
							"flightNumber": {
								"definition": {
									"path": "flightNumber"
								}
							},
							"date": {
								"definition": {
									"path": "date"
								}
							}
						}
					}
				]
			}
		],
		"process": {

		}

	}],

	"applicationData" : {
		"setting" : {
			"element": {
				"flightNumber": {
					"definition": {
						"path": "flightNumber"
					},
					"defaultValue" : {
						"dataTypeId": "test.string;1.0.0",
						"value": "A4H2M1"
					}
				},
				"date" : {
					"definition": {
						"path": "date"
					},
					"defaultValue" : {
						"dataTypeId": "test.date;1.0.0",
						"value": "2019"
					}
				}
			}
		}
	},

	"context" : {
		"group" : {
			"public" : {
				"element" : {
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
			}
		}			
	}
}
