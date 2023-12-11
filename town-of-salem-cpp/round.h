#pragma once
#include "std_lib_facilities.h"
class GameRound
{
private:
    int round_num;
    string left_gangster;
    string left_vote;

public:

    GameRound(int input_round_num, string input_left_gangster, string input_left_vote);
    GameRound();

    // getters
    int getRoundNum() const
    {
        return round_num;
    }    
    string getLeftGangster() const 
    {
        return left_gangster;
    }
    string getLeftVote() const 
    {
    
        return left_vote;
    }
    
    // setters
    void setRoundNum(int num);
    void setLeftGangster(int input_left_gangster);
    void setLeftVote(int input_left_vote);

    void newRound();
};
