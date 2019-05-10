package ch.noseryoung.statsoflegends.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.noseryoung.statsoflegends.R
import ch.noseryoung.statsoflegends.domain.Match
import ch.noseryoung.statsoflegends.domain.MatchHistory

class MatchHistoryAdapter(private var data: MatchHistory) :
    RecyclerView.Adapter<MatchHistoryAdapter.MatchHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHistoryAdapter.MatchHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.rcy_match_entry, parent, false)
        return MatchHolder(root)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MatchHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtKDACalc.text = data.matches[position].kda.kda.toString()
        val kills = data.matches[position].kda.kills
        val deaths = data.matches[position].kda.deaths
        val assists = data.matches[position].kda.assists
        holder.txtKDACalc.text = "${kills}/${deaths}/${assists}"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.matches.size

    class MatchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgChamp: ImageView = itemView.findViewById(R.id.imgChamp)
        var txtKDA: TextView = itemView.findViewById(R.id.txtKDA)
        var txtKDACalc: TextView = itemView.findViewById(R.id.txtKDACalc)
        var imgItem1: ImageView = itemView.findViewById(R.id.imgItem1)
        var imgItem2: ImageView = itemView.findViewById(R.id.imgItem2)
        var imgItem3: ImageView = itemView.findViewById(R.id.imgItem3)
        var imgItem4: ImageView = itemView.findViewById(R.id.imgItem4)
        var imgItem5: ImageView = itemView.findViewById(R.id.imgItem5)
        var imgItem6: ImageView = itemView.findViewById(R.id.imgItem6)
        var imgSum1: ImageView = itemView.findViewById(R.id.txtGameType)
        var imgSum2: ImageView = itemView.findViewById(R.id.imgSum2)
        var txtGameType: ImageView = itemView.findViewById(R.id.txtGameType)

        fun init(match: Match) {

        }
    }
}
