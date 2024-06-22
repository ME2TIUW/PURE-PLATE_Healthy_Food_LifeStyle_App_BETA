//package com.example.pureplate.models
//

data class Transaction(
    var restoImg : Int,
    var transactionQty : Int,
    var transactionPrice: Double,
    var foodName : String,
    var uid: String,
    var email: String,
    var documentId: String
)

object Transactions {
    var listTransactions : ArrayList<Transaction> = ArrayList()

    fun addTransactions(restoImg: Int, transactionQty: Int, transactionPrice: Double, foodName: String, uid: String, email: String, documentId: String)
    {
        val transactionId = listTransactions.size + 1
        listTransactions.add(Transaction(restoImg, transactionQty,transactionPrice,foodName, uid, email, documentId))
    }
}
