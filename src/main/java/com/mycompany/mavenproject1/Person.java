/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author Dubois Michel
 */
public class Person {
    public String firstName;
	public String lastName;
	public int age;
	public Person() {		
	}
	public Person(String firstName, String lastName,
				int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	public String toString() {
	    return "[" + firstName + " " + lastName +
		       " " + age +"]";
	}
}
