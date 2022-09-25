#ifndef FlatMap_HPP
#define FlatMap_HPP
#endif
#include "mine_vector.hpp"
#include <iostream>
#include <vector>
#include <algorithm>
#include <utility>
#include <string>

template <class Key, class Value> 
class FlatMap // ассоциативный контейнер, позволяющий хранить пары ключ-значение
{
private:
  mine_vector < std::pair<Key, Value> > arr;
  // returns potential pos of key in arr
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
  //copy
  FlatMap(const FlatMap& b) {
    arr.resize(b.arr.size());
    for(int i = 0; i < b.arr.size(); i++){
      arr[i] = b.arr[i];
    }
  };
  //move
  FlatMap(FlatMap&& b) {
    arr = std::move(b.arr);
  };
  // Обменивает значения двух флетмап.
  void swap(FlatMap& b) {
    FlatMap tmp = std::move(*this);
    *this = std::move(b);   
    b = std::move(tmp); 
  };    
  //copy
  FlatMap& operator=(const FlatMap& b) {
    arr.clear();
    arr.resize(b.arr.size());
    for(int i = 0; i < b.arr.size(); i++){
      arr[i] = b.arr[i];
    }
    return *this;
  };
  //move
  FlatMap& operator=(FlatMap&& b) {
    arr = std::move(b.arr);
    return *this;
  };
  // Очищает контейнер.
  void clear() {
    arr.clear();
  };
  // Удаляет элемент по заданному ключу.
  bool erase(const Key& k) {
    if(empty()) return 0;
    int pos = find_pos_of_key(k);
    if (arr[pos].first == k) {
        arr.erase(pos);
        return 1;   
    } else return 0;
  };
  // Вставка в контейнер. Возвращаемое значение - успешность вставки.
  bool insert(const Key& k, const Value& v) {
    const int pos = find_pos_of_key(k);
    if(!empty() && (pos != size()) && arr[pos].second == v) return 0;
    std::pair <Key, Value> tmp = std::make_pair(k, v);
    arr.insert(pos, tmp);
    return 1;
  };
  // Проверка наличия значения по заданному ключу.
  bool contains(const Key& k) const {
    if(empty()) return 0;
    int pos = find_pos_of_key(k);
    return arr[pos].first == k;  
  };
  // Возвращает значение по ключу. Небезопасный метод.
  // В случае отсутствия ключа в контейнере, следует вставить в контейнер
  // значение, созданное конструктором по умолчанию и вернуть ссылку на него. 
  Value& operator[](const Key& k) {
    int pos = find_pos_of_key(k);
    if (arr[pos].first != k) {
      Value v;
      insert(k,v);
    }
    return arr[pos].second;
  };
  // Возвращает значение по ключу. Бросает исключение при неудаче.
  Value& at(const Key& k) {
    int pos = find_pos_of_key(k);
    if (arr[pos].first != k) throw std::invalid_argument("Key wasn't found"); 
    return arr[pos].second;
  };
  const Value& at(const Key& k) const {
    return at(k);
  }
  size_t size() const {
    return arr.size();
  };
  bool empty() const {
    return arr.empty();
  };
  friend bool operator==(const FlatMap& a, const FlatMap& b) {
    return a.arr == b.arr;
  };
  friend bool operator!=(const FlatMap& a, const FlatMap& b) {
    return !(a==b);
  };
};