package ch.noseryoung.statsoflegends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.noseryoung.statsoflegends.components.MatchHistoryAdapter
import ch.noseryoung.statsoflegends.data.DataHolder
import ch.noseryoung.statsoflegends.net.MatchFactory
import kotlinx.android.synthetic.main.fragment_summary.*

class MatchHistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_matchhistory, container, false)

        root.findViewById<RecyclerView>(R.id.rcyHistory).adapter = MatchHistoryAdapter(context!!, DataHolder.matchHistory)
        root.findViewById<RecyclerView>(R.id.rcyHistory).layoutManager = LinearLayoutManager(context)

        DataHolder.matchHistory.setOnChange(Runnable {
            activity?.runOnUiThread {
                (root.findViewById<RecyclerView>(R.id.rcyHistory).adapter as MatchHistoryAdapter).notifyDataSetChanged()
            }
        })

        return root
    }
}
