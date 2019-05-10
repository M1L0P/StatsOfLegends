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

TL;DR

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

## Glossar

| Riot Term     | Wort                                   |
| ------------- | -------------------------------------- |
| Summoner      | Spieler                                |
| Summoner name | Name des Spielers in League of Legends |
| Champion      | Spielbarer Charakter                   |
| Match history | Spielverlauf eines Spieler             |

