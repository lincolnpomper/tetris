package com.lincolnpomper.tetris;

import com.lincolnpomper.tetris.core.GameMode;
import com.lincolnpomper.tetris.core.KeyConfig;

public interface GameStarterListener {

	void startGame(int numberOfPlayers, GameMode gameMode, KeyConfig[] playersKeyConfigs);
}
