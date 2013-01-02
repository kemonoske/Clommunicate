<?php 

require_once './include/db_connect.php';

if($_POST[name] && 
		$_POST[email] && checkEmail($_POST[email]) &&
		$_POST[location] &&
		$_POST[photo] && 
		$_POST[gender])	{
	
		

	if(mysql_query("INSERT INTO users(id, email, name, location, photo, gender)
			VALUES (null, '".$_POST[email]."','".$_POST[name]."','".$_POST[location]."','".
			((!@file_get_contents($_POST[photo]))?'null':$_POST[photo])
			."','".$_POST[gender]."');",$mysql_server))	{
			
		$output[state] = true;
		$output[cause] = 'Registrarea a fost efectuata cu succes.';	
		echo json_encode($output);
			
	}	else {

		$output[state] = false;
		$output[cause] = mysql_error();
		echo json_encode($output);
		
	}
}	else {
	$output[state] = false;
	$output[cause] = 'Nu a foss indicat unul din cimpurile obligatorii.';
	echo json_encode($output);
}

	function checkEmail( $email ){
		return filter_var( $email, FILTER_VALIDATE_EMAIL );
	}

?>