# Stats of Legends

## Idee und Grundkonzept

Stats of Legends ist eine native andorid applikation welche wir im Rahmen des UK355 planen und umsetzen. Benutzer der App können zusammengefasste Spielerstatistiken abfragen. Das spiel auf welche sich unsere App bezieht heisst League of Legends. Die Daten werden von der Riot Games API bezogen. Wir werden im nachfolgenden Dokument daher das vokabular verwenden welches auch Riot Games selbsts verwendet. Ein Glossar finden sie im nachfolgenden.

## Use cases

### #1: Summoner match history abfragen

Der Benutzer sollte eine match history eines summoners abragen koennen. Die match history beinhaltet verschiedene Details ueber die vergangenen Spiele des Spielers. Folgende Inormationen sollten jeweils pro Spieler angezeigt werden.

- Champion Bild
- Kills/Deaths/Assists Werte

  - Incl. Zusammengerechneter Wert
- Benutzte Items
- Benutzter Keystone
- Sieg, Niederlage oder remake
- Spieltyp (Blind, Draft, Solo/Duo, Flex)

### #2: Summoner summary abfragen

Der Benutzer sollte die Zusammenfassung eines Summoners abfragen koennen. Die Summary fasst das Spielverhalten der letzten 20 oder 10 Spielen eines Summoners zusammen. Die Zusammenassung enthaelt folgende Details.

- Icon des Summoners
- Name und Level des Summoners
- Solo/Duo und FlexRang des Summoners
- Siegesrate des Summoners
- 2 am meissten gespielte Champions
  - Bild des Champions
  - Spielrate des Summoners auf dem Champion
  - Siegesrate des Summoners auf dem Champion



## Über die Api

Um an alle Informationen zu kommen die wir brauchen müssen wir einerseits die "normale" Riot Api anragen. Auf der anderen Seite müssen wir aber auch auf die sogenannten "Static endpoints" der Api zugreiffen. 

### Static endpoints

Diese umfassen Daten wie Icons und Url zu Bildern von items und Champions aus dem Spiel. Diese sind Relevant für die anzeige in unserem GUI und da Riot Games von Zeit zu Zeit diese Grafiken erneuert müssen diese Automatisch in unsere Applikation geladen werden. Da die Statischen endpunkte jedoch eine sehr strikte Limitierung von 10 Anfragen in der Stunde hat können wir diese Daten nicht bei jedem Appstart erneuern. Stattdessen werden wir diese entsprechend den zwei Wöchigen updates neu laden.

### Dynamische endpoints

Diese umfassen die Daten vom Spieler aus welchen wir dann die match history und summary page generieren. Der grundablauf sieht in etwa so aus.

| Daten         | Von           |
| ------------- | ------------- |
| Summoner Name | UI input      |
| Account ID    | Summoner Name |
| Match history | Account ID    |
| Match IDs     | Match history |
| Match Daten   | Match IDs     |

### Abfragen Sammlung

#### Summoner by Summoner name

https://\<Region>.api.riotgames.com/lol/summoner/v4/summoners/by-name/\<Summoner name>?api_key=\<Key>

#### Beispiel

```json
{
    "profileIconId": 4088,
    "name": "Bilgewaters call",
    "puuid": "_aAV8VMmxfsYZ86wF_JuC9G9iD4s_b7Qz_Y6zl2xkl6MI2TnPfQgiaJ2QeQu2lQ9_7GR71YBHSDeWw",
    "summonerLevel": 193,
    "accountId": "0lSWla1eweU9RcQ-pTOFzB_d9T_zmbXlng4yS57K32h7CnA",
    "id": "23Sp99vm0nIdHnNfsFFHZ_ivNjBZnhu4cfy8letWSIry4W0",
    "revisionDate": 1557349646000
}
```

#### Match history by Account ID

https://\<Region>.api.riotgames.com/lol/match/v4/matchlists/by-account/\<Account ID>?endIndex=10&api_key=\<Key>

#### Beispiel

```json
{
    "matches": [
        {
            "lane": "JUNGLE",
            "gameId": 4022694315,
            "champion": 79,
            "platformId": "EUW1",
            "timestamp": 1557347163186,
            "queue": 420,
            "role": "NONE",
            "season": 13
        },
        {
            "lane": "BOTTOM",
            "gameId": 4022642652,
            "champion": 81,
            "platformId": "EUW1",
            "timestamp": 1557344240028,
            "queue": 440,
            "role": "DUO_CARRY",
            "season": 13
        },
        ...
    ],
    "endIndex": 10,
    "startIndex": 0,
    "totalGames": 150
}
```

