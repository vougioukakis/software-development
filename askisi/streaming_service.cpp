#include "streaming_service.h"
#include <iostream>
#include <string>
using namespace std;

usrList *Users = new usrList();
newRelList *newReleases = new newRelList();

category *horror = new category();
category *scifi = new category();
category *drama = new category();
category *romance = new category();
category *documentary = new category();
category *comedy = new category();

const char *categoryName[6] = {"horror", "scifi", "drama", "romance", "documentary", "comedy"};
category *whichCategory[6] = {horror, scifi, drama, romance, documentary, comedy};
movie *categoryTable[6] = {horror->head,
                           scifi->head,
                           drama->head,
                           romance->head,
                           documentary->head,
                           comedy->head};

// functions of streaming service header
/***************************************/
// EVENT M
void print_movies(void)
{
    for (int i = 0; i < 6; i++)
    {
        cout << categoryName[i] << ": ";
        whichCategory[i]->printList();
        cout << '\n';
    }
}

// EVENT R
int register_user(int uid)
{
    if (Users->isRegistered(uid))
        return -1;
    try
    {
        user *node = new user(uid, Users->sentinel->next);
        suggestionsList *list = new suggestionsList();
        node->suggestions = list;
        Users->sentinel->next = node;
    }
    catch (const exception &e)
    {
        cout << "exception caught" << endl;
        return -1;
    }

    Users->printList();
    return 0;
}

// EVENT U
int unregister_user(int uid)
{
    user *ptr = Users->sentinel->next;
    user *prev = Users->sentinel;
    while (ptr != nullptr && ptr->uid != uid)
    {
        prev = ptr;
        ptr = ptr->next;
    }

    if (ptr == nullptr)
    {
        cout << "not found" << endl;
        return -1;
    }

    prev->next = ptr->next;

    Users->printList();
    return 0;
}

// EVENT A
int add_new_movie(unsigned mid, movieCategory_t category, unsigned year)
{
    try
    {
        movie_info *info = new movie_info(mid, year);
        new_movie *ptr = newReleases->head;
        new_movie *prev = nullptr;

        while (ptr != nullptr && ptr->info.mid < mid)
        {
            prev = ptr;
            ptr = ptr->next;
        }

        new_movie *new_mov = new new_movie(*info, category, ptr);
        if (prev != nullptr)
            prev->next = new_mov;
        else if (prev == nullptr)
            newReleases->head = new_mov;
    }
    catch (const exception &e)
    {
        cout << "exception caught " << endl;
        return -1;
    }

    newReleases->printList();

    return 0;
}

// EVENT D
void distribute_new_movies(void)
{
    while (newReleases->head != nullptr)
    {
        new_movie *addition = newReleases->head;
        whichCategory[addition->category]->add(addition);
        categoryTable[addition->category] = whichCategory[addition->category]->head; // update the cat table
        newReleases->pop();
    }

    print_movies();
}



// EVENT W
movie *findMovie(unsigned mid)
{
    movie *ptr; // to start the loop
    for (int i = 0; i < 6; i++)
    {
        // ptr = whichCategory[i]->head;     alternative using classes
        ptr = categoryTable[i];
        while (ptr != nullptr && ptr->info.mid != mid)
        {
            ptr = ptr->next;
        }

        if (ptr != nullptr && ptr->info.mid == mid)
        {
            return ptr;
        }
    }

    return nullptr;
}

void showHistory(user *U)
{
    cout << "User " << U->uid << " watch history = ";
    movie *ptr = U->watchHistory;
    while (ptr != nullptr)
    {
        cout << "< " << ptr->info.mid << " >, ";
        ptr = ptr->next;
    }
    cout << '\n';
}

int watch_movie(int uid, unsigned mid)
{
    movie *moviePtr = findMovie(mid);
    user *usrPtr = Users->find(uid);
    if (moviePtr == nullptr || usrPtr == nullptr)
    {
        cout << "watch movie failed" << endl;
        return -1;
    }
    movie *addition = new movie(moviePtr->info, usrPtr->watchHistory);
    usrPtr->watchHistory = addition;
    showHistory(usrPtr);
    return 0;
}

// EVENT S
suggested_movie *insertToFront(user *usr, movie *addition, suggested_movie *currFront)
{
    suggested_movie *newMovie = new suggested_movie(addition->info, nullptr, nullptr);
    if (currFront != nullptr)
    {
        currFront->next = newMovie;
    }
    else if (currFront == nullptr)
    {
        usr->suggestions->head = newMovie;
        usr->suggestions->tail = newMovie;
    }
    newMovie->prev = currFront;
    newMovie->next = nullptr;
    return newMovie;
}

suggested_movie *insertToBack(user *usr, movie *addition, suggested_movie *currBack)
{
    suggested_movie *newMovie = new suggested_movie(addition->info, nullptr, nullptr);
    if (currBack != nullptr)
    {
        currBack->prev = newMovie;
    }
    else if (currBack == nullptr)
    {
        usr->suggestions->tail = newMovie;
    }
    newMovie->next = currBack;
    newMovie->prev = nullptr;
    return newMovie;
}

