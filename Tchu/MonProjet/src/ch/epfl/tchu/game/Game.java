package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;




public final class Game
{

    /**
     * the method to play the game : see detailed comments
     *
     * @param players a map that links a PlayerId to a class implementing the Player interface
     * @param playerNames the names of the players
     * @param tickets the game tickets
     * @param rng random generator
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng)
    {
        Preconditions.checkArgument(players.size()== PlayerId.COUNT && playerNames.size()== PlayerId.COUNT);

        Map<PlayerId, Integer> timeMap = new HashMap<>();
        timeMap.put(PlayerId.PLAYER_1,0);
        timeMap.put(PlayerId.PLAYER_2,0);

        //initPLayers called for each player
        players.forEach((k,v)->v.initPlayers(k, playerNames));

        //we launch the initial() method of GameState
        GameState theGameState = GameState.initial(tickets, rng);

        //we create an Info for both players
        Info infoFirstPlayer = new Info(playerNames.get(theGameState.currentPlayerId()));
        Info infoSecondPlayer = new Info(playerNames.get(theGameState.currentPlayerId().next()));
        Info infoCurrentPlayer = infoFirstPlayer;
        Info infoPreviousPlayer;

        //we send the info to both players on who will play first
        sendInfoToPlayers(players, infoCurrentPlayer.willPlayFirst());

        //we set initial ticket choice for the current player
        players.get(theGameState.currentPlayerId())
                .setInitialTicketChoice(theGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));

        //we retrieve five tickets from the GameState
        theGameState = theGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);

        //we set initial ticket choice for the second player
        players.get(theGameState.currentPlayerId().next())
                .setInitialTicketChoice(theGameState.topTickets(Constants.INITIAL_TICKETS_COUNT));

        //we retrieve five tickets from the GameState
        theGameState = theGameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);

        //we send an update to both players on the Game State
        sendUpdate(players, theGameState);

        //the current player chooses its initial tickets
        theGameState = theGameState
                .withInitiallyChosenTickets(theGameState.currentPlayerId(),players.get(theGameState.currentPlayerId()).chooseInitialTickets());
        //the second player chooses its initial tickets
        theGameState = theGameState
                .withInitiallyChosenTickets(theGameState.currentPlayerId().next(),players.get(theGameState.currentPlayerId().next()).chooseInitialTickets());

        //we send to the players the info on the chosen tickets
        sendInfoToPlayers(players, infoCurrentPlayer.keptTickets(theGameState.playerState(theGameState.currentPlayerId()).ticketCount()));
        sendInfoToPlayers(players, infoSecondPlayer.keptTickets(theGameState.playerState(theGameState.currentPlayerId().next()).ticketCount()));

        long tempTime = System.nanoTime();
        boolean firstTimeLastTurn = false;
        int index =0;

        do
        {
            //we get the current player id
            PlayerId currentPlayerId = theGameState.currentPlayerId();

            //disp info canPlay
            sendInfoToPlayers(players, infoCurrentPlayer.canPlay());

            //we get the current player
            Player current_Player = players.get(currentPlayerId);

            //we send an update of the the current game state
            sendUpdate(players, theGameState);

            //we ask the player which action he wants to do
            Player.TurnKind action = current_Player.nextTurn();

            switch (action) {
                case DRAW_TICKETS:
                    SortedBag<Ticket> drawTicket = theGameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    sendInfoToPlayers(players, infoCurrentPlayer.drewTickets(Constants.IN_GAME_TICKETS_COUNT));
                    SortedBag<Ticket> chosenTickets = current_Player.chooseTickets(drawTicket);
                    timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                    theGameState = theGameState.withChosenAdditionalTickets(drawTicket, chosenTickets, timeMap.get(currentPlayerId));
                    sendInfoToPlayers(players, infoCurrentPlayer.keptTickets(chosenTickets.size()));
                    break;
                case DRAW_CARDS:
                    int first_index = current_Player.drawSlot();
                    theGameState = theGameState.withCardsDeckRecreatedIfNeeded(rng);
                    timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                    theGameState = Game.drawCard(theGameState, first_index, infoCurrentPlayer, players, timeMap.get(currentPlayerId));
                    tempTime = System.nanoTime();
                    sendUpdate(players, theGameState);
                    int second_index = current_Player.drawSlot();
                    theGameState = theGameState.withCardsDeckRecreatedIfNeeded(rng);
                    timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                    theGameState = Game.drawCard(theGameState, second_index, infoCurrentPlayer, players, timeMap.get(currentPlayerId));

                    break;
                case CLAIM_ROUTE:
                    Route claimRoute = current_Player.claimedRoute();
                    SortedBag<Card> cardToPlay = current_Player.initialClaimCards();
                    if (claimRoute.level() == Route.Level.UNDERGROUND) {
                        sendInfoToPlayers(players, infoCurrentPlayer.attemptsTunnelClaim(claimRoute, cardToPlay));
                        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();

                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            theGameState = theGameState.withCardsDeckRecreatedIfNeeded(rng);
                            builder.add(theGameState.topCard());
                            theGameState = theGameState.withoutTopCard();
                        }

                        SortedBag<Card> drawnCards = builder.build();
                        theGameState = theGameState.withMoreDiscardedCards(SortedBag.of(drawnCards));

                        int numberOfAdditionalCards = claimRoute.additionalClaimCardsCount(cardToPlay, drawnCards);

                        sendInfoToPlayers(players, infoCurrentPlayer.drewAdditionalCards(drawnCards, numberOfAdditionalCards));

                        List<SortedBag<Card>> possibleAdditionalCards;

                        try {
                            possibleAdditionalCards = theGameState.currentPlayerState().possibleAdditionalCards(numberOfAdditionalCards, cardToPlay, drawnCards);
                        } catch (IllegalArgumentException e) {
                            possibleAdditionalCards = new ArrayList<>();
                        }


                        if (numberOfAdditionalCards >= 1 && possibleAdditionalCards.size() >= 1) {
                            SortedBag<Card> choosenCards = current_Player.chooseAdditionalCards(possibleAdditionalCards);

                            if (choosenCards.size() > 0) {
                                timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                                theGameState = theGameState.withClaimedRoute(claimRoute, choosenCards.union(cardToPlay), timeMap.get(currentPlayerId));
                                sendInfoToPlayers(players, infoCurrentPlayer.claimedRoute(claimRoute, choosenCards.union(cardToPlay)));
                            } else {
                                timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                                theGameState = theGameState.addTime(timeMap.get(currentPlayerId));
                                sendInfoToPlayers(players, infoCurrentPlayer.didNotClaimRoute(claimRoute));
                            }
                        } else if (numberOfAdditionalCards == 0) {
                            timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                            theGameState = theGameState.withClaimedRoute(claimRoute, cardToPlay, timeMap.get(currentPlayerId));
                            sendInfoToPlayers(players, infoCurrentPlayer.claimedRoute(claimRoute, cardToPlay));
                        } else {
                            timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                            theGameState = theGameState.addTime(timeMap.get(currentPlayerId));
                            sendInfoToPlayers(players, infoCurrentPlayer.didNotClaimRoute(claimRoute));
                        }
                    } else {
                        timeMap = Game.getTimePlayer(timeMap,currentPlayerId,tempTime);
                        theGameState = theGameState.withClaimedRoute(claimRoute, cardToPlay, timeMap.get(currentPlayerId));
                        sendInfoToPlayers(players, infoCurrentPlayer.claimedRoute(claimRoute, cardToPlay));
                    }
                    break;
            }


            //check if last turn begins, if so we start the counter for two turns
            index = (firstTimeLastTurn) ? index + 1 : index;
            if(theGameState.lastTurnBegins() && index==0)
            {
                sendInfoToPlayers(players, infoCurrentPlayer.lastTurnBegins(theGameState.currentPlayerState().carCount()));
                firstTimeLastTurn =true;
            }


            //we get the game state of the next turn
            theGameState = theGameState.forNextTurn();
            tempTime = System.nanoTime();

            //System.out.println("timePlayer1 : "+timePlayer1);
            //System.out.println("timePlayer2 : "+timePlayer2);

            //change the info status
            infoCurrentPlayer = (infoCurrentPlayer == infoFirstPlayer) ? infoSecondPlayer : infoFirstPlayer;

        }while(index!=2);

        infoCurrentPlayer = new Info(playerNames.get(theGameState.currentPlayerId()));
        infoPreviousPlayer = new Info(playerNames.get(theGameState.currentPlayerId().next()));

        //finding each player longest trail
        Trail longestTrailPlayer1 = Trail.longest(theGameState.currentPlayerState().routes());
        Trail longestTrailPlayer2 = Trail.longest(theGameState.playerState(theGameState.currentPlayerId().next()).routes());

        int sizeTrail1 = longestTrailPlayer1.length();
        int sizeTrail2 = longestTrailPlayer2.length();

        int finalPoints1 = theGameState.currentPlayerState().finalPoints();
        int finalPoints2 = theGameState.playerState(theGameState.currentPlayerId().next()).finalPoints();

        //finding time winner
        int time1 = theGameState.currentPlayerState().getTime();
        int time2 = theGameState.playerState(theGameState.currentPlayerId().next()).getTime();

        if(time1>time2)
        {
            sendInfoToPlayers(players, infoPreviousPlayer.getTimeBonus(time2));
            finalPoints2 += Constants.TIME_BONUS_POINTS;
        }
        else if(time1<time2)
        {
            sendInfoToPlayers(players, infoCurrentPlayer.getTimeBonus(time1));
            finalPoints1 += Constants.TIME_BONUS_POINTS;
        }
        else
        {
            sendInfoToPlayers(players, infoCurrentPlayer.getTimeBonus(time1));
            sendInfoToPlayers(players, infoPreviousPlayer.getTimeBonus(time2));
            finalPoints1 += Constants.TIME_BONUS_POINTS;
            finalPoints2 += Constants.TIME_BONUS_POINTS;
        }

        //longest trail announcement
        if(sizeTrail1>sizeTrail2)
        {
            sendInfoToPlayers(players, infoCurrentPlayer.getsLongestTrailBonus(longestTrailPlayer1));
            finalPoints1 += Constants.LONGEST_TRAIL_BONUS_POINTS;

        }
        else if(sizeTrail1<sizeTrail2)
        {
            sendInfoToPlayers(players, infoPreviousPlayer.getsLongestTrailBonus(longestTrailPlayer2));
            finalPoints2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
        }
        else
        {
            sendInfoToPlayers(players, infoCurrentPlayer.getsLongestTrailBonus(longestTrailPlayer1));
            sendInfoToPlayers(players, infoPreviousPlayer.getsLongestTrailBonus(longestTrailPlayer2));
            finalPoints1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
            finalPoints2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
        }

        sendUpdate(players, theGameState);

        //winning announcement


        if(finalPoints1>finalPoints2)
        {
            sendInfoToPlayers(players, infoCurrentPlayer.won(finalPoints1, finalPoints2));

        }
        else if(finalPoints1<finalPoints2)
        {
            sendInfoToPlayers(players, infoPreviousPlayer.won(finalPoints2, finalPoints1));
        }
        else
        {
            List<String> playerNamesList = List.of(playerNames.get(PlayerId.PLAYER_2),playerNames.get(PlayerId.PLAYER_1));
            sendInfoToPlayers(players, Info.draw(playerNamesList,finalPoints1));
        }

    }

    private static Map<PlayerId, Integer> getTimePlayer(Map<PlayerId, Integer> myMap, PlayerId currentPlayerId, long tempTime)
    {
        long oneBillion = 1000000000;

        Map<PlayerId, Integer> myNewMap= new HashMap<>(myMap);

        myNewMap.replace(currentPlayerId, myNewMap.get(currentPlayerId)+(int)((System.nanoTime()-tempTime)/oneBillion));

        return myNewMap;
    }

    private static void sendInfoToPlayers(Map<PlayerId, Player> players, String info)
    {
        players.forEach((k,v)->v.receiveInfo(info));
    }

    private static void sendUpdate(Map<PlayerId, Player> players, PublicGameState newState)
    {
        GameState tempGameState = (GameState) newState;
        Card cardToUpdate = tempGameState.cardState().deckSize()==0 ? null : tempGameState.topCard();
        players.forEach((k,v)->v.updateState(newState, tempGameState.playerState(k),cardToUpdate));

    }

    private static GameState drawCard(GameState gameState, int index, Info info, Map<PlayerId, Player> players, int newTime)
    {
        if (index == Constants.DECK_SLOT)
        {
            gameState = gameState.withBlindlyDrawnCard(newTime);
            sendInfoToPlayers(players, info.drewBlindCard());
        }
        else
        {
            Card chosenCard = gameState.cardState().faceUpCard(index);
            gameState = gameState.withDrawnFaceUpCard(index, newTime);
            sendInfoToPlayers(players, info.drewVisibleCard(chosenCard));
        }
        return gameState;
    }

}
