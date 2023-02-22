#include <stdio.h>
#include <math.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
using namespace std;

void print_table(vector<vector<int>> &table, int heigh, int width)
{
    for (int i = 0; i < heigh; i++)
    {
        for (int j = 0; j < width; j++)
        {
            cout << table[j][i] << "";
        }
        cout << endl;
    }
}

int minimum(int del, int insert, int leave)
{
    int temp = del;

    if (insert < temp)
    {
        temp = insert;
    }
    if (leave < temp)
    {
        temp = leave;
    }
    return temp;
}

char getSame(int del, int insert, int leave, int cur)
{
    char result;

    if (cur == insert)
    {
        result = 'i';
    }
    if (cur == leave)
    {
        result = 'l';
    }
    if (cur == del)
    {
        result = 'd';
    }

    return result;
}

char min(int del, int insert, int leave)
{
    int temp = 1000;
    char result = 'd';
    if (insert <= temp)
    {
        temp = insert;
        result = 'i';
    }
    if (leave <= temp)
    {
        result = 'l';
    }

    if (del <= temp)
    {
        temp = del;
        result = 'd';
    }

    return result;
}

int getDistance(string word, string pattern)
{
    // cout << word << " a " << pattern << endl;
    // cout << "get distance: w:" << word.size() << " p: " << pattern.size() << endl;
    if (word.size() == 0)
    {
        return INT8_MAX;
    }
    int heigh = pattern.size();
    int width = word.size();
    vector<vector<int>> table(word.size() + 1, vector<int>(pattern.size() + 1));

    for (int i = 0; i <= heigh; i++)
    {
        table[0][i] = i;
    }

    for (int i = 0; i <= width; i++)
    {
        table[i][0] = i;
    }

    for (int j = 1; j <= width; j++)

    {
        for (int i = 1; i <= heigh; i++)
        {
            int del = table[j - 1][i] + 1;
            int insert = table[j][i - 1] + 1;
            int leave = table[j - 1][i - 1];

            if (pattern[i - 1] != word[j - 1])
            {
                leave += 1;
            }

            table[j][i] = minimum(del, insert, leave);
        }
    }
    // cout << endl;
    // print_table(table, heigh + 1, width + 1);
    // cout << "distance: " << table[width][heigh] << endl;
    return table[width][heigh];
}

void howManyUncovered(string word, string pattern, int dist, vector<int> &hasSub)
{
    string cur_sub;
    int cur = -2;

    for (int i = 0; i < word.size(); i++)
    {
        // cur = hasSub[i];

        // cout << cur << " x " << hasSub[i] << endl;
        if (cur != hasSub[i] && hasSub[i] != -1)
        {
            // cout << cur << ": " << cur_sub << endl;
            // cout << "shoul be empyt \n";
            cur_sub.clear();
            cur_sub = word[i];
            cur = hasSub[i];
        }
        else
        {
            if (hasSub[i] == -1)
            {
                cur_sub += word[i];
                if (getDistance(cur_sub, pattern) <= dist)
                {
                    hasSub[i] = cur;
                }
            }
            else
            {
                cur_sub += word[i];
            }
        }
    }

    cur = hasSub[word.size()];

    for (int i = word.size(); i >= 0; i--)
    {

        // cout << cur << " x " << word[i] << endl;
        if (cur != hasSub[i] && hasSub[i] != -1)
        {
            // cout << cur << ": " << cur_sub << endl;
            // cout << "shoul be empyt \n";
            cur_sub.clear();
            cur_sub = word[i];
            cur = hasSub[i];
        }
        else
        {
            if (hasSub[i] == -1)
            {
                cur_sub += word[i];
                reverse(cur_sub.begin(), cur_sub.end());

                if (getDistance(cur_sub, pattern) <= dist)
                {
                    hasSub[i] = cur;
                }
                reverse(cur_sub.begin(), cur_sub.end());
            }
            else
            {
                cur_sub += word[i];
            }
        }
    }

    //cout -1
    int fails = 0;
    for (auto i : hasSub)
    {
        i == -1 ? fails++ : false;
    }

    cout << " " << fails;
}

