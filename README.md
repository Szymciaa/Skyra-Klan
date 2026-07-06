# Skyra-Klan

Professional Clan Plugin for Minecraft 1.21.4 (Paper/Spigot)

## Wymagania
- Minecraft 1.21.4 (Paper/Spigot)
- Java 21
- Vault (dla ekonomii)
- PlaceholderAPI (dla placeholderów)

## Instalacja
1. Pobierz plik `.jar` z [Releases](https://github.com/Szymciaa/Skyra-Klan/releases)
2. Umieść plik w folderze `plugins/`
3. Zainstaluj Vault i PlaceholderAPI jeśli jeszcze ich nie masz
4. Uruchom serwer

## Komendy

### Zarządzanie klanem
- `/klan zaloz <nazwa> <tag>` - Załóż nowy klan (nazwa max 5 znaków, tag max 3 znaki)
- `/klan zapos <nick>` - Zaproś gracza do klanu (tylko lider)
- `/klan opusc` - Opuść klan
- `/klan usun` - Usuń klan (tylko lider)
- `/klan info` - Wyświetl informacje o klanie
- `/klan ulepszenia` - Otwórz GUI ulepszań klanu (tylko lider)

## PlaceholderAPI

- `%skyra_klan%` - Nazwa klanu z tagiem gracza
- `%skyra_punkty%` - Liczba punktów gracza

## Ulepszenia Klanu

### Ulepszenie I: Miejsca w klanie
- **Koszt:** $5000
- **Zmiana:** 5 → 10 miejsc

### Ulepszenie II: Miejsca w klanie
- **Koszt:** $10000
- **Zmiana:** 10 → 15 miejsc

### Ulepszenie III: Miejsca w klanie
- **Koszt:** $20000
- **Zmiana:** 15 → 20 miejsc

## System Punktów

- Każdy gracz przy dołączeniu otrzymuje 1000 punktów
- Za zabicie gracza: +50-150 punktów (losowo)
- Za śmierć od gracza: -30-80 punktów (losowo)
- Śmierć od moba/upadku: brak zmian

## Autorzy
- Szymciaa

## Licencja
MIT