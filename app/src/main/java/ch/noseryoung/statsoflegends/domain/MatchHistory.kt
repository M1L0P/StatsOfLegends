package ch.noseryoung.statsoflegends.domain

class MatchHistory (private var matches: ArrayList<Match> = ArrayList()) {

    private var matchIds: HashSet<String> = HashSet()
    private var onChange: Runnable? = null

    fun setOnChange(onChange: Runnable) {
        this.onChange = onChange
    }

    fun addMatch(match: Match) {
        if(!matchIds.contains(match.matchId)) {
            matchIds.add(match.matchId)
            matches.add(match)

            if(this.onChange != null) {
                this.onChange?.run()
            }
        }
    }

    fun getMatches(): ArrayList<Match> {
        return matches
    }
}