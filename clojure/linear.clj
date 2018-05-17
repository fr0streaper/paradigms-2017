; auxiliary functions

(defn foldLeft [zero f args]
  (if (empty? args)
    zero
    (recur (f zero (first args)) f (rest args))))

(defn checkVectors [vs]
  (apply = (mapv count vs)))

(defn checkMatrixes [ms]
  (and (apply = (conj (mapv checkVectors ms) true))
       (apply = (mapv count ms))
       (apply = (mapv (fn [x] (count (first x))) ms))))

(defn checkMatrixesForMultiplication [ms]
  (and (apply = (conj (mapv checkVectors ms) true))
       (loop [ind 0 n (count ms) res true]
         (if (= ind (dec n))
           res
           (recur (inc ind) n (and res (= (count (first (get ms ind)))
                                          (count (get ms (inc ind))))))))))

(defn coordinateVectorApply [f & args]
  { :pre [(checkVectors args) (> (count args) 0)] }
  (apply mapv f args))

(defn coordinateMatrixApply [f & args]
  { :pre [(checkMatrixes args) (> (count args) 0)] }
  (apply mapv f args))

(defn getForm [x]
  { :pre [] }
  (loop [res [] t x]
    (if (number? t)
      res
      (recur (conj res (count t)) (first t)))))

(def cmp (comparator (fn [x y] (> (count x) (count y)))))

(defn canBroadcast [xf yf]
  { :pre [] }
  (= xf (subvec yf (- (count yf) (count xf)))))

(defn checkTensor
  ([x]
   (checkTensor x (getForm x)))
  ([x form]
   (cond
     (and (empty? form) (number? x)) true
     (or (and (empty? form) (not (number? x)))
         (and (not (empty? form)) (number? x))) false
     :else (apply = (conj (mapv (fn [p] (checkTensor p (rest form))) x) (= (count x) (first form)) true)))))

(defn checkTensors [args]
  { :pre [(apply = (conj (mapv checkTensor args) true))] }
  (let [mform (first (sort cmp (mapv getForm args)))]
    (apply = (conj (mapv (fn [x] (canBroadcast (getForm x) mform)) args) true))))

(defn broadcast [x form]
  { :pre [(canBroadcast (getForm x) form)] }
  (let [ext (vec (take (- (count form) (count (getForm x))) form))]
    (foldLeft
      x
      (fn [s cnt]
        (vec (take cnt (repeat s))))
      (vec (rseq ext)))))

(defn coordinateTensorApply [f & args]
  { :pre [(checkTensors args)] }
  (let [mform (first (sort cmp (mapv getForm args)))
        nargs (mapv (fn [x] (broadcast x mform)) args)]
    (cond
      (number? nargs) nargs
      (number? (first nargs)) (apply f nargs)
      :else (apply mapv (fn [& x] (apply coordinateTensorApply f x)) nargs))))

; main functions

; --- vector functions

(defn v+ [& args]
  { :pre [(checkVectors args) (> (count args) 0)] }
  (apply coordinateVectorApply + args))

(defn v- [& args]
  { :pre [(checkVectors args) (> (count args) 0)] }
  (apply coordinateVectorApply - args))

(defn v* [& args]
  { :pre [(checkVectors args) (> (count args) 0)] }
  (apply coordinateVectorApply * args))

(defn scalar [& args]
  { :pre [(checkVectors args) (> (count args) 0)] }
  (apply + (apply v* args)))

(defn vect [v & args]
  { :pre [(apply = (conj (mapv count args) 3))
          (checkVectors (conj args v))] }
  (foldLeft
    v
    (fn [x y]
      (let [x1 (get x 0) x2 (get x 1) x3 (get x 2)
            y1 (get y 0) y2 (get y 1) y3 (get y 2)]
        (vector (- (* x2 y3) (* x3 y2)) (- (* x3 y1) (* x1 y3)) (- (* x1 y2) (* x2 y1)))))
    args))

(defn v*s [v & sc]
  { :pre [] }
  (foldLeft
    v
    (fn [x, s]
      (mapv (fn [xi] (* xi s)) x))
    sc))

; --- matrix functions

(defn transpose [m]
  { :pre [(checkVectors m)] }
  (apply mapv vector m))

(defn m+ [& args]
  { :pre [(checkMatrixes args)] }
  (apply coordinateMatrixApply v+ args))

(defn m- [& args]
  { :pre [(checkMatrixes args)] }
  (apply coordinateMatrixApply v- args))

(defn m* [& args]
  { :pre [(checkMatrixes args)] }
  (apply coordinateMatrixApply v* args))

(defn m*s [m & sc]
  { :pre [(checkVectors m)] }
  (mapv (fn [v] (apply v*s v sc)) m))

(defn m*v [m v]
  { :pre [(checkVectors m) (= (count (first m)) (count v))] }
  (mapv (fn [x] (scalar x v)) m))

(defn m*m [& args]
  { :pre [(checkMatrixesForMultiplication args)] }
  (foldLeft
    (first args)
    (fn [x y]
      (apply mapv (fn [& args] (into [] (apply concat args))) (mapv
                                                                (fn [v]
                                                                  (mapv vector (m*v x v)))
                                                                (transpose y))))
    (rest args)))

; --- tensor functions

(defn b+ [& args]
  { :pre [(checkTensors args)] }
  (apply coordinateTensorApply + args))

(defn b- [& args]
  { :pre [(checkTensors args)] }
  (apply coordinateTensorApply - args))

(defn b* [& args]
  { :pre [(checkTensors args)] }
  (apply coordinateTensorApply * args))