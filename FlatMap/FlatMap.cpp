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
    return 0;
}