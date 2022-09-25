#include "FlatMap.hpp"
#include <iostream>
using namespace std;
class key{
    public:
    int a;
    key() {a=0;}
    key(int b){a=b;}
    friend bool operator<(key a, key b) {
        return a.a < b.a;
    }
    friend bool operator==(key a, key b) {
        return a.a == b.a;
    }
    friend bool operator!=(key a, key b) {
        return !(a.a == b.a);
    }
};
using namespace std;
int main() {
    FlatMap <key, int> a;
    a.insert(key(10), 20);
    a.insert(key(11), 21);
    a.insert(key(13), 23);
    a.insert(key(12), 22);
    cout<<a;
    FlatMap <key, int> b = a;
    a.insert(key(2),17);
    cout<<a;
    cout<<b;
    b = a;
    b.insert(key(3),18);
    cout<<a;
    cout<<b;
    a.swap(b);
    cout<<a;
    cout<<b;
    cout<<a[key(1)]<<endl;
    cout<<a;
    b.clear();
    cout<<b;
    cout<<a.at(2)<<endl;
    //cout<<a.at(4)<<endl;
    cout<<a;
    a.erase(3);
    cout<<a;
    return 0;
}