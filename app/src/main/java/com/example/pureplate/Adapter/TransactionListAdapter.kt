package com.example.pureplate.Adapter


import Transaction
import Transactions.listTransactions
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pureplate.MenuActivity
import com.example.pureplate.R
import com.google.firebase.firestore.FirebaseFirestore


class TransactionListAdapter(var dataset: ArrayList<Transaction>) : RecyclerView.Adapter<TransactionListAdapter.Holder>(){
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var transactions: Transaction
        lateinit var db: FirebaseFirestore

        //        bind view
        var imgFoodBuy : ImageView = itemView.findViewById(R.id.imgFoodBuy)
        var txtFoodBuy : TextView = itemView.findViewById(R.id.txtFoodBuy)
        var txtFoodBuyQty: TextView = itemView.findViewById(R.id.txtFoodBuyQty)
        var txtFoodBuyPrice: TextView = itemView.findViewById(R.id.txtFoodBuyPrice)

        fun bind(transaction: Transaction)
        {
            this.transactions = transaction


            val resourceId = itemView.context.resources.getIdentifier(transactions.restoImg.toString(), "drawable", itemView.context.packageName)
            imgFoodBuy.setImageResource(resourceId)
            txtFoodBuy.text = if (transaction.foodName.length > 20) {
                transaction.foodName.substring(0, 20) + "..."
            } else {
                transaction.foodName
            }
            txtFoodBuyQty.text = transactions.transactionQty.toString()
            txtFoodBuyPrice.text = transactions.transactionPrice.toString()



            val updateButton = itemView.findViewById<Button>(R.id.UpdateFoodBtn)
            updateButton.setOnClickListener {
                navigateToCarDetails(transaction.foodName, transaction.transactionQty)
            }

            val deleteButton = itemView.findViewById<Button>(R.id.DeleteFoodBtn)
            deleteButton.setOnClickListener {
                removeTransaction(transaction)

            }

        }
        fun navigateToCarDetails(carName: String, transactionQty: Int) {
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

        fun removeTransaction(transaction: Transaction) {
            listTransactions.remove(transaction)
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_transaction_food,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listTransactions.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var transactions = dataset.get(position)
        holder.bind(transactions)
    }
}

