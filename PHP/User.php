<?php 

require_once './include/db_connect.php';

if($_POST[email] && checkEmail($_POST[email]))	{
	
	$response  = mysql_query("
			SELECT u.id,
			u.name,
			u.email,
			u.location,
			u.photo,
			u.gender,
			(
					SELECT COUNT( id ) 
					FROM projects
					WHERE owner = u.id
				) projects, (
					SELECT COUNT(id_proj)
					FROM project_group
					WHERE id_usr = u.id
				) part_in
			FROM users u
			WHERE u.email = '".$_POST[email]."'"
			);
	$result = mysql_fetch_assoc($response);
	if($result != null)	{
		$output[state] = true;
		$output[id] = $result[id];
		$output[name] = $result[name];
		$output[email] = $result[email];
		$output[photo] = $result[photo];
		$output[gender] = ($result[gender] == 1)?true:false;
		$output[locale] = $result[location];
		$output[projects] = $result[projects];
		$output[part_in] = $result[part_in];
		echo json_encode($output);
			
	}	else {

		$output[state] = false;
		echo json_encode($output);
		
	}
}	else {
	$output[state] = false;
	echo json_encode($output);
}

	function checkEmail( $email ){
		return filter_var( $email, FILTER_VALIDATE_EMAIL );
	}

?>