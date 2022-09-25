#include "BigInt.hpp"
#include <gtest/gtest.h>

TEST(TESTS, eq) {
    for(int i = -1000000; i<=1000000; i++) {
        EXPECT_EQ(i, int(BigInt(i)));
    }
    for(int i = -100; i<=100; i++) {
        for(int j = -100; j<=100; j++) {
            EXPECT_EQ(i< j, BigInt(i)< BigInt(j));
            EXPECT_EQ(i<=j, BigInt(i)<=BigInt(j));
            EXPECT_EQ(i> j, BigInt(i)> BigInt(j));
            EXPECT_EQ(i>=j, BigInt(i)>=BigInt(j));
            EXPECT_EQ(i==j, BigInt(i)==BigInt(j));
            EXPECT_EQ(i!=j, BigInt(i)!=BigInt(j));
        }
    }
}

TEST(TESTS, plus_minus) {
    for(int i = -100; i<=100; i++) {
        for(int j = -10; j<=10; j++) {
            EXPECT_EQ(BigInt(i+j),BigInt(i)+BigInt(j));
            //EXPECT_EQ(i-j,int(BigInt(i)-BigInt(j)));
        }
    }
}

TEST(TESTS, mul_div) {
    BigInt x = 10, x1 = -10;
    BigInt y = 20, y1 = -20;
    EXPECT_EQ(x*y,y*x);
    EXPECT_EQ(-x*y,x*(-y));
    EXPECT_EQ(x1*y,x*y1);
    EXPECT_EQ(BigInt(x*y), BigInt(200));
    for(int i = -10; i<=10; i++) {
        for(int j = -10; j<=10; j++) {
            EXPECT_EQ(i*j, int(BigInt(i)*BigInt(j)));
        }
    }
    for(int i = -10; i<=10; i++) {
        for(int j = 1; j<=10; j++) {
            EXPECT_EQ(i/j, int(BigInt(i)/BigInt(j)));
            EXPECT_EQ(i%j, int(BigInt(i)%BigInt(j)));
        }
    }
}

int main(int argc, char** argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}