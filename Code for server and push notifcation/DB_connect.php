<?php

 
 define('HOST','SERVER_HOSTNAME');
 define('USER','DB_USERNAME');
 define('PASS','DB_PASSWORD');
 define('DB','DB_NAME');
 
 $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');