package com.clinica.devhealthgroup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

/* cards com recycler view*/

/*Onde cada objeto da lista será um item na lista*/
class ConsultasAdapter(
    val consultas: List<Consultas>,
    val onClick: (Consultas) -> Unit
): RecyclerView.Adapter<ConsultasAdapter.ConsultasViewHolder>() {

    /* parte visual de cada item da lista*/
    class ConsultasViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardDia: TextView
        val cardHora: TextView
        val cardPaciente: TextView
        val cardStatus: TextView
        val cardView: CardView

        init {
            cardDia = view.findViewById(R.id.card_dia)
            cardHora = view.findViewById(R.id.card_hora)
            cardPaciente = view.findViewById(R.id.card_paciente)
            cardStatus = view.findViewById(R.id.card_status)
            cardView = view.findViewById(R.id.card_consultas)
        }
    }

    override fun getItemCount() = this.consultas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ConsultasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_consultas, parent, false)
        val holder = ConsultasViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ConsultasViewHolder, position: Int) {
        val context = holder.itemView.context

        val consultas = this.consultas[position]

        holder.cardDia.text = "Data da consulta: ${consultas.data}"
        holder.cardHora.text = "Horário da consulta: ${consultas.horario}"
        holder.cardPaciente.text = "Id do paciente: ${consultas.paciente}"
        holder.cardStatus.text = "Status da consulta: ${consultas.status}"
        holder.itemView.setOnClickListener {onClick(consultas)}
       // holder.cardProgress.visibility = View.VISIBLE

        /*Picasso.with(context).load(consultas.foto).fit().into(holder.cardImg,
            object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    holder.cardProgress.visibility = View.GONE
                }

                override fun onError() {
                    holder.cardProgress.visibility = View.GONE
                }
            }
        )*/
    }
}