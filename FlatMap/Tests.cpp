#include <gtest/gtest.h>
#include "FlatMap.hpp"
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

inline void f1 (FlatMap <key,int> x) {
    ASSERT_EQ(x[key(1)], 1);
    ASSERT_EQ(x.at(key(1)), 1);
    ASSERT_TRUE(x.contains(key(1)));
    ASSERT_FALSE(x.empty());
    ASSERT_EQ(x.size(),9999);
};

TEST(index1, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        ASSERT_TRUE(a.insert(key(i),i));
    }
    f1(a);
    FlatMap <key, int> b = a;
    f1(b);
    FlatMap <key, int> c;
    c = a;
    f1(c);
    ASSERT_TRUE(a==c);
    a.insert(key(200000), 200001);
    ASSERT_TRUE(a!=c);
    a.erase(key(200000));
    ASSERT_TRUE(a==c);
    a.clear();
    ASSERT_EQ(a.size(),0);
    ASSERT_TRUE(a!=c);    
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

TEST(index3, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        ASSERT_TRUE(a.insert(key(i),i));
        ASSERT_FALSE(a.insert(key(i),i));
        ASSERT_EQ(a.size(),i);
    }
    for(int i=9999;i<=1;i--) {
        ASSERT_EQ(a.size(),i);
        ASSERT_TRUE(a.erase(key(i)));
        ASSERT_FALSE(a.erase(key(i)));
    }
}

TEST(adressing, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        ASSERT_TRUE(a.insert(key(i),i));
        ASSERT_FALSE(a.insert(key(i),i));
        ASSERT_EQ(a[key(i)], a.at(key(i)));
        const int x = a.at(key(i));
        ASSERT_EQ(x, a.at(key(i)));
        ASSERT_EQ(a.at(key(i)), i);
        ASSERT_EQ(a.size(),i);
    }
    a.clear();
    ASSERT_EQ(a.size(),0);
    for(int i=10000;i>=1;i--) {
        ASSERT_TRUE(a.insert(key(i),i));
        ASSERT_FALSE(a.insert(key(i),i));
    }
    ASSERT_EQ(a.size(),10000);
    a[key(-1)];
    ASSERT_EQ(a.size(),10001);
    a.clear();
    ASSERT_EQ(a.size(),0);
}

TEST(move, first) {
    FlatMap <key, int> a;
    ASSERT_EQ(a.size(), 0);
    for(int i = -100; i<=100; i++) {
        int kek = a.insert(key(i), i);
        ASSERT_TRUE(kek);
    }
    for(int i = -100; i<=100; i++) {
        ASSERT_TRUE(a.contains(key(i)));
    }
    FlatMap <key, int> b;
    for(int i = -1000; i>=-2000; i--) {
        ASSERT_TRUE(b.insert(key(i), i));
    }
    for(int i = -1000; i>=-2000; i--) {
        ASSERT_TRUE(b.contains(key(i)));
    }
    a.swap(b);
    for(int i = -100; i<=100; i++) {
        ASSERT_TRUE(b.contains(key(i)));
    }
    for(int i = -1000; i>=-2000; i--) {
        ASSERT_TRUE(a.contains(key(i)));
    }
    b.swap(a);
    for(int i = -100; i<=100; i++) {
        ASSERT_TRUE(a.contains(key(i)));
    }
    for(int i = -1000; i>=-2000; i--) {
        ASSERT_TRUE(b.contains(key(i)));
    }
    b.clear();
    ASSERT_TRUE(b.size() == 0);
    FlatMap <key, int> c = std::move(a);
    for(int i = -100; i<=100; i++) {
        ASSERT_TRUE(c.contains(key(i)));
    }
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}