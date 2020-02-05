<?php

$host = 'localhost';
$user = 'root';
$pass = '';
$db_name = 'school';

//establishing database connection
$mysqli = new mysqli($host, $user, $pass, $db_name);

//checking if connection is ok or not
if ($mysqli->connect_error) {
	die('I am dying here.. Plz, help me...');
}

//executing query and storing into $result var
$query = "SELECT * FROM tbl_student";
$result = $mysqli->query("SELECT * FROM tbl_student");

$students = array(
	"status" => false,
	"totalResult" => 0,
	"studentLists" => array()
);

function getClass($class_id) {
	$class_name = "";
	if ($class_id == 1) {
		$class_name = "Batch 1";
	} else if($class_id == 2) {
		$class_name = "Batch 2";
	} else if($class_id == 3) {
		$class_name = "Batch 3";
	} else if($class_id == 4) {
		$class_name = "Batch 4";
	}
	return $class_name;
}



$todaydate = new DateTime();

//checking if there are any rows in the result
if ($result->num_rows > 0) {
	$students["status"] = true;
	$students["totalResult"] = $result->num_rows;

	while ( $row = $result->fetch_assoc() ) {

		$birthdate = new DateTime($row['date_of_birth']);
		$interval = $todaydate->diff($birthdate);
		if ($row['date_of_birth'] != null && $row['date_of_birth'] != '0000-00-00'){

			$row['age']= $interval->format('%y');
		}else{
			$row['age']= '---';
		}

		$row['class_name']= getClass( $row['class_id']);
		

		array_push($students["studentLists"], $row);
	}
}

echo json_encode($students);

?>
