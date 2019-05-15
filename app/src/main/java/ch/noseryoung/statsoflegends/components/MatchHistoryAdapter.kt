package ch.noseryoung.statsoflegends.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ch.noseryoung.statsoflegends.R
import ch.noseryoung.statsoflegends.domain.MatchHistory
import ch.noseryoung.statsoflegends.persistence.StaticManager
import kotlin.math.roundToInt
import android.graphics.drawable.BitmapDrawable



class MatchHistoryAdapter(private val context: Context, private var data: MatchHistory) :
    RecyclerView.Adapter<MatchHistoryAdapter.MatchHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHistoryAdapter.MatchHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.rcy_match_entry, parent, false)
        return MatchHolder(root)
    }

    override fun onBindViewHolder(holder: MatchHolder, position: Int) {
        val match = data.getMatches()[position]
        val kills = match.kda.kills
        val deaths = match.kda.deaths
        val assists = match.kda.assists
        holder.txtKDA.text = "${kills}/${deaths}/${assists}"
        if(match.kda.deaths == 0) {
            holder.txtKDACalc.text = "Perf"
        } else {
            holder.txtKDACalc.text = match.kda.kda.roundToInt().toString()
        }
        holder.txtGameType.text = match.gameType
        holder.txtChampionName.text = match.championName


        // Images
        holder.imgChamp.setImageBitmap(StaticManager.getChampionIcon(context, match.championName))
        holder.imgItem1.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[0]))
        holder.imgItem2.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[1]))
        holder.imgItem3.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[2]))
        holder.imgItem4.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[3]))
        holder.imgItem5.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[4]))
        holder.imgItem6.setImageBitmap(StaticManager.getItemIcon(context, match.itemID[5]))

        holder.imgSum1.setImageBitmap(StaticManager.getSpellIcon(context, match.summonerSpellIDs.first))
        holder.imgSum2.setImageBitmap(StaticManager.getSpellIcon(context, match.summonerSpellIDs.second))

        holder.background.setImageBitmap(StaticManager.getChampionSplash(context, match.championName))

    }

    override fun getItemCount() = data.getMatches().size

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
        var imgSum1: ImageView = itemView.findViewById(R.id.imgSum1)
        var imgSum2: ImageView = itemView.findViewById(R.id.imgSum2)
        var txtGameType: TextView = itemView.findViewById(R.id.txtGameType)
        var txtChampionName: TextView = itemView.findViewById(R.id.txtChampionName)
        var background: ImageView = itemView.findViewById(R.id.imgBackground)
    }
}
