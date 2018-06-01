; --- proto-functions ---

(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    :else (proto-get (:prototype obj) key)))

(defn proto-call [this key & args]
  (apply (proto-get this key) (cons this args)))

; --- syntax sugar yeet ---

(defn field [key]
  (fn [name] (proto-get name key)))

(defn method [key]
  (fn [this & args] (apply proto-call this key args)))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))
(def operands (field :operands))
(def action (field :action))
(def symb (field :symb))

; --- abstract objects ---

(defn Constant [x]
  {
   :evaluate (fn [this vars] x)
   :toString (fn [this] (format "%.1f" (double x)))
   :diff (fn [this v] (Constant 0))
   })

(defn Variable [x]
  {
   :evaluate (fn [this vars] (get vars x))
   :toString (fn [this] (str x))
   :diff (fn [this v] (if (= v x) (Constant 1) (Constant 0)))
   })

(def RawOperation
  {
   :evaluate (fn [this vars] (apply (action this) (mapv (fn [x] (evaluate x vars)) (operands this))))
   :toString (fn [this] (str "(" (symb this) (apply str (mapv (fn [x] (str " " (toString x))) (operands this))) ")"))
   })

(defn Operation [action symb diff]
  (let [this {:prototype RawOperation :action action :symb symb :diff diff}]
    (fn [& args]
      (assoc this :operands args))))

; --- operations ---

(def Add
  (Operation
    +
    "+"
    (fn [this v]
      (apply Add (mapv (fn [x] (diff x v)) (operands this))))))

(def Subtract
  (Operation
    -
    "-"
    (fn [this v]
      (apply Subtract (mapv (fn [x] (diff x v)) (operands this))))))

(def Negate
  (Operation
    -
    "negate"
    (fn [this v]
      (apply Negate (mapv (fn [x] (diff x v)) (operands this))))))

(def Multiply
  (Operation
    *
    "*"
    (fn [this v]
      (apply Add
             (mapv (fn [x] (apply Multiply
                                  (mapv (fn [y] (if (identical? x y) (diff x v) y)) (operands this)))) (operands this))))))

(def Divide
  (Operation
    (fn [x y] (/ (double x) (double y)))
    "/"
    (fn [this v]
      (Divide
        (Subtract
          (Multiply (diff (nth (operands this) 0) v) (nth (operands this) 1))
          (Multiply (nth (operands this) 0) (diff (nth (operands this) 1) v)))
        (Multiply (nth (operands this) 1) (nth (operands this) 1))))))

(def Cos "stumble")

(def Sin
  (Operation
    (fn [x] (Math/sin x))
    "sin"
    (fn [this v]
      (Multiply (Cos (nth (operands this) 0))
                (diff (nth (operands this) 0) v)))))

(def Cos
  (Operation
    (fn [x] (Math/cos x))
    "cos"
    (fn [this v]
      (Negate (Multiply (Sin (nth (operands this) 0))
                        (diff (nth (operands this) 0) v))))))

; --- default parser ---

(def operations
  { '+ Add
   '- Subtract
   '* Multiply
   '/ Divide
   'negate Negate
   'sin Sin
   'cos Cos
   })

(defn parseObject [expr]
  (cond
    (number? expr) (Constant expr)
    (symbol? expr) (Variable (str expr))
    (string? expr) (parseObject (read-string expr))
    (seq? expr) (apply (get operations (first expr)) (mapv parseObject (rest expr)))))

; --- infix parser ---

(def expr)
(def ind (atom 0))
(def token (atom ""))

(defn whitespace? [s] (not (empty? (re-matches #"\s" (str s)))))

(defn digit? [x] (not (empty? (re-matches #"\d" (str x)))))

(defn variable? [x] (not (empty? (re-matches #"[xyz]" (str x)))))

(defn retrieve [] (subs expr @ind (inc @ind)))

(defn increment [] (swap! ind inc))

(defn getIdentifier []
  (if (or (= @ind (count expr)) (whitespace? (retrieve)))
    ""
    (do (increment) (str " " (getIdentifier)))))

(defn getNumber
  ([] (getNumber 0))
  ([acc]
   (if (or (= @ind (count expr)) (whitespace? (retrieve)) (not (digit? (retrieve))))
     acc
     (let [currInd @ind]
       (do
         (increment)
         (getNumber (+ (* 10 acc) (- (int (get expr currInd)) 48))))))))

(defn getVariable []
  (let [res (retrieve)]
    (do
      (increment)
      res)))

(defn skipWhitespace []
  (if (or (= @ind (count expr)) (not (whitespace? (retrieve))))
    nil
    (do (increment) (skipWhitespace))))

(def infixOperations { "+" "ADD" "-" "SUB" "*" "MUL" "/" "DIV" "(" "LB" ")" "RB" "negate" "NEG" "sin" "SIN" "cos" "COS"})
(def infixPriority { "NUM" 0 "VAR" 0 "LB" 0 "NEG" 0 "SIN" 0 "COS" 0 "MUL" 1 "DIV" 1 "ADD" 2 "SUB" 2})

(defn getToken []
  (do
    (skipWhitespace)
    (cond
      (= @ind (count expr)) nil
      (contains? infixOperations (retrieve)) (do (reset! token (get infixOperations (retrieve))) (increment))
      (digit? (retrieve)) (reset! token "NUM")
      (variable? (retrieve)) (reset! token "VAR")
      :else (do (reset! token (get infixOperations (getIdentifier))) (swap! ind #(+ % (count (getIdentifier))))))))

(defn priorityParse
  ([] (priorityParse 2 nil))
  ([priority res]
   (cond
     (= priority 2)
     (cond
       (= @token "ADD") (priorityParse 2 (Add res (priorityParse 1)))
       (= @token "SUB") (priorityParse 2 (Subtract res (priorityParse 1)))
       :else res)
     (= priority 1)
     (cond
       (= @token "MUL") (priorityParse 1 (Multiply res (priorityParse 0)))
       (= @token "DIV") (priorityParse 1 (Divide res (priorityParse 0)))
       :else res)
     :else
     (do (getToken)
         (cond
           (= @token "NUM") (let [res (Constant (getNumber))] (do (getToken) res))
           (= @token "VAR") (let [res (Variable (getVariable))] (do (getToken) res))
           (= @token "LB") (let [res (priorityParse)] (do (getToken) res))
           (= @token "NEG") (Negate (priorityParse 0 nil))
           (= @token "SIN") (Sin (priorityParse 0 nil))
           (= @token "COS") (Cos (priorityParse 0 nil)))))))

(defn parseObjectInfix [expression]
  (do (def expr expression) (priorityParse)))














