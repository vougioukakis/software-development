#ifndef STRUCTURES
#define STRUCTURES

typedef enum
{
    HORROR,
    SCIFI,
    DRAMA,
    ROMANCE,
    DOCUMENTARY,
    COMEDY
} movieCategory_t;

struct movie_info
{
    unsigned mid;
    unsigned year;

    movie_info();
    movie_info(unsigned mid, unsigned year);
};

struct movie
{
    movie_info info;
    movie *next;

    movie(movie_info info, movie *next);
};

struct new_movie
{
    movie_info info;
    movieCategory_t category;
    new_movie *next;

    new_movie(movie_info info, movieCategory_t cat, new_movie *next);
};

struct suggested_movie {
    movie_info info;
    suggested_movie *prev;
    suggested_movie *next;

    suggested_movie(movie_info info, suggested_movie *next, suggested_movie *prev);
};

class newRelList{
    public:
    new_movie *head;

    newRelList();
    void printList();
    void pop();
};

class category{
    public:
    movie *head;

    category();
    void printList();
    void add(new_movie *mov);
};

//doubly linked list
class suggestionsList{
public:
    suggested_movie *head;
    suggested_movie *tail;

    suggestionsList();
    void printList();
    void remove(unsigned mid);

};

struct user
{
    int uid;
    user *next;
    movie *watchHistory;
    suggestionsList *suggestions;

    user(int uid, user *next);
};

class usrList
{
public:
    user *sentinel;

    usrList();
    ~usrList();

    bool isRegistered(int uid);
    void printList();

    /*looks for user with uid,
    returns pointer to user struct.
    if not found returns nullptr*/
    user *find(int uid);
};

class filteredList
{
public:
    suggested_movie *head;
    suggested_movie *tail;

    filteredList();
    void printList();
    void add(movie *mov);
};

#endif