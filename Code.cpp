#include "Code.hpp"

int main() {
    std::string a = "-1000000000";
    BigInt x = a;
    BigInt y = 20;
    BigInt ans = x*y;
    for(int i = -100; i<=100; i++) {
        for(int j = -100; j<=100; j++) {
            if ((i<j) != (BigInt(i)<BigInt(j))) 
                std::cout<<i<<" "<<j<<" "<<(BigInt(i)<BigInt(j))<<std::endl;
        }
    }
    std::cout<<"SUCCESS"<<std::endl;
    return 0;
}