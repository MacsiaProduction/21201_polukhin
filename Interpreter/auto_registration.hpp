#ifndef auto_registration_HPP
#define auto_registration_HPP
#include "default_functions_factory.hpp"
#include "../res/log.hpp"
namespace auto_registration
{
    // register default_function in factory
    template <class T>
    struct register_function
    {
        register_function()
        {
            default_functions_factory::get_factory_instance()
                ->add_default_function(std::shared_ptr<T>(new T));
        }
    };
};
#endif