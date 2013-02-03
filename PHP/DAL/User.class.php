<?php

/**
 *
 * This class describes a system user POJO class
 *
 * @author Akira
 */
class User {

	private $id;
	private $name;
	private $email;
	private $locale;
	private $photo;
	private $gender;
	private $projects_created;
	private $projects_part_in;

	function __construct($id, $name, $email, $locale, $photo, $gender, $projects_created, $projects_part_in) {

		$this->setId($id);
		$this->setName($name);
		$this->setEmail($email);
		$this->setLocale($locale);
		$this->setPhoto($photo);
		$this->setGender($gender);
		$this->setProjectsCreated($projects_created);
		$this->setProjectsPartIn($projects_part_in);

	}

	function __destruct() {

		unset($this->id);
		unset($this->name);
		unset($this->email);
		unset($this->locale);
		unset($this->photo);
		unset($this->gender);
		unset($this->projects_created);
		unset($this->projects_part_in);

	}

	static function serialize($user)	{

		$array[id] = $user->getId();
		$array[name] = $user->getName();
		$array[email] = $user->getEmail();
		$array[locale] = $user->getLocale();
		$array[photo] = $user->getPhoto();
		$array[gender] = $user->getGender();
		$array[projects_created] = $user->getProjectsCreated();
		$array[projects_part_in] = $user->getProjectsPartIn();

		return $array;
	}

	/*
	 * Getters
	*/
	function getId() {
		return $this->id;
	}

	function getName() {
		return $this->name;
	}

	function getEmail() {
		return $this->email;
	}

	function getLocale() {
		return $this->locale;
	}

	function getPhoto() {
		return $this->photo;
	}

	function getGender() {
		return $this->gender;
	}

	function getProjectsCreated() {
		return $this->projects_created;
	}

	function getProjectsPartIn() {
		return $this->projects_part_in;
	}

	/*
	 * Setters
	*/
	function setId($id) {
		$this->id = $id;
	}

	function setName($name) {
		$this->name = $name;
	}

	function setEmail($email) {
		$this->email = $email;
	}

	function setLocale($locale) {
		$this->locale = $locale;
	}

	function setPhoto($photo) {
		$this->photo = $photo;
	}

	function setGender($gender) {
		$this->gender = $gender;
	}

	function setProjectsCreated($projects_created) {
		$this->projects_created = $projects_created;
	}

	function setProjectsPartIn($projects_part_in) {
		$this->projects_part_in = $projects_part_in;
	}
}
