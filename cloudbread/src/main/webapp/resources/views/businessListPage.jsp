<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
</head>
<body>
	<div data-ng-app="cloudbreadApp" data-ng-controller="businessPageCtrl">
		<div class="container-fluid">
			<div class="alert alert-success" ng-show="showSuccessAlert">
				<button type="button" class="close"
					data-ng-click="switchBool('showSuccessAlert')">×</button>
				<strong>Done!</strong> {{successTextAlert}}
			</div>

			<div class="alert alert-danger" ng-show="showErrorAlert">
				<button type="button" class="close"
					data-ng-click="switchBool('showErrorAlert')">×</button>
				<strong>Error!</strong> {{errorTextAlert}}
			</div>


			<nav class="navbar" role="navigation"
				style="background: linear-gradient(to left, rgb(142, 158, 171), rgb(238, 242, 243));">
				<!-- <div style="background: linear-gradient(to left, rgb(142, 158, 171), rgb(238, 242, 243));"> -->
				<div class="panel-heading">
					<h4>
						<strong>Welcome, {{authData.currentUser.userName}}</strong>
					</h4>
					<a href="#/login" class="btn btn-xs btn-primary pull-right"
						style="margin: 15px; margin-top: -35px;"><span
						class="glyphicon glyphicon-log-out"></span> Logout</a>
					<a href="#/business/upload" class="btn btn-xs btn-primary pull-right"
						style="margin: 15px; margin-top: -35px;"><span
						class="glyphicon glyphicon-backward"></span> Back to Upload Page</a>
					<h2 class="fontfamily" align="center"><strong>Business List</strong></h2>
				</div>
			</nav>

			<div ng-show="isDataPresent">
				<div class="row">
					<div class="col-sm-12">
						<table id="listTable"
							class="table table-striped table-bordered table-condensed table table-hover table-responsive w-auto">
							<thead>
								<tr class="info">
									<th height="70"><p class="text-center">Food </p></th>
									<th height="70"><p class="text-center">Total Quantity</p></th>
									<th height="70"><p class="text-center">Wastage Quantity</p></th>
									<th height="70"><p class="text-center">Category</p></th>
									<th height="70"><p class="text-center">Status</p></th>
									<th height="70"><p class="text-center">Pickup Time</p></th>
									<th height="70"><p class="text-center">Charity Accepted</p></th>
								</tr>
							</thead>
							<!-- style="width:60;height:60;" -->
							<tr ng-repeat="a in foodDetails['foodDetailsDTOList'] ">
								<td><center><img src="{{a['fileName']}}" height="40" width="110" alt="No image available" class="img-responsive img-box img-thumbnail"></img></center></td>
								<td align="center">{{a['totalQty']}}</td>
								<td align="center">{{a['wasteQty']}}</td>
								<td align="center">{{a['category']}}</td>
								<td align="center">{{a['reqStatus']}}</td>
								<td align="center"><p ng-bind="a['pickUpTime'] | date:'yyyy-MM-dd HH:mm'"></td>
								<td align="center">{{a['charityAccepted']}}</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>