movie *popHistory(user *user)
{
    if (user->watchHistory != nullptr)
    {
        movie *result = new movie(user->watchHistory->info, nullptr);
        movie *nextInStack = user->watchHistory->next;
        user->watchHistory = nextInStack;
        return result;
    }
    else
    {
        return nullptr;
    }
}

int suggest_movies(int uid)
{
    user *usr = Users->find(uid);
    if (usr == nullptr)
    {
        return -1;
    }

    int counter = 1;

    suggested_movie *currFront = usr->suggestions->head;
    suggested_movie *currBack = usr->suggestions->tail;

    user *ptr = Users->sentinel->next;
    while (ptr != nullptr)
    {
        if (ptr->uid != uid)
        {
            movie *addition = popHistory(ptr);

            if (addition == nullptr)
            {
                ptr = ptr->next;
                continue;
            }
            else
            {
                if (counter % 2 != 0)
                {
                    suggested_movie *adv = insertToFront(usr, addition, currFront);
                    currFront = adv;
                }
                else
                {
                    suggested_movie *adv = insertToBack(usr, addition, currBack);
                    currBack = adv;
                }
            }

            counter++;
        }
        ptr = ptr->next;
    }

    // link
    if (currFront != nullptr)
    {
        currFront->next = currBack;
    }

    if (currBack != nullptr)
    {
        currBack->prev = currFront;
    }

    cout << "user " << uid;
    usr->suggestions->printList();
    return 0;
}

// EVENT P
void print_users(void)
{
    user *usr = Users->sentinel->next;
    if (usr == nullptr)
    {
        return;
    }

    while (usr != nullptr)
    {
        cout << "\n ===== user " << usr->uid << endl;
        usr->suggestions->printList();
        showHistory(usr);
        usr = usr->next;
    }

}

// EVENT F

movie *advance(movie *ptr, unsigned year)
{
    while (ptr->info.year < year)
    {
        cout << "movie " << ptr->info.mid << endl;
        ptr = ptr->next;
        if (ptr == nullptr)
            break;
    }

    return ptr;
}

int filtered_movie_search(int uid, movieCategory_t category1,
                          movieCategory_t category2, unsigned year)
{

    movie *ptr1 = categoryTable[category1];
    movie *ptr2 = categoryTable[category2];
    filteredList *list = new filteredList();

    int count = 0;

    while (ptr1 != nullptr && ptr2 != nullptr)
    {

        // proxwraw ta ptrs twn 2 listwn mexri na vrw stoixeia pou mou kanoun
        while (ptr1 != nullptr && ptr1->info.year < year)
        {
            ptr1 = ptr1->next;
        }
        while (ptr2 != nullptr && ptr2->info.year < year)
        {
            ptr2 = ptr2->next;
        }

        if (ptr1 == nullptr || ptr2 == nullptr)
            break;

        // vlepw poio na valw
        if (ptr1->info.mid < ptr2->info.mid)
        {
            list->add(ptr1);
            ptr1 = ptr1->next;
            continue;
        }
        else
        {
            list->add(ptr2);
            ptr2 = ptr2->next;
            continue;
        }

        count++;
    }

    // afou teleiwsei i mia lista, vazw osa mou kanoun apo tin alli pou emeine
    if (ptr1 == nullptr)
    {
        while (ptr2 != nullptr)
        {
            while (ptr2 != nullptr && ptr2->info.year < year)
            {
                ptr2 = ptr2->next;
            }

            if (ptr2 == nullptr) break;
            list->add(ptr2);
            ptr2 = ptr2->next;
        }
    }

    if (ptr2 == nullptr)
    {
        while (ptr1 != nullptr)
        {
            while (ptr1 != nullptr && ptr1->info.year < year)
            {
                ptr1 = ptr1->next;
            }

            if (ptr1 == nullptr) break;
            list->add(ptr1);
            ptr1 = ptr1->next;
        }
    }
    list->printList();

    // merge tis 2 listes
    user *usr = Users->find(uid);
    usr->suggestions->printList();
    suggested_movie *sugTail = usr->suggestions->tail;

    if (sugTail != nullptr)
    {
        sugTail->next = list->head;
    }

    if (sugTail == nullptr)
    {
        usr->suggestions->head = list->head;
    }

    if (list->head != nullptr)
    {
        list->head->prev = sugTail;
    }


    if (list->tail != nullptr)
    {
        sugTail = list->tail;
    }

    usr->suggestions->printList();

    return 0;
}


// EVENT T

void take_off_movie(unsigned mid)
{
    //for every user in list
        //find movie in suggestions and remove it
    user *usr = Users->sentinel->next;
    while (usr != nullptr)
    {
        usr->suggestions->remove(mid);
        usr = usr->next;
    }
    // for every category in cat table

    for (int i = 0; i < 6; i++)
    {
        movie *ptr = categoryTable[i]; //head of first category

        if (ptr == nullptr) continue; //if category is empty go to next

        movie *before = nullptr;
        movie *after = nullptr;

        while (ptr != nullptr && ptr->info.mid != mid)
        {
            before = ptr;
            ptr = ptr->next;
        }

        if (ptr != nullptr && ptr->info.mid == mid)
        {
            after = ptr->next;

            //removal
            if (before != nullptr) before->next = after;
        }
    }

}