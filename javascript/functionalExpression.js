variableIds = {"x": 0, "y": 1, "z": 2};
consts = {"pi": Math.PI, "e": Math.E};

var operation = function (op) {
    return function () {
        var args = arguments;
        return function () {
            var values = [];

            for (var i = 0; i < args.length; i++) {
                values.push(args[i].apply(null, arguments));
            }

            return op.apply(null, values);
        }
    }
};

var add = operation(function (x, y) {
    return x + y;
});

var subtract = operation(function (x, y) {
    return x - y;
});

var multiply = operation(function (x, y) {
    return x * y;
});

var divide = operation(function (x, y) {
    return x / y;
});

var negate = operation(function (x) {
    return -x;
});

var cnst = function (x) {
    return function () {
        return x;
    };
};

var variable = function (x) {
    return function () {
        return arguments[variableIds[x]];
    };
};

var min3 = operation(function (x1, x2, x3) {
    return Math.min(x1, x2, x3);
});

var max5 = operation(function (x1, x2, x3, x4, x5) {
    return Math.max(x1, x2, x3, x4, x5);
});

var cube = operation(function (x) {
    return Math.pow(x, 3);
});

var cuberoot = operation(function (x) {
    return Math.pow(x, 1. / 3.);
});

for (var name in consts) {
    global[name] = cnst(consts[name]);
}

for (var name in variableIds) {
    global[name] = variable(name);
}

var parse = function (s) {
    var operations = {
        "/": divide,
        "*": multiply,
        "+": add,
        "-": subtract,
        "negate": negate,
        "min3": min3,
        "max5": max5,
        "cube": cube,
        "cuberoot": cuberoot
    };
    var argumentsCount = {
        "/": 2,
        "*": 2,
        "+": 2,
        "-": 2,
        "negate": 1,
        "min3": 3,
        "max5": 5,
        "cube": 1,
        "cuberoot": 1
    };

    var stack = [];
    var tokens = s.split(" ").filter(function (x) {
        return x.length > 0;
    });

    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if (token in consts) {
            stack.push(cnst(consts[token]));
        }
        else if (token in variableIds) {
            stack.push(variable(token));
        }
        else if (token in operations) {
            var args = [];
            for (var j = 0; j < argumentsCount[token]; j++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(operations[token].apply(null, args))
        }
        else {
            stack.push(cnst(parseFloat(token)));
        }
    }

    return stack.pop();
}