<?php

/**
 *	Acces functions for User entity
 *
 * @author Akira
 *
 */

class UserDAO {

	/**
	 * Constants defining request type that are posible for the
	 * user entity
	 */
	const EXISTS = 1;
	const GET = 2;
	const GET_ALL = 3;
	const REMOVE = 4;
	const UPDATE = 5;
	const ADD = 6;


	/**
	 * Checks if user with such email is contained in users table
	 * @param String $ident
	 */
	static function exists($ident) {

		require_once 'DBO.class.php';

		$db = DBO::getInstance();
		$ids = $db->prepare('SELECT count(*) nr
				FROM users 
				WHERE email = :ident');
		$ids->execute(array(':ident' => $ident));
		
		if($ids->errorCode() != 0)	{
			$error = $ids->errorInfo();
			throw new Exception($error[2]);
		}

		if($id = $ids->fetch())
			if($id[nr] != 0)
				return true;

		return false;
	}

	/**
	 * Checks if user with such id is contained in users table
	 * @param int $ident
	 */
	static function existsById($ident) {

		require_once 'DBO.class.php';

		$db = DBO::getInstance();
		$ids = $db->prepare('SELECT count(id) nr
				FROM users 
				WHERE id = :id');
		$ids->execute(array(':id' => $ident));
		
		if($ids->errorCode() != 0)	{
			$error = $ids->errorInfo();
			throw new Exception($error[2]);
		}

		if($id = $ids->fetch())
			if($id[nr] != 0)
				return true;

		return false;
	}

	/**
	 *
	 * Returns user object from Users table using email
	 * as identifier
	 *
	 * @param String $email email that identifies the user in system
	 * @throws Exception
	 * @return User|NULL
	 */
	static function getUser($email)	{

		require_once 'DBO.class.php';
		require_once 'DAL/User.class.php';

		$db = DBO::getInstance();
		$user = $db->prepare('
				SELECT u.id id,
				u.name name,
				u.email email,
				u.locale locale,
				u.photo photo,
				u.gender gender,
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
				WHERE u.email = :email');
		$user->execute(array(':email' => $email));
		if($user->errorCode() != 0)	{
			$error = $user->errorInfo();
			throw new Exception($error[2]);
		}

		if($user = $user->fetch())	{

			$user =  new User(
					$user[id],
					$user[name],
					$user[email],
					$user[locale],
					$user[photo],
					$user[gender],
					$user[projects],
					$user[part_in]);

			return $user;

		}	else
			throw new Exception("No user with such email.");

		return null;

	}
	

	/**
	 *
	 * Returns user object from Users table using id
	 * as identifier
	 *
	 * @param int $id id that identifies the user in system
	 * @throws Exception
	 * @return User|NULL
	 */
	static function getUserById($id)	{
	
		require_once 'DBO.class.php';
		require_once 'DAL/User.class.php';
	
		$db = DBO::getInstance();
		$user = $db->prepare('
				SELECT u.id id,
				u.name name,
				u.email email,
				u.locale locale,
				u.photo photo,
				u.gender gender,
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
				WHERE u.id = :uid');
		$user->execute(array(':uid' => $id));
		if($user->errorCode() != 0)	{
			$error = $user->errorInfo();
			throw new Exception($error[2]);
		}
	
		if($user = $user->fetch())	{
	
			$user =  new User(
					$user[id],
					$user[name],
					$user[email],
					$user[locale],
					$user[photo],
					$user[gender],
					$user[projects],
					$user[part_in]);
	
			return $user;
	
		}	else
			throw new Exception("No user with such email.");
	
		return null;
	
	}

	/**
	 * Used to add user object to Users table
	 * @param User $user - user object containing field data
	 * @throws Exception - if something odd happens
	 * @return boolean - if adding succeed
	 */
	static function addUser($user)	{

		require_once 'DBO.class.php';
		require_once 'DAL/User.class.php';

		$db = DBO::getInstance();
		$statement = $db->prepare('
				INSERT INTO users(id, email, name, locale, photo, gender)
				VALUES (null, :email, :name, :locale, :photo, :gender)');
		$statement->execute(array(
				':email' => $user->getEmail(),
				':name' => $user->getName(),
				':locale' => $user->getLocale(),
				':photo' => $user->getPhoto(),
				':gender' => $user->getGender()));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		return true;

	}

	/**
	 * Used to remove User from Users table
	 * @param int $uid - user id
	 * @throws Exception - if something odd happens
	 * @return boolean - if adding succeed
	 */
	static function removeUser($uid)	{

		require_once 'DBO.class.php';
		require_once 'DAL/Project.dao.class.php';

		$db = DBO::getInstance();
		
		$db->beginTransaction();
		
		$statement = $db->prepare('
				SELECT id_proj
				FROM project_group
				WHERE id_usr = :uid');
		$statement->execute(array(
				':uid' => $uid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		while($pid = $statement->fetch())	{
			ProjectDAO::removeProjectMemberunSafe($pid[id_proj], $uid);
		}

		$db = DBO::getInstance();
		$statement = $db->prepare('
				DELETE 
				FROM users
				WHERE id = :uid');
		
		$statement->execute(array(
				':uid' => $uid));
		
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		$db->commit();

		return true;

	}

	//static

}