#### Match by MatchID

https://\<Region>.api.riotgames.com/lol/match/v4/matches/4022694315?api_key=\<Key>

#### Beispiel

```json
{
    "seasonId": 13,
    "queueId": 430,
    "gameId": 4022326715,
    "participantIdentities": [
        {
            "player": {
                "currentPlatformId": "EUW1",
                "summonerName": "namesdonmatter",
                "matchHistoryUri": "/v1/stats/player_history/EUW1/239431640",
                "platformId": "EUW1",
                "currentAccountId": "ChDZS0L19pwamkvI011lxCJlwNeYcKEuroAdCQ6CzEtVIwU",
                "profileIcon": 3587,
                "summonerId": "iLeCbC5xR_9a1_aWlRH25UPggij5Je5ZeZ6Un-uwSFaPS72c",
                "accountId": "ChDZS0L19pwamkvI011lxCJlwNeYcKEuroAdCQ6CzEtVIwU"
            },
            "participantId": 1
        }
        ...
"participants": [
        {
            "spell1Id": 6,
            "participantId": 1,
            "timeline": {
                ...
            },
            "spell2Id": 12,
            "teamId": 100,
            "stats": {
                "neutralMinionsKilledTeamJungle": 0,
                "visionScore": 7,
                "magicDamageDealtToChampions": 4441,
                "largestMultiKill": 1,
                "totalTimeCrowdControlDealt": 163,
                "longestTimeSpentLiving": 395,
                "perk1Var1": 1609,
                "perk1Var3": 0,
                "perk1Var2": 0,
                "tripleKills": 0,
                "perk5": 8237,
                "perk4": 8224,
                "playerScore9": 0,
                "playerScore8": 0,
                "kills": 3,
                "playerScore1": 0,
                "playerScore0": 0,
                "playerScore3": 0,
                "playerScore2": 0,
                "playerScore5": 0,
                "playerScore4": 0,
                "playerScore7": 0,
                "playerScore6": 0,
                "perk5Var1": 278,
                "perk5Var3": 0,
                "perk5Var2": 0,
                "totalScoreRank": 0,
                "neutralMinionsKilled": 5,
                "statPerk1": 5002,
                "statPerk0": 5008,
                "damageDealtToTurrets": 5113,
                "physicalDamageDealtToChampions": 1737,
                "damageDealtToObjectives": 5113,
                "perk2Var2": 0,
                "perk2Var3": 0,
                "totalUnitsHealed": 8,
                "perk2Var1": 954,
                "perk4Var1": 847,
                "totalDamageTaken": 17974,
                "perk4Var3": 0,
                "wardsKilled": 0,
                "largestCriticalStrike": 0,
                "largestKillingSpree": 0,
                "quadraKills": 0,
                "magicDamageDealt": 21847,
                "firstBloodAssist": false,
                "item2": 3107,
                "item3": 3504,
                "item0": 3158,
                "item1": 3905,
                "item6": 3340,
                "item4": 3092,
                "item5": 1052,
                "perk1": 8446,
                "perk0": 8437,
                "perk3": 8451,
                "perk2": 8444,
                "perk3Var3": 0,
                "perk3Var2": 0,
                "perk3Var1": 221,
                "damageSelfMitigated": 10394,
                "magicalDamageTaken": 4141,
                "perk0Var2": 196,
                "firstInhibitorKill": false,
                "trueDamageTaken": 1656,
                "assists": 20,
                "perk4Var2": 0,
                "goldSpent": 10035,
                "trueDamageDealt": 4325,
                "participantId": 1,
                "physicalDamageDealt": 15393,
                "sightWardsBoughtInGame": 0,
                "totalDamageDealtToChampions": 6666,
                "physicalDamageTaken": 12176,
                "totalPlayerScore": 0,
                "win": true,
                "objectivePlayerScore": 0,
                "totalDamageDealt": 41565,
                "neutralMinionsKilledEnemyJungle": 5,
                "deaths": 9,
                "wardsPlaced": 3,
                "perkPrimaryStyle": 8400,
                "perkSubStyle": 8200,
                "turretKills": 0,
                "firstBloodKill": false,
                "trueDamageDealtToChampions": 487,
                "goldEarned": 12487,
                "killingSprees": 0,
                "unrealKills": 0,
                "firstTowerAssist": true,
                "firstTowerKill": false,
                "champLevel": 15,
                "doubleKills": 0,
                "inhibitorKills": 0,
                "firstInhibitorAssist": false,
                "perk0Var1": 257,
                "combatPlayerScore": 0,
                "perk0Var3": 0,
                "visionWardsBoughtInGame": 0,
                "pentaKills": 0,
                "totalHeal": 36979,
                "totalMinionsKilled": 41,
                "timeCCingOthers": 12,
                "statPerk2": 5001
            },
            "championId": 16
        },
```



