pragma circom 2.0.0;

// Simplified Poseidon for testing navigation
// (removed dependency on poseidon_constants.circom)

template Sigma() {
    signal input in;
    signal output out;

    signal in2;
    signal in4;

    in2 <== in*in;
    in4 <== in2*in2;

    out <== in4*in;
}

template Poseidon(nInputs) {
    signal input inputs[nInputs];
    signal output out;

    // Simplified - just sum inputs for testing
    var sum = 0;
    for (var i = 0; i < nInputs; i++) {
        sum += inputs[i];
    }
    out <== sum;
}
