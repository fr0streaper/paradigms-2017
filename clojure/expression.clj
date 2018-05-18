(defn operation [action]
  (fn [& args]
    (fn [vars]
      (apply action (mapv (fn [x] (x vars)) args)))))

(defn constant [value]
  (fn [vars] value))

(defn variable [sig]
  (fn [vars] (get vars sig)))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [x y] (/ (double x) (double y)))))
(def negate (operation (fn [x] (- x))))
(def sinh (operation (fn [x] (Math/sinh x))))
(def cosh (operation (fn [x] (Math/cosh x))))

(def operations
  { '+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   'sinh sinh
   'cosh cosh
   })
(defn parseFunction [expr]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    (string? expr) (parseFunction (read-string expr))
    (seq? expr) (apply (get operations (first expr)) (mapv parseFunction (rest expr)))))