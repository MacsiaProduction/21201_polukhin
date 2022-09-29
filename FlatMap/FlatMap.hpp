#ifndef FlatMap_HPP
#define FlatMap_HPP
#endif
#include "../res/mine_vector.hpp"
#include <utility>

template <class Key, class Value>
//Associative container that allows you to store key-value pairs
class FlatMap {
private:
  mine_vector < std::pair<Key, Value> > arr;
  //Returns potential pos of key in arr
  unsigned int find_pos_of_key(const Key& key) const{
    if (empty()) return 0;
    int start = 0, end = arr.size();
    for(int m = (start + end)/2; end-start > 1; m = (start + end)/2) {
        if ((arr[m].first < key)||(arr[m].first == key)) {
            start = m;
        } else end = m;
    }
    return (arr[start].first<key)?start+1:start; 
  }
public:
  FlatMap() = default;
  ~FlatMap() = default;
  //Copy constructor
  FlatMap(const FlatMap& b) {
    arr.resize(b.arr.size());
    for(int i = 0; i < b.arr.size(); i++){
      arr[i] = b.arr[i];
    }
  };
  //Move constructor
  FlatMap(FlatMap&& b) {
    arr = std::move(b.arr);
  };
  //Swap two FlatMaps containers
  void swap(FlatMap& b) {
    FlatMap tmp = std::move(*this);
    *this = std::move(b);   
    b = std::move(tmp); 
  };    
  //Copy operator
  FlatMap& operator=(const FlatMap& b) {
    arr.clear();
    arr.resize(b.arr.size());
    for(int i = 0; i < b.arr.size(); i++){
      arr[i] = b.arr[i];
    }
    return *this;
  };
  //Move operator
  FlatMap& operator=(FlatMap&& b) {
    arr = std::move(b.arr);
    return *this;
  };
  //Clears the container
  void clear() {
    arr.clear();
  };
  //Removes a key-value pair with the given key
  bool erase(const Key& k) {
    if(empty()) return 0;
    int pos = find_pos_of_key(k);
    if (arr[pos].first == k) {
        arr.erase(pos);
        return 1;   
    } else return 0;
  };
  //Insertion into container. The return value is the success of the insertion.
  bool insert(const Key& k, const Value& v) {
    const int pos = find_pos_of_key(k);
    if(!empty() && (pos != size()) && arr[pos].second == v) return 0;
    std::pair <Key, Value> tmp = std::make_pair(k, v);
    arr.insert(pos, tmp);
    return 1;
  };
  //Checking if a value exists for a given key.
  bool contains(const Key& k) const {
    if(empty()) return 0;
    int pos = find_pos_of_key(k);
    return arr[pos].first == k;  
  };
  //Returns value by key. Unsafe method.
  Value& operator[](const Key& k) {
    int pos = find_pos_of_key(k);
    if (arr[pos].first != k) {
      Value v;
      insert(k,v);
    }
    return arr[pos].second;
  };
  //Returns value by key. Returns exception on failure
  Value& at(const Key& k) {
    int pos = find_pos_of_key(k);
    if (arr[pos].first != k) throw std::invalid_argument("Key wasn't found"); 
    return arr[pos].second;
  };
  //Returns const value by key. Returns exception on failure
  const Value& at(const Key& k) const {
    return at(k);
  }
  //Returns number of pairs in container
  size_t size() const {
    return arr.size();
  };
  //Returns if the container is empty
  bool empty() const {
    return arr.empty();
  };
  //Returns if the containers content the same elements
  friend bool operator==(const FlatMap& a, const FlatMap& b) {
    return a.arr == b.arr;
  };
  //Returns if the containers do NOT content the same elements
  friend bool operator!=(const FlatMap& a, const FlatMap& b) {
    return !(a==b);
  };
};