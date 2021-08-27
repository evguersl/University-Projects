package ch.epfl.tchu.game;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_4 {
    private SignatureChecks_4() {}

    void checkStationPartition() {
        v03 = v01.connected(v02, v02);
    }

    void checkBuilder() {
        v04 = new ch.epfl.tchu.game.StationPartition.Builder(v05);
        v01 = v04.build();
        v04 = v04.connect(v02, v02);
    }

    void checkPlayerId() {
        v06 = ch.epfl.tchu.game.PlayerId.ALL;
        v05 = ch.epfl.tchu.game.PlayerId.COUNT;
        v07 = ch.epfl.tchu.game.PlayerId.PLAYER_1;
        v07 = ch.epfl.tchu.game.PlayerId.PLAYER_2;
        v07 = ch.epfl.tchu.game.PlayerId.valueOf(v08);
        v09 = ch.epfl.tchu.game.PlayerId.values();
        v07 = v07.next();
    }

    void checkPublicPlayerState() {
        v10 = new ch.epfl.tchu.game.PublicPlayerState(v05, v05, v11);
        v05 = v10.carCount();
        v05 = v10.cardCount();
        v05 = v10.claimPoints();
        v11 = v10.routes();
        v05 = v10.ticketCount();
    }


    ch.epfl.tchu.game.StationPartition v01;
    ch.epfl.tchu.game.Station v02;
    boolean v03;
    ch.epfl.tchu.game.StationPartition.Builder v04;
    int v05;
    java.util.List<ch.epfl.tchu.game.PlayerId> v06;
    ch.epfl.tchu.game.PlayerId v07;
    java.lang.String v08;
    ch.epfl.tchu.game.PlayerId[] v09;
    ch.epfl.tchu.game.PublicPlayerState v10;
    java.util.List<ch.epfl.tchu.game.Route> v11;
    ch.epfl.tchu.game.PlayerState v12;
    ch.epfl.tchu.SortedBag<ch.epfl.tchu.game.Ticket> v13;
    ch.epfl.tchu.SortedBag<ch.epfl.tchu.game.Card> v14;
    ch.epfl.tchu.game.Route v15;
    java.util.List<ch.epfl.tchu.SortedBag<ch.epfl.tchu.game.Card>> v16;
    ch.epfl.tchu.game.Card v17;
}
