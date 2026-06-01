cmake_minimum_required(VERSION 3.22.1)
project(bridge_dss)

# Zbierz pliki źródłowe DSS (tylko library/src, bez testów i examples)
file(GLOB_RECURSE DSS_SOURCES
    "dss/library/src/*.cpp"
)

# Wyklucz testy
list(FILTER DSS_SOURCES EXCLUDE REGEX ".*/tests/.*\\.cpp$")
list(FILTER DSS_SOURCES EXCLUDE REGEX ".*/examples/.*\\.cpp$")

add_library(
    bridge_dss
    SHARED
    dss_jni.cpp
    ${DSS_SOURCES}
)

target_include_directories(bridge_dss PRIVATE
    dss/library/src/api
    dss/library/src
    dss/library/src/system
    dss/library/src/solver_context
    dss/library/src/trans_table
    dss/library/src/moves
    dss/library/src/heuristic_sorting
    dss/library/src/lookup_tables
    dss/library/src/utility
)

target_compile_options(bridge_dss PRIVATE
    -O2
    -DVERSION_GCC
    -DTHREADING_STLIMPL
)

find_library(log-lib log)
target_link_libraries(bridge_dss ${log-lib})