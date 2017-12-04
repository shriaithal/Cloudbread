/**
 * 
 * @author Anuradha Rajashekar
 *
 */
var app = angular.module('cloudbreadApp', [ 'ngRoute', 'ngCookies', 'ngFileUpload','chart.js' ]);

app.config(function($routeProvider) {

	$routeProvider.when('/', {
		templateUrl : '/resources/views/login.jsp',
		controller : 'loginCtrl'
	}).when('/signup', {
		templateUrl : '/resources/views/signUp.jsp',
		controller : 'loginCtrl'
	}).when('/business/list', {
		templateUrl : '/resources/views/businessListPage.jsp',
		controller : 'businessPageCtrl'
	}).when('/business/upload', {
		templateUrl : '/resources/views/businessUploadPage.jsp',
		controller : 'businessPageCtrl'
	}).when('/charity/list', {
		templateUrl : '/resources/views/charityListPage.jsp',
		controller : 'charityPageCtrl'
	}).otherwise({
		redirectTo : "/"
	});
});

app.controller("loginCtrl",
				[
						'$scope',
						'$http',
						'$location',
						'$routeParams',
						'AuthenticationService',
						function($scope, $http, $location, $routeParams, AuthenticationService) {
					
							AuthenticationService.ClearCredentials();
							
							$scope.loginForm = function() {
								AuthenticationService.Login($scope.userName,$scope.password,function(response) {
													if (response.data.statusCode === '200') {					
														AuthenticationService.SetCredentials($scope.userName);
														//TODO : check role and navigate to upload or charity list
														if (response.data.role === 'Business') {
														$location.path('/business/upload');
														}
														else if(response.data.role === 'Charity'){
														$location.path('/charity/list');	
														}
													} else {
														$scope.showErrorAlert = true;
														$scope.errorTextAlert = "Username/password is incorrect";
													}
												})
							};

							$scope.signUpForm = function() {
								AuthenticationService.signUp($scope.userName,$scope.password,$scope.role,$scope.name,$scope.address,$scope.city,$scope.zipcode,function(response) {
													if (response.data.statusCode === '200') {
														AuthenticationService.SetCredentials($scope.userName, response.data.role, response.data.name, response.data.address,response.data.city,response.data.zipcode);
														$location.path('/login');
													} else {
														$scope.showErrorAlert = true;
														$scope.errorTextAlert = "User already exists";
													}
												})
							};

							$scope.switchBool = function(value) {
								$scope[value] = !$scope[value];
							};
						} ]);

app.factory('AuthenticationService', [
	'$http',
	'$cookieStore',
	'$rootScope',
	'$timeout',
	function($http, $cookieStore, $rootScope, $timeout) {
		var service = {};

		service.Login = function(userName, password, callback) {

			var req = {
				method : 'POST',
				url : "/login",
				data : {
					userName : userName,
					password : password
				},
				headers : {
					'Content-Type' : "application/json"
				}
			};
			$http(req).then(function(response) {
				callback(response);
			}, function(error) {
				callback(error);
			});
		};

		service.SetCredentials = function(userName, role, name,
				address,city,zipcode) {
			var authdata = userName;

			$rootScope.globals = {
				currentUser : {
					userName : userName,
					authdata : authdata
				}
			};

			$http.defaults.headers.common['Authorization'] = 'Basic '+ authdata;
			$cookieStore.put('globals', $rootScope.globals);
		};

		service.ClearCredentials = function() {
			$rootScope.globals = {};
			$cookieStore.remove('globals');
			$http.defaults.headers.common.Authorization = 'Basic ';
		};

		service.signUp = function(userName, password, role, name,
				address, city, zipcode, callback) {

			var req = {
				method : 'POST',
				url : "/signup",
				data : {
					userName : userName,
					password : password,
					role: role,
					name : name,
					address : address,
					city: city,
					zipcode: zipcode

				},
				headers : {
					'Content-Type' : "application/json"
				}
			};
			$http(req).then(function(response) {
				callback(response);
			}, function(error) {
				callback(error);
			});
		};

		return service;
	} ]);


