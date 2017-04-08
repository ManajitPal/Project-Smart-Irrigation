<?php 

require_once('DB_connect.php'); 

$data = $_GET['data']; //Getting post data 

$moistue = ($data/1023) * 100; // converts the data into percentage

if($data<=30)

{

	$res = array('body' => "It's time to water it!",'title' =>  $moisture." % Soil Moisture", 'sound' => "default");

	
//Push notification data to be sent to FCM
	$firedatajson = json_encode(array('notification' => $res, 'to' => "YOUR_MOBILE_TOKEN"), JSON_FORCE_OBJECT);

	


	$url = "https://fcm.googleapis.com/fcm/send";

	$headers = array('Content-Type: application/json', 'Authorization: key=YOUR_FIREBASE_APP_AUTH_KEY');

	$curl = curl_init();

	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($curl, CURLOPT_CUSTOMREQUEST, 'POST');
	curl_setopt($curl, CURLOPT_POSTFIELDS, $firedatajson);
	curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

	$response = curl_exec($curl);

	curl_close($curl);

	$Sql="UPDATE data SET Moisture =  '$moisture'";   // Sets the moisture data to the server

	mysqli_query($con,$Sql);
}
else
{

	$Sql="UPDATE data SET Humidity = '$moisture' ";   //Sets the moisture without notificaiton
	mysqli_query($con,$Sql);
}

mysqli_close($con);   //Closing the database connection 

?>