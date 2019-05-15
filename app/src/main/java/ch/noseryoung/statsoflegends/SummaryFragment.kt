package ch.noseryoung.statsoflegends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.noseryoung.statsoflegends.data.DataHolder

class SummaryFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_summary, container, false)

        root.findViewById<TextView>(R.id.txtSumName).text = DataHolder.summoner.nameBeauty
        root.findViewById<TextView>(R.id.txtSumLvl).text =
            "Level $DataHolder.summoner.level.toString()"

        return root
    }
}