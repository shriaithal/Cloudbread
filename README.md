# Cloudbread

### Introduction
Project 2: Cloudbread</br>
University Name: http://www.sjsu.edu/</br>
Course: [Cloud Technologies](http://info.sjsu.edu/web-dbgen/catalog/courses/CMPE281.html)</br>
Professor: [Sanjay Garje](https://www.linkedin.com/in/sanjaygarje/)</br>
ISA: [Divyankitha Urs](https://www.linkedin.com/in/divyankithaurs/)</br>
Student: [Anushri Srinath Aithal](https://www.linkedin.com/in/anushri-aithal/)</br>
         [Ashwini Shankar Narayan](https://www.linkedin.com/in/ashwinisnv/)</br>
         [Anuradha Rajashekar](https://www.linkedin.com/in/anu-rajashekar-4b950092/)</br>
         [Nidhi Jamar](https://www.linkedin.com/in/nidhijamar/)</br>

### Project Problem Statement:
Every restaurant has perfectly good food that they cannot sell at the end of their day. Large amount of these food goes wasted and is thrown away in the dumping zone. How can one efficiently use this food to kill someone’s hunger? What if there is a platform which connects restaurants to institutes such as food banks. With this platform not only, food banks can serve more hungry people additionally restaurants will also have a meaningful channel to distribute or dispose of the surplus food. It’s a win-win situation where business can contribute to a sustainable environment in a meaningful way at the same time charities help fight food poverty.     For this to happen both food banks and restaurants will have to register with the platform and exchange information regarding how much food is remaining and food banks can collect those food from the nearest restaurants

###	Proposed Solution/Project Idea:
Using AWS cloud services, we are developing and hosting a 3-Tier Web Application which enables frictionless communication between restaurant management, local charities and one who is hungry.
The application houses a portal for restaurant management to post information regarding all the leftovers in the form of images at the end of business hour. Using AWS Notifications, we notify local charities to pick up this available food. 
The application provides a portal where local charities can sign up to this platform. When notified about food availability, the charity responds by accepting to collect the food from the restaurant.
Analytics are provided to help restaurants reduce their food wastage. AWS Machine Learning is used to provide meaningful predictions about food wastage.
###	Features List
1.	Sign up form for new user to create an account. A new user record is created in DynamoDB. If an already existing user tries to sign up, he is prevented in doing the same.
2.	Login Page to allow only authorized users to login. Performs validation for username and password match. A role based login is enabled to redirect users to either Business Home Page or Charity Home Page. 
3.	Business Home Page provides a widget to upload leftover food images. It also takes in user input for amount of food cooked, food wasted and pick up time. The food images are uploaded to S3.
4.	Amazon Rekognition is used to identify the category of food. It is divided into 3 types – Raw, Canned and Processed.
5.	Amazon Machine Learning with RDS is used to display predicitive analytics on Food Wastage.
6.	An asynchronous notification system using AWS SQS and email using SES is set up to notify charity about food availability when restaurants upload.
7.	Charity Home Page displays all the food images uploaded by Business. Images are displayed from CloudFront.
8.	Data warehouse using Redshift and Lambda. When the ML Data .csv file will be uploaded to S3 bucket, the data from .csv file will be copied to Redshift.


