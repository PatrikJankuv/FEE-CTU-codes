#include "expr_impl.hpp"

#include <iostream>
#include <cmath>
#include <limits>

namespace exprs {
    void number::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << value;
    }

    double number::evaluate(const variable_map_t &variables) const {
        return value;
    }

    expr number::derive(const std::string &variable) const {
        return expr::ZERO;
    }

    expr number::simplify() const {
        return shared_from_this();
    }

    bool number::equals(const expr_base &b) const {
        if (const number *number_temp = dynamic_cast<number const *>(b.shared_from_this().get())) {
            return number_temp->value == value;
        }
        return false;
    }

    expr variable::derive(const std::string &variable) const {
        if(this->value == variable){
            return expr::ONE;
        }else{
            return expr::ZERO;
        }

    }

    expr variable::simplify() const {
        return shared_from_this();
    }

    double variable::evaluate(const expr_base::variable_map_t &variables) const {
        if(variables.find(value) == variables.end()){
            throw unbound_variable_exception("not found");
        }
        return variables.at(value);
    }

    void variable::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << value;
    }

    bool variable::equals(const expr_base &b) const {
        if (const variable *variable_temp = dynamic_cast<variable const *>(b.shared_from_this().get())) {
            return variable_temp->value == value;
        }
        return false;
    }

    void minus::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(- " << left << " " << right << ")";
    }

    bool minus::equals(const expr_base &b) const {
        if(const minus* a = dynamic_cast<minus const*>(b.shared_from_this().get())) {
            return a->left == left && a->right == right;
        }
        return false;
    }

    expr minus::simplify() const {
        auto left_temp = left->simplify();
        auto right_temp = right->simplify();

        if (right_temp == expr::ZERO) {
            return left_temp;
        } else {
            return left_temp - right_temp;
        }
    }

    expr minus::derive(const std::string &variable) const {
        expr left_temp = left->derive(variable);
        expr right_temp = right->derive(variable);
        return std::make_shared<exprs::minus>(minus(left_temp,right_temp));
    }

    double minus::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp_left = left->evaluate(variables);
        auto temp_right = right->evaluate(variables);
        return temp_left - temp_right;
    }

    void plus::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(+ " << left << " " << right << ")";
    }

    double plus::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp_left = left->evaluate(variables);
        auto temp_right = right->evaluate(variables);
        return temp_left + temp_right;
    }

    expr plus::derive(const std::string &variable) const {
        expr left_temp = left->derive(variable);
        expr right_temp = right->derive(variable);
        return std::make_shared<exprs::plus>(plus(left_temp,right_temp));
    }

    expr plus::simplify() const {
        auto left_temp = left->simplify();
        auto right_temp = right->simplify();

        if (left_temp == expr::ZERO) {
            return right_temp;
        } else if (right_temp == expr::ZERO) {
            return left_temp;
        } else {
            return std::make_shared<exprs::plus>(plus(left_temp,right_temp));
        }
    }

    bool plus::equals(const expr_base &b) const {
        if(const plus* a = dynamic_cast<plus const*>(b.shared_from_this().get())) {
            return a->left == left && a->right == right;
        }
        return false;
    }

    double multiply::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp_left = left->evaluate(variables);
        auto temp_right = right->evaluate(variables);
        return temp_left * temp_right;
    }

    expr multiply::derive(const std::string &variable) const {
        auto left_temp = left->derive(variable);
        auto right_temp = right->derive(variable);

        auto first = std::make_shared<multiply>(multiply(left_temp, right));
        auto second = std::make_shared<multiply>(multiply(left, right_temp));
        return std::make_shared<exprs::plus>(plus(first,second));

    }

    expr multiply::simplify() const {
        auto left_temp = left->simplify();
        auto right_temp = right->simplify();

        if (left_temp == expr::ZERO || right_temp == expr::ZERO) {
            return expr::ZERO;
        } else if (right_temp == expr::ONE) {
            return left_temp;
        } else if (left_temp == expr::ONE) {
            return right_temp;
        } else {
            return std::make_shared<exprs::multiply>(multiply(left_temp,right_temp));
        }
    }

    void multiply::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(* " << left << " " << right << ")";
    }

    bool multiply::equals(const expr_base &b) const {
        if(const multiply* a = dynamic_cast<multiply const*>(b.shared_from_this().get())) {
            return a->left == left && a->right == right;
        }
        return false;
    }

    double divide::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp_left = left->evaluate(variables);
        auto temp_right = right->evaluate(variables);
        return temp_left / temp_right;
    }

    expr divide::derive(const std::string &variable) const {
        auto left_temp = left->derive(variable);
        auto right_temp = right->derive(variable);

        auto first_top = std::make_shared<multiply>(multiply(left_temp, right));
        auto second_top = std::make_shared<multiply>(multiply(left, right_temp));

        auto top = std::make_shared<exprs::minus>(minus(first_top, second_top));
        auto bottom = std::make_shared<exprs::power>(power(right, std::make_shared<exprs::number>(number(2))));
        return std::make_shared<exprs::divide>(divide(top, bottom));
    }

    expr divide::simplify() const {
        auto left_temp = left->simplify();
        auto right_temp = right->simplify();

        if (left_temp == expr::ZERO && right_temp == expr::ZERO) {
            return left_temp / right_temp;
        } else if (right_temp == expr::ZERO) {
            return left_temp;
        } else if (left_temp == expr::ZERO) {
            return expr::ZERO;
        } else if (right_temp == expr::ONE) {
            return left_temp;

        } else {
            return left_temp / right_temp;
        }
    }

    void divide::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(/ " << left << " " << right << ")";
    }

    bool divide::equals(const expr_base &b) const {
        if(const divide* a = dynamic_cast<divide const*>(b.shared_from_this().get())) {
            return a->left == left && a->right == right;
        }
        return false;
    }

    double power::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp_bottom = bottom->evaluate(variables);
        auto temp_top = top->evaluate(variables);
        return pow(temp_bottom, temp_top);
    }

    expr power::derive(const std::string &variable) const {
        auto mul = shared_from_this();

        auto temp_top = std::make_shared<multiply>(multiply(bottom->derive(variable), this->top));
        auto left = std::make_shared<divide>(divide(temp_top, bottom));
        auto right = std::make_shared<multiply>(multiply(log(bottom), top->derive(variable)));
        auto mul2 = std::make_shared<plus>(plus(left, right));
        return std::make_shared<multiply>(multiply(mul, mul2));
    }

    expr power::simplify() const {
        auto bottom_temp = bottom->simplify();
        auto top_temp = top->simplify();

        if (top_temp == expr::ZERO) {
            return expr::ONE;
        } else if (bottom_temp == expr::ZERO) {
            return expr::ZERO;
        } else if (top_temp == expr::ONE) {
            return bottom_temp;
        } else {
            return std::make_shared<exprs::power>(bottom_temp, top_temp);
        }
    }

    void power::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(^ " << bottom << " " << top << ")";
    }

    bool power::equals(const expr_base &b) const {
        if(const power* a = dynamic_cast<power const*>(b.shared_from_this().get())) {
            return a->bottom == bottom && a->top == top;
        }
        return false;
    }

    double sinus::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp = value->evaluate(variables);
        return sin(temp);
    }

    expr sinus::derive(const std::string &variable) const {
        auto temp = value->derive(variable);
        return std::make_shared<exprs::multiply>(multiply(cos(this->value), temp));
    }

    expr sinus::simplify() const {
        auto temp = value->simplify();
        return std::make_shared<exprs::sinus>(sinus(temp));
    }

    void sinus::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(sin " << value << ")";
    }

    bool sinus::equals(const expr_base &b) const {
        if(const sinus* a = dynamic_cast<sinus const*>(b.shared_from_this().get())) {
            return a->value == value;
        }
        return false;
    }

    double cosinus::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp = value->evaluate(variables);
        return cos(temp);
    }

    expr cosinus::derive(const std::string &variable) const {
        auto sin_temp = std::make_shared<exprs::sinus>(sinus(value));
        auto temp = std::make_shared<exprs::minus>(minus(expr::ZERO, sin_temp));

        return std::make_shared<multiply>(temp, value->derive(variable));
    }

    expr cosinus::simplify() const {
        auto temp = value->simplify();
        return std::make_shared<exprs::cosinus>(cosinus(temp));
    }

    void cosinus::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(cos " << value << ")";
    }

    bool cosinus::equals(const expr_base &b) const {
        if(const cosinus* a = dynamic_cast<cosinus const*>(b.shared_from_this().get())) {
            return a->value == value;
        }
        return false;
    }

    double logarithm::evaluate(const expr_base::variable_map_t &variables) const {
        auto temp = value->evaluate(variables);
        if(temp <= 0){
            throw domain_exception("negative");
        }
        return log(temp);
    }

    expr logarithm::derive(const std::string &variable) const {
        return std::make_shared<divide>(divide(value->derive(variable), value));
    }

    expr logarithm::simplify() const {
        auto temp = value->simplify();

        if(temp == expr::ONE){
            return expr::ZERO;
        }else{
            return std::make_shared<exprs::logarithm>(logarithm(temp));
        }
    }

    void logarithm::write(std::ostream &out, expr_base::WriteFormat fmt) const {
        out << "(log " << value << ")";
    }

    bool logarithm::equals(const expr_base &b) const {
        if(const logarithm* a = dynamic_cast<logarithm const*>(b.shared_from_this().get())) {
            return a->value == value;
        }
        return false;
    }
} // namespace exrs
