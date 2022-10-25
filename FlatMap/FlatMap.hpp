#ifndef FlatMap_HPP
#define FlatMap_HPP

#include "../res/mine_vector.hpp"

//Associative container that allows you to store key-value pairs
template <class Key, class Value>
class FlatMap {
public:
  FlatMap() = default;
  ~FlatMap() = default;
  //Copy constructor
  FlatMap(const FlatMap& b) : arr(b.arr) {};
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
    if(*this == b) return *this;
    arr = b.arr;
    return *this;
  };
  //Move operator
  FlatMap& operator=(FlatMap&& b) {
    if(*this == b) return *this;
    arr = std::move(b.arr);
    return *this;
  };
  //Clears the container
  void clear() {
    arr.clear();
  };
  //Removes a key-value pair with the given key
  bool erase(const Key& k) {
    size_t pos = find_pos_of_key(k);
    if (pos <= size() && arr[pos].first == k) {
      arr.erase(pos);
      return true;   
    }
    return false;
  };
  //Insertion into container. The return value is the success of the insertion.
  bool insert(const Key& k, const Value& v) {
    const size_t pos = find_pos_of_key(k);
    if(!empty() && (pos != size()) && arr[pos].second == v) return 0;
    auto tmp = std::make_pair(k, v);
    arr.insert(pos, tmp);
    return 1;
  };
  //Checks if a value exists for a given key.
  bool contains(const Key& k) const {
    if(empty()) return 0;
    size_t pos = find_pos_of_key(k);
    return arr[pos].first == k;  
  };
  //Returns value by key. Unsafe method.
  Value& operator[](const Key& k) {
    size_t pos = find_pos_of_key(k);
    if (arr[pos].first != k) {
      insert(k, Value());
    }
    return arr[pos].second;
  };
  //Returns value reference by key. Returns exception on failure
  Value& at(const Key& k) {
    size_t pos = find_pos_of_key(k);
    if (arr[pos].first != k) throw std::invalid_argument("Key wasn't found"); 
    return arr[pos].second;
  };
  //Returns const value reference by key. Returns exception on failure
  const Value& at(const Key& k) const {
    size_t pos = find_pos_of_key(k);
    if (pos == size() || arr[pos].first != k) throw std::invalid_argument("Key wasn't found"); 
    return arr[pos].second;
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
private:
  mine_vector < std::pair<Key, Value> > arr;
  //Returns potential pos of key in arr
  size_t find_pos_of_key(const Key& key) const{
    if (empty()) return 0;
    size_t start = 0, end = size();
    for(size_t m = start/2 + end/2; end-start > 1;\
    m = start/2 + end/2 + (((start%2)*(end%2)==1)?1:0))
    {
      if ((arr[m].first < key)||(arr[m].first == key)) {
        start = m;
      } else end = m;
    }
    return (arr[start].first<key)?start+1:start; 
  }
};
#endif