package com.example.gametask.utils

import kotlin.random.Random

fun generateRandomTwoNumber(): Pair<Int, Int> {
    val random = Random
    val number1 = random.nextInt(1, 31)
    var number2 = random.nextInt(1, 31)

    // Ensure that the two numbers are different
    while (number1 == number2) {
        number2 = random.nextInt(1, 31)
    }

    return Pair(number1, number2)
}

fun generateResultOption(result: Int) : Array<Int> {
    val options = Array(4) {0}
    val maxNum = result + 10

    val random = Random

    val resultIndex = random.nextInt(0, 3)




    var option1 = random.nextInt(1, maxNum)
    while (option1 == result){
        option1 = random.nextInt(1, maxNum)
    }

    var option2 = random.nextInt(1, maxNum)
    while (option2 == result || option2 == option1){
        option2 = random.nextInt(1, maxNum)
    }

    var option3 = random.nextInt(1, maxNum)
    while (option3 == result || option3 == option1 || option3 == option2){
        option3 = random.nextInt(1, maxNum)
    }



    val list = arrayListOf(option1, option2, option3)

    for (i in 0..options.lastIndex){
        if (i == resultIndex){
            options[i] = result
        }else{
            val firstElement = list.first()
            options[i] = firstElement
            list.remove(firstElement)
        }
    }

    return options
}
