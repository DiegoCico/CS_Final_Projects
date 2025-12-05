#!/bin/bash

echo "Testing all configurations..."
echo ""

passed=0
failed=0

for config in configs/*.conf; do
    name=$(basename "$config")
    echo -n "Testing $name... "
    
    # Run the test with a timeout
    output=$(python3 run "$config" 2>&1 & PID=$!; sleep 12; kill $PID 2>/dev/null; wait $PID 2>/dev/null)
    
    # Check if it passed
    if echo "$output" | grep -q "All simulation responses verified"; then
        echo "PASS"
        ((passed++))
    else
        echo "FAIL"
        ((failed++))
    fi
    
    # Clean up
    pkill -f "python3 run" 2>/dev/null
    pkill -f 4700dns 2>/dev/null
    sleep 1
done

echo ""
echo "Results: $passed passed, $failed failed out of $((passed + failed)) total"
