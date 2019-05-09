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

- Benutzte Runen

- Spieltyp (Blind, Draft, Solo/Duo, Flex)

### #2: Summoner summary abfragen

Der Benutzer sollte die Zusammenfassung eines Summoners abfragen koennen. Die Summary fasst das Spielverhalten der letzten 20 oder 10 Spielen eines Summoners zusammen. Die Zusammenassung enthaelt folgende Details.

- Icon des Summoners
- Name und Level des Summoners
- Solo/Duo und FlexRang des Summoners
- Gewinnrate des Summoners
- 2 am meissten gespielte Champions
  - Bild des Champions
  - Spielrate des Summoners auf dem Champion
  - Gewinnrate des Summoners auf dem Champion

## Glossar

| Riot Term     | Wort                                   |
| ------------- | -------------------------------------- |
| Summoner      | Spieler                                |
| Summoner name | Name des Spielers in League of Legends |
| Champion      | Spielbarer Charakter                   |
| Match history | Spielverlauf eines Spieler             |

