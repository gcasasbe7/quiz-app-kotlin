package com.zmt.kotlin_wla.training

class ClassTypes {

    // DATA CLASSES: Main purpose is to hold data
    // Automatically implements these methods for each attribute:
    //equals()/ hashCode() pair
    //toString() of the form "User(name=John, age=42)"
    //componentN() functions corresponding to the properties in their order of declaration.
    //copy() function (see below).
    //val jack = User(name = "Jack", age = 1)
    //val olderJack = jack.copy(age = 10)
    data class User(val name: String, val age: Int)


    // INNER CLASSES: Sub classes that can access the parent class attributes (Opposite as Nested Classes)
    class Person{
        var name : String = ""
        var street : String = ""
        inner class Address{
            private var number : Int = 0 // Not accessible as it's private
            // We can access the person street from the inner class
            var fullAddress = street + " " + this.number // Accessible (see implementation fun below)
        }
    }

    fun implementation(){
        var personExample = Person()
        personExample.Address().fullAddress
    }


}