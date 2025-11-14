package gamestates.states;

import gamestates.Context;
import gamestates.GameState;

public class ReadyToPlay  implements GameState {

  private final Context context;

  public ReadyToPlay(Context context) {
    this.context = context;
  }

  @Override
  public void turn() {
    context.setGameState(new InPlay(context));
  }
}
