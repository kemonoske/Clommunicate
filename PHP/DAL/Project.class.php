<?php

/**
 * Describes Project entity
 * @author Akira
 *
 */

class Project {

	private $id;
	private $name;
	private $description;
	private $start_date;
	private $deadline;
	private $end_date;
	private $owner;

	function __construct($id, $name, $description, $start_date, $deadline, $end_date, $owner){

		$this->setId($id);
		$this->setName($name);
		$this->setDescription($description);
		$this->setStartDate($start_date);
		$this->setDeadline($deadline);
		$this->setEndDate($end_date);
		$this->setOwner($owner);
		
	}
	
	function __destruct(){
		
		unset($this->id);
		unset($this->name);
		unset($this->description);
		unset($this->start_date);
		unset($this->deadline);
		unset($this->end_date);
		unset($this->owner);
		
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
	
	function getDeadline(){
		
		return $this->deadline;
		
	}
	
	function getEndDate(){
		
		return $this->end_date;
		
	}
	
	function getOwner(){
		
		return $this->owner;
		
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
	
	function setDeadline($deadline){
		
		$this->deadline = $deadline;
		
	}
	
	function setEndDate($end_date){
		
		$this->end_date = $end_date;
		
	}
	
	function setOwner($owner){
		
		$this->owner = $owner;
		
	}
	
	static function serialize($project){
		
		$array[id] = $project->getId();
		$array[name] = $project->getName();
		$array[description] = $project->getDescription();
		$array[start_date] = $project->getStartDate();
		$array[deadline] = $project->getDeadline();
		$array[end_date] = $project->getEndDate();
		$array[owner] = $project->getOwner();
		
		return $array;
		
	}
	
}
