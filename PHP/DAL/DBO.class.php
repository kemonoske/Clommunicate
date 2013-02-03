<?php 

/**
 * This class describes connection to mysql server and
 * returns object  for working with database
 * @author Akira
 */

class DBO {
	private static $db;
	
	function __destruct()	{
		
		unset(self::$db);
		
	}
	
	public static function getInstance()	{
		
		if (self::$db == null)
			try {
			
				self::$db = new PDO(
					'mysql:dbname=akira_clommunicate;host='.$_ENV['OPENSHIFT_MYSQL_DB_HOST'], 
					$_ENV['OPENSHIFT_MYSQL_DB_USERNAME'],
					$_ENV['OPENSHIFT_MYSQL_DB_PASSWORD']
						);

				$statement = self::$db->prepare(
						'SET character_set_connection=utf8;'.
						'SET character_set_client=utf8;'.
						'SET character_set_results=utf8;'.
						'SET names utf8;'
						);
				$statement->execute();
				
			} catch (PDOException $e) {
				
				throw $e;
				
			}
		
		return self::$db;
		
	}

}

?>