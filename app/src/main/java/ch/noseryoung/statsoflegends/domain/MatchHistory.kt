package ch.noseryoung.statsoflegends.domain

class MatchHistory (private var matches: ArrayList<Match> = ArrayList()) {

    private var matchIds: HashSet<String> = HashSet()
    private var onChangeList: ArrayList<Runnable> = ArrayList()

    fun addOnChange(onChange: Runnable) {
        this.onChangeList.add(onChange)
    }

    fun addMatch(match: Match) {
        if(!matchIds.contains(match.matchId)) {
            matchIds.add(match.matchId)
            matches.add(match)

            for(onChange in onChangeList) {
                onChange.run()
            }
        }
    }

    fun getMatches(): ArrayList<Match> {
        return matches
    }
}