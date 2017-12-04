<!DOCTYPE html>
<html>
<head>
<!-- <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"> -->
</head>
<body>

	<div ng-app="cloudbreadApp" ng-controller="charityPageCtrl">
		<div class="container-fluid">
			<div class="alert alert-success" ng-show="showSuccessAlert">
				<button type="button" class="close"
					data-ng-click="switchBool('showSuccessAlert')">�</button>
				<strong>Done!</strong> {{successTextAlert}}
			</div>

			<div class="alert alert-danger" ng-show="showErrorAlert">
				<button type="button" class="close"
					data-ng-click="switchBool('showErrorAlert')">�</button>
				<strong>Error!</strong> {{errorTextAlert}}
			</div>
		
         <nav class="navbar" role="navigation" style="background: linear-gradient(to left, rgb(142, 158, 171), rgb(238, 242, 243));">
          <!-- <div style="background: linear-gradient(to left, rgb(142, 158, 171), rgb(238, 242, 243));"> -->
           <div class="panel-heading">
             <h4><strong>Welcome, {{authData.currentUser.userName}}</strong></h4>
             <a href="#/login" class="btn btn-xs btn-primary pull-right" style="margin:15px; margin-top:-35px;"><span class="glyphicon glyphicon-log-out"></span>Logout</a>
			 <h2 class="fontfamily" align="center"><strong>Cloud Bread</strong></h2>
			</div>
		</nav>

			
			<div ng-show="isDataPresent">
				<div class="row">
					<div class="col-sm-12">
						<table id="listTable"
							class="table table-striped table-bordered table-condensed table table-hover table-responsive w-auto">
							<thead>
								<tr class="info">
									<th height="70" align=""><p class="text-center">Available Food</p></th>
									<th height="70"><p class="text-center">Available Quantity</p></th>
									<th height="70"><p class="text-center">Category</p></th>
									<th height="70"><p class="text-center">Uploaded By</p></th>
									<th height="70"><p class="text-center">Pick up Address</p></th>
									<th height="70"><p class="text-center">Pick up Time</p></th>
									<th height="70" colspan="2"><p class="text-center">Action</p></th>
								</tr>
							</thead>

							<tr ng-repeat="a in foodDetails['foodDetailsDTOList'] ">
								<td><center><img src="{{a['fileName']}}" height="70" width="150" alt="No image available" class="img-responsive img-box img-thumbnail"></img></center></td>
								<td align="center">{{a['wasteQty']}}</td>
								<td align="center">{{a['category']}}</td>
								<td align="center">{{a['user']['name']}}</td>
								<td align="center">{{a['user']['address']}},
									{{a['user']['city']}}, {{a['user']['zipcode']}}</td>
								<td align="center"><p
										ng-bind="a['pickUpTime'] | date:'yyyy-MM-dd HH:mm'"></td>

								<td>
									<p ng-if="a['reqStatus'] == 'Accepted'">
										<button type="submit" ng-click=" "
											class="btn btn-default btn-success btn-sm">
											<span class="glyphicon glyphicon-thumbs-up"> </span> Accepted
										</button>
									</p>
									<p ng-if="a['reqStatus'] != 'Accepted'">
										<button type="submit" ng-click="updateStatus(a,'Accept')"
											class="btn btn-default btn-sm">
											<span class="glyphicon glyphicon-thumbs-up"> </span> Accept
										</button>
									</p>
								</td>
								<td>
									<p ng-if="a['reqStatus'] == 'Accepted'">
										<button type="submit" class="btn btn-default btn-sm"
											data-ng-click="updateStatus(a,'Reject')" disabled>
											<span class="glyphicon glyphicon-thumbs-down"> </span> Reject
										</button>
									</p>
									<p ng-if="a['reqStatus'] != 'Accepted'">
										<button type="submit" class="btn btn-default btn-sm"
											data-ng-click="updateStatus(a,'Reject')">
											<span class="glyphicon glyphicon-thumbs-down"> </span> Reject
										</button>
									</p>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		</div>
</body>