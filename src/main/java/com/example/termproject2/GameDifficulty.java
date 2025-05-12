package com.example.termproject2;

public enum GameDifficulty 
{
	Easy(1),
	Normal(2),
	Hard(3);

	private int difficulty;

	GameDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getDifficulty() {
		return difficulty;
	}
}

