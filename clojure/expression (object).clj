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
      (Multiply (Cos (operands this))
                (diff (operands this) v)))))

(def Cos
  (Operation
    (fn [x] (Math/cos x))
    "cos"
    (fn [this v]
      (Negate (Multiply (Sin (operands this))
                        (diff (operands this) v))))))

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

(defn whitespace? [s]
  (not (empty? (re-matches #"\s" (str s)))))

(defn retrieve []
  (subs expr @ind (inc @ind)))

(defn increment []
  (swap! ind inc))

(defn getIdentifier []
  (if (or (= @ind (count expr)) (whitespace? (retrieve)))
    ""
    (do (increment) (str " " (getIdentifier)))))

(defn getNumber
  ([] (getNumber 0))
  ([acc]
   (if (or (= @ind (count expr)) (whitespace? (retrieve)))
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

(defn getToken []
  (do
    (skipWhitespace)
    (cond
      (= @ind (count expr)) nil
      (= (retrieve) "+") (do (reset! token "ADD") (increment))
      (= (retrieve) "-") (do (reset! token "SUB") (increment))
      (= (retrieve) "*") (do (reset! token "MUL") (increment))
      (= (retrieve) "/") (do (reset! token "DIV") (increment))
      (not (empty? (re-matches #"\d" (str (retrieve))))) (reset! token "NUM")
      (not (empty? (re-matches #"[xyz]" (str (retrieve))))) (reset! token "VAR")
      :else (let [id (getIdentifier)]
              (cond
                (= id "negate") (do (reset! token "NEG") (swap! ind #(+ % 6)))
                (= id "sin") (do (reset! token "SIN") (swap! ind #(+ % 3)))
                (= id "cos") (do (reset! token "COS") (swap! ind #(+ % 3))))))))

(defn priority1 []
  (do
    (getToken)
    (cond
      (= @token "NUM") (let [res (Constant (getNumber))] (do (getToken) res))
      (= @token "VAR") (let [res (Variable (getVariable))] (do (getToken) res))
      (= @token "NEG") (Negate (priority1))
      (= @token "SIN") (Sin (priority1))
      (= @token "COS") (Cos (priority1)))))


(defn priority2
  ([] (priority2 (priority1)))
  ([res]
   (cond
     (= @token "MUL") (priority2 (Multiply res (priority1)))
     (= @token "DIV") (priority2 (Divide res (priority1)))
     :else res)))

(defn priority3
  ([] (priority3 (priority2)))
  ([res]
   (cond
     (= @token "ADD") (priority3 (Add res (priority2)))
     (= @token "SUB") (priority3 (Subtract res (priority2)))
     :else res)))

(defn parseObjectInfix [expression]
  (do (def expr expression) (priority3)))