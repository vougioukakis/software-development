#pragma once
#include "std_lib_facilities.h"

class player
{
private:
    int ID;
    string Name;
    bool Alive;
    string Role;
    int CurrentVotes;

public:

    player(int inputID, string inputName, bool inputAlive, string inputRole);

    player();
    
    // setters
    void setID(int inputID); 
    void setName(string inputName); 
    void setAlive(bool inputAlive); 
    void setRole(string inputRole);
    void setCurrentVotes(int inputCurrentVotes);

    // getters
    int getID() const { return ID; }
    string getName() const { return Name; }
    bool getAlive() const { return Alive; }
    string getRole() const { return Role; }
    int getCurrentVotes() const { return CurrentVotes; }

    //methods
    int vote();
    void gotVoted();

};
