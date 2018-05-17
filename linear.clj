; auxiliary functions

(defn coordinateVectorApply [f & args]
  { :pre [(apply = (mapv count args))] }
  (if (empty? args) 
    []
    (conj (mapv f (mapv first args)) (coordinateVectorApply f (rest args))))))

(defn foldLeft [zero f args]
  (if (empty? args)
    zero
    (recur (f zero (first args)) f (rest args)))
  
(defn getMatrixRow [x ind]
  (mapv (fn [v] (get v ind)) x))

; main functions

; --- vector functions

(defn v+ [& args] 
  (coordinateVectorApply + args))

(defn v- [& args]
  (coordinateVectorApply - args))

(defn v* [& args]
  (coordinateVectorApply * args))

(defn scalar [& args]
  (apply + (v* args)))

(defn vect [& args]
  { :pre [(apply = (conj (mapv count args) 3)) (>= (count args) 2)] }
  (foldLeft 
    (first args) 
    (fn [x y]
      (let [x1 (get x 0) x2 (get x 1) x3 (get x 2) 
            y1 (get y 0) y2 (get y 1) y3 (get y 2)]
        (vector (- (* x2 y3) (* x3 y2)) (- (* x3 y1) (* x1 y3) (- (* x1 y2) (* x2 y1)))))
    (rest args)) 
  
(defn v*s [v & sc]
  (foldLeft
    v
    (fn [x, s]
      (mapv (fn [xi] (* xi s)) x))
    sc)
  
; --- matrix functions

(defn m+ [& args]
  (mapv conj (mapv v+ args)))

(defn m- [& args]
  (mapv conj (mapv v- args)))

(defn m* [& args]
  (mapv conj (mapv v* args)))

(defn m*s [m & sc]
  (mapv (fn [v] (v*s v sc)) m))

(defn m*v [m & vs]
  (foldLeft 
    m
    (mapv scalar (mapv 
      (fn [ind] 
        (getMatrixRow m ind))
      (range (count (first m)))))
    vs))

(defn m*m [& args]
  (foldLeft
    (first args)
    (fn [x y]
      (mapv 
        (fn [v]
          (m*v x v))
        y))
    (rest args)))
  
(defn transpose [m]
  (mapv 
    (fn [ind] 
      (getMatrixRow m ind))
    (range (count (first m)))))