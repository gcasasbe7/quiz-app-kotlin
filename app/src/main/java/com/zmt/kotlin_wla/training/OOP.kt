package com.zmt.kotlin_wla.training

class OOP {

    // Class definition + required parameters to construct the object (PRIMARY CONSTRUCTOR #1)
    // OPEN tag allows other classes to inherit directly from this class
    open class Person(name: String, age : Int) {
        var name : String = ""
        var age : Int = 0

        // Initialization method -> INIT (Called after the primary constructor #2)
        init {
            this.name = name
            this.age = age
        }

        // Constructor  methods -> CONSTRUCTOR (Called after the init method #3)
        // Can handle lines of code
        // Has to contain primary parameters + extra data
        // Now we have 3 different ways to create a Person Object (Primary + 2 constructors)

        constructor(t: String, u : Int, r : String) : this(t,u) {
            this.name += " $r"
        }

        constructor(t: String, u : Int, r : Int) : this(t,u) {
            this.name += " $r"
        }

        // Declaration of Person class methods
        open fun printPerson() {
            print("Name : ${this.name} - ${this.age} years old.")
        }
    }

    // Enumerator with value
    enum class Year(val stringName : String) {
        FIRST("first"), SECOND("second"), THIRD("third"), FOURTH("fourth")
    }

    // INHERITANCE
    class Student(name: String, age: Int, var year: Year) : Person(name, age) {

        // Override parent function
        override fun printPerson(){
            print("Name : ${this.name} - ${this.age} years old is a Student of the ${this.year.stringName} grade.")
        }
    }


    // Example INIT & CONSTRUCTOR
    class Sample(private var s : String) {
        constructor(t: String, u: String) : this(t) {
            this.s += u
        }
        init {
            s += "B"
        }
    }
    /*
    Sample("T", "U")  -> "TBU"

    Sample(s : String) is the primary constructor
    init is called right after the primary constructor
    Then constructor (t:String, u:String) is called
     */
}