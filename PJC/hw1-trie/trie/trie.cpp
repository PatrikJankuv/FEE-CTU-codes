#include "trie.hpp"

#include <cassert>
#include <string>
#include <iterator>
#include <random>

//stage 1


trie::trie() {
    m_root = new trie_node();
    m_root->parent = nullptr;

    trie_node** childrens = m_root->children;


    for (size_t i = 0; i < num_chars; i++) {
        childrens[i] = nullptr;
    }
}

trie::trie(const std::vector<std::string>& strings) : trie() {
    for (auto word : strings) {
        insert(word);
    }
}

void deallocate(trie_node* node) {
    if (node == nullptr) return;

    for (size_t i = 0; i < num_chars; i++) {
        if (!(node->children[i] == nullptr)) {
            deallocate(node->children[i]);
        }
    }

//    delete konec;
    delete node;
}


trie::~trie() {
    deallocate(m_root);
}

bool trie::insert(const std::string& str) {
    trie_node* curr_node = m_root;

    //empty insert solution
    if (str.length() == 0) {
        curr_node->is_terminal = true;
        m_size++;
    }

    for (size_t i = 0; i < str.length(); i++)
    {
        char temp_char = str[i];

        if (curr_node->children[(int)temp_char]) {
            curr_node = curr_node->children[(int)temp_char];
        }
        else
        {
            curr_node->children[(int)temp_char] = new trie_node();

            trie_node** childrens = curr_node->children[(int)temp_char]->children;

            for (size_t i = 0; i < num_chars; i++) {
                childrens[i] = nullptr;
            }

            curr_node->children[(int)temp_char]->payload = temp_char;
            curr_node->children[(int)temp_char]->parent = curr_node;

            curr_node = curr_node->children[(int)temp_char];
        }
    }

    //if contains string return false
    if (curr_node->is_terminal) {
        return false;
    }


    curr_node->is_terminal = true;
    m_size++;

    return true;
}

//check if trie contains string

bool trie::contains(const std::string& str) const {
    trie_node* curr_node = m_root;

    for (size_t i = 0; i < str.length(); i++)
    {
        char temp = str[i];

        if (curr_node->children[(int)temp]) {
            curr_node = curr_node->children[(int)temp];
        }
        else {
            return false;
        }
    }

    if (curr_node->is_terminal == true) {
        return true;
    }

    return false;
}

bool trie::erase(const std::string& str) {
    trie_node* curr_node = m_root;

    //empty insert solution
    if (str.length() == 0) {
        curr_node->is_terminal = false;
        m_size--;
    }

    for (size_t i = 0; i < str.length(); i++)
    {
        char temp = str[i];

        if (curr_node->children[(int)temp]) {
            curr_node = curr_node->children[(int)temp];
        }
        else {
            return false;
        }
    }

    //if element was erased, cannot be erase again and for this reason has to return false
    if (curr_node->is_terminal == false) {
        return false;
    }

    curr_node->is_terminal = false;
    m_size--;
    return true;

}

size_t trie::size() const {
    return m_size;
}

bool trie::empty() const {
    if (m_size == 0) {
        return true;
    }
    else {
        return false;
    }
}

//stage 2

trie_node* get_left_for_begin(trie_node node, int start_from) {
    trie_node* result = nullptr;


    for (int i = start_from; i < num_chars; i++) {
        if (!(node.children[i] == nullptr) && node.children[i]->is_terminal) {
            return node.children[i];
        }
        else if (!(node.children[i] == nullptr) && !node.children[i]->is_terminal) {
            result = get_left_for_begin(*node.children[i], 0);

            //need to break
            if (!(result == nullptr)) {
                return result;
            }
        }
    }

    if (result == nullptr) {
        if (!(node.parent == nullptr)) {
            result = get_left_for_begin(*node.parent, node.payload + 1);
        }
    }

    return result;
}

trie::const_iterator trie::begin() const {

//    trie_node* temp = m_root;

    //empty solution
    if (m_root->is_terminal == true) {
//        const_iterator* it = new trie::const_iterator(m_root);
        return m_root;
    }

    return get_left_for_begin(*m_root, 0);
}

trie::const_iterator trie::end() const {
    return nullptr;
}

trie::const_iterator& trie::const_iterator::operator++() {
    const trie_node* temp = current_node;
    temp = get_left_for_begin(*current_node, 0);
    current_node = temp;

    return *this;

}

trie::const_iterator trie::const_iterator::operator++(int) {
    this->operator++();
    return *this;
}

trie::const_iterator::const_iterator(const trie_node* node) {
    current_node = node;
}

bool trie::const_iterator::operator==(const const_iterator& rhs) const {
    return current_node == rhs.current_node;
}

bool trie::const_iterator::operator!=(const const_iterator& rhs) const {
    return (!(rhs.operator==(*this)));
}

