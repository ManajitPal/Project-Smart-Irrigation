<?php
 
 require_once('DB_connect.php');
 
 $switch = $_POST['switch']; //Getting post data 
 
 $sql = "SELECT * FROM toggle WHERE switch='$switch'";   //This query is to check whether the switch is already in the same state or not

 $check = mysqli_fetch_array(mysqli_query($con,$sql)); // $check should have some value if the the $sql query returns some data
 
 if(isset($check))
 {

 echo 'The switch is already in the present state';
 }
 else
 { 

 $sql = "UPDATE toggle SET switch='$switch'"; // Updates the switch value to the one given by the user
  
 if(mysqli_query($con,$sql)) //Trying to insert the values to db. 
 { 
 //If inserted successfully 
	 if($switch==1)
	 {
		 echo "Plant watering turned ON";
	 }
	 else
	 {
		 echo "Plant watering turned OFF";
	 }

  }
  else
  {
   echo 'oops! Please try again!';    //In case any error occured 
  }
 }
 
 mysqli_close($con);    //Closing the database connection 
 
?>