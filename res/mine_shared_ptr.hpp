#include <utility>

template <class T>
class mine_shared_ptr{
public:
  mine_shared_ptr(T * p = nullptr) : ptr(p) {
    counter = new int(1);
  }

  mine_shared_ptr(const mine_shared_ptr& other) {
    ptr = other.ptr;
    counter = other.counter;
    (*counter)++;
  }

  mine_shared_ptr(mine_shared_ptr && other) {
    ptr = std::move(other.ptr);
    other.ptr = nullptr;
    counter = std::move(other.counter);
    other.counter = new int(1);
  }

  mine_shared_ptr& operator=(const mine_shared_ptr& other) {
    if (ptr == other.ptr) return *this;
    ~mine_shared_ptr();
    ptr = other.ptr;
    counter = other.counter;
    (*counter)++;
    return *this;
  }

  mine_shared_ptr& operator=(mine_shared_ptr&& other) {
    if (ptr == other.ptr) return *this;
    ~mine_shared_ptr();
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
    ~mine_shared_ptr();
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

  ~mine_shared_ptr() {
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