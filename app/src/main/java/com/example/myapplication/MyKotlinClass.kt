package com.example.myapplication

/**
 * 创建时间: 2022/01/25 16:05 <br>
 * 作者: leo <br>
 * 描述:
 */
class MyKotlinClass {
  fun sum(a: Int, b: Int): Int {
    return a + b;
  }

  public fun delete(a: Int, b: Int): Int = a + b;

  public fun sumArrays(vararg array1: Int) {
  }

  var age: String? = "23"

  class TestNestedClass  {

    fun TestNestedFun() {
      print("kotlin test for leo")
    }

    var name: String = "leo"
    var url: String = "www.baidu.com"
    var city: String = "beijing"

  }

  fun main(args: Array<String>){
    var testNestedClass: TestNestedClass = TestNestedClass();
    testNestedClass.city = "jiangxi"


  }

}