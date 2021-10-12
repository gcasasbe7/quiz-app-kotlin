package com.zmt.kotlin_wla.training

class KotlinHelper {

    // IMMUTABLE CONSTANT VALUE
    val finalValue : String = "Immutable value"

    // NUMBER
    var byt : Byte = 1              // Size: 8 bits [1 ... 255]
    var short : Short = 20          // Size: 16 bits
    var int : Int = 100             // Size: 32 bits
    var long : Long = 200000        // Size: 64 bits

    // DECIMAL NUMBER
    var float : Float = 3.2f        // Size: 32 bits
    var double : Double = 3.4       // Size: 64 bits

    // CHARACTER
    var char : Char = 'e'           // Size: 8 to 16 bits

    // LISTS
    var items : Array<Int> = arrayOf(1,2,3,4,5,6,7,8,9)
    var items2 : Array<Int> = Array(10) { i -> i * 2 }

    // NEW WAY TO CONCATENATE VALUES WITHIN STRINGS
    fun newFormatString() : String {
        return "The final value is $finalValue"
    }

    // RANGE, PROGRESSIONS AND FOREACH
    fun loopRange() : String {
        var s : String = ""

        var range : IntRange = 1..100
        var reverseRange : IntProgression = 100 downTo 1
        var stepProg : IntProgression = 1..100 step 5
        var untilProg : IntProgression = 1 until 1000 // [1,1000)

        for (i : Int in reverseRange) {
            s += i
        }

        return s
    }

    // LAMBDAS
    fun lambda() : String {

        // Variable firstLambda of type function with 2 String parameters returns a boolean. True if both Strings are equal
        var firstLambda : (String,String) -> Boolean = {
            a: String, b: String -> a == b
        }

        return if (firstLambda("Text 1", "Text 2")) "Correct" else "Incorrect"
    }
}