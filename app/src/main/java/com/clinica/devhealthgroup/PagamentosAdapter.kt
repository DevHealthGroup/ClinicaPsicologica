package com.clinica.devhealthgroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/* cards com recycler view*/

/*Onde cada objeto da lista será um item na lista*/
class PagamentosAdapter(
    val pagamentos: List<Pagamentos>,
    val onClick: (Pagamentos) -> Unit
): RecyclerView.Adapter<PagamentosAdapter.PagamentosViewHolder>() {

    /* parte visual de cada item da lista*/
    class PagamentosViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardStatus: TextView
        val cardConsulta: TextView
        val cardValor: TextView
        val cardView: CardView

        init {
            cardStatus = view.findViewById(R.id.card_status)
            cardConsulta = view.findViewById(R.id.card_consulta)
            cardValor = view.findViewById(R.id.card_valor)
            cardView = view.findViewById(R.id.card_pagamentos)
        }
    }

    override fun getItemCount() = this.pagamentos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PagamentosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pagamentos, parent, false)
        val holder = PagamentosViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: PagamentosViewHolder, position: Int) {
        val context = holder.itemView.context

        val pagamentos = this.pagamentos[position]

        holder.cardStatus.text = "Status: ${pagamentos.status}"
        holder.cardConsulta.text = "Id da consulta: ${pagamentos.consulta}"
        holder.cardValor.text = "Valor: ${pagamentos.valor}"
        holder.itemView.setOnClickListener {onClick(pagamentos)}

    }
}