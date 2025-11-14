package gamestates;

import gamestates.states.GameOver;
import gamestates.states.ReadyToPlay;

class Game implements Context {

  private GameState gameState;

  Game() {
    gameState = new ReadyToPlay(this);
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public void play() {
    while (gameState.getClass() != GameOver.class) {
      gameState.turn();
    }
    gameState.turn();
  }


}
