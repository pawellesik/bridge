cmake_minimum_required(VERSION 3.22.1)
project(bridge_dds)

# Zbierz pliki źródłowe DDS (tylko library/src, bez testów i examples)
file(GLOB_RECURSE DDS_SOURCES
    "dds/library/src/*.cpp"
)

# Wyklucz testy
list(FILTER DDS_SOURCES EXCLUDE REGEX ".*/tests/.*\\.cpp$")
list(FILTER DDS_SOURCES EXCLUDE REGEX ".*/examples/.*\\.cpp$")

add_library(
    bridge_dds
    SHARED
    dds_jni.cpp
    ${DDS_SOURCES}
)

target_include_directories(bridge_dds PRIVATE
    dds/library/src/api
    dds/library/src
    dds/library/src/system
    dds/library/src/solver_context
    dds/library/src/trans_table
    dds/library/src/moves
    dds/library/src/heuristic_sorting
    dds/library/src/lookup_tables
    dds/library/src/utility
)

target_compile_options(bridge_dds PRIVATE
    -O2
    -DVERSION_GCC
    -DTHREADING_STLIMPL
)

find_library(log-lib log)
target_link_libraries(bridge_dds ${log-lib})
