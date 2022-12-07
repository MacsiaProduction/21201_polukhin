#ifndef mine_vector_HPP
#define mine_vector_HPP
#include <stddef.h>
#include <algorithm>
template <class K>
class mine_vector
{
public:
	mine_vector(size_t size = 0)
	{
		arr = new K[size];
		real_len = len = size;
	}
	mine_vector(const mine_vector<K> &b)
	{
		arr = new K[b.len];
		real_len = len = b.len;
		std::copy(b.arr, b.arr + b.len, arr);
	}
	~mine_vector()
	{
		delete[] arr;
	}
	mine_vector &operator=(const mine_vector &old)
	{
		clear();
		resize(old.len);
		std::copy(old.arr, old.arr + old.len, arr);
		return *this;
	}
	mine_vector &operator=(mine_vector &&old)
	{
		clear();
		resize(old.len);
		std::move(old.arr, old.arr + old.len, arr);
		return *this;
	}
	K &operator[](size_t index) const
	{
		return arr[index];
	}
	bool empty() const
	{
		return len == 0;
	}
	size_t size() const
	{
		return len;
	}
	void push_back(K to_add)
	{
		insert(len - 1, to_add);
	};
	void clear()
	{
		resize(0);
	};
	void erase(size_t index)
	{
		(arr + index)->~K();
		for (size_t i = index; i < len - 1; i++)
		{
			arr[i] = std::move(arr[i + 1]);
		}
		resize(len - 1);
	};
	void insert(size_t index, K value)
	{
		resize(len + 1);
		for (size_t i = len - 1; i > index; i--)
		{
			arr[i] = std::move(arr[i - 1]);
		}
		arr[index] = value;
	};
	friend bool operator==(const mine_vector &a, const mine_vector &b)
	{
		if (a.len != b.len)
			return 0;
		for (size_t i = 0; i < a.len; i++)
		{
			if (a[i] != b[i])
				return false;
		}
		return true;
	}

private:
	void resize(size_t new_size)
	{
		if (real_len < new_size || real_len >= kLen_multiplier * kLen_multiplier * new_size)
		{
			K *tmp = new K[new_size * kLen_multiplier];
			std::move(arr, arr + std::min(new_size, len), tmp);
			delete[] arr;
			arr = tmp;
			real_len = kLen_multiplier * new_size;
		}
		len = new_size;
	}
	K *arr;
	static constexpr int kLen_multiplier = 2;
	size_t len;
	size_t real_len;
};
#endif