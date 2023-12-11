#include "data_structures.h"
#include <iostream>
using namespace std;

/******* USERS ********/

user::user(int uid, user *next)
{
    this->uid = uid;
    this->next = next;
}

usrList::usrList()
{
    this->sentinel = new user(-1, nullptr);
}
usrList::~usrList() {}

bool usrList::isRegistered(int uid)
{
    user *ptr = this->sentinel->next;
    while (ptr != nullptr && ptr->uid != uid)
        ptr = ptr->next;
    if (ptr == nullptr)
        return false;
    return true;
}

void usrList::printList()
{
    user *nodePtr = this->sentinel->next;
    cout << "Users = " << endl;
    while (nodePtr != nullptr)
    {
        cout << nodePtr->uid << ", ";
        nodePtr = nodePtr->next;
    }

    cout << '\n';
}

user *usrList::find(int uid)
{
    user *ptr = this->sentinel->next;
    while (ptr != nullptr && ptr->uid != uid)
    {
        ptr = ptr->next;
    }
    return ptr;
}

/******** MOVIES *********/

movie_info::movie_info() {}
movie_info::movie_info(unsigned mid, unsigned year)
{
    this->mid = mid;
    this->year = year;
}

new_movie::new_movie(movie_info info, movieCategory_t cat, new_movie *next)
{
    this->info = info;
    this->category = cat;
    this->next = next;
}

movie::movie(movie_info info, movie *next)
{
    this->info = info;
    this->next = next;
}

suggested_movie::suggested_movie(movie_info info, suggested_movie *prev, suggested_movie *next)
{
    this->info = info;
    this->next = next;
    this->prev = prev;
}
/******* MOVIE LISTS ********/
newRelList::newRelList()
{
    this->head = nullptr;
}

void newRelList::printList()
{
    new_movie *ptr = this->head;
    cout << "New movies: " << endl;
    while (ptr != nullptr)
    {
        cout << "<" << ptr->info.mid << " ," << ptr->category << " ," << ptr->info.year << " >" << endl;
        ptr = ptr->next;
    }
}

void newRelList::pop()
{
    if (this->head != nullptr)
    {
        new_movie *ptr = this->head;
        this->head = this->head->next;
        ptr->next = nullptr;
    }
}

/******* CATEGORY LISTS ******/
category::category()
{
    this->head = nullptr;
}

void category::printList()
{
    movie *ptr = this->head;
    while (ptr != nullptr)
    {
        cout << "<id: " << ptr->info.mid << ", year: " << ptr->info.year << ">, ";
        ptr = ptr->next;
    }
    cout << "\n";
}

void category::add(new_movie *mov)
{
    movie_info *info = new movie_info(mov->info.mid, mov->info.year);
    movie *ptr = this->head;
    movie *prev = nullptr;

    while (ptr != nullptr && ptr->info.mid < mov->info.mid)
    {
        prev = ptr;
        ptr = ptr->next;
    }

    movie *addition = new movie(*info, ptr);
    if (prev != nullptr)
        prev->next = addition;
    else if (prev == nullptr)
        this->head = addition;
}

/****** SUGGESTIONS LIST *******/

suggestionsList::suggestionsList()
{
    this->head = nullptr;
    this->tail = nullptr;
}

void suggestionsList::printList()
{
    cout << "suggested movies: ";
    suggested_movie *printed = this->head;
    if (printed == nullptr){
        cout << "empty suggestions list" << endl;
    }

    while (printed != nullptr)
    {
        cout << "<" << printed->info.mid << ">, ";
        printed = printed->next;
    }

    cout << '\n';
}

void suggestionsList::remove(unsigned mid)
{
    suggested_movie *movie = this->head;
    suggested_movie *before = nullptr;
    suggested_movie *after = nullptr;

    if (movie == nullptr) return;

    while(movie != nullptr && movie->info.mid != mid)
    {
        before = movie;
        movie = movie->next;
    }

    if (movie == nullptr) return;
    //once found
    after = movie->next;

    if (after != nullptr) after->prev = before;

    if (before != nullptr) before->next = after;

}
/******* FILTERED LIST ********/

filteredList::filteredList()
{
    this->head = nullptr;
    this->tail = nullptr;
}

void filteredList::printList()
{
    cout << "filtered movies: ";
    suggested_movie *printed = this->head;
    if (printed == nullptr){
        cout << "empty filtered list" << endl;
    }


    while (printed != nullptr)
    {
        cout << "<" << printed->info.mid << ">, ";
        printed = printed->next;
    }

    cout << '\n';
}


void filteredList::add(movie *mov) //Θ(1)
{
    suggested_movie *addition = new suggested_movie(mov->info, nullptr, nullptr);

    if (this->head == nullptr)
    {
        this->head = addition;
    }

    if (this->tail == nullptr)
    {
        this->tail = addition;
    }
    else if(this->tail != nullptr)
    {
        suggested_movie *temp = this->tail;
        this->tail->next = addition;
        this->tail = addition;
        temp->next = addition;

    }

    cout << "added successfully" << endl;

    
}