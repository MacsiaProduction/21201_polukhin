#ifndef CODE_HPP
#define CODE_HPP
#endif // CODE_HPP

#include <iostream>
#include <bitset>
#include <cmath>
#include <ctype.h>
#include <string>

#define MAXLEN_BIGINT 64

class BigInt {
public:
    BigInt(){
        sign = 0;
        bin[0] = 0;
    };
    BigInt(int in) {
        sign = in<0;
        for (int i = 0; in != 0; i++) {
            bin[bin.size() - 1 - i] = in%2;
            in/=2;
        }
    };
    BigInt(std::string in) {
        int start = 0;
        if(in[0] == '-') start = 1;
        if (in[start] == '0') throw std::invalid_argument ("zero starting number!");
        for(int i = start; i < in.length(); i++) {
            if(!isdigit(in[i])) throw std::invalid_argument ("not a digit!");
            else {
                *this = *this*BigInt(10) + BigInt(in[i]-'0');
            }
        }
        if(in[0] == '-') sign = 1;
    }; // бросает исключение std::invalid_argument при ошибке
    BigInt(const BigInt& in) {
        sign = in.sign;
        for(int i = 0; i < bin.size(); i++) {
            bin[i] = in.bin[i];
        }
    };
    ~BigInt() {
        //default destructor
    };
    BigInt& operator=(const BigInt& in){
        sign = in.sign;
        for(int i = 0; i < bin.size(); i++) {
            bin[i] = in.bin[i];
        }
        return *this;
    }; //возможно присваивание самому себе!

    BigInt operator~() const {
        BigInt ans;
        ans.bin = ~(*this).bin;
        return ans;
    };
    BigInt& operator++() {
        (*this) += BigInt(1);
        return *this;
    };
    const BigInt operator++(int){
        BigInt ans = *this + BigInt(1);
        return ans;
    };
    BigInt& operator--() {
        (*this) -= BigInt(1);
        return *this;
    };
    const BigInt operator--(int){
        BigInt ans = *this - BigInt(1);
        return ans;
    };

    BigInt& operator+=(const BigInt& in) {
        if (sign != in.sign) {
            if(sign) {
                BigInt tmp = *this;
                tmp.sign = !tmp.sign;
                *this = in - tmp;
            } else {
                BigInt tmp = in;
                tmp.sign = !tmp.sign;
                *this -= tmp;
            }
            return *this;
        }
        BigInt ans;
        ans.sign = sign;
        bool overflow = false;
        for(int i = bin.size()-1; i >= 0; i--) {
            if( bin[i] & in.bin[i] & overflow) {ans.bin[i] = 1; overflow = 1;}
            else if(!bin[i] &!in.bin[i] & overflow) {ans.bin[i] = 1; overflow = 0;}
            else if( bin[i] &!in.bin[i] & overflow) {ans.bin[i] = 0; overflow = 1;}
            else if(!bin[i] & in.bin[i] & overflow) {ans.bin[i] = 0; overflow = 1;}
            else if( bin[i] & in.bin[i] &!overflow) {ans.bin[i] = 0; overflow = 1;}
            else if( bin[i] &!in.bin[i] &!overflow) {ans.bin[i] = 1; overflow = 0;}
            else if(!bin[i] &!in.bin[i] &!overflow) {ans.bin[i] = 0; overflow = 0;}
            else if(!bin[i] & in.bin[i] &!overflow) {ans.bin[i] = 1; overflow = 0;}
        }
        *this = ans;
        return *this;
    };
    BigInt& operator*=(const BigInt& in) {
        BigInt ans = 0;
        for(int i = bin.size() - 1; i >= 0; i--) {
            if (in.bin[i]) {
                BigInt tmp = *this;
                tmp.bin = tmp.bin >> (bin.size() - 1 - i);
                ans += tmp;
            }
        }
        ans.sign = in.sign ^ (*this).sign;
        (*this) = ans;
        return *this; 
    };
    BigInt& operator-=(const BigInt& in) {
        if (sign != in.sign) {
            BigInt tmp = in;
            tmp.sign = !tmp.sign;
            *this += tmp;
            return *this;
        }
        if (*this < in) return (*this = -(in - *this));
        BigInt ans;
        ans.sign = sign;
        bool overflow = false;
        for(int i = bin.size()-1; i >= 0; i--) {
            if( bin[i] & in.bin[i] & overflow) {ans.bin[i] = 1; overflow = 1;}
            else if(!bin[i] &!in.bin[i] & overflow) {ans.bin[i] = 1; overflow = 1;}
            else if( bin[i] &!in.bin[i] & overflow) {ans.bin[i] = 0; overflow = 0;}
            else if(!bin[i] & in.bin[i] & overflow) {ans.bin[i] = 0; overflow = 1;}
            else if( bin[i] & in.bin[i] &!overflow) {ans.bin[i] = 0; overflow = 0;}
            else if( bin[i] &!in.bin[i] &!overflow) {ans.bin[i] = 1; overflow = 0;}
            else if(!bin[i] &!in.bin[i] &!overflow) {ans.bin[i] = 0; overflow = 0;}
            else if(!bin[i] & in.bin[i] &!overflow) {ans.bin[i] = 1; overflow = 1;}
        }
        *this = ans;
        return *this;
    };
    BigInt& operator/=(const BigInt& in) {
        BigInt ans = 0;
        if (sign == in.sign) {
            while (ans*in <= *this) ++ans;
            return *this = ans--;
        } else {
            while (ans*in >= *this) --ans;
            return *this = ans++;
        }
    }; //yeaaaa great speed, i know)
    BigInt& operator^=(const BigInt& in) {
        this->sign = sign ^ in.sign;
        this->bin = this->bin ^ in.bin;
        return *this;
    };
    BigInt& operator%=(const BigInt& in) {
        BigInt ans = *this / in;
        *this = *this - (ans * in);
        return *this;
    };
    BigInt& operator&=(const BigInt& in) {
        this->sign = sign & in.sign;
        this->bin = this->bin & in.bin;
        return *this;
    };
    BigInt& operator|=(const BigInt& in) {
        this->sign = sign | in.sign;
        this->bin = this->bin | in.bin;
        return *this;
    };

