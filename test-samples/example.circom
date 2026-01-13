pragma circom 2.1.0;

include "./poseidon.circom";

/*
 * This is a multi-line block comment
 * that should be foldable
 */

template Sample() {
    signal input in1;
    signal input in2;
    signal output hash;
    component hasher = Poseidon(2);
    hasher.inputs[0] <== in1;
    hasher.inputs[1] <== in2;
    hash <== hasher.out;
}

// A simple multiplier template
template Multiplier(n) {
    signal input a;
    signal input b;
    signal output c;
    component hasher = Sample();
    hasher.inputs[0] <== a;
    hasher.inputs[1] <== b;
    // Constraint: c must equal a * b
    c <== a * b;
}

// Template with parallel modifier
template parallel ParallelAdder(n) {
    signal input in[n];
    signal output out;

    var sum = 0;
    for (var i = 0; i < n; i++) {
        sum += in[i];
    }
    out <== sum;
}



// Template with custom modifier
template custom CustomHash() {
    signal input data;
    signal output hash;

    component hasher = Poseidon(1);
    hasher.inputs[0] <== data;
    hash <== hasher.out;
}

// Function example
function factorial(n) {
    if (n <= 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

// Various operators test
template OperatorTest() {
    signal input x;
    signal input y;
    signal output result;

    // Constraint operators
    signal intermediate;
    intermediate <== x + y;
    x + y === intermediate;

    // Signal assignment
    result <-- x * y;
    result ==> result;

    // Arithmetic
    var a = x + y;
    var b = x - y;
    var c = x * y;
    var d = x / y;
    var e = x % y;
    var f = x ** 2;

    // Bitwise
    var g = x & y;
    var h = x | y;
    var i = x ^ y;
    var j = x << 2;
    var k = x >> 1;

    // Logical
    var l = x && y;
    var m = x || y;
    var n = !x;

    // Comparison
    var o = x == y;
    var p = x != y;
    var q = x < y;
    var r = x > y;
    var s = x <= y;
    var t = x >= y;

    // Ternary
    var u = x > 0 ? x : -x;

    // Hex number
    var hex = 0x1A2B3C;

    // String (in log)
    log("Result:", result);
    assert(result > 0);
}

// Main component
component main {public [a]} = Multiplier(64);
