#include <gtest/gtest.h>
#include "../FlatMap.hpp"
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
FlatMap <key, int> a;

void f1 (FlatMap <key,int> x) {
    ASSERT_EQ(x[key(1)], 10);
    ASSERT_EQ(x.at(key(1)), 10);
    ASSERT_TRUE(x.contains(key(1)));
    ASSERT_FALSE(x.empty());
    ASSERT_EQ(x.size(),1);
};

TEST(index1, first) {
    FlatMap <key, int> a;
    a.insert(key(1), 10);
    f1(a);
    FlatMap <key, int> b = a;
    f1(b);
    FlatMap <key, int> c;
    c = a;
    f1(c);
}

TEST(index2, first) {
    FlatMap <key, int> a;
    a.insert(key(10),10);
    //already
    for(int i = 11; i<= 100; i++) {
       ASSERT_FALSE(a.erase(key(i))); 
    }
    ASSERT_FALSE(a.empty());
    ASSERT_EQ(a.size(),1);
    a.erase(key(10));
    ASSERT_TRUE(a.empty());
    ASSERT_EQ(a.size(),0);
}



int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}