#include <stdio.h>
#include <iostream>
#include <vector>
#include <algorithm>
#include <fstream>
#include <sstream>
using namespace std;
int MAX_INTEGER = 2147483646;

struct Task{
    int p;
    int r;
    int d;
    int index;

    Task(int p, int r, int d, int index){
        this->p = p;
        this->r = r;
        this->d = d;
        this->index = index;
    }
};

vector<Task*> tasks;
vector<Task*> current_solution;
int lower_index = -1;

int c;
int perms;
int UB;
bool cancelRecursion;


int max(int a, int b){
    return a > b ? a : b;
}

int min(int a, int b){
    return a < b ? a : b;
}

void printTask(){
    for(auto t : tasks){
        cout << t->p << " " << t->r << " " << t->d  << " i: " << t->index << endl;
    }
}

void copy_tasks_to_solution(){
    current_solution.clear();
    for(auto t : tasks){
        current_solution.push_back(t);
    }
}


bool is_missed_deadline(int index_of_unassigned_tasks){
    for(int i = index_of_unassigned_tasks; i < tasks.size(); i++){
        if(max(c,  tasks[i]->r) + tasks[i]->p > tasks[i]->d){
            return true;
        }    
    }
    return false;
}

bool is_lower_bound_on_solution(int index_of_unassigned_tasks){
    int min_r = MAX_INTEGER;
    int sum_p = 0;

    for(int i = index_of_unassigned_tasks; i < tasks.size(); i++){
        if(tasks[i]->r < min_r) min_r = tasks[i]->r;
        sum_p += tasks[i]->p;       
    }

    int LB = max(c, min_r) + sum_p;
    
    return LB < UB;
}

bool is_minr_smaller_than_c(int index_of_unassigned_tasks){
    int min_r = MAX_INTEGER;

    for(int i = index_of_unassigned_tasks; i < tasks.size(); i++){
        if(tasks[i]->r < min_r) min_r = tasks[i]->r;   
    }
    return c <= min_r;
}

void permute(int l, int r)
{
	if (l == r){
        perms++;
        c +=  tasks[l]->p;
        UB = c;
        copy_tasks_to_solution();
        c -=  tasks[l]->p;
    }
	else
	{
		for (int i = l; i <= r; i++)
		{

            if(l <= lower_index) continue;

			swap(tasks[l], tasks[i]);
            int time_gap;
            if(c >= tasks[l]->r){
                time_gap = tasks[l]->p;
            }else{
                time_gap = tasks[l]->p + tasks[l]->r - c;
            }

            c += time_gap;
            // printTask();
            // cout << endl;
            if(!is_missed_deadline(l+1)){
                if(is_lower_bound_on_solution(l+1)){
                    if(!is_minr_smaller_than_c(l+1)){                        
                        permute(l+1, r);
                    }else{
                        lower_index = i;
                        permute(l+1, r);
                    }                    
                }                
            }
            
            c-= time_gap; 
			swap(tasks[l], tasks[i]);
		    }
        
	}
}

void loadData(const char * path){
    string line;
    int number_of_tasks;

    ifstream indata;
    indata.open(path);
    std::getline(indata, line);
    istringstream iss(line);
    iss >> number_of_tasks;

    for (int i = 0; i < number_of_tasks; i++){
        int p, r, d;

        getline(indata, line);
        istringstream iss(line);
        iss >> p >> r >> d;
        Task * temp = new Task(p, r, d, i);
        tasks.push_back(temp);
    }

    sort(tasks.begin(), tasks.end(), []( Task* lhs, Task* rhs) {
      return lhs->r < rhs->r;
   });
}

void write_solution(const char * path){
    ofstream output_file(path);

    if(perms == 0){
        output_file << "-1" << endl;
        output_file.close(); 
        return;
    }

    vector<int> out_put (current_solution.size(), -1);

    int time = 0;
    for(auto t : current_solution){
        // cout << t->p << " " << t->r << " " << t->d  << " " << t->index<< endl;
        if(time >= t->r){
            out_put[t->index] = time;
            time = time + t->p;
        }else{
            out_put[t->index] = t->r;
            time = t->r + t->p;
        }
    }
    
    
    for(auto i : out_put){
        output_file << i << endl;
    }

    output_file.close();        
}


// Driver Code
int main(int argc, const char * argv[])
{
    UB = MAX_INTEGER;
    perms = 0;
    loadData(argv[1]);
	int n = tasks.size();
	permute(0, n-1);

    write_solution(argv[2]);
	return 0;
}

// This is code is contributed by rathbhupendra
