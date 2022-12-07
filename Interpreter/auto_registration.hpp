#ifndef auto_registration_HPP
#define auto_registration_HPP

#include <memory>
#include "default_functions_factory.hpp"

namespace auto_registration
{
    // registers default_function in factory
    template <class T>
    struct register_function
    {
        register_function()
        {
            default_functions_factory<default_function>::get_factory_instance()
                ->add_default_function(std::move(std::unique_ptr<T>(new T)));
        }
    };
};
#endif