package com.faithl.raphael.vo

class RapResult {

    var code: Int = 0
    var message: String? = null
    var data: Any? = null

    constructor(code: Int, message: String, data: Any) {
        this.code = code
        this.message = message
        this.data = data
    }

    constructor(code: Int, message: String) {
        this.code = code
        this.message = message
    }

    constructor(code: Int, data: Any) {
        this.code = code
        this.data = data
    }

    constructor(code: Int) {
        this.code = code
    }

    constructor()

    companion object {
        val SUCCESS = RapResult(10000, "success")
        val FAIL = RapResult(10001, "fail")
    }

}