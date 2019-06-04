package ch.noseryoung.statsoflegends

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.persistence.StaticManager
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import ch.noseryoung.statsoflegends.persistence.ImageType


class SummaryFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_summary, container, false)

        update(root)

        DataHolder.matchHistory.addOnChange(Runnable {
            root.post {
                update(root)
            }
        })

        return root
    }

    fun update(root: View) {
        root.findViewById<TextView>(R.id.txtSumName).text = DataHolder.summoner.nameBeauty
        root.findViewById<TextView>(R.id.txtSumLvl).text =
            "Level ${DataHolder.summoner.level}"

        // Summoner icon
        root.findViewById<ImageView>(R.id.imgSumIcon).setImageBitmap(StaticManager.get(DataHolder.summoner.icon, ImageType.ICON, context!!))

        // Champion images
        root.findViewById<ImageView>(R.id.imgSumC1).setImageBitmap(
            StaticManager.get(DataHolder.getPrimaryChampion(), ImageType.CHAMPION, context!!))
        root.findViewById<ImageView>(R.id.imgSumC2).setImageBitmap(
            StaticManager.get(DataHolder.getSecondaryChampion(), ImageType.CHAMPION, context!!))

        // Win rate
        root.findViewById<ProgressBar>(R.id.progWon).progress = DataHolder.getWinRate()
        root.findViewById<ProgressBar>(R.id.progWon).progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.winColor))
        root.findViewById<TextView>(R.id.txtSumWin).text = "${DataHolder.getWinRate()}% won"

        // Primary champion play rate bar
        root.findViewById<ProgressBar>(R.id.progPrimary).progress = DataHolder.getPrimaryChampionPickRate()
        root.findViewById<ProgressBar>(R.id.progPrimary).progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.played))
        root.findViewById<TextView>(R.id.txtSumPrimaryPick).text = "${DataHolder.getPrimaryChampionPickRate()}% picked"

        // Primary champion win rate bar
        root.findViewById<ProgressBar>(R.id.progPrimaryWin).progress = DataHolder.getPrimaryChampionWinRate()
        root.findViewById<ProgressBar>(R.id.progPrimaryWin).progressTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.winColor))
        root.findViewById<TextView>(R.id.txtSumPrimaryWin).text = "${DataHolder.getPrimaryChampionWinRate()}% won"


        // Secondary champion play rate bar
        root.findViewById<ProgressBar>(R.id.progSecondary).progress = DataHolder.getSecondaryChampionPickRate()
        root.findViewById<ProgressBar>(R.id.progSecondary).progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.played))
        root.findViewById<TextView>(R.id.txtSumSecondaryPick).text = "${DataHolder.getSecondaryChampionPickRate()}% picked"

        // Secondary champion win rate bar
        root.findViewById<ProgressBar>(R.id.progSecondaryWin).progress = DataHolder.getSecondaryChampionWinRate()
        root.findViewById<ProgressBar>(R.id.progSecondaryWin).progressTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.winColor))
        root.findViewById<TextView>(R.id.txtSumSecondaryWin).text = "${DataHolder.getSecondaryChampionWinRate()}% won"


        // Ranking Solo/Duo
        root.findViewById<TextView>(R.id.txtSumRankedSolo).text = "${DataHolder.summoner.solo.tier} ${DataHolder.summoner.solo.rank}"
        if(!DataHolder.summoner.solo.rank.isEmpty()) {
            root.findViewById<TextView>(R.id.txtSumRankedSoloPoints).text = "${DataHolder.summoner.solo.leaguePoints} LP"
        } else {
            root.findViewById<TextView>(R.id.txtSumRankedSoloPoints).visibility = View.GONE
        }
        root.findViewById<ConstraintLayout>(R.id.constraintLayoutSumSolo).background =
            ColorDrawable(ContextCompat.getColor(context!!, getRankColor(DataHolder.summoner.solo.tier)))

        // Ranking Flex
        root.findViewById<TextView>(R.id.txtSumRankedFlex).text = "${DataHolder.summoner.flex.tier} ${DataHolder.summoner.flex.rank}"
        if(!DataHolder.summoner.flex.rank.isEmpty()) {
            root.findViewById<TextView>(R.id.txtSumRankedFlexPoints).text = "${DataHolder.summoner.flex.leaguePoints} LP"
        } else {
            root.findViewById<TextView>(R.id.txtSumRankedFlexPoints).visibility = View.GONE
        }
        root.findViewById<ConstraintLayout>(R.id.constraintLayoutSumFlex).background =
            ColorDrawable(ContextCompat.getColor(context!!, getRankColor(DataHolder.summoner.flex.tier)))
    }

    fun getRankColor(rank: String) : Int {
        Log.e("MilooliM", "Rank: "+rank)
        when(rank.toLowerCase()) {
            "iron" -> return R.color.ironElo
            "bronze" -> return R.color.bronzeElo
            "silver" -> return R.color.silverElo
            "gold" -> return R.color.goldElo
            "platin" -> return R.color.platElo
            "diamond" -> return R.color.diamondElo
            "master" -> return R.color.masterElo
            "grandmaster" -> return R.color.graMasterElo
            "challenger" -> return R.color.challengerElo
            else -> return R.color.unrankedElo
        }
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
    }
}