
#include "player.h"

// constructors
player::player(int inputID, string inputName, bool inputAlive, string inputRole) 
{
    ID = inputID;
    Name = inputName;
    Alive = inputAlive;
    Role = inputRole;
}

player::player(){}

// members

void player::setID(int inputID) { this->ID = inputID; }
void player::setName(string inputName) { this->Name = inputName; }
void player::setAlive(bool inputAlive) { this->Alive = inputAlive; }
void player::setRole(string inputRole) { this->Role = inputRole; }
void player::setCurrentVotes(int inputCurrentVotes) { this->CurrentVotes = inputCurrentVotes; }

int player::vote()
{
    string voteStr = "";
    int voteIndex = -1;
    cout << Name << ", place your vote" << endl;

    while (true)
    {

        //showPlayersForVote();

        getline(cin, voteStr);

        try
        {
            if (voteStr.size() - 1 != 0)
            {
                throw 1;
            }
            else
            {
                voteIndex = stoi(voteStr);
                if (voteIndex > 6 || voteIndex < 0)
                {
                    throw 2;
                }


            }
        }
        catch (...)
        {
            cout << "invalid input\nplease choose one of the options "
                    << endl;
            cin.clear();
            continue;
        }
        break;
    }
    return voteIndex;
}

void player::gotVoted()
{
    setCurrentVotes(getCurrentVotes() + 1);
}

