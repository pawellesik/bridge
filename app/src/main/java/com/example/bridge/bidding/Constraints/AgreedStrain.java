package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;
import com.example.bridge.bidding.Tools.Strain;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Constraint sprawdzający czy aktualna linia licytacji dotyczy uzgodnionego koloru (atutu).
 */
public class AgreedStrain extends StaticConstraint {
    private final Strain[] strains; // Lista akceptowalnych mian (np. Kier, Pik)

    /**
     * @param strains Miana, które uznajemy za uzgodnione.
     */
    public AgreedStrain(Strain... strains) {
        this.strains = strains;
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        Strain[] sList = strains;
        if (sList.length == 0) {
            Strain s = getStrain(null, call);
            if (s != null) {
                sList = new Strain[]{s};
            } else {
                return false;
            }
        }
        
        Suit lastShown = ps.getPairState().getLastShownSuit();
        if (lastShown == null) return false;
        
        Strain agreedStrain = lastShown.toStrain();
        for (Strain s : sList) {
            if (s == agreedStrain) return true;
        }
        return false;
    }
}


/**
 * Klasa AgreedStrain to "strażnik licytacji", którego zadaniem jest sprawdzenie, czy aktualnie licytowana odzywka dotyczy koloru, który para już wcześniej uznała za swój (pokazała w licytacji).
Oto szczegółowy opis jej działania:
1. Dwa tryby pracy (Konstruktor)
Klasa może działać na dwa sposoby, w zależności od tego, jak ją wywołasz:
•
Tryb konkretny: new AgreedStrain(Strain.Hearts) – Sprawdza, czy ostatnim pokazanym kolorem pary są Kiery. Nie interesuje go, co Ty teraz licytujesz, patrzy tylko w przeszłość pary.
•
Tryb dynamiczny: new AgreedStrain() – Sprawdza, czy odzywka, którą właśnie rozważasz, jest w tym samym kolorze, co ostatnio pokazany kolor pary.
2. Algorytm logiczny (Metoda conforms)
Kiedy silnik sprawdza ten warunek, wykonuje następujące kroki:
1.
Ustalenie listy mian (sList):
◦
Jeśli podałeś miana w konstruktorze (np. Piki i Kiery), używa ich.
◦
Jeśli konstruktor był pusty, patrzy na Call (odzywkę), którą silnik właśnie analizuje. Jeśli to jest Bid (np. 4S), wyciąga z niego miano (Spades) i tworzy z niego listę do sprawdzenia.
2.
Pobranie "Pamięci Pary":
◦
Sięga do ps.getPairState().getLastShownSuit(). To jest kluczowy moment – system sprawdza, jaki był ostatni naturalny kolor zalicytowany przez Twoją linię (NS lub EW), pomijając wtrącenia przeciwników.
3.
Porównanie:
◦
Jeśli nikt z Was jeszcze nic nie zalicytował -> false.
◦
Jeśli ostatni kolor pary zgadza się z mianem z punktu 1 -> true.
3. Przykład praktyczny
Załóżmy licytację: 1♥ – Pass – 2♥ – Pass – ?
Przypadek A: Użycie new AgreedStrain()
W Twoich regułach masz zapis: bids.add(shows(Bid._4H, new AgreedStrain(), points(18, 20)));
•
Co się dzieje? Silnik analizuje odzywkę 4H.
•
Działanie: AgreedStrain widzi, że rozważasz 4H (miano Kier). Sprawdza LastShownSuit pary. Partner otworzył 1H, Ty dałeś 2H, więc ostatni kolor to Kier.
•
Wynik: Kier == Kier -> TRUE. Reguła może zostać użyta.
Przypadek B: Użycie konkretnego miana (Licytacja szlemowa)
Masz regułę, która pozwala licytować Blackwooda tylko, jeśli uzgodniliście Piki: bids.add(shows(Bid._4NT, new AgreedStrain(Strain.Spades), points(25, 30)));
•
Licytacja 1: 1S - 2S - ? -> Ostatni kolor to Piki. AgreedStrain(Spades) zwraca TRUE. Możesz pytać o asy.
•
Licytacja 2: 1H - 2H - ? -> Ostatni kolor to Kiery. AgreedStrain(Spades) zwraca FALSE. Reguła Blackwooda nie "wskoczy", bo szukamy Pików, a para licytuje Kiery.
4. Kluczowa zaleta
AgreedStrain pozwala tworzyć bardzo uniwersalne reguły. Zamiast pisać oddzielną regułę dla 4H po Hearts i 4S po Spades, możesz napisać jedną: bids.add(shows(nextLevelBid(), new AgreedStrain(), ...)) co oznacza: "Podbij nasz uzgodniony kolor o jeden poziom wyżej".
W skrócie: Jest to mechanizm, który pilnuje, aby robot "trzymał się tematu" i nie zmieniał koloru bez powodu, gdy para już znalazła swój wspólny mianownik.
**/

























































































































































































































































































































































































































































































