# Compiler
CXX = g++

# Compiler flags
CXXFLAGS = -std=c++11 -Wall

# Source files
SRCS = main.cpp streaming_service.cpp data_structures.cpp

# Object files
OBJ_DIR = out
OBJS = $(patsubst %.cpp,$(OBJ_DIR)/%.o,$(SRCS))

# Executable name
EXEC = my_program

# Targets
all: $(OBJ_DIR) $(EXEC)

$(EXEC): $(OBJS)
	$(CXX) $(CXXFLAGS) $(OBJS) -o $(EXEC)

$(OBJ_DIR)/%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(OBJ_DIR):
	mkdir -p $(OBJ_DIR)

# Clean
clean:
	rm -rf $(OBJ_DIR) $(EXEC)
