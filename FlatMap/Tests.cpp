#include <gtest/gtest.h>
#include "FlatMap.hpp"
class key{
    public:
    int a = 0;
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
    EXPECT_EQ(x[key(1)], 1);
    EXPECT_EQ(x.at(key(1)), 1);
    EXPECT_TRUE(x.contains(key(1)));
    EXPECT_FALSE(x.empty());
    EXPECT_EQ(x.size(),9999);
};

TEST(index1, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        EXPECT_TRUE(a.insert(key(i),i));
    }
    f1(a);
    FlatMap <key, int> b = a;
    f1(b);
    FlatMap <key, int> c;
    c = a;
    f1(c);
    EXPECT_TRUE(a==c);
    a.insert(key(200000), 200001);
    EXPECT_TRUE(a!=c);
    a.erase(key(200000));
    EXPECT_TRUE(a==c);
    a.clear();
    EXPECT_EQ(a.size(),0);
    EXPECT_TRUE(a!=c);    
}

TEST(index2, first) {
    FlatMap <key, int> a;
    a.insert(key(10),10);
    //already
    for(int i = 11; i<= 100; i++) {
       EXPECT_FALSE(a.erase(key(i))); 
    }
    EXPECT_FALSE(a.empty());
    EXPECT_EQ(a.size(),1);
    a.erase(key(10));
    EXPECT_TRUE(a.empty());
    EXPECT_EQ(a.size(),0);
}

TEST(index3, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        EXPECT_TRUE(a.insert(key(i),i));
        EXPECT_FALSE(a.insert(key(i),i));
        EXPECT_EQ(a.size(),i);
    }
    for(int i=1;i<10000;i++) {
        EXPECT_EQ(a.size(),10000-i);
        EXPECT_TRUE(a.erase(key(i)));
        EXPECT_FALSE(a.erase(key(i)));
    }
}

TEST(adressing, first) {
    FlatMap <key, int> a;
    for(int i=1;i<10000;i++) {
        EXPECT_TRUE(a.insert(key(i),i));
        EXPECT_FALSE(a.insert(key(i),i));
        EXPECT_EQ(a[key(i)], a.at(key(i)));
        EXPECT_EQ(a.size(),i);
    }
    const FlatMap <key, int> b = a;
    a.clear();
    for(int i=1;i<10000;i++) {
        EXPECT_EQ(i, b.at(key(i)));
    }
    //a.clear();
    EXPECT_EQ(a.size(),0);
    for(int i=10000;i>=1;i--) {
        EXPECT_TRUE(a.insert(key(i),i));
        EXPECT_FALSE(a.insert(key(i),i));
    }
    EXPECT_EQ(a.size(),10000);
    a[key(-1)];
    EXPECT_EQ(a.size(),10001);
    a.clear();
    EXPECT_EQ(a.size(),0);
}

TEST(move, first) {
    FlatMap <key, int> a;
    EXPECT_EQ(a.size(), 0);
    for(int i = -100; i<=100; i++) {
        int kek = a.insert(key(i), i);
        EXPECT_TRUE(kek);
    }
    for(int i = -100; i<=100; i++) {
        EXPECT_TRUE(a.contains(key(i)));
    }
    FlatMap <key, int> b;
    for(int i = -1000; i>=-2000; i--) {
        EXPECT_TRUE(b.insert(key(i), i));
    }
    for(int i = -1000; i>=-2000; i--) {
        EXPECT_TRUE(b.contains(key(i)));
    }
    a.swap(b);
    for(int i = -100; i<=100; i++) {
        EXPECT_TRUE(b.contains(key(i)));
    }
    for(int i = -1000; i>=-2000; i--) {
        EXPECT_TRUE(a.contains(key(i)));
    }
    b.swap(a);
    for(int i = -100; i<=100; i++) {
        EXPECT_TRUE(a.contains(key(i)));
    }
    for(int i = -1000; i>=-2000; i--) {
        EXPECT_TRUE(b.contains(key(i)));
    }
    b.clear();
    EXPECT_TRUE(b.size() == 0);
    FlatMap <key, int> c = std::move(a);
    for(int i = -100; i<=100; i++) {
        EXPECT_TRUE(c.contains(key(i)));
    }
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}