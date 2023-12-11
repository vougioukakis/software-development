project(townofsalem)

cmake_minimum_required(VERSION 3.1.4)

set(CXX_STANDARD 17)

add_executable(TownOfSalem TownOfSalem.cpp player.cpp round.cpp)

file(COPY players.txt DESTINATION ${CMAKE_BINARY_DIR})