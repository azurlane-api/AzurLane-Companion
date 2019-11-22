cmake_minimum_required(VERSION 3.4.1)

add_library(api SHARED
            api.c)

# Include libraries needed for api lib
target_link_libraries(api
            android
            log)