#### Ranking by ID

https://\<Region>.api.riotgames.com/lol/league/v4/entries/by-summoner/\<ID>?api_key=\<key>
**Achtung: ** ID entspricht nicht account ID

#### Beispiel

```json
[
    {
        "queueType": "RANKED_SOLO_5x5",
        "summonerName": "Bilgewaters call",
        "hotStreak": false,
        "wins": 38,
        "veteran": false,
        "losses": 46,
        "rank": "I",
        "tier": "SILVER",
        "inactive": false,
        "freshBlood": false,
        "leagueId": "153e8bd0-2158-11e9-8726-c81f66db01ef",
        "summonerId": "23Sp99vm0nIdHnNfsFFHZ_ivNjBZnhu4cfy8letWSIry4W0",
        "leaguePoints": 58
    },
    {
        "queueType": "RANKED_FLEX_SR",
        "summonerName": "Bilgewaters call",
        "hotStreak": false,
        "wins": 25,
        "veteran": false,
        "losses": 33,
        "rank": "I",
        "tier": "SILVER",
        "inactive": false,
        "freshBlood": false,
        "leagueId": "8819bcd0-214c-11e9-9d00-c81f66dacb22",
        "summonerId": "23Sp99vm0nIdHnNfsFFHZ_ivNjBZnhu4cfy8letWSIry4W0",
        "leaguePoints": 79
    }
]
```

#### Runes by RuneID

http://opgg-static.akamaized.net/images/lol/perk/\<Rune ID>.png

### Icon

http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/\<Icon ID>.png

### Champion Name

https://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion.json

#### Beispiel

```json
{
  "type": "champion",
  "format": "standAloneComplex",
  "version": "6.24.1",
  "data": {
    "Aatrox": {
      "version": "6.24.1",
      "id": "Aatrox",
      "key": "266",
      "name": "Aatrox",
      "title": "the Darkin Blade",
      "blurb": "Aatrox is a legendary warrior, one of only five that remain of an ancient race known as the Darkin. He wields his massive blade with grace and poise, slicing through legions in a style that is hypnotic to behold. With each foe felled, Aatrox's ...",
      "info": {
        "attack": 8,
        "defense": 4,
        "magic": 3,
        "difficulty": 4
      },
      "image": {
        "full": "Aatrox.png",
        "sprite": "champion0.png",
        "group": "champion",
        "x": 0,
        "y": 0,
        "w": 48,
        "h": 48
      },
      "tags": [
        "Fighter",
        "Tank"
      ],
      "partype": "BloodWell",
      "stats": {
        "hp": 537.8,
        "hpperlevel": 85,
        "mp": 105.6,
        "mpperlevel": 45,
        "movespeed": 345,
        "armor": 24.384,
        "armorperlevel": 3.8,
        "spellblock": 32.1,
        "spellblockperlevel": 1.25,
        "attackrange": 150,
        "hpregen": 6.59,
        "hpregenperlevel": 0.5,
        "mpregen": 0,
        "mpregenperlevel": 0,
        "crit": 0,
        "critperlevel": 0,
        "attackdamage": 60.376,
        "attackdamageperlevel": 3.2,
        "attackspeedoffset": -0.04,
        "attackspeedperlevel": 3
      }
    },
```



### Summoner Spell

http://opgg-static.akamaized.net/images/lol/spell/\<Name>.png



## Glossar

| Riot Term     | Wort                                   |
| ------------- | -------------------------------------- |
| Summoner      | Spieler                                |
| Summoner name | Name des Spielers in League of Legends |
| Champion      | Spielbarer Charakter                   |
| Match history | Spielverlauf eines Spieler             |