app.controller("businessPageCtrl", [
	'Upload',
	'$scope',
	'$rootScope',
	'$http',
	'$location',
	'$routeParams',
	'$q',
	'$cookieStore',
	function(Upload, $scope,$rootScope, $http, $location, $routeParams, $q, $cookieStore) {
		
		$scope.customerFileOnChange = function(element) {

			var reader = new FileReader();
			reader.readAsDataURL(element.files[0]);
		};
		var init = function() {
				$scope.authData = $cookieStore.get('globals');
			if (!$scope.authData) {
				$location.path('/');
			}
			getFoodDetails();
			getGraphPlot();
			getGraphPlot2();
		};

		$scope.uploadCustomerFile = function(file) {
			
			
			if(file == null) {
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "No File to upload!";
				return;
			}
			
			if(file.size > 10485760) {
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "Failed to upload! File Size is more than 10MB!";
				return;
			}
			file.upload = Upload.upload({
				url : "/upload",
				method : "POST",
				data : {
					foodFile : file,
					fileName : file.name,
					userName : $scope.authData.currentUser.userName,
					totalFoodCooked : $scope.totalFoodCooked,
					foodWasteQty : $scope.foodWasteQty,
					pickUpTime : $scope.pickUpTime

				},
				headers : {
					'Content-Type' : "application/json"
				}
			});
			
			file.upload.then(function(response) {
				$scope.showSuccessAlert = true;
				$scope.successTextAlert = "Successfully uploaded the file";
				$scope.isDataPresent = true;
				$scope.custFile = null;
				$scope.totalFoodCooked = "";
				$scope.foodWasteQty = "";
				$scope.pickUpTime = "";
			}, function(response) {
				$scope.showErrorAlert = true;
				$scope.errorTextAlert = "File upload failed! Please try again after sometime!";
			});
		};
		
		var getFoodDetails = function() {
			var req = {
					url : "/businessList",
					type : "GET",	
					params : {
						userName : $scope.authData.currentUser.userName
						
					},
					headers : {
						'Content-Type' : "application/json"
					}
			}
			$http(req).then(function(response) {
				$scope.foodDetails = response.data;
				$scope.isDataPresent = true;
			}, function(error) {
				$scope.isDataPresent = false;
			});
		};
		
		//call Graph Plot
		var getGraphPlot = function() {
			var req = {
				method : 'GET',
				url : "/graphPlots",
				params : {
					userName : $scope.authData.currentUser.userName
				},
				headers : {
					'Content-Type' : "application/json"
				}
			}
			$http(req).then(function(response) {
				$scope.order = response.data;
				
				//Show graph
			    //$scope.labels = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
//				$scope.labels = [$rootScope.order.series2[0],$rootScope.order.series2[1],$rootScope.order.series2[2],$rootScope.order.series2[3],$rootScope.order.series2[4],$rootScope.order.series2[5]];
//			   // $scope.series = ['Series A', 'Series B'];
//			    $scope.data = [$rootScope.order.series1[0],$rootScope.order.series1[1],$rootScope.order.series1[2],$rootScope.order.series1[3],$rootScope.order.series1[4],$rootScope.order.series1[5]];
				$scope.labels = $scope.order.series2;
				$scope.data = $scope.order.series1;
				
				
			    $scope.datasetOverride = [{
			        yAxisID: 'y-axis-1',
			       xAxisID: 'x-axis-1'
			    }];
			    $scope.options = {
			        scales: {
			            yAxes: [{
			                id: 'y-axis-1',
			                type: 'linear',
			                position: 'left',
			                scaleLabel: {
								display: true,
								labelString:' Food Wastage'
							},
							
			            }],

			            xAxes: [{
			                scaleLabel: {
			                	   display: true,
			                    labelString: 'Days of Week'
			                },
			                ticks: {
			                    autoSkip: false
			                }
			            }],
			           
			        }
			    };
			    
				$scope.isDataPresent = true;
			}, function(error) {
				$scope.isDataPresent = false;
			});
		};
		
		
		////Plot222222
		//call Graph Plot2
		var getGraphPlot2 = function() {
			var req = {
				method : 'GET',
				url : "/graphPlots",
				params : {
					userName : $scope.authData.currentUser.userName
				},
				headers : {
					'Content-Type' : "application/json"
				}
			}
			$http(req).then(function(response) {
				$scope.order1 = response.data;
				
				//Show graph
			    //$scope.labels = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
//				$scope.labels1 = [$rootScope.order1.series2[6],$rootScope.order1.series2[7],
//			    	$rootScope.order1.series2[8],$rootScope.order1.series2[9],$rootScope.order1.series2[10]];
			   // $scope.series = ['Series A', 'Series B'];
//			    $scope.data1 = [$rootScope.order1.series1[6],$rootScope.order1.series1[7],
//			    	$rootScope.order1.series1[8],$rootScope.order1.series1[9],$rootScope.order1.series1[10]];
			    
				$scope.labels1 = $scope.order1.series4;
				$scope.data1 = $scope.order1.series3;
				
			    $scope.datasetOverride1 = [{
			        yAxisID: 'y-axis-1',
			       xAxisID: 'x-axis-1'
			    }];
			    $scope.options1 = {
			        scales: {
			            yAxes: [{
			                id: 'y-axis-1',
			                type: 'linear',
//			                display: true,
			                position: 'left',
			                scaleLabel: {
								display: true,
								labelString:' Food Wastage'
							},
							
			            }],

			            xAxes: [{
			                scaleLabel: {
			                	   display: true,
			                    labelString: 'Days of Week'
			                },
			                ticks: {
			                    autoSkip: false
			                }
			            }],	            
			        }
			    };
			    
				$scope.isDataPresent = true;
			}, function(error) {
				$scope.isDataPresent = false;
			});
		};
		
		
		init();
		$scope.switchBool = function (value) {
	        $scope[value] = !$scope[value];
	    };
	    
	} ]);


app.controller("charityPageCtrl", [ '$scope','$rootScope', '$http', '$location', '$routeParams',
	'$q', '$cookieStore',
	function($scope, $rootScope,$http, $location, $routeParams, $q, $cookieStore) {

		var init = function() {
			$scope.authData = $cookieStore.get('globals');
//			if (!$scope.authData) {
//				$location.path('/');
//			}
			
			getFoodDetails();
		};
		var getFoodDetails = function() {
			var req = {
					method : 'GET',
					url : "/charityList",
					params : {
						userName : $scope.authData.currentUser.userName
					},
					headers : {
						'Content-Type' : "application/json"
					}
			};
			
			$http(req).then(function(response) {
				
				$scope.foodDetails = response.data;
				$scope.isDataPresent = true;
			}, function(error) {
				
				$scope.isDataPresent = false;
			});
		};

		$scope.updateStatus =function(a,status){
		
			var req = {
					method : 'POST',
					url : "/updateStatus",
					data : {
						requestId : a.requestId,
						status : status,
						userName : $scope.authData.currentUser.userName
					},
					headers : {
						'Content-Type' : "application/json"
					}
				};
			$http(req).then(function(response) {
				$scope.foodDetails = response.data;
				$scope.isDataPresent = true;
			}, function(error) {
				$scope.isDataPresent = false;
			});
		};
		
		$scope.switchBool = function(value) {
			$scope[value] = !$scope[value];
		};
		init();
	} ]);

