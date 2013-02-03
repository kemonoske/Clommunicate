<?php

/**
 * Describes task entity
 * @author Akira
 *
 */
class Task {
	private $id;
	private $name;
	private $description;
	private $start_date;
	private $end_date;
	private $type;
	private $completed;
	private $owner;
	private $assigned;
	
	function __construct($id, $name, $description, $start_date, $end_date, $type, $completed, $owner, $assigned){
		
		$this->setId($id);
		$this->setName($name);
		$this->setDescription($description);
		$this->setStartDate($start_date);
		$this->setEndDate($end_date);
		$this->setType($type);
		$this->setCompleted($completed);
		$this->setOwner($owner);
		$this->setAssigned($assigned);
		
	}
	
	function __destruct(){
		
		unset($this->id);
		unset($this->name);
		unset($this->description);
		unset($this->start_date);
		unset($this->end_date);
		unset($this->type);
		unset($this->completed);
		unset($this->owner);
		unset($this->assigned);
		
	}
	
	/**
	 * Getters
	 */
	function getId(){
		
		return $this->id;
		
	}
	
	function getName(){
		
		return $this->name;
		
	}

	function getDescription(){
		
		return $this->description;
				
	}
	
	function getStartDate(){
		
		return $this->start_date;
		
	}

	function getEndDate(){
		
		return $this->end_date;
		
	}
	
	function getType(){
		
		return $this->type;
		
	} 
	
	function isCompleted(){
		
		return $this->completed;
		
	}
	
	function getOwner(){
		
		return $this->owner;
		
	}
	
	function getAssigned(){
		
		return $this->assigned;
		
	}

	/**
	 * Setters
	 */
	
	function setId($id){
		
		$this->id = $id;
		
	}
	
	function setName($name){
		
		$this->name = $name;
		
	}

	function setDescription($description){
		
		$this->description = $description;
		
	}
	
	function setStartDate($start_date){
		
		$this->start_date = $start_date;
		
	}
	
	function setEndDate($end_date){
		
		$this->end_date = $end_date;
		
	}
	
	function setCompleted($completed){
		
		$this->completed = $completed;
		
	}
	
	function setType($type){
		
		$this->type = $type;
		
	}
	
	function setOwner($owner){
		
		$this->owner = $owner;
		
	}
	
	function setAssigned($assigned){
		
		$this->assigned =  $assigned;
		
	}
	
	static function serialize($task){
		
		$array[id] = $task->getId();
		$array[name] = $task->getName();
		$array[description] = $task->getDescription();
		$array[start_date] = $task->getStartDate();
		$array[end_date] = $task->getEndDate();
		$array[type] = $task->getType();
		$array[completed] = $task->isCompleted();
		$array[owner] = $task->getOwner();
		$array[assigned] = $task->getAssigned();
		
		return $array;
	}
	
}


