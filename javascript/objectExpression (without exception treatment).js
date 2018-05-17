var variableIds = {"x": 0, "y": 1, "z": 2}

//Const
function Const(x) {
    this.getValue = function () {
        return x;
    };
}
Const.prototype.evaluate = function () {
    return this.getValue();
};
Const.prototype.diff = function (d) {
    return ZERO;
};
Const.prototype.toString = function () {
    return this.getValue().toString();
};

var ZERO = new Const(0);
var ONE = new Const(1);
var TWO = new Const(2);
var E = new Const(Math.E);

//Variable
function Variable(x) {
    var id = variableIds[x]
    this.getName = function () {
        return x;
    };
    this.getId = function () {
        return id;
    };
}
Variable.prototype.evaluate = function () {
    return arguments[this.getId()];
};
Variable.prototype.diff = function (d) {
    if (d === this.getName()) {
        return ONE;
    }
    return ZERO;
};
Variable.prototype.toString = function () {
    return this.getName();
};

//Operations
function Operation() {
    var args = [].slice.call(arguments);
    this.getArgs = function () {
        return args;
    };
}
Operation.prototype.evaluate = function () {
    var args = this.getArgs();
    var values = [];

    for (var i = 0; i < args.length; i++) {
        values.push(args[i].evaluate.apply(args[i], arguments));
    }

    return this.performAction.apply(null, values);
};
Operation.prototype.diff = function (d) {
    var args = this.getArgs();
    var values = [];

    for (var i = 0; i < args.length; i++) {
        values.push(args[i].diff(d));
    }
    values = args.concat(values);

    return this.performDiff.apply(this, values);
};
Operation.prototype.toString = function () {
    return this.getArgs().join(" ") + " " + this.getOperation();
};

function DefineOperation(constructor, action, diff, symbol) {
    this.constructor = constructor;
    this.performAction = action;
    this.performDiff = diff;
    this.getOperation = function () { return symbol; };
}
DefineOperation.prototype = Operation.prototype;

function wrapOperation(action, diff, symbol) {
    var constructor = function () {
        var args = arguments
        Operation.apply(this, args);
    }
    constructor.prototype = new DefineOperation(constructor, action, diff, symbol);
    return constructor;
}

var Add = wrapOperation(
    function (x, y) {
        return x + y;
    },
    function (x, y, dx, dy) {
        return new Add(dx, dy);
    },
    "+"
)

var Subtract = wrapOperation(
    function (x, y) {
        return x - y;
    },
    function (x, y, dx, dy) {
        return new Subtract(dx, dy);
    },
    "-"
)

var Multiply = wrapOperation(
    function (x, y) {
        return x * y;
    },
    function (x, y, dx, dy) {
        return new Add(new Multiply(x, dy), new Multiply(y, dx));
    },
    "*"
)

var Divide = wrapOperation(
    function (x, y) {
        return x / y;
    },
    function (x, y, dx, dy) {
        return new Divide(new Subtract(new Multiply(dx, y), new Multiply(x, dy)), new Square(y));
    },
    "/"
)

var Negate = wrapOperation(
    function (x) {
        return -x;
    },
    function (x, dx) {
        return new Negate(dx);
    },
    "negate"
)

var Square = wrapOperation(
    function (x) {
        return x * x;
    },
    function (x, dx) {
        return new Multiply(new Multiply(TWO, x), dx);
    },
    "square"
)

var Sqrt = wrapOperation(
    function (x) {
        return Math.sqrt(Math.abs(x));
    },
    function (x, dx) {
        return new Divide(new Multiply(x, dx), new Multiply(TWO, new Sqrt(new Multiply(new Square(x), x))));
    },
    "sqrt"
)

var Power = wrapOperation(
    function (x, y) {
        return Math.pow(x, y);
    },
    function (x, y, dx, dy) {
        return new Multiply(new Power(x, new Subtract(y, ONE)), new Add(new Multiply(y, dx),
            new Multiply(x, new Multiply(dy, new Log(E, x)))));
    },
    "pow"
)

var Log = wrapOperation(
    function (x, y) {
        return Math.log(Math.abs(y)) / Math.log(Math.abs(x));
    },
    function (x, y, dx, dy) {
        return new Divide(new Subtract(new Divide(new Multiply(dy, new Log(E, x)), y),
            new Divide(new Multiply(dx, new Log(E, y)), x)), new Square(new Log(E, x)));
    },
    "log"
)

var create = function(constructor, args) {
    var env = Object.create(constructor.prototype);
    constructor.apply(env, args);
    return env;
}

var parse = function (s) {
    var operations = {
        "/": Divide,
        "*": Multiply,
        "+": Add,
        "-": Subtract,
        "negate": Negate,
        "sqrt": Sqrt,
        "square": Square,
        "pow": Power,
        "log": Log
    };
    var argumentsCount = {
        "/": 2,
        "*": 2,
        "+": 2,
        "-": 2,
        "negate": 1,
        "sqrt": 1,
        "square": 1,
        "pow": 2,
        "log": 2
    };

    var stack = [];
    var tokens = s.split(" ").filter(function (x) {
        return x.length > 0;
    });

    for (var i = 0; i < tokens.length; i++) {
        var token = tokens[i];
        if (token in variableIds) {
            stack.push(new Variable(token));
        }
        else if (token in operations) {
            var args = [];
            for (var j = 0; j < argumentsCount[token]; j++) {
                args.push(stack.pop());
            }
            args.reverse();
            stack.push(create(operations[token], args));
        }
        else {
            stack.push(new Const(parseFloat(token)));
        }
    }

    return stack.pop();
}