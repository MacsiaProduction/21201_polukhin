#include <utility>
template <class T>
class smart_ptr{
public: // std::shared_ptr
  smart_ptr(T * p = nullptr) : ptr(p) {
    counter = new int(1);
  }

  smart_ptr(const smart_ptr& other) {
    ptr = other.ptr;
    counter = other.counter;
    (*counter)++;
  }

  smart_ptr(smart_ptr && other) {
    ptr = std::move(other.ptr);
    other.ptr = nullptr;
    counter = std::move(other.counter);
    other.counter = new int(1);
  }

  smart_ptr& operator=(const smart_ptr& other) {
    if (ptr == other.ptr) return *this;
    ~smart_ptr();
    ptr = other.ptr;
    counter = other.counter;
    (*counter)++;
    return *this;
  }

  smart_ptr& operator=(smart_ptr&& other) {
    if (ptr == other.ptr) return *this;
    ~smart_ptr();
    ptr = std::move(other.ptr);
    other.ptr = nullptr;
    counter = std::move(other.counter);
    other.counter = new int(1);
    (*counter)++;
    return *this;
  }

  T* get() { return ptr; }

  void reset(T* new_ptr) {
    if (ptr == new_ptr) return;
    ~smart_ptr();
    //smart_ptr(new_ptr);
    ptr = new_ptr;
    counter = new int(1);
  }

  T* release() {
    T* tmp = ptr;
    (*counter)--;
    ptr = nullptr;
    counter = new int(1);
    return tmp;
  }

  T operator->() { return &ptr; }

  T operator*() { return *ptr; }

  ~smart_ptr() {
    if (*counter != 1) (*counter)--;
    else {
      delete counter;
      delete ptr;
    }
  }
  private: 
    T* ptr;
    int* counter;
};