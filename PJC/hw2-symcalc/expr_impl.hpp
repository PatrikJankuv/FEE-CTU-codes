#pragma once

#include "expr.hpp"
#include <iosfwd>
#include <utility>

namespace exprs {
    class number : public expr_base {
        friend class ::expr;

    public:
        explicit number(double num) : value(num) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        ~number() override = default;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        double value;
    };

    class variable : public expr_base {
        friend class ::expr;

    public:
        explicit variable(std::string value) : value(std::move(value)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        std::string value;
    };

    class plus : public expr_base {
        friend class ::expr;

    public:
        explicit plus(expr  left, expr  right) : left(std::move(left)), right(std::move(right)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr left;
        expr right;
    };

    class minus : public expr_base {
        friend class ::expr;

    public:
        explicit minus(expr  left, expr  right) : left(std::move(left)), right(std::move(right)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr left;
        expr right;
    };

    class multiply : public expr_base {
        friend class ::expr;

    public:
        explicit multiply(expr  left, expr  right) : left(std::move(left)), right(std::move(right)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;


        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr left;
        expr right;
    };

    class divide : public expr_base {
        friend class ::expr;

    public:
        explicit divide(expr  left, expr  right) : left(std::move(left)), right(std::move(right)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;


        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr left;
        expr right;
    };

    class power : public expr_base {
        friend class ::expr;

    public:
        explicit power(expr  bottom, expr  top) : bottom(std::move(bottom)), top(std::move(top)) {};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr bottom;
        expr top;
    };

    class sinus : public expr_base {
        friend class ::expr;

    public:
        explicit sinus(expr  value) : value(std::move(value)){};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr value;
    };

    class cosinus : public expr_base {
        friend class ::expr;

    public:
        explicit cosinus(expr& value) : value(value){};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr value;
    };

    class logarithm : public expr_base {
        friend class ::expr;

    public:
        explicit logarithm(expr value) : value(std::move(value)){};

        double evaluate(const variable_map_t &variables) const override;

        expr derive(std::string const &variable) const override;

        expr simplify() const override;

        void write(std::ostream &out, WriteFormat fmt) const override;

        bool equals(const expr_base &b) const override;

    private:
        expr value;
    };
}
