package com.zmt.kotlin_wla.training

class Generics {


    class Report<T>(data : T){
        var information : T = data
    }

    private fun <T> timesTwo(data : T): Int where T : Number {
        return data.toInt() * 2
    }

    fun example(){
        var stringReport  : Report<String> = Report("Hello boys")
        var integerReport : Report<Int> = Report(25)


        timesTwo(2) // Works
        //timesTwo("2") // Doesn't work because of 'where' condition on function definition
    }
}