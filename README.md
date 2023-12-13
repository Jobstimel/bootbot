### Anleitung

#### 1. Discord vorbereiten

- Einen Discord Server erstellen
- Einen Discord Bot bzw. eine Discord Application erstellen: [Mehr Infos hier](https://github.com/discord-jda/JDA/wiki/3%29-Getting-Started)
- Den Discord Bot zum gewünschten Server hinzufügen
  * Scopes: bot
  * Bot Permissions: Send Messages
- Den Bot Token und die Channel Id für den nächsten Schritt speichern

#### 2. Die Konfigurations Dateien erstellen
- Einen beliebigen Ordner für die Konfigurationsdateien wählen / erstellen
  * Standardmäßig schaut das Programm nach C:\bootbot
- In dem Ordner eine Datei 'config.properties' mit folgendem Inhalt erstellen:
  * bot.token={Der Bot Token aus Schritt 1}
  * channel.id={Die Channel Id aus Schritt 1}
- In dem Ordner eine Datei 'boots.txt' mit folgendem Inhalt erstellen:
  * Erste Zeile: Name;Groesse;Geschlecht
  * In den anderen Zeilen können dann beliebige Schuhmodelle definiert werden, z.B:
  * Jordan Retro 11;37.5;Herren
- Beide Dateien speichern

### 3. Application starten
- Option 1: Konfigurationsdateien aus Schritt 2 sind in C:\bootbot:
  * java -jar bootbot-0.0.1-SNAPSHOT.jar
- Option 2: Konfigurationsdateien aus Schritt 2 sind in anderem Ordner:
  * java -jar bootbot-0.0.1-SNAPSHOT.jar --config.folder.path=pfad\zu\anderem\ordner
