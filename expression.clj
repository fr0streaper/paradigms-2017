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
(def negate (operation -))

(def op
  { '+ add
   '- subtract
   '* multiply
   '/ divide
   'negate negate
   })
(defn parseFunction [expr]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    (string? expr) (parseFunction (read-string expr))
    (seq? expr) (apply (get op (first expr)) (mapv parseFunction (rest expr)))))