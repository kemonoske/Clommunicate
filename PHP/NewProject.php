<?php 

require_once './include/db_connect.php';

if($_POST[name] && 
		$_POST[description] &&
		$_POST[owner] &&
		$_POST[deadline])	{
		
	if(mysql_query("INSERT INTO projects(id, name, description, start_date, deadline, end_date, owner)
			VALUES (null, '".$_POST[name]."','".$_POST[description]."',CURDATE(),'".$_POST[deadline]."', null,'".$_POST[owner]."');",$mysql_server))	{
		
		$project_index = mysql_insert_id();
		$i = 0;
		$members = $_POST[members];
		while(isset($members[$i]))	{
			mysql_query("INSERT INTO project_group (id_usr, id_proj)
			VALUES ('".$members[$i]."','".$project_index."')",$mysql_server);
			$i++;
		}
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
	$output[cause] = 'Nu a fost indicat unul din cimpurile obligatorii.';
	echo json_encode($output);
}

	function checkEmail( $email ){
		return filter_var( $email, FILTER_VALIDATE_EMAIL );
	}

?>