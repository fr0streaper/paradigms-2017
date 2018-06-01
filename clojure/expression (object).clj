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

(defn parseObjectInfix [expr]
  ())