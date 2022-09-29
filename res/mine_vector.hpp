#ifndef mine_vector_HPP
#define mine_vector_HPP
#endif
#include <iostream>
#include <vector>
#include <algorithm>
#include <utility>
template <class K>
class mine_vector {
  private:
	K* arr;
	int len;
  public:
	mine_vector() {
		arr = new K[1];
		len = 0;
	}
	mine_vector(const mine_vector<K>& b) {
		mine_vector();
		resize(b.len);
		for(int i = 0; i<len; i++) {
			arr[i] = b.arr[i];
		}
	}
	mine_vector& operator=(mine_vector&& old) {
		resize(old.len);
		for(int i = 0; i<len; i++) {
			arr[i] = std::move(old[i]);
		}
		return *this;
	}
	K& operator[](int index) const{
		return arr[index];
	}
	bool empty() const{
		return (len == 0);
	}
	int size() const{
		return len;
	}
	void resize(unsigned int new_size) {
		if (len == new_size) return;
		K* tmp = new K[new_size+1];
		for(int i = 0; i < std::min(int(new_size), len); i++) {
			tmp[i] = std::move(arr[i]);
		}
		delete[] arr;
		arr = tmp;
		len = new_size;
	}
	void push_back(K to_add) {
		insert(len-1,to_add);
	};
	void clear() {
		resize(0);
	};
	void erase(int index) {
		for(int i = index; i<len-1; i++){
			arr[i]=std::move(arr[i+1]);
		}
		resize(len-1);
	};
	void insert(int index, K value) {
		resize(len+1);
		for(int i = len-1; i>index; i--) {
			arr[i] = std::move(arr[i-1]);
		}
		arr[index] = value;
	};
  	friend bool operator==(const mine_vector& a, const mine_vector& b) {
		if (a.len != b.len) return 0;
		for(int i = 0; i<a.len; i++) {
			if (a[i]!=b[i]) return 0;
		}
		return 1;
	}
};
