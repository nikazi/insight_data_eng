# Overall design
The solution includes 3 major components:
1. Input reader: reads data row by row to have a low memory footprint.
2. Session extractor: keep hashmap of all access to form the sessions. As soon as a session is formed 
it will be passed to session writer and will be evicted from hashmap to keep minimum memroy usage.
3. Output session writer: writes a given session to the output file.

# Algorithm
It uses a hashmap to keep track of open sessions and close the session as soon as the next record time is more than the allowed session gap. This solution will have limited memory impact since it will remove from hashmap as soon as a session is found. The only case the memory can explode is when all records have same time and the gap never reaches.
We can improve he algorithm further to cover that case as well by limiting number of open sessions in the hashmap.

# Technologies
This project use bazel build tool.

# Changes to run_test.sh
 Since this project use bazel so copying src code is not easily possible. So we disabled copying src files and made the test run to work with the existing binary. 
 We also changed the diff to ignore the line order since the problem never mentioned about specific order of the output.