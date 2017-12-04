/**
 * 
 * @author Anuradha Rajashekar
 *
 */
<!DOCTYPE html>
<html >
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
</head>
<body>
	 <div data-ng-app="cloudbreadApp" data-ng-controller="loginCtrl" style="background-image:url(/resources/views/795-de-Milioane-de-Oameni-Sufera-de-Foame-in-Intreaga-Lume.jpg); top:-20px; height:450px;">
	<!-- <div data-ng-app="cloudbreadApp" data-ng-controller="loginCtrl" style="background-image:url(/resources/views/795-de-Milioane-de-Oameni-Sufera-de-Foame-in-Intreaga-Lume.jpg); height: 416px;
    width: auto;"> -->
	
	<div class="alert alert-danger" ng-show="showErrorAlert">
        <button type="button" class="close" data-ng-click="switchBool('showErrorAlert')">×</button> <strong>Error!</strong> {{errorTextAlert}}
     </div>
        
	<!-- <div class="" style=" top: 60px;
    position: absolute; right: 8%;
    background-color: dimgrey	;
    width: 25%;
    text-align: left;
    padding: 35px;
    margin-top: 500px;
    margin-right: 551px;	"> -->
    <div class="" style=" top: 60px;
    position: absolute; right: 8%;
    background-color: dimgrey; width: 25%; height:40%;
    text-align: left;padding: 35px;margin-right: 439px;margin-top: 391px;">
	<!-- <h1 class="welcome text-center">Welcome</h1> -->
        <div class="card card-container">
    <h2 class='login_title text-center title-login'>Login</h2>
    <hr>
    	<form class="form-signin" name="login" ng-submit="loginForm()">
                
           <span id="reauth-email" class="reauth-email"></span>
           <p class="input_title">Email</p>
           <input type='text' name='userName' ng-model='userName' id='userName' class="login_box"  required autofocus>
           
           <p class="input_title">Password</p>
          	<input type='password' name='password' ng-model='password' id='password' class="login_box"  required>
                
          <div class="row" style="margin-top: 20px">
      		<div class="col-xs-4 buttons-login">
      			<input name="submit" type="submit" id="submitForm" value="Submit" class="btn btn-primary btn-sm"/>
      		</div>
      	
      		<div class="col-xs-4 buttons-login">
      			<input name="cancel" type="reset" id="resetForm" value="Cancel" class="btn btn-primary btn-sm"/>
      		</div>
      	</div>
      	<div class="row">
      	<a href="#/signup" style="font-size: small; color:white;padding-left: 15px;">New User? Sign up here!</a>
      	</div>
      	
     </form>
     
  </div>
</div>
	</div>
