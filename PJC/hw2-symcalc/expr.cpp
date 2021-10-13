#include "expr.hpp"
#include "expr_impl.hpp"
#include "tokenizer.hpp"

#include <stdexcept>
#include <iostream>
#include <sstream>
#include <stack>


const expr expr::ZERO = expr::number(0.0);
const expr expr::ONE = expr::number(1.0);

// TODO: overloaded operators +, -, *, /, functions pow, log, sin, cos,
//       expr::number, expr::variable, operator==, operator<<,
//       create_expression_tree
expr operator+(expr a, expr b) {
    return std::make_shared<exprs::plus>(a, b);
}

expr operator-(expr a, expr b) {
    return std::make_shared<exprs::minus>(a, b);
}

expr operator*(expr a, expr b) {
    return std::make_shared<exprs::multiply>(a, b);
}

expr operator/(expr a, expr b) {
    return std::make_shared<exprs::divide>(a, b);
}

expr pow(expr bottom, expr top) {
    return std::make_shared<exprs::power>(bottom, top);
}

expr sin(expr value) {
    return std::make_shared<exprs::sinus>(value);
}

expr cos(expr value) {
    return std::make_shared<exprs::cosinus>(value);
}

expr log(expr value) {
    return std::make_shared<exprs::logarithm>(value);
}

expr expr::number(double value) {
    return std::make_shared<exprs::number>(exprs::number(value));
}

expr expr::variable(std::string name) {
    return std::make_shared<exprs::variable>(name);
}

bool operator==(const expr &a, const expr &b) {
    return a->equals(*b->shared_from_this());
}

std::ostream &operator<<(std::ostream &os, const expr &a) {
    a->write(os, expr_base::WriteFormat(expr_base::WriteFormat::Prefix));
    return os;
}

bool is_operator(const Token &token) {
    return (token.id == TokenId::Plus || token.id == TokenId::Minus || token.id == TokenId::Multiply ||
            token.id == TokenId::Divide || token.id == TokenId::Power);
}