    BigInt operator+() const {
        BigInt ans = *this;
        ans.sign = 0;
        return ans;
    };  // unary +
    BigInt operator-() const {
        BigInt ans = *this;
        ans.sign = 1;
        return ans;
    };  // unary -

    bool operator==(const BigInt& y) const {
        return (sign == y.sign && bin == y.bin);
    };
    bool operator!=(const BigInt& y) const {
        return !(*this == y);
    };
    bool operator< (const BigInt& y) const {
        if ((*this) == y) return false;
        if (sign != y.sign) return sign;
        if (sign) {
            for (int i = 0; i < bin.size(); i++) {
            if (bin[i] != y.bin[i]) return !y.bin[i];
        }
        return true;
        } else {
            for (int i = 0; i < bin.size(); i++) {
                if (bin[i] != y.bin[i]) return y.bin[i];
            }
            return false;
        }
    }; 
    bool operator> (const BigInt& x) const {
        return !(*this == x || *this < x);
    };
    bool operator<=(const BigInt& x) const {
        return !(*this > x);
    };
    bool operator>=(const BigInt& x) const {
        return !(*this < x);
    };

    operator int() const {
        int answer = 0;
        for(int i = bin.size()-1; i>=bin.size() - 31; i--) {
            answer|=bin[i]<<(31-i);
        }
        if (sign) return -answer;
        else return answer;
    };
    operator std::string() const {
        std::string ans = "";
        BigInt tmp = *this;
        tmp.sign = 0;
        while (tmp!=BigInt(0)) {
            char kek = int(tmp % BigInt(10))+'0';
            tmp/=BigInt(10);
            ans.push_back(kek);
        }
        if(sign) ans.push_back('-');
        int n = ans.length();
        for (int i = 0; i < n / 2; i++) std::swap(ans[i], ans[n - i - 1]);
        return ans;
    };
    size_t size() const {
        return sizeof(*this);
    };  // size in bytes
private:
    std::bitset <MAXLEN_BIGINT> bin;
    bool sign = 0; //0 - plus, 1 - minus
};

BigInt operator+(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans += y;
    return ans;
};
BigInt operator-(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans -= y;
    return ans;
};
BigInt operator*(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans *= y;
    return ans;
};
BigInt operator/(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans /= y;
    return ans;
};
BigInt operator^(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans ^= y;
    return ans;
};
BigInt operator%(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans %= y;
    return ans;
};
BigInt operator&(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans &= y;
    return ans;
};
BigInt operator|(const BigInt& x, const BigInt& y) {
    BigInt ans = x;
    ans |= y;
    return ans;
};

std::ostream& operator<<(std::ostream& o, const BigInt& i) {
    return o<<std::string(i);
};
