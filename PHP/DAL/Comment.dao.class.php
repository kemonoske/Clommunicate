<?php


/**
 * User to describe connection between Comment entity 
 * and comments table
 * @author Akira
 *
 */
class CommentDAO {
	
	const ADD = 1;
	const REMOVE = 2;
	const LIST_BY_TASK_ID = 3;

	/**
	 * Checks if comment with such id is contained in comments table
	 * @param int $ident
	 */
	static function exists($ident) {
	
		require_once 'DBO.class.php';
	
		$db = DBO::getInstance();
		$ids = $db->prepare('SELECT id FROM comments');
		$ids->execute();
		if($ids->errorCode() != 0)	{
			$error = $ids->errorInfo();
			throw new Exception($error[2]);
		}
	
		while($id = $ids->fetch())
			if($ident == $id[id])
				return true;
	
		return false;
	}
	
	/**
	 * Adds comment object to database
	 * @param Comment $comment
	 * @throws Exception
	 * @return boolean
	 */
	static function addComment($comment)	{

		require_once 'DBO.class.php';
		require_once 'Comment.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('
				INSERT INTO comments(id, owner, author, text, time)
				VALUES (null, :owner, :author, :text, null)');
		$statement->execute(array(
				':owner' => $comment->getOwner(),
				':author' => $comment->getAuthor(),
				':text' => $comment->getText()));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		return true;
		
	}
	
	/**
	 * Deletes  comment from database using id
	 * @param int $cid - comment id
	 * @throws Exception
	 * @return boolean
	 */
	static function removeComment($cid)	{

		require_once 'DBO.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('DELETE 
				FROM comments
				WHERE id = :cid');
		$statement->execute(array(
				':cid' => $cid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		return true;
		
		
	}
	
	/**
	 * return s a list of comments using task id
	 * @param int $tid - task id
	 * @throws Exception
	 * @return boolean
	 */
	static function listCommentsByTaskId($tid){
		
		require_once 'DBO.class.php';
		require_once 'Comment.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('
				SELECT *
				FROM comments
				WHERE owner = :tid');
		$statement->execute(array(
				':tid' => $tid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}

		$i = 0;
		
		while($row = $statement->fetch())	{
		
			$comment = new Comment(
					$row[id],
					$row[owner], 
					$row[text], 
					$row[author], 
					$row[time]);
			
			$comments[$i++] = Comment::serialize($comment);
		}
		
		return $comments;
		
	}

	/**
	 * return last comment in a task
	 * @param int $tid - task id
	 * @throws Exception
	 * @return boolean
	 */
	static function getLastComment($tid){
	
		require_once 'DBO.class.php';
		require_once 'Comment.class.php';
	
		$db = DBO::getInstance();
	
		$statement = $db->prepare('
				SELECT *
				FROM comments
				WHERE id = (
					SELECT MAX(id)
					FROM comments
					WHERE owner = :tid
				)');
		$statement->execute(array(
				':tid' => $tid));
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
	
		if($row = $statement->fetch())	{
	
			$comment = new Comment(
					$row[id],
					$row[owner],
					$row[text],
					$row[author],
					$row[time]);
				
			$comment = Comment::serialize($comment);
				
		}
	
		return $comment;
	
	}
	
	
	/**
	 * return number of coments that apeared 
	 * after coment id received
	 * @param int $cid - comment id
	 * @param int $tid - task id
	 * @throws Exception
	 * @return boolean
	 */
	static function countAfter($cid, $tid){
		
		require_once 'DBO.class.php';
		require_once 'Comment.class.php';
		
		$db = DBO::getInstance();
		
		$statement = $db->prepare('
			SELECT COUNT(*) count
			FROM comments
			WHERE id > :cid
			AND owner = :tid');
		$statement->execute(array(
				':cid' => $cid,
				':tid' => $tid));
		
		if($statement->errorCode() != 0)	{
			$error = $statement->errorInfo();
			throw new Exception($error[2]);
		}
		
		if($row = $statement->fetch())	{
		
			$count = $row[count];
			
		}
		
		return $count;
		
	}
	
}