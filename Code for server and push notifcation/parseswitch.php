<?php
 require_once('DB_connect.php');
 
// Gets switch data from the table called "toggle" and parses it as a JSON object
 
$sql = "SELECT * FROM toggle";
 
$res = mysqli_query($con,$sql);


echo json_encode(mysqli_fetch_object($res));
 
mysqli_close($con);
 
?>