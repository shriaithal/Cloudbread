<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script>
</head>
<body>
	<div data-ng-app="cloudbreadApp" data-ng-controller="businessPageCtrl">
		<div class="container-fluid">
			<div class="alert alert-success" ng-show="showSuccessAlert">
				<button type="button" class="close" data-ng-click="switchBool('showSuccessAlert')">×</button>
				<strong>Done!</strong> {{successTextAlert}}
			</div>

			<div class="alert alert-danger" ng-show="showErrorAlert">
				<button type="button" class="close" data-ng-click="switchBool('showErrorAlert')">×</button>
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
					<a href="#/business/list" class="btn btn-xs btn-primary pull-right"
						style="margin: 15px; margin-top: -35px;"><span
						class="glyphicon glyphicon-forward"></span> Show List Page</a>
					<h2 class="fontfamily" align="center"><strong>Cloud Bread</strong></h2>
				</div>
			</nav>
	  </div>
		<form class="form-signin" name="upload" onsubmit="uploadPartnerImg(); return false;" enctype="multipart/form-data">
			<span id="reauth-email" class="reauth-email"></span>
			<div class="container">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-4">
								<div class="form-group">
									<label class="input_title">Total Food Cooked</label>
           							<input type='text' name='totalFoodCooked' ng-model='totalFoodCooked' id='totalFoodCooked' class="login_box"  required autofocus>
								</div>
							</div>
							
							<div class="col-xs-4">
								<div class="form-group">
									<label class="input_title">Wasted Food Quantity</label>
          							<input type='text' name='foodWasteQty' ng-model='foodWasteQty' id='foodWasteQty' class="login_box"  required>
								</div>
							</div>
							
							<div class="col-xs-4">
								<div class="form-group">
									<label class="input_title">Food Pickup Time</label>
									<input type="datetime-local" name='pickUpTime' ng-model='pickUpTime' id='pickUpTime' class="login_box"  required>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-4">
								<div class="form-group">
									<label class="input_title">Upload Food Photo</label>
          	   						<input type='file' accept="image/*" data-ng-model="custFile" onchange="angular.element(this).scope().customerFileOnChange(this)" data-classButton="btn btn-link" ngf-select required data-input="false" name="file"/>
          	   					</div>
          	   				</div>
          	   				<div class="col-xs-2">
          	   						<a href="javascript:void(0)" data-ng-click="uploadCustomerFile(custFile)" class="btn btn-sm btn-primary" style="margin-left: -107px;margin-bottom: -37px;">
      		  						<span class="glyphicon glyphicon-upload"></span>Submit</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>


		<br/>
		<br/>
      <div ng-show="isDataPresent">
      <div class="container-fluid">
        <div class="row">
        <div class="col-sm-4 col-md-10 col-lg-6">
               <h4 align="center">Actual Food Wasted</h4>
               <br/>
		 <div class="container" style="height: 200px; width: 60%;" align="left" >
          <canvas id="line" class="chart chart-line" chart-data="data" chart-labels="labels" chart-series="series" chart-options="options" 
             chart-dataset-override="datasetOverride" data-ng-model="line">
          </canvas>
        </div>
        </div>
        
        <div class="col-sm-4 col-md-10 col-lg-6" >
               <h4 align="center">Predicted Food Waste</h4>
               <br/>
        <div class="container" style="height: 300px; width: 60%;" align="right" >
          <canvas id="line1" class="chart chart-line" chart-data="data1" chart-labels="labels1" chart-series="series1" chart-options="options1" 
             chart-dataset-override="datasetOverride1" data-ng-model="line1">
          </canvas>
        </div>
        </div>
        </div>
        </div>
        </div>
        
	</div>

</body>
</html>
	