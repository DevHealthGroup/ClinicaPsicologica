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

class ArtigosAdapter(
    val artigos: List<Artigos>,
    val onClick: (Artigos) -> Unit
): RecyclerView.Adapter<ArtigosAdapter.ArtigosViewHolder>() {

    class ArtigosViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardNome: TextView
        val cardImg: ImageView
        val cardProgress: ProgressBar
        val cardView: CardView

        init {
            cardNome = view.findViewById(R.id.card_nome)
            cardImg = view.findViewById(R.id.card_image)
            cardProgress = view.findViewById(R.id.card_progress)
            cardView = view.findViewById(R.id.card_artigos)
        }
    }

    override fun getItemCount() = this.artigos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ArtigosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_artigos, parent, false)
        val holder = ArtigosViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ArtigosViewHolder, position: Int) {
        val context = holder.itemView.context

        val artigos = this.artigos[position]

        holder.cardNome.text = artigos.nome
        holder.cardProgress.visibility = View.VISIBLE

        Picasso.with(context).load(artigos.foto).fit().into(holder.cardImg,
            object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    holder.cardProgress.visibility = View.GONE
                }

                override fun onError() {
                    holder.cardProgress.visibility = View.GONE
                }
            }
        )

        holder.itemView.setOnClickListener {onClick(artigos)}

    }


}