void fullFillTable(vector<vector<int>> &table, int heigh, int width, string word, string pattern, int dist, vector<int> &hasSub)
{
    int intervals = 0, non_covered = 0;
    int last_border = 0;

    for (int i = 0; i < heigh; i++)
    {
        table[0][i] = i;
    }

    for (int j = 1; j < width; j++)

    {
        for (int i = 1; i < heigh; i++)
        {
            int del = table[j - 1][i] + 1;
            int insert = table[j][i - 1] + 1;
            int leave = table[j - 1][i - 1];

            if (pattern[i - 1] != word[j - 1])
                leave += 1;

            table[j][i] = minimum(del, insert, leave);
        }

        if (table[j][heigh - 1] == dist)
        {
            // cout << word[j - 1] << endl;
            // cout << endl;
            // print_table(table, heigh, width);
            //find length off substring
            int x = j, y = heigh - 1;
            int d_break = j;

            // cout << "x " << x << " y " << y << " = " << table[x][y] << endl;
            // cout << "last " << last_border << endl;
            do
            {
                int del = table[x - 1][y];
                // cout << "del x " << x - 1 << " y " << y << " = " << table[x - 1][y] << endl;

                int insert = table[x][y - 1];
                // cout << "insert x " << x << " y " << y - 1 << " = " << table[x][y - 1] << endl;

                int leave = table[x - 1][y - 1];
                // cout << "leave x " << x - 1 << " y " << y - 1 << " = " << table[x - 1][y - 1] << endl;

                char result = min(del, insert, leave);

                if (word[x - 1] == pattern[y - 1])
                {
                    result = getSame(del, insert, leave, table[x - 1][y - 1]);
                }

                // cout << result << endl;

                switch (result)
                {
                case 'i':
                    --y;
                    break;
                case 'l':
                    --x;
                    --y;
                    break;
                case 'd':
                    --x;
                    break;
                default:
                    break;
                }

            } while (y > 0 && x > last_border + 1);

            // cout << "result x " << x << " y " << y << " = " << table[x][y] << endl;

            if (x != last_border)
            {
                // cout << "fuck " << x - last_border - 1 << endl;
                non_covered += x - last_border - 1;
            }

            // cout << "x " << x << " j " << j << " last b " << last_border << endl;
            for (int k = x; k <= j; k++)
            {
                hasSub[k - 1] = d_break - 1;
            }

            // if (hasSub[x] == -1)
            // {
            //     hasSub[x - 1] = d_break - 1;
            // }

            if (hasSub[x - 2] == -1 && last_border == x - 1)
            {
                hasSub[x - 1] = d_break - 1;
            }

            // cout << "sub: ";
            string shoul_first;
            for (int k = x; k <= j; k++)
            {
                // cout << word[k - 1] << " ";
                shoul_first += word[k - 1];
            }
            // cout << endl;

            // cout << shoul_first << endl;

            if (getDistance(shoul_first, pattern) > dist)
            {
                hasSub[x - 1] = -1;
            }

            // cout << "non " << non_covered << endl;
            //reset column
            intervals++;

            for (int i = 0; i < heigh; i++)
            {
                table[j][i] = i;
            }

            // for (int k = last_border; k < j; k++)
            // {
            //     cout << word[k];
            // }

            // cout << endl;
            // for (int k = last_border; k < j; k++)
            // {
            //     cout << table[k][heigh - 1] << "|";
            // }
            // cout << endl;

            // for (int k = x - 1; k < j; k++)
            // {
            //     cout << word[k];
            // }
            // cout << endl;
            // for (int k = x - 1; k < j; k++)
            // {
            //     cout << table[k][heigh - 1] << "|";
            // }

            // cout << "\n---------------" << endl;

            last_border = j;
        }
    }
    // cout << endl;
    // print_table(table, heigh, width);

    cout << intervals;
}

// driver program to test above function
int main()
{
    ios::sync_with_stdio(false);
    string word;
    string pattern;
    int dist;

    cin >> word;
    cin >> pattern;
    cin >> dist;

    vector<int> hasSub(word.size(), -1);

    vector<vector<int>> table(word.size() + 1, vector<int>(pattern.size() + 1));

    fullFillTable(table, pattern.size() + 1, word.size() + 1, word, pattern, dist, hasSub);
    vector<vector<int>> table2(word.size() + 1, vector<int>(pattern.size() + 1));
    // cout << endl;
    // for (auto l : hasSub)
    // {
    //     cout << l << " ";
    // }
    // cout << endl;
    howManyUncovered(word, pattern, dist, hasSub);
    // cout << endl;
    // cout << " dist " << getDistance("vvede", "jede") << endl;
    return 0;
}