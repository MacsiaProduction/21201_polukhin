#include "../Interpreter.cpp"
#include <vector>
#include <gtest/gtest.h>
void check(std::string input, std::string expected_out)
{
    Interpreter interpreter;
    std::stringstream input_flow(input);
    interpreter.process_text(input_flow);
    std::string out = mine_out::flush_buffer();
    EXPECT_EQ(expected_out, out);
}

void check_exception(std::string input)
{
    Interpreter interpreter;
    std::stringstream input_flow(input);
    EXPECT_ANY_THROW(interpreter.process_text(input_flow));
    mine_out::flush_buffer();
}

TEST(int_and_string, first)
{
    for (long long int i = INT32_MIN; i < INT32_MIN + 100000; i++)
    {
        // check(std::to_string(i) + " .", std::to_string(i) + " ");
    }
    std::vector<std::string> strings = {"keks", "avbdsfa", "fasdfgfdsg", "dagfgsd", "fasdfat3 wrfgasf", "qaafsd", "afdsc", "fasdg dfsag sdfg dsgas f", "s", "", "fafe", "sdf  sdf"};
    for (auto &x : strings)
    {
        check(".\" " + x + "\"", x + " ");
    }
    check_exception(".\" keks");
    check_exception("finally");
    check_exception("i");
};

// created by using actual forth compiler :)
TEST(default_funcs, first)
{
    std::string in = "1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10 + . * .  / . - .  mod . dup . drop . swap . rot . over . emit . cr  1 0 < . 0 1 < . 0 1 > . 1 0 > . 1 0 = . 0 1 = . 1 1 = .";
    std::string expected_out = "19 56 0 -1 1 10 9 7 5 6 6 0 1 0 1 0 0 1 ";
    check(in, expected_out);
    check_exception("1 0 /");
    check_exception(".");
}

TEST(if, first)
{
    check("3 if 1 . else 2 . then ;", "1 ");
    check("0 if 1 . else 2 . then ;", "2 ");
    check("0 if 1 . then ; 2 .", "2 ");
    check("10 if 1 . then ; 3 .", "1 3 ");
    check("0 if 1 . else 2 . then ;", "2 ");
    check_exception("0 if 1 else 2 then");
    check_exception("0 if 1 else 2");
}

TEST(loop, first)
{
    check("1 10 0 do i . loop ; .", "0 1 2 3 4 5 6 7 8 9 1 ");
    check("10 0 1 do 1 . loop ; .", "10 ");
    check("0 3 1 2 1 1 1 3 1 do do i + loop ; loop ; .", "3 ");
    check("3 1 3 1 3 1 3 1 3 1 3 1 do do 1 . loop ; loop ;", "1 1 1 1 ");
    check(": foo 0 do 3 1 loop ;\n 32 foo \n do do do 1 . loop ; loop ; loop ;", "1 1 1 1 1 1 1 1 ");
    check(": foo 0 do 3 1 loop ;\n 128 foo \n do do do do do do 1 . loop ; loop ; loop ; loop ; loop ; loop ; ", "1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 ");
    check_exception("10 0 1 do 1 . loop .");
    check_exception("10 0 1 do 1 .");
}

TEST(mine_funcs, first)
{
    check(": foo 0 do .\" Foo\" loop ;\n 3 foo 5 foo", "Foo Foo Foo Foo Foo Foo Foo Foo ");
    check(": foo 0 do 1 + loop ;\n 0 1024 foo .", "1024 ");
}

TEST(vars, first)
{
    check("variable kek kek @ . 100 kek ! kek @ .", "0 100 ");
    check("variable k1 1 k1 ! variable k2 2 k2 ! k1 @ . variable k3 3 k3 ! k1 @ . variable k4 4 k4 ! k1 @ .",
          "1 1 1 ");
    check("variable k1 1 k1 ! variable k2 2 k2 ! k1 @ . k2 @ . variable k3 3 k3 ! k1 @ . k2 @ . k3 @ . variable k4  4 k4 ! k1 @ .", "1 2 1 2 3 1 ");
}

TEST(file, last)
{
    std::ofstream tmp("tmp.txt");
    tmp << "3 if 1 . else 2 . then ;";
    tmp.close();
    std::ifstream tmp_read_only("tmp.txt");
    Interpreter interpreter;
    interpreter.process_file(tmp_read_only);
    std::string expected_out = "1 ";
    std::string out = mine_out::flush_buffer();
    EXPECT_EQ(expected_out, out);
    remove("tmp.txt");
}

int main(int argc, char **argv)
{
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}