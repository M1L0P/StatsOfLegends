package ch.noseryoung.statsoflegends.domain

class MatchHistory (private var matches: ArrayList<Match> = ArrayList()) {

    private var matchIds: HashSet<String> = HashSet()

    fun addMatch(match: Match) {
        if(!matchIds.contains(match.matchId)) {
            matchIds.add(match.matchId)
            matches.add(match)
        }
    }

    fun getMatches(): ArrayList<Match> {
        return matches

    }
}