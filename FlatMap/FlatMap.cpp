
#include "FlatMap.hpp"

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
    mine_vector<int> a = 0;
    return 0;
}