trie::const_iterator::reference trie::const_iterator::operator*() const {
    const trie_node* temp = current_node;
    std::string str = "";

    while (temp->parent != nullptr) {
        str = temp->payload + str;
        temp = temp->parent;
    }

    return str;
}


//3rd set of tests

trie::trie(trie&& rhs) : trie() {
    auto it = rhs.begin();
    auto et = rhs.end();

    while (it != et) {
        insert(*it);
        rhs.erase(*it);
        ++it;
    }
}

trie::trie(const trie& rhs) : trie(){
    auto it = rhs.begin();
    auto et = rhs.end();

    while (it != et) {
        insert(*it);
        ++it;
    }
}

trie& trie::operator=(const trie& rhs) {
    if (this == &rhs) {
        return *this;
    }

    *this = trie(rhs);
    return *this;
}

trie& trie::operator=(trie&& rhs) {
    swap(rhs);

    return *this;
}

void trie::swap(trie& rhs) {
    trie_node* node = m_root;
    size_t nodeSize = m_size;

    m_root = rhs.m_root;
    m_size = rhs.m_size;

    rhs.m_root = node;
    rhs.m_size = nodeSize;
}

void swap(trie& lhs, trie& rhs) {
    lhs.swap(rhs);
}

bool trie::operator==(const trie& rhs) const {
    std::vector<std::string> allWordsFromRHS = { rhs.begin(), rhs.end() };
    std::vector<std::string> allWords = { this->begin(), this->end() };

    return allWords == allWordsFromRHS;
}

bool operator!=(const trie& lhs, const trie& rhs) {
    return !(lhs == rhs);
}

bool trie::operator<(const trie& rhs) const {
    return std::lexicographical_compare(this->begin(), this->end(), rhs.begin(), rhs.end());
}

bool operator>(const trie& lhs, const trie& rhs) {
    if (lhs < rhs || lhs == rhs) {
        return false;
    }
    else {
        return true;
    }
}

bool operator<=(const trie& lhs, const trie& rhs) {
    return !(lhs > rhs);
}

bool operator>=(const trie& lhs, const trie& rhs) {
    return !(lhs < rhs);
}

std::ostream& operator<<(std::ostream& out, trie const& trie) {
    return out;
}


//4th set of tests
trie_node* find_string_node(trie_node * m_root, const std::string& str) {
    trie_node* curr_node = m_root;

    for (size_t i = 0; i < str.length(); i++)
    {
        char temp = str[i];

        if (curr_node->children[(int)temp]) {
            curr_node = curr_node->children[(int)temp];
        }
        else {
            return nullptr;
        }
    }

    return curr_node;
}

std::vector<std::string> trie::search_by_prefix(const std::string& str) const {
    std::vector<std::string> prefix;
    trie_node* root_of_prefix = find_string_node(m_root, str);

    if (root_of_prefix == nullptr) {
        return prefix;
    }

    if (root_of_prefix->is_terminal) {
        prefix.push_back(str);
    }

    auto temp = get_left_for_begin(*root_of_prefix, 0);
    auto temp_it = new trie::const_iterator(temp);
    auto temp_et = this->end();

    std::vector<std::string> allWordsFromSubTrie = { *temp_it, temp_et };


    for (auto s : allWordsFromSubTrie) {
        prefix.push_back(s);
    }

    delete temp_it;
    return prefix;
}

std::vector<std::string> trie::get_prefixes(const std::string& str) const {
    std::vector<std::string> prefixes;
    trie_node* temp_leaf = find_string_node(m_root, str);

    if (temp_leaf == nullptr) {
        std::string sub = "";

        for (int i = str.length() - 1; i > 0; i--) {
            sub = str.substr(0, i);
            temp_leaf = find_string_node(m_root, sub);

            if (temp_leaf != nullptr) {
                break;
            }
        }
    }

    if (temp_leaf == nullptr) {
        return prefixes;
    }


    while (!(temp_leaf->parent == nullptr)) {

        if (temp_leaf->is_terminal) {
            std::string str = "";

            const trie_node* temp = temp_leaf;


            while (temp->parent != nullptr) {
                str = temp->payload + str;
                temp = temp->parent;
            }
            prefixes.push_back(str);
        }
        temp_leaf = temp_leaf->parent;
    }

    return prefixes;
}

trie trie::operator|(trie const& rhs) const {
    trie trie;
    std::vector<std::string> allWordsFromRHS = { rhs.begin(), rhs.end() };
    std::vector<std::string> allWords = { this->begin(), this->end() };

    for (auto word : allWordsFromRHS) {
        trie.insert(word);
    }
    for (auto word : allWords) {
        trie.insert(word);
    }

    return trie;
}

trie trie::operator&(trie const& rhs) const {
    trie trie;
    std::vector<std::string> allWords = { this->begin(), this->end() };


    for (auto word : allWords) {
        if (rhs.contains(word)) {
            trie.insert(word);
        }
    }

    return trie;
}