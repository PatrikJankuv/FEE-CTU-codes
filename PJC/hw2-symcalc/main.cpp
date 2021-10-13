#include <iostream>

#include "cmdline.hpp" // parse_command
#include "app.hpp" // handle_expr_line

#include <vector>
#include <iostream>

int main(int argc, char *argv[])
{
    std::vector<Commands::Command> commands;

    for(int i = 1; i < argc; i++){
        commands.push_back(parse_command(argv[i]));
    }

    std::string source;

    while(getline(std::cin, source)){
        try{
            handle_expr_line(std::cout, source, commands);
        }
        catch (...) {
            std::cout << "handle exception caught" << std::endl;
        }
    }

    return 0;
}
