<?php

/**
 * Describes Comment entity
 * @author Akira
 *
 */
class Comment {
	
	private $id;
	private $owner;
	private $text;
	private $author;
	private $time;
	
	function __construct($id, $owner, $text, $author, $time){
		
		$this->setId($id);
		$this->setOwner($owner);
		$this->setText($text);
		$this->setAuthor($author);
		$this->setTime($time);
		
	}
	
	function __destruct(){
		
		unset($this->id);
		unset($this->owner);
		unset($this->text);
		unset($this->author);
		unset($this->time);
		
	}
	
	/**
	 * Getters
	 */
	
	function getId(){
		
		return $this->id;
		
	}
	
	function getOwner(){
		
		return $this->owner;
		
	}
	
	function getText(){
		
		return $this->text;
		
	}
	
	function getAuthor(){
		
		return $this->author;
		
	}
	
	function getTime(){
		
		return $this->time;
		
	}
	
	/**
	 * Setters
	 */
	
	function setId($id){
		
		$this->id = $id;
		
	}
	
	function setOwner($owner){
		
		$this->owner = $owner;
		
	}
	
	function setText($text){
		
		$this->text = $text;
		
	}

	function setAuthor($author){
		
		$this->author = $author;
		
	}
	
	function setTime($time){
		
		$this->time = $time;
		
	}
	
	static function serialize($comment){

		$array[id] = $comment->getId();
		$array[owner] = $comment->getOwner();
		$array[text] = $comment->getText();
		$array[author] = $comment->getAuthor();
		$array[time] = $comment->getTime();
		
		return $array;
		
	}
	
	
}
