#include "round.h"

GameRound::GameRound(int input_round_num, string input_left_gangster, string input_left_vote)
{
    round_num = input_round_num;
    left_gangster = input_left_gangster;
    left_vote = input_left_vote;
}

GameRound::GameRound()
{
    round_num = 0;
    left_gangster = "none";
    left_vote = "none";

}

// setters
void GameRound::setRoundNum(int num) 
{
    this->round_num = num;
}
void GameRound::setLeftGangster(int input_left_gangster) 
{

    this->left_gangster = input_left_gangster;
}
void GameRound::setLeftVote(int input_left_vote) 
{

    this->left_vote = input_left_vote;
}


void GameRound::newRound()
{
    setRoundNum(round_num + 1);
}