expr create_expression_tree(const std::string &expression) {
    auto xxx = std::stringstream(expression);

    int parens = 0;
    bool last_num_var = false;

    auto output = std::stack<expr>();
    auto operators = std::stack<Token>();

    auto tknzr = Tokenizer(xxx);
    while (true) {
        try {
            auto token = tknzr.next();

            if (token.id == TokenId::Number) {
                if (last_num_var) {
                    throw parse_error("nono");
                }

                output.push(expr::number(token.number));
                last_num_var = true;
            } else if (token.id == TokenId::Identifier) {
                if (last_num_var) {
                    throw parse_error("nono");
                }

                if (token.identifier == "sin" || token.identifier == "cos" || token.identifier == "log") {
                    auto temp_token = tknzr.next();

                    if (temp_token.id == TokenId::LParen) {
                        operators.push(token);
                        operators.push(temp_token);
                        parens++;
                    } else if (temp_token.id == TokenId::Number) {
                        throw parse_error("");
                    } else if (token.id == TokenId::End) break;
                    else {
                        output.push(expr::variable(token.identifier));
                          last_num_var = true;
                    }

                } else {
                    output.push(expr::variable(token.identifier));
                    last_num_var = true;
                }

            } else if (is_operator(token)) {
                last_num_var = false;
                if (!operators.empty()) {

                    auto top = operators.top();

                    while (!operators.empty()) {
                        if ((is_operator(top)) &&
                            ((top.op_precedence() > token.op_precedence()) ||
                             (top.op_precedence() == token.op_precedence() &&
                              token.associativity() == Associativity::Left)) && (top.id != TokenId::LParen)) {
                            if (output.size() < 2) {
                                throw parse_error("oh no");
                            }
                            auto temp1 = output.top();
                            output.pop();
                            auto temp2 = output.top();
                            output.pop();

                            if (top.id == TokenId::Plus) {
                                output.push(operator+(temp2, temp1));
                            } else if (top.id == TokenId::Minus) {
                                output.push(operator-(temp2, temp1));
                            } else if (top.id == TokenId::Multiply) {
                                output.push(operator*(temp2, temp1));
                            } else if (top.id == TokenId::Divide) {
                                output.push(operator/(temp2, temp1));
                            } else if (top.id == TokenId::Power) {
                                output.push(pow(temp2, temp1));
                            }
                            operators.pop();
                            if (!operators.empty()) {
                                top = operators.top();
                            }


                        } else {
                            break;
                        }
                    }
                }
                operators.push(token);
            } else if (token.id == TokenId::LParen) {
                if (last_num_var) {
                    throw unknown_function_exception("");
                }
                operators.push(token);
                parens++;
            } else if (token.id == TokenId::RParen) {
                if (parens <= 0) {
                    throw parse_error("error");
                }
                parens--;

                while (!operators.empty()) {
                    auto temp = operators.top();
                    operators.pop();

                    if (temp.id == TokenId::LParen && operators.empty()) {
                         break;
                    }
                    else if(temp.id == TokenId::LParen && !(operators.top().identifier == "sin" || operators.top().identifier == "cos" || operators.top().identifier == "log")){
                        break;
                    }
                    else {
                        if(output.empty()){
                            throw parse_error("i have here job!!!");
                        }

                        if (temp.identifier == "sin") {
                            auto output_temp = output.top();
                            output.pop();
                            output.push(sin(output_temp));
                            break;

                        } else if (temp.identifier == "cos") {
                            auto output_temp = output.top();
                            output.pop();
                            output.push(cos(output_temp));
                            break;

                        } else if (temp.identifier == "log") {
                            auto output_temp = output.top();
                            output.pop();
                            output.push(log(output_temp));
                            break;

                        } else if (is_operator(temp)) {
                            auto output_temp = output.top();
                            output.pop();

                            auto output_temp2 = output.top();
                            output.pop();

                            if (temp.id == TokenId::Plus) {
                                output.push(operator+(output_temp2, output_temp));
                            } else if (temp.id == TokenId::Minus) {
                                output.push(operator-(output_temp2, output_temp));
                            } else if (temp.id == TokenId::Multiply) {
                                output.push(operator*(output_temp2, output_temp));
                            } else if (temp.id == TokenId::Divide) {
                                output.push(operator/(output_temp2, output_temp));
                            } else if (temp.id == TokenId::Power) {
                                output.push(pow(output_temp2, output_temp));
                            }

                        }

                    }
//                    operators.pop();
                }
            } else if (token.id == TokenId::End) break;
        }
        catch (const tokenize_error &e) {
            throw e;
        }
    }

    if (parens != 0) {
        throw parse_error("parens error");
    }
    while (!operators.empty()) {

        auto opr = operators.top();
        operators.pop();

        if (output.empty()) {
            throw parse_error("oh no again");
        }

        if (opr.identifier == "sin") {
            auto output_temp = output.top();
            output.pop();
            output.push(sin(output_temp));
        } else if (opr.identifier == "cos") {
            auto output_temp = output.top();
            output.pop();
            output.push(cos(output_temp));
        } else if (opr.identifier == "log") {
            auto output_temp = output.top();
            output.pop();
            output.push(log(output_temp));
        } else if (is_operator(opr)) {
            if (output.size() < 2) {
                throw parse_error("oh no");
            }
            auto output_temp = output.top();
            output.pop();

            auto output_temp2 = output.top();
            output.pop();
            if (opr.id == TokenId::Plus) {
                output.push(operator+(output_temp2, output_temp));
            } else if (opr.id == TokenId::Minus) {
                output.push(operator-(output_temp2, output_temp)
                );
            } else if (opr.id == TokenId::Multiply) {
                output.push(operator*(output_temp2, output_temp)
                );
            } else if (opr.id == TokenId::Divide) {
                output.push(operator/(output_temp2, output_temp)
                );
            } else if (opr.id == TokenId::Power) {
                output.push(pow(output_temp2, output_temp)
                );
            }
        }
    }
    if (output.empty()) {
        throw parse_error("nula");
    }
    return output.top();

}


