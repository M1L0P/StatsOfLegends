package ch.noseryoung.statsoflegends

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.IntProperty
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
import kotlinx.android.synthetic.main.fragment_summary.*
import android.util.DisplayMetrics
import kotlin.math.roundToInt


class SummaryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_summary, container, false)

        root.findViewById<TextView>(R.id.txtSumName).text = DataHolder.summoner.nameBeauty
        root.findViewById<TextView>(R.id.txtSumLvl).text =
            "Level ${DataHolder.summoner.level}"

        root.findViewById<ImageView>(R.id.imgSumIcon).setImageBitmap(StaticManager.getProfileIcon(context!!, DataHolder.summoner.icon))

        root.findViewById<ImageView>(R.id.imgSumC1).setImageBitmap(
            StaticManager.getChampionIcon(context!!, DataHolder.getPrimaryChampion()))
        root.findViewById<ImageView>(R.id.imgSumC2).setImageBitmap(
            StaticManager.getChampionIcon(context!!, DataHolder.getSecondaryChampion()))

        //root.findViewById<TextView>(R.id.txtC1Games).text =
        //    "${DataHolder.getPrimaryChampionRate()}%"
        root.findViewById<TextView>(R.id.txtC2Games).text =
            "${DataHolder.getSecondaryChampionRate()}%"

        return root
    }

    override fun onResume() {
        super.onResume()

        val fullWidth = constraintLayout3.measuredWidth

        Log.e("MilooliM", "Measured: "+fullWidth)
        Log.e("MilooliM", "C1: "+fullWidth.div(100).times(DataHolder.getPrimaryChampionRate()))
        Log.e("MilooliM", "C2: "+fullWidth.div(100).times(DataHolder.getSecondaryChampionRate()))


        progPrimary.progress = DataHolder.getPrimaryChampionRate()

        progPrimary.getProgressDrawable().setColorFilter(
            Color.BLUE, PorterDuff.Mode.SRC_IN);

        progPrimary.setProgressTintList(ColorStateList.valueOf(Color.RED));

        progPrimary.scaleY = 10f
    }
}