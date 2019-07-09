//     Underscore.js 1.9.1
//     http://underscorejs.org
//     (c) 2009-2018 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
//     Underscore may be freely distributed under the MIT license.

(function() {

  // Baseline setup
  // --------------

  // Establish the root object, `window` (`self`) in the browser, `global`
  // on the server, or `this` in some virtual machines. We use `self`
  // instead of `window` for `WebWorker` support.
  var root = typeof self == 'object' && self.self === self && self ||
            typeof global == 'object' && global.global === global && global ||
            this ||
            {};

  // Save the previous value of the `_` variable.
  var previousUnderscore = root._;

  // Save bytes in the minified (but not gzipped) version:
  var ArrayProto = Array.prototype, ObjProto = Object.prototype;
  var SymbolProto = typeof Symbol !== 'undefined' ? Symbol.prototype : null;

  // Create quick reference variables for speed access to core prototypes.
  var push = ArrayProto.push,
      slice = ArrayProto.slice,
      toString = ObjProto.toString,
      hasOwnProperty = ObjProto.hasOwnProperty;

  // All **ECMAScript 5** native function implementations that we hope to use
  // are declared here.
  var nativeIsArray = Array.isArray,
      nativeKeys = Object.keys,
      nativeCreate = Object.create;

  // Naked function reference for surrogate-prototype-swapping.
  var Ctor = function(){};

  // Create a safe reference to the Underscore object for use below.
  var _ = function(obj) {
    if (obj instanceof _) return obj;
    if (!(this instanceof _)) return new _(obj);
    this._wrapped = obj;
  };

  // Export the Underscore object for **Node.js**, with
  // backwards-compatibility for their old module API. If we're in
  // the browser, add `_` as a global object.
  // (`nodeType` is checked to ensure that `module`
  // and `exports` are not HTML elements.)
  if (typeof exports != 'undefined' && !exports.nodeType) {
    if (typeof module != 'undefined' && !module.nodeType && module.exports) {
      exports = module.exports = _;
    }
    exports._ = _;
  } else {
    root._ = _;
  }

  // Current version.
  _.VERSION = '1.9.1';

  // Internal function that returns an efficient (for current engines) version
  // of the passed-in callback, to be repeatedly applied in other Underscore
  // functions.
  var optimizeCb = function(func, context, argCount) {
    if (context === void 0) return func;
    switch (argCount == null ? 3 : argCount) {
      case 1: return function(value) {
        return func.call(context, value);
      };
      // The 2-argument case is omitted because we’re not using it.
      case 3: return function(value, index, collection) {
        return func.call(context, value, index, collection);
      };
      case 4: return function(accumulator, value, index, collection) {
        return func.call(context, accumulator, value, index, collection);
      };
    }
    return function() {
      return func.apply(context, arguments);
    };
  };

  var builtinIteratee;

  // An internal function to generate callbacks that can be applied to each
  // element in a collection, returning the desired result — either `identity`,
  // an arbitrary callback, a property matcher, or a property accessor.
  var cb = function(value, context, argCount) {
    if (_.iteratee !== builtinIteratee) return _.iteratee(value, context);
    if (value == null) return _.identity;
    if (_.isFunction(value)) return optimizeCb(value, context, argCount);
    if (_.isObject(value) && !_.isArray(value)) return _.matcher(value);
    return _.property(value);
  };

  // External wrapper for our callback generator. Users may customize
  // `_.iteratee` if they want additional predicate/iteratee shorthand styles.
  // This abstraction hides the internal-only argCount argument.
  _.iteratee = builtinIteratee = function(value, context) {
    return cb(value, context, Infinity);
  };

  // Some functions take a variable number of arguments, or a few expected
  // arguments at the beginning and then a variable number of values to operate
  // on. This helper accumulates all remaining arguments past the function’s
  // argument length (or an explicit `startIndex`), into an array that becomes
  // the last argument. Similar to ES6’s "rest parameter".
  var restArguments = function(func, startIndex) {
    startIndex = startIndex == null ? func.length - 1 : +startIndex;
    return function() {
      var length = Math.max(arguments.length - startIndex, 0),
          rest = Array(length),
          index = 0;
      for (; index < length; index++) {
        rest[index] = arguments[index + startIndex];
      }
      switch (startIndex) {
        case 0: return func.call(this, rest);
        case 1: return func.call(this, arguments[0], rest);
        case 2: return func.call(this, arguments[0], arguments[1], rest);
      }
      var args = Array(startIndex + 1);
      for (index = 0; index < startIndex; index++) {
        args[index] = arguments[index];
      }
      args[startIndex] = rest;
      return func.apply(this, args);
    };
  };

  // An internal function for creating a new object that inherits from another.
  var baseCreate = function(prototype) {
    if (!_.isObject(prototype)) return {};
    if (nativeCreate) return nativeCreate(prototype);
    Ctor.prototype = prototype;
    var result = new Ctor;
    Ctor.prototype = null;
    return result;
  };

  var shallowProperty = function(key) {
    return function(obj) {
      return obj == null ? void 0 : obj[key];
    };
  };

  var has = function(obj, path) {
    return obj != null && hasOwnProperty.call(obj, path);
  }

  var deepGet = function(obj, path) {
    var length = path.length;
    for (var i = 0; i < length; i++) {
      if (obj == null) return void 0;
      obj = obj[path[i]];
    }
    return length ? obj : void 0;
  };

  // Helper for collection methods to determine whether a collection
  // should be iterated as an array or as an object.
  // Related: http://people.mozilla.org/~jorendorff/es6-draft.html#sec-tolength
  // Avoids a very nasty iOS 8 JIT bug on ARM-64. #2094
  var MAX_ARRAY_INDEX = Math.pow(2, 53) - 1;
  var getLength = shallowProperty('length');
  var isArrayLike = function(collection) {
    var length = getLength(collection);
    return typeof length == 'number' && length >= 0 && length <= MAX_ARRAY_INDEX;
  };

  // Collection Functions
  // --------------------

  // The cornerstone, an `each` implementation, aka `forEach`.
  // Handles raw objects in addition to array-likes. Treats all
  // sparse array-likes as if they were dense.
  _.each = _.forEach = function(obj, iteratee, context) {
    iteratee = optimizeCb(iteratee, context);
    var i, length;
    if (isArrayLike(obj)) {
      for (i = 0, length = obj.length; i < length; i++) {
        iteratee(obj[i], i, obj);
      }
    } else {
      var keys = _.keys(obj);
      for (i = 0, length = keys.length; i < length; i++) {
        iteratee(obj[keys[i]], keys[i], obj);
      }
    }
    return obj;
  };

  // Return the results of applying the iteratee to each element.
  _.map = _.collect = function(obj, iteratee, context) {
    iteratee = cb(iteratee, context);
    var keys = !isArrayLike(obj) && _.keys(obj),
        length = (keys || obj).length,
        results = Array(length);
    for (var index = 0; index < length; index++) {
      var currentKey = keys ? keys[index] : index;
      results[index] = iteratee(obj[currentKey], currentKey, obj);
    }
    return results;
  };

  // Create a reducing function iterating left or right.
  var createReduce = function(dir) {
    // Wrap code that reassigns argument variables in a separate function than
    // the one that accesses `arguments.length` to avoid a perf hit. (#1991)
    var reducer = function(obj, iteratee, memo, initial) {
      var keys = !isArrayLike(obj) && _.keys(obj),
          length = (keys || obj).length,
          index = dir > 0 ? 0 : length - 1;
      if (!initial) {
        memo = obj[keys ? keys[index] : index];
        index += dir;
      }
      for (; index >= 0 && index < length; index += dir) {
        var currentKey = keys ? keys[index] : index;
        memo = iteratee(memo, obj[currentKey], currentKey, obj);
      }
      return memo;
    };

    return function(obj, iteratee, memo, context) {
      var initial = arguments.length >= 3;
      return reducer(obj, optimizeCb(iteratee, context, 4), memo, initial);
    };
  };

  // **Reduce** builds up a single result from a list of values, aka `inject`,
  // or `foldl`.
  _.reduce = _.foldl = _.inject = createReduce(1);

  // The right-associative version of reduce, also known as `foldr`.
  _.reduceRight = _.foldr = createReduce(-1);

  // Return the first value which passes a truth test. Aliased as `detect`.
  _.find = _.detect = function(obj, predicate, context) {
    var keyFinder = isArrayLike(obj) ? _.findIndex : _.findKey;
    var key = keyFinder(obj, predicate, context);
    if (key !== void 0 && key !== -1) return obj[key];
  };

  // Return all the elements that pass a truth test.
  // Aliased as `select`.
  _.filter = _.select = function(obj, predicate, context) {
    var results = [];
    predicate = cb(predicate, context);
    _.each(obj, function(value, index, list) {
      if (predicate(value, index, list)) results.push(value);
    });
    return results;
  };

  // Return all the elements for which a truth test fails.
  _.reject = function(obj, predicate, context) {
    return _.filter(obj, _.negate(cb(predicate)), context);
  };

  // Determine whether all of the elements match a truth test.
  // Aliased as `all`.
  _.every = _.all = function(obj, predicate, context) {
    predicate = cb(predicate, context);
    var keys = !isArrayLike(obj) && _.keys(obj),
        length = (keys || obj).length;
    for (var index = 0; index < length; index++) {
      var currentKey = keys ? keys[index] : index;
      if (!predicate(obj[currentKey], currentKey, obj)) return false;
    }
    return true;
  };

  // Determine if at least one element in the object matches a truth test.
  // Aliased as `any`.
  _.some = _.any = function(obj, predicate, context) {
    predicate = cb(predicate, context);
    var keys = !isArrayLike(obj) && _.keys(obj),
        length = (keys || obj).length;
    for (var index = 0; index < length; index++) {
      var currentKey = keys ? keys[index] : index;
      if (predicate(obj[currentKey], currentKey, obj)) return true;
    }
    return false;
  };

  // Determine if the array or object contains a given item (using `===`).
  // Aliased as `includes` and `include`.
  _.contains = _.includes = _.include = function(obj, item, fromIndex, guard) {
    if (!isArrayLike(obj)) obj = _.values(obj);
    if (typeof fromIndex != 'number' || guard) fromIndex = 0;
    return _.indexOf(obj, item, fromIndex) >= 0;
  };

  // Invoke a method (with arguments) on every item in a collection.
  _.invoke = restArguments(function(obj, path, args) {
    var contextPath, func;
    if (_.isFunction(path)) {
      func = path;
    } else if (_.isArray(path)) {
      contextPath = path.slice(0, -1);
      path = path[path.length - 1];
    }
    return _.map(obj, function(context) {
      var method = func;
      if (!method) {
        if (contextPath && contextPath.length) {
          context = deepGet(context, contextPath);
        }
        if (context == null) return void 0;
        method = context[path];
      }
      return method == null ? method : method.apply(context, args);
    });
  });

  // Convenience version of a common use case of `map`: fetching a property.
  _.pluck = function(obj, key) {
    return _.map(obj, _.property(key));
  };

  // Convenience version of a common use case of `filter`: selecting only objects
  // containing specific `key:value` pairs.
  _.where = function(obj, attrs) {
    return _.filter(obj, _.matcher(attrs));
  };

  // Convenience version of a common use case of `find`: getting the first object
  // containing specific `key:value` pairs.
  _.findWhere = function(obj, attrs) {
    return _.find(obj, _.matcher(attrs));
  };

  // Return the maximum element (or element-based computation).
  _.max = function(obj, iteratee, context) {
    var result = -Infinity, lastComputed = -Infinity,
        value, computed;
    if (iteratee == null || typeof iteratee == 'number' && typeof obj[0] != 'object' && obj != null) {
      obj = isArrayLike(obj) ? obj : _.values(obj);
      for (var i = 0, length = obj.length; i < length; i++) {
        value = obj[i];
        if (value != null && value > result) {
          result = value;
        }
      }
    } else {
      iteratee = cb(iteratee, context);
      _.each(obj, function(v, index, list) {
        computed = iteratee(v, index, list);
        if (computed > lastComputed || computed === -Infinity && result === -Infinity) {
          result = v;
          lastComputed = computed;
        }
      });
    }
    return result;
  };

  // Return the minimum element (or element-based computation).
  _.min = function(obj, iteratee, context) {
    var result = Infinity, lastComputed = Infinity,
        value, computed;
    if (iteratee == null || typeof iteratee == 'number' && typeof obj[0] != 'object' && obj != null) {
      obj = isArrayLike(obj) ? obj : _.values(obj);
      for (var i = 0, length = obj.length; i < length; i++) {
        value = obj[i];
        if (value != null && value < result) {
          result = value;
        }
      }
    } else {
      iteratee = cb(iteratee, context);
      _.each(obj, function(v, index, list) {
        computed = iteratee(v, index, list);
        if (computed < lastComputed || computed === Infinity && result === Infinity) {
          result = v;
          lastComputed = computed;
        }
      });
    }
    return result;
  };

  // Shuffle a collection.
  _.shuffle = function(obj) {
    return _.sample(obj, Infinity);
  };

  // Sample **n** random values from a collection using the modern version of the
  // [Fisher-Yates shuffle](http://en.wikipedia.org/wiki/Fisher–Yates_shuffle).
  // If **n** is not specified, returns a single random element.
  // The internal `guard` argument allows it to work with `map`.
  _.sample = function(obj, n, guard) {
    if (n == null || guard) {
      if (!isArrayLike(obj)) obj = _.values(obj);
      return obj[_.random(obj.length - 1)];
    }
    var sample = isArrayLike(obj) ? _.clone(obj) : _.values(obj);
    var length = getLength(sample);
    n = Math.max(Math.min(n, length), 0);
    var last = length - 1;
    for (var index = 0; index < n; index++) {
      var rand = _.random(index, last);
      var temp = sample[index];
      sample[index] = sample[rand];
      sample[rand] = temp;
    }
    return sample.slice(0, n);
  };

  // Sort the object's values by a criterion produced by an iteratee.
  _.sortBy = function(obj, iteratee, context) {
    var index = 0;
    iteratee = cb(iteratee, context);
    return _.pluck(_.map(obj, function(value, key, list) {
      return {
        value: value,
        index: index++,
        criteria: iteratee(value, key, list)
      };
    }).sort(function(left, right) {
      var a = left.criteria;
      var b = right.criteria;
      if (a !== b) {
        if (a > b || a === void 0) return 1;
        if (a < b || b === void 0) return -1;
      }
      return left.index - right.index;
    }), 'value');
  };

  // An internal function used for aggregate "group by" operations.
  var group = function(behavior, partition) {
    return function(obj, iteratee, context) {
      var result = partition ? [[], []] : {};
      iteratee = cb(iteratee, context);
      _.each(obj, function(value, index) {
        var key = iteratee(value, index, obj);
        behavior(result, value, key);
      });
      return result;
    };
  };

  // Groups the object's values by a criterion. Pass either a string attribute
  // to group by, or a function that returns the criterion.
  _.groupBy = group(function(result, value, key) {
    if (has(result, key)) result[key].push(value); else result[key] = [value];
  });

  // Indexes the object's values by a criterion, similar to `groupBy`, but for
  // when you know that your index values will be unique.
  _.indexBy = group(function(result, value, key) {
    result[key] = value;
  });

  // Counts instances of an object that group by a certain criterion. Pass
  // either a string attribute to count by, or a function that returns the
  // criterion.
  _.countBy = group(function(result, value, key) {
    if (has(result, key)) result[key]++; else result[key] = 1;
  });

  var reStrSymbol = /[^\ud800-\udfff]|[\ud800-\udbff][\udc00-\udfff]|[\ud800-\udfff]/g;
  // Safely create a real, live array from anything iterable.
  _.toArray = function(obj) {
    if (!obj) return [];
    if (_.isArray(obj)) return slice.call(obj);
    if (_.isString(obj)) {
      // Keep surrogate pair characters together
      return obj.match(reStrSymbol);
    }
    if (isArrayLike(obj)) return _.map(obj, _.identity);
    return _.values(obj);
  };

  // Return the number of elements in an object.
  _.size = function(obj) {
    if (obj == null) return 0;
    return isArrayLike(obj) ? obj.length : _.keys(obj).length;
  };

  // Split a collection into two arrays: one whose elements all satisfy the given
  // predicate, and one whose elements all do not satisfy the predicate.
  _.partition = group(function(result, value, pass) {
    result[pass ? 0 : 1].push(value);
  }, true);

  // Array Functions
  // ---------------

  // Get the first element of an array. Passing **n** will return the first N
  // values in the array. Aliased as `head` and `take`. The **guard** check
  // allows it to work with `_.map`.
  _.first = _.head = _.take = function(array, n, guard) {
    if (array == null || array.length < 1) return n == null ? void 0 : [];
    if (n == null || guard) return array[0];
    return _.initial(array, array.length - n);
  };

  // Returns everything but the last entry of the array. Especially useful on
  // the arguments object. Passing **n** will return all the values in
  // the array, excluding the last N.
  _.initial = function(array, n, guard) {
    return slice.call(array, 0, Math.max(0, array.length - (n == null || guard ? 1 : n)));
  };

  // Get the last element of an array. Passing **n** will return the last N
  // values in the array.
  _.last = function(array, n, guard) {
    if (array == null || array.length < 1) return n == null ? void 0 : [];
    if (n == null || guard) return array[array.length - 1];
    return _.rest(array, Math.max(0, array.length - n));
  };

  // Returns everything but the first entry of the array. Aliased as `tail` and `drop`.
  // Especially useful on the arguments object. Passing an **n** will return
  // the rest N values in the array.
  _.rest = _.tail = _.drop = function(array, n, guard) {
    return slice.call(array, n == null || guard ? 1 : n);
  };

  // Trim out all falsy values from an array.
  _.compact = function(array) {
    return _.filter(array, Boolean);
  };

  // Internal implementation of a recursive `flatten` function.
  var flatten = function(input, shallow, strict, output) {
    output = output || [];
    var idx = output.length;
    for (var i = 0, length = getLength(input); i < length; i++) {
      var value = input[i];
      if (isArrayLike(value) && (_.isArray(value) || _.isArguments(value))) {
        // Flatten current level of array or arguments object.
        if (shallow) {
          var j = 0, len = value.length;
          while (j < len) output[idx++] = value[j++];
        } else {
          flatten(value, shallow, strict, output);
          idx = output.length;
        }
      } else if (!strict) {
        output[idx++] = value;
      }
    }
    return output;
  };

  // Flatten out an array, either recursively (by default), or just one level.
  _.flatten = function(array, shallow) {
    return flatten(array, shallow, false);
  };

  // Return a version of the array that does not contain the specified value(s).
  _.without = restArguments(function(array, otherArrays) {
    return _.difference(array, otherArrays);
  });

  // Produce a duplicate-free version of the array. If the array has already
  // been sorted, you have the option of using a faster algorithm.
  // The faster algorithm will not work with an iteratee if the iteratee
  // is not a one-to-one function, so providing an iteratee will disable
  // the faster algorithm.
  // Aliased as `unique`.
  _.uniq = _.unique = function(array, isSorted, iteratee, context) {
    if (!_.isBoolean(isSorted)) {
      context = iteratee;
      iteratee = isSorted;
      isSorted = false;
    }
    if (iteratee != null) iteratee = cb(iteratee, context);
    var result = [];
    var seen = [];
    for (var i = 0, length = getLength(array); i < length; i++) {
      var value = array[i],
          computed = iteratee ? iteratee(value, i, array) : value;
      if (isSorted && !iteratee) {
        if (!i || seen !== computed) result.push(value);
        seen = computed;
      } else if (iteratee) {
        if (!_.contains(seen, computed)) {
          seen.push(computed);
          result.push(value);
        }
      } else if (!_.contains(result, value)) {
        result.push(value);
      }
    }
    return result;
  };

  // Produce an array that contains the union: each distinct element from all of
  // the passed-in arrays.
  _.union = restArguments(function(arrays) {
    return _.uniq(flatten(arrays, true, true));
  });

  // Produce an array that contains every item shared between all the
  // passed-in arrays.
  _.intersection = function(array) {
    var result = [];
    var argsLength = arguments.length;
    for (var i = 0, length = getLength(array); i < length; i++) {
      var item = array[i];
      if (_.contains(result, item)) continue;
      var j;
      for (j = 1; j < argsLength; j++) {
        if (!_.contains(arguments[j], item)) break;
      }
      if (j === argsLength) result.push(item);
    }
    return result;
  };

  // Take the difference between one array and a number of other arrays.
  // Only the elements present in just the first array will remain.
  _.difference = restArguments(function(array, rest) {
    rest = flatten(rest, true, true);
    return _.filter(array, function(value){
      return !_.contains(rest, value);
    });
  });

  // Complement of _.zip. Unzip accepts an array of arrays and groups
  // each array's elements on shared indices.
  _.unzip = function(array) {
    var length = array && _.max(array, getLength).length || 0;
    var result = Array(length);

    for (var index = 0; index < length; index++) {
      result[index] = _.pluck(array, index);
    }
    return result;
  };

  // Zip together multiple lists into a single array -- elements that share
  // an index go together.
  _.zip = restArguments(_.unzip);

  // Converts lists into objects. Pass either a single array of `[key, value]`
  // pairs, or two parallel arrays of the same length -- one of keys, and one of
  // the corresponding values. Passing by pairs is the reverse of _.pairs.
  _.object = function(list, values) {
    var result = {};
    for (var i = 0, length = getLength(list); i < length; i++) {
      if (values) {
        result[list[i]] = values[i];
      } else {
        result[list[i][0]] = list[i][1];
      }
    }
    return result;
  };

  // Generator function to create the findIndex and findLastIndex functions.
  var createPredicateIndexFinder = function(dir) {
    return function(array, predicate, context) {
      predicate = cb(predicate, context);
      var length = getLength(array);
      var index = dir > 0 ? 0 : length - 1;
      for (; index >= 0 && index < length; index += dir) {
        if (predicate(array[index], index, array)) return index;
      }
      return -1;
    };
  };

  // Returns the first index on an array-like that passes a predicate test.
  _.findIndex = createPredicateIndexFinder(1);
  _.findLastIndex = createPredicateIndexFinder(-1);

  // Use a comparator function to figure out the smallest index at which
  // an object should be inserted so as to maintain order. Uses binary search.
  _.sortedIndex = function(array, obj, iteratee, context) {
    iteratee = cb(iteratee, context, 1);
    var value = iteratee(obj);
    var low = 0, high = getLength(array);
    while (low < high) {
      var mid = Math.floor((low + high) / 2);
      if (iteratee(array[mid]) < value) low = mid + 1; else high = mid;
    }
    return low;
  };

  // Generator function to create the indexOf and lastIndexOf functions.
  var createIndexFinder = function(dir, predicateFind, sortedIndex) {
    return function(array, item, idx) {
      var i = 0, length = getLength(array);
      if (typeof idx == 'number') {
        if (dir > 0) {
          i = idx >= 0 ? idx : Math.max(idx + length, i);
        } else {
          length = idx >= 0 ? Math.min(idx + 1, length) : idx + length + 1;
        }
      } else if (sortedIndex && idx && length) {
        idx = sortedIndex(array, item);
        return array[idx] === item ? idx : -1;
      }
      if (item !== item) {
        idx = predicateFind(slice.call(array, i, length), _.isNaN);
        return idx >= 0 ? idx + i : -1;
      }
      for (idx = dir > 0 ? i : length - 1; idx >= 0 && idx < length; idx += dir) {
        if (array[idx] === item) return idx;
      }
      return -1;
    };
  };

  // Return the position of the first occurrence of an item in an array,
  // or -1 if the item is not included in the array.
  // If the array is large and already in sort order, pass `true`
  // for **isSorted** to use binary search.
  _.indexOf = createIndexFinder(1, _.findIndex, _.sortedIndex);
  _.lastIndexOf = createIndexFinder(-1, _.findLastIndex);

  // Generate an integer Array containing an arithmetic progression. A port of
  // the native Python `range()` function. See
  // [the Python documentation](http://docs.python.org/library/functions.html#range).
  _.range = function(start, stop, step) {
    if (stop == null) {
      stop = start || 0;
      start = 0;
    }
    if (!step) {
      step = stop < start ? -1 : 1;
    }

    var length = Math.max(Math.ceil((stop - start) / step), 0);
    var range = Array(length);

    for (var idx = 0; idx < length; idx++, start += step) {
      range[idx] = start;
    }

    return range;
  };

  // Chunk a single array into multiple arrays, each containing `count` or fewer
  // items.
  _.chunk = function(array, count) {
    if (count == null || count < 1) return [];
    var result = [];
    var i = 0, length = array.length;
    while (i < length) {
      result.push(slice.call(array, i, i += count));
    }
    return result;
  };

  // Function (ahem) Functions
  // ------------------

  // Determines whether to execute a function as a constructor
  // or a normal function with the provided arguments.
  var executeBound = function(sourceFunc, boundFunc, context, callingContext, args) {
    if (!(callingContext instanceof boundFunc)) return sourceFunc.apply(context, args);
    var self = baseCreate(sourceFunc.prototype);
    var result = sourceFunc.apply(self, args);
    if (_.isObject(result)) return result;
    return self;
  };

  // Create a function bound to a given object (assigning `this`, and arguments,
  // optionally). Delegates to **ECMAScript 5**'s native `Function.bind` if
  // available.
  _.bind = restArguments(function(func, context, args) {
    if (!_.isFunction(func)) throw new TypeError('Bind must be called on a function');
    var bound = restArguments(function(callArgs) {
      return executeBound(func, bound, context, this, args.concat(callArgs));
    });
    return bound;
  });

  // Partially apply a function by creating a version that has had some of its
  // arguments pre-filled, without changing its dynamic `this` context. _ acts
  // as a placeholder by default, allowing any combination of arguments to be
  // pre-filled. Set `_.partial.placeholder` for a custom placeholder argument.
  _.partial = restArguments(function(func, boundArgs) {
    var placeholder = _.partial.placeholder;
    var bound = function() {
      var position = 0, length = boundArgs.length;
      var args = Array(length);
      for (var i = 0; i < length; i++) {
        args[i] = boundArgs[i] === placeholder ? arguments[position++] : boundArgs[i];
      }
      while (position < arguments.length) args.push(arguments[position++]);
      return executeBound(func, bound, this, this, args);
    };
    return bound;
  });

  _.partial.placeholder = _;

  // Bind a number of an object's methods to that object. Remaining arguments
  // are the method names to be bound. Useful for ensuring that all callbacks
  // defined on an object belong to it.
  _.bindAll = restArguments(function(obj, keys) {
    keys = flatten(keys, false, false);
    var index = keys.length;
    if (index < 1) throw new Error('bindAll must be passed function names');
    while (index--) {
      var key = keys[index];
      obj[key] = _.bind(obj[key], obj);
    }
  });

  // Memoize an expensive function by storing its results.
  _.memoize = function(func, hasher) {
    var memoize = function(key) {
      var cache = memoize.cache;
      var address = '' + (hasher ? hasher.apply(this, arguments) : key);
      if (!has(cache, address)) cache[address] = func.apply(this, arguments);
      return cache[address];
    };
    memoize.cache = {};
    return memoize;
  };

  // Delays a function for the given number of milliseconds, and then calls
  // it with the arguments supplied.
  _.delay = restArguments(function(func, wait, args) {
    return setTimeout(function() {
      return func.apply(null, args);
    }, wait);
  });

  // Defers a function, scheduling it to run after the current call stack has
  // cleared.
  _.defer = _.partial(_.delay, _, 1);

  // Returns a function, that, when invoked, will only be triggered at most once
  // during a given window of time. Normally, the throttled function will run
  // as much as it can, without ever going more than once per `wait` duration;
  // but if you'd like to disable the execution on the leading edge, pass
  // `{leading: false}`. To disable execution on the trailing edge, ditto.
  _.throttle = function(func, wait, options) {
    var timeout, context, args, result;
    var previous = 0;
    if (!options) options = {};

    var later = function() {
      previous = options.leading === false ? 0 : _.now();
      timeout = null;
      result = func.apply(context, args);
      if (!timeout) context = args = null;
    };

    var throttled = function() {
      var now = _.now();
      if (!previous && options.leading === false) previous = now;
      var remaining = wait - (now - previous);
      context = this;
      args = arguments;
      if (remaining <= 0 || remaining > wait) {
        if (timeout) {
          clearTimeout(timeout);
          timeout = null;
        }
        previous = now;
        result = func.apply(context, args);
        if (!timeout) context = args = null;
      } else if (!timeout && options.trailing !== false) {
        timeout = setTimeout(later, remaining);
      }
      return result;
    };

    throttled.cancel = function() {
      clearTimeout(timeout);
      previous = 0;
      timeout = context = args = null;
    };

    return throttled;
  };

  // Returns a function, that, as long as it continues to be invoked, will not
  // be triggered. The function will be called after it stops being called for
  // N milliseconds. If `immediate` is passed, trigger the function on the
  // leading edge, instead of the trailing.
  _.debounce = function(func, wait, immediate) {
    var timeout, result;

    var later = function(context, args) {
      timeout = null;
      if (args) result = func.apply(context, args);
    };

    var debounced = restArguments(function(args) {
      if (timeout) clearTimeout(timeout);
      if (immediate) {
        var callNow = !timeout;
        timeout = setTimeout(later, wait);
        if (callNow) result = func.apply(this, args);
      } else {
        timeout = _.delay(later, wait, this, args);
      }

      return result;
    });

    debounced.cancel = function() {
      clearTimeout(timeout);
      timeout = null;
    };

    return debounced;
  };

  // Returns the first function passed as an argument to the second,
  // allowing you to adjust arguments, run code before and after, and
  // conditionally execute the original function.
  _.wrap = function(func, wrapper) {
    return _.partial(wrapper, func);
  };

  // Returns a negated version of the passed-in predicate.
  _.negate = function(predicate) {
    return function() {
      return !predicate.apply(this, arguments);
    };
  };

  // Returns a function that is the composition of a list of functions, each
  // consuming the return value of the function that follows.
  _.compose = function() {
    var args = arguments;
    var start = args.length - 1;
    return function() {
      var i = start;
      var result = args[start].apply(this, arguments);
      while (i--) result = args[i].call(this, result);
      return result;
    };
  };

  // Returns a function that will only be executed on and after the Nth call.
  _.after = function(times, func) {
    return function() {
      if (--times < 1) {
        return func.apply(this, arguments);
      }
    };
  };

  // Returns a function that will only be executed up to (but not including) the Nth call.
  _.before = function(times, func) {
    var memo;
    return function() {
      if (--times > 0) {
        memo = func.apply(this, arguments);
      }
      if (times <= 1) func = null;
      return memo;
    };
  };

  // Returns a function that will be executed at most one time, no matter how
  // often you call it. Useful for lazy initialization.
  _.once = _.partial(_.before, 2);

  _.restArguments = restArguments;

  // Object Functions
  // ----------------

  // Keys in IE < 9 that won't be iterated by `for key in ...` and thus missed.
  var hasEnumBug = !{toString: null}.propertyIsEnumerable('toString');
  var nonEnumerableProps = ['valueOf', 'isPrototypeOf', 'toString',
    'propertyIsEnumerable', 'hasOwnProperty', 'toLocaleString'];

  var collectNonEnumProps = function(obj, keys) {
    var nonEnumIdx = nonEnumerableProps.length;
    var constructor = obj.constructor;
    var proto = _.isFunction(constructor) && constructor.prototype || ObjProto;

    // Constructor is a special case.
    var prop = 'constructor';
    if (has(obj, prop) && !_.contains(keys, prop)) keys.push(prop);

    while (nonEnumIdx--) {
      prop = nonEnumerableProps[nonEnumIdx];
      if (prop in obj && obj[prop] !== proto[prop] && !_.contains(keys, prop)) {
        keys.push(prop);
      }
    }
  };

  // Retrieve the names of an object's own properties.
  // Delegates to **ECMAScript 5**'s native `Object.keys`.
  _.keys = function(obj) {
    if (!_.isObject(obj)) return [];
    if (nativeKeys) return nativeKeys(obj);
    var keys = [];
    for (var key in obj) if (has(obj, key)) keys.push(key);
    // Ahem, IE < 9.
    if (hasEnumBug) collectNonEnumProps(obj, keys);
    return keys;
  };

  // Retrieve all the property names of an object.
  _.allKeys = function(obj) {
    if (!_.isObject(obj)) return [];
    var keys = [];
    for (var key in obj) keys.push(key);
    // Ahem, IE < 9.
    if (hasEnumBug) collectNonEnumProps(obj, keys);
    return keys;
  };

  // Retrieve the values of an object's properties.
  _.values = function(obj) {
    var keys = _.keys(obj);
    var length = keys.length;
    var values = Array(length);
    for (var i = 0; i < length; i++) {
      values[i] = obj[keys[i]];
    }
    return values;
  };

  // Returns the results of applying the iteratee to each element of the object.
  // In contrast to _.map it returns an object.
  _.mapObject = function(obj, iteratee, context) {
    iteratee = cb(iteratee, context);
    var keys = _.keys(obj),
        length = keys.length,
        results = {};
    for (var index = 0; index < length; index++) {
      var currentKey = keys[index];
      results[currentKey] = iteratee(obj[currentKey], currentKey, obj);
    }
    return results;
  };

  // Convert an object into a list of `[key, value]` pairs.
  // The opposite of _.object.
  _.pairs = function(obj) {
    var keys = _.keys(obj);
    var length = keys.length;
    var pairs = Array(length);
    for (var i = 0; i < length; i++) {
      pairs[i] = [keys[i], obj[keys[i]]];
    }
    return pairs;
  };

  // Invert the keys and values of an object. The values must be serializable.
  _.invert = function(obj) {
    var result = {};
    var keys = _.keys(obj);
    for (var i = 0, length = keys.length; i < length; i++) {
      result[obj[keys[i]]] = keys[i];
    }
    return result;
  };

  // Return a sorted list of the function names available on the object.
  // Aliased as `methods`.
  _.functions = _.methods = function(obj) {
    var names = [];
    for (var key in obj) {
      if (_.isFunction(obj[key])) names.push(key);
    }
    return names.sort();
  };

  // An internal function for creating assigner functions.
  var createAssigner = function(keysFunc, defaults) {
    return function(obj) {
      var length = arguments.length;
      if (defaults) obj = Object(obj);
      if (length < 2 || obj == null) return obj;
      for (var index = 1; index < length; index++) {
        var source = arguments[index],
            keys = keysFunc(source),
            l = keys.length;
        for (var i = 0; i < l; i++) {
          var key = keys[i];
          if (!defaults || obj[key] === void 0) obj[key] = source[key];
        }
      }
      return obj;
    };
  };

  // Extend a given object with all the properties in passed-in object(s).
  _.extend = createAssigner(_.allKeys);

  // Assigns a given object with all the own properties in the passed-in object(s).
  // (https://developer.mozilla.org/docs/Web/JavaScript/Reference/Global_Objects/Object/assign)
  _.extendOwn = _.assign = createAssigner(_.keys);

  // Returns the first key on an object that passes a predicate test.
  _.findKey = function(obj, predicate, context) {
    predicate = cb(predicate, context);
    var keys = _.keys(obj), key;
    for (var i = 0, length = keys.length; i < length; i++) {
      key = keys[i];
      if (predicate(obj[key], key, obj)) return key;
    }
  };

  // Internal pick helper function to determine if `obj` has key `key`.
  var keyInObj = function(value, key, obj) {
    return key in obj;
  };

  // Return a copy of the object only containing the whitelisted properties.
  _.pick = restArguments(function(obj, keys) {
    var result = {}, iteratee = keys[0];
    if (obj == null) return result;
    if (_.isFunction(iteratee)) {
      if (keys.length > 1) iteratee = optimizeCb(iteratee, keys[1]);
      keys = _.allKeys(obj);
    } else {
      iteratee = keyInObj;
      keys = flatten(keys, false, false);
      obj = Object(obj);
    }
    for (var i = 0, length = keys.length; i < length; i++) {
      var key = keys[i];
      var value = obj[key];
      if (iteratee(value, key, obj)) result[key] = value;
    }
    return result;
  });

  // Return a copy of the object without the blacklisted properties.
  _.omit = restArguments(function(obj, keys) {
    var iteratee = keys[0], context;
    if (_.isFunction(iteratee)) {
      iteratee = _.negate(iteratee);
      if (keys.length > 1) context = keys[1];
    } else {
      keys = _.map(flatten(keys, false, false), String);
      iteratee = function(value, key) {
        return !_.contains(keys, key);
      };
    }
    return _.pick(obj, iteratee, context);
  });

  // Fill in a given object with default properties.
  _.defaults = createAssigner(_.allKeys, true);

  // Creates an object that inherits from the given prototype object.
  // If additional properties are provided then they will be added to the
  // created object.
  _.create = function(prototype, props) {
    var result = baseCreate(prototype);
    if (props) _.extendOwn(result, props);
    return result;
  };

  // Create a (shallow-cloned) duplicate of an object.
  _.clone = function(obj) {
    if (!_.isObject(obj)) return obj;
    return _.isArray(obj) ? obj.slice() : _.extend({}, obj);
  };

  // Invokes interceptor with the obj, and then returns obj.
  // The primary purpose of this method is to "tap into" a method chain, in
  // order to perform operations on intermediate results within the chain.
  _.tap = function(obj, interceptor) {
    interceptor(obj);
    return obj;
  };

  // Returns whether an object has a given set of `key:value` pairs.
  _.isMatch = function(object, attrs) {
    var keys = _.keys(attrs), length = keys.length;
    if (object == null) return !length;
    var obj = Object(object);
    for (var i = 0; i < length; i++) {
      var key = keys[i];
      if (attrs[key] !== obj[key] || !(key in obj)) return false;
    }
    return true;
  };


  // Internal recursive comparison function for `isEqual`.
  var eq, deepEq;
  eq = function(a, b, aStack, bStack) {
    // Identical objects are equal. `0 === -0`, but they aren't identical.
    // See the [Harmony `egal` proposal](http://wiki.ecmascript.org/doku.php?id=harmony:egal).
    if (a === b) return a !== 0 || 1 / a === 1 / b;
    // `null` or `undefined` only equal to itself (strict comparison).
    if (a == null || b == null) return false;
    // `NaN`s are equivalent, but non-reflexive.
    if (a !== a) return b !== b;
    // Exhaust primitive checks
    var type = typeof a;
    if (type !== 'function' && type !== 'object' && typeof b != 'object') return false;
    return deepEq(a, b, aStack, bStack);
  };

  // Internal recursive comparison function for `isEqual`.
  deepEq = function(a, b, aStack, bStack) {
    // Unwrap any wrapped objects.
    if (a instanceof _) a = a._wrapped;
    if (b instanceof _) b = b._wrapped;
    // Compare `[[Class]]` names.
    var className = toString.call(a);
    if (className !== toString.call(b)) return false;
    switch (className) {
      // Strings, numbers, regular expressions, dates, and booleans are compared by value.
      case '[object RegExp]':
      // RegExps are coerced to strings for comparison (Note: '' + /a/i === '/a/i')
      case '[object String]':
        // Primitives and their corresponding object wrappers are equivalent; thus, `"5"` is
        // equivalent to `new String("5")`.
        return '' + a === '' + b;
      case '[object Number]':
        // `NaN`s are equivalent, but non-reflexive.
        // Object(NaN) is equivalent to NaN.
        if (+a !== +a) return +b !== +b;
        // An `egal` comparison is performed for other numeric values.
        return +a === 0 ? 1 / +a === 1 / b : +a === +b;
      case '[object Date]':
      case '[object Boolean]':
        // Coerce dates and booleans to numeric primitive values. Dates are compared by their
        // millisecond representations. Note that invalid dates with millisecond representations
        // of `NaN` are not equivalent.
        return +a === +b;
      case '[object Symbol]':
        return SymbolProto.valueOf.call(a) === SymbolProto.valueOf.call(b);
    }

    var areArrays = className === '[object Array]';
    if (!areArrays) {
      if (typeof a != 'object' || typeof b != 'object') return false;

      // Objects with different constructors are not equivalent, but `Object`s or `Array`s
      // from different frames are.
      var aCtor = a.constructor, bCtor = b.constructor;
      if (aCtor !== bCtor && !(_.isFunction(aCtor) && aCtor instanceof aCtor &&
                               _.isFunction(bCtor) && bCtor instanceof bCtor)
                          && ('constructor' in a && 'constructor' in b)) {
        return false;
      }
    }
    // Assume equality for cyclic structures. The algorithm for detecting cyclic
    // structures is adapted from ES 5.1 section 15.12.3, abstract operation `JO`.

    // Initializing stack of traversed objects.
    // It's done here since we only need them for objects and arrays comparison.
    aStack = aStack || [];
    bStack = bStack || [];
    var length = aStack.length;
    while (length--) {
      // Linear search. Performance is inversely proportional to the number of
      // unique nested structures.
      if (aStack[length] === a) return bStack[length] === b;
    }

    // Add the first object to the stack of traversed objects.
    aStack.push(a);
    bStack.push(b);

    // Recursively compare objects and arrays.
    if (areArrays) {
      // Compare array lengths to determine if a deep comparison is necessary.
      length = a.length;
      if (length !== b.length) return false;
      // Deep compare the contents, ignoring non-numeric properties.
      while (length--) {
        if (!eq(a[length], b[length], aStack, bStack)) return false;
      }
    } else {
      // Deep compare objects.
      var keys = _.keys(a), key;
      length = keys.length;
      // Ensure that both objects contain the same number of properties before comparing deep equality.
      if (_.keys(b).length !== length) return false;
      while (length--) {
        // Deep compare each member
        key = keys[length];
        if (!(has(b, key) && eq(a[key], b[key], aStack, bStack))) return false;
      }
    }
    // Remove the first object from the stack of traversed objects.
    aStack.pop();
    bStack.pop();
    return true;
  };

  // Perform a deep comparison to check if two objects are equal.
  _.isEqual = function(a, b) {
    return eq(a, b);
  };

  // Is a given array, string, or object empty?
  // An "empty" object has no enumerable own-properties.
  _.isEmpty = function(obj) {
    if (obj == null) return true;
    if (isArrayLike(obj) && (_.isArray(obj) || _.isString(obj) || _.isArguments(obj))) return obj.length === 0;
    return _.keys(obj).length === 0;
  };

  // Is a given value a DOM element?
  _.isElement = function(obj) {
    return !!(obj && obj.nodeType === 1);
  };

  // Is a given value an array?
  // Delegates to ECMA5's native Array.isArray
  _.isArray = nativeIsArray || function(obj) {
    return toString.call(obj) === '[object Array]';
  };

  // Is a given variable an object?
  _.isObject = function(obj) {
    var type = typeof obj;
    return type === 'function' || type === 'object' && !!obj;
  };

  // Add some isType methods: isArguments, isFunction, isString, isNumber, isDate, isRegExp, isError, isMap, isWeakMap, isSet, isWeakSet.
  _.each(['Arguments', 'Function', 'String', 'Number', 'Date', 'RegExp', 'Error', 'Symbol', 'Map', 'WeakMap', 'Set', 'WeakSet'], function(name) {
    _['is' + name] = function(obj) {
      return toString.call(obj) === '[object ' + name + ']';
    };
  });

  // Define a fallback version of the method in browsers (ahem, IE < 9), where
  // there isn't any inspectable "Arguments" type.
  if (!_.isArguments(arguments)) {
    _.isArguments = function(obj) {
      return has(obj, 'callee');
    };
  }

  // Optimize `isFunction` if appropriate. Work around some typeof bugs in old v8,
  // IE 11 (#1621), Safari 8 (#1929), and PhantomJS (#2236).
  var nodelist = root.document && root.document.childNodes;
  if (typeof /./ != 'function' && typeof Int8Array != 'object' && typeof nodelist != 'function') {
    _.isFunction = function(obj) {
      return typeof obj == 'function' || false;
    };
  }

  // Is a given object a finite number?
  _.isFinite = function(obj) {
    return !_.isSymbol(obj) && isFinite(obj) && !isNaN(parseFloat(obj));
  };

  // Is the given value `NaN`?
  _.isNaN = function(obj) {
    return _.isNumber(obj) && isNaN(obj);
  };

  // Is a given value a boolean?
  _.isBoolean = function(obj) {
    return obj === true || obj === false || toString.call(obj) === '[object Boolean]';
  };

  // Is a given value equal to null?
  _.isNull = function(obj) {
    return obj === null;
  };

  // Is a given variable undefined?
  _.isUndefined = function(obj) {
    return obj === void 0;
  };

  // Shortcut function for checking if an object has a given property directly
  // on itself (in other words, not on a prototype).
  _.has = function(obj, path) {
    if (!_.isArray(path)) {
      return has(obj, path);
    }
    var length = path.length;
    for (var i = 0; i < length; i++) {
      var key = path[i];
      if (obj == null || !hasOwnProperty.call(obj, key)) {
        return false;
      }
      obj = obj[key];
    }
    return !!length;
  };

  // Utility Functions
  // -----------------

  // Run Underscore.js in *noConflict* mode, returning the `_` variable to its
  // previous owner. Returns a reference to the Underscore object.
  _.noConflict = function() {
    root._ = previousUnderscore;
    return this;
  };

  // Keep the identity function around for default iteratees.
  _.identity = function(value) {
    return value;
  };

  // Predicate-generating functions. Often useful outside of Underscore.
  _.constant = function(value) {
    return function() {
      return value;
    };
  };

  _.noop = function(){};

  // Creates a function that, when passed an object, will traverse that object’s
  // properties down the given `path`, specified as an array of keys or indexes.
  _.property = function(path) {
    if (!_.isArray(path)) {
      return shallowProperty(path);
    }
    return function(obj) {
      return deepGet(obj, path);
    };
  };

  // Generates a function for a given object that returns a given property.
  _.propertyOf = function(obj) {
    if (obj == null) {
      return function(){};
    }
    return function(path) {
      return !_.isArray(path) ? obj[path] : deepGet(obj, path);
    };
  };

  // Returns a predicate for checking whether an object has a given set of
  // `key:value` pairs.
  _.matcher = _.matches = function(attrs) {
    attrs = _.extendOwn({}, attrs);
    return function(obj) {
      return _.isMatch(obj, attrs);
    };
  };

  // Run a function **n** times.
  _.times = function(n, iteratee, context) {
    var accum = Array(Math.max(0, n));
    iteratee = optimizeCb(iteratee, context, 1);
    for (var i = 0; i < n; i++) accum[i] = iteratee(i);
    return accum;
  };

  // Return a random integer between min and max (inclusive).
  _.random = function(min, max) {
    if (max == null) {
      max = min;
      min = 0;
    }
    return min + Math.floor(Math.random() * (max - min + 1));
  };

  // A (possibly faster) way to get the current timestamp as an integer.
  _.now = Date.now || function() {
    return new Date().getTime();
  };

  // List of HTML entities for escaping.
  var escapeMap = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#x27;',
    '`': '&#x60;'
  };
  var unescapeMap = _.invert(escapeMap);

  // Functions for escaping and unescaping strings to/from HTML interpolation.
  var createEscaper = function(map) {
    var escaper = function(match) {
      return map[match];
    };
    // Regexes for identifying a key that needs to be escaped.
    var source = '(?:' + _.keys(map).join('|') + ')';
    var testRegexp = RegExp(source);
    var replaceRegexp = RegExp(source, 'g');
    return function(string) {
      string = string == null ? '' : '' + string;
      return testRegexp.test(string) ? string.replace(replaceRegexp, escaper) : string;
    };
  };
  _.escape = createEscaper(escapeMap);
  _.unescape = createEscaper(unescapeMap);

  // Traverses the children of `obj` along `path`. If a child is a function, it
  // is invoked with its parent as context. Returns the value of the final
  // child, or `fallback` if any child is undefined.
  _.result = function(obj, path, fallback) {
    if (!_.isArray(path)) path = [path];
    var length = path.length;
    if (!length) {
      return _.isFunction(fallback) ? fallback.call(obj) : fallback;
    }
    for (var i = 0; i < length; i++) {
      var prop = obj == null ? void 0 : obj[path[i]];
      if (prop === void 0) {
        prop = fallback;
        i = length; // Ensure we don't continue iterating.
      }
      obj = _.isFunction(prop) ? prop.call(obj) : prop;
    }
    return obj;
  };

  // Generate a unique integer id (unique within the entire client session).
  // Useful for temporary DOM ids.
  var idCounter = 0;
  _.uniqueId = function(prefix) {
    var id = ++idCounter + '';
    return prefix ? prefix + id : id;
  };

  // By default, Underscore uses ERB-style template delimiters, change the
  // following template settings to use alternative delimiters.
  _.templateSettings = {
    evaluate: /<%([\s\S]+?)%>/g,
    interpolate: /<%=([\s\S]+?)%>/g,
    escape: /<%-([\s\S]+?)%>/g
  };

  // When customizing `templateSettings`, if you don't want to define an
  // interpolation, evaluation or escaping regex, we need one that is
  // guaranteed not to match.
  var noMatch = /(.)^/;

  // Certain characters need to be escaped so that they can be put into a
  // string literal.
  var escapes = {
    "'": "'",
    '\\': '\\',
    '\r': 'r',
    '\n': 'n',
    '\u2028': 'u2028',
    '\u2029': 'u2029'
  };

  var escapeRegExp = /\\|'|\r|\n|\u2028|\u2029/g;

  var escapeChar = function(match) {
    return '\\' + escapes[match];
  };

  // JavaScript micro-templating, similar to John Resig's implementation.
  // Underscore templating handles arbitrary delimiters, preserves whitespace,
  // and correctly escapes quotes within interpolated code.
  // NB: `oldSettings` only exists for backwards compatibility.
  _.template = function(text, settings, oldSettings) {
    if (!settings && oldSettings) settings = oldSettings;
    settings = _.defaults({}, settings, _.templateSettings);

    // Combine delimiters into one regular expression via alternation.
    var matcher = RegExp([
      (settings.escape || noMatch).source,
      (settings.interpolate || noMatch).source,
      (settings.evaluate || noMatch).source
    ].join('|') + '|$', 'g');

    // Compile the template source, escaping string literals appropriately.
    var index = 0;
    var source = "__p+='";
    text.replace(matcher, function(match, escape, interpolate, evaluate, offset) {
      source += text.slice(index, offset).replace(escapeRegExp, escapeChar);
      index = offset + match.length;

      if (escape) {
        source += "'+\n((__t=(" + escape + "))==null?'':_.escape(__t))+\n'";
      } else if (interpolate) {
        source += "'+\n((__t=(" + interpolate + "))==null?'':__t)+\n'";
      } else if (evaluate) {
        source += "';\n" + evaluate + "\n__p+='";
      }

      // Adobe VMs need the match returned to produce the correct offset.
      return match;
    });
    source += "';\n";

    // If a variable is not specified, place data values in local scope.
    if (!settings.variable) source = 'with(obj||{}){\n' + source + '}\n';

    source = "var __t,__p='',__j=Array.prototype.join," +
      "print=function(){__p+=__j.call(arguments,'');};\n" +
      source + 'return __p;\n';

    var render;
    try {
      render = new Function(settings.variable || 'obj', '_', source);
    } catch (e) {
      e.source = source;
      throw e;
    }

    var template = function(data) {
      return render.call(this, data, _);
    };

    // Provide the compiled source as a convenience for precompilation.
    var argument = settings.variable || 'obj';
    template.source = 'function(' + argument + '){\n' + source + '}';

    return template;
  };

  // Add a "chain" function. Start chaining a wrapped Underscore object.
  _.chain = function(obj) {
    var instance = _(obj);
    instance._chain = true;
    return instance;
  };

  // OOP
  // ---------------
  // If Underscore is called as a function, it returns a wrapped object that
  // can be used OO-style. This wrapper holds altered versions of all the
  // underscore functions. Wrapped objects may be chained.

  // Helper function to continue chaining intermediate results.
  var chainResult = function(instance, obj) {
    return instance._chain ? _(obj).chain() : obj;
  };

  // Add your own custom functions to the Underscore object.
  _.mixin = function(obj) {
    _.each(_.functions(obj), function(name) {
      var func = _[name] = obj[name];
      _.prototype[name] = function() {
        var args = [this._wrapped];
        push.apply(args, arguments);
        return chainResult(this, func.apply(_, args));
      };
    });
    return _;
  };

  // Add all of the Underscore functions to the wrapper object.
  _.mixin(_);

  // Add all mutator Array functions to the wrapper.
  _.each(['pop', 'push', 'reverse', 'shift', 'sort', 'splice', 'unshift'], function(name) {
    var method = ArrayProto[name];
    _.prototype[name] = function() {
      var obj = this._wrapped;
      method.apply(obj, arguments);
      if ((name === 'shift' || name === 'splice') && obj.length === 0) delete obj[0];
      return chainResult(this, obj);
    };
  });

  // Add all accessor Array functions to the wrapper.
  _.each(['concat', 'join', 'slice'], function(name) {
    var method = ArrayProto[name];
    _.prototype[name] = function() {
      return chainResult(this, method.apply(this._wrapped, arguments));
    };
  });

  // Extracts the result from a wrapped and chained object.
  _.prototype.value = function() {
    return this._wrapped;
  };

  // Provide unwrapping proxy for some methods used in engine operations
  // such as arithmetic and JSON stringification.
  _.prototype.valueOf = _.prototype.toJSON = _.prototype.value;

  _.prototype.toString = function() {
    return String(this._wrapped);
  };

  // AMD registration happens at the end for compatibility with AMD loaders
  // that may not enforce next-turn semantics on modules. Even though general
  // practice for AMD registration is to be anonymous, underscore registers
  // as a named module because, like jQuery, it is a base library that is
  // popular enough to be bundled in a third party lib, but not be part of
  // an AMD load request. Those cases could generate an error when an
  // anonymous define() is called outside of a loader request.
  if (typeof define == 'function' && define.amd) {
    define('underscore', [], function() {
      return _;
    });
  }
}());
//     Backbone.js 1.3.3

//     (c) 2010-2016 Jeremy Ashkenas, DocumentCloud and Investigative Reporters & Editors
//     Backbone may be freely distributed under the MIT license.
//     For all details and documentation:
//     http://backbonejs.org

(function(factory) {

  // Establish the root object, `window` (`self`) in the browser, or `global` on the server.
  // We use `self` instead of `window` for `WebWorker` support.
  var root = (typeof self == 'object' && self.self === self && self) ||
            (typeof global == 'object' && global.global === global && global);

  // Set up Backbone appropriately for the environment. Start with AMD.
  if (typeof define === 'function' && define.amd) {
    define(['underscore', 'jquery', 'exports'], function(_, $, exports) {
      // Export global even in AMD case in case this script is loaded with
      // others that may still expect a global Backbone.
      root.Backbone = factory(root, exports, _, $);
    });

  // Next for Node.js or CommonJS. jQuery may not be needed as a module.
  } else if (typeof exports !== 'undefined') {
    var _ = require('underscore'), $;
    try { $ = require('jquery'); } catch (e) {}
    factory(root, exports, _, $);

  // Finally, as a browser global.
  } else {
    root.Backbone = factory(root, {}, root._, (root.jQuery || root.Zepto || root.ender || root.$));
  }

})(function(root, Backbone, _, $) {

  // Initial Setup
  // -------------

  // Save the previous value of the `Backbone` variable, so that it can be
  // restored later on, if `noConflict` is used.
  var previousBackbone = root.Backbone;

  // Create a local reference to a common array method we'll want to use later.
  var slice = Array.prototype.slice;

  // Current version of the library. Keep in sync with `package.json`.
  Backbone.VERSION = '1.3.3';

  // For Backbone's purposes, jQuery, Zepto, Ender, or My Library (kidding) owns
  // the `$` variable.
  Backbone.$ = $;

  // Runs Backbone.js in *noConflict* mode, returning the `Backbone` variable
  // to its previous owner. Returns a reference to this Backbone object.
  Backbone.noConflict = function() {
    root.Backbone = previousBackbone;
    return this;
  };

  // Turn on `emulateHTTP` to support legacy HTTP servers. Setting this option
  // will fake `"PATCH"`, `"PUT"` and `"DELETE"` requests via the `_method` parameter and
  // set a `X-Http-Method-Override` header.
  Backbone.emulateHTTP = false;

  // Turn on `emulateJSON` to support legacy servers that can't deal with direct
  // `application/json` requests ... this will encode the body as
  // `application/x-www-form-urlencoded` instead and will send the model in a
  // form param named `model`.
  Backbone.emulateJSON = false;

  // Proxy Backbone class methods to Underscore functions, wrapping the model's
  // `attributes` object or collection's `models` array behind the scenes.
  //
  // collection.filter(function(model) { return model.get('age') > 10 });
  // collection.each(this.addView);
  //
  // `Function#apply` can be slow so we use the method's arg count, if we know it.
  var addMethod = function(length, method, attribute) {
    switch (length) {
      case 1: return function() {
        return _[method](this[attribute]);
      };
      case 2: return function(value) {
        return _[method](this[attribute], value);
      };
      case 3: return function(iteratee, context) {
        return _[method](this[attribute], cb(iteratee, this), context);
      };
      case 4: return function(iteratee, defaultVal, context) {
        return _[method](this[attribute], cb(iteratee, this), defaultVal, context);
      };
      default: return function() {
        var args = slice.call(arguments);
        args.unshift(this[attribute]);
        return _[method].apply(_, args);
      };
    }
  };
  var addUnderscoreMethods = function(Class, methods, attribute) {
    _.each(methods, function(length, method) {
      if (_[method]) Class.prototype[method] = addMethod(length, method, attribute);
    });
  };

  // Support `collection.sortBy('attr')` and `collection.findWhere({id: 1})`.
  var cb = function(iteratee, instance) {
    if (_.isFunction(iteratee)) return iteratee;
    if (_.isObject(iteratee) && !instance._isModel(iteratee)) return modelMatcher(iteratee);
    if (_.isString(iteratee)) return function(model) { return model.get(iteratee); };
    return iteratee;
  };
  var modelMatcher = function(attrs) {
    var matcher = _.matches(attrs);
    return function(model) {
      return matcher(model.attributes);
    };
  };

  // Backbone.Events
  // ---------------

  // A module that can be mixed in to *any object* in order to provide it with
  // a custom event channel. You may bind a callback to an event with `on` or
  // remove with `off`; `trigger`-ing an event fires all callbacks in
  // succession.
  //
  //     var object = {};
  //     _.extend(object, Backbone.Events);
  //     object.on('expand', function(){ alert('expanded'); });
  //     object.trigger('expand');
  //
  var Events = Backbone.Events = {};

  // Regular expression used to split event strings.
  var eventSplitter = /\s+/;

  // Iterates over the standard `event, callback` (as well as the fancy multiple
  // space-separated events `"change blur", callback` and jQuery-style event
  // maps `{event: callback}`).
  var eventsApi = function(iteratee, events, name, callback, opts) {
    var i = 0, names;
    if (name && typeof name === 'object') {
      // Handle event maps.
      if (callback !== void 0 && 'context' in opts && opts.context === void 0) opts.context = callback;
      for (names = _.keys(name); i < names.length ; i++) {
        events = eventsApi(iteratee, events, names[i], name[names[i]], opts);
      }
    } else if (name && eventSplitter.test(name)) {
      // Handle space-separated event names by delegating them individually.
      for (names = name.split(eventSplitter); i < names.length; i++) {
        events = iteratee(events, names[i], callback, opts);
      }
    } else {
      // Finally, standard events.
      events = iteratee(events, name, callback, opts);
    }
    return events;
  };

  // Bind an event to a `callback` function. Passing `"all"` will bind
  // the callback to all events fired.
  Events.on = function(name, callback, context) {
    return internalOn(this, name, callback, context);
  };

  // Guard the `listening` argument from the public API.
  var internalOn = function(obj, name, callback, context, listening) {
    obj._events = eventsApi(onApi, obj._events || {}, name, callback, {
      context: context,
      ctx: obj,
      listening: listening
    });

    if (listening) {
      var listeners = obj._listeners || (obj._listeners = {});
      listeners[listening.id] = listening;
    }

    return obj;
  };

  // Inversion-of-control versions of `on`. Tell *this* object to listen to
  // an event in another object... keeping track of what it's listening to
  // for easier unbinding later.
  Events.listenTo = function(obj, name, callback) {
    if (!obj) return this;
    var id = obj._listenId || (obj._listenId = _.uniqueId('l'));
    var listeningTo = this._listeningTo || (this._listeningTo = {});
    var listening = listeningTo[id];

    // This object is not listening to any other events on `obj` yet.
    // Setup the necessary references to track the listening callbacks.
    if (!listening) {
      var thisId = this._listenId || (this._listenId = _.uniqueId('l'));
      listening = listeningTo[id] = {obj: obj, objId: id, id: thisId, listeningTo: listeningTo, count: 0};
    }

    // Bind callbacks on obj, and keep track of them on listening.
    internalOn(obj, name, callback, this, listening);
    return this;
  };

  // The reducing API that adds a callback to the `events` object.
  var onApi = function(events, name, callback, options) {
    if (callback) {
      var handlers = events[name] || (events[name] = []);
      var context = options.context, ctx = options.ctx, listening = options.listening;
      if (listening) listening.count++;

      handlers.push({callback: callback, context: context, ctx: context || ctx, listening: listening});
    }
    return events;
  };

  // Remove one or many callbacks. If `context` is null, removes all
  // callbacks with that function. If `callback` is null, removes all
  // callbacks for the event. If `name` is null, removes all bound
  // callbacks for all events.
  Events.off = function(name, callback, context) {
    if (!this._events) return this;
    this._events = eventsApi(offApi, this._events, name, callback, {
      context: context,
      listeners: this._listeners
    });
    return this;
  };

  // Tell this object to stop listening to either specific events ... or
  // to every object it's currently listening to.
  Events.stopListening = function(obj, name, callback) {
    var listeningTo = this._listeningTo;
    if (!listeningTo) return this;

    var ids = obj ? [obj._listenId] : _.keys(listeningTo);

    for (var i = 0; i < ids.length; i++) {
      var listening = listeningTo[ids[i]];

      // If listening doesn't exist, this object is not currently
      // listening to obj. Break out early.
      if (!listening) break;

      listening.obj.off(name, callback, this);
    }

    return this;
  };

  // The reducing API that removes a callback from the `events` object.
  var offApi = function(events, name, callback, options) {
    if (!events) return;

    var i = 0, listening;
    var context = options.context, listeners = options.listeners;

    // Delete all events listeners and "drop" events.
    if (!name && !callback && !context) {
      var ids = _.keys(listeners);
      for (; i < ids.length; i++) {
        listening = listeners[ids[i]];
        delete listeners[listening.id];
        delete listening.listeningTo[listening.objId];
      }
      return;
    }

    var names = name ? [name] : _.keys(events);
    for (; i < names.length; i++) {
      name = names[i];
      var handlers = events[name];

      // Bail out if there are no events stored.
      if (!handlers) break;

      // Replace events if there are any remaining.  Otherwise, clean up.
      var remaining = [];
      for (var j = 0; j < handlers.length; j++) {
        var handler = handlers[j];
        if (
          callback && callback !== handler.callback &&
            callback !== handler.callback._callback ||
              context && context !== handler.context
        ) {
          remaining.push(handler);
        } else {
          listening = handler.listening;
          if (listening && --listening.count === 0) {
            delete listeners[listening.id];
            delete listening.listeningTo[listening.objId];
          }
        }
      }

      // Update tail event if the list has any events.  Otherwise, clean up.
      if (remaining.length) {
        events[name] = remaining;
      } else {
        delete events[name];
      }
    }
    return events;
  };

  // Bind an event to only be triggered a single time. After the first time
  // the callback is invoked, its listener will be removed. If multiple events
  // are passed in using the space-separated syntax, the handler will fire
  // once for each event, not once for a combination of all events.
  Events.once = function(name, callback, context) {
    // Map the event into a `{event: once}` object.
    var events = eventsApi(onceMap, {}, name, callback, _.bind(this.off, this));
    if (typeof name === 'string' && context == null) callback = void 0;
    return this.on(events, callback, context);
  };

  // Inversion-of-control versions of `once`.
  Events.listenToOnce = function(obj, name, callback) {
    // Map the event into a `{event: once}` object.
    var events = eventsApi(onceMap, {}, name, callback, _.bind(this.stopListening, this, obj));
    return this.listenTo(obj, events);
  };

  // Reduces the event callbacks into a map of `{event: onceWrapper}`.
  // `offer` unbinds the `onceWrapper` after it has been called.
  var onceMap = function(map, name, callback, offer) {
    if (callback) {
      var once = map[name] = _.once(function() {
        offer(name, once);
        callback.apply(this, arguments);
      });
      once._callback = callback;
    }
    return map;
  };

  // Trigger one or many events, firing all bound callbacks. Callbacks are
  // passed the same arguments as `trigger` is, apart from the event name
  // (unless you're listening on `"all"`, which will cause your callback to
  // receive the true name of the event as the first argument).
  Events.trigger = function(name) {
    if (!this._events) return this;

    var length = Math.max(0, arguments.length - 1);
    var args = Array(length);
    for (var i = 0; i < length; i++) args[i] = arguments[i + 1];

    eventsApi(triggerApi, this._events, name, void 0, args);
    return this;
  };

  // Handles triggering the appropriate event callbacks.
  var triggerApi = function(objEvents, name, callback, args) {
    if (objEvents) {
      var events = objEvents[name];
      var allEvents = objEvents.all;
      if (events && allEvents) allEvents = allEvents.slice();
      if (events) triggerEvents(events, args);
      if (allEvents) triggerEvents(allEvents, [name].concat(args));
    }
    return objEvents;
  };

  // A difficult-to-believe, but optimized internal dispatch function for
  // triggering events. Tries to keep the usual cases speedy (most internal
  // Backbone events have 3 arguments).
  var triggerEvents = function(events, args) {
    var ev, i = -1, l = events.length, a1 = args[0], a2 = args[1], a3 = args[2];
    switch (args.length) {
      case 0: while (++i < l) (ev = events[i]).callback.call(ev.ctx); return;
      case 1: while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1); return;
      case 2: while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1, a2); return;
      case 3: while (++i < l) (ev = events[i]).callback.call(ev.ctx, a1, a2, a3); return;
      default: while (++i < l) (ev = events[i]).callback.apply(ev.ctx, args); return;
    }
  };

  // Aliases for backwards compatibility.
  Events.bind   = Events.on;
  Events.unbind = Events.off;

  // Allow the `Backbone` object to serve as a global event bus, for folks who
  // want global "pubsub" in a convenient place.
  _.extend(Backbone, Events);

  // Backbone.Model
  // --------------

  // Backbone **Models** are the basic data object in the framework --
  // frequently representing a row in a table in a database on your server.
  // A discrete chunk of data and a bunch of useful, related methods for
  // performing computations and transformations on that data.

  // Create a new model with the specified attributes. A client id (`cid`)
  // is automatically generated and assigned for you.
  var Model = Backbone.Model = function(attributes, options) {
    var attrs = attributes || {};
    options || (options = {});
    this.cid = _.uniqueId(this.cidPrefix);
    this.attributes = {};
    if (options.collection) this.collection = options.collection;
    if (options.parse) attrs = this.parse(attrs, options) || {};
    var defaults = _.result(this, 'defaults');
    attrs = _.defaults(_.extend({}, defaults, attrs), defaults);
    this.set(attrs, options);
    this.changed = {};
    this.initialize.apply(this, arguments);
  };

  // Attach all inheritable methods to the Model prototype.
  _.extend(Model.prototype, Events, {

    // A hash of attributes whose current and previous value differ.
    changed: null,

    // The value returned during the last failed validation.
    validationError: null,

    // The default name for the JSON `id` attribute is `"id"`. MongoDB and
    // CouchDB users may want to set this to `"_id"`.
    idAttribute: 'id',

    // The prefix is used to create the client id which is used to identify models locally.
    // You may want to override this if you're experiencing name clashes with model ids.
    cidPrefix: 'c',

    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // Return a copy of the model's `attributes` object.
    toJSON: function(options) {
      return _.clone(this.attributes);
    },

    // Proxy `Backbone.sync` by default -- but override this if you need
    // custom syncing semantics for *this* particular model.
    sync: function() {
      return Backbone.sync.apply(this, arguments);
    },

    // Get the value of an attribute.
    get: function(attr) {
      return this.attributes[attr];
    },

    // Get the HTML-escaped value of an attribute.
    escape: function(attr) {
      return _.escape(this.get(attr));
    },

    // Returns `true` if the attribute contains a value that is not null
    // or undefined.
    has: function(attr) {
      return this.get(attr) != null;
    },

    // Special-cased proxy to underscore's `_.matches` method.
    matches: function(attrs) {
      return !!_.iteratee(attrs, this)(this.attributes);
    },

    // Set a hash of model attributes on the object, firing `"change"`. This is
    // the core primitive operation of a model, updating the data and notifying
    // anyone who needs to know about the change in state. The heart of the beast.
    set: function(key, val, options) {
      if (key == null) return this;

      // Handle both `"key", value` and `{key: value}` -style arguments.
      var attrs;
      if (typeof key === 'object') {
        attrs = key;
        options = val;
      } else {
        (attrs = {})[key] = val;
      }

      options || (options = {});

      // Run validation.
      if (!this._validate(attrs, options)) return false;

      // Extract attributes and options.
      var unset      = options.unset;
      var silent     = options.silent;
      var changes    = [];
      var changing   = this._changing;
      this._changing = true;

      if (!changing) {
        this._previousAttributes = _.clone(this.attributes);
        this.changed = {};
      }

      var current = this.attributes;
      var changed = this.changed;
      var prev    = this._previousAttributes;

      // For each `set` attribute, update or delete the current value.
      for (var attr in attrs) {
        val = attrs[attr];
        if (!_.isEqual(current[attr], val)) changes.push(attr);
        if (!_.isEqual(prev[attr], val)) {
          changed[attr] = val;
        } else {
          delete changed[attr];
        }
        unset ? delete current[attr] : current[attr] = val;
      }

      // Update the `id`.
      if (this.idAttribute in attrs) this.id = this.get(this.idAttribute);

      // Trigger all relevant attribute changes.
      if (!silent) {
        if (changes.length) this._pending = options;
        for (var i = 0; i < changes.length; i++) {
          this.trigger('change:' + changes[i], this, current[changes[i]], options);
        }
      }

      // You might be wondering why there's a `while` loop here. Changes can
      // be recursively nested within `"change"` events.
      if (changing) return this;
      if (!silent) {
        while (this._pending) {
          options = this._pending;
          this._pending = false;
          this.trigger('change', this, options);
        }
      }
      this._pending = false;
      this._changing = false;
      return this;
    },

    // Remove an attribute from the model, firing `"change"`. `unset` is a noop
    // if the attribute doesn't exist.
    unset: function(attr, options) {
      return this.set(attr, void 0, _.extend({}, options, {unset: true}));
    },

    // Clear all attributes on the model, firing `"change"`.
    clear: function(options) {
      var attrs = {};
      for (var key in this.attributes) attrs[key] = void 0;
      return this.set(attrs, _.extend({}, options, {unset: true}));
    },

    // Determine if the model has changed since the last `"change"` event.
    // If you specify an attribute name, determine if that attribute has changed.
    hasChanged: function(attr) {
      if (attr == null) return !_.isEmpty(this.changed);
      return _.has(this.changed, attr);
    },

    // Return an object containing all the attributes that have changed, or
    // false if there are no changed attributes. Useful for determining what
    // parts of a view need to be updated and/or what attributes need to be
    // persisted to the server. Unset attributes will be set to undefined.
    // You can also pass an attributes object to diff against the model,
    // determining if there *would be* a change.
    changedAttributes: function(diff) {
      if (!diff) return this.hasChanged() ? _.clone(this.changed) : false;
      var old = this._changing ? this._previousAttributes : this.attributes;
      var changed = {};
      for (var attr in diff) {
        var val = diff[attr];
        if (_.isEqual(old[attr], val)) continue;
        changed[attr] = val;
      }
      return _.size(changed) ? changed : false;
    },

    // Get the previous value of an attribute, recorded at the time the last
    // `"change"` event was fired.
    previous: function(attr) {
      if (attr == null || !this._previousAttributes) return null;
      return this._previousAttributes[attr];
    },

    // Get all of the attributes of the model at the time of the previous
    // `"change"` event.
    previousAttributes: function() {
      return _.clone(this._previousAttributes);
    },

    // Fetch the model from the server, merging the response with the model's
    // local attributes. Any changed attributes will trigger a "change" event.
    fetch: function(options) {
      options = _.extend({parse: true}, options);
      var model = this;
      var success = options.success;
      options.success = function(resp) {
        var serverAttrs = options.parse ? model.parse(resp, options) : resp;
        if (!model.set(serverAttrs, options)) return false;
        if (success) success.call(options.context, model, resp, options);
        model.trigger('sync', model, resp, options);
      };
      wrapError(this, options);
      return this.sync('read', this, options);
    },

    // Set a hash of model attributes, and sync the model to the server.
    // If the server returns an attributes hash that differs, the model's
    // state will be `set` again.
    save: function(key, val, options) {
      // Handle both `"key", value` and `{key: value}` -style arguments.
      var attrs;
      if (key == null || typeof key === 'object') {
        attrs = key;
        options = val;
      } else {
        (attrs = {})[key] = val;
      }

      options = _.extend({validate: true, parse: true}, options);
      var wait = options.wait;

      // If we're not waiting and attributes exist, save acts as
      // `set(attr).save(null, opts)` with validation. Otherwise, check if
      // the model will be valid when the attributes, if any, are set.
      if (attrs && !wait) {
        if (!this.set(attrs, options)) return false;
      } else if (!this._validate(attrs, options)) {
        return false;
      }

      // After a successful server-side save, the client is (optionally)
      // updated with the server-side state.
      var model = this;
      var success = options.success;
      var attributes = this.attributes;
      options.success = function(resp) {
        // Ensure attributes are restored during synchronous saves.
        model.attributes = attributes;
        var serverAttrs = options.parse ? model.parse(resp, options) : resp;
        if (wait) serverAttrs = _.extend({}, attrs, serverAttrs);
        if (serverAttrs && !model.set(serverAttrs, options)) return false;
        if (success) success.call(options.context, model, resp, options);
        model.trigger('sync', model, resp, options);
      };
      wrapError(this, options);

      // Set temporary attributes if `{wait: true}` to properly find new ids.
      if (attrs && wait) this.attributes = _.extend({}, attributes, attrs);

      var method = this.isNew() ? 'create' : (options.patch ? 'patch' : 'update');
      if (method === 'patch' && !options.attrs) options.attrs = attrs;
      var xhr = this.sync(method, this, options);

      // Restore attributes.
      this.attributes = attributes;

      return xhr;
    },

    // Destroy this model on the server if it was already persisted.
    // Optimistically removes the model from its collection, if it has one.
    // If `wait: true` is passed, waits for the server to respond before removal.
    destroy: function(options) {
      options = options ? _.clone(options) : {};
      var model = this;
      var success = options.success;
      var wait = options.wait;

      var destroy = function() {
        model.stopListening();
        model.trigger('destroy', model, model.collection, options);
      };

      options.success = function(resp) {
        if (wait) destroy();
        if (success) success.call(options.context, model, resp, options);
        if (!model.isNew()) model.trigger('sync', model, resp, options);
      };

      var xhr = false;
      if (this.isNew()) {
        _.defer(options.success);
      } else {
        wrapError(this, options);
        xhr = this.sync('delete', this, options);
      }
      if (!wait) destroy();
      return xhr;
    },

    // Default URL for the model's representation on the server -- if you're
    // using Backbone's restful methods, override this to change the endpoint
    // that will be called.
    url: function() {
      var base =
        _.result(this, 'urlRoot') ||
        _.result(this.collection, 'url') ||
        urlError();
      if (this.isNew()) return base;
      var id = this.get(this.idAttribute);
      return base.replace(/[^\/]$/, '$&/') + encodeURIComponent(id);
    },

    // **parse** converts a response into the hash of attributes to be `set` on
    // the model. The default implementation is just to pass the response along.
    parse: function(resp, options) {
      return resp;
    },

    // Create a new model with identical attributes to this one.
    clone: function() {
      return new this.constructor(this.attributes);
    },

    // A model is new if it has never been saved to the server, and lacks an id.
    isNew: function() {
      return !this.has(this.idAttribute);
    },

    // Check if the model is currently in a valid state.
    isValid: function(options) {
      return this._validate({}, _.extend({}, options, {validate: true}));
    },

    // Run validation against the next complete set of model attributes,
    // returning `true` if all is well. Otherwise, fire an `"invalid"` event.
    _validate: function(attrs, options) {
      if (!options.validate || !this.validate) return true;
      attrs = _.extend({}, this.attributes, attrs);
      var error = this.validationError = this.validate(attrs, options) || null;
      if (!error) return true;
      this.trigger('invalid', this, error, _.extend(options, {validationError: error}));
      return false;
    }

  });

  // Underscore methods that we want to implement on the Model, mapped to the
  // number of arguments they take.
  var modelMethods = {keys: 1, values: 1, pairs: 1, invert: 1, pick: 0,
      omit: 0, chain: 1, isEmpty: 1};

  // Mix in each Underscore method as a proxy to `Model#attributes`.
  addUnderscoreMethods(Model, modelMethods, 'attributes');

  // Backbone.Collection
  // -------------------

  // If models tend to represent a single row of data, a Backbone Collection is
  // more analogous to a table full of data ... or a small slice or page of that
  // table, or a collection of rows that belong together for a particular reason
  // -- all of the messages in this particular folder, all of the documents
  // belonging to this particular author, and so on. Collections maintain
  // indexes of their models, both in order, and for lookup by `id`.

  // Create a new **Collection**, perhaps to contain a specific type of `model`.
  // If a `comparator` is specified, the Collection will maintain
  // its models in sort order, as they're added and removed.
  var Collection = Backbone.Collection = function(models, options) {
    options || (options = {});
    if (options.model) this.model = options.model;
    if (options.comparator !== void 0) this.comparator = options.comparator;
    this._reset();
    this.initialize.apply(this, arguments);
    if (models) this.reset(models, _.extend({silent: true}, options));
  };

  // Default options for `Collection#set`.
  var setOptions = {add: true, remove: true, merge: true};
  var addOptions = {add: true, remove: false};

  // Splices `insert` into `array` at index `at`.
  var splice = function(array, insert, at) {
    at = Math.min(Math.max(at, 0), array.length);
    var tail = Array(array.length - at);
    var length = insert.length;
    var i;
    for (i = 0; i < tail.length; i++) tail[i] = array[i + at];
    for (i = 0; i < length; i++) array[i + at] = insert[i];
    for (i = 0; i < tail.length; i++) array[i + length + at] = tail[i];
  };

  // Define the Collection's inheritable methods.
  _.extend(Collection.prototype, Events, {

    // The default model for a collection is just a **Backbone.Model**.
    // This should be overridden in most cases.
    model: Model,

    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // The JSON representation of a Collection is an array of the
    // models' attributes.
    toJSON: function(options) {
      return this.map(function(model) { return model.toJSON(options); });
    },

    // Proxy `Backbone.sync` by default.
    sync: function() {
      return Backbone.sync.apply(this, arguments);
    },

    // Add a model, or list of models to the set. `models` may be Backbone
    // Models or raw JavaScript objects to be converted to Models, or any
    // combination of the two.
    add: function(models, options) {
      return this.set(models, _.extend({merge: false}, options, addOptions));
    },

    // Remove a model, or a list of models from the set.
    remove: function(models, options) {
      options = _.extend({}, options);
      var singular = !_.isArray(models);
      models = singular ? [models] : models.slice();
      var removed = this._removeModels(models, options);
      if (!options.silent && removed.length) {
        options.changes = {added: [], merged: [], removed: removed};
        this.trigger('update', this, options);
      }
      return singular ? removed[0] : removed;
    },

    // Update a collection by `set`-ing a new list of models, adding new ones,
    // removing models that are no longer present, and merging models that
    // already exist in the collection, as necessary. Similar to **Model#set**,
    // the core operation for updating the data contained by the collection.
    set: function(models, options) {
      if (models == null) return;

      options = _.extend({}, setOptions, options);
      if (options.parse && !this._isModel(models)) {
        models = this.parse(models, options) || [];
      }

      var singular = !_.isArray(models);
      models = singular ? [models] : models.slice();

      var at = options.at;
      if (at != null) at = +at;
      if (at > this.length) at = this.length;
      if (at < 0) at += this.length + 1;

      var set = [];
      var toAdd = [];
      var toMerge = [];
      var toRemove = [];
      var modelMap = {};

      var add = options.add;
      var merge = options.merge;
      var remove = options.remove;

      var sort = false;
      var sortable = this.comparator && at == null && options.sort !== false;
      var sortAttr = _.isString(this.comparator) ? this.comparator : null;

      // Turn bare objects into model references, and prevent invalid models
      // from being added.
      var model, i;
      for (i = 0; i < models.length; i++) {
        model = models[i];

        // If a duplicate is found, prevent it from being added and
        // optionally merge it into the existing model.
        var existing = this.get(model);
        if (existing) {
          if (merge && model !== existing) {
            var attrs = this._isModel(model) ? model.attributes : model;
            if (options.parse) attrs = existing.parse(attrs, options);
            existing.set(attrs, options);
            toMerge.push(existing);
            if (sortable && !sort) sort = existing.hasChanged(sortAttr);
          }
          if (!modelMap[existing.cid]) {
            modelMap[existing.cid] = true;
            set.push(existing);
          }
          models[i] = existing;

        // If this is a new, valid model, push it to the `toAdd` list.
        } else if (add) {
          model = models[i] = this._prepareModel(model, options);
          if (model) {
            toAdd.push(model);
            this._addReference(model, options);
            modelMap[model.cid] = true;
            set.push(model);
          }
        }
      }

      // Remove stale models.
      if (remove) {
        for (i = 0; i < this.length; i++) {
          model = this.models[i];
          if (!modelMap[model.cid]) toRemove.push(model);
        }
        if (toRemove.length) this._removeModels(toRemove, options);
      }

      // See if sorting is needed, update `length` and splice in new models.
      var orderChanged = false;
      var replace = !sortable && add && remove;
      if (set.length && replace) {
        orderChanged = this.length !== set.length || _.some(this.models, function(m, index) {
          return m !== set[index];
        });
        this.models.length = 0;
        splice(this.models, set, 0);
        this.length = this.models.length;
      } else if (toAdd.length) {
        if (sortable) sort = true;
        splice(this.models, toAdd, at == null ? this.length : at);
        this.length = this.models.length;
      }

      // Silently sort the collection if appropriate.
      if (sort) this.sort({silent: true});

      // Unless silenced, it's time to fire all appropriate add/sort/update events.
      if (!options.silent) {
        for (i = 0; i < toAdd.length; i++) {
          if (at != null) options.index = at + i;
          model = toAdd[i];
          model.trigger('add', model, this, options);
        }
        if (sort || orderChanged) this.trigger('sort', this, options);
        if (toAdd.length || toRemove.length || toMerge.length) {
          options.changes = {
            added: toAdd,
            removed: toRemove,
            merged: toMerge
          };
          this.trigger('update', this, options);
        }
      }

      // Return the added (or merged) model (or models).
      return singular ? models[0] : models;
    },

    // When you have more items than you want to add or remove individually,
    // you can reset the entire set with a new list of models, without firing
    // any granular `add` or `remove` events. Fires `reset` when finished.
    // Useful for bulk operations and optimizations.
    reset: function(models, options) {
      options = options ? _.clone(options) : {};
      for (var i = 0; i < this.models.length; i++) {
        this._removeReference(this.models[i], options);
      }
      options.previousModels = this.models;
      this._reset();
      models = this.add(models, _.extend({silent: true}, options));
      if (!options.silent) this.trigger('reset', this, options);
      return models;
    },

    // Add a model to the end of the collection.
    push: function(model, options) {
      return this.add(model, _.extend({at: this.length}, options));
    },

    // Remove a model from the end of the collection.
    pop: function(options) {
      var model = this.at(this.length - 1);
      return this.remove(model, options);
    },

    // Add a model to the beginning of the collection.
    unshift: function(model, options) {
      return this.add(model, _.extend({at: 0}, options));
    },

    // Remove a model from the beginning of the collection.
    shift: function(options) {
      var model = this.at(0);
      return this.remove(model, options);
    },

    // Slice out a sub-array of models from the collection.
    slice: function() {
      return slice.apply(this.models, arguments);
    },

    // Get a model from the set by id, cid, model object with id or cid
    // properties, or an attributes object that is transformed through modelId.
    get: function(obj) {
      if (obj == null) return void 0;
      return this._byId[obj] ||
        this._byId[this.modelId(obj.attributes || obj)] ||
        obj.cid && this._byId[obj.cid];
    },

    // Returns `true` if the model is in the collection.
    has: function(obj) {
      return this.get(obj) != null;
    },

    // Get the model at the given index.
    at: function(index) {
      if (index < 0) index += this.length;
      return this.models[index];
    },

    // Return models with matching attributes. Useful for simple cases of
    // `filter`.
    where: function(attrs, first) {
      return this[first ? 'find' : 'filter'](attrs);
    },

    // Return the first model with matching attributes. Useful for simple cases
    // of `find`.
    findWhere: function(attrs) {
      return this.where(attrs, true);
    },

    // Force the collection to re-sort itself. You don't need to call this under
    // normal circumstances, as the set will maintain sort order as each item
    // is added.
    sort: function(options) {
      var comparator = this.comparator;
      if (!comparator) throw new Error('Cannot sort a set without a comparator');
      options || (options = {});

      var length = comparator.length;
      if (_.isFunction(comparator)) comparator = _.bind(comparator, this);

      // Run sort based on type of `comparator`.
      if (length === 1 || _.isString(comparator)) {
        this.models = this.sortBy(comparator);
      } else {
        this.models.sort(comparator);
      }
      if (!options.silent) this.trigger('sort', this, options);
      return this;
    },

    // Pluck an attribute from each model in the collection.
    pluck: function(attr) {
      return this.map(attr + '');
    },

    // Fetch the default set of models for this collection, resetting the
    // collection when they arrive. If `reset: true` is passed, the response
    // data will be passed through the `reset` method instead of `set`.
    fetch: function(options) {
      options = _.extend({parse: true}, options);
      var success = options.success;
      var collection = this;
      options.success = function(resp) {
        var method = options.reset ? 'reset' : 'set';
        collection[method](resp, options);
        if (success) success.call(options.context, collection, resp, options);
        collection.trigger('sync', collection, resp, options);
      };
      wrapError(this, options);
      return this.sync('read', this, options);
    },

    // Create a new instance of a model in this collection. Add the model to the
    // collection immediately, unless `wait: true` is passed, in which case we
    // wait for the server to agree.
    create: function(model, options) {
      options = options ? _.clone(options) : {};
      var wait = options.wait;
      model = this._prepareModel(model, options);
      if (!model) return false;
      if (!wait) this.add(model, options);
      var collection = this;
      var success = options.success;
      options.success = function(m, resp, callbackOpts) {
        if (wait) collection.add(m, callbackOpts);
        if (success) success.call(callbackOpts.context, m, resp, callbackOpts);
      };
      model.save(null, options);
      return model;
    },

    // **parse** converts a response into a list of models to be added to the
    // collection. The default implementation is just to pass it through.
    parse: function(resp, options) {
      return resp;
    },

    // Create a new collection with an identical list of models as this one.
    clone: function() {
      return new this.constructor(this.models, {
        model: this.model,
        comparator: this.comparator
      });
    },

    // Define how to uniquely identify models in the collection.
    modelId: function(attrs) {
      return attrs[this.model.prototype.idAttribute || 'id'];
    },

    // Private method to reset all internal state. Called when the collection
    // is first initialized or reset.
    _reset: function() {
      this.length = 0;
      this.models = [];
      this._byId  = {};
    },

    // Prepare a hash of attributes (or other model) to be added to this
    // collection.
    _prepareModel: function(attrs, options) {
      if (this._isModel(attrs)) {
        if (!attrs.collection) attrs.collection = this;
        return attrs;
      }
      options = options ? _.clone(options) : {};
      options.collection = this;
      var model = new this.model(attrs, options);
      if (!model.validationError) return model;
      this.trigger('invalid', this, model.validationError, options);
      return false;
    },

    // Internal method called by both remove and set.
    _removeModels: function(models, options) {
      var removed = [];
      for (var i = 0; i < models.length; i++) {
        var model = this.get(models[i]);
        if (!model) continue;

        var index = this.indexOf(model);
        this.models.splice(index, 1);
        this.length--;

        // Remove references before triggering 'remove' event to prevent an
        // infinite loop. #3693
        delete this._byId[model.cid];
        var id = this.modelId(model.attributes);
        if (id != null) delete this._byId[id];

        if (!options.silent) {
          options.index = index;
          model.trigger('remove', model, this, options);
        }

        removed.push(model);
        this._removeReference(model, options);
      }
      return removed;
    },

    // Method for checking whether an object should be considered a model for
    // the purposes of adding to the collection.
    _isModel: function(model) {
      return model instanceof Model;
    },

    // Internal method to create a model's ties to a collection.
    _addReference: function(model, options) {
      this._byId[model.cid] = model;
      var id = this.modelId(model.attributes);
      if (id != null) this._byId[id] = model;
      model.on('all', this._onModelEvent, this);
    },

    // Internal method to sever a model's ties to a collection.
    _removeReference: function(model, options) {
      delete this._byId[model.cid];
      var id = this.modelId(model.attributes);
      if (id != null) delete this._byId[id];
      if (this === model.collection) delete model.collection;
      model.off('all', this._onModelEvent, this);
    },

    // Internal method called every time a model in the set fires an event.
    // Sets need to update their indexes when models change ids. All other
    // events simply proxy through. "add" and "remove" events that originate
    // in other collections are ignored.
    _onModelEvent: function(event, model, collection, options) {
      if (model) {
        if ((event === 'add' || event === 'remove') && collection !== this) return;
        if (event === 'destroy') this.remove(model, options);
        if (event === 'change') {
          var prevId = this.modelId(model.previousAttributes());
          var id = this.modelId(model.attributes);
          if (prevId !== id) {
            if (prevId != null) delete this._byId[prevId];
            if (id != null) this._byId[id] = model;
          }
        }
      }
      this.trigger.apply(this, arguments);
    }

  });

  // Underscore methods that we want to implement on the Collection.
  // 90% of the core usefulness of Backbone Collections is actually implemented
  // right here:
  var collectionMethods = {forEach: 3, each: 3, map: 3, collect: 3, reduce: 0,
      foldl: 0, inject: 0, reduceRight: 0, foldr: 0, find: 3, detect: 3, filter: 3,
      select: 3, reject: 3, every: 3, all: 3, some: 3, any: 3, include: 3, includes: 3,
      contains: 3, invoke: 0, max: 3, min: 3, toArray: 1, size: 1, first: 3,
      head: 3, take: 3, initial: 3, rest: 3, tail: 3, drop: 3, last: 3,
      without: 0, difference: 0, indexOf: 3, shuffle: 1, lastIndexOf: 3,
      isEmpty: 1, chain: 1, sample: 3, partition: 3, groupBy: 3, countBy: 3,
      sortBy: 3, indexBy: 3, findIndex: 3, findLastIndex: 3};

  // Mix in each Underscore method as a proxy to `Collection#models`.
  addUnderscoreMethods(Collection, collectionMethods, 'models');

  // Backbone.View
  // -------------

  // Backbone Views are almost more convention than they are actual code. A View
  // is simply a JavaScript object that represents a logical chunk of UI in the
  // DOM. This might be a single item, an entire list, a sidebar or panel, or
  // even the surrounding frame which wraps your whole app. Defining a chunk of
  // UI as a **View** allows you to define your DOM events declaratively, without
  // having to worry about render order ... and makes it easy for the view to
  // react to specific changes in the state of your models.

  // Creating a Backbone.View creates its initial element outside of the DOM,
  // if an existing element is not provided...
  var View = Backbone.View = function(options) {
    this.cid = _.uniqueId('view');
    _.extend(this, _.pick(options, viewOptions));
    this._ensureElement();
    this.initialize.apply(this, arguments);
  };

  // Cached regex to split keys for `delegate`.
  var delegateEventSplitter = /^(\S+)\s*(.*)$/;

  // List of view options to be set as properties.
  var viewOptions = ['model', 'collection', 'el', 'id', 'attributes', 'className', 'tagName', 'events'];

  // Set up all inheritable **Backbone.View** properties and methods.
  _.extend(View.prototype, Events, {

    // The default `tagName` of a View's element is `"div"`.
    tagName: 'div',

    // jQuery delegate for element lookup, scoped to DOM elements within the
    // current view. This should be preferred to global lookups where possible.
    $: function(selector) {
      return this.$el.find(selector);
    },

    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // **render** is the core function that your view should override, in order
    // to populate its element (`this.el`), with the appropriate HTML. The
    // convention is for **render** to always return `this`.
    render: function() {
      return this;
    },

    // Remove this view by taking the element out of the DOM, and removing any
    // applicable Backbone.Events listeners.
    remove: function() {
      this._removeElement();
      this.stopListening();
      return this;
    },

    // Remove this view's element from the document and all event listeners
    // attached to it. Exposed for subclasses using an alternative DOM
    // manipulation API.
    _removeElement: function() {
      this.$el.remove();
    },

    // Change the view's element (`this.el` property) and re-delegate the
    // view's events on the new element.
    setElement: function(element) {
      this.undelegateEvents();
      this._setElement(element);
      this.delegateEvents();
      return this;
    },

    // Creates the `this.el` and `this.$el` references for this view using the
    // given `el`. `el` can be a CSS selector or an HTML string, a jQuery
    // context or an element. Subclasses can override this to utilize an
    // alternative DOM manipulation API and are only required to set the
    // `this.el` property.
    _setElement: function(el) {
      this.$el = el instanceof Backbone.$ ? el : Backbone.$(el);
      this.el = this.$el[0];
    },

    // Set callbacks, where `this.events` is a hash of
    //
    // *{"event selector": "callback"}*
    //
    //     {
    //       'mousedown .title':  'edit',
    //       'click .button':     'save',
    //       'click .open':       function(e) { ... }
    //     }
    //
    // pairs. Callbacks will be bound to the view, with `this` set properly.
    // Uses event delegation for efficiency.
    // Omitting the selector binds the event to `this.el`.
    delegateEvents: function(events) {
      events || (events = _.result(this, 'events'));
      if (!events) return this;
      this.undelegateEvents();
      for (var key in events) {
        var method = events[key];
        if (!_.isFunction(method)) method = this[method];
        if (!method) continue;
        var match = key.match(delegateEventSplitter);
        this.delegate(match[1], match[2], _.bind(method, this));
      }
      return this;
    },

    // Add a single event listener to the view's element (or a child element
    // using `selector`). This only works for delegate-able events: not `focus`,
    // `blur`, and not `change`, `submit`, and `reset` in Internet Explorer.
    delegate: function(eventName, selector, listener) {
      this.$el.on(eventName + '.delegateEvents' + this.cid, selector, listener);
      return this;
    },

    // Clears all callbacks previously bound to the view by `delegateEvents`.
    // You usually don't need to use this, but may wish to if you have multiple
    // Backbone views attached to the same DOM element.
    undelegateEvents: function() {
      if (this.$el) this.$el.off('.delegateEvents' + this.cid);
      return this;
    },

    // A finer-grained `undelegateEvents` for removing a single delegated event.
    // `selector` and `listener` are both optional.
    undelegate: function(eventName, selector, listener) {
      this.$el.off(eventName + '.delegateEvents' + this.cid, selector, listener);
      return this;
    },

    // Produces a DOM element to be assigned to your view. Exposed for
    // subclasses using an alternative DOM manipulation API.
    _createElement: function(tagName) {
      return document.createElement(tagName);
    },

    // Ensure that the View has a DOM element to render into.
    // If `this.el` is a string, pass it through `$()`, take the first
    // matching element, and re-assign it to `el`. Otherwise, create
    // an element from the `id`, `className` and `tagName` properties.
    _ensureElement: function() {
      if (!this.el) {
        var attrs = _.extend({}, _.result(this, 'attributes'));
        if (this.id) attrs.id = _.result(this, 'id');
        if (this.className) attrs['class'] = _.result(this, 'className');
        this.setElement(this._createElement(_.result(this, 'tagName')));
        this._setAttributes(attrs);
      } else {
        this.setElement(_.result(this, 'el'));
      }
    },

    // Set attributes from a hash on this view's element.  Exposed for
    // subclasses using an alternative DOM manipulation API.
    _setAttributes: function(attributes) {
      this.$el.attr(attributes);
    }

  });

  // Backbone.sync
  // -------------

  // Override this function to change the manner in which Backbone persists
  // models to the server. You will be passed the type of request, and the
  // model in question. By default, makes a RESTful Ajax request
  // to the model's `url()`. Some possible customizations could be:
  //
  // * Use `setTimeout` to batch rapid-fire updates into a single request.
  // * Send up the models as XML instead of JSON.
  // * Persist models via WebSockets instead of Ajax.
  //
  // Turn on `Backbone.emulateHTTP` in order to send `PUT` and `DELETE` requests
  // as `POST`, with a `_method` parameter containing the true HTTP method,
  // as well as all requests with the body as `application/x-www-form-urlencoded`
  // instead of `application/json` with the model in a param named `model`.
  // Useful when interfacing with server-side languages like **PHP** that make
  // it difficult to read the body of `PUT` requests.
  Backbone.sync = function(method, model, options) {
    var type = methodMap[method];

    // Default options, unless specified.
    _.defaults(options || (options = {}), {
      emulateHTTP: Backbone.emulateHTTP,
      emulateJSON: Backbone.emulateJSON
    });

    // Default JSON-request options.
    var params = {type: type, dataType: 'json'};

    // Ensure that we have a URL.
    if (!options.url) {
      params.url = _.result(model, 'url') || urlError();
    }

    // Ensure that we have the appropriate request data.
    if (options.data == null && model && (method === 'create' || method === 'update' || method === 'patch')) {
      params.contentType = 'application/json';
      params.data = JSON.stringify(options.attrs || model.toJSON(options));
    }

    // For older servers, emulate JSON by encoding the request into an HTML-form.
    if (options.emulateJSON) {
      params.contentType = 'application/x-www-form-urlencoded';
      params.data = params.data ? {model: params.data} : {};
    }

    // For older servers, emulate HTTP by mimicking the HTTP method with `_method`
    // And an `X-HTTP-Method-Override` header.
    if (options.emulateHTTP && (type === 'PUT' || type === 'DELETE' || type === 'PATCH')) {
      params.type = 'POST';
      if (options.emulateJSON) params.data._method = type;
      var beforeSend = options.beforeSend;
      options.beforeSend = function(xhr) {
        xhr.setRequestHeader('X-HTTP-Method-Override', type);
        if (beforeSend) return beforeSend.apply(this, arguments);
      };
    }

    // Don't process data on a non-GET request.
    if (params.type !== 'GET' && !options.emulateJSON) {
      params.processData = false;
    }

    // Pass along `textStatus` and `errorThrown` from jQuery.
    var error = options.error;
    options.error = function(xhr, textStatus, errorThrown) {
      options.textStatus = textStatus;
      options.errorThrown = errorThrown;
      if (error) error.call(options.context, xhr, textStatus, errorThrown);
    };

    // Make the request, allowing the user to override any Ajax options.
    var xhr = options.xhr = Backbone.ajax(_.extend(params, options));
    model.trigger('request', model, xhr, options);
    return xhr;
  };

  // Map from CRUD to HTTP for our default `Backbone.sync` implementation.
  var methodMap = {
    'create': 'POST',
    'update': 'PUT',
    'patch': 'PATCH',
    'delete': 'DELETE',
    'read': 'GET'
  };

  // Set the default implementation of `Backbone.ajax` to proxy through to `$`.
  // Override this if you'd like to use a different library.
  Backbone.ajax = function() {
    return Backbone.$.ajax.apply(Backbone.$, arguments);
  };

  // Backbone.Router
  // ---------------

  // Routers map faux-URLs to actions, and fire events when routes are
  // matched. Creating a new one sets its `routes` hash, if not set statically.
  var Router = Backbone.Router = function(options) {
    options || (options = {});
    if (options.routes) this.routes = options.routes;
    this._bindRoutes();
    this.initialize.apply(this, arguments);
  };

  // Cached regular expressions for matching named param parts and splatted
  // parts of route strings.
  var optionalParam = /\((.*?)\)/g;
  var namedParam    = /(\(\?)?:\w+/g;
  var splatParam    = /\*\w+/g;
  var escapeRegExp  = /[\-{}\[\]+?.,\\\^$|#\s]/g;

  // Set up all inheritable **Backbone.Router** properties and methods.
  _.extend(Router.prototype, Events, {

    // Initialize is an empty function by default. Override it with your own
    // initialization logic.
    initialize: function(){},

    // Manually bind a single named route to a callback. For example:
    //
    //     this.route('search/:query/p:num', 'search', function(query, num) {
    //       ...
    //     });
    //
    route: function(route, name, callback) {
      if (!_.isRegExp(route)) route = this._routeToRegExp(route);
      if (_.isFunction(name)) {
        callback = name;
        name = '';
      }
      if (!callback) callback = this[name];
      var router = this;
      Backbone.history.route(route, function(fragment) {
        var args = router._extractParameters(route, fragment);
        if (router.execute(callback, args, name) !== false) {
          router.trigger.apply(router, ['route:' + name].concat(args));
          router.trigger('route', name, args);
          Backbone.history.trigger('route', router, name, args);
        }
      });
      return this;
    },

    // Execute a route handler with the provided parameters.  This is an
    // excellent place to do pre-route setup or post-route cleanup.
    execute: function(callback, args, name) {
      if (callback) callback.apply(this, args);
    },

    // Simple proxy to `Backbone.history` to save a fragment into the history.
    navigate: function(fragment, options) {
      Backbone.history.navigate(fragment, options);
      return this;
    },

    // Bind all defined routes to `Backbone.history`. We have to reverse the
    // order of the routes here to support behavior where the most general
    // routes can be defined at the bottom of the route map.
    _bindRoutes: function() {
      if (!this.routes) return;
      this.routes = _.result(this, 'routes');
      var route, routes = _.keys(this.routes);
      while ((route = routes.pop()) != null) {
        this.route(route, this.routes[route]);
      }
    },

    // Convert a route string into a regular expression, suitable for matching
    // against the current location hash.
    _routeToRegExp: function(route) {
      route = route.replace(escapeRegExp, '\\$&')
                   .replace(optionalParam, '(?:$1)?')
                   .replace(namedParam, function(match, optional) {
                     return optional ? match : '([^/?]+)';
                   })
                   .replace(splatParam, '([^?]*?)');
      return new RegExp('^' + route + '(?:\\?([\\s\\S]*))?$');
    },

    // Given a route, and a URL fragment that it matches, return the array of
    // extracted decoded parameters. Empty or unmatched parameters will be
    // treated as `null` to normalize cross-browser behavior.
    _extractParameters: function(route, fragment) {
      var params = route.exec(fragment).slice(1);
      return _.map(params, function(param, i) {
        // Don't decode the search params.
        if (i === params.length - 1) return param || null;
        return param ? decodeURIComponent(param) : null;
      });
    }

  });

  // Backbone.History
  // ----------------

  // Handles cross-browser history management, based on either
  // [pushState](http://diveintohtml5.info/history.html) and real URLs, or
  // [onhashchange](https://developer.mozilla.org/en-US/docs/DOM/window.onhashchange)
  // and URL fragments. If the browser supports neither (old IE, natch),
  // falls back to polling.
  var History = Backbone.History = function() {
    this.handlers = [];
    this.checkUrl = _.bind(this.checkUrl, this);

    // Ensure that `History` can be used outside of the browser.
    if (typeof window !== 'undefined') {
      this.location = window.location;
      this.history = window.history;
    }
  };

  // Cached regex for stripping a leading hash/slash and trailing space.
  var routeStripper = /^[#\/]|\s+$/g;

  // Cached regex for stripping leading and trailing slashes.
  var rootStripper = /^\/+|\/+$/g;

  // Cached regex for stripping urls of hash.
  var pathStripper = /#.*$/;

  // Has the history handling already been started?
  History.started = false;

  // Set up all inheritable **Backbone.History** properties and methods.
  _.extend(History.prototype, Events, {

    // The default interval to poll for hash changes, if necessary, is
    // twenty times a second.
    interval: 50,

    // Are we at the app root?
    atRoot: function() {
      var path = this.location.pathname.replace(/[^\/]$/, '$&/');
      return path === this.root && !this.getSearch();
    },

    // Does the pathname match the root?
    matchRoot: function() {
      var path = this.decodeFragment(this.location.pathname);
      var rootPath = path.slice(0, this.root.length - 1) + '/';
      return rootPath === this.root;
    },

    // Unicode characters in `location.pathname` are percent encoded so they're
    // decoded for comparison. `%25` should not be decoded since it may be part
    // of an encoded parameter.
    decodeFragment: function(fragment) {
      return decodeURI(fragment.replace(/%25/g, '%2525'));
    },

    // In IE6, the hash fragment and search params are incorrect if the
    // fragment contains `?`.
    getSearch: function() {
      var match = this.location.href.replace(/#.*/, '').match(/\?.+/);
      return match ? match[0] : '';
    },

    // Gets the true hash value. Cannot use location.hash directly due to bug
    // in Firefox where location.hash will always be decoded.
    getHash: function(window) {
      var match = (window || this).location.href.match(/#(.*)$/);
      return match ? match[1] : '';
    },

    // Get the pathname and search params, without the root.
    getPath: function() {
      var path = this.decodeFragment(
        this.location.pathname + this.getSearch()
      ).slice(this.root.length - 1);
      return path.charAt(0) === '/' ? path.slice(1) : path;
    },

    // Get the cross-browser normalized URL fragment from the path or hash.
    getFragment: function(fragment) {
      if (fragment == null) {
        if (this._usePushState || !this._wantsHashChange) {
          fragment = this.getPath();
        } else {
          fragment = this.getHash();
        }
      }
      return fragment.replace(routeStripper, '');
    },

    // Start the hash change handling, returning `true` if the current URL matches
    // an existing route, and `false` otherwise.
    start: function(options) {
      if (History.started) throw new Error('Backbone.history has already been started');
      History.started = true;

      // Figure out the initial configuration. Do we need an iframe?
      // Is pushState desired ... is it available?
      this.options          = _.extend({root: '/'}, this.options, options);
      this.root             = this.options.root;
      this._wantsHashChange = this.options.hashChange !== false;
      this._hasHashChange   = 'onhashchange' in window && (document.documentMode === void 0 || document.documentMode > 7);
      this._useHashChange   = this._wantsHashChange && this._hasHashChange;
      this._wantsPushState  = !!this.options.pushState;
      this._hasPushState    = !!(this.history && this.history.pushState);
      this._usePushState    = this._wantsPushState && this._hasPushState;
      this.fragment         = this.getFragment();

      // Normalize root to always include a leading and trailing slash.
      this.root = ('/' + this.root + '/').replace(rootStripper, '/');

      // Transition from hashChange to pushState or vice versa if both are
      // requested.
      if (this._wantsHashChange && this._wantsPushState) {

        // If we've started off with a route from a `pushState`-enabled
        // browser, but we're currently in a browser that doesn't support it...
        if (!this._hasPushState && !this.atRoot()) {
          var rootPath = this.root.slice(0, -1) || '/';
          this.location.replace(rootPath + '#' + this.getPath());
          // Return immediately as browser will do redirect to new url
          return true;

        // Or if we've started out with a hash-based route, but we're currently
        // in a browser where it could be `pushState`-based instead...
        } else if (this._hasPushState && this.atRoot()) {
          this.navigate(this.getHash(), {replace: true});
        }

      }

      // Proxy an iframe to handle location events if the browser doesn't
      // support the `hashchange` event, HTML5 history, or the user wants
      // `hashChange` but not `pushState`.
      if (!this._hasHashChange && this._wantsHashChange && !this._usePushState) {
        this.iframe = document.createElement('iframe');
        this.iframe.src = 'javascript:0';
        this.iframe.style.display = 'none';
        this.iframe.tabIndex = -1;
        var body = document.body;
        // Using `appendChild` will throw on IE < 9 if the document is not ready.
        var iWindow = body.insertBefore(this.iframe, body.firstChild).contentWindow;
        iWindow.document.open();
        iWindow.document.close();
        iWindow.location.hash = '#' + this.fragment;
      }

      // Add a cross-platform `addEventListener` shim for older browsers.
      var addEventListener = window.addEventListener || function(eventName, listener) {
        return attachEvent('on' + eventName, listener);
      };

      // Depending on whether we're using pushState or hashes, and whether
      // 'onhashchange' is supported, determine how we check the URL state.
      if (this._usePushState) {
        addEventListener('popstate', this.checkUrl, false);
      } else if (this._useHashChange && !this.iframe) {
        addEventListener('hashchange', this.checkUrl, false);
      } else if (this._wantsHashChange) {
        this._checkUrlInterval = setInterval(this.checkUrl, this.interval);
      }

      if (!this.options.silent) return this.loadUrl();
    },

    // Disable Backbone.history, perhaps temporarily. Not useful in a real app,
    // but possibly useful for unit testing Routers.
    stop: function() {
      // Add a cross-platform `removeEventListener` shim for older browsers.
      var removeEventListener = window.removeEventListener || function(eventName, listener) {
        return detachEvent('on' + eventName, listener);
      };

      // Remove window listeners.
      if (this._usePushState) {
        removeEventListener('popstate', this.checkUrl, false);
      } else if (this._useHashChange && !this.iframe) {
        removeEventListener('hashchange', this.checkUrl, false);
      }

      // Clean up the iframe if necessary.
      if (this.iframe) {
        document.body.removeChild(this.iframe);
        this.iframe = null;
      }

      // Some environments will throw when clearing an undefined interval.
      if (this._checkUrlInterval) clearInterval(this._checkUrlInterval);
      History.started = false;
    },

    // Add a route to be tested when the fragment changes. Routes added later
    // may override previous routes.
    route: function(route, callback) {
      this.handlers.unshift({route: route, callback: callback});
    },

    // Checks the current URL to see if it has changed, and if it has,
    // calls `loadUrl`, normalizing across the hidden iframe.
    checkUrl: function(e) {
      var current = this.getFragment();

      // If the user pressed the back button, the iframe's hash will have
      // changed and we should use that for comparison.
      if (current === this.fragment && this.iframe) {
        current = this.getHash(this.iframe.contentWindow);
      }

      if (current === this.fragment) return false;
      if (this.iframe) this.navigate(current);
      this.loadUrl();
    },

    // Attempt to load the current URL fragment. If a route succeeds with a
    // match, returns `true`. If no defined routes matches the fragment,
    // returns `false`.
    loadUrl: function(fragment) {
      // If the root doesn't match, no routes can match either.
      if (!this.matchRoot()) return false;
      fragment = this.fragment = this.getFragment(fragment);
      return _.some(this.handlers, function(handler) {
        if (handler.route.test(fragment)) {
          handler.callback(fragment);
          return true;
        }
      });
    },

    // Save a fragment into the hash history, or replace the URL state if the
    // 'replace' option is passed. You are responsible for properly URL-encoding
    // the fragment in advance.
    //
    // The options object can contain `trigger: true` if you wish to have the
    // route callback be fired (not usually desirable), or `replace: true`, if
    // you wish to modify the current URL without adding an entry to the history.
    navigate: function(fragment, options) {
      if (!History.started) return false;
      if (!options || options === true) options = {trigger: !!options};

      // Normalize the fragment.
      fragment = this.getFragment(fragment || '');

      // Don't include a trailing slash on the root.
      var rootPath = this.root;
      if (fragment === '' || fragment.charAt(0) === '?') {
        rootPath = rootPath.slice(0, -1) || '/';
      }
      var url = rootPath + fragment;

      // Strip the hash and decode for matching.
      fragment = this.decodeFragment(fragment.replace(pathStripper, ''));

      if (this.fragment === fragment) return;
      this.fragment = fragment;

      // If pushState is available, we use it to set the fragment as a real URL.
      if (this._usePushState) {
        this.history[options.replace ? 'replaceState' : 'pushState']({}, document.title, url);

      // If hash changes haven't been explicitly disabled, update the hash
      // fragment to store history.
      } else if (this._wantsHashChange) {
        this._updateHash(this.location, fragment, options.replace);
        if (this.iframe && fragment !== this.getHash(this.iframe.contentWindow)) {
          var iWindow = this.iframe.contentWindow;

          // Opening and closing the iframe tricks IE7 and earlier to push a
          // history entry on hash-tag change.  When replace is true, we don't
          // want this.
          if (!options.replace) {
            iWindow.document.open();
            iWindow.document.close();
          }

          this._updateHash(iWindow.location, fragment, options.replace);
        }

      // If you've told us that you explicitly don't want fallback hashchange-
      // based history, then `navigate` becomes a page refresh.
      } else {
        return this.location.assign(url);
      }
      if (options.trigger) return this.loadUrl(fragment);
    },

    // Update the hash location, either replacing the current entry, or adding
    // a new one to the browser history.
    _updateHash: function(location, fragment, replace) {
      if (replace) {
        var href = location.href.replace(/(javascript:|#).*$/, '');
        location.replace(href + '#' + fragment);
      } else {
        // Some browsers require that `hash` contains a leading #.
        location.hash = '#' + fragment;
      }
    }

  });

  // Create the default Backbone.history.
  Backbone.history = new History;

  // Helpers
  // -------

  // Helper function to correctly set up the prototype chain for subclasses.
  // Similar to `goog.inherits`, but uses a hash of prototype properties and
  // class properties to be extended.
  var extend = function(protoProps, staticProps) {
    var parent = this;
    var child;

    // The constructor function for the new subclass is either defined by you
    // (the "constructor" property in your `extend` definition), or defaulted
    // by us to simply call the parent constructor.
    if (protoProps && _.has(protoProps, 'constructor')) {
      child = protoProps.constructor;
    } else {
      child = function(){ return parent.apply(this, arguments); };
    }

    // Add static properties to the constructor function, if supplied.
    _.extend(child, parent, staticProps);

    // Set the prototype chain to inherit from `parent`, without calling
    // `parent`'s constructor function and add the prototype properties.
    child.prototype = _.create(parent.prototype, protoProps);
    child.prototype.constructor = child;

    // Set a convenience property in case the parent's prototype is needed
    // later.
    child.__super__ = parent.prototype;

    return child;
  };

  // Set up inheritance for the model, collection, router, view and history.
  Model.extend = Collection.extend = Router.extend = View.extend = History.extend = extend;

  // Throw an error when a URL is needed, and none is supplied.
  var urlError = function() {
    throw new Error('A "url" property or function must be specified');
  };

  // Wrap an optional error callback with a fallback error event.
  var wrapError = function(model, options) {
    var error = options.error;
    options.error = function(resp) {
      if (error) error.call(options.context, model, resp, options);
      model.trigger('error', model, resp, options);
    };
  };

  return Backbone;
});
if(nosliw===undefined) 
{
	var nosliw = {};
}

_.extend(nosliw, function(){
	
	var loc_moduleName = "nosliw";
	
	var loc_nodes = {}; 
	
	var loc_modules = [];
	
	var loc_out = {
		getPackage : function(packageName){
			return createPackage(packageName);
		},
		
		getNode : function(nodePath){
			return this.getPackage(nodePath).useNode();
		},
		
		getNodeData : function(nodePath){
			return this.getNode(nodePath).getData();
		},
		
		createNode : function(nodePath, nodeData){
			var node = this.getNode(nodePath);
			node.setData(nodeData);
		},
		
		//callBackFunction(eventName, nodeName), this : node
		registerNodeEvent : function(nodeName, eventName, callBackFunction){
			var node = this.getNode(nodeName);
			if(eventName=="all"){
				node.on(eventName, function(eventName, eventName, nodeName){
					callBackFunction.call(node, event, nodeName);
				}, node);
			}
			else{
				node.on(eventName, callBackFunction, node);
			}
		},

		registerSetNodeDataEvent : function(nodeName, callBackFunction){
			var node = this.getNode(nodeName);
			var nodeData = node.getData();
			if(nodeData!=undefined){
				callBackFunction.call(node, this.NODEEVENT_SETDATA, nodeName);
			}
			this.registerNodeEvent(nodeName, this.NODEEVENT_SETDATA, callBackFunction)
		},
		
		triggerNodeEvent : function(nodeName, eventName){
			this.getNode(nodeName).trigger(eventName, eventName, nodeName);
		},
		
		registerModule : function(module, packageObj){
			loc_modules.push([module, packageObj]);
		},
		
		initModules : function(){
			//execute start callback method of each module 
			for(var i in loc_modules){
				var module = loc_modules[i];
				module[0].start(module[1]);
			}
		},
		
		error : function(errorData){
			console.error(this.getNodeData("common.utility.basicUtility").stringify(errorData));
		},

		warning : function(errorData){
			console.warn(this.getNodeData("common.utility.basicUtility").stringify(errorData));
		}
	};
	
	var createPackage = function(path){
		
		var loc_path = path; 
		
		var loc_package = {

				prv_useNode : function(){
					var node = loc_nodes[this.getName()];
					if(node==undefined){
						//if node does not exists, create empty one
						node = createNode(this.getName());
						loc_nodes[this.getName()] = node;
					}
					return node;
				},
				
				prv_setNodeData : function(nodeData){
					var node = this.prv_useNode();
					node.setData(nodeData);
					return node;
				},
				
				getPackage : function(path){
					if(path===undefined)  return this;
					return createPackage(path);
				},
				getChildPackage : function(relativePath){
					if(relativePath==undefined)  return this;
					return createPackage(loc_path+"."+relativePath);
				},
				useNode : function(path){
					var childPackage = this.getPackage(path);
					return childPackage.prv_useNode();
				},
				useChildNode : function(relativePath){
					var childPackage = this.getChildPackage(relativePath);
					return childPackage.prv_useNode();
				},
				setNodeData : function(path, nodeData){
					return this.getPackage(path).prv_setNodeData(nodeData);
				},
				setChildNodeData : function(path, nodeData){
					return this.getChildPackage(path).prv_setNodeData(nodeData);
				},
				createNode : function(path, nodeData){
					var nodePackage = this.getPackage(path);
					return nodePackage.prv_setNodeData(nodeData);
				},
				createChildNode : function(relativePath, nodeData){
					var nodePackage = this.getChildPackage(relativePath);
					return nodePackage.prv_setNodeData(nodeData);
				},
				getNodeData : function(path){
					var out = this.useNode(path).getData();
					if(out===undefined)  nosliw.logging.error(loc_moduleName, "The node", path, "cannot find by package : " + this.getName());  
					return out;
				},
				getChildNodeData : function(relativePath){
					var out = this.useChildNode(relativePath).getData();
					if(out===undefined)  nosliw.logging.error(loc_moduleName, "The node", relativePath, "cannot find by package : " + this.getName());  
					return out;
				},
				getName : function(){return loc_path;}
			};
			return loc_package;
		
	};
	
	//Create node, node is wrapper of data
	//Sometimes, node is created, but data will set later
	//It happens during build dependency. 
	//A module require another module make required node build, but at that time, the module data does not create yet
	var createNode = function(packageName){
		var loc_data;
		var loc_packageName = packageName;
		
		var loc_node = {
			getData : function(){
				return loc_data;
			},
			setData : function(data){
				loc_data = data;
				nosliw.triggerNodeEvent(this.getPackageName(), loc_out.NODEEVENT_SETDATA);
			},
			getPackageName : function(){
				return loc_packageName;
			}
		}
		//every node is a event source
		_.extend(loc_node, Backbone.Events);
		return loc_node;
	};
	
	loc_out.NODEEVENT_SETDATA = "setData";
	
	//set logging object
	loc_out.registerSetNodeDataEvent("service.loggingservice.createLoggingService", function(){	nosliw.logging = this.getData()();	});
	
	return loc_out;
}());

var library = nosliw.getPackage("constant");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var COMMONATRIBUTECONSTANT=

{
  "REQUEST_GETUIRESOURCE_NAME": "name",
  "REQUEST_GETENTITYDEFINITIONBYNAMES_NAMES": "names",
  "REQUEST_GETDATATYPES_EXISTINGARRAY": "existingArray",
  "REQUEST_GETDATATYPES_REQUESTARRAY": "requestArray",
  "ENTITYNAME_ATTRIBUTENAME": "ENTITYNAME_ATTRIBUTENAME",
  "SERVICEDATA_CODE": "code",
  "SERVICEDATA_MESSAGE": "message",
  "SERVICEDATA_DATA": "data",
  "SERVICEDATA_METADATA": "metaData",
  "DATATYPEINFO_CATEGARY": "categary",
  "DATATYPEINFO_TYPE": "type",
  "DATATYPEINFO_KEY": "key",
  "DATATYPEINFO_ENTITYGROUPS": "entityGroups",
  "DATATYPEINFO_VERSION": "version",
  "DATATYPEINFO_CHILD_CATEGARY": "childCategary",
  "DATATYPEINFO_CHILD_TYPE": "childTYPE",
  "DATATYPEINFO_CHILD_ENTITYGROUPS": "childEntityGroups",
  "DATATYPE_DATATYPEINFO": "dataTypeInfo",
  "DATATYPE_PARENT": "parent",
  "DATATYPE_OPERATIONINFOS": "operationInfos",
  "DATATYPE_NEWOPERATIONINFOS": "newOperationInfos",
  "DATATYPE_OPERATIONSCRIPTS": "operationScripts",
  "DATAOPERATIONINFO_NAME": "name",
  "DATAOPERATIONINFO_DESCRIPTION": "description",
  "DATAOPERATIONINFO_CONVERTPATH": "convertPath",
  "DATAOPERATIONINFO_OUT": "out",
  "DATAOPERATIONINFO_INS": "ins",
  "DATAOPERATIONINFO_DEPENDENTDATATYPES": "dependentDataTypes",
  "DATA_DATATYPEINFO": "dataTypeInfo",
  "DATA_VALUE": "value",
  "WRAPER_DATA": "data",
  "WRAPER_DATATYPE": "dataType",
  "WRAPER_INFO": "info",
  "OPERATIONINFO_OPERATIONID": "operationId",
  "OPERATIONINFO_OPERATION": "operation",
  "OPERATIONINFO_AUTOCOMMIT": "isAutoCommit",
  "OPERATIONINFO_VALIDATION": "isValidation",
  "OPERATIONINFO_SCOPE": "scope",
  "OPERATIONINFO_ROOTOPERATION": "rootOperation",
  "OPERATIONINFO_SUBMITABLE": "submitable",
  "OPERATIONINFO_EXTRA": "extra",
  "OPERATIONINFO_ENTITYID": "entityId",
  "OPERATIONINFO_ATTRIBUTEPATH": "attributePath",
  "OPERATIONINFO_DATA": "data",
  "OPERATIONINFO_TRANSACTIONID": "transactionId",
  "OPERATIONINFO_REFERENCEPATH": "referencePath",
  "OPERATIONINFO_WRAPER": "wraper",
  "OPERATIONINFO_ENTITYDEFINITION": "entityDefinition",
  "OPERATIONINFO_ATTRIBUTEDEFINITION": "attributeDefinition",
  "OPERATIONINFO_ENTITYTYPE": "entityType",
  "OPERATIONINFO_ELEMENTID": "elementId",
  "OPERATIONINFO_QUERYNAME": "queryName",
  "OPERATIONINFO_QUERYENTITYWRAPER": "queryEntityWraper",
  "OPERATIONINFO_VALUE": "value",
  "OPERATIONINFO_REFENTITYID": "refEntityID",
  "OPERATIONINFO_PARMS": "parms",
  "OPERATIONINFO_ENTITYOPERATIONS": "entityOperations",
  "ENTITYATTRDEF_NAME": "name",
  "ENTITYATTRDEF_FULLNAME": "fullName",
  "ENTITYATTRDEF_CRITICALVALUE": "criticalValue",
  "ENTITYATTRDEF_DESCRIPTION": "description",
  "ENTITYATTRDEF_ISEMPTYONINIT": "isEmptyOnInit",
  "ENTITYATTRDEF_DATATYPEDEFINFO": "dataTypeDefInfo",
  "ENTITYATTRDEF_VALIDATION": "validation",
  "ENTITYATTRDEF_RULES": "rules",
  "ENTITYATTRDEF_OPTIONS": "options",
  "ENTITYATTRDEF_EVENTS": "events",
  "ENTITYATTRDEF_ISCRITICAL": "isCritical",
  "ENTITYATTRDEF_DEFAULTVALUE": "defaultValue",
  "ENTITYATTRDEF_ELEMENT": "element",
  "ENTITYID_ID": "id",
  "ENTITYID_ENTITYTYPE": "entityType",
  "ENTITYID_KEY": "key",
  "DATAWRAPER_CHILDDATATYPE": "childDataType",
  "DATAWRAPER_ID": "id",
  "DATAWRAPER_ENTITYID": "entityID",
  "DATAWRAPER_ATTRPATH": "attrPath",
  "DATAWRAPER_PARENTENTITYID": "parentEntityID",
  "DATAWRAPER_ATTRCONFIGURE": "attrConfigure",
  "ENTITYPATH_PATH": "path",
  "ENTITYPATH_ENTITYID": "entityID",
  "ENTITYPATH_EXPECTPATH": "expectPath",
  "ENTITYPATHINFO_ENTITYPATH": "entityPath",
  "ENTITYPATHINFO_PATHSEGMENTS": "pathSegments",
  "ENTITYPATHINFO_DATA": "data",
  "QUERYENTITYATTRIBUTEWRAPER_ENTITYPATH": "entityPath",
  "QUERYDEFINITION_NAME": "name",
  "QUERYDEFINITION_EXPRESSIONINFO": "expressionInfo",
  "QUERYDEFINITION_PROJECTATTRIBUTES": "projectAttributes",
  "QUERYPROJECTATTRIBUTE_ENTITYNAME": "entityName",
  "QUERYPROJECTATTRIBUTE_ATTRIBUTE": "attribute",
  "ENTITYMANAGER_PERSISTANT": "persistent",
  "ENTITYMANAGER_TRANSACTIONS": "transactions",
  "CONSTANTGROUP_TYPE": "type",
  "CONSTANTGROUP_FILEPATH": "filepath",
  "CONSTANTGROUP_CLASSNAME": "classname",
  "CONSTANTGROUP_PACKAGENAME": "packagename",
  "CONSTANTGROUP_DEFINITIONS": "definitions",
  "SERVICEDATA_SERVICEDATA_CODE": "code",
  "SERVICEDATA_SERVICEDATA_MESSAGE": "message",
  "SERVICEDATA_SERVICEDATA_DATA": "data",
  "SERVICEDATA_SERVICEDATA_METADATA": "metaData",
  "ENTITYINFO_NAME": "name",
  "ENTITYINFO_DESCRIPTION": "description",
  "ENTITYINFO_INFO": "info",
  "STRINGABLEVALUE_STRUCTURE": "structure",
  "STRINGABLEVALUE_TYPE": "type",
  "STRINGABLEVALUE_SUBTYPE": "subType",
  "STRINGABLEVALUE_STRINGVALUE": "stringValue",
  "STRINGABLEVALUE_RESOLVED": "resolved",
  "STRINGABLEVALUE_VALUE": "atomic_value",
  "STRINGABLEVALUE_ELEMENTS": "elements",
  "STRINGABLEVALUE_PROPERTIES": "properties",
  "STRINGABLEVALUE_ID": "id",
  "DATATYPECRITERIA_TYPE": "type",
  "DATATYPECRITERIA_CHILDREN": "children",
  "DATATYPECRITERIA_DATATYPEID": "dataTypeId",
  "DATATYPECRITERIA_ELEMENTDATATYPECRITERIA": "elementDataTypeCriteria",
  "DATATYPECRITERIA_IDSCRITERIA": "idsCriteria",
  "DATATYPECRITERIA_DATATYPEFROM": "dataTypeFrom",
  "DATATYPECRITERIA_DATATYPETO": "dataTypeTo",
  "VARIABLEINFO_CRITERIA": "criteria",
  "VARIABLEINFO_STATUS": "status",
  "VARIABLEINFO_INFO": "info",
  "_EXPRESSION": "expression",
  "_OPERAND": "operand",
  "_VARIABLENAMES": "variableNames",
  "_REFERENCENAMES": "referenceNames",
  "EXPRESSION_VARIABLEINFOS": "variableInfos",
  "DATA_DATATYPEID": "dataTypeId",
  "DATATYPE_INFO": "info",
  "DATATYPE_NAME": "name",
  "DATATYPE_PARENTSINFO": "parentsInfo",
  "DATATYPE_LINKEDVERSION": "linkedVersion",
  "DATATYPE_ISCOMPLEX": "isComplex",
  "DATATYPEID_NAME": "name",
  "DATATYPEID_VERSION": "version",
  "DATATYPEID_PARMS": "parms",
  "DATATYPEID_FULLNAME": "fullName",
  "DATATYPEOPERATION_OPERATIONINFO": "operationInfo",
  "DATATYPEOPERATION_TARGETDATATYPE": "targetDataType",
  "DATATYPEVERSION_MAJOR": "major",
  "DATATYPEVERSION_MINOR": "minor",
  "DATATYPEVERSION_REVISION": "revision",
  "DATATYPEVERSION_NAME": "name",
  "DATA_VALUEFORMAT": "valueFormat",
  "DATAOPERATIONINFO_TYPE": "type",
  "DATAOPERATIONINFO_PAMRS": "parms",
  "DATAOPERATIONINFO_OUTPUT": "output",
  "DATAOPERATIONINFO_INFO": "info",
  "DATATYPEID_OPERATION": "operation",
  "DATAOPERATIONOUTPUTINFO_DESCRIPTION": "description",
  "DATAOPERATIONOUTPUTINFO_CRITERIA": "criteria",
  "OPERATIONPARM_NAME": "name",
  "OPERATIONPARM_DATA": "data",
  "DATAOPERATIONPARMINFO_NAME": "name",
  "DATAOPERATIONPARMINFO_ISBASE": "isBase",
  "DATATYPERELATIONSHIP_PATH": "path",
  "DATATYPERELATIONSHIP_TARGET": "target",
  "DATATYPERELATIONSHIP_SOURCE": "source",
  "MATCHER_REVERSE": "reverse",
  "MATCHER_DATATYPEID": "dataTypeId",
  "MATCHER_RELATIONSHIP": "relationship",
  "MATCHER_SUBMATCHERS": "subMatchers",
  "OPERAND_TYPE": "type",
  "OPERAND_STATUS": "status",
  "OPERAND_CHILDREN": "children",
  "OPERAND_DATATYPEINFO": "dataTypeInfo",
  "OPERAND_CONVERTERS": "converters",
  "OPERAND_OUTPUTCRITERIA": "outputCriteria",
  "OPERAND_NAME": "name",
  "OPERAND_DATA": "data",
  "OPERAND_DATATYPEID": "dataTypeId",
  "OPERAND_OPERATION": "operation",
  "OPERAND_PARMS": "parms",
  "OPERAND_BASE": "base",
  "OPERAND_MATCHERSPARMS": "matchersParms",
  "OPERAND_REFERENCENAME": "referenceName",
  "OPERAND_VARIABLENAME": "variableName",
  "DEFINITIONACTIVITY_OUTPUT": "output",
  "EXECUTABLEACTIVITY_RESULTNAME": "resultName",
  "DEFINITIONACTIVITY_EXPRESSION": "expression",
  "EXECUTABLEACTIVITY_SCRIPTEXPRESSION": "scriptExpression",
  "EXECUTABLEACTIVITY_SCRIPTEXPRESSIONSCRIPT": "scriptExpressionScript",
  "DEFINITIONACTIVITY_PROVIDER": "provider",
  "DEFINITIONACTIVITY_PARMMAPPING": "parmMapping",
  "EXECUTABLEACTIVITY_SERVICE": "service",
  "EXECUTABLEACTIVITY_PROVIDER": "provider",
  "DEFINITIONACTIVITY_FLOW": "flow",
  "EXECUTABLEACTIVITY_FLOW": "flow",
  "DEFINITIONACTIVITY_TYPE": "type",
  "DEFINITIONACTIVITY_INPUT": "input",
  "DEFINITIONACTIVITY_RESULT": "result",
  "DEFINITIONPROCESS_CONTEXT": "context",
  "DEFINITIONPROCESS_ACTIVITY": "activity",
  "DEFINITIONPROCESS_SERVICEPROVIDER": "activity",
  "DEFINITIONPROCESSSUITE_CONTEXT": "context",
  "DEFINITIONPROCESSSUITE_PROCESS": "process",
  "DEFINITIONRESULTACTIVITYNORMAL_FLOW": "flow",
  "DEFINITIONRESULTACTIVITYNORMAL_OUTPUT": "output",
  "DEFINITIONSEQUENCEFLOW_TARGET": "target",
  "EXECUTABLEACTIVITY_TYPE": "type",
  "EXECUTABLEACTIVITY_ID": "id",
  "EXECUTABLEACTIVITY_DEFINITION": "definition",
  "EXECUTABLEACTIVITY_INPUT": "input",
  "EXECUTABLEACTIVITY_RESULT": "result",
  "EXECUTABLEPROCESS_DEFINITION": "definition",
  "EXECUTABLEPROCESS_ID": "id",
  "EXECUTABLEPROCESS_ACTIVITY": "activity",
  "EXECUTABLEPROCESS_STARTACTIVITYID": "startActivityId",
  "EXECUTABLEPROCESS_CONTEXT": "context",
  "EXECUTABLEPROCESS_RESULT": "result",
  "EXECUTABLEPROCESS_INITSCRIPT": "initScript",
  "EXECUTABLERESULTACTIVITYNORMAL_FLOW": "flow",
  "EXECUTABLERESULTACTIVITYNORMAL_DATAASSOCIATION": "dataAssociation",
  "PLUGINACTIVITY_TYPE": "type",
  "PLUGINACTIVITY_SCRIPT": "script",
  "PLUGINACTIVITY_DEFINITION": "definition",
  "PLUGINACTIVITY_PROCESSOR": "processor",
  "RESOURCE_ID": "id",
  "RESOURCE_RESOURCEDATA": "resourceData",
  "RESOURCE_INFO": "info",
  "RESOURCEDEPENDENT_ALIAS": "alias",
  "RESOURCEDEPENDENT_ID": "id",
  "RESOURCEID_ID": "id",
  "RESOURCEID_TYPE": "type",
  "RESOURCEINFO_ID": "id",
  "RESOURCEINFO_INFO": "info",
  "RESOURCEINFO_DEPENDENCY": "dependency",
  "RESOURCEINFO_CHILDREN": "children",
  "EXPRESSION_OPERAND": "operand",
  "EXPRESSION_VARIABLESMATCHERS": "variablesMatchers",
  "RUNTIMETASKEXECUTECONVERTER_DATAT": "data",
  "RUNTIMETASKEXECUTECONVERTER_MATCHERS": "matchers",
  "RUNTIMETASKEXECUTEDATAOPERATION_DATATYPEID": "dataTypeId",
  "RUNTIMETASKEXECUTEDATAOPERATION_OPERATION": "operation",
  "RUNTIMETASKEXECUTEDATAOPERATION_PARMS": "parms",
  "RUNTIMETASKEXECUTEEXPRESSION_EXPRESSION": "expression",
  "RUNTIMETASKEXECUTEEXPRESSION_VARIABLESVALUE": "variablesValue",
  "GATEWAYLOADTESTEXPRESSION_COMMAND_LOADTESTEXPRESSION": "loadTestExpression",
  "GATEWAYLOADTESTEXPRESSION_COMMAND_LOADTESTEXPRESSION_ELEMENT_SUITE": "suite",
  "GATEWAYLOADTESTEXPRESSION_COMMAND_LOADTESTEXPRESSION_ELEMENT_EXPRESSIONNAME": "expressionName",
  "GATEWAYLOADTESTEXPRESSION_COMMAND_LOADTESTEXPRESSION_ELEMENT_VARIABLES": "variables",
  "_COMMAND_GETCHILDCRITERIA": "getChildCriteria",
  "_COMMAND_GETCHILDCRITERIA_CRITERIA": "criteria",
  "_COMMAND_GETCHILDCRITERIA_CHILDNAME": "childName",
  "_COMMAND_GETCHILDRENCRITERIA": "getChildrenCriteria",
  "_COMMAND_GETCHILDRENCRITERIA_CRITERIA": "criteria",
  "_COMMAND_ADDCHILDCRITERIA": "addChildCriteria",
  "_COMMAND_ADDCHILDCRITERIA_CRITERIA": "criteria",
  "_COMMAND_ADDCHILDCRITERIA_CHILDNAME": "childName",
  "_COMMAND_ADDCHILDCRITERIA_CHILD": "child",
  "_COMMAND_GETOUTPUTCRITERIA": "getOutputCriteria",
  "_COMMAND_GETOUTPUTCRITERIA_EXPRESSION": "expression",
  "_COMMAND_GETOUTPUTCRITERIA_CONSTANTS": "constants",
  "_COMMAND_GETOUTPUTCRITERIA_VARIABLESCRITERIA": "variablesCriteria",
  "_COMMAND_EXECUTEEXPRESSION": "executeExpression",
  "_COMMAND_EXECUTEEXPRESSION_EXPRESSION": "expression",
  "_COMMAND_EXECUTEEXPRESSION_CONSTANTS": "constants",
  "_COMMAND_EXECUTEEXPRESSION_VARIABLESVALUE": "variablesValue",
  "GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES": "requestDiscoverResources",
  "GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES_RESOURCEIDS": "resourceIds",
  "GATEWAYRESOURCE_COMMAND_DISCOVERANDLOADRESOURCES": "requestDiscoverAndLoadResources",
  "GATEWAYRESOURCE_COMMAND_DISCOVERANDLOADRESOURCES_RESOURCEIDS": "resourceIds",
  "GATEWAYRESOURCE_COMMAND_LOADRESOURCES": "requestLoadResources",
  "GATEWAYRESOURCE_COMMAND_LOADRESOURCES_RESOURCEINFOS": "resourceInfos",
  "GATEWAYOUTPUT_SCRIPTS": "scripts",
  "GATEWAYOUTPUT_DATA": "data",
  "JSSCRIPTINFO_NAME": "name",
  "JSSCRIPTINFO_FILE": "file",
  "JSSCRIPTINFO_SCRIPT": "script",
  "_VALUE": "value",
  "RUNTIME_NODENAME_GATEWAY": "gatewayObj",
  "RUNTIME_GATEWAY_RESOURCE": "resources",
  "RUNTIME_GATEWAY_CRITERIA": "criteria",
  "RUNTIME_GATEWAY_DISCOVERY": "discovery",
  "RUNTIMEGATEWAYJS_REQUEST_DISCOVERRESOURCES": "requestDiscoverResources",
  "RUNTIMEGATEWAYJS_REQUEST_DISCOVERRESOURCES_RESOURCEIDS": "resourceIds",
  "RUNTIMEGATEWAYJS_REQUEST_DISCOVERANDLOADRESOURCES": "requestDiscoverAndLoadResources",
  "RUNTIMEGATEWAYJS_REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS": "resourceIds",
  "RUNTIMEGATEWAYJS_REQUEST_LOADRESOURCES": "requestLoadResources",
  "RUNTIMEGATEWAYJS_REQUEST_LOADRESOURCES_RESOURCEINFOS": "resourceInfos",
  "RUNTIMEGATEWAYJS_REQUEST_GETEXPRESSIONS": "getExpressions",
  "RUNTIMEGATEWAYJS_REQUEST_GETEXPRESSIONS_EXPRESSIONS": "expressions",
  "RUNTIMEGATEWAYJS_REQUEST_GETEXPRESSIONS_ELEMENT_SUITE": "suite",
  "RUNTIMEGATEWAYJS_REQUEST_GETEXPRESSIONS_ELEMENT_EXPRESSIONNAME": "expressionName",
  "RUNTIMEGATEWAYJS_REQUEST_GETEXPRESSIONS_ELEMENT_VARIABLES": "variables",
  "_NAME": "name",
  "_VERSION": "version",
  "_DATATYPENAME": "dataTypeName",
  "_GATEWAY": "gateway",
  "RESOURCEDATAJSLIBRARY_URIS": "uris",
  "_OPERATIONID": "operationId",
  "_OPERATIONNAME": "operationName",
  "_EMBEDEDEXPRESSION": "embededExpression",
  "_VARIABLESVALUE": "variablesValue",
  "_SCRIPTEXPRESSION": "scriptExpression",
  "_TYPE": "type",
  "_OUTPUTMAPPING": "outputMapping",
  "_INPUTMAPPING": "inputMapping",
  "_TASK": "task",
  "EXECUTABLEDATAASSOCIATION_TYPE": "type",
  "EXECUTABLEDATAASSOCIATION_DEFINITION": "definition",
  "EXECUTABLEDATAASSOCIATION_INPUT": "input",
  "EXECUTABLEDATAASSOCIATION_OUTPUT": "output",
  "EXECUTABLEGROUPDATAASSOCIATION_ELEMENT": "element",
  "EXECUTABLEWRAPPERTASK_OUTPUTMAPPING": "outputMapping",
  "EXECUTABLEWRAPPERTASK_INPUTMAPPING": "inputMapping",
  "EXECUTABLEWRAPPERTASK_TASK": "task",
  "ENTITYINFO_ASSOCIATION": "association",
  "EXECUTABLEASSOCIATION_CONTEXT": "context",
  "EXECUTABLEASSOCIATION_PATHMAPPING": "pathMapping",
  "EXECUTABLEASSOCIATION_FLATINPUT": "flatInput",
  "EXECUTABLEASSOCIATION_FLATOUTPUT": "flatOutput",
  "EXECUTABLEASSOCIATION_OUTPUTMATCHERS": "outputMatchers",
  "EXECUTABLEASSOCIATION_CONVERTFUNCTION": "convertFunction",
  "EXECUTABLEDATAASSOCIATION_ASSOCIATION": "association",
  "CONTEXT_ELEMENT": "element",
  "CONTEXTDEFINITIONELEMENT_TYPE": "type",
  "CONTEXTDEFINITIONELEMENT_PROCESSED": "processed",
  "CONTEXTDEFINITIONLEAFCONSTANT_VALUE": "value",
  "CONTEXTDEFINITIONELEMENT_CRITERIA": "criteria",
  "CONTEXTDEFINITIONELEMENT_PATH": "path",
  "CONTEXTDEFINITIONELEMENT_PARENT": "parent",
  "CONTEXTDEFINITIONELEMENT_PARENTCATEGARY": "parentCategary",
  "CONTEXTDEFINITIONELEMENT_DEFINITION": "definition",
  "CONTEXTDEFINITIONELEMENT_MATCHERS": "matchers",
  "CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS": "reverseMatchers",
  "CONTEXTDEFINITIONELEMENT_CHILD": "child",
  "CONTEXTDEFINITIONROOT_DEFINITION": "definition",
  "CONTEXTDEFINITIONROOT_DEFAULT": "defaultValue",
  "CONTEXTFLAT_CONTEXT": "context",
  "CONTEXTFLAT_LOCAL2GLOBAL": "local2Global",
  "CONTEXTGROUP_GROUP": "group",
  "CONTEXTGROUP_INFO": "info",
  "CONTEXTGROUP_INFO_INHERIT": "inherit",
  "CONTEXTGROUP_INFO_POPUP": "popup",
  "CONTEXTGROUP_INFO_ESCALATE": "escalate",
  "CONTEXTPATH_ROOTNAME": "rootEleName",
  "CONTEXTPATH_PATH": "path",
  "_CONSTANTNAME": "constantName",
  "_DEFINITION": "definition",
  "_ELEMENTS": "elements",
  "EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONS": "scriptExpressions",
  "EMBEDEDSCRIPTEXPRESSION_SCRIPTFUNCTION": "scriptFunction",
  "EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONSCRIPTFUNCTION": "scriptExpressionScriptFunction",
  "SCRIPTEXPRESSION_DEFINITION": "definition",
  "SCRIPTEXPRESSION_SCRIPTFUNCTION": "scriptFunction",
  "SCRIPTEXPRESSION_EXPRESSIONS": "expressions",
  "SCRIPTEXPRESSION_VARIABLENAMES": "variableNames",
  "_SCRIPT": "script",
  "_VARIABLENAME": "variableName",
  "SERVICEINTERFACE_PARM": "parm",
  "SERVICEINTERFACE_RESULT": "result",
  "SERVICEOUTPUT_CRITERIA": "criteria",
  "SERVICEPARM_CRITERIA": "criteria",
  "SERVICEPARM_DEFAULT": "default",
  "SERVICERESULT_OUTPUT": "output",
  "DEFINITIONSERVICE_STATIC": "static",
  "DEFINITIONSERVICE_RUNTIME": "runtime",
  "GATEWAYSERVICE_COMMAND_REQUEST": "request",
  "GATEWAYSERVICE_COMMAND_REQUEST_QUERY": "query",
  "GATEWAYSERVICE_COMMAND_REQUEST_PARMS": "parms",
  "_IMPLEMENTATION": "implementation",
  "_CONFIGURE": "configure",
  "INFOSERVICESTATIC_ID": "id",
  "INFOSERVICESTATIC_TAG": "tag",
  "INFOSERVICESTATIC_INTERFACE": "interface",
  "QUERYSERVICE_SERVICEID": "serviceId",
  "QUERYSERVICE_INTERFACE": "interface",
  "RESULTSERVICE_RESULTNAME": "resultName",
  "RESULTSERVICE_OUTPUT": "output",
  "DEFINITIONMAPPINGSERVICE_PARMMAPPING": "parmMapping",
  "DEFINITIONMAPPINGSERVICE_RESULTMAPPING": "resultMapping",
  "DEFINITIONSERVICEINENTITY_USE": "use",
  "DEFINITIONSERVICEINENTITY_PROVIDER": "provider",
  "DEFINITIONSERVICEPROVIDER_SERVICEID": "serviceId",
  "DEFINITIONSERVICEPROVIDER_SERVICEINTERFACE": "serviceInterface",
  "DEFINITIONSERVICEUSE_PROVIDER": "provider",
  "DEFINITIONSERVICEUSE_SERVICEMAPPING": "serviceMapping",
  "EXECUTABLESERVICEUSE_PROVIDER": "provider",
  "EXECUTABLESERVICEUSE_NAME": "name",
  "EXECUTABLESERVICEUSE_INFO": "info",
  "EXECUTABLESERVICEUSE_SERVICEMAPPING": "serviceMapping",
  "DATATYPE_OPERATIONS": "operations",
  "STRINGABLEVALUE_DESCRIPTION": "description",
  "STRINGABLEVALUE_COMPLEX": "complex",
  "DATAOPERATIONINFO_OPERATIONID": "operationId",
  "_SOURCE": "source",
  "_RELATIONSHIPS": "relationship",
  "DATAOPERATIONINFO_BASEPARM": "baseParm",
  "DATAOPERATIONINFO_DATATYPNAME": "dataTypeName",
  "STRINGABLEVALUE_DATATYPEID": "dataTypeId",
  "STRINGABLEVALUE_OPERATIONID": "operationId",
  "STRINGABLEVALUE_SOURCEDATATYPE": "sourceDataType",
  "STRINGABLEVALUE_TARGETDATATYPE": "targetDataType",
  "STRINGABLEVALUE_PATH": "path",
  "STRINGABLEVALUE_TARGETTYPE": "targetType",
  "RUNTIME_GATEWAY_LOADLIBRARIES": "loadLibraries",
  "RUNTIME_GATEWAY_TESTEXPRESSION": "testExpression",
  "RUNTIME_GATEWAY_SERVICE": "service",
  "STRINGABLEVALUE_RESOURCEID": "resourceId",
  "STRINGABLEVALUE_DEPENDENCY": "dependency",
  "RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO": "operationInfo",
  "MINIAPPSERVLET_COMMAND_LOGIN": "login",
  "MINIAPPSERVLET_COMMAND_LOADMINIAPP": "loadMiniApp",
  "MINIAPPSERVLET_COMMAND_LOADMINIAPP_APPID": "appId",
  "MINIAPPSERVLET_COMMAND_LOADMINIAPP_USERID": "userId",
  "MINIAPPSERVLET_COMMAND_LOADMINIAPP_ENTRY": "entry",
  "MINIAPPSERVLET_COMMAND_CREATEDATA": "createData",
  "MINIAPPSERVLET_COMMAND_CREATEDATA_USERID": "userId",
  "MINIAPPSERVLET_COMMAND_CREATEDATA_APPID": "appId",
  "MINIAPPSERVLET_COMMAND_CREATEDATA_DATANAME": "dataName",
  "MINIAPPSERVLET_COMMAND_CREATEDATA_DATAINFO": "dataInfo",
  "MINIAPPSERVLET_COMMAND_UPDATEDATA": "updateData",
  "MINIAPPSERVLET_COMMAND_UPDATEDATA_ID": "id",
  "MINIAPPSERVLET_COMMAND_UPDATEDATA_DATAINFO": "dataInfo",
  "MINIAPPSERVLET_COMMAND_DELETEDATA": "deleteData",
  "MINIAPPSERVLET_COMMAND_DELETEDATA_DATATYPE": "dataType",
  "MINIAPPSERVLET_COMMAND_DELETEDATA_ID": "id",
  "GATEWAYSERVLET_COMMAND_PARM_RUNTIMEINFO": "runtimeInfo",
  "REQUESTINFO_CLIENTID": "clientId",
  "REQUESTINFO_COMMAND": "command",
  "REQUESTINFO_PARMS": "parms",
  "SERVICEINFO_SERVICE_COMMAND": "command",
  "SERVICEINFO_SERVICE_PARMS": "parms",
  "SERVICESERVLET_REQUEST_TYPE": "type",
  "SERVICESERVLET_REQUEST_SERVICE": "service",
  "SERVICESERVLET_REQUEST_MODE": "mode",
  "SERVICESERVLET_REQUEST_CHILDREN": "children",
  "DEFINITIONAPP_ID": "id",
  "DEFINITIONAPP_CONTEXT": "context",
  "DEFINITIONAPP_ENTRY": "entry",
  "DEFINITIONAPP_APPLICATIONDATA": "applicationData",
  "DEFINITIONAPPENTRYUI_MODULE": "module",
  "DEFINITIONAPPENTRYUI_PROCESS": "process",
  "DEFINITIONAPPENTRYUI_CONTEXT": "context",
  "DEFINITIONAPPMODULE_ROLE": "role",
  "DEFINITIONAPPMODULE_MODULE": "module",
  "DEFINITIONAPPMODULE_STATUS": "status",
  "DEFINITIONAPPMODULE_EVENTHANDLER": "eventHandler",
  "DEFINITIONAPPMODULE_INPUTMAPPING": "inputMapping",
  "DEFINITIONAPPMODULE_OUTPUTMAPPING": "outputMapping",
  "EXECUTABLEAPPENTRY_ID": "id",
  "EXECUTABLEAPPENTRY_MODULE": "module",
  "EXECUTABLEAPPENTRY_CONTEXT": "context",
  "EXECUTABLEAPPENTRY_PROCESS": "process",
  "EXECUTABLEAPPENTRY_APPLICATIONDATA": "applicationData",
  "EXECUTABLEAPPENTRY_INITSCRIPT": "initScript",
  "EXECUTABLEAPPMODULE_MODULEDEFID": "moduleDefId",
  "EXECUTABLEAPPMODULE_ROLE": "role",
  "EXECUTABLEAPPMODULE_MODULE": "module",
  "EXECUTABLEAPPMODULE_INPUTMAPPING": "inputMapping",
  "EXECUTABLEAPPMODULE_OUTPUTMAPPING": "outputMapping",
  "EXECUTABLEAPPMODULE_DATADEPENDENCY": "dataDependency",
  "EXECUTABLEAPPMODULE_EVENTHANDLER": "eventHandler",
  "COMPONENTWITHCONFIGURATION_PAGEINFO": "pageInfo",
  "COMPONENTWITHCONFIGURATION_SERVICE": "service",
  "DEFINITIONEVENTHANDLER_PROCESS": "process",
  "EXECUTABLEEVENTHANDLER_PROCESS": "process",
  "DEFINITIONACTIVITY_PARTID": "partId",
  "DEFINITIONACTIVITY_COMMAND": "command",
  "EXECUTABLEACTIVITY_PARTID": "partId",
  "EXECUTABLEACTIVITY_COMMAND": "command",
  "DEFINITIONACTIVITY_UI": "ui",
  "EXECUTABLEACTIVITY_UI": "ui",
  "DEFINITIONDECORATION_GLOBAL": "global",
  "DEFINITIONDECORATION_UI": "ui",
  "DEFINITIONMODULE_CONTEXT": "context",
  "DEFINITIONMODULE_UI": "ui",
  "DEFINITIONMODULE_PROCESS": "process",
  "DEFINITIONMODULE_DECORATION": "decoration",
  "DEFINITIONMODULEUI_PAGE": "page",
  "DEFINITIONMODULEUI_INPUTMAPPING": "inputMapping",
  "DEFINITIONMODULEUI_OUTPUTMAPPING": "outputMapping",
  "DEFINITIONMODULEUI_EVENTHANDLER": "eventHandler",
  "DEFINITIONMODULEUI_TYPE": "type",
  "DEFINITIONMODULEUI_STATUS": "status",
  "EXECUTABLEMODULE_ID": "id",
  "EXECUTABLEMODULE_CONTEXT": "context",
  "EXECUTABLEMODULE_UI": "ui",
  "EXECUTABLEMODULE_DECORATION": "decoration",
  "EXECUTABLEMODULE_PROCESS": "process",
  "EXECUTABLEMODULE_INITSCRIPT": "initScript",
  "EXECUTABLEMODULEUI_ID": "id",
  "EXECUTABLEMODULEUI_PAGE": "page",
  "EXECUTABLEMODULEUI_INPUTMAPPING": "inputMapping",
  "EXECUTABLEMODULEUI_OUTPUTMAPPING": "outputMapping",
  "EXECUTABLEMODULEUI_EVENTHANDLER": "eventHandler",
  "INFOPAGE_ID": "id",
  "DEFINITIONUICOMMAND_PARM": "parm",
  "DEFINITIONUICOMMAND_RESULT": "result",
  "DEFINITIONUIEVENT_DATA": "data",
  "ELEMENTEVENT_UIID": "uiId",
  "ELEMENTEVENT_EVENT": "event",
  "ELEMENTEVENT_FUNCTION": "function",
  "ELEMENTEVENT_SELECTION": "selection",
  "UIRESOURCEDEFINITION_ID": "id",
  "UIRESOURCEDEFINITION_CONTEXT": "context",
  "UIRESOURCEDEFINITION_TYPE": "type",
  "UIRESOURCEDEFINITION_SCRIPTEXPRESSIONSINCONTENT": "scriptExpressionsInContent",
  "UIRESOURCEDEFINITION_SCRIPTEXPRESSIONINATTRIBUTES": "scriptExpressionInAttributes",
  "UIRESOURCEDEFINITION_SCRIPTEXPRESSIONINTAGATTRIBUTES": "scriptExpressionTagAttributes",
  "UIRESOURCEDEFINITION_SCRIPT": "script",
  "UIRESOURCEDEFINITION_HTML": "html",
  "UIRESOURCEDEFINITION_ELEMENTEVENTS": "elementEvents",
  "UIRESOURCEDEFINITION_TAGEVENTS": "tagEvents",
  "UIRESOURCEDEFINITION_UITAGS": "uiTags",
  "UIRESOURCEDEFINITION_ATTRIBUTES": "attributes",
  "UIRESOURCEDEFINITION_UITAGLIBS": "uiTagLibs",
  "UIRESOURCEDEFINITION_CONSTANTS": "constants",
  "UIRESOURCEDEFINITION_EXPRESSIONS": "expressions",
  "UIRESOURCEDEFINITION_EVENTS": "events",
  "UIRESOURCEDEFINITION_SERVICES": "services",
  "UIRESOURCEDEFINITION_SERVICEPROVIDERS": "serviceProviders",
  "UIRESOURCEDEFINITION_COMMANDS": "commands",
  "UIRESOURCEDEFINITION_TAGNAME": "tagName",
  "UIRESOURCEDEFINITION_TAGCONTEXT": "tagContext",
  "UIRESOURCEDEFINITION_EVENT": "event",
  "UIRESOURCEDEFINITION_EVENTMAPPING": "eventMapping",
  "UIRESOURCEDEFINITION_CONTEXTMAPPING": "contextMapping",
  "UIRESOURCEDEFINITION_COMMANDMAPPING": "commandMapping",
  "UIRESOURCEDEFINITION_SERVICEMAPPING": "serviceMapping",
  "EMBEDEDSCRIPTEXPRESSION_UIID": "uiId",
  "EMBEDEDSCRIPTEXPRESSION_ATTRIBUTE": "attribute",
  "UITAGDEFINITION_NAME": "name",
  "UITAGDEFINITION_SCRIPT": "script",
  "UITAGDEFINITION_ATTRIBUTES": "attributes",
  "UITAGDEFINITION_CONTEXT": "context",
  "UITAGDEFINITION_REQUIRES": "requires",
  "UITAGDEFINITION_EVENT": "event",
  "_DESCRIPTION": "description",
  "GROUP_ID": "id",
  "MINIAPP_ID": "id",
  "MINIAPP_DATAOWNERTYPE": "dataOwnerType",
  "MINIAPP_DATAOWNERID": "dataOwnerId",
  "MINIAPPSETTINGDATA_OWNERINFO": "ownerInfo",
  "MINIAPPSETTINGDATA_DATABYNAME": "dataByName",
  "OWNERINFO_USERID": "userId",
  "OWNERINFO_COMPONENTID": "componentId",
  "OWNERINFO_COMPONENTTYPE": "componentType",
  "SETTINGDATA_ID": "id",
  "SETTINGDATA_NAME": "name",
  "SETTINGDATA_OWNERINFO": "ownerInfo",
  "SETTINGDATA_DATA": "data",
  "USER_ID": "id",
  "USERGROUPMINIAPP_GROUP": "group",
  "USERGROUPMINIAPP_MINIAPP": "miniApp",
  "USERINFO_USER": "user",
  "USERINFO_GROUP": "group",
  "USERINFO_MINIAPP": "miniApp",
  "USERINFO_GROUPMINIAPP": "groupMiniApp",
  "GATEWAYAPPDATA_GATEWAY_APPDATA": "appData",
  "GATEWAYAPPDATA_COMMAND_GETAPPDATA": "getAppData",
  "GATEWAYAPPDATA_COMMAND_GETAPPDATA_OWNER": "owner",
  "GATEWAYAPPDATA_COMMAND_GETAPPDATA_DATANAME": "dataName",
  "GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA": "updateAppData",
  "GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_OWNER": "owner",
  "GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_DATABYNAME": "dataByName"
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("COMMONATRIBUTECONSTANT", COMMONATRIBUTECONSTANT); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var COMMONCONSTANT=

{
  "SERVICECODE_SUCCESS": 200,
  "SERVICECODE_FAILURE": 400,
  "SERVICECODE_EXCEPTION": 5000,
  "ERRORCODE_NEWDATAOPERATION_NOTDEFINED": 1000,
  "ERRORCODE_DATAOPERATION_NOTDEFINED": 1005,
  "ERRORCODE_DATAOPERATION_NOTEXIST": 1010,
  "ERRORCODE_DATATYPE_WRONGTYPE": 1100,
  "ERRORCODE_DATATYPE_WRONGVERSION": 1101,
  "ERRORCODE_APPLICATION_INVALIDCLIENTID": 5201,
  "ERRORCODE_DATA_INVALID": 1102,
  "SERVICECODE_ENTITYOPERATION_FORWARD": 100,
  "ERRORCODE_ENTITYOPERATION_AUTOCOMMIT": 2001,
  "ERRORCODE_ENTITYOPERATION_INVALIDTRANSACTION": 2002,
  "ERRORCODE_ENTITYOPERATION_INVALIDSCOPE": 2003,
  "ERRORCODE_REMOTESERVICE_SUSPEND": 9000,
  "ERRORCODE_REMOTESERVICE_EXCEPTION": 5000,
  "OPERATIONDEF_SCRIPT_JAVASCRIPT": "javascript",
  "OPERATIONDEF_PATH_VERSION": "version",
  "OPERATIONDEF_PATH_PARENT": "parent",
  "ENTITYOPERATION_SCOPE_UNDEFINED": -1,
  "ENTITYOPERATION_SCOPE_GLOBAL": 1,
  "ENTITYOPERATION_SCOPE_ENTITY": 2,
  "ENTITYOPERATION_SCOPE_OPERATION": 3,
  "SYMBOL_KEYWORD": "#",
  "SYMBOL_GROUP": "@",
  "SYMBOL_ENTITYNAME_COMMON": "..",
  "SYMBOL_ENTITYNAME_CURRENT": ".",
  "SEPERATOR_NAMEVALUE": "\u003d",
  "SEPERATOR_ELEMENT": ",",
  "SEPERATOR_SEGMENT": ";",
  "SEPERATOR_PART": ":",
  "SEPERATOR_PATH": ".",
  "SEPERATOR_FULLNAME": ".",
  "SEPERATOR_DETAIL": "|",
  "SEPERATOR_SURFIX": "_",
  "SEPERATOR_PREFIX": "_",
  "SEPERATOR_LEVEL1": ";",
  "SEPERATOR_LEVEL2": "|",
  "SEPERATOR_LEVEL3": ":",
  "SEPERATOR_LEVEL4": ",",
  "SEPERATOR_ARRAYSTART": "[",
  "SEPERATOR_ARRAYEND": "]",
  "SEPERATOR_VARSTART": "{{",
  "SEPERATOR_VAREND": "}}",
  "SEPERATOR_EXPRESSIONSTART": "${",
  "SEPERATOR_EXPRESSIONEND": "}",
  "SEPERATOR_CONTEXT_CATEGARY_NAME": "___",
  "DATAOPERATION_NEWDATA": "new",
  "DATAOPERATION_TOPARENTTYPE": "toParentType",
  "DATAOPERATION_FROMPARENTTYPE": "fromParentType",
  "DATAOPERATION_TOVERSION": "toVersion",
  "DATAOPERATION_FROMVERSION": "fromVersion",
  "DATAOPERATION_GETCHILD": "getChild",
  "DATAOPERATION_GETCHILDDATATYPE": "getChildDatatype",
  "DATAOPERATION_COMPARE": "compare",
  "DATAOPERATION_PARSELITERAL": "parseLiteral",
  "DATAOPERATION_COMPLEX_GETCHILDRENNAMES": "getChildrenNames",
  "DATAOPERATION_COMPLEX_GETCHILDDATA": "getChildData",
  "DATAOPERATION_COMPLEX_GETCHILDDATA_NAME": "name",
  "DATAOPERATION_COMPLEX_GETCHILDDATABYINDEX": "getChildDataByIndex",
  "DATAOPERATION_COMPLEX_SETCHILDDATA": "setChildData",
  "DATAOPERATION_COMPLEX_ISACCESSCHILDBYID": "isAccessChildById",
  "DATAOPERATION_COMPLEX_LENGTH": "length",
  "DATAOPERATION_COMPLEX_ADDCHILD": "addChild",
  "DATAOPERATION_COMPLEX_REMOVECHILD": "removeChild",
  "DATAOPERATION_PARM_BASENAME": "ABCDEFGHIJKLMN",
  "DATAOPERATION_TYPE_NORMAL": "normal",
  "DATAOPERATION_TYPE_NEW": "new",
  "DATAOPERATION_TYPE_CONVERT": "convert",
  "DATAOPERATION_TYPE_CONVERTFROM": "convertFrom",
  "DATAOPERATION_TYPE_TOFORMAT": "toFormat",
  "DATATYPECRITERIA_TYPE_ANY": "any",
  "DATATYPECRITERIA_TYPE_DATATYPEID": "dataTypeId",
  "DATATYPECRITERIA_TYPE_DATATYPEIDS": "dataTypeIds",
  "DATATYPECRITERIA_TYPE_DATATYPERANGE": "dataTypeRange",
  "DATATYPECRITERIA_TYPE_AND": "and",
  "DATATYPECRITERIA_TYPE_OR": "or",
  "DATATYPECRITERIA_TYPE_EXPRESSION": "expression",
  "DATATYPECRITERIA_TYPE_REFERENCE": "reference",
  "DATATYPECRITERIA_TYPE_LITERATE": "literate",
  "EXPRESSION_OPERAND_CONSTANT": "constant",
  "EXPRESSION_OPERAND_VARIABLE": "variable",
  "EXPRESSION_OPERAND_REFERENCE": "reference",
  "EXPRESSION_OPERAND_OPERATION": "operation",
  "EXPRESSION_OPERAND_DATAOPERATION": "dataoperation",
  "EXPRESSION_OPERAND_DATATYPEOPERATION": "datatypeoperation",
  "EXPRESSION_OPERAND_ATTRIBUTEOPERATION": "attributeoperation",
  "EXPRESSION_OPERAND_DATASOURCE": "datasource",
  "EXPRESSION_OPERAND_PATHOPERATION": "pathoperation",
  "EXPRESSION_OPERAND_NEWOPERATION": "newoperation",
  "EXPRESSION_VARIABLE_STATUS_OPEN": "open",
  "EXPRESSION_VARIABLE_STATUS_CLOSE": "close",
  "EXPRESSION_OPERAND_STATUS_NEW": "new",
  "EXPRESSION_OPERAND_STATUS_PROCESSED": "processed",
  "EXPRESSION_OPERAND_STATUS_INVALID": "invalid",
  "EXPRESSION_VARIABLE_THIS": "this",
  "EXPRESSION_VARIABLE_PARENT": "parent",
  "EXPRESSION_VARIABLE_ENTITY": "entity",
  "EXPRESSION_VARIABLE_DATA1": "data1",
  "EXPRESSION_VARIABLE_DATA2": "data2",
  "DATATYPE_RELATIONSHIPTYPE_SELF": "self",
  "DATATYPE_RELATIONSHIPTYPE_ROOT": "root",
  "DATATYPE_RELATIONSHIPTYPE_INTERMEDIA": "intermedia",
  "DATATYPE_CATEGARY_SIMPLE": "simple",
  "DATATYPE_CATEGARY_BLOCK": "block",
  "DATATYPE_CATEGARY_CONTAINER": "container",
  "DATATYPE_CATEGARY_ENTITY": "entity",
  "DATATYPE_CATEGARY_QUERYENTITY": "queryentity",
  "DATATYPE_CATEGARY_REFERENCE": "reference",
  "DATATYPE_CATEGARY_OBJECT": "object",
  "DATATYPE_CATEGARY_DATA": "data",
  "DATATYPE_CATEGARY_SERVICE": "service",
  "DATATYPE_TYPE_INTEGER": "integer",
  "DATATYPE_TYPE_FLOAT": "float",
  "DATATYPE_TYPE_BOOLEAN": "boolean",
  "DATATYPE_TYPE_STRING": "string",
  "DATATYPE_TYPE_CONTAINER_ENTITYATTRIBUTE": "normal",
  "DATATYPE_TYPE_CONTAINER_OPTIONS": "options",
  "DATATYPE_TYPE_CONTAINER_QUERY": "query",
  "DATATYPE_TYPE_CONTAINER_WRAPPER": "wrapper",
  "DATATYPE_TYPE_REFERENCE_NORMAL": "normal",
  "DATATYPE_TYPE_QUERYENTITY_NORMAL": "normal",
  "DATATYPE_PATHSEGMENT_PARENT": "parent",
  "DATATYPE_PATHSEGMENT_LINKED": "linked",
  "UIRESOURCE_TYPE_RESOURCE": "resource",
  "UIRESOURCE_TYPE_TAG": "tag",
  "CONTEXT_ELEMENTTYPE_RELATIVE": "relative",
  "CONTEXT_ELEMENTTYPE_DATA": "data",
  "CONTEXT_ELEMENTTYPE_VALUE": "value",
  "CONTEXT_ELEMENTTYPE_CONSTANT": "constant",
  "CONTEXT_ELEMENTTYPE_NODE": "node",
  "CONTEXT_ELEMENTTYPE_UNKNOW": "unknow",
  "ENTITY_CRITICALVALUE_OTHER": "other",
  "ATTRIBUTE_PATH_CRITICAL": "critical",
  "ATTRIBUTE_PATH_ELEMENT": "element",
  "ATTRIBUTE_PATH_ENTITY": "entity",
  "ATTRIBUTE_PATH_EACH": "each",
  "OPTIONS_TYPE_STATIC": "static",
  "OPTIONS_TYPE_DYNAMIC": "dynamic",
  "OPTIONS_TYPE_DYNAMIC_EXPRESSION": "expression",
  "OPTIONS_TYPE_DYNAMIC_EXPRESSION_ATTRIBUTE": "attribute",
  "OPTIONS_TYPE_QUERY": "query",
  "OPTIONS_TYPE_COMPLEX": "complex",
  "CONFIGUREITEM_ENTITY_ISEMPTYONINIT": "emptyOnInit",
  "EVENTTYPE_ENTITY_OPERATION": 1,
  "EVENTTYPE_ENTITY_MODIFY": 2,
  "EVENTTYPE_ENTITY_NEW ": 3,
  "EVENTTYPE_ENTITY_CLEARUP ": 4,
  "EVENT_ENTITY_CHANGE ": "entityChange",
  "WRAPECLEARUP_PARM_SCOPE": "scope",
  "SORTING_ORDER_ASCEND": 0,
  "SORTING_ORDER_DESCEND": 1,
  "SORTING_TYPE_EXPRESSION": "expression",
  "SORTING_TYPE_ATTRIBUTE": "attribute",
  "SORTING_TYPE_PROGRAMMING": "programming",
  "COMPARE_LESS": -1,
  "COMPARE_LARGER": 1,
  "COMPARE_EQUAL": 0,
  "SERVICEDATA_METADATA_TRANSACTIONID": "transactionId",
  "ENTITYOPERATION_ATTR_ATOM_SET": "ENTITYOPERATION_ATTR_ATOM_SET",
  "ENTITYOPERATIONCODE_ATTR_ATOM_SET": 101,
  "ENTITYOPERATION_ATTR_CRITICAL_SET": "ENTITYOPERATION_ATTR_CRITICAL_SET",
  "ENTITYOPERATIONCODE_ATTR_CRITICAL_SET": 112,
  "ENTITYOPERATION_ATTR_ELEMENT_NEW": "ENTITYOPERATION_ATTR_ELEMENT_NEW",
  "ENTITYOPERATIONCODE_ATTR_ELEMENT_NEW": 113,
  "ENTITYOPERATION_ATTR_ELEMENT_DELETE": "ENTITYOPERATION_ATTR_ELEMENT_DELETE",
  "ENTITYOPERATIONCODE_ATTR_ELEMENT_DELETE": 153,
  "ENTITYOPERATION_ENTITY_OPERATIONS": "ENTITYOPERATION_ENTITY_OPERATIONS",
  "ENTITYOPERATIONCODE_ENTITY_OPERATIONS": 114,
  "ENTITYOPERATION_ENTITY_NEW": "ENTITYOPERATION_ENTITY_NEW",
  "ENTITYOPERATIONCODE_ENTITY_NEW": 102,
  "ENTITYOPERATION_ENTITY_DELETE": "ENTITYOPERATION_ENTITY_DELETE",
  "ENTITYOPERATIONCODE_ENTITY_DELETE": 103,
  "ENTITYOPERATION_TRANSACTION_START": "ENTITYOPERATION_TRANSACTION_START",
  "ENTITYOPERATIONCODE_TRANSACTION_START": 104,
  "ENTITYOPERATION_TRANSACTION_COMMIT": "ENTITYOPERATION_TRANSACTION_COMMIT",
  "ENTITYOPERATIONCODE_TRANSACTION_COMMIT": 105,
  "ENTITYOPERATION_TRANSACTION_ROLLBACK": "ENTITYOPERATION_TRANSACTION_ROLLBACK",
  "ENTITYOPERATIONCODE_TRANSACTION_ROLLBACK": 106,
  "ENTITYOPERATION_QUERY_ENTITY_ADD": "ENTITYOPERATION_QUERY_ENTITY_ADD",
  "ENTITYOPERATIONCODE_QUERY_ENTITY_ADD": 107,
  "ENTITYOPERATION_QUERY_ENTITY_REMOVE": "ENTITYOPERATION_QUERY_ENTITY_REMOVE",
  "ENTITYOPERATIONCODE_QUERY_ENTITY_REMOVE": 108,
  "ENTITYOPERATION_QUERY_ENTITY_MODIFY": "ENTITYOPERATION_QUERY_ENTITY_MODIFY",
  "ENTITYOPERATIONCODE_QUERY_ENTITY_MODIFY": 109,
  "ENTITYOPERATION_ATTR_REFERENCE_SET": "ENTITYOPERATION_ATTR_REFERENCE_SET",
  "ENTITYOPERATIONCODE_ATTR_REFERENCE_SET": 110,
  "ENTITYOPERATION_ENTITYATTR_ADD": "ENTITYOPERATION_ENTITYATTR_ADD",
  "ENTITYOPERATIONCODE_ENTITYATTR_ADD": 111,
  "ENTITYOPERATION_ENTITYATTR_REMOVE": "ENTITYOPERATION_ENTITYATTR_REMOVE",
  "ENTITYOPERATIONCODE_ENTITYATTR_REMOVE": 113,
  "UIRESOURCE_ATTRIBUTE_CONTEXT": "context",
  "UIRESOURCE_ATTRIBUTE_UIID": "nosliwid",
  "UIRESOURCE_ATTRIBUTE_EVENT": "event",
  "UIRESOURCE_ATTRIBUTE_DATABINDING": "data",
  "UIRESOURCE_TAG_PLACEHOLDER": "nosliw",
  "UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX": "nosliw-",
  "UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX": "-tag-start",
  "UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX": "-tag-end",
  "UIRESOURCE_FUNCTION_EXCECUTEEXPRESSION": "excecuteExpression",
  "UIRESOURCE_UIEXPRESSIONFUNCTION_PREFIX": "expression",
  "UIRESOURCE_CONTEXTTYPE_PUBLIC": "public",
  "UIRESOURCE_CONTEXTTYPE_PROTECTED": "protected",
  "UIRESOURCE_CONTEXTTYPE_INTERNAL": "internal",
  "UIRESOURCE_CONTEXTTYPE_PRIVATE": "private",
  "CONTEXTNODE_INFO_MODE": "mode",
  "CONTEXTNODE_INFO_PARENTCONTEXTCATEGARY": "parentContextCategary",
  "UIRESOURCEMAN_SETTINGNAME_SCRIPTLOCATION": "temp.scriptLocation",
  "DATATYPEMAN_SETTINGNAME_SCRIPTLOCATION": "temp.scriptLocation",
  "REMOTESERVICE_LOGIN": "login",
  "REMOTESERVICE_GETUIRESOURCE": "getUIResource",
  "REMOTESERVICE_GETDATATYPES": "getDataTypes",
  "REMOTESERVICE_EXECUTEEXPRESSION": "executeExpression",
  "REMOTESERVICE_GETALLENTITYDEFINITIONS": "getAllEntityDefinitions",
  "REMOTESERVICE_GETENTITYDEFINITIONBYNAMES": "getEntityDefinitionByNames",
  "DATAACCESS_ENTITYSTATUS_NORMAL": 0,
  "DATAACCESS_ENTITYSTATUS_CHANGED": 1,
  "DATAACCESS_ENTITYSTATUS_NEW": 2,
  "DATAACCESS_ENTITYSTATUS_DEAD": 3,
  "APPLICATION_CONFIGURE_DATATYPE": "dataType",
  "APPLICATION_CONFIGURE_ENTITYDEFINITION": "entityDefinition",
  "APPLICATION_CONFIGURE_UIRESOURCE": "uiResource",
  "APPLICATION_CONFIGURE_UITAG": "uiTag",
  "APPLICATION_CONFIGURE_QUERYDEFINITION": "queryDefinition",
  "APPLICATION_CONFIGURE_USERENV": "userEnv",
  "APPLICATION_CONFIGURE_LOGGER": "logger",
  "REMOTESERVICE_TASKTYPE_NORMAL": "normal",
  "REMOTESERVICE_TASKTYPE_GROUP": "group",
  "REMOTESERVICE_TASKTYPE_GROUPCHILD": "groupchild",
  "REMOTESERVICE_GROUPTASK_MODE_ALL": "all",
  "REMOTESERVICE_GROUPTASK_MODE_ALWAYS": "always",
  "SERVICENAME_LOGIN": "login",
  "SERVICENAME_SERVICE": "service",
  "SERVICECOMMAND_GROUPREQUEST": "groupRequest",
  "SCRIPTTYPE_UIRESOURCE": "uiResource",
  "SCRIPTTYPE_DATAOPERATIONS": "dataOperations",
  "SCRIPTTYPE_UITAGS": "uiTags",
  "REFERENCE_TYPE_ABSOLUTE": 0,
  "REFERENCE_TYPE_RELATIVE": 1,
  "TESTRESULT_TYPE_SUITE": "SUITE",
  "TESTRESULT_TYPE_CASE": "CASE",
  "TESTRESULT_TYPE_ITEM": "ITEM",
  "TEST_TYPE_SUITE": "SUITE",
  "TEST_TYPE_CASE": "CASE",
  "TEST_TYPE_ITEM": "ITEM",
  "CONFIGURATION_DEFAULTBASE": "default",
  "STRINGABLE_ATOMICVALUETYPE_STRING": "string",
  "STRINGABLE_ATOMICVALUETYPE_BOOLEAN": "boolean",
  "STRINGABLE_ATOMICVALUETYPE_INTEGER": "integer",
  "STRINGABLE_ATOMICVALUETYPE_FLOAT": "float",
  "STRINGABLE_ATOMICVALUETYPE_ARRAY": "array",
  "STRINGABLE_ATOMICVALUETYPE_MAP": "map",
  "STRINGABLE_ATOMICVALUETYPE_OBJECT": "object",
  "STRINGABLE_VALUESTRUCTURE_ATOMIC": "atomic",
  "STRINGABLE_VALUESTRUCTURE_MAP": "map",
  "STRINGABLE_VALUESTRUCTURE_LIST": "list",
  "STRINGABLE_VALUESTRUCTURE_ENTITY": "entity",
  "STRINGABLE_VALUESTRUCTURE_OBJECT": "object",
  "STRINGALBE_VALUEINFO_ATOMIC": "atomic",
  "STRINGALBE_VALUEINFO_REFERENCE": "reference",
  "STRINGALBE_VALUEINFO_ENTITY": "entity",
  "STRINGALBE_VALUEINFO_ENTITYOPTIONS": "entityOptions",
  "STRINGALBE_VALUEINFO_LIST": "list",
  "STRINGALBE_VALUEINFO_MAP": "map",
  "STRINGALBE_VALUEINFO_OBJECT": "object",
  "STRINGALBE_VALUEINFO_MODE": "mode",
  "STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_ABSOLUTE": "absolute",
  "STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTY": "property",
  "STRINGALBE_VALUEINFO_COLUMN_ATTRPATH_PROPERTYASPATH": "propertyAsPath",
  "UITAG_NAME_INCLUDE": "include",
  "UITAG_NAME_INCLUDE_PARM_SOURCE": "source",
  "UITAG_PARM_CONTEXT": "context",
  "UITAG_PARM_EVENT": "event",
  "UITAG_PARM_COMMAND": "command",
  "UITAG_PARM_SERVICE": "service",
  "UIRESOURCE_CONTEXTINFO_INSTANTIATE": "instantiate",
  "UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL": "manual",
  "UIRESOURCE_CONTEXTINFO_INSTANTIATE_AUTO": "auto",
  "UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION": "relativeConnection",
  "UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL": "physical",
  "UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL": "logical",
  "DATAOPERATION_VAR_TYPE_IN": "parm",
  "DATAOPERATION_VAR_TYPE_OUT": "out",
  "RUNTIME_RESOURCE_TYPE_OPERATION": "operation",
  "RUNTIME_RESOURCE_TYPE_DATATYPE": "datatype",
  "RUNTIME_RESOURCE_TYPE_CONVERTER": "converter",
  "RUNTIME_RESOURCE_TYPE_JSLIBRARY": "jslibrary",
  "RUNTIME_RESOURCE_TYPE_JSHELPER": "jshelper",
  "RUNTIME_RESOURCE_TYPE_JSGATEWAY": "jsGateway",
  "RUNTIME_RESOURCE_TYPE_UIRESOURCE": "uiResource",
  "RUNTIME_RESOURCE_TYPE_UITAG": "uiTag",
  "RUNTIME_RESOURCE_TYPE_UIMODULE": "uiModule",
  "RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION": "uiModuleDecoration",
  "RUNTIME_RESOURCE_TYPE_UIAPPENTRY": "uiAppEntry",
  "RUNTIME_RESOURCE_TYPE_UIAPPDECORATION": "uiAppDecoration",
  "RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE": "uiAppConfigure",
  "RUNTIME_RESOURCE_TYPE_PROCESS": "process",
  "RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN": "activityPlugin",
  "RUNTIME_RESOURCE_TYPE_MATCHER": "matcher",
  "RUNTIME_LANGUAGE_JS": "javascript",
  "RUNTIME_ENVIRONMENT_RHINO": "rhino",
  "RUNTIME_ENVIRONMENT_BROWSER": "browser",
  "RUNTIME_LANGUAGE_JS_GATEWAY": "runtime.gateway",
  "CATEGARY_NAME": "CATEGARY_NAME",
  "DATATASK_TYPE_EXPRESSION": "expression",
  "DATATASK_TYPE_DATASOURCE": "dataSource",
  "EXPRESSIONTASK_STEPTYPE_EXPRESSION": "expression",
  "EXPRESSIONTASK_STEPTYPE_BRANCH": "branch",
  "EXPRESSIONTASK_STEPTYPE_LOOP": "loop",
  "MINIAPPSERVICE_TYPE_SERVICE": "service",
  "MINIAPPDATA_TYPE_SETTING": "setting",
  "MINIAPPUIENTRY_NAME_MAINMOBILE": "main_mobile",
  "MINIAPPUIENTRY_MAINMOBILE_MODULE_SETTING": "setting",
  "MINIAPPUIENTRY_MAINMOBILE_MODULE_APPLICATION": "application",
  "MINIAPPUIENTRY_MAINMOBILE_DATA_MAIN": "main",
  "MINIAPPUIENTRY_MAINMOBILE_SERVICE_MAIN": "main",
  "ACTIVITY_TYPE_START": "start",
  "ACTIVITY_TYPE_END": "end",
  "ACTIVITY_TYPE_EXPRESSION": "expression",
  "ACTIVITY_TYPE_LOOP": "loop",
  "ACTIVITY_TYPE_BRANCH": "branch",
  "ACTIVITY_TYPE_CALLPROCESS": "callProcess",
  "ACTIVITY_RESULT_SUCCESS": "success",
  "ACTIVITY_RESULT_FAIL": "fail",
  "ACTIVITY_RESULT_EXCEPTION": "exception",
  "ACTIVITY_OUTPUTVARIABLE_OUTPUT": "output",
  "SERVICE_RESULT_SUCCESS": "success",
  "SERVICE_RESULT_FAIL": "fail",
  "SERVICE_RESULT_EXCEPTION": "exception",
  "SERVICE_OUTPUTNAME_OUTPUT": "output",
  "NOSLIW_RESERVE_ATTRIBUTE": "nosliwattribute_",
  "NOSLIW_RESERVE_ATTRIBUTE_PLACEHOLDER": "nosliwattribute_placeholder",
  "DECORATION_COMMAND_COMMANDPROCESS": "commandProcess",
  "DECORATION_COMMAND_EVENTPROCESS": "eventProcess",
  "DATAASSOCIATION_RELATEDENTITY_DEFAULT": "default",
  "DATAASSOCIATION_RELATEDENTITY_SELF": "self",
  "GLOBAL_VALUE_DEFAULT": "default",
  "CONTEXTSTRUCTURE_TYPE_FLAT": "flat",
  "CONTEXTSTRUCTURE_TYPE_NOTFLAT": "notFlat",
  "CONTEXTSTRUCTURE_TYPE_EMPTY": "empty",
  "DATAASSOCIATION_TYPE_MAPPING": "mapping",
  "DATAASSOCIATION_TYPE_MIRROR": "mirror",
  "DATAASSOCIATION_TYPE_NONE": "none",
  "MINIAPP_DATAOWNER_APP": "app",
  "MINIAPP_DATAOWNER_GROUP": "group",
  "GATEWAY_OPTIONS": "options"
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("COMMONCONSTANT", COMMONCONSTANT); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_CONSTANT=
{
	  "SCRIPTOBJECT_VARIABLE_RESOURCEVIEW": "code",

	  "EVENT_EVENTNAME_ALL": "all",
	  
	  //wrapper event
	  "WRAPPER_EVENT_SET" : "EVENT_WRAPPER_SET",                        //the value get set					
	  "WRAPPER_EVENT_ADDELEMENT" : "EVENT_WRAPPER_ADDELEMENT",          //element is added to container
	  "WRAPPER_EVENT_DELETEELEMENT" : "EVENT_WRAPPER_DELETEELEMENT",    //element is removed from container.
	  "WRAPPER_EVENT_FORWARD" : "EVENT_WRAPPER_FORWARD",				//forward original event information to child
	  "WRAPPER_EVENT_CHANGE" : "EVENT_WRAPPER_CHANGE",					//indicate something changes on itself, need to update data 
	  "WRAPPER_EVENT_REFRESH" : "EVENT_WRAPPER_REFRESH",				//indicate children or itself has some change, sometimes need to refresh 
	  "WRAPPER_EVENT_DELETE" : "EVENT_WRAPPER_DELETE",                //delete means the path does not exist anymore. all the resources related with this wrapper should be destroy (variable, child wrapper)
	  "WRAPPER_EVENT_CLEARUP_BEFORE" : "EVENT_WRAPPER_CLEARUP_BEFORE",                //clear up means release resource
	  "WRAPPER_EVENT_CLEARUP_AFTER" : "EVENT_WRAPPER_CLEARUP_AFTER",                //clear up means release resource

	  "EACHELEMENTCONTAINER_EVENT_RESET" : "EACHELEMENTCONTAINER_EVENT_RESET",   				//element container need to loop again
	  "EACHELEMENTCONTAINER_EVENT_NEWELEMENT" : "EACHELEMENTCONTAINER_EVENT_NEWELEMENT",        //new element with element variable in event data. only on variable, not on wrapper 
	  "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT" : "EACHELEMENTCONTAINER_EVENT_DELETEELEMENT",  //delete element with element variable in event data. only on variable, not on wrapper 

	  
	  //operation on wrapper
	  "WRAPPER_OPERATION_SET" : "WRAPPER_OPERATION_SET",
	  "WRAPPER_OPERATION_ADDELEMENT" : "WRAPPER_OPERATION_ADDELEMENT",      //
	  "WRAPPER_OPERATION_DELETEELEMENT" : "WRAPPER_OPERATION_DELETEELEMENT",
	  "WRAPPER_OPERATION_GET" : "WRAPPER_OPERATION_GET",
	  "WRAPPER_OPERATION_DELETE" : "WRAPPER_OPERATION_DELETE",		//delete means the path does not exist anymore. all the resources related with this wrapper should be destroy (variable, child wrapper) 

	  //wrapper variable events
	  "WRAPPERVARIABLE_EVENT_SETDATA" : "WRAPPERVARIABLE_EVENT_SETDATA",
	  "WRAPPERVARIABLE_EVENT_CLEARUP" : "WRAPPERVARIABLE_EVENT_CLEARUP",    //clear up resource

	  "WRAPPERVARIABLE_EVENT_NEW" : "WRAPPERVARIABLE_EVENT_NEW",
	  "WRAPPERVARIABLE_EVENT_DESTROY" : "WRAPPERVARIABLE_EVENT_DESTROY",
	  
	  //wrapper type
	  "WRAPPER_TYPE_OBJECT" : "object",
	  "WRAPPER_TYPE_APPDATA" : "appdata",
	  "WRAPPER_TYPE_ENTITY" : "entity",

	  "DATA_TYPE_OBJECT" : "object",
	  "DATA_TYPE_APPDATA" : "appdata",
	  "DATA_TYPE_DYNAMIC" : "dynamic",
	  
	  //context events
	  "CONTEXT_EVENT_BEFOREUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE",
	  "CONTEXT_EVENT_UPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_UPDATE",
	  "CONTEXT_EVENT_AFTERUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE",
	  "CONTEXT_ATTRIBUTE_THISCONTEXT" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT",

	  //expression event
	  "EXPRESSION_EVENT_DONE" : "EXPRESSION_EVENT_DONE",
	  
	  
	  "LIFECYCLE_UIRESOURCE_EVENT_BEFOREINITCONTEXT" : "onBeforeInitContext",
	  "LIFECYCLE_UIRESOURCE_EVENT_AFTERINITCONTEXT" : "onAfterInitContext",

	  "LIFECYCLE_CONTEXT_EVENT_CLEARUP" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_CLEARUP",
	  "LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_BEFOREUPDATE",
	  "LIFECYCLE_CONTEXT_EVENT_UPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_UPDATE",
	  "LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_EVENT_AFTERUPDATE",
	  "LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT" : "NOSLIWCONSTANT.LIFECYCLE_CONTEXT_ATTRIBUTE_THISCONTEXT",
	  
	  
	  "LIFECYCLE_RESOURCE_EVENT_INIT" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT",
	  "LIFECYCLE_RESOURCE_EVENT_DEACTIVE" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE",
	  "LIFECYCLE_RESOURCE_EVENT_SUSPEND" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND",
	  "LIFECYCLE_RESOURCE_EVENT_RESUME" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME",
	  "LIFECYCLE_RESOURCE_EVENT_DESTROY" : "NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY",
	  "LIFECYCLE_RESOURCE_ATTRIBUTE_THISCONTEXT" : "resourceThisContext",
	  "LIFECYCLE_RESOURCE_EVENT_NOTRANSITION" : "LIFECYCLE_RESOURCE_EVENT_NOTRANSITION",
	  "LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION" : "LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION",
	  "LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION" : "LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION",
	  //status of resource : start - active - suspended - dead
	  
	  "LIFECYCLE_RESOURCE_STATUS_START" : "START",
	  "LIFECYCLE_RESOURCE_STATUS_ACTIVE" : "ACTIVE",
	  "LIFECYCLE_RESOURCE_STATUS_SUSPENDED" : "SUSPENDED",
	  "LIFECYCLE_RESOURCE_STATUS_DEAD" : "DEAD",

	  "LIFECYCLE_COMPONENT_STATUS_INIT" : "INIT",
	  "LIFECYCLE_COMPONENT_STATUS_ACTIVE" : "ACTIVE",
	  "LIFECYCLE_COMPONENT_STATUS_SUSPENDED" : "SUSPENDED",
	  "LIFECYCLE_COMPONENT_STATUS_DEAD" : "DEAD",

	  "LIFECYCLE_COMPONENT_TRANSIT_DESTROY" : "INIT_DEAD",
	  "LIFECYCLE_COMPONENT_TRANSIT_ACTIVE" : "INIT_ACTIVE",
	  "LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE" : "_INIT_ACTIVE",
	  "LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE" : "ACTIVE_INIT",
	  "LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE_REVERSE" : "_ACTIVE_INIT",
	  "LIFECYCLE_COMPONENT_TRANSIT_SUSPEND" : "ACTIVE_SUSPENDED",
	  "LIFECYCLE_COMPONENT_TRANSIT_SUSPEND_REVERSE" : "_ACTIVE_SUSPENDED",
	  "LIFECYCLE_COMPONENT_TRANSIT_RESUME" : "SUSPENDED_ACTIVE",
	  "LIFECYCLE_COMPONENT_TRANSIT_RESUME_REVERSE" : "_SUSPENDED_ACTIVE",

	  
	  //the requester type
	  "REQUESTER_TYPE_UITAG" : "uiTag",						//requester is a ui tag
	  "REQUESTER_TYPE_SERVICE" : "service",					//requester is from service
	  
	  //request type
	  "REQUEST_TYPE_SERVICE" : 			"service",						//request type is service
	  "REQUEST_TYPE_SET" : 				"set",						//
	  "REQUEST_TYPE_SEQUENCE" : 		"sequence",						//
	  "REQUEST_TYPE_UITAG" : 			"uiTag",						//
	  "REQUEST_TYPE_DATAOPERATION" : 	"dataOperation",						//
	  "REQUEST_TYPE_SIMPLE" : 			"simple",						//
	  "REQUEST_TYPE_EXECUTOR" : 		"executor",						//
	  "REQUEST_TYPE_REMOTE" : 		"executor",						//

	  "REQUEST_TYPE_DATAOPERATION" : 	"dataoperation",			//request type is data operation
	  "REQUEST_TYPE_WRAPPEROPERATION" : "wrapperoperation",		//request type is operation on wrapper
	  
	  "REQUEST_STATUS_INIT" : 			"init",
	  "REQUEST_STATUS_ACTIVE" : 		"active",
	  "REQUEST_STATUS_ALMOSTDONE" : 	"almostDone",
	  "REQUEST_STATUS_DONE" : 			"done",
	  
	  "UITAG_ATTRIBUTE_ERRORHANDLER" : "errorHandler",

	  "UITAG_DATABINDING_NAME_DEFAULT" : "data",
	  
	  //remote service result 
	  "REMOTESERVICE_RESULT_SUCCESS" : 0,					//success result     		
	  "REMOTESERVICE_RESULT_ERROR" : 1,     				//error result (logic error)
	  "REMOTESERVICE_RESULT_EXCEPTION" : 2,     			//exception result (ajax error which is not logic error)

	  //remote service task status 
	  "REMOTESERVICE_SERVICESTATUS_PROCESSING" : 1,				//under processing
	  "REMOTESERVICE_SERVICESTATUS_SUCCESS" : 2,				//processed and get success result     		
	  "REMOTESERVICE_SERVICESTATUS_EXCEPTION" : 3,					//processed and get exception result     		
	  "REMOTESERVICE_SERVICESTATUS_QUEUE" : 4,					//service waiting for process     		
	  "REMOTESERVICE_SERVICESTATUS_FAIL" : 5,				//
	  
	  //remote service type
	  "REMOTESERVICE_TASKTYPE_NORMAL" : "normal",     		//single service
	  "REMOTESERVICE_TASKTYPE_GROUP" : "group",				//a group of service
	  "REMOTESERVICE_TASKTYPE_GROUPCHILD" : "groupchild",	//service within service group

	  //group task mode
	  "REMOTESERVICE_GROUPTASK_MODE_ALL" : "all",			//group task is success only when all child tasks are success
	  "REMOTESERVICE_GROUPTASK_MODE_ALWAYS" : "always",		//group task is always success 
	  
	  //context types
	  "CONTEXT_TYPE_QUERYENTITY" : "queryentity",			//query entity
	  "CONTEXT_TYPE_ENTITY" : "entity",						//entity data
	  "CONTEXT_TYPE_CONTAINER" : "container",				//container 
	  "CONTEXT_TYPE_DATA" : "data",							//any data
	  "CONTEXT_TYPE_OBJECT" : "object",						//normal object 
	  "CONTEXT_TYPE_SERVICE" : "service",					//service
	  
	  //type for different object
	  "TYPEDOBJECT_TYPE_EVENTOBJECT" : 			100, 
	  "TYPEDOBJECT_TYPE_VALUE" : 				0, 
	  "TYPEDOBJECT_TYPE_DATA" : 				1, 
	  "TYPEDOBJECT_TYPE_WRAPPER" :				2, 
	  "TYPEDOBJECT_TYPE_VARIABLE" : 			3, 
	  "TYPEDOBJECT_TYPE_VARIABLEWRAPPER" : 		200, 
	  "TYPEDOBJECT_TYPE_REQUEST" : 				4, 
	  "TYPEDOBJECT_TYPE_CONTEXTVARIABLE" : 		5, 
	  "TYPEDOBJECT_TYPE_CONTEXT" : 				6, 
	  "TYPEDOBJECT_TYPE_EXTENDEDCONTEXT" : 		7, 
	  "TYPEDOBJECT_TYPE_UIVIEW" :		 		8, 
	  "TYPEDOBJECT_TYPE_UIMODULE" :		 		9, 
	  "TYPEDOBJECT_TYPE_UIRESOURCEVIEW" : 		10, 
	  "TYPEDOBJECT_TYPE_UITAG" :		 		11, 
	  "TYPEDOBJECT_TYPE_APPMODULE" :		 	12, 
	  "TYPEDOBJECT_TYPE_APPMODULEUI" :		 	13, 
	  "TYPEDOBJECT_TYPE_CONFIGURES" :		 	14, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_DATASET" :	 			15, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA" :	 		16, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION" :	 					17, 
	  "TYPEDOBJECT_TYPE_PROCESS" :	 							18, 
	  "TYPEDOBJECT_TYPE_DATAASSOCIATION_EXTERNALMAPPING": 		19,
	  "TYPEDOBJECT_TYPE_ERROR":							 		20,
	  "TYPEDOBJECT_TYPE_COMPONENTDECORATION":			 		21,
	  "TYPEDOBJECT_TYPE_COMPONENTCONFIGURE":			 		22,
	  
	  //status of request
	  "REQUEST_STATUS_INIT" :			0, 
	  "REQUEST_STATUS_PROCESSING" :		1, 
	  "REQUEST_STATUS_DONE" :			2, 
	
	  "REQUEST_EVENT_NEW" : "NOSLIWCONSTANT.REQUEST_EVENT_NEW",
	  "REQUEST_EVENT_ACTIVE" : "NOSLIWCONSTANT.REQUEST_EVENT_ACTIVE",
	  "REQUEST_EVENT_ALMOSTDONE" : "NOSLIWCONSTANT.REQUEST_EVENT_ALMOSTDONE",
	  "REQUEST_EVENT_DONE" : "NOSLIWCONSTANT.REQUEST_EVENT_DONE",

	  "REQUEST_EVENT_INDIVIDUAL_SUCCESS" : "NOSLIWCONSTANT.REQUEST_EVENT_INDIVIDUAL_SUCCESS",
	  "REQUEST_EVENT_INDIVIDUAL_ERROR" : "NOSLIWCONSTANT.REQUEST_EVENT_INDIVIDUAL_ERROR",
	  "REQUEST_EVENT_INDIVIDUAL_EXCEPTION" : "NOSLIWCONSTANT.REQUEST_EVENT_INDIVIDUAL_EXCEPTION",
 
	  
	  "REQUESTPROCESS_EVENT_START" : "NOSLIWCONSTANT.REQUESTPROCESS_EVENT_START",
	  "REQUESTPROCESS_EVENT_DONE" : "NOSLIWCONSTANT.REQUESTPROCESS_EVENT_DONE",

	  "REQUESTRESULT_EVENT_SUCCESS" : "SUCCESS",
	  "REQUESTRESULT_EVENT_ERROR" : "ERROR",
	  "REQUESTRESULT_EVENT_EXCEPTION" : "EXCEPTION",
	  
	  "UIRESOURCE_FUNCTION_INIT" : 		"init",
	  "UIRESOURCE_FUNCTION_DESTROY" : 	"destroy",
	  
	  "PAGE_COMMAND_REFRESH" : "refresh",

	  "IODATASET_EVENT_CHANGE" : "IODATASET_EVENT_CHANGE",
	  
	  "MODULE_EVENT_UIEVENT" : "MODULE_EVENT_UIEVENT",

	  "APP_EVENT_MODULEEVENT" : "APP_EVENT_MODULEEVENT",
		  
	  "EVENT_COMPONENT_VALUECHANGE" : "EVENT_COMPONENT_VALUECHANGE"
	  
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("CONSTANT", node_CONSTANT); 

})(packageObj);
var library = nosliw.getPackage("service");
//get/create package
var packageObj = library.getChildPackage("loggingservice");    

(function(packageObj){
	//get used node
	var node_runtimeName;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createLoggingService = function(){
	var loc_logging;

	var loc_buildMessage = function(arguments){
		var out = "";
		for(var i in arguments){
			out = out + " " + arguments[i];
		}
		return out;
	}
	
	var loc_rhinoLogFun = function(){
//		java.lang.System.out.println(loc_buildMessage(arguments));
	}

	var loc_rhinoErrorFun = function(){
//		java.lang.System.err.println(loc_buildMessage(arguments));
	}
	
	var loc_getDefaultLogging = function(){
		return  {
				trace : loc_rhinoLogFun,
				debug : loc_rhinoLogFun,
				info : loc_rhinoLogFun,
				warn : loc_rhinoLogFun,
				error : loc_rhinoLogFun,
				fatal : loc_rhinoLogFun
			};
	};
	
	var loc_getLogging = function(){
		if(loc_logging==undefined){
			if(node_runtimeName=="rhino"){
				loc_logging = loc_getDefaultLogging(); 
			}
			else{
				if (typeof log4javascript !== 'undefined') {
					loc_logging = log4javascript.getDefaultLogger();
				}
				else{
					loc_logging = loc_getDefaultLogging(); 
				}
			}
		}
		return loc_logging;
	}
	
	var loc_processArguments = function(args){
		var out = [];
		for(var i in args){
			try{
				out.push(JSON.stringify(args[i]==undefined?"undefined":args[i]));
			}
			catch(err){
			}
		}
		return out;
	}
	
	loc_out = {
		trace : function(){
			loc_getLogging().trace.apply(loc_logging, loc_processArguments(arguments));
		},
		debug : function(){			
			loc_getLogging().debug.apply(loc_logging, loc_processArguments(arguments));
		},
		info : function(){			
			loc_getLogging().info.apply(loc_logging, loc_processArguments(arguments));
		},
		warn : function(){			
			loc_getLogging().warn.apply(loc_logging, loc_processArguments(arguments));
		},
		error : function(){			
			loc_getLogging().error.apply(loc_logging, loc_processArguments(arguments));
		},
		fatal : function(){			
			loc_getLogging().fatal.apply(loc_logging, loc_processArguments(arguments));
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("runtime.name", function(){node_runtimeName = this.getData();});

//Register Node by Name
packageObj.createChildNode("createLoggingService", node_createLoggingService); 

})(packageObj);
var library = nosliw.getPackage("common");
//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_makeObjectWithType;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "EVENT";

	var node_EventInfo = function(eventName, eventData, request){
		this.eventName = eventName;
		this.eventData = eventData;
		this.request = request;
	};
	
	var node_createEventObject = function(){
		var loc_backboneEventObj = _.extend({}, Backbone.Events);
		var loc_listeners = [];
	
		var out = {
				pri_getBackboneEventObj : function(){		return loc_backboneEventObj;		},
				
				/*
				 * trigger event on source
				 * eventName : event name
				 * parms : can be any multiple value to transfer to handler 
				 */
				triggerEvent : function(eventName){		loc_backboneEventObj.trigger(eventName, arguments);		},

				/*
				 * register listener to source
				 * 		listener : event object
				 */
				registerListener : function(eventName, listener, handler, thisContext){
					var that = thisContext;
					if(that==undefined){
						if(this.getBaseObject!=null)   that = this.getBaseObject();
					}
					if(eventName===undefined)  eventName = node_CONSTANT.EVENT_EVENTNAME_ALL; 
					
					//for event in backbone.js, the parms are different depending on the event type
					//for "all" event, the first parm is event name
					//for other event, the first parm is the beginning of data
					var isAllEvent = false;
					if(node_CONSTANT.EVENT_EVENTNAME_ALL===eventName)  isAllEvent = true;
					
					if(listener==undefined){
						//if listener is not provided, then create one
						listener = node_createEventObject();
					}
					
					listener.pri_getBackboneEventObj().listenTo(loc_backboneEventObj, eventName, function(parm1, parm2){
						//within this method, "this" refer to listenerEventObj
						//we need to set "this" as source
						var parms;
						if(isAllEvent===true)		handler.apply(that, parm2);
						else		handler.apply(that, parm1);
					});
					loc_listeners.push(listener);

					//return listener object
					return listener;
				},
				
				/*
				 * stop listener from listenering any events
				 */
				unregister : function(listener){	listener.pri_getBackboneEventObj().stopListening(loc_backboneEventObj);	},
				
				unregisterAllListeners : function(){
					var that = this;
					//unregister all listeners
					_.each(loc_listeners, function(listener, key){
						that.unregister(listener);
					});
				},
				
				clearup : function(){
					//stop listening to other 
					this.pri_getBackboneEventObj().stopListening();
					//unregister all listeners
					this.unregisterAllListeners();
					loc_listeners = [];
				}
		};
		
		out = node_makeObjectWithType(out, node_CONSTANT.TYPEDOBJECT_TYPE_EVENTOBJECT);
		return out;
	};
	
	/*
	 * build an object with event obj
	 */
	var loc_makeObjectWithEvent = function(obj){
		var eventObj = node_createEventObject();
		return node_buildInterface(obj, INTERFACENAME, eventObj);
	};
	
	var node_getEventInterface = function(object){
		var eventObj = node_getInterface(object, INTERFACENAME);
		if(eventObj==undefined){
			var obj = loc_makeObjectWithEvent(object);
			eventObj = node_getEventInterface(obj);
		}
		return eventObj;
	};

/**
 * utility object containing all the methods related with events
 * 		listen to event
 * 		trigger event
 * 		unregisterEvent
 * backbone implements all the event behavior
 * however, backbone pollute original object by adding many new attribute
 * therefore, we add an attribute to original object, and that attribute is the source and listener object for backbone
 */
var node_utility = 
	{
		/*
		 * get event object from value
		 * if value is event object, then return value
		 * if value contains event object interface, then get interface object and return it
		 * if value not contains event object interface, then build event object interface, and return it 
		 */
		getEventObject : function(value){
			var out;
			if(node_getObjectType(value)==node_CONSTANT.TYPEDOBJECT_TYPE_EVENTOBJECT){
				out = value;
			}
			else{
				out = node_getEventInterface(value);
			}
			return out;
		},
	
		/*
		 * trigger event on source
		 */
		triggerEvent : function(source, eventName, data){
			var parms = [];
			for(var i=1; i<arguments.length; i++){
				parms.push(arguments[i]);
			}
			var eventObject = this.getEventObject(source);
			eventObject.triggerEvent.apply(eventObject, parms);
		},

		/*
		 * register listener to source
		 */
		registerListener : function(listener, source, eventName, handler, thisContext){
			var listenerEventObject = this.getEventObject(listener);
			var sourceEventObject = this.getEventObject(source);
			sourceEventObject.registerListener(eventName, listenerEventObject, handler, thisContext);
		},
		
		/*
		 * stop listener from listenering any events
		 */
		unregister : function(source, listener){
			this.getEventObject(source).unregister(this.getEventObject(listener));
		},
		
		unregisterAllListeners : function(source, listeners){
			var that = this;
			//unregister all listeners
			_.each(listeners, function(listener, key){
				that.unregister(source, listener);
			});
		},
	};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createEventObject", node_createEventObject); 
packageObj.createChildNode("utility", node_utility); 
packageObj.createChildNode("EventInfo", node_EventInfo); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("interface");    

(function(packageObj){
	//get used node
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var interfaceAttributeName = "____Interface";

/**
 * Append interface object to base object
 */
var loc_getInterfaceObject = function(baseObject, createIfNotExist){
	if(baseObject==undefined)  return undefined;
	
	var interfaceObj = baseObject[interfaceAttributeName];
	if(interfaceObj==undefined && createIfNotExist===true){
		interfaceObj = {};
		baseObject[interfaceAttributeName] = interfaceObj;

		//add "getInterfaceObject" method to baseObject
		baseObject.getInterfaceObject = function(name){
			if(name==undefined)   return interfaceObj;
			else   return interfaceObj[name];
		}
	}
	return interfaceObj;
};

var node_buildInterface = function(baseObject, name, newInterfaceObj){
	var interfaceBase = loc_getInterfaceObject(baseObject, true);
	
	//add "getBaseObject" method to interfaceObject
	newInterfaceObj.getBaseObject = function(){
		return baseObject;
	};

	//store interface object as attribute of baseObject
	baseObject["interfaceObject"+node_basicUtility.capitalizeFirstLetter(name)] = newInterfaceObj;
	
	interfaceBase[name] = newInterfaceObj;
	
	//execute bind callback (bindBaseObject)
	if(newInterfaceObj.bindBaseObject!=undefined){
		newInterfaceObj.bindBaseObject(baseObject);
	}
	
	return baseObject;
};

var node_getInterface = function(baseObject, name){
	var interfaceObj = loc_getInterfaceObject(baseObject, false);
	if(interfaceObj===undefined)  return undefined;
	else return interfaceObj[name];
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildInterface", node_buildInterface); 
packageObj.createChildNode("getInterface", node_getInterface); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("lifecycle");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
	var node_getObjectName;
	var node_getObjectType;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "lifecycle";
	
/*
 * utility functions to build lifecycle object
 */
var node_makeObjectWithLifecycle = function(baseObject, lifecycleCallback, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createResourceLifecycle(thisContext==undefined?baseObject:thisContext, lifecycleCallback));
};
	
var node_getLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var node_destroyUtil = function(baseObject, request){
	if(baseObject.destroy!=undefined)    baseObject.destroy(request);
	else{
		var lifecycle = node_getLifecycleInterface(baseObject);
		if(lifecycle!=undefined)	lifecycle.destroy(request);
	}
};

/**
 * create resource lifecycle object which provide basic lifecycle method and status
 * all the thisContext for life cycle method is either loc_thisContext or this
 * the transition of status can be monitored by register event listener, so that when status transition is done, the listener will be informed
 */
var loc_createResourceLifecycle = function(thisContext, lifecycleCallback){
	//this context for lifycycle callback method
	var loc_thisContext = thisContext;
	
	//name for this lifecycle object, it can be used in logging
//	var loc_name = name;
	//status: start with START status
	var loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
	//wether lifecycle object is under transit
	var loc_inTransit = false;	

	//life cycle call back including all call back method
	var loc_lifecycleCallback = lifecycleCallback==undefined? {}:lifecycleCallback;
	
	
	/*
	 * get this context
	 */
	var loc_getThisContext = function(){
		return loc_thisContext;
	};

	/*
	 * method called when transition is finished
	 */
	var loc_finishStatusTransition = function(oldStatus, status){
		loc_inTransit = false;
		node_eventUtility.triggerEvent(loc_out.getBaseObject(), node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, {
			oldStatus : oldStatus,
			newStatus : status,
		});
	};
	
	//method for init event
	var loc_onResourceInit = function(args, handlers, requestInfo){
		//if resource has been inited, then just do nothing
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT];
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, args);
		
		return loc_processStatuesResult(initResult, loc_out.finishInit, handlers, requestInfo);
	};

	
	var loc_processStatuesResult = function(result, switchFun, handlers, requestInfo){
		if(result==true || result==undefined){
			//success finish
			switchFun.call(loc_out);
			return true;
		}
		if(result==false)   return  false;           //not finish, wait for finish method get called
		
		var entityType = node_getObjectType(result);
		if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
			//if return request, then build wrapper request
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ProcessLifecycleResult", {}), handlers, requestInfo);
			out.addRequest(result);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("LifecycleStateTransit", {}), 
				function(requestInfo){
					switchFun.call(loc_out);
				}));
			return out;
		}
		//fallback: not switch status
		return false;
	};
	
	//method for init event
	var loc_onResourceDeactive = function(){
		//if resource is START OR DEAD, then just do nothing
		if(loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START || loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDeactive is called
		if(initResult!=false)		loc_out.finishDeactive();
	};
	
	//method for destroy method
	var loc_onResourceDestroy = function(){
		if(loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY];
		
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDestroy is called
		if(initResult!=false)		loc_out.finishDestroy();
	};

	//method for suspend method
	var loc_onResourceSuspend = function(){
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishSuspend is called
		if(initResult!=false)		loc_out.finishSuspend();
	};

	//method for resume method
	var loc_onResourceResume = function(){
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishResume is called
		if(initResult!=false)		loc_out.finishResume();
	};
	
	var loc_out = {
		init : function(){
			loc_onResourceInit.call(this, arguments);	
		},

		//must have handlers and requestInfo as last two parms
		initRequest : function(){	
			return loc_onResourceInit.apply(this, [arguments, 
				node_requestUtility.getHandlersFromFunctionArguments(arguments), 
				node_requestUtility.getRequestInfoFromFunctionArguments(arguments)]);	
		},
			
		finishInit : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		destroy : function(){
			loc_onResourceDestroy.apply(this, arguments);
		},
		
		finishDestroy : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		refresh : function(){
			var oldStatus = this.getResourceStatus();
			
			var listener = {};
			var that = this;
			this.registerEventListener(listener, function(event, status){
				if(status==oldStatus){
					that.unregisterEventListener(listener);
				}
				else{
					if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START){
						that.init();
					}
					else if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
						if(oldStatus==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
							that.suspend();
						}
					}
				}
			});
			
			loc_onResourceDeactive.apply(this, arguments);
		},
		
		finishDeactive : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		suspend : function(){
			loc_onResourceSuspend.apply(this, arguments);
		},
		
		finishSuspend : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		resume : function(){
			loc_onResourceResume.apply(this, arguments);
		},
		
		finishResume : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		getResourceStatus : function(){
			return loc_status;
		},

		registerEventListener : function(listener, handler){
			node_eventUtility.registerListener(listener, loc_out.getBaseObject(), node_CONSTANT.EVENT_EVENTNAME_ALL, handler);
		},

		unregisterEventListener : function(listener){
			node_eventUtility.unregister(listener);
		},
		
		//callback method when this interface is connect to baseObject
		bindBaseObject : function(baseObject){
			//listener to status transit event by itself
			loc_out.registerEventListener(baseObject, function(event, data){
				//logging ths status transit
				//only for module with name

				var name = node_getObjectName(baseObject); 
				if(name!=null){
					nosliw.logging.info(name, "status transit from " + data.oldStatus + " to " + data.newStatus);
				}
			});
		}
	};

	loc_constructor = function(){
	};
	
	loc_constructor();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.getObjectName", function(){node_getObjectName = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});

//Register Node by Name
packageObj.createChildNode("getLifecycleInterface", node_getLifecycleInterface); 
packageObj.createChildNode("makeObjectWithLifecycle", node_makeObjectWithLifecycle); 
packageObj.createChildNode("destroyUtil", node_destroyUtil); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("namingconvension");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_namingConvensionUtility = function(){
	
	return {
		cascadeLevel1 : function(seg1, seg2, normal){
			return this.cascadeParts(seg1, seg2, node_COMMONCONSTANT.SEPERATOR_LEVEL1, normal);
		},

		cascadePath : function(path1, path2, normal){
			return this.cascadeParts(path1, path2, node_COMMONCONSTANT.SEPERATOR_PATH, normal);
		},

		cascadePart : function(part1, part2, normal){
			return this.cascadeParts(part1, part2, node_COMMONCONSTANT.SEPERATOR_PART, normal);
		},
		
		cascadeParts : function(part1, part2, seperator, normal){
			//if normal, just put together
			if(normal==true)  return part1 + seperator + part2;
			
			//otherwise, do smart way
			var out;
			if(node_basicUtility.isStringEmpty(part1)){
				out = part2;
			}
			else{
				if(node_basicUtility.isStringEmpty(part2))  out = part1;
				else	out = part1 + seperator + part2;
			}
			return out;
		},

		parseLevel1 : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL1);
		},

		parseLevel2 : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_LEVEL2);
		},

		/*
		 * get all sub path from full path
		 */
		parsePathInfos : function(fullPath){
			return fullPath.split(node_COMMONCONSTANT.SEPERATOR_PATH);
		},
		
		/*
		 * get all sub path from full path
		 */
		parseDetailInfos : function(details){
			return details.split(node_COMMONCONSTANT.SEPERATOR_DETAIL);
		},
		
	};
}();

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("namingConvensionUtility", node_namingConvensionUtility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_basicUtility;
	var node_parseSegment;
//*******************************************   Start Node Definition  ************************************** 	

var node_objectOperationUtility = 
{
		/*
		 * get attribute value according to the path
		 */
		getObjectAttributeByPath : function(obj, prop) {
			if(prop==undefined || prop=='')  return obj;
		    var parts = prop.split('.');
		    return this.getObjectAttributeByPathSegs(obj, parts);
		},

		getObjectAttributeByPathSegs : function(obj, propSegs) {
			if(obj==undefined)  return;
			if(propSegs==undefined || propSegs.length==0)  return obj;
			
		    var parts = propSegs,
		        last = parts.pop(),
		        l = parts.length,
		        i = 1,
		        current = parts[0];

		    if(current==undefined)  return obj[last];
		    
		    while((obj = obj[current]) && i < l) {
		        current = parts[i];
		        i++;
		    }

		    if(obj) {
		        return obj[last];
		    }
		},

		/*
		 * do operation on object
		 * 		obj : root object
		 * 		prop : path from root object
		 * 		command : what to do
		 * 		data : data for command
		 */
		operateObject : function(obj, prop, command, data){
			return this.operateObjectByPathSegs(obj, prop.split('.'), command, data);
		},

		operateObjectByPathSegs : function(obj, pathSegs, command, data){
			var baseObj = obj;
			var attribute = "";
			
			if(pathSegs==undefined || pathSegs.length==0){
				baseObj = obj;
			}
			else if(pathSegs.length==1){
				baseObj = obj;
				attribute = pathSegs[0];
			}
			else{
				var segs = pathSegs;
				var size = segs.length;
				for(var i=0; i<size-1; i++){
					var attr = segs[i];
					var obj = baseObj[attr];
					if(obj==undefined){
						obj = {};
						baseObj[attr] = obj; 
					}
					baseObj = obj;
				}
				attribute = segs[i];
			}
			
			if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
				baseObj[attribute] = data;
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
				//if container does not exist, then create a map
				if(baseObj[attribute]==undefined)  baseObj[attribute] = {};
				if(data.index!=undefined){
					baseObj[attribute][data.index]=data.data;
				}
				else{
					//if index is not specified, for array, just append it
					if(_.isArray(baseObj[attribute])){
						baseObj[attribute].push(data.data);
					}
				}
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
				delete baseObj[attribute][data];
			}			
		},


};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});

//Register Node by Name
packageObj.createChildNode("objectOperationUtility", node_objectOperationUtility); 
	
})(packageObj);
//get/create package
var packageObj = library.getChildPackage("objectwithid");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "id";
	
	/*
	 * build an object have id info
	 */
	var node_makeObjectWithId = function(obj, id){
		return node_buildInterface(obj, INTERFACENAME, id);
	};

	/*
	 * get object's id info
	 */
	var node_getObjectId = function(object){
		return node_getInterface(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithId", node_makeObjectWithId); 
packageObj.createChildNode("getObjectId", node_getObjectId); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("objectwithname");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "name";
	
	/*
	 * build an object to named object
	 */
	var node_makeObjectWithName = function(obj, name){
		return node_buildInterface(obj, INTERFACENAME, name);
	};

	/*
	 * get object's name
	 */
	var node_getObjectName = function(object){
		return node_getInterface(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
	
//Register Node by Name
packageObj.createChildNode("makeObjectWithName", node_makeObjectWithName); 
packageObj.createChildNode("getObjectName", node_getObjectName); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_buildInterface;
	var node_getInterface;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "TYPE";
	
/*
 * build an object to typed object
 */
var node_makeObjectWithType = function(obj, type){
	return node_buildInterface(obj, INTERFACENAME, type);
};

/*
 * get object's type info
 * if no type info, the use VALUE as type  
 */
var node_getObjectType = function(object){
	var type = node_getInterface(object, INTERFACENAME);
	if(type!=undefined)  return type;
	else return node_CONSTANT.TYPEDOBJECT_TYPE_VALUE;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithType", node_makeObjectWithType); 
packageObj.createChildNode("getObjectType", node_getObjectType); 

})(packageObj);

//get/create package
var packageObj = library.getChildPackage("orderedcontainer");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

	
/**
 * 
 */
var node_newOrderedContainerGeneral = function(dataArray, keyName){
	this.keyArray = [];
	this.dataMap = {};
	this.keyName = keyName;
	
	if(dataArray!=undefined){
		for(var index in dataArray){
			this.addData(dataArray[index]);
		}
	}
};
	
	
/**
 * 
 */
var newOrderedContainer = function(dataWraperArray, childDataType, keyName){
	this.keyArray = [];
	this.dataMap = {};
	this.childDataType = childDataType;
	this.keyName = keyName;
	
	var dataArray = dataWraperArray;
	
	if(this.keyName==undefined){
		if(this.childDataType.categary=="entity" || this.childDataType.categary=="query"){
			this.keyName = "ID";
		}
	}

	for(var index in dataArray){
		this.addData(dataArray[index]);
	}
};

newOrderedContainer.prototype = prototype;

var prototype = {
		addData : function(data){
			if(this.keyName==undefined){
				var index=0;
				if(this.dataArray.length>0){
					index = this.dataArray[this.dataArray.length-1] + 1;
				}
				this.keyArray.push(index);
				this.dataMap[index] = data;
			}
			else{
					var keyValue = getObjectAttributeByPath(data, this.keyName);
					this.keyArray.push(keyValue);
					this.dataMap[keyValue] = data;
			}
		},
		
		removeData : function(key){
			for(var i in this.keyArray){
				if(this.keyArray[i]==key){
					this.keyArray.splice(i, 1);
					break;
				}
			}
			delete this.dataMap[key];
		},
		
		updateData : function(data, key){
			if(key!=undefined){
			}
			else{
				if(this.keyName!=undefined){
					key = getObjectAttributeByPath(data, this.keyName);
				}
			}
			
			if(key!=undefined){
				this.dataMap[key].data = data.data;
				return this.dataMap[key];
			}
		},
		
		getKeyArray : function(){
			return this.keyArray;
		},
		
		getDataByKey : function(key){
			return this.dataMap[key];
		},
		
		getDataList : function(){
			var out = [];
			for(var key in this.getKeyArray()){
				out.push(this.getDataByKey(key));
			}
		},
};


var node_handleDataContainerEachElement = function(dataContainerWraper, handler){
	if(dataContainerWraper==undefined)   return;
	var dataContainer = dataContainerWraper.container;
	var childDataType = dataContainerWraper.childDataType;
	var keyArray = dataContainer.getKeyArray();
	for(var key in keyArray){
		var eleWraper = dataContainer.getDataByKey(key);
		var contextEle = createContextElement(eleWraper);
    	handler(key, eleWraper, contextEle);
	}
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data


//Register Node by Name
packageObj.createChildNode("OrderedContainer.newEntity", node_newOrderedContainerGeneral); 
packageObj.createChildNode("utils.handleDataContainerEachElement", node_handleDataContainerEachElement); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("orderedcontainer");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_newOrderedContainerGeneral = function(dataArray, keyName){
	this.keyArray = [];
	this.dataMap = {};
	this.keyName = keyName;
	
	if(dataArray!=undefined){
		for(var index in dataArray){
			this.addData(dataArray[index]);
		}
	}
};
	
	
/**
 * 
 */
var newOrderedContainer = function(dataWraperArray, childDataType, keyName){
	this.keyArray = [];
	this.dataMap = {};
	this.childDataType = childDataType;
	this.keyName = keyName;
	
	var dataArray = dataWraperArray;
	
	if(this.keyName==undefined){
		if(this.childDataType.categary=="entity" || this.childDataType.categary=="query"){
			this.keyName = "ID";
		}
	}

	for(var index in dataArray){
		this.addData(dataArray[index]);
	}
};

newOrderedContainer.prototype = prototype;

var prototype = {
		addData : function(data){
			if(this.keyName==undefined){
				var index=0;
				if(this.dataArray.length>0){
					index = this.dataArray[this.dataArray.length-1] + 1;
				}
				this.keyArray.push(index);
				this.dataMap[index] = data;
			}
			else{
					var keyValue = getObjectAttributeByPath(data, this.keyName);
					this.keyArray.push(keyValue);
					this.dataMap[keyValue] = data;
			}
		},
		
		removeData : function(key){
			for(var i in this.keyArray){
				if(this.keyArray[i]==key){
					this.keyArray.splice(i, 1);
					break;
				}
			}
			delete this.dataMap[key];
		},
		
		updateData : function(data, key){
			if(key!=undefined){
			}
			else{
				if(this.keyName!=undefined){
					key = getObjectAttributeByPath(data, this.keyName);
				}
			}
			
			if(key!=undefined){
				this.dataMap[key].data = data.data;
				return this.dataMap[key];
			}
		},
		
		getKeyArray : function(){
			return this.keyArray;
		},
		
		getDataByKey : function(key){
			return this.dataMap[key];
		},
		
		getDataList : function(){
			var out = [];
			for(var key in this.getKeyArray()){
				out.push(this.getDataByKey(key));
			}
		},
};


var node_handleDataContainerEachElement = function(dataContainerWraper, handler){
	if(dataContainerWraper==undefined)   return;
	var dataContainer = dataContainerWraper.container;
	var childDataType = dataContainerWraper.childDataType;
	var keyArray = dataContainer.getKeyArray();
	for(var key in keyArray){
		var eleWraper = dataContainer.getDataByKey(key);
		var contextEle = createContextElement(eleWraper);
    	handler(key, eleWraper, contextEle);
	}
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data


//Register Node by Name
packageObj.createChildNode("OrderedContainer.newEntity1", node_newOrderedContainerGeneral); 
packageObj.createChildNode("utils.handleDataContainerEachElement1", node_handleDataContainerEachElement); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("patternmatcher");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_Pattern = function(pattern, onMatch){
	this.pattern = pattern;
	this.onMatch = onMatch;
};
	
var node_createPatternMatcher = function(patterns){
	
	var loc_patterns = patterns;
	if(loc_patterns==undefined)   loc_patterns = [];
	
	var loc_results = {};
	
	var loc_out = {
		
		addPattern : function(pattern){
			loc_patterns.push(pattern);
		},	
			
		match : function(content){
			var resultInfo = loc_results[content];
			if(resultInfo!=undefined){
				return loc_patterns[resultInfo.index].onMatch(resultInfo.result);
			}
			
			for(var i in loc_patterns){
				var result = loc_patterns[i].pattern.exec(content);
				if(result!=undefined){
					loc_results[content] = {
						index : i,
						result : result
					};
					return loc_patterns[i].onMatch(result);
				}
			}
		}
			
	};
	
	return loc_out;
};	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createPatternMatcher", node_createPatternMatcher); 
packageObj.createChildNode("Pattern", node_Pattern); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("segmentparser");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_COMMONCONSTANT;

//*******************************************   Start Node Definition  ************************************** 	

var parsePathSegment = function(path, first, lastReverse){
	return parseSegment(path, undefined, first, lastReverse);
};
	
/*
 * parse the path information
 */
var parseSegment = function(path, sep, first, lastReverse){
	
	var loc_segments = [];
	var loc_seperator = undefined;
	var loc_isEmpty = false;
	var loc_index = 0;

	//start and end index to iterate
	var loc_firstIndex = first;
	var loc_lastIndex ;
	
	var seperator = sep;
	if(node_basicUtility.isStringEmpty(seperator))   seperator = node_COMMONCONSTANT.SEPERATOR_PATH; 
	if(node_basicUtility.isStringEmpty(path))  loc_isEmpty = true;
	else{
		loc_seperator = seperator;
		loc_segments = path.split(seperator);
		
		if(loc_firstIndex==undefined)   loc_firstIndex = 0;
		
		if(lastReverse==undefined)   loc_lastIndex = loc_segments.length-1;
		else  loc_lastIndex = loc_segments.length-1-lastReverse;
		loc_index = loc_firstIndex;
	}
		
	var loc_out = {
		isEmpty : function(){return loc_isEmpty;},
		
		next : function(){
			if(this.isEmpty())   return undefined;
			var out = loc_segments[loc_index];
			loc_index++;
			return out;
		},
	
		hasNext : function(){
			if(this.isEmpty())  return false;
			if(loc_index>loc_lastIndex)  return false;
			return true;
		},
		
		getSegmentSize : function(){
			if(this.isEmpty())  return 0;
			return loc_segments.length;
		},
		
		getSegments : function(){
			if(this.isEmpty())  return [];
			return loc_segments;
		},
		
		getRestPath : function(){
			if(this.isEmpty())  return undefined;
			var out = "";
			for(var i=loc_index; i<this.getSegmentSize(); i++){
				if(i!=loc_index)  out = out + loc_seperator;
				out = out + loc_segments[i];
			}
			return out;
		},
		
		getPreviousPath : function(){
			if(this.isEmpty())  return undefined;
			var out = "";
			for(var i=0; i<loc_index; i++){
				if(i!=0)  out = out+ loc_seperator;
				out = out + loc_segments[i];
			}
			return out;
		}
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("parseSegment", parseSegment); 
packageObj.createChildNode("parsePathSegment", parsePathSegment); 
	
})(packageObj);
//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * service information object 
 * this structor can be used in different senario: romote task, service request 
 * 		command: service name
 * 		data:    input data
 */
var node_ServiceInfo = function(command, parms){
	this.command = command;
	this.parms = parms;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("ServiceInfo", node_ServiceInfo); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("setting");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * store setting for sync task, (service, command)
 */
var node_createConfiguresBase = function(baseConfigures){
	
	var loc_baseConfigures = node_createConfigures(baseConfigures);
	
	var loc_out = {
			createConfigures : function(configures){
				return loc_baseConfigures.mergeWith(node_createConfigures(configures));
			},
			
			getBaseConfigures : function(){
				return loc_baseConfigures;
			},
	};
	
	return loc_out;
};

/*
 * configure entity
 * input: 
 * 		string : name1:value1;name2:value2;name3:value3
 * 		object : configure object
 */
var node_createConfigures = function(configures){
	var loc_configures = {};
	if(_.isString(configures)){
		//literal
		var pairs = configures.split(node_NOSLIWCOMMONCONSTANT.SEPERATOR_ELEMENT);
		for(var i in pairs){
			var pair = pairs[i];
			var segs = pair.split(NOSLIWCOMMONCONSTANT.SEPERATOR_PART); 
			loc_configures[seg[0]] = sets[1];
		}
	}
	else if(_.isObject(configures)){
		if(node_getObjectType(configures)===node_CONSTANT.TYPEDOBJECT_TYPE_CONFIGURES){
			return configures;
		}
		else{
			loc_configures = configures;
		}
	}
	
	var loc_out = {
		getConfiguresObject : function(){
			return loc_configures;
		},
			
		getConfigure : function(name){
			return loc_configures[name];
		},
		
		setConfigure : function(name, value){
			loc_configures[name] = value;
		},
		
		mergeWith : function(configures){
			var configuresObj = node_basicUtility.mergeObjects(loc_configures, configures.getConfiguresObject());
			return node_createConfigures(configuresObj);
		},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONFIGURES);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfiguresBase", node_createConfiguresBase);  
packageObj.createChildNode("createConfigures", node_createConfigures); 
	
})(packageObj);
//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_basicUtility = 
{
		/*
		 * create an value with meaning of empty
		 */
		createEmptyValue : function(){return "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		isEmptyValue : function(value){ return value== "alkfjalsdkfjsafjoweiurwerwelkjdlsjdf";},
		
		emptyStringIfUndefined : function(value){ 
			if(value==undefined)  return "";
			return value;
		},
		
		/*
		 * merge two object and create a new one
		 * specificOne will override the defaultone object
		 */
		mergeObjects : function(defaultOne, specificOne){
			
			var out = {};
			_.each(defaultOne, function(attr, name, list){
				out[name] = attr;
			});

			_.each(specificOne, function(attr, name, list){
				out[name] = attr;
			});
			return out;
		},
		
		cloneObject : function(object){
			var newObject = jQuery.extend(true, {}, object);	
			return newObject;
		},
		
		isStringEmpty : function(str){
			if(str==undefined || str+''=='')  return true;
			else false;
		},

		htmlDecode : function(input){
			var e = document.createElement('div');
			e.innerHTML = input;
			return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
		},

		isAtomData : function(data){
			if(_.isString(data) || _.isNumber(data) || _.isBoolean(data))  return true;
		},
		
		capitalizeFirstLetter : function(string) {
		    return string.charAt(0).toUpperCase() + string.slice(1);
		},
		
		isEmptyObject :function (obj) {
			if(obj==undefined)  return true;
			for(var prop in obj) {
			    if (Object.prototype.hasOwnProperty.call(obj, prop)) {
			    	return false;
				}
			}
			return true;
		},
		
		stringify : function(value){
			if(value==undefined)   return "undefined";
			try{
				return JSON.stringify(value);
			}
			catch(e){
				return value.toString();
			}
		}
		
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data


//Register Node by Name
packageObj.createChildNode("basicUtility", node_basicUtility); 
	
})(packageObj);
var library = nosliw.getPackage("expression");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_Data;

//*******************************************   Start Node Definition  ************************************** 	

var node_dataUtility = 
{
	createStringData : function(stringValue){
		return new node_Data("test.string;1.0.0", stringValue);
	}
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("expression.entity.Data", function(){node_Data = this.getData();});

//Register Node by Name
packageObj.createChildNode("dataUtility", node_dataUtility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONCONSTANT;
	var node_resourceUtility;
	var node_expressionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_Data = function(dataTypeId, value){
	this.dataTypeId = dataTypeId;
	this.value = value;
};	
	
var node_OperationParm = function(data, name, isBase){
	this.data = data;
	this.name = name;
	this.isBase = isBase==undefined?false:isBase;
};	
	
var node_OperationParms = function(parmsArray){
	this.pri_parmsArray = parmsArray;
	this.pri_parmsMap = {};
	_.each(parmsArray, function(parm, index, list){
		var parmName = parm.name;
		if(parmName==undefined)		parmName = node_COMMONCONSTANT.DATAOPERATION_PARM_BASENAME;
		this.pri_parmsMap[parmName] = parm.data;
		if(parm.isBase===true)   this.baseParm = parmName;
	}, this);
};

node_OperationParms.prototype = {
	getParm : function(name){
		if(name===undefined)  return this.getBase();
		else return this.pri_parmsMap[name];
	},
	
	getBase : function(){
		return this.pri_parmsMap[this.baseParm];
	} 
};

var node_OperationContext = function(resourcesTree, aliases){
	this.pri_resourcesTree = resourcesTree;
	this.pri_aliases = aliases;
	this.logging = nosliw.logging;
}

node_OperationContext.prototype = {
	getResourceById : function(resourceId){
		return node_resourceUtility.getResourceFromTree(this.pri_resourcesTree, resourceId);
	},
	
	getResourceByName : function(alias){
		var resourceId = this.pri_aliases[alias];
		return this.getResourceById(resourceId);
	},

	getResourceDataByName : function(alias){
		var resourceId = this.pri_aliases[alias];
		return this.getResourceById(resourceId).resourceData;
	},
	
	operation : function(dataTypeId, operation, parmArray){
		var dataOperationResourceId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
		return node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, this.pri_resourcesTree);
	},
	
	executeExpression : function(expression, parms){
		
	}
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("Data", node_Data); 
packageObj.createChildNode("OperationParm", node_OperationParm); 
packageObj.createChildNode("OperationParms", node_OperationParms); 
packageObj.createChildNode("OperationContext", node_OperationContext); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_resourceUtility;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_basicUtility;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
	var node_DependentServiceRequestInfo;
	var node_expressionUtility;
	var node_dataUtility;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createExpressionService = function(){
	/**
	 * Request for execute expression
	 */
	var loc_getExecuteExpressionRequest = function(expression, variables, constants, references, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpression", {"expression":expression, "variables":variables}), handlers, requestInfo);
		var variablesInfo = expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_VARIABLEINFOS];
			
		//if have variables, convert variables
		var varsMatchers = expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_VARIABLESMATCHERS];
		if(varsMatchers==undefined)  varsMatchers = {};
		var varsMatchRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatcherVariable", {"variables":variables, "variablesMatchers":varsMatchers}), 
				{			
					success : function(reqInfo, setResult){
						var variables = reqInfo.getData();
						var matchedVars = {};
						_.each(variables, function(varData, varName, list){
							var matchedVar = setResult.getResult(varName);
							if(matchedVar==undefined){
								matchedVar = variables[varName];
							}
							matchedVars[varName] = matchedVar;
						}, this);
						reqInfo.setData(matchedVars);
						
						//execute operand
						var executeOperandRequest = loc_getExecuteOperandRequest(expression, expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_OPERAND], reqInfo.getData(), constants, references, {
							success : function(requestInfo, operandResult){
								return operandResult;
							}
						}, null);
						return executeOperandRequest;
					}, 
				}, 
				null).withData(variables);
			
			_.each(variables, function(varData, varName, list){
				var varMatchers = varsMatchers[varName];
				if(varMatchers!=undefined){
					var request = loc_getMatchDataTaskRequest(varData, varMatchers, {}, null);
					varsMatchRequest.addRequest(varName, request);
				}
			}, this);

			out.addRequest(varsMatchRequest);
		
		return out;
	};

	//execute general operand
	var loc_getExecuteOperandRequest = function(expression, operand, variables, constants, references, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		var out;
		var operandType = operand[node_COMMONATRIBUTECONSTANT.OPERAND_TYPE];
		switch(operandType){
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_CONSTANT:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteConstantOperand", {"operand":operand, "constants":constants}), 
					function(requestInfo){
						var constantName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_NAME];
						var constantData = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_DATA];
						if(constantData==undefined)  constantData = requestInfo.getService().parms.constants[constantName];
						return constantData;
					}, 
					handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_VARIABLE: 
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteVariableOperand", {"operand":operand, "variables":variables}), 
					function(requestInfo){  
						var varName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_VARIABLENAME];
						return requestInfo.getService().parms.variables[varName];  
					}, 
					handlers, requestInfo);
		    break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_OPERATION:
			out = loc_getExecuteOperationOperandRequest(expression, operand, variables, constants, references, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_REFERENCE:
			out = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteReferenceOperand", {"operand":operand, "references":references}), 
					function(requestInfo){
						var refName = requestInfo.getService().parms.operand[node_COMMONATRIBUTECONSTANT.OPERAND_REFERENCENAME];
						return requestInfo.getService().parms.references[refName];
					}, 
					handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_DATASOURCE:
			out = loc_getExecuteDataSourceOperandRequest(expression, operand, variables, handlers, requestInfo);
			break;
		case node_COMMONCONSTANT.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
			break;
		}
		return out;
	};

	
	//execute operation operand
	var loc_getExecuteDataSourceOperandRequest = function(expression, dataSourceOperand, variables, handlers, requester_parent){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataSourceOperand", {"dataSourceOperand":dataSourceOperand}), handlers, requester_parent);

		var getParmsValueRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("GetDataSourceParmsValue", {}), {
			success : function(requestInfo, setResult){
				var gatewayParms = {};
				gatewayParms[node_COMMONATRIBUTECONSTANT.GATEWAYDATASOURCE_COMMAND_GETDATA_NAME] = dataSourceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATASOURCE_NAME];
				gatewayParms[node_COMMONATRIBUTECONSTANT.GATEWAYDATASOURCE_COMMAND_GETDATA_PARMS] = setResult.getResults(); 
				return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(
						node_COMMONATRIBUTECONSTANT.DATASOURCEMANAGER_GATEWAY_DATASOURCE, 
						node_COMMONATRIBUTECONSTANT.GATEWAYDATASOURCE_COMMAND_GETDATA, 
						gatewayParms);
			}
		});
		var dataSourceDefinition = dataSourceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATASOURCE_DEFINITION];
		_.each(dataSourceDefinition[node_COMMONATRIBUTECONSTANT.DATASOURCEDEFINITION_PARMS], function(parmDef, parmName){
			//get parm value from constant first
			var getParmValueRequest;
			var parmValue = dataSourceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATASOURCE_CONSTANT][parmName];
			if(parmValue!=undefined){
				//get from constant
				getParmValueRequest = node_createServiceRequestInfoSimple({}, function(requestInfo){	return parmValue;});
			}
			else{
				//try from variable
				var mappedVarName = dataSourceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATASOURCE_VARCONFIGURE][parmName];
				if(mappedVarName==undefine)  mappedVarName = parmName;    //no mapping, use original name to find varible

				//get from variable
				var varValue = variables[mappedVarName];
				if(varValue!=undefined){
					var varMatchers = dataSourceOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATASOURCE_PARMMATCHERS][parmName];
					//convert according to matchers
					getParmValueRequest = loc_getMatchDataTaskRequest(varValue, varMatchers);
				}
			}
			
			if(getParmValueRequest!=undefined)		getParmsValueRequest.addRequest(parmName, getParmValueRequest);
		});
		
		out.addRequest(getParmsValueRequest);
		return out;
	};
	
	//execute operation operand
	var loc_getExecuteOperationOperandRequest = function(expression, operationOperand, variables, constants, references, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperationOperand", {"operationOperand":operationOperand, "variables":variables}), handlers, requestInfo);

		//cal all parms
		var parmsOperand = operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_PARMS];
		var parmsOperandRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("CalOperationParms", {"parms":parmsOperand}), {
			success : function(requestInfo, setResult){
				var parmsData = setResult.getResults();
				
				//match parms and base
				var parmsMatcherRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchOperationParms", {"parmsData":parmsData, "matchers":operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS]}), {
					success : function(requestInfo, parmMatchResult){
						var parmsData = requestInfo.getData();
						var parmMatchedData = parmMatchResult.getResults();
						_.each(parmMatchedData, function(parmValue, parmName, list){
							parmsData[parmName] = parmValue;
						}, this);
						out.setData(parmsData);

						//build parms for operation
						var operationParms = [];
						_.each(parmsData, function(parmData, parmName, list){
							operationParms.push(new node_OperationParm(parmData, parmName));
						}, this);

						//execute data operation
						var dataTypeId = operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_DATATYPEID];
						var executeOperationRequest = loc_getExecuteOperationRequest(dataTypeId, operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_OPERATION], operationParms, {
							success : function(requestInfo, data){
								return data;
							}
						});
						return executeOperationRequest;
					}
				}).withData(parmsData);
				_.each(operationOperand[node_COMMONATRIBUTECONSTANT.OPERAND_MATCHERSPARMS], function(parmMatchers, parmName, list){
					var parmMatchRequest = loc_getMatchDataTaskRequest(parmsData[parmName], parmMatchers, {});
					parmsMatcherRequest.addRequest(parmName, parmMatchRequest);
				}, this);
				return parmsMatcherRequest;
			}
		});
		_.each(parmsOperand, function(parmOperand, parmName, list){
			var parmOperandRequest = loc_getExecuteOperandRequest(expression, parmOperand, variables, constants, references, {
				success :function(request, parmValue){
					var kkkk = 5555;
					if(parmName=='base' && parmValue.operation!=undefined){
						var kkkkk = 5555;
						kkkkk++;
					}
					return parmValue;
				}
			});
			parmsOperandRequest.addRequest(parmName, parmOperandRequest);
		}, this);
		out.addRequest(parmsOperandRequest);
		
		return out;
	}

	
	//convert individual data according to matchers
	var loc_getMatchDataTaskRequest = function(data, matchers, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var service = new node_ServiceInfo("MatchData", {"data":data, "matcher":matchers});
		
		var dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
		var matcher;
		if(matchers!=undefined){
			matcher = matchers[dataTypeId];
		}
		
		var out;
		if(matcher==undefined){
			//if converter does not created, then get it
			nosliw.error("no matches for data type: " + dataTypeId);
		}
		else{
			var relationship = matcher[node_COMMONATRIBUTECONSTANT.MATCHER_RELATIONSHIP];
			var subMatchers = matcher[node_COMMONATRIBUTECONSTANT.MATCHER_SUBMATCHERS];
			var sourceDataTypeId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_SOURCE];
			var targetDataTypeId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_TARGET];
			
			if(sourceDataTypeId==targetDataTypeId){
				if(node_basicUtility.isEmptyObject(subMatchers)==true){
					//no need to convert
					out = node_createServiceRequestInfoSimple(service, function(requestInfo){
						return requestInfo.getService().parms.data;
					}, handlers, requestInfo);
				}
				else{
					out = node_createServiceRequestInfoService(service, handlers, requestInfo);
					var resourceRequestDependency = new node_DependentServiceRequestInfo(loc_getSubMatchDataTaskRequest(data, subMatchers), {
						success : function(requestInfo, convertedSubData){
							return convertedSubData;
						}
					});
					out.setDependentService(resourceRequestDependency);
				}
			}
			else{
				out = node_createServiceRequestInfoSequence(service, handlers, requestInfo);
				var matcherSegments = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_PATH];

				var targets = [];
				var sourceId = relationship[node_COMMONATRIBUTECONSTANT.DATATYPERELATIONSHIP_SOURCE];
				var targetId;
				for(var i=0; i<matcherSegments.length; i++){
					targetId = node_namingConvensionUtility.parseLevel2(matcherSegments[i])[1];
					targets.push(targetId);
					sourceId = targetId;
				}
				
				var converterData = data;
				for(var i=0; i<targets.length; i++){
					var converterRequest = loc_getExecuteConverterToRequest(converterData, targets[i], matcher.reverse, {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}, out);
					out.addRequest(converterRequest);
				}

				//convert sub data
				if(!node_basicUtility.isEmptyObject(subMatchers)==true){
					out.addRequest(loc_getSubMatchDataTaskRequest(converterData, subMatchers, {
						success : function(requestInfo, convertedData){
							converterData = convertedData;
						}
					}));
				}
				
				out.setRequestProcessors({
					success : function(reqInfo, result){
						return converterData;
					}, 
				});
			}
			return out;
		}
	};

	//convert data according to sub matchers
	var loc_getSubMatchDataTaskRequest = function(data, submatchers, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);

		//get all subNames involved in match
		var subNames = [];
		_.each(submatchers, function(submatcher, name){subNames.push(name)});

		var subDatas = {};
		
		//get all sub data
		var getSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("GetSubDatas", {"data":data, "subNames":subNames}), {
			success : function(requestInfo, subDatasResult){
				subDatas = subDatasResult.getResults();
			}
		});
		
		_.each(subNames, function(name){
			//get each sub data request
			getSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "getSubData", [{"name":node_dataUtility.createStringData("name")}], {}));
		});

		//convert all sub data
		var convertSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("MatchSubDatas", {"data":subDatas, "subNames":subNames}), {
			success : function(requestInfo, subDatasResult){
				subDatas = subDatasResult.getResults();
			}
		});
		
		//convert each sub data
		_.each(subNames, function(name){
			convertSubDatasRequest.addRequest(name, loc_getMatchDataTaskRequest(subDatas[name], submatchers[name], {}));
		});
		
		//set all sub data
		var setSubDatasRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("SetSubDatas", {"subDatas":subDatas, "subNames":subNames}), {
			success : function(requestInfo, subValuesResult){
			}
		});
			
		_.each(subNames, function(name){
			setSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "setSubData", [{"name":node_dataUtility.createStringData("name")}, {"data":subDatas[name]}], {}));
		});
			
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SubMatchers", {"data":data, "submatchers":submatchers}), handlers, requestInfo);
		out.addRequest(getSubDatasRequest);
		out.addRequest(convertSubDatasRequest);
		out.addRequest(setSubDatasRequest);
		return out;
	};
	
	//execute conterter
	var loc_getExecuteConverterToRequest = function(data, targetDataTypeId, reverse, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteConverter", {"data":data, "targetDataTypeId":targetDataTypeId}), handlers, requestInfo);

		var dataTypeId;
		if(reverse){
			dataTypeId = targetDataTypeId;
		}
		else{
			dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
		}
		
		var converterResourceId = node_resourceUtility.createConverterResourceId(dataTypeId);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([converterResourceId], {
			success : function(requestInfo, resourcesTree){
				var dataTypeId;
				if(reverse){
					dataTypeId = data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
				}
				else{
					dataTypeId = targetDataTypeId;
				}
				return node_expressionUtility.executeConvertResource(converterResourceId, data, dataTypeId, reverse, resourcesTree);
			}
		});
		out.addRequest(getResourceRequest);
		return out;
	};
	
	//execute data operation
	var loc_getExecuteOperationRequest = function(dataTypeId, operation, parmArray, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteOperation", {"dataType":dataTypeId, "operation":operation, "parms":parmArray}), handlers, requestInfo);

		var dataOperationResourceId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
		var getResourceRequest = nosliw.runtime.getResourceService().getGetResourcesRequest([dataOperationResourceId], {
			success : function(requestInfo, resourcesTree){
				var opResult = node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, resourcesTree);
				return opResult;
			}
		});
		out.addRequest(getResourceRequest);
		return out;
	};	

	var loc_getExecuteScriptRequest = function(script, expressions, variables, scriptConstants, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExpressionService_ExecuteScript", {"script":script, "expressions":expressions, "variables":variables}), handlers, requestInfo);

		//calculate multiple expression
		var executeMultipleExpressionRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("ExecuteMultipleExpression", {"expressions":expressions, "variables":variables}), {
			success : function(requestInfo, expressionsResult){
				var expressionsData = expressionsResult.getResults();
				return script.call(undefined, expressionsData, scriptConstants, variables);
			}
		});
		_.each(expressions, function(expression, name){
			//find variable value only for this expression
			var expVariables = {};
			_.each(expression[node_COMMONATRIBUTECONSTANT.EXPRESSION_VARIABLEINFOS], function(varInfo, name){
				expVariables[name] = variables[name];
			});
			executeMultipleExpressionRequest.addRequest(name, loc_getExecuteExpressionRequest(expression, expVariables, {}, {}));
		});
		
		out.addRequest(executeMultipleExpressionRequest);
		return out;
	};

	var loc_out = {
		
		getExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			return loc_getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
		},
			
		executeExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			var requestInfo = this.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getExecuteExpressionRequest : function(expression, variables, constants, references, handlers, requester_parent){
			return loc_getExecuteExpressionRequest(expression, variables, constants, references, handlers, requester_parent);
		},
			
		executeExecuteExpressionRequest : function(expression, variables, constants, references, handlers, requester_parent){
			var requestInfo = this.getExecuteExpressionRequest(expression, variables, constants, references, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getMatchDataRequest : function(data, matchers, handlers, requester_parent){
			return loc_getMatchDataTaskRequest(data, matchers, handlers, requester_parent);
		},

		executeMatchDataRequest : function(data, matchers, handlers, requester_parent){
			var requestInfo = this.getMatchDataRequest(data, matchers, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		/**
		 * Execute script expression
		 * 		script : function with parameter map (name : expression result)
		 * 		expressions : map (name : expression)
		 * 		variables : variables for expression
		 * 		scriptConstants : constants in script
		 */
		getExecuteScriptRequest : function(script, expressions, variables, scriptConstants, handlers, requester_parent){
			return loc_getExecuteScriptRequest(script, expressions, variables, scriptConstants, handlers, requester_parent);
		},
	
		executeExecuteScriptExpressionRequest : function(script, expressions, variables, scriptConstants, handlers, requester_parent){
			var requestInfo = this.getExecuteScriptRequest(script, expressions, variables, scriptConstants, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};
	
	loc_out = node_buildServiceProvider(loc_out, "expressionService");
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.dataUtility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createExpressionService", node_createExpressionService); 
	
})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
	var node_resourceUtility;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = 
{
		executeOperationResource : function(resourceId, parmArray, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_RESOURCEDATA];
			var dataOperationInfo = dataOperationResource[node_COMMONATRIBUTECONSTANT.RESOURCE_INFO][node_COMMONATRIBUTECONSTANT.RESOURCEMANAGERJSOPERATION_INFO_OPERATIONINFO];
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//base data is "this" in operation function
			var baseData;
			var operationParmArray = [];
			var parmsDefinitions = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_PAMRS];
			_.each(parmArray, function(parm, index, list){
				var parmName = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME];
				if(parmName==undefined){
					//if no parm name, then use base name
					parmName = dataOperationInfo[node_COMMONATRIBUTECONSTANT.DATAOPERATIONINFO_BASEPARM];
					parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_NAME] = parmName;
				}
				
				var parmDefinition = parmsDefinitions[parmName];
				var isBase = false;
				if(parmDefinition[node_COMMONATRIBUTECONSTANT.DATAOPERATIONPARMINFO_ISBASE]=="true"){
					isBase = true;
					baseData = parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA];
				}
				operationParmArray.push(new node_OperationParm(parm[node_COMMONATRIBUTECONSTANT.OPERATIONPARM_DATA], parmName, isBase));
			}, this);
			
			nosliw.logging.info("************************  operation   ************************");
			nosliw.logging.info(resourceId);
			_.each(parmArray, function(parm, index){
				nosliw.logging.info("Parm " + parm.name+":", parm.data);
			});
			
			var operationResult = dataOperationFun.call(baseData, new node_OperationParms(operationParmArray), operationContext);

			nosliw.logging.info("Out : ", operationResult);
			nosliw.logging.info("***************************************************************");
			
			return operationResult;
		},

		executeConvertResource : function(resourceId, data, dataTypeId, reverse, resourcesTree){
			var dataOperationResource = node_resourceUtility.getResourceFromTree(resourcesTree, resourceId);
			var dataOperationFun = dataOperationResource.resourceData;
			
			//build operation context
			var operationContext = new node_OperationContext(resourcesTree, dataOperationResource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY]);
			
			//data is "this" in operation function
			var result = dataOperationFun.call(data, data, dataTypeId, reverse, operationContext);
			return result;
		},
		
		getExecuteGetSubDataRequest : function(data, name, handler, requester_parent){
			getSubDatasRequest.addRequest(name, loc_out.getExecuteOperationRequest(data[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID], "getSubData", parmsArray, {}));
		}

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("process");
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
//*******************************************   Start Node Definition  ************************************** 	

//process output
var node_ProcessResult = function(resultName, value){
	this.resultName = resultName;
	this.value = value;
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

//Register Node by Name
packageObj.createChildNode("ProcessResult", node_ProcessResult); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_ioTaskProcessor;
	var node_IOTaskResult;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	
//*******************************************   Start Node Definition  **************************************
//normal activity output (next activity + context)
var loc_NormalActivityOutput = function(next, context){
	this.next = next;
	this.context = context;
};

//end activity output (result name + context)
var loc_EndActivityOutput = function(resultName, context){
	this.resultName = resultName;
	this.context = context;
};

	
var node_createProcess = function(processDef, envObj){
	var loc_processDef = processDef;
	var loc_envObj = envObj;
	var loc_processContextIO;
	var loc_activityPlugins = [];
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(processDef, envObj){
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
	};	
	
	var loc_createContextIORequest = function(input, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var valueType = node_getObjectType(input);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_EXTERNALMAPPING){
			out.addRequest(node_createDataAssociation(input.dataIO, input.dataAssociationDef).getExecuteRequest({
				success : function(request, mappingedIO){
					return mappingedIO.getGetDataSetValueRequest({
						success : function(request, mappingedDataSet){
							loc_processContextIO = node_createIODataSet(loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](mappingedDataSet));
						}
					});
				}
			}));
		}
		else{
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){	
				loc_processContextIO = node_createIODataSet(loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_INITSCRIPT](input));
			}));
		}
		return out;
	};
	
	var loc_getActivityPluginRequest = function(pluginName, handlers, request){
		var service = new node_ServiceInfo("getActivityPlugin", {"pluginName":pluginName})
		var plugin = loc_activityPlugins[pluginName];
		if(plugin!=null){
			return node_createServiceRequestInfoSimple(service, function(requestInfo){	return plugin;}, handlers, request);
		}
		else{
			var out = node_createServiceRequestInfoSequence(service, handlers, request);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([pluginName], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, {
				success : function(request, resourceData){
					var plugin = resourceData[pluginName][node_COMMONATRIBUTECONSTANT.PLUGINACTIVITY_SCRIPT](nosliw, loc_envObj);
					loc_activityPlugins[pluginName] = plugin;
					return plugin;
				}
			}));
			return out;
		}
	};
	
	var loc_getExecuteNormalActivityRequest = function(normalActivity, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteNormalActivity", {"activity":normalActivity}), handlers, request);
		
		out.addRequest(node_ioTaskProcessor.getExecuteIORequest(
				loc_processContextIO, 
				undefined,
				normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_INPUT], 
				function(input, handlers, request){
					var executeActivityPluginRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivityPlugin", {}), handlers, request);
					//get activity plugin 
					executeActivityPluginRequest.addRequest(loc_getActivityPluginRequest(normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE], {
						success : function(requestInfo, activityPlugin){
							//execute activity plugin
							return activityPlugin.getExecuteActivityRequest(normalActivity, input, loc_envObj, {
								success : function(requestInfo, activityResult){  //get activity results (result name + result value map)
									return activityResult;
								}
							});
						}
					}));
					return executeActivityPluginRequest;
				}, 
				function(resultName){
					return normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][resultName][node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_DATAASSOCIATION]; 
				},
				loc_processContextIO,
				{
					success : function(request, taskResult){
						var activityResultConfig = normalActivity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULT][taskResult.resultName];
						return new loc_NormalActivityOutput(activityResultConfig[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET]);
					}
				}));
		return out;
	};

	var loc_getExecuteActivitySequenceRequest = function(activityId, activities, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteActivity", {"activityId":activityId}), handlers, request);
		var activitExecuteRequest;
		var activity = activities[activityId];
		var activityType = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_TYPE];
		if(activityType==node_COMMONCONSTANT.ACTIVITY_TYPE_START){
			activitExecuteRequest = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteStartActivity", {"activity":activity}), 
					function(requestInfo){
						var nextActivityId = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLERESULTACTIVITYNORMAL_FLOW][node_COMMONATRIBUTECONSTANT.DEFINITIONSEQUENCEFLOW_TARGET];
						return loc_getExecuteActivitySequenceRequest(nextActivityId, activities); 
					});
		}
		else if(activityType==node_COMMONCONSTANT.ACTIVITY_TYPE_END){
			activitExecuteRequest = node_createServiceRequestInfoSimple(new node_ServiceInfo("ExecuteEndActivity", {"activity":activity}), 
					function(requestInfo){
						return new loc_EndActivityOutput(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_RESULTNAME]);
					}, 
					handlers, request);
		}
		else{
			activitExecuteRequest = loc_getExecuteNormalActivityRequest(activity, {
				success : function(requestInfo, normalActivityOutput){
					return loc_getExecuteActivitySequenceRequest(normalActivityOutput.next, activities, normalActivityOutput.context);
				}
			}, request);
		}
		out.addRequest(activitExecuteRequest);
		return out;
	};

	var loc_getExecuteProcessRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcess", {}), handlers, request);

		var startActivityId = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_STARTACTIVITYID];
		var activities = loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ACTIVITY];
		out.addRequest(loc_getExecuteActivitySequenceRequest(startActivityId, activities, {
			success : function(requestInfo, endActivityOutput){
				var dataAssociation = node_createDataAssociation(node_createIODataSet(endActivityOutput.context), loc_processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_RESULT][endActivityOutput.resultName], loc_processContextIO);
				return dataAssociation.getExecuteRequest(						
					{
						success : function(requestInfo, processOutputIODataSet){
							return new node_ProcessResult(endActivityOutput.resultName, processOutputIODataSet);
						}
					});
			}
		}));
		return out;
	};

	var loc_getExecuteEmbededProcessRequest = function(embededProcess, input, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteEmbededProcess", {"embededProcess":embededProcess, "input":input}), handlers, request);
		out.addRequest(loc_getExecuteProcessRequest(embededProcess, input, {
			success : function(requestInfo, processResult){
				var backToGlobal = embededProcess[node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_BACKTOGLOBAL][processResult.resultName];
				if(backToGlobal!=null){
					return node_ioTaskUtility.getExecuteDataAssociationRequest(processResult.value, backToGlobal, {
						success : function(request, globalData){
							return new node_ProcessResult(processResult.resultName, globalData.getData());
						}
					});
				}
				else return new node_ProcessResult(processResult.resultName);
			}
		}));
		return out;
	};

	var loc_out = {
			
		getExecuteProcessRequest : function(input, outputMappingsByResult, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_createContextIORequest(input));
			out.addRequest(loc_getExecuteProcessRequest({
				success : function(request, processResult){
					if(outputMappingsByResult==undefined)  return processResult;
					else{
						var outputMapping = outputMappingsByResult[processResult.resultName];
						node_createDataAssociation(loc_processContextIO, outputMapping.dataAssociationDef, outputMapping.dataIO).getExecuteRequest({
							success : function(request, mappedIO){
								return new node_ProcessResult(request.getData(), mappedIO);
							}
						}).withData(processResult.resultName);
						
					}
				}
			}));
			return out;
		},	

		executeProcessRequest : function(input, outputMappingsByResult, handlers, request){
			var requestInfo = this.getExecuteProcessRequest(input, outputMappingsByResult, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		}	
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PROCESS);

	node_getLifecycleInterface(loc_out).init(processDef, envObj);

	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskProcessor", function(){node_ioTaskProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcess", node_createProcess); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_createProcess;

//*******************************************   Start Node Definition  **************************************
var node_createProcessRuntime = function(envObj){

	var loc_envObj = envObj; 
	
	var loc_out = {

		getExecuteProcessResourceRequest : function(id, input, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcessResource", {"id":id, "input":input}), handlers, requestInfo);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([id], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_PROCESS, {
				success : function(requestInfo, processes){
					var process = processes[id];
					return node_createProcess(process, loc_envObj).getExecuteProcessRequest(input, outputMappingsByResult); 
//					loc_out.getExecuteProcessRequest(process, input, outputMappingsByResult);
				}
			}));
			
			return out;
		},
			
		executeProcessResourceRequest : function(id, input, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessResourceRequest(id, input, outputMappingsByResult, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteProcessRequest : function(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requester_parent);

			var outputMappingsByResult;
			var outputMappingDef = processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_OUTPUTMAPPING];
			if(outputMappingDef!=undefined){
				outputMappingsByResult = {};
				_.each(outputMappingDef, function(dataAssociation, resultName){
					outputMappingsByResult[resultName] = new node_ExternalMapping(loc_uiModule.getIOContext(), dataAssociation);
				});
			}

			var output = {};
			out.addRequest(node_createDataAssociation(input, processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_INPUTMAPPING], output).getExecuteDataAssociationRequest(extraInputDataSet, {
				success : function(request, input){
					return input.getGetDataValueRequest(undefined, {
						success : function(request, inputData){
							return node_createProcess(processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK], loc_envObj).getExecuteProcessRequest(inputData, outputMappingsByResult);
						}
					}, request);
//					return node_createProcess(processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK], loc_envObj).getExecuteProcessRequest(input.getData(), outputMappingsByResult);
				}
			}));
			return out;
		},
		
		executeProcessRequest : function(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessRequest(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

var node_createProcessRuntimeFactory = function(){
	var loc_out = {
		createProcessRuntime : function(envObj){
			return node_createProcessRuntime(node_createEnv(envObj));
		}
	};
	return loc_out;
};

var node_createEnv = function(envObj){
	
	if(envObj==undefined)  envObj = {};
	
	var extended = {
			
		buildOutputVarialbeName : function(varName){
			return "nosliw_"+varName;
		},
			
	};
	
	return _.extend(extended, envObj);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("process.createProcess", function(){node_createProcess = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcessRuntimeFactory", node_createProcessRuntimeFactory); 

})(packageObj);
var library = nosliw.getPackage("request");
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

/*
 * store requester object
 * 	type: requestor type (tag, resoruce, system, ...)
 * 	id : requestor id
 * 	info: other information related with type
 */
var node_Requester = function(type, id, info){
	this.type = type;
	this.id = id;
	this.info = info;
};

/*
 * information about request execute 
 *     function to execute
 *     thisContext for function
 */
var node_ServiceRequestExecuteInfo = function(fun, thisContext){
	this.pri_method = fun;
	this.pri_thisContext = thisContext;
};

node_ServiceRequestExecuteInfo.prototype = {
	execute : function(requestInfo){
		return this.pri_method.call(this.pri_context, requestInfo);		
	},
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("Requester", node_Requester); 
packageObj.createChildNode("ServiceRequestExecuteInfo", node_ServiceRequestExecuteInfo); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_eventUtility;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_requestUtility;
	var node_getLifecycleInterface;
//*******************************************   Start Node Definition  ************************************** 	


/**
 * this is object that handler multiple event related with request
 * it wait until request is finished, then it emitt event 
 */
var node_createRequestEventGroupHandler = function(eventHandler, thisContext){
	
	//sync task name for remote call 
	var loc_moduleName = "requestEventGroup";
	
	var loc_eventObject = node_createEventObject();

	//how to handle 
	var loc_eventHandler = eventHandler;
	var loc_thisContext = thisContext;
	
	//all elements
	var loc_elements = {};
	
	//all active root requesters 
	var loc_requesters = {};

	var loc_size = 0;
	
	/*
	 * handle function for all element
	 * process the event only when 
	 * 		requestInfo is done status
	 * 		or requestInfo trigger done event
	 */
	var loc_processEvent = function(){
		request = node_requestUtility.getRequestInfoFromFunctionArguments(arguments);
		if(request==undefined){
			//no request
			loc_eventHandler.call(loc_thisContext, request);
			return;
		}
		
		//all processing is based on root request
		var rootRequest = request.getRootRequest();
		var requestId = rootRequest.getId();
		var request = loc_requesters[requestId];
		if(request==undefined){
			loc_requesters[requestId] = rootRequest;
			rootRequest.registerEventListener(loc_eventObject, function(e, data, req){
				if(e==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
					if(loc_requesters[requestId]!=undefined){
						delete loc_requesters[requestId];
						rootRequest.unregisterEventListener(loc_eventObject);
						loc_eventHandler.call(loc_thisContext, rootRequest);
					}
				}
				else if(e==node_CONSTANT.REQUEST_EVENT_DONE){
				}
			});
		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		//unregister all listeners
		loc_eventObject.clearup();
		
		loc_eventHandler = undefined; 
		loc_thisContext = undefined;
		loc_requesters = {};
	};
	
	var loc_out = {
		/*
		 * add an element to group
		 */
		addElement : function(element, name){
			if(name==undefined)  name = loc_size+"";
			loc_elements[name] = element;
			//register element to event
			node_eventUtility.registerListener(loc_eventObject, element, undefined, loc_processEvent, this);
			loc_size++;
		},
		
		size : function(){return loc_size;},
		getElement : function(name){ return loc_elements[name+""]; },
		
		triggerEvent : function(requestInfo){
			loc_processEvent(undefined, undefined, requestInfo);
		},
		
		destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_moduleName);
	
	return loc_out;
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRequestEventGroupHandler", node_createRequestEventGroupHandler); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createEventObject;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var loc_RequestInfo = function(request, processRemote, attchedTo){
	this.request = request;
	this.processRemote = processRemote;
	this.attchedTo = attchedTo;
};

var loc_trigueRequestEvent = function(request, eventName, eventData){
	request.trigueIndividualEvent(eventName, eventData);
};

var loc_processRequest = function(request, processRemote, processedCallBack){
	var loc_moduleName = "requestManager";
	nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start request : ", request.getService());
	
	//add request processor in order to logging the result
	request.setRequestProcessors({
		start : function(request){
//				nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start handler");
		},
		success : function(request, data){
			nosliw.logging.info(loc_moduleName, request.getInnerId(), "Success handler : ", data);
//				nosliw.logging.trace(loc_moduleName, request.getInnerId(), "Data ", data);
			loc_trigueRequestEvent(request, node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_SUCCESS, data);
			processedCallBack(request);
			return data;
		}, 
		error : function(request, data){
			nosliw.logging.error(loc_moduleName, request.getInnerId(), "Error handler : ", data);
//				nosliw.logging.error(loc_moduleName, request.getInnerId(), "Data ", data);
			loc_trigueRequestEvent(request, node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_ERROR, data);
			processedCallBack(request);
			return data;
		}, 
		exception : function(request, data){
			nosliw.logging.error(loc_moduleName, request.getInnerId(), "Exception handler : ", data);
//				nosliw.logging.error(loc_moduleName, request.getInnerId(), "Data ", data);
			loc_trigueRequestEvent(request, node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_EXCEPTION, data);
			processedCallBack(request);
			return data;
		}, 
	});

	//execute start handler
	var startOut = request.executeStartHandler(request);

	var execute = request.getRequestExecuteInfo();
	if(execute!=undefined){
		//run execute function, return remote task info if have
		var remoteTask = execute.execute(request);
		//whether submit the remoteTask
		if(remoteTask==undefined){
//				nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request locally : ", node_basicUtility.stringify(request.getService()));
		}
		else{
			processRemote = true;   //kkkk
			if(processRemote!=false){
				//submit the remote task
//					nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request with remote request Id :", remoteTask.requestId);
				nosliw.runtime.getRemoteService().addServiceTask(remoteTask);
			}
			else{
//					nosliw.logging.info(loc_moduleName, request.getInnerId(), "Finish request by creating remote request info object Id :", remoteTask.requestId);
				//return the remote task, let the call to decide what to do with remoteTask
				return remoteTask;
			}
		}
	}
	else{
		var service = request.service;
		if(service.type=='dataoperation'){
			requestOperateContextPathValue(request);
		}
	}
};
	
	
var loc_createRequestGroup = function(firstRequest, attachTo){
	var loc_requestSum = 0;
	
	var loc_rootRequest;
	
	var loc_requestInfosById = {};
	var loc_requestIdList = [];
	
	var loc_eventObject = node_createEventObject();

	var loc_doneCallBack;
	
	var loc_attached = [];
	var loc_attachTo = attachTo;
	
	var loc_status = node_CONSTANT.REQUEST_STATUS_INIT;
	
	var loc_requestProcessed = function(request){
		var requestId = loc_getRequestId(request);
		if(loc_requestInfosById[requestId]!=undefined){
			delete loc_requestInfosById[requestId];
			loc_requestIdList.splice( loc_requestIdList.indexOf(requestId), 1 );
			loc_requestSum--;
		}
		if(loc_requestSum==0){
			if(loc_status==node_CONSTANT.REQUEST_STATUS_ACTIVE){
				loc_status = node_CONSTANT.REQUEST_STATUS_ALMOSTDONE;
				loc_rootRequest.registerEventListener(loc_eventObject, 
						function(eventName, eventData){
							if(eventName==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
								loc_rootRequest.unregisterEventListener(loc_eventObject);
								if(loc_requestSum==0){
									loc_doneCallBack(loc_out);
									loc_rootRequest.done();
									loc_status = node_CONSTANT.REQUEST_STATUS_DONE;
								}
								else{
									loc_status = node_CONSTANT.REQUEST_STATUS_ACTIVE;
								}
							}						
						}
					);
				//when no child request under root request, it means root request almost done
				//almost done with root request
				loc_rootRequest.almostDone();
			}
		}
	};

	var loc_getRequestId = function(request){	return request.getInnerId();	};
	
	var loc_out = {
			
		getId : function(){  return loc_rootRequest.getId();  },
		
		addAttached : function(request){			loc_attached.push(request);		},
		getAttached : function(){  return loc_attached;   },
		
		getAttachTo : function(){  return loc_attachTo;  },
		
		addRequestInfo : function(requestInfo){
			if(loc_rootRequest==undefined){
				loc_rootRequest = requestInfo.request.getRootRequest();
				loc_id = loc_rootRequest.getId();
			}
			var requestId = loc_getRequestId(requestInfo.request);
			loc_requestInfosById[requestId] = requestInfo;
			loc_requestIdList.push(requestId);
			loc_requestSum++;
			
			if(loc_status!=node_CONSTANT.REQUEST_STATUS_INIT){
				loc_processRequest(requestInfo.request, requestInfo.processRemote, loc_requestProcessed);
			}
		},
		
		startProcess : function(doneCallBack){
			loc_doneCallBack = doneCallBack;
			loc_status = node_CONSTANT.REQUEST_STATUS_ACTIVE;
			_.each(loc_requestInfosById, function(requestInfo, id){
				loc_processRequest(requestInfo.request, requestInfo.processRemote, loc_requestProcessed);
			});
		},
		
		destroy : function(){
			loc_eventObject.clearup();
		},
		
	};
	
	loc_out.addRequestInfo(firstRequest);
	nosliw.logging.info("Request Group New !!!!!!  " + loc_id + (attachTo==undefined?"":("   Attached to : "+ attachTo.getId())));
	
	return loc_out;
};
	
	
var node_createRequestServiceProcessor = function(){
	//store all the live requests
	//request organized according to root request
	//{root request id  -- {  current request id --- request  }  } 
	var loc_groups = {};
	var loc_processingGroupSum = 0;
	var loc_requestsSum = 0;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_groupQueue = [];
	
	var loc_findGroupInQueue = function(request){
		var out;
		_.each(loc_groupQueue, function(group, index){
			if(group.getId()==request.getId())  out = group;
			_.each(group.getAttached(), function(attached, index){
				if(attached.getId()==request.getId())  out = attached;
			});
		});
		return out;
	};
	
	var loc_findGroupInProcessing = function(request){
		var out;
		_.each(loc_groups, function(group, id){
			if(group.getId()==request.getId())  out = group;
			_.each(group.getAttached(), function(attached, index){
				if(attached.getId()==request.getId())  out = attached;
			});
		});
		return out;
	};

	var loc_findGroup = function(request){
		var out = loc_findGroupInQueue(request);
		if(out!=undefined)  return out;
		return loc_findGroupInProcessing(request);
	};
	
	var loc_addGroupToQueue = function(group){
		loc_groupQueue.push(group);
		loc_processNextGroup();
	};
	
	var loc_processGroup = function(group){
		if(group.getAttachTo()==undefined) loc_eventSource.triggerEvent(node_CONSTANT.REQUESTPROCESS_EVENT_START, group.getId());
		nosliw.logging.info("Request Group : ", group.getId(), " Start Processing !!!!!!");
		loc_processingGroupSum++;
		loc_groups[group.getId()] = group;
		group.startProcess(function(group){
			if(group.getAttachTo()==undefined) loc_eventSource.triggerEvent(node_CONSTANT.REQUESTPROCESS_EVENT_DONE, group.getId());
			nosliw.logging.info("Request Group : ", group.getId(), " Done !!!!!!");
			group.destroy();
			delete loc_groups[group.getId()];
			loc_processingGroupSum--;
			loc_processNextGroup();
		});
		//process attached group
		_.each(group.getAttached(), function(attached, index){
			loc_processGroup(attached);
		});
	};
	
	var loc_processNextGroup = function(){
		if(loc_processingGroupSum==0)
		loc_processGroupInQueue();
	};
	
	var loc_processGroupInQueue = function(){
		if(loc_groupQueue.length>0){
			var group = loc_groupQueue[0];
			loc_groupQueue.shift();//slice(1);
			loc_processGroup(group);
		}
	};
	
	var loc_addRequest = function(requestInfo){
		var group = loc_findGroup(requestInfo.request);
		if(group!=undefined){
			group.addRequestInfo(requestInfo);
		}
		else{
			group = loc_createRequestGroup(requestInfo, requestInfo.attchedTo);
//			group.addRequestInfo(requestInfo);
			if(requestInfo.attchedTo==undefined){
				loc_addGroupToQueue(group);
			}
			else{
				var attachedGroup = loc_findGroupInQueue(requestInfo.attchedTo);
				if(attachedGroup!=undefined){
					//attached in queue, then append to it
					attachedGroup.addAttached(group);
				}
				else{
					attachedGroup = loc_findGroupInProcessing(requestInfo.attchedTo);
					if(attachedGroup!=undefined){
						//attached in processing, then add to processing
						loc_processGroup(group);
					}
					else{
						//otherwise, treat it as normal request
						loc_addGroupToQueue(group);
					}
				}
			}
		}
	};
	
	var loc_out = {
		processRequest : function(request, configure){
			var loc_moduleName = "requestManager";
			nosliw.logging.info(loc_moduleName, request.getInnerId(), "Start Request");
			
			var processRemote = true;
			if(configure!=undefined && configure.processRemote!=undefined){
				processRemote = configure.processRemote;
			}
			
			var attchedTo = undefined;
			if(configure!=undefined && configure.attchedTo!=undefined){
				attchedTo = configure.attchedTo;
			}
			
			return loc_addRequest(new loc_RequestInfo(request, processRemote, attchedTo));
		},	

		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
	};

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createRequestServiceProcessor", node_createRequestServiceProcessor); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
	var node_requestUtility;
	var node_eventUtility;
	var node_basicUtility;
	var node_errorUtility;
	var node_createEventObject;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * requester_parent: requester or parent request
 */
var node_createServiceRequestInfoCommon = function(service, handlers, requester_parent){
	
	var loc_moduleName = "requestInfo";

	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//parse requester_parent parm to get parent or requester info
		var parent = undefined;
		var requester = undefined;
		if(node_getObjectType(requester_parent)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST)		parent = requester_parent;
		else		requester = requester_parent;

		//who initilize this request
		loc_out.pri_requester = requester;
		//set parent request
		loc_out.setParentRequest(parent);
		
		//unique id for this request sequence
		if(loc_out.pri_id==undefined)		loc_out.pri_id = nosliw.generateId();
		//unique id for each request, so that we can trace each request in log
		loc_out.pri_innerId = nosliw.generateId();
		
		//what want to do 
		loc_out.pri_service = service;
		
		//original request handlers
		if(handlers!=undefined)		loc_out.pri_handlers = handlers;
		else loc_out.pri_handlers = {};

		//store all the information for implement the request (runtime)
		loc_out.pri_metaData = {
			//the execute information to process this request directly
			pri_execute : undefined,
			//final handlers used during implementation
			pri_handlers : {},
			//a list of thing to do after normal handler
			pri_postProcessors : [],
			//whether this request need remote request
			pri_isLocal : true, 
			//remote request this service request depend on
			pri_remoteRequest : undefined,
			//other data
			pri_data : {},
			//all parms for this request
			pri_parmData : {},
			//request status : init, processing, done
			pri_status : node_CONSTANT.REQUEST_STATUS_INIT,
			//request process result
			pri_result : undefined,
			//request process input, it is for runtime input which cannot be determined during request creation 
			pri_input : undefined,
			//event object
			pri_eventObject : node_createEventObject(),
			pri_eventObjectIndividual : node_createEventObject(),
		};
		
		//construct handlers
		loc_out.pri_metaData.pri_handlers = {
			start : loc_constructHandler("start"),
			success : loc_constructHandler("success"),
			error : loc_constructHandler("error"),
			exception : loc_constructHandler("exception"),
		};
		
		if(loc_out.pri_service!=undefined){
			//copy all the service data to metaData.data
			_.each(loc_out.pri_service.parms, function(value, name, list){
				this[name] = value;
			}, loc_out.pri_metaData.pri_parmData);
		};
		
		//set status, trigger event, clear result
		loc_initRequest();
	};
	
	/*
	 * construct handler so that it can return data 
	 */
	var loc_constructHandler = function(type){
		return function(requestInfo, data){
			if(type=="start") {
				//start to process request
				loc_startRequest();
			}
			
			//execute configured handler
			var handler = loc_out.pri_handlers[type];
			var out = data;
			if(handler!=undefined){
				var d = handler.call(loc_out, loc_out, data);
				if(d!=undefined){
					if(node_basicUtility.isEmptyValue(d))   out = undefined;  
					else out = d;
				}
			}
			
			//execute configured post process
			var postProcessors = loc_out.getPostProcessors();
			for(var i in postProcessors){
				var postHandler = postProcessors[i][type];
				if(postHandler!=undefined)			out = postHandler.call(loc_out, loc_out, out);
			}
			
			if(type!="start"){
				//do sth when request is done
				loc_finishRequest(out);
			}
			return out;
		};
	};
	
	/*
	 * do sth when request is created
	 *     change status
	 *     clear result
	 *     trigue event
	 */
	var loc_initRequest = function(){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_INIT);
		loc_out.setResult();
		loc_out.pri_metaData.pri_eventObject.triggerEvent(node_CONSTANT.REQUEST_EVENT_NEW, {}, loc_out);
	};
	
	/*
	 * do sth when request start processing
	 *     change status
	 *     clear result
	 *     trigue event
	 */
	var loc_startRequest = function(){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_PROCESSING);
		loc_out.setResult();
		loc_out.pri_metaData.pri_eventObject.triggerEvent(node_CONSTANT.REQUEST_EVENT_ACTIVE, {}, loc_out);
	};
	
	/*
	 * do sth when request finish processing
	 *     change status
	 *     set result
	 *     trigue event
	 */
	var loc_finishRequest = function(data){
		loc_out.setStatus(node_CONSTANT.REQUEST_STATUS_DONE);
		loc_out.setResult(data);
	};
	
	var loc_destroy = function(){
		loc_out.pri_metaData.pri_eventObject.clearup();
	};

	var loc_out = {
			
			getId : function(){return this.pri_id;},
			setId : function(id){
				this.pri_id = id;
				//do something after id get changed, for instance, change id of all child
				this.ovr_afterSetId();
			},
			ovr_afterSetId : function(){},
			
			getInnerId : function(){return this.getId()+"-"+this.pri_innerId;},
			
			getRequester : function(){return this.pri_requester;},
			setRequester : function(requester){this.pri_requester = requester;},
			
			getService : function(){return this.pri_service;},
			
			getType : function(){return this.pri_type;},
			setType : function(type){this.pri_type=type;},
			
			/*
			 * get/set meta data 
			 */
			getParms : function(){ return this.pri_metaData.pri_parmData; },
			getParmData : function(name){return this.pri_metaData.pri_parmData[name];},
			setParmData : function(name, data){this.pri_metaData.pri_parmData[name]=data;},

			
			getData : function(name){
				if(name==undefined)  name="default";
				return this.pri_metaData.pri_data[name];
			},
			setData : function(data, name){this.pri_metaData.pri_data[name]=data;},
			withData : function(data, name){
				if(name==undefined)  name="default";
				this.setData(data, name);
				return this;
			},
			
			/*
			 * execute info: provide function to run for this request
			 */
			getRequestExecuteInfo : function(){return this.pri_metaData.pri_execute;},
			setRequestExecuteInfo : function(execute){this.pri_metaData.pri_execute=execute;},
			
			/*
			 * hanlers within metadata is current handlers for request
			 */
			getHandlers : function(){return this.pri_metaData.pri_handlers;},
			setHandlers : function(handlers){this.pri_metaData.pri_handlers = handlers;},
			getPostProcessors : function(){ return this.pri_metaData.pri_postProcessors},
			addPostProcessor : function(processor){ 
				this.pri_metaData.pri_postProcessors.push(processor); 
			},
			
			exectueHandlerByServiceData : function(serviceData, thisContext){
				var resultStatus = node_errorUtility.getServiceDataStatus(serviceData);
				switch(resultStatus){
				case node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS:
					return this.executeSuccessHandler(serviceData.data, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
					return this.executeErrorHandler(serviceData, thisContext);
					break;
				case node_CONSTANT.REMOTESERVICE_RESULT_ERROR:
					return this.executeExceptionHandler(serviceData, thisContext);
					break;
				}
				
			},
			
			executeHandler : function(type, thisContext, data){
				if(type=="start")		return this.executeStartHandler(thisContext);
				if(type=="success")		return this.executeSuccessHandler(data, thisContext);
				if(type=="error")		return this.executeErrorHandler(data, thisContext);
				if(type=="exception")		return this.executeExceptionHandler(data, thisContext);
			},
			
			executeStartHandler : function(thisContext){
//				nosliw.logging.info(loc_moduleName, this.getInnerId(), "Start handler");
				var out = undefined;
				//internal handler
				var handler = this.getHandlers().start;
				if(handler!=undefined)		out = handler.call(thisContext, this);
				return out;
			},
			
			executeSuccessHandler : function(data, thisContext){
//				nosliw.logging.info(loc_moduleName, this.getInnerId(), "Success handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), "Data ", data);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().success;
				if(handler!=undefined)			out = handler.call(thisContext, this, data);
				return out;
			},
			
			executeErrorHandler : function(serviceData, thisContext){
//				nosliw.logging.error(loc_moduleName, this.getInnerId(), "Error handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), serviceData);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().error;
				if(handler!=undefined)			out = handler.call(thisContext, this, serviceData);
				return out;
			},

			executeExceptionHandler : function(serviceData, thisContext){
//				nosliw.logging.error(loc_moduleName, this.getInnerId(), "Exception handler");
//				nosliw.logging.trace(loc_moduleName, this.getInnerId(), serviceData);

				var out = undefined;
				//internal handler
				var handler = this.getHandlers().exception;
				if(handler!=undefined)			out = handler.call(thisContext, this, serviceData);
				return out;
			},
			
			
			/*
			 * set processor so that they can do sth before call the handlers
			 * we can keep call this method to insert mutiple processor 
			 */
			setRequestProcessors : function(processors){
				
				var handlers = this.getHandlers();
				var newHandlers = {
					start : node_requestUtility.createRequestProcessorHandlerFunction(handlers.start, processors.start),
					success : node_requestUtility.createRequestProcessorHandlerFunction(handlers.success, processors.success),
					error : node_requestUtility.createRequestProcessorHandlerFunction(handlers.error, processors.error),
					exception : node_requestUtility.createRequestProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
			},

			/*
			 * set processor so that they can do sth after call the handlers 
			 */
/*			
			setRequestPostProcessors : function(processors){
				var handlers = this.getHandlers();
				var newHandlers = {
					start : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.start, processors.start),
					success : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.success, processors.success),
					error : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.error, processors.error),
					exception : node_requestUtility.createRequestPostProcessorHandlerFunction(handlers.exception, processors.exception),
				};
				this.setHandlers(node_requestUtility.mergeHandlers(handlers, newHandlers));
			},
*/
			
			/*
			 * whether this request do remote ajax call 
			 */
			isLocalRequest : function(){return this.pri_metaData.pri_isLocal;},
			setIsLocalRequest : function(local){this.pri_metaData.pri_isLocal=local;},

			/*
			 * 
			 */
			getRemoteRequest : function(){return this.pri_metaData.pri_remoteRequest;},
			setRemoteRequest : function(remoteRequest){this.pri_metaData.pri_remoteRequest=remoteRequest;},
			
			getParentRequest : function(){  return this.pri_parentRequest;  },
			setParentRequest : function(parentRequest){  
				if(parentRequest!=undefined){
					this.pri_parentRequest = parentRequest;
					
					//set dependent request id based on parent request id
					this.setId(parentRequest.getId());
					//set dependent requester base on parent requester
					this.setRequester(parentRequest.getRequester());
				}
			},
			
			/*
			 * root request 
			 */
			getRootRequest : function(){
				var request = this;
				var parent = request.getParentRequest(); 
				while(parent!=undefined){
					request = parent;
					parent = request.getParentRequest();
				}
				return request;
			},
			isRootRequest : function(){  return this.getParentRequest()==null;   },
			
			getStatus : function(){  return this.pri_metaData.pri_status;},
			setStatus : function(status){  this.pri_metaData.pri_status = status;},
			
			getResult : function(){  return this.pri_metaData.pri_result; },
			setResult : function(result){  this.pri_metaData.pri_result = result; },

			getInput : function(){  return this.pri_metaData.pri_input; },
			setInput : function(input){  this.pri_metaData.pri_input = input; },
			
			registerIndividualEventListener : function(listener, handler, thisContext){	return this.pri_metaData.pri_eventObjectIndividual.registerListener(undefined, listener, handler, thisContext);	},
			unregisterIndividualEventListener : function(listener){	this.pri_metaData.pri_eventObjectIndividual.unregister(listener);	},
			trigueIndividualEvent : function(event, eventData){	this.pri_metaData.pri_eventObjectIndividual.triggerEvent(event, eventData, this);	},
			
			
			//it is for root request only
			registerEventListener : function(listener, handler, thisContext){	return this.pri_metaData.pri_eventObject.registerListener(undefined, listener, handler, thisContext);	},
			unregisterEventListener : function(listener){	this.pri_metaData.pri_eventObject.unregister(listener);	},
			trigueEvent : function(event, eventData){	this.pri_metaData.pri_eventObject.triggerEvent(event, eventData, this);	},

			//it is for root request only
			almostDone : function(){	this.trigueEvent(node_CONSTANT.REQUEST_EVENT_ALMOSTDONE);		},
			done : function(){
				this.trigueEvent(node_CONSTANT.REQUEST_EVENT_DONE);
				loc_destroy();
			},
			
			trackRequestStack : function(){
				if(loc_out.pri_parentRequest!=undefined)   loc_out.pri_parentRequest.trackRequestStack();
				console.log(node_basicUtility.stringify(loc_out.pri_service));
			}
	};
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoCommon", node_createServiceRequestInfoCommon); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request's process is done by a function
 */
var node_createServiceRequestInfoExecutor = function(service, executor, handlers, requester_parent){

	var loc_executorFun = executor;
	
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_EXECUTOR);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_executorFun, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoExecutor", node_createServiceRequestInfoExecutor); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_RemoteServiceTask;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request is based on a remote request
 */
var node_createServiceRequestInfoRemote = function(syncName, service, setting, handlers, requester_parent){

	var loc_syncName = syncName;
	var loc_service = node_requestUtility.buildService(service);
	var loc_setting = setting;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var remoteServiceTask = new node_RemoteServiceTask(loc_syncName, loc_service, 
				{
					success : function(request, data){
						loc_out.executeSuccessHandler(data, this);
					},
					error : function(request, serviceData){
						loc_out.executeErrorHandler(serviceData, this);
					},
					exception : function(request, serviceData){
						loc_out.executeExceptionHandler(serviceData, this);
					}
				}
		, loc_out, loc_setting);
		return remoteServiceTask;
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_REMOTE);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoRemote", node_createServiceRequestInfoRemote); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_getObjectType;
	var node_createServiceRequestInfoCommon;
	var node_ServiceRequestExecuteInfo;
	var node_requestProcessor;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * a group of requests that will be processed one by one
 * for cases that have uncertain request consequence when request group is created (or next request is depend on previous request result)
 * the item within requests can be two type of value:
 * 		request info object
 * 		or function that return request info object
 * the return value of success handler of each request tells a lot of information about next requestinfo:
 * 		undefined:  	no indication for next  
 * 		request info :	used as next request info
 * 		array:			used as a array of request info	
 */
var node_createServiceRequestInfoSequence = function(service, handlers, requester_parent){

	var loc_startOutDataName = "startOutDataName";
	
	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//store all request info objects 
		loc_out.pri_requestInfos = [];
		//the next requester index
		loc_out.pri_cursor = 0;
		
		//modify start handler, in order to process output from start handler
		loc_out.addPostProcessor({
			start : function(requestInfo, startOut){
				loc_out.setData(startOut, loc_startOutDataName);
			}
		});
	};
	
	//add child request, 
	//requestInfo can be single request or a array of request
	var loc_addChildRequest = function(requestInfo){
		if(loc_out.getStatus()!=node_CONSTANT.REQUEST_STATUS_INIT){
			if(loc_out.pri_cursor<loc_out.pri_requestInfos.length-1){
				loc_out.pri_requestInfos = loc_out.pri_requestInfos.slice(0, loc_out.pri_cursor+1);
			}
		}
		if(_.isArray(requestInfo)==true){
			_.each(requestInfo, function(req, i){
				loc_out.pri_requestInfos.push(req);
			});
		}
		else{
			loc_out.pri_requestInfos.push(requestInfo);	
		}
	}
	
	
	/*
	 * process request in sequence according to its index
	 * data : return value from previous request
	 */
	var loc_processNextRequestInSequence = function(previousRequest, data){
		
		if(_.isFunction(data)){
			data = data.call(loc_out, loc_out);
		}
		
		if(_.isArray(data)==true){
			//check if this array is data or a array of request
			var isRequestArray = true;
			if(data.length==0)  isRequestArray = false;
			else{
				for(var i in data){
					if(node_getObjectType(data[i])!=node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
						isRequestArray = false;
						break;
					}
				}
			}
			if(isRequestArray==true){
				//if data is an array of request
				loc_addChildRequest(data);
				data = undefined;
			}
		}
		else{
			if(node_getObjectType(data)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				//for request
				loc_addChildRequest(data);
				data = undefined;
			}
		}
		
		if(loc_out.pri_requestInfos.length<=loc_out.pri_cursor){
			//not more request in queue
			loc_out.executeSuccessHandler(data, loc_out);
		}
		else{
			var requestInfo = loc_out.pri_requestInfos[loc_out.pri_cursor];

			requestInfo.setParentRequest(loc_out);
			
			//pass the result from previous request to input of current request
			if(previousRequest!=undefined)		requestInfo.setInput(previousRequest.getResult());
			
			var processMode = requestInfo.getParmData('processMode');
			if(processMode=="eventBased"){
				var listener = requestInfo.registerIndividualEventListener(undefined, function(eventName, eventData){
					if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_SUCCESS){
						loc_out.pri_cursor++;
						loc_processNextRequestInSequence(requestInfo, eventData);
					}
					else if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_ERROR){
						loc_out.executeErrorHandler(eventData, loc_out);
					}
					if(eventName==node_CONSTANT.REQUEST_EVENT_INDIVIDUAL_EXCEPTION){
						loc_out.executeExceptionHandler(eventData, loc_out);
					}
					requestInfo.unregisterIndividualEventListener(listener);
				}, requestInfo);
			}
			else if(processMode=="promiseBased"){
				requestInfo.addPostProcessor({
					success : function(requestInfo, out){
						var promise = new Promise(function(resolve, reject) {
							  resolve({
								  request : requestInfo,
								  data : out
							  });
						});

						promise.then(function(result) {
							loc_out.pri_cursor++;
							loc_processNextRequestInSequence(requestInfo, out);
						}, function(err) {});
					},
					error : function(requestInfo, serviceData){
						loc_out.executeErrorHandler(serviceData, loc_out);
					},
					exception : function(requestInfo, serviceData){
						loc_out.executeExceptionHandler(serviceData, loc_out);
					},
				});
			}
			else{
				requestInfo.addPostProcessor({
					success : function(requestInfo, out){
						loc_out.pri_cursor++;
						loc_processNextRequestInSequence(requestInfo, out);
					},
					error : function(requestInfo, serviceData){
						loc_out.executeErrorHandler(serviceData, loc_out);
					},
					exception : function(requestInfo, serviceData){
						loc_out.executeExceptionHandler(serviceData, loc_out);
					},
				});
			}
			node_requestProcessor.processRequest(requestInfo);
		}
		
	};
	
	var loc_process = function(){
		//retrieve start handler out
		var startHandlerOut = loc_out.getData(loc_startOutDataName);
		//start process first request
		loc_processNextRequestInSequence(undefined, startHandlerOut);
	};
	
	var loc_out = {
			
		ovr_afterSetId : function(){
			//change all children's id
			var id = this.getId();
			_.each(loc_out.pri_requestInfos, function(childRequestInfo, name, list){
				childRequestInfo.setId(id);
			}, this);			
		},
			
		addRequest : function(requestInfo){
			if(requestInfo!=undefined)	loc_addChildRequest(requestInfo);
		},
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SEQUENCE);
	
	loc_constructor(service, handlers, requester_parent);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSequence", node_createServiceRequestInfoSequence); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_requestProcessor;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * This type of request is for service that is depend on another service 
 * Another service would do the most job
 * Once another service done its job, this service's handler will be called
 */
var node_createServiceRequestInfoService = function(service, handlers, requester_parent){
	
	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//dependent service
		loc_out.pri_dependentService = undefined;
	};
	
	var loc_buildDependentRequestHandler = function(hanlderName){
			return function(dependentRequestInfo, data){
				var parentHandler = loc_out.getHandlers()[hanlderName];
				var dependentResultProcessor = loc_out.pri_dependentService.processors==undefined?undefined : loc_out.pri_dependentService.processors[hanlderName];
				var parentRequestInfo = dependentRequestInfo.getParentRequest();
				
				var out = data;
				if(dependentResultProcessor!=undefined){
					out = dependentResultProcessor.call(dependentRequestInfo, dependentRequestInfo, data);
				}
				if(parentHandler!=undefined){
					out = parentRequestInfo.executeHandler(hanlderName, parentRequestInfo, out);
				}			
				return out;
			};
	};
	
	/*
	 * create handlers for child request
	 */
	var loc_buildDependentRequestHandlers = function(){
		loc_out.pri_dependentService.requestInfo.addPostProcessor({
			start : loc_buildDependentRequestHandler("start"),
			success : loc_buildDependentRequestHandler("success"),
			error : loc_buildDependentRequestHandler("error"),
			exception : loc_buildDependentRequestHandler("exception"),
		});
	};

	/*
	 * process request with child request 
	 */
	var loc_processRequestWithDependentRequest = function(reqeustInfo){
		var dependentRequest = reqeustInfo.getDependentServiceRequestInfo();
		return node_requestProcessor.processRequest(dependentRequest);
	};
	
	var loc_out = {
			ovr_afterSetId : function(){
				//change all children's id
				var id = this.getId();
				loc_out.pri_dependentService.requestInfo.setId(id);
			},
			
			getDependentServiceRequestInfo : function(){
				var dependentService = this.pri_dependentService;
				if(dependentService!=undefined){
					return dependentService.requestInfo;
				}
			},
			
			/*
			 * when some service need to do sth part of which can be provided by another service
			 * this service can be child service so that it does not need to be implemented again within another service
			 * child service has the its own requestinfo that share the request id and requester with its parent service 
			 */
			setDependentService : function(dependentService){
				this.pri_dependentService = dependentService;
				//set parent request
				this.pri_dependentService.requestInfo.setParentRequest(this);
				
				//create child reqeust handler based on original handlers and processors
				loc_buildDependentRequestHandlers(); 
				
				this.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_processRequestWithDependentRequest, this));
			},
	};

	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SERVICE);
	
	loc_constructor(service, handlers, requester_parent);
	
	return loc_out;
};

/*
 * information about child service
 * child service and parent have the same reqeuster
 * 		requestInfo : 	request infor for child service
 * 		processor: 		do something after child request return
 */
var node_DependentServiceRequestInfo = function(requestInfo, processors){
	this.requestInfo = requestInfo;
	this.processors = processors;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoService", node_createServiceRequestInfoService); 
packageObj.createChildNode("entity.DependentServiceRequestInfo", node_DependentServiceRequestInfo); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_requestProcessor;
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createServiceRequestInfoSet = function(service, handlers, requester_parent){

	service = node_requestUtility.buildService(service);

	var loc_constructor = function(service, handlers, requester_parent){
		//all the requests   name -- request
		loc_out.pri_requests = {};
		//total request number
		loc_out.pri_requestSum = 0;
		//number of request processed
		loc_out.pri_requestNum = 0;
		//request results (only exception)
		loc_out.pri_requestResults = loc_reateRequestSetResult();
	};
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		
		//if no child request, 
		if(loc_out.pri_requestSum==0)		loc_processResult();
		
		var remoteRequests = [];
		_.each(loc_out.pri_requests, function(childRequestInfo, name, list){
			var remoteRequest = node_requestProcessor.processRequest(childRequestInfo, false);
			if(remoteRequest!=undefined){
				if(_.isArray(remoteRequest)){
					for(var i in remoteRequest){
						remoteRequests.push(remoteRequest[i]);
					}
				}
				else{
					remoteRequests.push(remoteRequest);
				}
			}
		}, this);
		if(remoteRequests.length==0)  return;
		return remoteRequests;
	};
		
	/*
	 * create handlers for child request
	 */
	var loc_updateChildRequestHandlers = function(name, childRequest){
		childRequest.addPostProcessor({
			success : loc_createChildRequestSuccessProcessor(name),
			error : loc_createChildRequestSuccessProcessor(name),
			exception : loc_createChildRequestSuccessProcessor(name),
		});
	};

	/*
	 * create handler for child request
	 * 	this handler combine original handler and childResultProcessor to create new handler
	 */
	var loc_createChildRequestSuccessProcessor = function(name){
		return function(childRequestInfo, data){
			var out = data;
			loc_out.pri_requestResults.addResult(name, out);
			loc_processResult();
			return out;
		};
	};
	
	/*
	 * 
	 */
	var loc_processResult = function(){
		loc_out.pri_requestNum++;
		if(loc_out.pri_requestNum>=loc_out.pri_requestSum){
			//if finish all requests
			loc_out.executeSuccessHandler(loc_out.pri_requestResults, loc_out);
		}
	};
	
	var loc_out = {
		ovr_afterSetId : function(){
			//change all children's id
			var id = this.getId();
			_.each(loc_out.pri_requests, function(childRequestInfo, name, list){
				childRequestInfo.setId(id);
			}, this);			
		},
		
		addRequest : function(name, childRequest){
			if(childRequest!=undefined){
				childRequest.setParentRequest(this);
				this.pri_requests[name] = childRequest;
				loc_updateChildRequestHandlers(name, childRequest);
				this.pri_requestSum++;
			}
		},
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	loc_constructor(service, handlers, requester_parent);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SET);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};


loc_reateRequestSetResult = function(){
	var loc_results = {};
	var loc_out = {
		addResult : function(name, result){
			loc_results[name] = result;
		},
		
		getResult : function(name){
			return loc_results[name];
		},
		
		getResults : function(){
			return loc_results;
		},
		
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSet", node_createServiceRequestInfoSet); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_CONSTANT;
	var node_requestUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * The request's process is done by a function
 */
var node_createServiceRequestInfoSimple = function(service, processor, handlers, requester_parent){

	service = node_requestUtility.buildService(service);
	var loc_processorFun = processor;
	
	/*
	 * exectue function 
	 */
	var loc_process = function(requestInfo){
		var out = loc_processorFun.call(this, requestInfo);
		loc_out.executeSuccessHandler(out, loc_out);
	};
		
	var loc_out = {
			
	};
	
	loc_out = _.extend(node_createServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(node_CONSTANT.REQUEST_TYPE_SIMPLE);
	
	loc_out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(loc_process, this));
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoSimple", node_createServiceRequestInfoSimple); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_Requester;
//*******************************************   Start Node Definition  ************************************** 	

var node_buildServiceProvider = function(object, moduleName){
	var loc_moduleName = moduleName;
	
	//default requester 
	var loc_requester = new node_Requester(node_CONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	var loc_service = {
		getModuleName : function(){
			return loc_moduleName;
		},
	
		getRequestInfo : function(request){
			if(request==undefined)   return loc_requester;
			else return request;
		}
	};
	
	var loc_out = _.extend(object, loc_service);
	
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.Requester", function(){node_Requester = this.getData();});


//Register Node by Name
packageObj.createChildNode("buildServiceProvider", node_buildServiceProvider); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_eventUtility;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	var loc_out = {
			
			buildService : function(service){
				if(service!=undefined)  return service;
				else{
					return {
						name : loc_out.buildService.caller.caller.name,
					};
				} 
			},
			
			/**
			 * last one in argus should be request info
			 */
			getRequestInfoFromFunctionArguments : function(argsArray){
				return argsArray[argsArray.length-1];
			},

			getHandlersFromFunctionArguments : function(argsArray){
				return argsArray[argsArray.length-2];
			},
			
			/*
			 * register listener for event related with request 
			 */
			registerEventWithRequest : function(source, eventName, handler, thisContext){
				var listener = {};
				node_eventUtility.registerEvent(listener, source, eventName, function(event, data){
					handler.call(this, event, data.data, data.requestInfo);
				}, thisContext);
				return listener;
			},
			
			/*
			 * trigger event related with request
			 */
			triggerEventWithRequest : function(source, event, data, requestInfo){
				node_eventUtility.triggerEvent(source, event, {
					data : data,
					requestInfo : requestInfo,
				});		
			},

			
			/*
			 * get remote service task by requestInfo according to  convention
			 * convention : 
			 * 		remote request service name : requestInfo service name
			 * 		remote request service data : requestInfo service data
			 */
			getRemoteServiceTask : function(syncTaskName, requestInfo){
				//since is building remote service task, this request need remote call
				requestInfo.setIsLocalRequest(false);
				//create remote request handlers based on service request handlers 
				var handlers = node_requestUtility.getRemoteServiceTaskHandlersFromRequestHandlers(requestInfo.getHandlers());
				var remoteReq = new NosliwRemoteServiceTask(syncTaskName, new NosliwServiceInfo(requestInfo.getService().command, requestInfo.getParms()), handlers, requestInfo);
				requestInfo.setRemoteRequest(remoteReq);
				return remoteReq;
			},
			
			/*
			 * clone handlers
			 */
			cloneHandlers : function(handlers){
				return this.mergeHandlers(handlers);
			},
			
			/*
			 * merge two handlers together to create a new one
			 * the second handler will override the first one
			 */
			mergeHandlers : function(handlers, overrideHandlers){
				var out = {};
				
				out.start = handlers.start;
				if(overrideHandlers!=undefined && overrideHandlers.start!=undefined)  out.start=overrideHandlers.start;

				out.success = handlers.success;
				if(overrideHandlers!=undefined && overrideHandlers.success!=undefined)  out.success=overrideHandlers.success;

				out.error = handlers.error;
				if(overrideHandlers!=undefined && overrideHandlers.error!=undefined)  out.error=overrideHandlers.error;

				out.exception = handlers.exception;
				if(overrideHandlers!=undefined && overrideHandlers.exception!=undefined)  out.exception=overrideHandlers.exception;

				return out;
			},

			/*
			 * create remote service tasks handlers from request handlers
			 */
			getRemoteServiceTaskHandlersFromRequestHandlers : function(handlers){
				var requestHandlers = handlers;
				var out = {};
				
				var success = requestHandlers.success;
				if(success!=undefined){
					out.success = function(serviceTask, data){
						var requestInfo = serviceTask.requestInfo;
						success.call(requestInfo, requestInfo, data);
					};
				}

				var error = requestHandlers.error;
				if(error!=undefined){
					out.error = function(serviceTask, serviceData){
						var requestInfo = serviceTask.requestInfo;
						error.call(requestInfo, requestInfo, serviceData);
					};
				}
				
				var exception = requestHandlers.exception;
				if(exception!=undefined){
					out.exception = function(serviceTask, serviceData){
						var requestInfo = serviceTask.requestInfo;
						exception.call(requestInfo, requestInfo, serviceData);
					};
				}

				return out;
			},

			/*
			 * create a new handler function that insert  requestProcessor into handler input
			 */
			createRequestProcessorHandlerFunction : function(handler1, requestProcessor1){
				var handler = handler1;
				var requestProcessor = requestProcessor1;
				if(requestProcessor==undefined)  return handler;
				return function(requestInfo, data){
					var processorOut = requestProcessor.call(requestInfo, requestInfo, data);
					if(handler==undefined)  return processorOut;
					return handler.call(requestInfo, requestInfo, processorOut);
				};
			},

			/*
			 * create a new handler function that do post process into handler input
			 */
			createRequestPostProcessorHandlerFunction : function(handler1, requestProcessor1){
				var handler = handler1;
				var requestProcessor = requestProcessor1;
				if(requestProcessor==undefined)  return handler;
				return function(requestInfo, data){
					var handlerOut = undefined;
					if(handler!=undefined)  handlerOut = handler.call(requestInfo, requestInfo, data);
					var processorOut = requestProcessor.call(requestInfo, requestInfo, handlerOut);
					return processorOut;
				};
			},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("service");
//get/create package
var packageObj = library.getChildPackage("idservice");    


(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createIdService = function(){
	var loc_id = 0;
	
	var loc_out = {
		generateId : function(){
			loc_id++;
			return loc_id;
		},
	
		reset : function(){
			loc_id = 0;
			return loc_id;
		}
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createIdService", node_createIdService); 

})(packageObj);
var library = nosliw.getPackage("resource");
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = type; 
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = id; 
};	
	
var node_Resource = function(resourceInfo, resourceData, info){
	this.resourceInfo = resourceInfo;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_Resource;
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * Create Resource Manager
 * It manage resources
 * It does not do the job of loading resources, it is the job of resource service
 */
var node_createResourceManager = function(){
	
	var loc_resources = {};

	var loc_getResource = function(resourceId){
		return node_resourceUtility.getResourceFromTree(loc_resources, resourceId);
	};
	
	var loc_out = {
		
		/**
		 * Add resource to resourc manager 
		 */
		addResource : function(resourceInfo, resourceData, info){
			var resource = new node_Resource(resourceInfo, resourceData, info);
			node_resourceUtility.buildResourceTree(loc_resources, resource);
		},	
	
		/**
		 * Same as get resources
		 * It also mark the resource as using by user
		 */
		useResource : function(resourceId, userId){
			return loc_getResource(resourceId);
		},
		
		/**
		 * It mark the resource as not using by user
		 */
		dismissResource : function(resourceId, userId){
			
		},
	};
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.entity.Resource", function(){node_Resource = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createResourceManager", node_createResourceManager); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_buildServiceProvider;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoExecutor;
	var node_requestServiceProcessor;
	var node_ServiceInfo;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_resourceUtility;
	var node_ResourceId;
	var node_DependentServiceRequestInfo;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_resourceManager = resourceManager;

	var loc_getFindDsicoveredResourcesRequest = function(resourceIds, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("FindDiscoveredResources", {"resourcesId":resourceIds}), function(requestInfo){
			var result = {
				found : {},
				missed : []
			};
			loc_findDiscoveredResources(resourceIds, result);
			return result;
		}, handlers, requestInfo);
		return out;
	};
	
	//find all the resources by id and related resources
	var loc_findDiscoveredResources = function(resourceIds, result){
		var foundResourcesTree = result.found;
		var missedResourceIds = result.missed;
		
		_.each(resourceIds, function(resourceId, index, list){
			var resource = loc_resourceManager.useResource(resourceId);
			if(resource!=undefined){
				//resource exist
				node_resourceUtility.buildResourceTree(foundResourcesTree, resource);

				//discover related resources (dependency and children)
				var relatedResourceIds = []; 
				var resourceInfo = resource.resourceInfo;
				_.each(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY], function(childResourceId, alias, list){
					relatedResourceIds.push(childResourceId);
				}, this);

				_.each(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_CHILDREN], function(childResourceId, alias, list){
					relatedResourceIds.push(childResourceId);
				}, this);
				
				loc_findDiscoveredResources(relatedResourceIds, result);
			}
			else{
				missedResourceIds.push(resourceId);
			}
		});
	};

	//load resources for runtime
	var loc_getLoadResourcesRequest = function(resourceInfos, handlers, requester_parent){
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_RESOURCE;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_LOADRESOURCES;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_LOADRESOURCES_RESOURCEINFOS] = resourceInfos;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
		
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoService(new node_ServiceInfo("LoadResources", {"resourcesInfo":resourceInfos}), handlers, requestInfo);
		out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
		
		return out;
	};
	
	var loc_out = {
		//resource get
		getRequireResourcesRequest : function(resourcesInfo, handlers, requester_parent){
			var serviceInfo = new node_ServiceInfo("RequireResources", {"resourcesInfo":resourcesInfo});
			
			//look for resources in resource manager
			var resourceTree = {};
			var resourceInfos = [];
			_.each(resourcesInfo, function(resourceInfo, index, list){
				var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
				if(resource!=undefined)			node_resourceUtility.buildResourceTree(resourceTree, resource);
				else		resourceInfos.push(resourceInfo);
			}, this);
			
			var out;
			if(resourceInfos.length==0){
				//all exists
				 out = node_createServiceRequestInfoSimple(serviceInfo, function(){ return resourceTree; }, handlers, loc_out.getRequestInfo(requester_parent));
			}
			else{
				out = node_createServiceRequestInfoService(serviceInfo, handlers, loc_out.getRequestInfo(requester_parent));
				
				//load some
				var loadResourceRequest = loc_getLoadResourcesRequest(resourceInfos, {}, null);
				
				out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
					success : function(requestInfo){
						_.each(resourceInfos, function(resourceInfo, index, list){
							var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
							node_resourceUtility.buildResourceTree(resourceTree, resource);
						}, this);
						return resourceTree;
					}}));
			}
			return out;
		},
			
			
		executeRequireResourcesRequest : function(resourcesInfo, handlers, requester_parent){
			var requestInfo = this.getRequireResourcesRequest(resourcesInfo, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
		//resource discovery + get
		getGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetResources", {"resourcesId":resourceIds}), handlers, loc_out.getRequestInfo(requester_parent));

			//find missing resources
			out.addRequest(loc_getFindDsicoveredResourcesRequest(resourceIds, {
				success : function(requestInfo, data){
					var missedResourceIds = data.missed;
					var foundResourcesTree = data.found;
					if(missedResourceIds.length==0){
						//all found
						return foundResourcesTree;
					}
					else{
						//need load resource
						//do discovery first
						var discoverResourcesRequest = loc_out.getDiscoverResourcesRequest(missedResourceIds, {
							success : function(requestInfo, resourceInfos){
								//after discovery, load resources
								var loadResourceRequest = loc_getLoadResourcesRequest(resourceInfos, {
									success : function(requestInfo){
										_.each(resourceInfos, function(resourceInfo, index, list){
											var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
											node_resourceUtility.buildResourceTree(foundResourcesTree, resource);
										}, this);
										return foundResourcesTree;
									}
								});
								return loadResourceRequest;
							}
						});
						return discoverResourcesRequest;
					}
				}
			}, out));
			return out;
		},
			
		executeGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
		//resource discovery
		getDiscoverResourcesRequest : function(resourceIds, handlers, requester_parent){
			
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_RESOURCE;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES_RESOURCEIDS] = resourceIds;
			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
			
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("DiscoverResources", {"resourcesId":resourceIds}), handlers, requestInfo);
			out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
			return out;
		},

		executeDiscoverResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getDiscoverResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		//getGetResourcesRequest + return resource data by type
		getGetResourceDataByTypeRequest : function(ids, resourceType, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("GetResourceDataByTypeRequest", {"ids":ids, "resourceType":resourceType}), handlers, requestInfo)
			
			//get resource request
			var resourceIds = [];
			for(var i in ids)		resourceIds.push(new node_ResourceId(resourceType, ids[i]));
			var loadResourceRequest = this.getGetResourcesRequest(resourceIds);
			
			out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
				success : function(requestInfo, resourceTree){
					//translate tree to resources by id
					var resourcesData = {};
					var resources = node_resourceUtility.getResourcesByTypeFromTree(resourceTree, resourceType);
					_.each(resources, function(resource, id){
						resourcesData[id] = resource.resourceData;
					});
					return resourcesData;
				}
			}));
			return out;
		},
		
		executeGetResourceDataByTypeRequest : function(ids, resourceType, handlers, requester_parent){
			var requestInfo = this.getGetResourceDataByTypeRequest(ids, resourceType, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		/**
		 * Import resource 
		 */
		importResource : function(resourceInfo, resourceData, info){
			loc_resourceManager.addResource(resourceInfo, resourceData, info);
		},
		
		getResource : function(resourceId){
			return loc_resourceManager.useResource(resourceId);
		}
	};
	
	loc_out = node_buildServiceProvider(loc_out, "resourceService");
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoExecutor", function(){node_createServiceRequestInfoExecutor = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createResourceService", node_createResourceService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = 
{
		buildResourceTree : function(tree, resource){
			var resourceId = resource.resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID];
			var type = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined){
				typeResources = {};
				tree[type] = typeResources;
			}
			typeResources[id] = resource; 
		},

		getResourceFromTree : function(tree, resourceId){
			var type = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = tree[type];
			if(typeResources==undefined)  return undefined;
			return typeResources[id];
		},
		
		getResourcesByTypeFromTree : function(tree, resourceType){
			var typeResources = tree[resourceType];
			return typeResources;
		},
		
		createOperationResourceId : function(dataTypeId, operation){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = node_namingConvensionUtility.cascadeLevel1(dataTypeId, operation); 
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_OPERATION; 
			return out;
		},

		createConverterResourceId : function(dataTypeId){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = node_namingConvensionUtility.cascadeLevel1(dataTypeId); 
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONVERTER; 
			return out;
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("uidata");


/**
 * value : 	data itself, have no data type information

 * data : 	value + data type
 * 			data can only used for reading, not for writing
 * wrapper:
 * 			wrapper should not be exposed to user, only variable
 * 			wrapper has two ways to do it:  data based and parent wrapper based
 * 				data based : it store root data and the path from root data to this data
 * 				parent wrapper based: it store parent wrapper and the path from data in parent wrapper to this data
 * 			wrapper is for data operation request, when through wrapper, all other wrapper is informed of the data change
 * 			with wrapper, you can do followings:
 * 				get current data represented by wrapper
 * 				create child wrapper based on path to child wrapper
 * 				register listeners for data operation event
 * 				operate on data according to request infor and trigue event
 * 			three type of events: 
 * 				data operation event : inform children and variable about what happend within wrapper
 * 				lifecycle event : inform children about lifeccycle winthin wrapper : clearup
 * 				internal event : 
 * 					inform children about data operation event. 
 * 					for instance, for delete element operation, the container will receive the DELETEELEMENT event
 * 					then beside forward the same event, the container will triggue another event DELETE for delete child
 * 					this DELETE event should not be processed by variable, it should only be delivered to responding children  
 *  
 * variable: 
 * 		variable can contain wrapper 
 * 		
 * 
 * 			a variable that can contain wrapper that can be set to differen value
 * 			variable only listen to data operation event from wrapper, NO lifecycle event. 
 * 			It is because all the all the wrapper lifecycle is driven by variable
 * 			two types of wrapper variables: normal and child 
 * 			child variable is dependent on normal variable: its wrapper is wrapper based on wrapper within parent variable
 * 			variable event : 
 * 				SETWRAPPER
 * 				CLEARUP
 * 			wrapper operation event:
 * 				CHANGE
 * 				ADDELEMENT
 * 				DELETEELEMENT	
 * 
 * variable wrapper : 
 * 		wrapper of variable
 *		only variable wrapper is exposed to user, no data wrapper or variable
 *		no lifecycle event exposed, only data operation event exposed
 *		whoever create variable wrapper, please release it
 * 
 * context : 
 * 			a set of normal variables wrappers
 * 			event : 
 * 				BEFOREUPDATE
 * 				AFTERUPDATE
 * 				UPDATE
 * 
 * contextVariable info: 
 * 			not a real variable
 * 			name + path to describe the variable
 * 
 */


/*
wrapper:
wrapper is about data operation, get value
adapter :   provided by variable
	value adapter 
	path adapter
	

data operation event : (listened by variable, child wrapper)
all data operation is converted to data operation on root data, then trigue data operation event 	
root wrapper event trigue by data operation:	
	WRAPPER_EVENT_SET : WRAPPER_OPERATION_SET
	WRAPPER_EVENT_ADDELEMENT: WRAPPER_OPERATION_ADDELEMENT
	WRAPPER_EVENT_DELETEELEMENT:  WRAPPER_OPERATION_DELETEELEMENT
	WRAPPER_EVENT_DELETE :  WRAPPER_OPERATION_DELETE
	
leaf wrapper 
	WRAPPER_EVENT_CHANGE : inform child that the data is dirty
	WRAPPER_EVENT_FORWARD : event happened on child node, just forward event to child who maybe response to this event
	
	WRAPPER_EVENT_DELETE : when got WRAPPER_EVENT_DELETE event from parent, or grand parent ...
	WRAPPER_EVENT_ADDELEMENT : when got WRAPPER_EVENT_ADDELEMENT event from parent
	WRAPPER_EVENT_DELETEELEMENT : when got WRAPPER_EVENT_DELETEELEMENT event from parent
	WRAPPER_EVENT_SET : when got WRAPPER_EVENT_SET event from parent
	
lifecycle event : (listened by child wrapper only)
	WRAPPER_EVENT_CLEARUP    when wrapper is destroyed (which may trigued by destroy method call or parent CLEARUP event)

internal event : (listened by child wrapper only)
	WRAPPER_EVENT_DELETE	 when wrapper is informed by WRAPPER_EVENT_DELETEELEMENT to delete element within this wrapper, trigue delete event with path on child wrapper

when wrapper data is deleted, wrapper will trigue two event:
	DELETE
	CLEARUP
	
	
variable :
	variable is about 
	not like wrapper which only reference to parent wrapper, variable also contains child variables (loop through child)
	the reason containing child variable is that when create child variable, parent can reuse variable
	
adapter : 
	valueAdapter
	pathAdapter
	eventAdapter
	destroyAdapter
	dataOperationAdapter
	
variable only listen to operation event from wrapper, not lifecycle event, not internal event
variable does not need to listen to lifecycle event from wrapper, because 
	variable will determine lifecycle of wrapper (either by variable.destory or delete operation)

communication between parent variable and child variable is only about two event related with delete: WRAPPER_EVENT_DELETE, WRAPPER_EVENT_CLEARUP
	
lifecycle event: (listened by parent variable only, so that parent can remove child from itself)
	WRAPPER_EVENT_CLEARUP	when child variable is destroyed, 
	when parent variable is destroyed, parent will destroy all children
	
data operation event : (listened by variable wrapper)
	any data operation event from wrapper
	
	
	
for wrapper with any adapter (pathAdapter) does not allow child wrapper with empty path
it will cause issue when operate on child wrapper 	
	
	
	
	
	
	 * 
 */


//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createEventObject;
var node_eventUtility;
var node_createContextElement;
var node_createContextVariableInfo;
var node_createVariable;
var node_DataOperationService;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_namingConvensionUtility;
var node_ServiceInfo;
var node_dataUtility;
var node_createServiceRequestInfoSimple;
var node_createVariableWrapper;
var node_getHandleEachElementRequest;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSet;
var node_uiDataOperationServiceUtility;
var node_createUIDataOperationRequest;
var node_uiDataOperationServiceUtility;
var node_UIDataOperation;
var node_createVariableGroup;

//*******************************************   Start Node Definition  ************************************** 	
/*
 * elementInfosArray : an array of element info describing context element
 * 
 */
var node_createContext = function(elementInfosArray, request){
	
	//according to contextVariableInfo, find the base variable from Context
	//base variable contains two info: 1. variable,  2. path from variable
	var loc_findBaseVariable = function(contextVariableInfo){
		var fullPath = contextVariableInfo.getFullPath();
		
		//get parent var from adapter first
		//find longest matching path
		var parentVar;
		var varPath = contextVariableInfo.path;
		var pathLength = -1;
		_.each(loc_out.prv_adapters, function(adapterVariable, path){
			var comparePath = node_dataUtility.comparePath(fullPath, path);
			if(path.length>pathLength){
				if(comparePath.compare==0){
					parentVar = adapterVariable;
					varPath = "";
					pathLength = path.length;
				}
				else if(comparePath.compare==1){
					parentVar = adapterVariable;
					varPath = comparePath.subPath;
					pathLength = path.length;
				}
			}
		});
		
		//not found, use variable from elements
		if(parentVar==undefined){
			if(loc_out.prv_elements[contextVariableInfo.name]!=undefined){
				parentVar = loc_out.prv_elements[contextVariableInfo.name].variable;
				varPath = contextVariableInfo.path;
			}
			else return;
		}
		
		return {
			variable : parentVar,
			path : varPath
		}
	};
	
	/*
	 * get context element variable by name
	 */
	var loc_getContextElementVariable = function(name){ 
		var contextEle = loc_out.prv_elements[name];
		if(contextEle==undefined)  return undefined;
		return contextEle.variable;
	};
	
	var loc_createVariableFromContextVariableInfo = function(contextVariableInfo, adapterInfo, requestInfo){
		var baseVar = loc_findBaseVariable(contextVariableInfo);
		if(baseVar==undefined){
			nosliw.error(contextVariableInfo);
			loc_findBaseVariable(contextVariableInfo);
		}
		var variable = baseVar.variable.createChildVariable(baseVar.path, adapterInfo, requestInfo); 
		return variable;
	};
	
	var loc_buildAdapterVariableFromMatchers = function(rootName, path, matchers, reverseMatchers){
		var contextVar = node_createContextVariableInfo(rootName, path);
		var adapter = {
			getInValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, matchers, handlers, request);
			},
			getOutValueRequest : function(value, handlers, request){
				return nosliw.runtime.getExpressionService().getMatchDataRequest(value, reverseMatchers, handlers, request);
			},
		};
		var variable = loc_createVariableFromContextVariableInfo(contextVar, {
			valueAdapter : adapter
		});
		return variable;
	};
	
	var loc_flatArray = function(valueArray, out){
		if(Array.isArray(valueArray)){
			_.each(valueArray, function(value, index){
				loc_flatArray(value, out);
			});
		}
		else{
			out.push(valueArray);
		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		_.each(loc_out.prv_elements, function(element, name){
			//clear up variable
			element.variable.release(requestInfo);
		});
		loc_out.prv_elements = {};
		
		loc_out.prv_eventObject.clearup();
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(elementInfosArray, request){
		//context elements (wrapper variables)
		loc_out.prv_elements = {};
		//object used as event object
		loc_out.prv_eventObject = node_createEventObject();
		//adapter variables by path
		loc_out.prv_adapters = {};
		
		loc_out.prv_valueChangeEventEnable = false;
		loc_out.prv_valueChangeEventSource = node_createEventObject();
		loc_out.prv_eleVariableGroup = node_createVariableGroup([], function(request){
			if(loc_out.prv_valueChangeEventEnable == true){
				loc_out.prv_valueChangeEventSource.triggerEvent(node_CONSTANT.CONTEXT_EVENT_UPDATE, undefined, request);
			}
		});
		
		//process refer to parent first
		var flatedelEmentInfosArray = [];
		loc_flatArray(elementInfosArray, flatedelEmentInfosArray);
		_.each(flatedelEmentInfosArray, function(elementInfo, key){
			loc_addContextElement(elementInfo, request);
		});
		loc_out.prv_valueChangeEventEnable = true;
	};

	var loc_addContextElement = function(elementInfo, request){
		//create empty wrapper variable for each element
		var contextEle = node_createContextElement(elementInfo, request);
		loc_out.prv_eleVariableGroup.addVariable(contextEle.variable, contextEle.path);
		if(contextEle!=undefined){
			loc_out.prv_elements[elementInfo.name] = contextEle;
			
			var eleVar = contextEle.variable;
			nosliw.logging.info("************************  Named variable creation  ************************");
			nosliw.logging.info("Name: " + contextEle.name);
			nosliw.logging.info("ID: " + eleVar.prv_id);
			nosliw.logging.info("Wrapper: " + (eleVar.prv_wrapper==undefined?"":eleVar.prv_wrapper.prv_id));
//			nosliw.logging.info("Parent: " , ((eleVar.prv_getRelativeVariableInfo()==undefined)?"":eleVar.prv_getRelativeVariableInfo().parent.prv_id));
//			nosliw.logging.info("ParentPath: " , ((eleVar.prv_getRelativeVariableInfo()==undefined)?"":eleVar.prv_getRelativeVariableInfo().path)); 
			nosliw.logging.info("***************************************************************");
			
			//get all adapters from elementInfo
			_.each(elementInfo.info.matchers, function(matchers, path){
				loc_out.prv_adapters[node_dataUtility.combinePath(elementInfo.name, path)] = loc_buildAdapterVariableFromMatchers(elementInfo.name, path, matchers, elementInfo.info.reverseMatchers[path]);
			});
		}
	};
	
	var loc_out = {
		
		getContextElement : function(name){
			return loc_getContextElementVariable(name);
		},
			
		addContextElement : function(elementInfo, request){		
			var flatedelEmentInfosArray = [];
			loc_flatArray(elementInfo, flatedelEmentInfosArray);
			_.each(flatedelEmentInfosArray, function(elementInfo, key){
				loc_addContextElement(elementInfo, request);
			});
		},	
			
		/*
		 * create context variable
		 */
		createVariable : function(contextVariableInfo, requestInfo){
			return loc_createVariableFromContextVariableInfo(contextVariableInfo, requestInfo);
		},
		
		getDataOperationRequest : function(eleName, operationService, handlers, request){
			var operationPath = operationService.parms.path;
			var baseVariable = loc_findBaseVariable(node_createContextVariableInfo(eleName, operationPath));
			if(operationPath!=undefined){
				operationService.parms.path = baseVariable.path;
			}
			return baseVariable.variable.getDataOperationRequest(operationService, handlers, request);
		},
		
		createHandleEachElementProcessor : function(name, path){
			var eleVar = loc_out.prv_elements[name].variable;
			return node_createHandleEachElementProcessor(eleVar, path);
		},
		
		getElementsName : function(){
			var out = [];
			_.each(loc_out.prv_elements, function(ele, eleName){  out.push(eleName);});
			return out;
		},

		getElementsVariable : function(){
			var out = [];
			_.each(loc_out.prv_elements, function(ele, eleName){	out.push(ele.variable);	});
			return out;
		},

		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		getUpdateContextRequest : function(values, handlers, requestInfo){
			loc_out.prv_valueChangeEventEnable = false;
			var that = this;
			var outRequest = node_createServiceRequestInfoSequence({}, handlers, requestInfo);
			var setRequest = node_createServiceRequestInfoSet({}, {
				success : function(requestInfo, result){
					loc_out.prv_valueChangeEventEnable = true;
				}
			});
			
			_.each(loc_out.getElementsName(), function(name, index){
				var value = values[name];
				if(value!=undefined){
					var dataOpRequest = node_createUIDataOperationRequest(that, new node_UIDataOperation(name, node_uiDataOperationServiceUtility.createSetOperationService("", value)));
					setRequest.addRequest(name, dataOpRequest);
				}
			});
			
			outRequest.addRequest(setRequest);
			return outRequest;
		},
		
		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_out.prv_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterValueChangeEventListener : function(listener){	return loc_out.prv_valueChangeEventSource.unregister(listener);},
		
	};

	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXT);
	
	node_getLifecycleInterface(loc_out).init(elementInfosArray, request);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariable", function(){node_createVariable = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.dataoperation.DataOperationService", function(){node_DataOperationService = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.orderedcontainer.createHandleEachElementProcessor", function(){node_createHandleEachElementProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createVariableGroup", function(){node_createVariableGroup = this.getData();});


//Register Node by Name
packageObj.createChildNode("createContext", node_createContext); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createVariableWrapper;

//*******************************************   Start Node Definition  ************************************** 	

//extended context is context + extra variables
var node_createExtendedContext = function(context, exVars){
	var loc_context = context;
	var loc_exVars = exVars;
	
	var loc_out = {
		findeVariable : function(eleName){
			var out = loc_context.getContextElement(eleName);
			if(out==undefined)  out = exVars[eleName].variable.prv_getVariable();
			return out;
		}
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_EXTENDEDCONTEXT);
	return loc_out;
};

/*
 * entity for context based variable description
 * It contains : 
 * 		name : context element name
 * 		path : path from context element
 * possible parms: 
 * 		name + path
 * 		contextVariable
 * 		string
 */
var node_createContextVariableInfo = function(n, p){
	var path = p;
	var name = n;
	if(path==undefined){
		//if second parms does not exist, then try to parse name to get path info
		if(node_getObjectType(name)==node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE){
			//if firs parm is context variable object
			path = name.path;
			name = name.name;
		}
		else{
			path="";
			var index = name.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
			if(index!=-1){
				path = name.substring(index+1);
				name = name.substring(0, index);
			}
		}
	}
	
	path = node_basicUtility.emptyStringIfUndefined(path)+"";
	
	var loc_out = {
		//context item name
		name : name,
		//path
		path : path,
		//key
		key : node_namingConvensionUtility.cascadePath(name, path),
		getFullPath : function(){
			return this.key;
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE);
	
	return loc_out;
};


/*
 * object to describe context element info, two models:
 * 		1. name + parent context + parent context contextVariable + info
 * 		2. name + data/value + path + info
 * 		3. name + parent variable + path + info
 * 		4. name + undefined + value type
 */
var node_createContextElementInfo = function(name, data1, data2, adapterInfo, info){
	var loc_out = {
		name : name,
	};
	var type = node_getObjectType(data1);
	if(type==node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXT){
		//input is context + context variable
		loc_out.context = data1;
		loc_out.contextVariable = node_createContextVariableInfo(data2);
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_EXTENDEDCONTEXT){
		//input is extended context + context variable
		var contextVarInfo = node_createContextVariableInfo(data2);
		loc_out.variable = data1.findeVariable(contextVarInfo.name);
		loc_out.path = contextVarInfo.path;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
		//input is variable
		loc_out.variable = data1;
		loc_out.path = data2;
	}
	else if(type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER){
		//input is variable wrapper
		loc_out.variable = data1.prv_getVariable();
		loc_out.path = data2;
	}
	else{
		//input is data/value
		loc_out.data1 = data1;
		loc_out.data2 = data2;
	}
	
	loc_out.info = info==undefined ? {} : info;
	loc_out.adapterInfo = adapterInfo;
	return loc_out;
};

/*
 * create real context element based on element info 
 * it contains following attribute:
 * 		name
 * 		variable
 * 		info
 */
var node_createContextElement = function(elementInfo, requestInfo){
	var loc_out = {
		name : elementInfo.name,
		info : elementInfo.info,
	};

	var adapterInfo = elementInfo.adapterInfo;
	//get variable
	if(elementInfo.context!=undefined){
		var context = elementInfo.context;
		var contextVar = elementInfo.contextVariable;
		loc_out.variable = context.createVariable(contextVar, adapterInfo, requestInfo);
		//cannot create context element variable
		if(loc_out.variable==undefined)   return;
	}
	else if(elementInfo.variable!=undefined)		loc_out.variable = node_createVariableWrapper(elementInfo.variable, elementInfo.path, adapterInfo, requestInfo);
	else		loc_out.variable = node_createVariableWrapper(elementInfo.data1, elementInfo.data2, adapterInfo, requestInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContextVariableInfo", node_createContextVariableInfo); 
packageObj.createChildNode("createContextElementInfo", node_createContextElementInfo); 
packageObj.createChildNode("createContextElement", node_createContextElement); 
packageObj.createChildNode("createExtendedContext", node_createExtendedContext); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createContextElementInfo;
var node_createContext;
var node_createContextVariableInfo;
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_uiDataOperationServiceUtility;
var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
		
	var loc_out = {
		parseContextElementName : function(name){
			var segs = name.split(node_COMMONCONSTANT.SEPERATOR_CONTEXT_CATEGARY_NAME);
			var out = {};
			if(segs.length==1){
				out.name = name;
			}
			else if(segs.length==2){
				out.categary = segs[1];
				out.name = segs[0];
			}
			return out;
		},

		getContextValueAsParmsRequest : function(context, handlers, requestInfo){
			return this.getContextEleValueAsParmsRequest(context.prv_elements, handlers, requestInfo);
		},
		
		//only context element without categary info
		getContextEleValueAsParmsRequest : function(contextItems, handlers, requestInfo){
			var outRequest = node_createServiceRequestInfoSequence({}, handlers, requestInfo);
			var setRequest = node_createServiceRequestInfoSet({}, {
				success : function(requestInfo, result){
					var out = {};
					_.each(result.getResults(), function(contextData, name){
						if(contextData!=undefined)		out[name] = contextData.value;
					});
					return out;
				}
			});
			_.each(contextItems, function(ele, eleName){
				var eleNameInfo = loc_out.parseContextElementName(eleName);
				if(eleNameInfo.categary==undefined){
					setRequest.addRequest(eleName, ele.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
				}
			});
			outRequest.addRequest(setRequest);
			return outRequest;
		},
	
		//from flat context to context group
		buildContextGroupRequest : function(contextItems, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetContextValue", {}), handlers, request);
			var calContextValue = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var value = {};
					_.each(resultSet.getResults(), function(eleValue, eleName){
						var eleNameInfo = loc_out.parseContextElementName(eleName);
						var categaryContext = value[eleNameInfo.categary];
						if(categaryContext==undefined){
							categaryContext = {};
							value[eleNameInfo.categary] = categaryContext;
						}
						categaryContext[eleNameInfo.name] = eleValue==undefined? undefined:eleValue.value;
					});
					return value;
				}
			});
	
			_.each(contextItems, function(contextItem, eleName){
				var eleNameInfo = loc_out.parseContextElementName(eleName);
				if(eleNameInfo.categary!=undefined){
					//only those with category info
					calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
				}
			});
			
			out.addRequest(calContextValue);
			return out;
		},
		
		getContextStateRequest : function(contextItems, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetContextState", {}), handlers, request);
			var calContextValue = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var state = {};
					_.each(resultSet.getResults(), function(eleData, eleName){
						if(eleData!=undefined)			state[eleName] = eleData.value;
					});
					return state;
				}
			});
	
			var validVariable = {};
			_.each(contextItems, function(contextItem, eleName){
				var variable = contextItem.variable.prv_variable;
				if(variable.prv_isBase==true){
					if(validVariable[variable.prv_id]==undefined){
						validVariable[variable.prv_id] = variable;
						//only base element
						calContextValue.addRequest(eleName, contextItem.variable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService()));
					}
				}
			});
			
			out.addRequest(calContextValue);
			return out;
		},

		//build context according to context definition and parent context
		buildContext : function(contextDef, parentContext, requestInfo){
			//build context element first
			var contextElementInfosArray = [];
			
			_.each(contextDef, function(contextDefRootObj, eleName){
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				
				var info = {
					matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
					reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && 
							contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_LOGICAL){
						//physical relative
						if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT){
	//					if(contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==true){
							//process relative that  refer to element in parent context
							var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
							var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
							var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
							contextElementInfosArray.push(node_createContextElementInfo(eleName, parentContext, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
					else{
						//not relative or logical relative variable
						var defaultValue = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
						
						var criteria;
						if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE)	criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_DEFINITION][node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA];
						else  criteria = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_CRITERIA]; 
						if(criteria!=undefined){
							//app data, if no default, empty variable with wrapper type
							if(defaultValue!=undefined) 	contextElementInfosArray.push(node_createContextElementInfo(eleName, node_dataUtility.createDataOfAppData(defaultValue), "", undefined, info));
							else  contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_APPDATA, undefined, info));
						}
						else{
							//object, if no default, empty variable with wrapper type
							if(defaultValue!=undefined)		contextElementInfosArray.push(node_createContextElementInfo(eleName, defaultValue, "", undefined, info));
							else contextElementInfosArray.push(node_createContextElementInfo(eleName, undefined, node_CONSTANT.DATA_TYPE_OBJECT, undefined, info));
						}
					}
				}
			});	
				
			var context = node_createContext(contextElementInfosArray, requestInfo);
	
			//for relative which refer to context ele in same context
			_.each(contextDef, function(contextDefRootObj, eleName){
				var contextDefRootEle = contextDefRootObj[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFINITION];
				var info = {
						matchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_MATCHERS],
						reverseMatchers : contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_REVERSEMATCHERS]
				};
				var type = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_TYPE];
				var contextInfo = contextDefRootObj[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
				//if context.info.instantiate===manual, context does not need to create in the framework
				if(contextInfo[node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE]!=node_COMMONCONSTANT.UIRESOURCE_CONTEXTINFO_INSTANTIATE_MANUAL){
					if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PARENT]==node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_SELF){
	//				if(type==node_COMMONCONSTANT.CONTEXT_ELEMENTTYPE_RELATIVE && contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_ISTOPARENT]==false){
						var pathObj = contextDefRootEle[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONELEMENT_PATH];
						var rootName = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_ROOTNAME];
						var path = pathObj[node_COMMONATRIBUTECONSTANT.CONTEXTPATH_PATH];
						//only process element that parent is created
						if(context.getContextElement(rootName)!=undefined){
							context.addContextElement(node_createContextElementInfo(eleName, context, node_createContextVariableInfo(rootName, path), undefined, info));
						}
					}
				}
			});	
			
			return context;
		},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_createRequestEventGroupHandler;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * this is a factory to create variables group
 * this group contains context variables: context name + path
 * this object help to build event response for context variable
 * 		variables : a group of context variables
 * 		handler : function(contextVariableEventInfo) handle all context variables events
 * 		thisContext : the this context for event handler
 */

var node_createContextVariablesGroup = function(context, contextVariableInfosArray, handler, thisContext){

	//context
	var loc_context = context;
	//event handler
	var loc_handler = handler;
	//variables
	var loc_variables = {};
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(contextVariableInfo){
		var variable = loc_context.createVariable(contextVariableInfo);
		loc_variables[contextVariableInfo.getFullPath()] = variable;
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), contextVariableInfo.key);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(context, contextVariableInfosArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in contextVariableInfosArray){
			loc_addElement(contextVariableInfosArray[i]);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		
		_.each(loc_variables, function(variable, key){
			variable.release(requestInfo);
		});
		
		loc_context = undefined;
		loc_handler = undefined;
	};

	var loc_out = {
		/*
		 * add 
		 */
		addVariable : function(contextVariable){	loc_addElement(contextVariable);		},
		
		getVariable : function(fullPath){	return loc_variables[fullPath];		},
		
		getVariables : function(){  return loc_variables;  },
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(context, contextVariableInfosArray, handler, thisContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContextVariablesGroup", node_createContextVariablesGroup); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("data.entity");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
var node_CONSTANT;	
//*******************************************   Start Node Definition  ************************************** 	
/*
 * data is a combination of value + dataType
 */
var node_createData = function(value, dataTypeInfo){
	var loc_out = {
		value : value,
		dataTypeInfo : dataTypeInfo,
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATA);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("createData", node_createData); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_appDataWrapperUtility;	
var node_createServiceRequestInfoSequence;
var node_ServiceInfo;
var node_uiDataOperationServiceUtility;
var node_requestServiceProcessor;
var node_OperationParm;
var node_parseSegment;
var node_parsePathSegment;
var node_createServiceRequestInfoSimple;
var node_createServiceRequestInfoSet;
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataTypeHelperData = function(){
	
	var loc_getDirectChildValueRequest = function(parentValue, path, handlers, request){
		var operationParms = [];
		operationParms.push(new node_OperationParm(parentValue, "base"));
		operationParms.push(new node_OperationParm({
			dataTypeId: "test.string;1.0.0",
			value : path
		}, "name"));

		return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
				parentValue.dataTypeId, 
				node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
				operationParms, handlers, request);
	}; 

	var loc_getCurrentSegmentChildValueRequest = function(parentValue, segs, handlers, request){
		return loc_getDirectChildValueRequest(parentValue, segs.next(), handlers, request);			
	};
	
	var loc_getSegmentsChildValueRequest = function(parentValue, segs, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetSegsChildValue", {"parent":parentValue, "segs":segs}), handlers, request);
		if(segs.hasNext()){
			out.addRequest(loc_getCurrentSegmentChildValueRequest(parentValue, segs, {
				success : function(request, segChildValue){
					return loc_getSegmentsChildValueRequest(segChildValue, segs);
				}
			}));
		}
		else{
			//end of segments
			out.addRequest(node_createServiceRequestInfoSimple({}, function(request){
				return parentValue;
			})); 
		}
		return out;
	};
	
	var loc_getOperationBaseRequest = function(value, path, first, lastReverse, handlers, request){
		var segs = node_parsePathSegment(path, first, lastReverse);
		var out = loc_getSegmentsChildValueRequest(value, segs, handlers, request);
		out.setRequestProcessors({
			success : function(request, value){
				return {
					base : value,
					attribute : segs.getRestPath()
				}
			}
		});
		return out;
	};
	
	
	var loc_out = {		
		
			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, request){
				var pathSegs = node_parsePathSegment(path);
				var out = loc_getSegmentsChildValueRequest(parentValue, pathSegs, handlers, request);
				out.setRequestProcessors({
					success : function(request, childValue){
						return node_dataUtility.cloneValue(childValue);
					}
				});
				return out;
			},
			
			getDataOperationRequest : function(value, dataOperationService, handlers, request){
				var command = dataOperationService.command;
				var operationData = dataOperationService.parms;

				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DataOperation", {"command":command, "operationData":operationData}), handlers, request);

				if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
					out.addRequest(loc_getOperationBaseRequest(value, operationData.path, undefined, 1, {
						success : function(requestInfo, operationBase){
							var operationParms = [];
							operationParms.push(new node_OperationParm(operationBase.base, "base"));
							operationParms.push(new node_OperationParm({
								dataTypeId: "test.string;1.0.0",
								value : operationBase.path
							}, "name"));
							operationParms.push(new node_OperationParm(operationData.value, "value"));

							return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
									operationBase.base.dataTypeId, 
									node_COMMONCONSTANT.DATAOPERATION_COMPLEX_SETCHILDDATA, 
									operationParms);
						}
					}));
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					out.addRequest(loc_getOperationBaseRequest(value, operationData.path, undefined, 0, {
						success : function(requestInfo, operationBase){
							var operationParms = [];
							operationParms.push(new node_OperationParm(value, "base"));
							out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
								value.dataTypeId, 
								node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ISACCESSCHILDBYID, 
								operationParms, {
									success : function(request, isAccessChildById){
										if(isAccessChildById.value){
											//througth id
											
										}
										else{
											//throught index,
											var operationParms = [];
											operationParms.push(new node_OperationParm(operationBase.base, "base"));
											operationParms.push(new node_OperationParm({
												dataTypeId: "test.integer;1.0.0",
												value : operationData.index
											}, "index"));
											operationParms.push(new node_OperationParm(operationData.value, "child"));
											out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
												value.dataTypeId, 
												node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ADDCHILD, 
												operationParms, {
													success : function(request, childValue){
														return value;
													}
												}
											));
										}										
									}
								}
							));
						}
					}));
					
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETE){
					
				}
				
				return out;
			},

			getGetElementsRequest : function(value, handlers, request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetElements", {"value":value}), handlers, request); 
				
				var operationParms = [];
				operationParms.push(new node_OperationParm(value, "base"));
				out.addRequest(nosliw.runtime.getExpressionService().getExecuteOperationRequest(
					value.dataTypeId, 
					node_COMMONCONSTANT.DATAOPERATION_COMPLEX_ISACCESSCHILDBYID, 
					operationParms, {
						success : function(request, isAccessChildById){
							if(isAccessChildById.value){
								//througth id
								
							}
							else{
								//throught index,
								//get length first
								var operationParms = [];
								operationParms.push(new node_OperationParm(value, "base"));
								return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
									value.dataTypeId, 
									node_COMMONCONSTANT.DATAOPERATION_COMPLEX_LENGTH, 
									operationParms, {
										success : function(request, arrayValueLength){
											var allElesRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("", {}), {
												success : function(request, setResult){
													var elements = [];
													for(var i=0; i<arrayValueLength.value; i++){
														var eleValue = setResult.getResult(i+"");
														elements.push({
															value : eleValue
														});
													}
													return elements;
												}
											}); 

											for(var i=0; i<arrayValueLength.value; i++){
												var operationParms = [];
												operationParms.push(new node_OperationParm(value, "base"));
												operationParms.push(new node_OperationParm({
													dataTypeId: "test.integer;1.0.0",
													value : i,
												}, "index"));
												allElesRequest.addRequest(i+"", nosliw.runtime.getExpressionService().getExecuteOperationRequest(
													value.dataTypeId, 
													node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATABYINDEX, 
													operationParms));
											}
											return allElesRequest;
										}
								});
							}
						}
					})
				);
				return out;
			}, 
			
			//clean up resource in value
			destroyValue : function(value){},
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_APPDATA;		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.appdata.utility", function(){node_appDataWrapperUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parsePathSegment", function(){node_parsePathSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_APPDATA], node_createDataTypeHelperData());
});

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper.dynamic");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_parseSegment;
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataTypeHelperDynamic = function(){
	
	var loc_out = {		

			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, requester_parent){
				return node_createServiceRequestInfoSimple({}, function(){
					return parentValue;
				}, handlers, requester_parent);
			},
			
			//do operation on value
			getDataOperationRequest : function(value, dataOperationService, handlers, requester_parent){
			},
			
			//loop through elements under value
			getGetElementsRequest : function(value, handlers, request){
			}, 
	
			destroyValue : function(value){},
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_DYNAMIC;		},
	
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});


nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_DYNAMIC], node_createDataTypeHelperDynamic());
});

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_parseSegment;
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataTypeHelperObject = function(){
	
	/*
	 * get attribute value according to the path
	 */
	var loc_getObjectAttributeByPath = function(obj, prop) {
		if(obj==undefined)  return;
		if(prop==undefined || prop=='')  return obj;
		
	    var parts = prop.split('.'),
	        last = parts.pop(),
	        l = parts.length,
	        i = 1,
	        current = parts[0];

	    if(current==undefined)  return obj[last];
	    
	    while((obj = obj[current]) && i < l) {
	        current = parts[i];
	        i++;
	    }

	    if(obj) {
	        return obj[last];
	    }
	};

	
	var loc_getOperationBase = function(value, path){
		var baseObj = value;
		var attribute = path;
		
		if(node_basicUtility.isStringEmpty(path)){
			baseObj = value;
		}
		else if(path.indexOf('.')==-1){
			baseObj = value;
			attribute = path;
		}
		else{
			var segs = node_parseSegment(path);
			var size = segs.getSegmentSize();
			for(var i=0; i<size-1; i++){
				var attr = segs.next();
				var obj = baseObj[attr];
				if(obj==undefined){
					obj = {};
					baseObj[attr] = obj; 
				}
				baseObj = obj;
			}
			attribute = segs.next();
		}
		return {
			base : baseObj, 
			attribute : attribute
		}
	}
	
	var loc_out = {		

			//get child value by path
			getChildValueRequest : function(parentValue, path, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetChildValueFromObjectValue", {"parent":parentValue, "path":path}), function(requestInfo){
					var out = loc_getObjectAttributeByPath(parentValue, path);
					return node_dataUtility.cloneValue(out);
				}, handlers, requester_parent);
			},
			
			//do operation on value
			getDataOperationRequest : function(value, dataOperationService, handlers, requester_parent){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetDataOperationFromObjectValue", {"value":value, "dataOperationService":dataOperationService}), function(requestInfo){
					var command = dataOperationService.command;
					var operationData = dataOperationService.parms;
					
					//get operation base
					var operationBase = loc_getOperationBase(value, operationData.path);
					
					var out = value;
					if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
						if(_.isObject(value) || _.isArray(value)){
							//for array or object
							if(node_basicUtility.isStringEmpty(operationBase.attribute))	out = operationData.value;
							else	operationBase.base[operationBase.attribute] = operationData.value;
						}
						else{
							//for basic value
							out = operationData.value;
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
						if(_.isArray(operationBase.base) || _.isObject(operationBase.base)){
							//get container object to add element to it
							var containerObj;
							if(node_basicUtility.isStringEmpty(operationBase.attribute))	containerObj = operationBase.base; 
							else{
								containerObj = operationBase.base[operationBase.attribute];
								if(containerObj==undefined){
									//if container does not exist, then create it
									if(operationData.id!=undefined)		operationBase.base[operationBase.attribute] = {};
									else		operationBase.base[operationBase.attribute] = [];
								}
								containerObj = operationBase.base[operationBase.attribute];
							}
							
							if(operationData.index!=undefined){
								if(_.isArray(containerObj))		containerObj.splice(operationData.index, 0, operationData.value);
								else if(_.isObject(containerObj)) containerObj[operationData.id]=operationData.value;
							}
							else{
								//if index is not specified, for array, just append it
								if(_.isArray(containerObj))		containerObj.push(operationData.value);
								else if(_.isObject(containerObj)) containerObj[operationData.id]=operationData.value;
							}
						}
						else{
							//not valid operation 
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
						var containerObj;
						if(node_basicUtility.isStringEmpty(operationBase.attribute))	containerObj = operationBase.base; 
						else	containerObj = operationBase.base[operationBase.attribute];

						if(containerObj!=undefined){
							if(_.isArray(containerObj))		containerObj.splice(operationData.index, 1);
							else if(_.isObject(containerObj)) delete containerObj[operationData.id];
						}
						else{
							//not valid operation 
						}
					}
					else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETE){
						if(_.isArray(operationBase.base)){
							operationBase.base.splice(parseInt(operationBase.attribute), 1);
						}
						else if(_.isObject(operationBase.base)){
							delete operationBase.base[operationBase.attribute];
						}
					}
					return out;
				
				}, handlers, requester_parent);
			},
			
			//loop through elements under value
			getGetElementsRequest : function(value, handlers, request){
				return node_createServiceRequestInfoSimple(new node_ServiceInfo("GetElements", {"value":value}), function(requestInfo){
					var elements = [];
					if(_.isArray(value)){
						//for array
						_.each(value, function(eleValue, index){
							elements.push({
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					}
					else if(_.isObject(value)){
						//for object
						_.each(value, function(eleValue, name){
							elements.push({
								id : name,
								value : node_dataUtility.cloneValue(eleValue)
							});
						}, this);
					} 
					return elements;
				}, handlers, request);
			}, 
			
			//clean up resource in value
			destroyValue : function(value){},
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_OBJECT;		},
	
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});


nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerDataTypeHelper([node_CONSTANT.DATA_TYPE_OBJECT], node_createDataTypeHelperObject());
});

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("data");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_makeObjectWithType;
var node_CONSTANT;	
var node_createData;
var node_namingConvensionUtility;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_utility = function(){
	
	return {

		isCyclic : function(original) {
			  var seenObjects = [];
			  var seenPaths = [];

			  function detect (obj, path) {
			    if (obj && typeof obj === 'object') {
			    	var index = seenObjects.indexOf(obj);
			      if ( index!== -1 && path.startsWith(seenPaths[index])) {
			        return true;
			      }
			      seenObjects.push(obj);
			      seenPaths.push(path);
			      for (var key in obj) {
			        if (obj.hasOwnProperty(key) && detect(obj[key], path+"."+key)) {
			        	console.log(original);
			        	console.log('cycle at ' + path+"."+key);
			        	console.log('cycle at ' + seenPaths[seenObjects.indexOf(obj[key])]);
			        	
			        	
			          return true;
			        }
			      }
			    }
			    return false;
			  }

			  return detect(original, "");
		},
		
		cloneValue : function(value){
			if(value==undefined)  return undefined;
			try{
				var copy = JSON.parse(node_basicUtility.stringify(value));
			}
			catch(e){
				this.isCyclic(value);   //kkkk
				copy = value;
			}
			return copy;
		},
		
		/**
		 * compare two path
		 * if path1 equals path2, then 0
		 * if path1 contains path2, then 1
		 * if path2 contains path1, then 2
		 * otherwise, -1
		 */
		comparePath : function(path1, path2){
			var compare = 0;
			var result = "";
			var p1 = node_basicUtility.emptyStringIfUndefined(path1)+"";
			var p2 = node_basicUtility.emptyStringIfUndefined(path2)+"";
			if(p1==p2){
				compare = 0;
				result = p1; 
			}
			else if(node_basicUtility.isStringEmpty(p1)){
				compare = 2;
				result = p2;
			}
			else if(node_basicUtility.isStringEmpty(p2)){
				compare = 1;
				result = p1;
			}
			else if(p1.startsWith(p2+".")){
				compare = 1;
				result = p1.substring((p2+".").length);
			}
			else if(p2.startsWith(p1+".")){
				compare = 2;
				result = p2.substring((p1+".").length);
			}
			else{
				compare = -1;
				result = undefined;
			}
			return {
				compare : compare,
				subPath : result
			};
		},
		
		combinePath : function(){
			var out = "";
			_.each(arguments, function(seg, index){
				out = node_namingConvensionUtility.cascadePath(out, seg);
			});
			return out;
		},
		
		createDataOfAppData : function(appData){
			var out = node_createData(appData, node_CONSTANT.DATA_TYPE_APPDATA);
			return out;
		},
		
		createDataOfObject : function(obj){
			var out = node_createData(obj, node_CONSTANT.DATA_TYPE_OBJECT);
			return out;
		},
		
		createDataOfDynamic : function(obj){
			var out = node_createData(obj, node_CONSTANT.DATA_TYPE_DYNAMIC);
			return out;
		},
	
		/*
		 * create object data by value
		 * if object is already data, then do nothing
		 * otherwise, wraper it to data 
		 */
		createDataByObject : function(object, dataTypeInfo){
			var out = object;
			if(node_getObjectType(object)!=node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				out = this.createDataByValue(object, dataTypeInfo);
			}
			return out;
		},
		
		/*
		 * create object data by value 
		 */
		createDataByValue : function(value, dataTypeInfo){
			var out;
			if(dataTypeInfo!=undefined){
				out = node_createData(value, dataTypeInfo);
			}
			else{
				out = node_createData(value, node_CONSTANT.DATA_TYPE_OBJECT);
			}
			return out;
		},
		
		createEmptyData : function(){
			return node_createData("");
		},
		
		isEmptyData : function(data){
			if(data==undefined)  return true;
			if(data.dataTypeInfo==undefined)  return true;
			return false;
		},
		
		getValueOfData : function(data){
			var dataTypeInfo = data.dataTypeInfo;
			var out = data.value;
			if(dataTypeInfo==node_CONSTANT.DATA_TYPE_DYNAMIC){
				out = out.getValue();
			}
			return out;
		}
		
	};	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
//*******************************************   Start Node Definition  ************************************** 	

//entity to describe relative variable : parent + path to parent
var node_RelativeEntityInfo = function(parent, path){
	this.parent = parent;
	this.path = node_basicUtility.emptyStringIfUndefined(path);
	return this;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("RelativeEntityInfo", node_RelativeEntityInfo); 

})(packageObj);
	nosliw.registerNodeEvent("runtime1", "active", function(eventName, nodeName){

		var node_buildVariableTree = nosliw.getNodeData("uidata.test.buildVariableTree");
		var node_buildContext = nosliw.getNodeData("uidata.test.buildContext");
		var node_createContextVariableInfosGroup = nosliw.getNodeData("uidata.context.createContextVariablesGroup");
		var node_createContextVariableInfo = nosliw.getNodeData("uidata.context.createContextVariableInfo");
		
		  var objectData1 = {
				 string : "string value",
				 int : 12345,
				 boolean : true,
				 object : {
					 string : "2 string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 string : "6 string value",
						 int : 12345,
						 boolean : true,
					 },
					 array : [
						 {
							 string : "4 string value",
							 int : 12345,
							 boolean : true,
						 }, 
						 "3 string value",
						 12345,
						 true,
						 ["2 1", "2 2", "2 3", "2 4"]
					],
				 },
				 array : ["1", "2", "3", "4"],
		  };

		  
		  var objectData2 = {
			   child : {
				     string : "string value",
					 int : 12345,
					 boolean : true,
					 object : {
						 string : "2 string value new wrapper",
						 int : 12345,
						 boolean : true,
						 object : {
							 string : "6 string value",
							 int : 12345,
							 boolean : true,
						 },
						 array : [
							 {
								 string : "4 string value",
								 int : 12345,
								 boolean : true,
							 }, 
							 "3 string value",
							 12345,
							 true,
							 ["2 1", "2 2", "2 3", "2 4"]
						],
					 },
					 array : ["1", "2", "3", "4"],
			   }
			};

			 var contextDefinition = [
				 ["context1", ["root1", objectData1, "object", true]],
				 ["context2", ["root2", "hello world", "", true]],
				 ["context3", ["root3", "context1", "root1.object"]],
			];
		  
		 var contexts = node_buildContext(contextDefinition);
			
		 
		 var variablesDefinition = [
			 ["leaf1", "context1", "root1", "string"],
			 ["leaf2", "context1", "root1", "array.0"],
			 ["leaf5", "context1", "root1", "object"],
			 ["leaf7", "context1", "root1", "object.string"],
			 ["leaf8", "context1", "root1", "array"],
			 ["leaf9", "context1", "root1", "object"],
			 ["leaf10", "context1", "root1", "array.0.string"],
		];
		 
		 var variablesTree = node_buildVariableTree(variablesDefinition, contexts);
		 variablesTree.printVariable("leaf1");
		 variablesTree.printVariable("leaf2");
		 variablesTree.printVariable("leaf5");
		 variablesTree.printVariable("leaf7");
		 variablesTree.printVariable("leaf8");
		 variablesTree.printVariable("leaf9");

		 var contextVars = [];
		 contextVars.push(node_createContextVariableInfo("root1", "array"));
		 node_createContextVariableInfosGroup(contexts.getContext("context1"), contextVars, function(event){
			  nosliw.logging.debug("Variable Group Event : ");
		 });
		 
		 
		 variablesTree.dataOperate(["leaf8", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"0.string", data:"new data"}]);
		 variablesTree.printVariable("leaf2");
		 variablesTree.printVariable("leaf8");
		 variablesTree.printVariable("leaf10");
		 
		 
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
//		 
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data11111"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
//
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"array", data:"new data", index: 7}]);
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"array", data:"new data"}]);
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"object", data:"new data", index: "new"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf9");
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, {path:"object", index: "int"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf9");
//
//		 variablesTree.dataOperate(["root1", node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, {path:"object", data:"new data", index: "new"}]);
//
//		 
//		 variablesTree.dataOperate(["leaf21", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"", data:"new data11111"}]);
//		 variablesTree.printVariable("root2");
//		 variablesTree.printVariable("leaf21");
		 
//		 var newWrapper = variablesTree.getVariable("leaf31").getWrapper();
//		 variablesTree.getVariable("root1").setWrapper(newWrapper);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf1");
//
//		 variablesTree.dataOperate(["leaf5", node_CONSTANT.WRAPPER_OPERATION_SET, {path:"string", data:"new data"}]);
//		 variablesTree.printVariable("root1");
//		 variablesTree.printVariable("leaf6");
//		 variablesTree.printVariable("leaf7");
		 
	});
//get/create package
var packageObj = library.getChildPackage("test");    

(function(packageObj){
//get used node
var node_wrapperFactory;
var node_createContextElementInfo;
var node_createContext;
var node_createContextVariableInfo;
var node_ServiceInfo;
var node_CONSTANT;
var node_createEventObject;
var node_createWrapperVariable;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_buildContext = function(contextsDefinition){
	
	var loc_contexts = {};
	var loc_eventObject = node_createEventObject();

	_.each(contextsDefinition, function(contextDefintion){
		var contextName = contextDefintion[0];
		var elementInfosArray = [];
		for(var i=1; i<contextDefintion.length; i++){
			var contextElementInfo;
			var contextEleDefinition = contextDefintion[i];
			var contextElementName = contextEleDefinition[0];
			if(contextEleDefinition[3]!=undefined){
				contextElementInfo = node_createContextElementInfo(contextElementName, contextEleDefinition[1], contextEleDefinition[2]);
			}
			else{
				contextElementInfo = node_createContextElementInfo(contextElementName, loc_contexts[contextEleDefinition[1]], contextEleDefinition[2]);
			}
			elementInfosArray.push(contextElementInfo);
		}
		var context = node_createContext(elementInfosArray);
		loc_contexts[contextName] = context;
		
		context.registerContextListener(loc_eventObject, function(event, context, requestInfo){
			  nosliw.logging.debug("Context Event : ", contextName, event);
		}, this);
	});
	
	var loc_out = {
		
		getContext : function(name){  return loc_contexts[name];  },
		
		
		
	};
	
	return loc_out;
};

var node_buildVariableTree = function(variablesDefinition, contexts){
	 var loc_contexts = contexts; 
	 var loc_variables = {};
	 var loc_eventObject = node_createEventObject();
	 _.each(variablesDefinition, function(variableDef, index){
		 var variableName = variableDef[0];
		 var contextName = variableDef[1];
		 var contextEleName = variableDef[2];
		 var path = variableDef[3];
		 
		 var context = loc_contexts.getContext(contextName);
		 var contextVariable = node_createContextVariableInfo(contextEleName, path);
		 var variable = context.createVariable(contextVariable);
		 loc_variables[variableName] = variable;
		 
		 //listen to event from variable
		 variable.registerDataChangeEventListener(loc_eventObject, function(event, path, operationValue, requestInfo){
			  nosliw.logging.debug("Variable Event Data Operation : ", name, event, path, JSON.stringify(operationValue));
		 }, name);

		 variable.registerLifecycleEventListener(loc_eventObject, function(event, data, requestInfo){
			  nosliw.logging.debug("Variable Event Lifecycle : ", name, event, JSON.stringify(data));
		 }, name);
		 
		 nosliw.logging.debug("Variable created: ", name, JSON.stringify(variable.getValue()));
	 });
	 
	 var out = {
			
		getContext : function(name){  return loc_contexts[name];  },
		
		getValue : function(name){
			return this.getVariable(name).getValue();
		},
	 
		getVariable : function(name){
			return loc_variables[name];
		},
		
		getWrapper : function(name){
			this.getVariable(name).getWrapper();
		},
		
		dataOperate : function(operationDef){
			var variable = loc_variables[operationDef[0]];
			var operation = operationDef[1];
			var opValue = operationDef[2];
		    variable.requestDataOperation(new node_ServiceInfo(operation, opValue));
		},

		dataOperates : function(operationsDef){
			for(var i in operationsDef){
				this.dataOperate(operationsDef[i]);
			}
		},
		
		printVariable : function(name){
			 nosliw.logging.debug("Variable value: ", name, JSON.stringify(this.getValue(name)));
		}
	 };
	 
	 return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createWrapperVariable", function(){node_createWrapperVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("buildVariableTree", node_buildVariableTree); 
packageObj.createChildNode("buildContext", node_buildContext); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("uidataoperation");    

(function(packageObj){
//get used node
var node_CONSTANT;	
var node_getObjectType;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_namingConvensionUtility;
var node_dataUtility;
var node_createContextVariableInfo;
//*******************************************   Start Node Definition  ************************************** 	

//create request for data operation
var node_createUIDataOperationRequest = function(context, uiDataOperation, handlers, requester_parent){
	var target = uiDataOperation.target;
	var targetType = node_getObjectType(target);
	var operationService = uiDataOperation.operationService;
	var request;
	switch(targetType)
	{
	case node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER:
		request = target.getDataOperationRequest(operationService, handlers, requester_parent);
		break;
	case node_CONSTANT.TYPEDOBJECT_TYPE_CONTEXTVARIABLE:
		operationService.parms.path = node_dataUtility.combinePath(target.path, operationService.parms.path);
		request = context.getDataOperationRequest(target.name, operationService, handlers, requester_parent);
		break;
	default : 
		//target is context element name
		var targeContextVar = node_createContextVariableInfo(target);
		operationService.parms.path = node_dataUtility.combinePath(targeContextVar.path, operationService.parms.path);
		request = context.getDataOperationRequest(targeContextVar.name, operationService, handlers, requester_parent);
	}
	return request;
};

/**
 * Request for batch of data operation
 * It contains a set of data operation service, so that this request is a batch of data operation as a whole
 * 
 */
var node_createBatchUIDataOperationRequest = function(context, handlers, requester_parent){

	//all the child requests service  
	var loc_uiDataOperations = [];
	
	//data context
	var loc_context = context;
	
	var loc_index = 0;
	
	var loc_out = node_createServiceRequestInfoSet(new node_ServiceInfo("BatchUIDataOperation", {}), handlers, requester_parent)
	
	loc_out.addUIDataOperation = function(uiDataOperation){
		this.addRequest(loc_index+"", node_createUIDataOperationRequest(loc_context, uiDataOperation));
		loc_index++;

		//for debugging purpose
		loc_uiDataOperations.push(uiDataOperation);
	};
	return loc_out;
};

//operate on targe
//   target : variable, wrapper, context variable
//   operationService : service for operation
var node_UIDataOperation = function(target, operationService){
	this.target = target;
	this.operationService = operationService;
};


//utility method to build data operation service
var node_uiDataOperationServiceUtility = function(){

	var loc_createServiceInfo = function(command, parms){
		var out = new node_ServiceInfo(command, parms);
		out.clone = function(){
			return loc_createServiceInfo(out.command, out.parms.clone());
		}
		return out;
	};
	
	var loc_out = {
			createGetOperationData : function(path){
				return {
					path : path,
					clone : function(){
						return loc_out.createGetOperationData(this.path);
					}
				};
			},
			
			createGetOperationService : function(path){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_GET, this.createGetOperationData(path));
			},
			
			createSetOperationData : function(path, value, dataType){
				return {
					path : path,
					value : value,
					dataType : dataType,
					clone : function(){
						return loc_out.createSetOperationData(this.path, node_dataUtility.cloneValue(this.value), dataType);
					}
				};
			},
			
			createSetOperationService : function(path, value, dataType){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_SET, this.createSetOperationData(path, value, dataType));
			},

			//index or id or both
			createAddElementOperationData : function(path, value, index, id){
				return {
					path : path,              
					index : index,            	//index in array, mandatory
					id : id,					//unchanged path from parent, if not provided, then will generate unique one
					value : value,
					clone : function(){
						return loc_out.createAddElementOperationData(this.path, node_dataUtility.cloneValue(this.value), this.index, this.id);
					}
				};
			},

			createAddElementOperationService : function(path, value, index, id){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, this.createAddElementOperationData(path, value, index, id));
			},

			//index or id, cannot both
			createDeleteElementOperationData : function(path, index, id){
				return {
					path : path,
					index : index,
					id : id,
					clone : function(){
						return loc_out.createDeleteElementOperationData(this.path, this.index, this.id);
					}
				};
			},
			
			createDeleteElementOperationService : function(path, index, id){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, this.createDeleteElementOperationData(path, index, id));
			},

			createDeleteOperationData : function(path){
				return {
					path : path,
					clone : function(){
						return loc_out.createDeleteOperationData(this.path);
					}
				};
			},
			
			createDeleteOperationService : function(path){
				return loc_createServiceInfo(node_CONSTANT.WRAPPER_OPERATION_DELETE, this.createDeleteOperationData(path));
			}
	};
	
	return loc_out;
}();


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIDataOperationRequest", node_createUIDataOperationRequest); 
packageObj.createChildNode("createBatchUIDataOperationRequest", node_createBatchUIDataOperationRequest); 
packageObj.createChildNode("UIDataOperation", node_UIDataOperation); 
packageObj.createChildNode("uiDataOperationServiceUtility", node_uiDataOperationServiceUtility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_ServiceInfo;
var node_CONSTANT;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_eventUtility;
var node_requestUtility;
var node_wrapperFactory;
var node_basicUtility;
var node_createEventObject;
var node_RelativeEntityInfo;
var node_variableUtility;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSet;
var node_OrderedContainerElementInfo;
var node_createVariableWrapper;
var node_createOrderedContainersInfo;
var node_createOrderVariableContainer;
var node_uiDataOperationServiceUtility;
var node_dataUtility;
var node_requestServiceProcessor;
var node_createServiceRequestInfoSimple;

//*******************************************   Start Node Definition  **************************************

/**
 * new variable
 * input model : 
 *		1. parent variable + path from parent
 *      2. data + undefined
 *      3. value + value type
 */
var node_newVariable = function(data1, data2, adapterInfo, requestInfo){
	//relative to parent : parent + path
	var loc_relativeVariableInfo;
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo, requestInfo){
		//whether this variable is live or destroyed
		loc_out.prv_isLive = true;

		//every variable has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();
		
		//adapter that will apply to value of wrapper
		loc_out.prv_valueAdapter = adapterInfo==undefined ? undefined : adapterInfo.valueAdapter;

		loc_out.prv_pathAdapter = adapterInfo==undefined ? undefined : adapterInfo.pathAdapter;
		
		//adapter that affect the event from wrapper and listener of this variable
		loc_out.prv_eventAdapter = adapterInfo==undefined ? undefined : adapterInfo.eventAdapter;

		loc_out.prv_destoryAdapter = adapterInfo==undefined ? undefined : adapterInfo.destroyAdapter; 
		
		//adapter that affect the data operation on wrapper, what is this for kkkkkk
		loc_out.prv_dataOperationRequestAdapter = adapterInfo==undefined ? undefined : adapterInfo.dataOperationRequestAdapter;;
		
		//lifecycle event object 
		loc_out.prv_lifecycleEventObject = node_createEventObject();
		//data operation event object
		loc_out.prv_dataOperationEventObject = node_createEventObject();
		//data change event object, it means child or itself changed
		loc_out.prv_dataChangeEventObject = node_createEventObject();

		//wrapper object
		loc_out.prv_wrapper = undefined;
		
		loc_out.prv_isBase = true;
		//wrapper type incase no wrapper object
		loc_out.prv_wrapperType;
		
		//normal child variables by path
		loc_out.prv_childrenVariable = {};
		
		//record how many usage of this variable.
		//when usage go to 0, that means it should be clean up
		loc_out.prv_usage = 0;
		
		var data1Type = node_getObjectType(data1);
		if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
			//for variable having parent variable
			loc_out.prv_isBase = false;
			loc_relativeVariableInfo = new node_RelativeEntityInfo(data1, data2);
			
			//add current variable as child of data1 variable
			data1.prv_addChildVariable(loc_out, data2, adapterInfo);
			
			//build wrapper relationship with parent
			loc_out.prv_updateWrapperInRelativeVariable();
		}
		else{
			//for base object/data
			loc_out.prv_isBase = true;
			if(data1!=undefined){
				//create wrapper
				loc_setWrapper(node_wrapperFactory.createWrapper(data1, data2, requestInfo), requestInfo);
				//store wrapper type
				loc_out.prv_wrapperType = loc_out.prv_wrapper.getDataType();
			}
			else	loc_out.prv_wrapperType = data2;		//not create wrapper, just store the value type
		}
		
		nosliw.logging.info("************************  variable created   ************************");
		nosliw.logging.info("ID: " + loc_out.prv_id);
		if(loc_relativeVariableInfo!=undefined){
			nosliw.logging.info("Parent: " + loc_relativeVariableInfo.parent.prv_id);
			nosliw.logging.info("Parent Path: " + loc_relativeVariableInfo.path);
		}
		nosliw.logging.info("***************************************************************");
	};
	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){loc_destroy();};
	
	//destroy variable and trigue lifecycle event 
	var loc_destroy = function(requestInfo){
		if(loc_out.prv_isLive){
			nosliw.logging.info("************************  variable destroying   ************************");
			nosliw.logging.info("ID: " + loc_out.prv_id);
			nosliw.logging.info("***************************************************************");
			
			//trigger lifecycle event
			loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE, {}, requestInfo);

			loc_out.prv_isLive = false;
			
			//clean up event object
			loc_out.prv_dataOperationEventObject.clearup();
			loc_out.prv_dataChangeEventObject.clearup();
	
			//clean up adapter
			if(loc_out.prv_destoryAdapter!=undefined)   loc_out.prv_destoryAdapter();
	
			//clear children variable first
			_.each(loc_out.prv_childrenVariable, function(childVarInfo, path){		childVarInfo.variable.destroy(requestInfo);	});
			loc_out.prv_childrenVariable = {};
			
			//destroy wrapper 
			loc_destroyWrapper(requestInfo);
	
			//trigger lifecycle event
			loc_out.prv_lifecycleEventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_AFTER, {}, requestInfo);
			loc_out.prv_lifecycleEventObject.clearup();
			for (var key in loc_out){
			    if (loc_out.hasOwnProperty(key)){
			    	//don't delete function
			    	if(typeof loc_out[key] != 'function'){
			    		delete loc_out[key];
			    	}
			    }
			}
			loc_out.prv_isLive = false;
		}
	};
	
	/*
	 * destroy wrapper within this variable
	 * no event triggered
	 * destroy wrapper means wrapper's all resource get released
	 * it does not means variable is destroyed 
	 */
	var loc_destroyWrapper = function(requestInfo){
		//if no wrapper, no effect
		if(loc_out.prv_isWrapperExists()){
			//unregister listener from wrapper
			loc_out.prv_wrapper.unregisterDataOperationEventListener(loc_out.prv_dataOperationEventObject);
			loc_out.prv_wrapper.unregisterDataChangeEventListener(loc_out.prv_dataChangeEventObject);
			
			//destroy wrapper
			loc_out.prv_wrapper.destroy(requestInfo);
		}
		loc_out.prv_wrapper = undefined;
	};
	
	//listen to wrapper data operation event
	var loc_registerWrapperDataOperationEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, requestInfo){
			loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);
			
			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
				//data operation event turn to lifecycle event
				loc_out.destroy();
			}
		});
	};

	var loc_registerWrapperDataChangeEvent = function(){
		if(loc_out.prv_wrapper==undefined)  return;
		loc_out.prv_wrapper.registerDataChangeEventListener(loc_out.prv_dataChangeEventObject, function(event, eventData, requestInfo){
			loc_out.prv_dataChangeEventObject.triggerEvent(event, eventData, requestInfo);
		});
	};

	
	var loc_addNormalChildVariable = function(childVar, path){
		var childVarInfo = new node_ChildVariableInfo(childVar, path, path);
		loc_addChildVariable(loc_out.prv_childrenVariable, childVarInfo);
		return childVarInfo;
	};

	var loc_addChildVariableWithAdapter = function(childVar, path){
		var id = path+"_"+childVar.prv_id;
		var childVarInfo = new node_ChildVariableInfo(childVar, path, id, false);
		loc_addChildVariable(loc_out.prv_childrenVariable, childVarInfo);
		return childVarInfo;
	};
	
	var loc_addChildVariable = function(container, childVarInfo){
		container[childVarInfo.id] = childVarInfo;
		childVarInfo.variable.registerLifecycleEventListener(loc_out.prv_lifecycleEventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE){
				childVarInfo.variable.unregisterLifecycleEventListener(loc_out.prv_lifecycleEventObject);
				delete container[childVarInfo.id];
			}
		});
	};
	
	//set new wrapper
	var loc_setWrapper = function(wrapper, requestInfo){
		//destroy old wrapper first
		loc_destroyWrapper(requestInfo);
		//set wrapper
		loc_out.prv_wrapper = wrapper;
		
		if(wrapper!=undefined){
			nosliw.logging.info("************************  set wrapper to variable   ************************");
			nosliw.logging.info("variable: " + loc_out.prv_id);
			if(loc_out.prv_wrapper!=undefined)		nosliw.logging.info("wrapper: " + loc_out.prv_wrapper.prv_id);
			else  nosliw.logging.info("wrapper: " + undefined);
			nosliw.logging.info("***************************************************************");
			
			if(loc_out.prv_isWrapperExists()){
				//adapter
				loc_out.prv_wrapper.setValueAdapter(loc_out.prv_valueAdapter);
				loc_out.prv_wrapper.setPathAdapter(loc_out.prv_pathAdapter);
				loc_out.prv_wrapper.setEventAdapter(loc_out.prv_eventAdapter);
				//event listener
				loc_registerWrapperDataOperationEvent();
				loc_registerWrapperDataChangeEvent();
			}
			//update wrapper in children variable accordingly
			_.each(loc_out.prv_childrenVariable, function(childVariableInfo, id){
				childVariableInfo.variable.prv_updateWrapperInRelativeVariable(requestInfo);
			});
		}
	};

	var loc_out = {
			
			//update wrapper when parent's wrapper changed
			prv_updateWrapperInRelativeVariable : function(requestInfo){
				var parentVar = loc_relativeVariableInfo.parent;
				var parentWrapper = parentVar.prv_getWrapper();
				if(parentVar.prv_isWrapperExists())   loc_setWrapper(node_wrapperFactory.createWrapper(parentWrapper, loc_relativeVariableInfo.path), requestInfo);
				else loc_setWrapper(undefined, requestInfo);
			},
			
			prv_getWrapper : function(){	return this.prv_wrapper;	},

			prv_isWrapperExists : function(){
				if(this.prv_wrapper==null)   return false;
				return this.prv_wrapper.prv_isLive;
			},

			
			//has to be base variable
			//   data 
			//   value + dataTypeInfo
			//   value
			//   undefined, 
			prv_getSetBaseDataRequest : function(parm1, parm2, handlers, requestInfo){
				if(parm1==undefined){
					//no value means empty variable, no wrapper
					return node_createServiceRequestInfoSimple(new node_ServiceInfo("", {}), 
							function(requestInfo){  
								loc_setWrapper(undefined, requestInfo);
								if(parm2!=undefined)   loc_out.prv_wrapperType = parm2;
							}, 
							handlers, requestInfo);
				}
				
				//create empty wrapper fist
				var wrapperValue;      //store the value
				var entityType = node_getObjectType(parm1);
				var wrapper;
				if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
					wrapperValue = parm1.value;
					parm1.value = undefined;
					//new wrapper according to data type
					wrapper = node_wrapperFactory.createWrapper(parm1, parm2, requestInfo);
					loc_setWrapper(wrapper, requestInfo);
				}
				else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
					if(this.prv_wrapper==undefined){
						//no wrapper, generate one first
						wrapperValue = parm1;
						wrapper = node_wrapperFactory.createWrapper(undefined, loc_out.prv_wrapperType, requestInfo);
						loc_setWrapper(wrapper, requestInfo);
					}
					else{
						wrapper = this.prv_wrapper;
						wrapperValue = parm1;
						parm1 = undefined;
					}
				}
				//set new value
				return wrapper.getDataOperationRequest(node_uiDataOperationServiceUtility.createSetOperationService("", wrapperValue), handlers, requestInfo);
			},

			prv_getRelativeVariableInfo : function(){  return loc_relativeVariableInfo;   },

			prv_getChildren : function(){  return this.prv_childrenVariable;  },
			
			setValueAdapter : function(valueAdapter){  
				this.prv_valueAdapter = valueAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setValueAdapter(valueAdapter);
			},
			
			setPathAdapter : function(pathAdapter){  
				this.prv_pathAdapter = pathAdapter;
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setPathAdapter(pathAdapter);
			},

			setEventAdapter : function(eventAdapter){	
				this.prv_eventAdapter = eventAdapter;	
				if(this.prv_wrapper!=undefined)		this.prv_wrapper.setEventAdapter(eventAdapter);
			},
			
			prv_addChildVariable : function(childVar, path, adapterInfo){
				var out;
				if(adapterInfo==undefined){
					out = loc_addNormalChildVariable(childVar, path);
				}
				else{
					out = loc_addChildVariableWithAdapter(childVar, path);
				}
				return out;
			},
			
			//create child variable, if exist, then reuse it
			//return child variable info : {}
			//     variable : child variable 
			//     id : key in child container for child variable
			//		path
			createChildVariable : function(path, adapterInfo, requestInfo){
				return nosliw.runtime.getUIVariableManager().createChildVariable(this, path, adapterInfo);
			},

			/*
			 * register handler for operation event
			 */
			registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataChangeEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			unregisterDataOperationEventListener : function(listenerEventObj){		this.prv_dataOperationEventObject.unregister(listenerEventObj);		},
			unregisterDataChangeEventListener : function(listenerEventObj){		this.prv_dataChangeEventObject.unregister(listenerEventObj);		},
			getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
			getDataChangeEventObject : function(){   return this.prv_dataChangeEventObject;   },
			
			/*
			 * register handler for event of communication between parent and child variables
			 */
			registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){return this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);	},
			unregisterLifecycleEventListener : function(listenerEventObj){		this.prv_lifecycleEventObject.unregister(listenerEventObj);		},
			getLifecycleEventObject : function(){   return this.prv_lifecycleEventObject;   },
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var that = this;
				var out;
				
				if(operationService.command!=node_CONSTANT.WRAPPER_OPERATION_SET && !this.prv_isWrapperExists())  return node_createServiceRequestInfoSimple(undefined, function(){}, handlers, requester_parent);
				
				if(operationService.command==node_CONSTANT.WRAPPER_OPERATION_SET && loc_out.prv_isBase==true && node_basicUtility.isStringEmpty(operationService.parms.path)){
					//for set root data
					return loc_out.prv_getSetBaseDataRequest(operationService.parms.value, operationService.parms.dataType, handlers, requester_parent);
				}
				
				if(this.prv_wrapper!=undefined){
					if(loc_out.prv_dataOperationRequestAdapter!=undefined){
						out = loc_out.prv_dataOperationRequestAdapter.dataOperationRequest(operationService, handlers, requester_parent);
					}
					else{
						out = this.prv_wrapper.getDataOperationRequest(operationService, handlers, requester_parent);
					}
				}
				
				out.setRequestProcessors({
					success : function(requestInfo, data){
						nosliw.logging.info("************************  variable operation   ************************");
						nosliw.logging.info("ID: " + that.prv_id);
						nosliw.logging.info("Wrapper: " + (that.prv_wrapper==undefined?"":that.prv_wrapper.prv_id));
						nosliw.logging.info("Parent: " , ((loc_relativeVariableInfo==undefined)?"":loc_relativeVariableInfo.parent.prv_id));
						nosliw.logging.info("ParentPath: " , ((loc_relativeVariableInfo==undefined)?"":loc_relativeVariableInfo.path)); 
						nosliw.logging.info("Request: " , node_basicUtility.stringify(operationService));
						nosliw.logging.info("Result: " , node_basicUtility.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				
				return out;
			},
			
			executeDataOperationRequest : function(operationService, handlers, request){
				var requestInfo = this.getDataOperationRequest(operationService, handlers, request);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			use : function(){
				return nosliw.runtime.getUIVariableManager().useVariable(this);
			},
			
			release : function(requestInfo){
				nosliw.runtime.getUIVariableManager().releaseVariable(this);
			},
			
			destroy : function(requestInfo){
				node_getLifecycleInterface(loc_out).destroy(requestInfo);
//				nosliw.runtime.getUIVariableManager().destroyVariable(this);
			},
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo, requestInfo);
	return loc_out;
};


var node_ChildVariableInfo = function(childVar, path, id, isNormal){
	this.variable = childVar;
	this.path = path;
	this.id = id;
	this.isNormal = isNormal==undefined?true:isNormal;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.entity.RelativeEntityInfo", function(){node_RelativeEntityInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.OrderedContainerElementInfo", function(){node_OrderedContainerElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createOrderedContainersInfo", function(){node_createOrderedContainersInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createOrderVariableContainer", function(){node_createOrderVariableContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.utility", function(){node_variableUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});


//Register Node by Name

packageObj.createChildNode("ChildVariableInfo", node_ChildVariableInfo); 
packageObj.createChildNode("newVariable", node_newVariable); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("orderedcontainer");    

(function(packageObj){
//get used node
var node_ServiceInfo;
var node_CONSTANT;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_eventUtility;
var node_requestUtility;
var node_wrapperFactory;
var node_basicUtility;
var node_createEventObject;
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;
var node_createServiceRequestInfoSet;
var node_createVariableWrapper;
var node_requestServiceProcessor;
var node_wrapperFactory;
var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createHandleEachElementProcessor = function(baseVariable, path){
	//based variable and path
	var loc_baseVariable = baseVariable;
	var loc_path = path;
	
	//container variable to deal with element 
	var loc_containerVariable;

	//store child element order info
	var loc_orderChildrenInfo;

	//store element variable according to variable path
	var loc_elementsVariable;
	
	//event object
	var loc_eventObject = node_createEventObject();
	
	//add element to container
	//   index : position in container
	//   id : id of the element, optional
	var loc_addElement = function(index, id, requestInfo){
		//add element to order child info
		var eleInfo = loc_orderChildrenInfo.insertElement(index, id, requestInfo);
		//create child variable according to path provided by orderChildrenInfo
		var eleVariable = loc_containerVariable.createChildVariable(eleInfo.path);
		loc_elementsVariable[eleInfo.path] = eleVariable;
		//data operation event handle for child variable 
		eleVariable.registerDataOperationEventListener(loc_eventObject, function(event, eventData, request){
			if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
				loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_DELETEELEMENT, eleInfo.indexVariable, request);
				eleVariable.release();
				loc_orderChildrenInfo.deleteElement(eleInfo.path, request);
				delete loc_elementsVariable[eleInfo.path];
			}
		});
		return new node_OrderedContainerElementInfo(eleVariable, eleInfo.indexVariable);
	};
	
	var loc_getElements = function(){
		var out = [];
		_.each(loc_orderChildrenInfo.getElements(), function(ele, index){
			out.push(new node_OrderedContainerElementInfo(loc_elementsVariable[ele.path], ele.indexVariable));
		});
		return out;
	};
	
	var loc_getHandleEachElementOfOrderContainerRequest = function(elementHandler, handlers, request){
		//container looped
		//handle each element
		var i = 0;
		var handleElementsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("HandleElements", {"elements":loc_orderChildrenInfo.getElements()}), handlers, request);
		_.each(loc_orderChildrenInfo.getElements(), function(ele, index){
			var elementHandlerResult = elementHandler.call(loc_out, loc_elementsVariable[ele.path], ele.indexVariable);
			//output of elementHandleRequestFactory method maybe request, maybe just object
			if(node_getObjectType(elementHandlerResult)==node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST){
				//add child request from factory
				//eleId as path
				handleElementsRequest.addRequest(i+"", elementHandlerResult);
				i++;
			}
		});
		return handleElementsRequest;
	};
	
	var loc_buildContainerVarWrapper = function(){
		loc_orderChildrenInfo = node_createContainerOrderInfo();
		loc_elementsVariable = {};
		
		//prepare adapter for container variable
		var adapterInfo = {
			pathAdapter : loc_orderChildrenInfo,
			eventAdapter : function(event, eventData, pathPosition, requestInfo){
				if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE||(pathPosition==0 && event==node_CONSTANT.WRAPPER_EVENT_SET)){
					//when container value was changed
					//delete element data first
					loc_destroyContainerVariable();
					//trigue event
					loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_RESET, undefined, requestInfo);
					//no event to child
					return false;
				}
				return true;
			},
			destroyAdapter : function(){
//				loc_lifecycleEventObject.clearup();
//				loc_dataOperationEventObject.clearup();
//				loc_orderChildrenInfo.destroy();
			}
		};
		
		loc_containerVariable = loc_baseVariable.createChildVariable(loc_path, adapterInfo);
		
		loc_containerVariable.registerDataOperationEventListener(undefined, function(event, eventData, requestInfo){
			if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
				var newEventData = loc_addElement(eventData.index, eventData.id, requestInfo);
				loc_trigueEvent(node_CONSTANT.EACHELEMENTCONTAINER_EVENT_NEWELEMENT, newEventData, requestInfo);
			}
		}, this);
		return loc_containerVariable;
	};
	
	var loc_destroyContainerVariable = function(requestInfo){
		if(loc_orderChildrenInfo!=undefined){
			loc_orderChildrenInfo.destroy();
			loc_orderChildrenInfo = undefined;
		}
		if(loc_elementsVariable!=undefined){
			//release elements variable
			_.each(loc_elementsVariable, function(eleVar, path){
				eleVar.unregisterDataOperationEventListener(loc_eventObject);
				eleVar.release();
			});
			loc_elementsVariable = undefined;
		}
		if(loc_containerVariable!=undefined){
			//release container variable
			loc_containerVariable.unregisterDataOperationEventListener(loc_eventObject);
			loc_containerVariable.release();
			loc_containerVariable = undefined;
		}
	};
	
	var loc_trigueEvent = function(event, eventData, requestInfo){
		loc_eventObject.triggerEvent(event, eventData, requestInfo);
	};
	
	var loc_out = {
		
		getLoopRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence({}, handlers, request);
			if(loc_orderChildrenInfo==undefined){
				//if no loop request did before
				//build container related object (order child info, container variable)
				var containerVariable = loc_buildContainerVarWrapper();
				//get container value first
				out.addRequest(containerVariable.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(loc_path), {
					success : function(request, data){
						if(data==undefined)  return;
						
						//loop through element
						return node_wrapperFactory.getDataTypeHelper(data.dataTypeInfo).getGetElementsRequest(data.value, {
							success : function(request, valueElements){
								//create child variables
								_.each(valueElements, function(valueEle, index){
									loc_addElement(index, valueEle.id, request);
								});
								return loc_getElements(); 
//								return loc_getHandleEachElementOfOrderContainerRequest(elementHandler);
							}
						});
					}
				}));
			}
			else{
				//loop did before
				return node_createServiceRequestInfoSimple(undefined, function(){return loc_getElements();}, handlers, request);
//				out.addRequest(loc_getHandleEachElementOfOrderContainerRequest(elementHandler));
			}
			
			return out;
		},
		
		executeLoopRequest : function(elementHandler, handlers, request){
			var requestInfo = this.getLoopRequest(elementHandler, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		registerEventListener : function(listenerEventObj, handler, thisContext){
			loc_eventObject.registerListener(undefined, listenerEventObj, handler, thisContext);
		},
		
		//destroy 
		destroy : function(requestInfo){
			loc_eventObject.clearup();
			loc_destroyContainerVariable(requestInfo);
		},
	};
	
	return loc_out;
};



//element info expose to end user
//including two variable: element variabe, index variable
var node_OrderedContainerElementInfo = function(elementVar, indexVar){
	this.elementVar = elementVar;
	this.indexVar = indexVar;
	
	this.getElement = function(){
		return node_createVariableWrapper(this.elementVar);
	};
	
	this.getIndex = function(){
		return node_createVariableWrapper(this.indexVar);
	};
	
	return this;
};

var node_ContainerElementInfo = function(path, indexVar){
	//path for locating element variable from container aprent
	this.path = path;
	//index variable for position in container
	this.indexVariable = indexVar;
	return this;
};

//store order information of container
//	path : identify element variable from container variable
//	id : identify element data from container data
//	index : position of element in container data
//in order to identify elelment data from container data, try use id if provided, otherwise, use position
var node_createContainerOrderInfo = function(){
	
	var loc_generateId = function(){
		loc_out.prv_id++;
		return "id"+loc_out.prv_id+"";
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_out.prv_id = 0;

		//map from path to id, if not provided, then no id can use, just use index instead
		loc_out.prv_idByPath = {};
		//all elements
		loc_out.prv_elementsInfo = [];
	};

	var loc_createIndexVariable = function(path, requestInfo){
		var value = {
				path : path,
				eventObject : node_createEventObject(),
				
				getValue : function(){
					var index = loc_out.prv_getIndexByPath(this.path);
					return index;
				},	
					
				registerListener : function(eventObj, handler, thisContext){
					this.eventObject.registerListener(undefined, eventObj, handler, thisContext);
				},

				trigueEvent : function(requestInfo){
					this.eventObject.triggerEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, undefined, requestInfo);
				},
				
				destroy : function(){
					this.eventObject.clearup();
				},
			};
		return node_createVariableWrapper(node_dataUtility.createDataOfDynamic(value), undefined, undefined, requestInfo);
	};

	
	var loc_trigueIndexChange = function(startIndex, requestInfo){
		for(var i=startIndex; i<loc_out.prv_elementsInfo.length; i++){
			loc_out.prv_elementsInfo[i].indexVariable.executeDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(""), {
				success : function(request, data){
					data.value.trigueEvent(request);
				}
			}, requestInfo);
		}
	};
	
	var loc_out = {
			
		prv_getIndexByPath : function(path){
			var out;
			_.some(this.prv_elementsInfo, function(eleInfo, index){
				if(eleInfo.path==path){
					out = index;
					return true;
				}
			});
			return out;
		},	
			
		//index : position to insert element
		//id : something to identify element with data
		insertElement : function(index, id, requestInfo){
			//path is something that won't change
			//if id for element is provided, use id as path
			//otherwise, generate id as path
			var path = id;
			if(path==undefined)  path = loc_generateId();

			//no id provided for element, then no mapping added
			if(id!=undefined)  loc_out.prv_idByPath[path] = id;
			
			//generate element info
			var eleInfo = new node_ContainerElementInfo(path, loc_createIndexVariable(path, requestInfo));
			loc_out.prv_elementsInfo.splice(index, 0, eleInfo);
			
			//trigue index change event
			loc_trigueIndexChange(index+1, requestInfo);
			return eleInfo;
		},
		
		deleteElement : function(path, requestInfo){
			delete this.prv_idByPath[path];
			
			var index = this.prv_getIndexByPath(path);
			this.prv_elementsInfo.splice(index, 1);
			
			loc_trigueIndexChange(index, requestInfo);
		},
		
		getElements : function(){		return loc_out.prv_elementsInfo;	},

		//from variable path to data path
		toRealPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from path to real path
			var realElePath = loc_out.prv_idByPath[elePath];       //has id for this path
			if(realElePath==undefined){
				//not provided, then use index as path, can be improved using index variable
				realElePath = loc_out.prv_getIndexByPath(elePath) + "";
			}
			
			//build full path again
			var out = realElePath;
			if(index!=-1){
				out = out + path.substring(index);
			}
			return out;
		},
		
		//from data path to variable path
		toAdapteredPath : function(path){
			//find first path seg
			var index = path.indexOf(".");
			var elePath = path;
			if(index!=-1)  elePath = path.substring(0, index);
			
			//convert first path seg from real path to adapted path
			var adapteredElePath;
			//whether data path is id, if so find path from idByPaht 
			_.each(loc_out.prv_idByPath, function(id, p){
				if(id==elePath) adapteredElePath = p;
			});
		
			if(adapteredElePath==undefined){
				//otherwise, data path is index
				adapteredElePath = loc_out.prv_elementsInfo[parseInt(elePath)].path;
			}
			
			//build full path again
			var out = adapteredElePath;
			if(index!=-1){
				out = out + path.substring(index);
			}
			return out;
		},
		
		destroy : function(){
			_.each(this.prv_elementsInfo, function(eleInfo, index){
				eleInfo.indexVariable.release();
			});
		},

	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableWrapper", function(){node_createVariableWrapper = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name

packageObj.createChildNode("createHandleEachElementProcessor", node_createHandleEachElementProcessor); 

packageObj.createChildNode("OrderedContainerElementInfo", node_OrderedContainerElementInfo); 
packageObj.createChildNode("ContainerElementInfo", node_ContainerElementInfo); 
packageObj.createChildNode("createContainerOrderInfo", node_createContainerOrderInfo); 


})(packageObj);
//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_createRequestEventGroupHandler;
//*******************************************   Start Node Definition  ************************************** 	

var node_createVariableGroup = function(variablesArray, handler, thisContext){

	//event handler
	var loc_handler = handler;
	//variables
	var loc_variables = {};
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(variable, key){
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), key+"");
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(variablesArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in variablesArray){
			loc_addElement(variablesArray[i], i);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		loc_handler = undefined;
	};

	var loc_out = {

		addVariable : function(variable, key){	loc_addElement(variable, key);		},
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(variablesArray, handler, thisContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableGroup", node_createVariableGroup); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_getObjectType;
var node_ChildVariableInfo;
var node_newVariable;

//*******************************************   Start Node Definition  **************************************

var node_createVariableManager = function(){
	//all variables
	var loc_variables = {};
	//variable usage
	var loc_varUsage = {};

	var loc_newVariable = function(data1, data2, adapterInfo){
		var variable = node_newVariable(data1, data2, adapterInfo);
		loc_variables[variable.prv_id] = variable;
		loc_varUsage[variable.prv_id] = 0;
		return variable;
	};

	var loc_useVariable = function(variableId){	
		loc_varUsage[variableId]++;
		return loc_variables[variableId];
	};
	
	var loc_releaseVariable = function(variableId){
		loc_varUsage[variableId]--;
		if(loc_varUsage[variableId]<=0){
			loc_variables[variableId].destroy();
			delete loc_variables[variableId];
			delete loc_varUsage[variableId];
		}
		else return loc_variables[variableId];
	};
	
	var loc_out = {
		
		getVariableInfo : function(id){		
			return { 
				id :id,
				variable :loc_variables[id],
				usage : loc_varUsage[id]
			};	
		},
		
		createVariable : function(data1, data2, adapterInfo){
			var data1Type = node_getObjectType(data1);
			if(data1Type==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE){
				//if data1 is variable, then use crete child variable 
				return this.createChildVariable(data1, data2, adapterInfo);
			}
			else{
				return loc_newVariable(data1, data2, adapterInfo);
			}
		},

		createChildVariable : function(variable, path, adapterInfo){
			var out;
			if(adapterInfo==undefined){
				//normal child, try to reuse existing one
				var childVarInfo;
				if(path==undefined || path=="")  childVarInfo = new node_ChildVariableInfo(variable, "");
				else childVarInfo = variable.prv_childrenVariable[path];
				
				if(childVarInfo==undefined){
					out = loc_newVariable(variable, path, adapterInfo);
				}
				else{
					out = childVarInfo.variable;
				}
			}
			else{
				//child with extra info
				out = loc_newVariable(variable, path, adapterInfo);
			}
			return out;
		},
		
		useVariable : function(variable){
			return loc_useVariable(variable.prv_id);  
		},
		
		releaseVariable : function(variable){  return loc_releaseVariable(variable.prv_id);  },
		
		destroyVariable : function(variable) {  
			loc_varUsage[variable.prv_id]--;
			this.releaseVariable(variable);
		}
		
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.ChildVariableInfo", function(){node_ChildVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.newVariable", function(){node_newVariable = this.getData();});


//Register Node by Name
packageObj.createChildNode("createVariableManager", node_createVariableManager); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("variable");    

(function(packageObj){
//get used node
var node_ServiceInfo;
var node_CONSTANT;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_eventUtility;
var node_requestUtility;
var node_wrapperFactory;
var node_basicUtility;
var node_createEventObject;
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;
var node_getHandleEachElementRequest;
var node_requestServiceProcessor;

//*******************************************   Start Node Definition  **************************************
//input model:
//	variable
//	variable wrapper + path
//	same as variable input
var node_createVariableWrapper = function(data1, data2, adapterInfo, requestInfo){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(data1, data2, adapterInfo, requestInfo){

		//every variable has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();

		var entityType = node_getObjectType(data1);
		if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLE && node_basicUtility.isStringEmpty(data2) && adapterInfo==undefined){
			loc_out.prv_variable = data1;
		}
		else{
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER)	data1 = data1.prv_getVariable();
			loc_out.prv_variable = nosliw.runtime.getUIVariableManager().createVariable(data1, data2, adapterInfo, requestInfo);
		}
		
		//use variable when created
		loc_out.prv_variable.use();
		
		//event source for event that communicate operation information with outsiders
		loc_out.prv_dataOperationEventObject = node_createEventObject();
		loc_out.prv_dataChangeEventObject = node_createEventObject();

		//receive event from variable and trigue new same event
		//the purpose of re-trigue the new event is for release the resources after this variable wrapper is released
		loc_out.prv_variable.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, function(event, eventData, request){
			if(loc_out.prv_dataOperationEventObject!=undefined) loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, request);
		}, loc_out);
		loc_out.prv_variable.registerDataChangeEventListener(loc_out.prv_dataChangeEventObject, function(event, eventData, request){
			if(loc_out.prv_dataChangeEventObject!=undefined) loc_out.prv_dataChangeEventObject.triggerEvent(event, eventData, request);
		}, loc_out);
	};	

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		//take care of release event 
		loc_out.prv_dataOperationEventObject.clearup();
		loc_out.prv_dataChangeEventObject.clearup();
		delete loc_out.prv_dataOperationEventObject;
		delete loc_out.prv_dataChangeEventObject;
		//release variable
		loc_out.prv_variable.release();
		delete loc_out.prv_variable;
	};	
	
	var loc_out = {
		
		prv_getVariable : function(){	return this.prv_variable;	},
		
		createChildVariable : function(path, adapterInfo, requestInfo){	
			return node_createVariableWrapper(this, path, adapterInfo, requestInfo);
		}, 
		
		release : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		isEmpty : function(){  return this.prv_variable.prv_isWrapperExists();    },
		
		getDataOperationRequest : function(operationService, handlers, request){	return this.prv_variable.getDataOperationRequest(operationService, handlers, request);	},
		executeDataOperationRequest : function(operationService, handlers, request){
			var requestInfo = this.getDataOperationRequest(operationService, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){return this.prv_dataChangeEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);},
		unregisterDataOperationEventListener : function(listenerEventObj){return this.prv_dataOperationEventObject.unregister(listenerEventObj);},
		unregisterDataChangeEventListener : function(listenerEventObj){return this.prv_dataChangeEventObject.unregister(listenerEventObj);},
		getDataOperationEventObject : function(){   return this.prv_dataOperationEventObject;   },
		getDataChangeEventObject : function(){   return this.prv_dataChangeEventObject;   },
		
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VARIABLEWRAPPER);
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());

	node_getLifecycleInterface(loc_out).init(data1, data2, adapterInfo, requestInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.orderedcontainer.getHandleEachElementRequest", function(){node_getHandleEachElementRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariableWrapper", node_createVariableWrapper); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_getObjectType;
var node_createWraperCommon;
var node_getObjectId;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_wrapperFactory = function(){
	
	var loc_dataTypeHelpers = {};
	
	var loc_out = {
		
		/*
		 * Register wrapper factory by data type,
		 * Different type of data have different wrapper implementation
		 * One wrapper type may support different data type
		 * For instance: object, data
		 */	
		registerDataTypeHelper : function(dataTypeIds, dataTypeHelper){
			_.each(dataTypeIds, function(dataTypeId, index){
				loc_dataTypeHelpers[dataTypeId] = dataTypeHelper;
			});
		},
		

		/*
		 * parent wrapper + path
		 * data
		 * value + dataType 
		 */	
		createWrapper : function(parm1, parm2, requestInfo){
			var wrapperParm1;
			var dataType = undefined;
			var path = undefined;
			
			var entityType = node_getObjectType(parm1);
			if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
				dataType = parm1.getDataType();
				wrapperParm1 = parm1;
				path = parm2;
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
				dataType = parm1.dataTypeInfo;
				wrapperParm1 = parm1.value;
			}
			else if(entityType==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE){
				dataType = parm2;
				wrapperParm1 = parm1;
			}
			else{
				dataType = parm2;
				wrapperParm1 = parm1;
			}
			
			if(node_basicUtility.isStringEmpty(dataType))   dataType = node_CONSTANT.DATA_TYPE_OBJECT;
			
			var out = node_createWraperCommon(wrapperParm1, path, this.getDataTypeHelper(dataType), dataType);

			return out;
		},
		
		getDataTypeHelper : function(dataTypeInfo){
			return loc_dataTypeHelpers[dataTypeInfo];
		},
			
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.createWraperCommon", function(){node_createWraperCommon = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.getObjectId", function(){node_getObjectId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("wrapperFactory", node_wrapperFactory); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_basicUtility;
var node_parseSegment;
var node_createServiceRequestInfoSequence;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_OperationParm;
var node_dataUtility;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = function(){
	
	var loc_getSegmentChildAppDataRequest = function(opData, segs, i){
		var size = segs.getSegmentSize();
		if(i>=size){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				return node_dataUtility.createDataOfAppData(opData);
			}); 
		}
		else{
			var operationParms = [];
			operationParms.push(new node_OperationParm(opData, "base"));
			operationParms.push(new node_OperationParm({
				dataTypeId: "test.string;1.0.0",
				value : segs.next()
			}, "name"));

			return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
					opData.dataTypeId, 
					node_COMMONCONSTANT.DATAOPERATION_COMPLEX_GETCHILDDATA, 
					operationParms, {
						success : function(request, childData){
							if(childData==undefined)  node_dataUtility.createDataOfAppData();;
							i++;
							return loc_getSegmentChildAppDataRequest(childData, segs, i);
						}
					});
		}
		
	};
		
	var loc_out = {

			getGetChildAppDataRequest : function(appData, path, handlers, requester_parent){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetChildAppData", {"appData":appData, "path":path}), handlers, requester_parent);
				out.addRequest(loc_getSegmentChildAppDataRequest(appData, node_parseSegment(path), 0));
				return out;
			},

			getSetChildAppDataRequest : function(baseData, path, opData, handlers, requester_parent){
				var index = path.lastIndexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
				var basePath = undefined;
				var leafAttr = path;
				if(index!=-1){
					basePath = path.subString(0, index);
					leafAttr = path.subString(index+1);
				}
				
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SetChildAppData", {"baseData":baseData, "opData":opData, "path":path}), handlers, requester_parent);
				
				var getRequest = this.getGetChildAppDataRequest(baseData, basePath, {
					success : function(requestInfo, data){
						var appData = data.value;
						var operationParms = [];
						operationParms.push(new node_OperationParm({
							dataTypeId: "test.string;1.0.0",
							value : leafAttr
						}, "name"));
						operationParms.push(new node_OperationParm(appData, "base"));
						operationParms.push(new node_OperationParm(opData, "value"));
						return nosliw.runtime.getExpressionService().getExecuteOperationRequest(
								appData.dataTypeId, 
								node_COMMONCONSTANT.DATAOPERATION_COMPLEX_SETCHILDDATA, 
								operationParms, {
									success : function(request, data){
										return data;
									}
								});
					}
				}, requester_parent);
				
				out.addRequest(getRequest)
				return out;
			}
	};	
	
	
	return loc_out;

}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_EventInfo;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_createServiceRequestInfoSequence;
var node_uiDataOperationServiceUtility;
var node_createServiceRequestInfoSimple;
var node_ServiceInfo;
var node_createServiceRequestInfoSet;
var node_requestServiceProcessor;
var node_createWrapperOrderedContainer;
var node_RelativeEntityInfo;
	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * parm1 : parent wrapper / value
 * path : valid for relative wrapper, path to parent
 * typeHelper : utility methods according to different data type : object data / app data
 * dataType : object data / appdata / dynamic
 */
var node_createWraperCommon = function(parm1, path, typeHelper, dataType){
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(parm1, path, typeHelper, dataType){
		//whether this wrapper is live or destroyed
		loc_out.prv_isLive = true;

		//every wrapper has a id, it is for debuging purpose
		loc_out.prv_id = nosliw.runtime.getIdService().generateId();
		
		//helper object that depend on data type in wrapper 
		loc_out.prv_typeHelper = typeHelper;
		
		//what kind of data this wrapper represent(object, appdata or dynamic)
		loc_out.prv_dataType = dataType;
		
		//if true, this wrapper is based on root data, otherwise, this wrapper is based on parent wrapper, 
		loc_out.prv_dataBased = true;

		//for base wrapper, it is root value
		//information for relative wrapper : parent, path
		loc_out.prv_relativeWrapperInfo = undefined;
		
		//event and listener for data operation event
		loc_out.prv_lifecycleEventObject = node_createEventObject();		//life cycle
		loc_out.prv_dataOperationEventObject = node_createEventObject();    //data operation
		loc_out.prv_internalEventObject = node_createEventObject();			//internal data operation, it does not expose to variable, only expose to child
		loc_out.prv_dataChangeEventObject = node_createEventObject();		//when either data operation or internal data operation event trigued, data change event is triggued. it is only for variable

		//for relative wrapper, it is temporarily calculated value got during operation on value, screenshot
		loc_out.prv_value = undefined

		//a list of wrapper operations that should be applied to wrapper
		//if this list is not empty, that means we only need to apply all operation first, then get data
		//in this case, isValidData is true
		loc_out.prv_toBeDoneWrapperOperations = [];
		
		//whether the data need to calculated from parent, or sync with parent 
		//true : data is not dirty, in order to get data, apply prv_toBeDoneWrapperOperations operations to prv_value
		//false : data is dirty
		loc_out.prv_isValidData = false;

		//adapter for converting value
		//with adapter, we can insert some converting job into this wrapper, 
		//this converting job can transform the wrapper value during read and set
		loc_out.prv_valueAdapter;
		
		//path adapter is for 
		//1. calculate real path from parent when do data operation
		//2. calculate path from real path 
		loc_out.prv_pathAdapter;
		
		//adapter that affect the event
		loc_out.prv_eventAdapter;
		
		//whether data based or wrapper based
		if(node_getObjectType(parm1)==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//wrapper based
			loc_out.prv_dataBased = false;
			//relative information
			loc_out.prv_relativeWrapperInfo = new node_RelativeEntityInfo(parm1, path);
			//inherit data type from parent
			loc_out.prv_dataType = loc_out.prv_relativeWrapperInfo.parent.getDataType();

			//listen to parent's life cycle event
			loc_out.prv_relativeWrapperInfo.parent.registerLifecycleEventListener(loc_out.prv_lifecycleEventObject, loc_lifecycleEventProcessor);
			//listen to parent's data operation event
			loc_out.prv_relativeWrapperInfo.parent.registerDataOperationEventListener(loc_out.prv_dataOperationEventObject, loc_dataOperationEventProcessor, this);
			//listen to parents's internal event : similar to data operation event, except that it won't expose to variable
			loc_out.prv_relativeWrapperInfo.parent.registerInternalEventListener(loc_out.prv_internalEventObject, loc_dataOperationEventProcessor, this);
		}
		else{
			//data based
			loc_out.prv_dataBased = true;
			loc_setValue(parm1);
		}
		
		nosliw.logging.info("************************  wrapper created   ************************");
		nosliw.logging.info("ID: " + loc_out.prv_id);
		if(loc_out.prv_relativeWrapperInfo!=undefined){
			nosliw.logging.info("Parent: " + loc_out.prv_relativeWrapperInfo.parent.prv_id);
			nosliw.logging.info("Parent Path: " + loc_out.prv_relativeWrapperInfo.path);
		}
		nosliw.logging.info("***************************************************************");
		
	};

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){  loc_destroy();  };

	//destroy resources in wrapper
	//lifecycle event trigued
	var loc_destroy = function(requestInfo){
		if(loc_out.prv_isLive==true){
			nosliw.logging.info("************************  wrapper destroying   ************************");
			nosliw.logging.info("ID: " + loc_out.prv_id);
			nosliw.logging.info("***************************************************************");

			//forward the lifecycle event
			loc_trigueLifecycleEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE, {}, requestInfo);
			
			loc_out.prv_isLive = false;
			
			//clear up event source
			loc_out.prv_dataOperationEventObject.clearup();
			loc_out.prv_internalEventObject.clearup();
			loc_out.prv_dataChangeEventObject.clearup();

			//for destroy, release resource
			loc_invalidateData();

			loc_trigueLifecycleEvent(node_CONSTANT.WRAPPER_EVENT_CLEARUP_AFTER, {}, requestInfo);
			loc_out.prv_lifecycleEventObject.clearup();
			for (var key in loc_out){
			    if (loc_out.hasOwnProperty(key)){
			    	//don't delete function
			    	if(typeof loc_out[key] != 'function'){
				        delete loc_out[key];
			    	}
			    }
			}		
			loc_out.prv_isLive = false;
		}
	};
	
	//process lifecycle event in parent
	var loc_lifecycleEventProcessor = function(event, eventData, requestInfo){
		if(loc_out.prv_isLive){
			if(event==node_CONSTANT.WRAPPER_EVENT_CLEARUP_BEFORE){
				//if parent destroyed, destroy itself
				loc_destroy(requestInfo);
			}				
		}
	};
	
	//
	var loc_getAdapterPathFromEventElementPath = function(eventData){
		var elePath;
		if(eventData.id!=undefined)  elePath = eventData.id;
		else if(eventData.index!=undefined)  elePath = eventData.index+"";
		if(loc_out.prv_pathAdapter!=undefined){
			elePath = loc_out.prv_pathAdapter.toAdapteredPath(elePath);
		}
		return elePath;
	}
	
	//process parent data operation event
	var loc_dataOperationEventProcessor = function(event, eventData, requestInfo){
		if(loc_out.prv_isLive==true){
			if(event==node_CONSTANT.WRAPPER_EVENT_FORWARD){
				//for forward event, expand it
				event = eventData.event;
				eventData = eventData.value;
			}
			
			//store all the generated events
			var events = {
				internal : [],
				dataOperation : [],
				lifecycle : []
			};
			
			//in order to avoid any change on original event data, clone event data so that we can modify it and resent it
			if(eventData!=undefined)  eventData = eventData.clone();
			
			var pathPosition = undefined;
			if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE){
				//for change event from parent, just make data invalid & forward the change event, 
				loc_invalidateData(requestInfo);
				events.dataOperation.push(new node_EventInfo(node_CONSTANT.WRAPPER_EVENT_CHANGE));
			}
			else{
				var pathCompare = node_dataUtility.comparePath(loc_out.prv_relativeWrapperInfo.path, eventData.path);
				pathPosition = pathCompare.compare; 
				if(pathPosition == 0){
					//event happens on this wrapper, trigue the same
					//inform the change of wrapper
					eventData.path = "";
					if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
						events.dataOperation.push(new node_EventInfo(event, eventData));
					}
					else if(event==node_CONSTANT.WRAPPER_EVENT_ADDELEMENT){
						//store data operation event
						loc_addToBeDoneDataOperation(event, eventData);
						//inform outside about change
						events.dataOperation.push(new node_EventInfo(event, eventData));
					}
					else if(event==node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT){
						var elePath = loc_getAdapterPathFromEventElementPath(eventData);
						//transform to delete event, delete event is internal only, means it would not inform to user, use inform child 
						events.internal.push(loc_triggerForwardEvent(node_CONSTANT.WRAPPER_EVENT_DELETE, node_uiDataOperationServiceUtility.createDeleteOperationService(elePath), requestInfo));
						
						eventData.id = elePath;   //kkkk
						//store data operation event
						loc_addToBeDoneDataOperation(event, eventData);
						//inform outside about change
						events.dataOperation.push(new node_EventInfo(event, eventData));
					}
					else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
						if(loc_out.prv_valueAdapter==undefined){
							loc_setValue(eventData.value);
							events.dataOperation.push(new node_EventInfo(event, eventData));
						}
						else{
							//apply adapter to value
							var r = loc_out.prv_valueAdapter.getInValueRequest(eventData.value, {
								success: function(request, value){
									loc_setValue(value);
									eventData.value = value;
									events.dataOperation.push(new node_EventInfo(event, eventData));
								}
							}, requestInfo);
							node_requestServiceProcessor.processRequest(r);
						}
					}
				}
				else if(pathPosition == 1){
					//something happens in the middle between parent and this
					if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
						eventData.path = "";
						events.dataOperation.push(new node_EventInfo(event, eventData));
					}
					else if(event==node_CONSTANT.WRAPPER_EVENT_SET){
						loc_invalidateData(requestInfo);
						events.dataOperation.push(new node_EventInfo(node_CONSTANT.WRAPPER_EVENT_CHANGE));
					}
				}
				else if(pathPosition == 2){
					//something happens beyond this, just forward the event with sub path, only set event
					//store the change
					eventData.path = loc_out.toAdapteredPath(pathCompare.subPath);
					loc_addToBeDoneDataOperation(event, eventData);
					events.internal.push(loc_triggerForwardEvent(event, eventData, requestInfo));
				}
				else{
					//not on right path, do nothing
				}
			}
			
			var trigueEvent = true;
			if(loc_out.prv_eventAdapter!=undefined){
				//apply trigue
				trigueEvent = loc_out.prv_eventAdapter(event, eventData, pathPosition, requestInfo);
			}
			//trigue event
			if(trigueEvent==true){
				loc_trigueEvents(events, requestInfo);
			}
		}

	};
	
	//trigue events collection
	var loc_trigueEvents = function(events, requestInfo){
		_.each(events.internal, function(eventInfo){   loc_trigueInternalEvent(eventInfo.eventName, eventInfo.eventData, requestInfo);   });
		_.each(events.dataOperation, function(eventInfo){
			loc_trigueDataOperationEvent(eventInfo.eventName, eventInfo.eventData, requestInfo);
			//when delete, then destroy, triggue lifecycle event
			if(eventInfo.eventName==node_CONSTANT.WRAPPER_EVENT_DELETE)	loc_destroy(requestInfo);
		});
		_.each(events.lifecycle, function(eventInfo){   loc_trigueLifecycleEvent(eventInfo.eventName, eventInfo.eventData, requestInfo); });
		//if something happened on child side, then trigue data change event (refresh)
		if(events.internal.length+events.dataOperation.length>0)   loc_trigueDataChangeEvent(node_CONSTANT.WRAPPER_EVENT_REFRESH, undefined, requestInfo);
	};
	

	//get value of current wrapper request
	var loc_getGetValueRequest = function(handlers, requester_parent){
		var out;
		var operationService = new node_ServiceInfo("Internal_GetWrapperValue", {});
		if(loc_out.prv_dataBased==true){
			//root data
			out = node_createServiceRequestInfoSimple(operationService,	function(){	return loc_out.prv_value;  }, handlers, requester_parent);
		}
		else{
			if(loc_out.prv_isValidData==false){
				//data is dirty, then calculate data, sync data
				out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
				//get parent data first
				var calParentDataRequest = loc_out.prv_relativeWrapperInfo.parent.getDataOperationRequest(node_uiDataOperationServiceUtility.createGetOperationService(), {
					success : function(request, parentData){
						//calculate current value from parent
						//path from parent to calculate child value
						var childPath = loc_out.prv_relativeWrapperInfo.parent.toRealPath(loc_out.prv_relativeWrapperInfo.path); 

						return loc_out.prv_typeHelper.getChildValueRequest(parentData.value, childPath, {
							success : function(requestInfo, value){
								//set local value
								if(loc_out.prv_valueAdapter==undefined){
									if(loc_out.prv_typeHelper==undefined){
										requestInfo.trackRequestStack();
									}
									
									loc_setValue(value);
									return value;
								}
								else{
									//apply adapter to value
									return loc_out.prv_valueAdapter.getInValueRequest(value, {
										success: function(request, value){
											loc_setValue(value);
											return value;
										}
									}, requestInfo);
								}
							}
						});
					}
				});
				out.addRequest(calParentDataRequest);
			}
			else{
				//if data is not dirty, apply all the operation
				if(loc_out.prv_toBeDoneWrapperOperations.length>0){
					//calculate current value 
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					out.addRequest(loc_getProcessToBeDoneValueOperationRequest(0, loc_out.prv_value));
					
					out.setRequestProcessors({
						success : function(requestInfo, value){
							loc_out.prv_toBeDoneWrapperOperations.splice(0,loc_out.prv_toBeDoneWrapperOperations.length)
							loc_out.prv_value = value;
							return value;
						}
					});
				}
				else{
					out = node_createServiceRequestInfoSimple(operationService, function(){return loc_out.prv_value;}, handlers, requester_parent);
				}
			}
		}
		return out;
	};
	
	var loc_getProcessToBeDoneValueOperationRequest = function(i, value, handlers, request){
		var out = loc_out.prv_typeHelper.getDataOperationRequest(value, loc_out.prv_toBeDoneWrapperOperations[i], {
			success : function(requestInfo, value){
				i++;
				if(i<loc_out.prv_toBeDoneWrapperOperations.length){
					return loc_getProcessToBeDoneValueOperationRequest(i, value);
				}
				else{
					return value;
				}
			}
		}, request);
		
		return out;
	};
	
	//change value totally
	var loc_setValue = function(value){
		//make value invalid first
		loc_invalidateData();
		//then store value
		loc_out.prv_isValidData = true;
		loc_out.prv_value = value;
		
		if(loc_out.prv_dataType==node_CONSTANT.DATA_TYPE_DYNAMIC){
			//dynamic value may have event
			if(loc_out.prv_value!=undefined){
				loc_out.prv_value.registerListener(loc_out.prv_dataOperationEventObject, function(event, eventData, requestInfo){
					loc_trigueDataChangeEvent(event, eventData, requestInfo);
				});
			}
		}
	};
	
	//add to be done operation
	//it only when data is valid
	//if data is not valid, data should be recalculated
	var loc_addToBeDoneDataOperation = function(event, eventData){
		if(loc_out.prv_isValidData==true){
			var command;
			switch(event){
			case node_CONSTANT.WRAPPER_EVENT_SET:
				command = node_CONSTANT.WRAPPER_OPERATION_SET;
				break;
			case node_CONSTANT.WRAPPER_EVENT_ADDELEMENT:
				command = node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT;
				break;
			case node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT:
				command = node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT;
				break;
			case node_CONSTANT.WRAPPER_EVENT_DELETE:
				command = node_CONSTANT.WRAPPER_OPERATION_DELETE;
				break;
			}

			loc_out.prv_toBeDoneWrapperOperations.push(new node_ServiceInfo(command, eventData));
		}
	};
	
	/*
	 * mark data as invalid so that it would be recalculated
	 */
	var loc_invalidateData = function(requestInfo){
		if(loc_out.prv_typeHelper!=undefined)	loc_out.prv_typeHelper.destroyValue(loc_out.prv_value);
		loc_out.prv_isValidData = false;
		loc_out.prv_value = undefined;
		loc_out.prv_toBeDoneWrapperOperations = [];
	};
	
	var loc_makeDataFromValue = function(value){    return node_dataUtility.createDataByObject(value, loc_out.prv_dataType);	};

	var loc_trigueDataOperationEvent = function(event, eventData, requestInfo){
		if(loc_out.prv_isLive)  loc_out.prv_dataOperationEventObject.triggerEvent(event, eventData, requestInfo);	
	};
	var loc_trigueLifecycleEvent = function(event, eventData, requestInfo){		
		if(loc_out.prv_isLive)  loc_out.prv_lifecycleEventObject.triggerEvent(event, eventData, requestInfo);	
	};
	var loc_trigueInternalEvent = function(event, eventData, requestInfo){
		if(loc_out.prv_isLive)  loc_out.prv_internalEventObject.triggerEvent(event, eventData, requestInfo);	
	};
	var loc_trigueDataChangeEvent = function(event, eventData, requestInfo){	
		if(loc_out.prv_isLive)  loc_out.prv_dataChangeEventObject.triggerEvent(event, eventData, requestInfo);	
	};
	
	var loc_triggerForwardEvent = function(event, eventData, requestInfo){
		var eData = {
				event : event, 
				value : eventData 
		};
		return new node_EventInfo(node_CONSTANT.WRAPPER_EVENT_FORWARD, eData);
	};
	
	//trigger event according to data operation on root value
	var loc_triggerEventByDataOperation = function(command, dataOperationParms, requestInfo){
		var event;
		var eventData = dataOperationParms.clone();
		switch(command){
		case node_CONSTANT.WRAPPER_OPERATION_SET:
			event = node_CONSTANT.WRAPPER_EVENT_SET;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_ADDELEMENT;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT:
			event = node_CONSTANT.WRAPPER_EVENT_DELETEELEMENT;
			break;
		case node_CONSTANT.WRAPPER_OPERATION_DELETE:
			event = node_CONSTANT.WRAPPER_EVENT_DELETE;
			break;
		}
		
		var events = {
				internal : [],
				dataOperation : [],
				lifecycle : []
			};

		if(node_basicUtility.isStringEmpty(eventData.path)){
			//on data itself
			events.dataOperation.push(new node_EventInfo(event, eventData));
		}
		else{
			//on child node
			events.internal.push(loc_triggerForwardEvent(event, eventData, requestInfo));
		}
		loc_trigueEvents(events, requestInfo);
	};

	var loc_out = {
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				if(loc_out.prv_isLive==false)  return;
				var that = this;
				
				var command = operationService.command;
				var operationData = operationService.parms;
				var out;
				
				if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
					out = node_createServiceRequestInfoSequence(operationService, handlers, requester_parent);
					//get current value first
					out.addRequest(loc_getGetValueRequest({
						success : function(request, value){
							if(node_basicUtility.isStringEmpty(operationData.path))		return loc_makeDataFromValue(value);
							else
								//calculate value according to path
								return that.prv_typeHelper.getChildValueRequest(value, operationData.path, {
									success : function(requestInfo, value){
										return loc_makeDataFromValue(value);
									}
								});
						}
					}));
				}
				else{
					var opService = operationService.clone();
					if(this.prv_dataBased==true){
						//operate on root value
						if(command==node_CONSTANT.WRAPPER_OPERATION_SET && node_basicUtility.isStringEmpty(opService.parms.path)){
							//if set to base, then just set directly
							out = node_createServiceRequestInfoSimple(operationService,	function(){
								loc_setValue(operationService.parms.value);
							}, handlers, requester_parent);
						}
						else{
							out = this.prv_typeHelper.getDataOperationRequest(loc_out.prv_value, opService, handlers, requester_parent);
						}
						out.addPostProcessor({
							success : function(requestInfo, data){
								//trigue event
								loc_triggerEventByDataOperation(opService.command, opService.parms, requestInfo);
							}
						});
					}
					else{
						//otherwise, convert to operation on parent, util reach root
						if(command==node_CONSTANT.WRAPPER_OPERATION_SET && this.prv_valueAdapter!=undefined){
							//apply adapter for SET command
							out = node_createServiceRequestInfoSequence({}, handlers, requester_parent);
							//apply adapter to value
							out.addRequest(this.prv_valueAdapter.getOutValueRequest(operationData.value, {
								success: function(request, value){
									opService.parms.path = that.prv_relativeWrapperInfo.parent.toRealPath(node_dataUtility.combinePath(that.prv_relativeWrapperInfo.path, opService.parms.path)) ;
									opService.parms.value = value;
									return that.prv_relativeWrapperInfo.parent.getDataOperationRequest(opService);
								}
							}));
						}
						else{
							opService.parms.path = this.prv_relativeWrapperInfo.parent.toRealPath(node_dataUtility.combinePath(this.prv_relativeWrapperInfo.path, opService.parms.path)) ;
							out = this.prv_relativeWrapperInfo.parent.getDataOperationRequest(opService, handlers, requester_parent);
						}
					}
				}
				
				//logging wrapper operation
				out.setRequestProcessors({
					success : function(requestInfo, data){
						nosliw.logging.info("************************  wrapper operation   ************************");
						nosliw.logging.info("Command: " + requestInfo.getService().command);
						nosliw.logging.info("ID: " + that.prv_id);
						nosliw.logging.info("Parent: " , ((that.prv_relativeWrapperInfo==undefined)?"":that.prv_relativeWrapperInfo.parent.prv_id));
						nosliw.logging.info("ParentPath: " , ((that.prv_relativeWrapperInfo==undefined)?"":that.prv_relativeWrapperInfo.path)); 
						nosliw.logging.info("Request: " , node_basicUtility.stringify(operationService));
						nosliw.logging.info("Result: " , node_basicUtility.stringify(data));
						nosliw.logging.info("***************************************************************");
						return data;
					}
				});
				return out;
			},

			getDataType : function(){  return this.prv_dataType;   },
			getDataTypeHelper : function(){  return this.prv_typeHelper;   },
			
			/*
			 * handler : function (event, path, operationValue, requestInfo)
			 */
			registerDataOperationEventListener : function(listenerEventObj, handler, thisContext){		this.prv_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			registerLifecycleEventListener : function(listenerEventObj, handler, thisContext){		this.prv_lifecycleEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			registerInternalEventListener : function(listenerEventObj, handler, thisContext){		this.prv_internalEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			registerDataChangeEventListener : function(listenerEventObj, handler, thisContext){		this.prv_dataChangeEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);		},
			unregisterDataOperationEventListener : function(listenerEventObj){		this.prv_dataOperationEventObject.unregister(listenerEventObj);		},
			unregisterLifecycleEventListener : function(listenerEventObj){		this.prv_lifecycleEventObject.unregister(listenerEventObj);		},
			unregisterInternalEventListener : function(listenerEventObj){		this.prv_internalEventObject.unregister(listenerEventObj);		},
			unregisterDataChangeEventListener : function(listenerEventObj){		this.prv_dataChangeEventObject.unregister(listenerEventObj);		},
			
			createChildWrapper : function(path, request){		return node_wrapperFactory.createWrapper(this, path, request);		},
			
			//path conversion using path adapter
			setPathAdapter : function(pathAdapter){	this.prv_pathAdapter = pathAdapter;		},
			toRealPath : function(path){	return loc_out.prv_pathAdapter!=undefined ? this.prv_pathAdapter.toRealPath(path) : path;	},
			toAdapteredPath : function(path){	return loc_out.prv_pathAdapter!=undefined ? this.prv_pathAdapter.toAdapteredPath(path) : path;		},
			
			setValueAdapter : function(valueAdapter){  this.prv_valueAdapter = valueAdapter;  },

			setEventAdapter : function(eventAdapter){  this.prv_eventAdapter = eventAdapter;  },
			
			destroy : function(requestInfo){		loc_destroy(requestInfo);		},
			
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER);
	
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(parm1, path, typeHelper, dataType);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.EventInfo", function(){node_EventInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.createWrapperOrderedContainer", function(){node_createWrapperOrderedContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.entity.RelativeEntityInfo", function(){node_RelativeEntityInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createWraperCommon", node_createWraperCommon); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_basicUtility;
var node_parseSegment;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_utility = {
	
		/*
		 * get attribute value according to the path
		 */
		getObjectAttributeByPath : function(obj, prop) {
			if(obj==undefined)  return;
			if(prop==undefined || prop=='')  return obj;
			
		    var parts = prop.split('.'),
		        last = parts.pop(),
		        l = parts.length,
		        i = 1,
		        current = parts[0];

		    if(current==undefined)  return obj[last];
		    
		    while((obj = obj[current]) && i < l) {
		        current = parts[i];
		        i++;
		    }

		    if(obj) {
		        return obj[last];
		    }
		},

		getObjectAttributeByPathSegs : function(obj, propSegs) {
			if(obj==undefined)  return;
			if(propSegs==undefined || propSegs.length==0)  return obj;
			
		    var parts = propSegs,
		        last = parts.pop(),
		        l = parts.length,
		        i = 1,
		        current = parts[0];

		    if(current==undefined)  return obj[last];
		    
		    while((obj = obj[current]) && i < l) {
		        current = parts[i];
		        i++;
		    }

		    if(obj) {
		        return obj[last];
		    }
		},

		/*
		 * do operation on object
		 * 		obj : root object
		 * 		prop : path from root object
		 * 		command : what to do
		 * 		data : data for command
		 */
		operateObject : function(obj, prop, command, data){
			var baseObj = obj;
			var attribute = prop;
			
			if(node_basicUtility.isStringEmpty(prop)){
				baseObj = obj;
			}
			else if(prop.indexOf('.')==-1){
				baseObj = obj;
				attribute = prop;
			}
			else{
				var segs = node_parseSegment(prop);
				var size = segs.getSegmentSize();
				for(var i=0; i<size-1; i++){
					var attr = segs.next();
					var obj = baseObj[attr];
					if(obj==undefined){
						obj = {};
						baseObj[attr] = obj; 
					}
					baseObj = obj;
				}
				attribute = segs.next();
			}
			
			if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
				baseObj[attribute] = data;
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
				//if container does not exist, then create a map
				if(baseObj[attribute]==undefined)  baseObj[attribute] = {};
				if(data.index!=undefined){
					baseObj[attribute][data.index]=data.data;
				}
				else{
					//if index is not specified, for array, just append it
					if(_.isArray(baseObj[attribute])){
						baseObj[attribute].push(data.data);
					}
				}
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
				delete baseObj[attribute][data];
			}			
		},

		operateObjectByPathSegs : function(obj, pathSegs, command, data){
			var baseObj = obj;
			var attribute = "";
			
			if(pathSegs==undefined || pathSegs.length==0){
				baseObj = obj;
			}
			else if(pathSegs.length==1){
				baseObj = obj;
				attribute = pathSegs[0];
			}
			else{
				var segs = pathSegs;
				var size = segs.getSegmentSize();
				for(var i=0; i<size-1; i++){
					var attr = segs.next();
					var obj = baseObj[attr];
					if(obj==undefined){
						obj = {};
						baseObj[attr] = obj; 
					}
					baseObj = obj;
				}
				attribute = segs.next();
			}
			
			if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
				baseObj[attribute] = data;
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
				//if container does not exist, then create a map
				if(baseObj[attribute]==undefined)  baseObj[attribute] = {};
				if(data.index!=undefined){
					baseObj[attribute][data.index]=data.data;
				}
				else{
					//if index is not specified, for array, just append it
					if(_.isArray(baseObj[attribute])){
						baseObj[attribute].push(data.data);
					}
				}
			}
			else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
				delete baseObj[attribute][data];
			}			
		},


};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.segmentparser.parseSegment", function(){node_parseSegment = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("remote");
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/*
 * setting object for remote service task 
 */
var node_RemoteServiceSetting = function(mode){
	this.mode = mode;
};

/*
 * remote service request object (for ajax)
 */
var node_RemoteServiceRequest = function(serviceTask){
	//id of remote task
	this.id = nosliw.runtime.getIdService().generateId();
	//task id
	this.taskId = serviceTask.id;
	//type of request: group or normal
	this.type = serviceTask.type;
	//service : command and parms
	this.service = serviceTask.service;
	//children request for group request
	this.children = [];
	//unique id within nosliw client
	this.requestId = serviceTask.requestId;
	
	//build children request for group request
	if(this.type==node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP){
		for(var i in serviceTask.children){
			this.children.push(new node_RemoteServiceRequest(serviceTask.children[i]));
		}
	}
};

/*
 * remote request object --- normal
 */
var node_RemoteServiceTask = function(syncName, service, handlers, requestInfo, setting){
	//unique id
	this.id = nosliw.runtime.getIdService().generateId();
	//normal task
	this[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_REQUEST_TYPE] = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_NORMAL;
	//sync task name, different sync name have different configuration
	this.syncName = syncName;
	//parent requestInfo
	this.requestInfo = requestInfo;
	//service : command and parms
	this.service = service;
	//
	this.setting = setting;
	//handlers
	this.handlers = handlers;
	this.infos = {};
	//unique id within nosliw client
	this.requestId = nosliw.generateId();
	
	//used for ajax request
	this.remoteRequest = new node_RemoteServiceRequest(this); 
};

node_RemoteServiceTask.prototype = {
	getRemoteServiceRequest : function(){
		return this.remoteRequest;
	},
};


/*
 * a group of requests
 * 
 */
var node_RemoteServiceGroupTask = function(syncName, handlers, requestInfo, setting){
	//unique id
	this.id = nosliw.runtime.getIdService().generateId();
	//task type : group
	this[node_COMMONATRIBUTECONSTANT.SERVICESERVLET_REQUEST_TYPE] = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP;
	//sync task name, different sync name have different configuration
	this.syncName = syncName;
	//parent requestInfo
	this.requestInfo = requestInfo;
	//
	this.setting = setting;
	//children remote service task
	this.children = [];
	//handlers
	this.handlers = handlers;
	this.metaData = {};
	this.infos = {	};
	//unique id within nosliw client
	this.requestId = nosliw.generateId();

	//used for ajax request
	this.remoteRequest = new node_RemoteServiceRequest(this); 
};

node_RemoteServiceGroupTask.prototype = {
	addTask : function(task){
		//mark task as group child
		task.type = node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUPCHILD;
		this.children.push(task);
		this.remoteRequest.children.push(task.getRemoteServiceRequest());
	},
	
	setMetaData : function(name, data){
		this.metaData[name] = data;
	},
	
	getMetaData : function(name){
		return this.metaData[name];
	},
	
	getRemoteServiceRequest : function(){
		return this.remoteRequest;
	},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("RemoteServiceSetting", node_RemoteServiceSetting); 
packageObj.createChildNode("RemoteServiceRequest", node_RemoteServiceRequest); 
packageObj.createChildNode("RemoteServiceTask", node_RemoteServiceTask); 
packageObj.createChildNode("RemoteServiceGroupTask", node_RemoteServiceGroupTask); 

})(packageObj);

//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_COMMONCONSTANT;
var node_ServiceData;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_errorUtility = function(){
	return {
		/*
		 * exception service data for suspended reason
		 */
		createRemoteServiceSuspendedServiceData : function(reason){
			return new node_ServiceData(
					node_COMMONCONSTANT.ERRORCODE_REMOTESERVICE_SUSPEND, 
					'No service call allowed this time, please try later!!',
					reason); 
		},
		
		/*
		 * exception service data for ajax call error
		 */
		createRemoteServiceExceptionServiceData : function(obj, textStatus, errorThrown){
			var serviceData = new node_ServiceData(
					node_COMMONCONSTANT.ERRORCODE_REMOTESERVICE_EXCEPTION,
				textStatus+'-'+errorThrown
			);
			return serviceData;
		},
	};
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("error.entity.ServiceData", function(){node_ServiceData = this.getData();});

//Register Node by Name
packageObj.createChildNode("errorUtility", node_errorUtility); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_createConfiguresBase;
var node_remoteServiceUtility;
var node_RemoteServiceTask;
var node_createRemoteSyncTask;
var node_COMMONCONSTANT;
var node_CONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_makeObjectWithLifecycle;
//*******************************************   Start Node Definition  ************************************** 	


/*
 * handle all the remote service
 *  	network detection
 *  	remote call
 * this manager has four status: 
 * 		start:		not init yet 
 * 		active:		running receiving request
 * 		suspend:	when exception happened, the system is turned into suspend, and will not process any request
 * 		dead:
 *  for the user of remote service manager, it should listen to the manager status's transit event, 
 *  and send request only when the service manager is in active status
 *  request on any other status will not be process: 
 *  	suspend:	exeption handler will be called
 *  	others:   	ignored 
 */
var node_createRemoteService = function(){
	var loc_moduleName = "remoteService";

	//store all sync tasks by name
	var loc_tasks = {};
	
	//store the reason why this manager is suspended
	var loc_suspendReason = undefined;

	//a timed processor that triggered every 3 second
	var loc_timerProcessor = undefined;
	
	//predefined sync task configure, so that we don't need to create it everytime, just get it by name
	var loc_syncTaskConfigures = {};

	//default setting / base setting for synTask
	var loc_syncTaskBaseConfigure = function(){
		var setting = {
			type : "POST",
			dataType: "json",
			async : true,
		};
		setting[node_COMMONATRIBUTECONSTANT.REQUESTINFO_COMMAND] = node_COMMONCONSTANT.SERVICECOMMAND_GROUPREQUEST;
		return node_createConfiguresBase(setting);
	}();

	
	/*
	 * add one service task to system
	 * put task into queue/queue status, but do not process them in this function
	 */
	function loc_addServiceTask(serviceTask){
		//sync name for this task
		var syncName = serviceTask.syncName;
		
		//if sync tasks for this service task is not exist, then create one 
		var syncTasks = loc_tasks[syncName];
		if(syncTasks==undefined){
			
			var configureName = loc_getConfigureName(syncName);
			var syncTaksConfigure = loc_getSyncTaskConfiguresByName(configureName);
			if(syncTaksConfigure==undefined){
				syncTaksConfigure = loc_syncTaskBaseConfigure.getBaseConfigures();
			}
			
			syncTasks = node_createRemoteSyncTask(syncName, loc_out, syncTaksConfigure);
			loc_tasks[syncName] = syncTasks;
		}
		syncTasks.addTask(serviceTask);
	};

	/*
	 * get sync task configure by name
	 */
	function loc_getSyncTaskConfiguresByName(name){
		if(name==undefined)  return undefined;
		return loc_syncTaskConfigures[name];
	};
	
	/*
	 * get configure name from sync name
	 * sync name is in the format of :   configure name : real name
	 */
	function loc_getConfigureName(syncName){
		var out = syncName;
		var index = syncName.indexOf(node_COMMONCONSTANT.SEPERATOR_PART);
		if(index!=-1){
			out = syncName.subString(index+1);
		}
		return out;
	};
	
	/*
	 * process all tasks
	 */
	function loc_processTasks(){
		var emptyTasks = [];
		_.each(loc_tasks, function(syncTask, name, list){
			if(syncTask.isEmpty()==false){
				syncTask.processTasks();
			}
			else{
				//if sync task is empty, remove it
				emptyTasks.push(syncTask);
			}
		});
		
		for(var i in emptyTasks){
			delete loc_tasks[emptyTasks[i].name];
		}
	}
	
	/*
	 * this is a timed function to do task every 3 second
	 */
	var loc_timerProcess = function(){
		var status = loc_out.getResourceStatus();
		if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
			//try to process all tasks
			loc_processTasks();
		}
		else if(status==NOSLIWCONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
			//check recover
			loc_out.resume();
		}		
	};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		/*
		 * register predefined sync task configure
		 */
		registerSyncTaskConfigure : function(name, configure){
			var newConfigure = loc_syncTaskBaseConfigure.createConfigures(configure);
			newConfigure.setConfigure("url", nosliw.serverBase+newConfigure.getConfigure("url"));
			loc_syncTaskConfigures[name] = newConfigure;
		},
		
		/*
		 * add a service task or array of service task
		 */
		addServiceTask : function(serviceTask){
			var status = this.interfaceObjectLifecycle.getResourceStatus();
			if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
				//if in active status, then process them
				if(_.isArray(serviceTask)==true){
					for(var i in serviceTask){
						loc_addServiceTask(serviceTask[i]);
					}
				}
				else{
					loc_addServiceTask(serviceTask);
				}
				
				//process all tasks (sync and async)
				loc_processTasks();
			}
			else if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
				//if in suspend status, not accept any service task
				//inform outsider through exception handler
				var serviceData = nosliwRemoteServiceErrorUtility.createRemoteServiceSuspendedServiceData(loc_suspendReason);
				loc_handleServiceResult(serviceTask, serviceData);
			}
			return serviceTask;
		},
		
		removeServiceTask : function(id){},
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
//		loc_timerProcessor = setInterval(loc_timerProcess, 3000);
	};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND] = function(){
	};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME] = function(){
	};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		clearInterval(loc_timerProcessor);
	};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE] = function(){
	};
	
	//append resource life cycle method to out obj
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_moduleName);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfiguresBase", function(){node_createConfiguresBase = this.getData();});
nosliw.registerSetNodeDataEvent("remote.utility", function(){node_remoteServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.createRemoteSyncTask", function(){node_createRemoteSyncTask = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRemoteService", node_createRemoteService); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_COMMONATRIBUTECONSTANT;
var node_COMMONCONSTANT;
var node_CONSTANT;
var node_remoteServiceErrorUtility;
var node_remoteServiceUtility;
var node_errorUtility;
var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	


/**
 * 
 */
var node_createRemoteSyncTask = function(name, remoteServiceMan, setting){
	var loc_moduleName = "syncTask";
	
	//reference to remote service manager obj
	var loc_remoteServiceMan = remoteServiceMan;
	
	//name of this sync task
	var loc_name = name;

	//setting for this sync task
	var loc_setting = setting;
	
	//store all sync tasks that have been processed
	var loc_syncTasks = [];
	//store all sync tasks that have not been processed
	var loc_syncTaskQueue = [];
	//flag whether sync tasks in queue is ready for process
	var loc_syncReady = true;
	
	/*
	 * process all sync tasks in task array
	 * 		service : url service
	 * 		command : command to use
	 */
	var loc_syncRemoteCall = function(){
		//set flag so that no new processing allowed before this function is finished
		loc_syncReady = false;

		//id for this remote call
		loc_id = nosliw.runtime.getIdService().generateId();
		
		//prepare remote request and do ajax call
		nosliw.logging.info("************************  Remote AJAX Request   ************************");
		nosliw.logging.info("Syntask: " + loc_name);
		nosliw.logging.info("Remote call Id: " + loc_id);
		
		var serviceTaskRequests = [];
		for(var i in loc_syncTasks){
			var remoteRequest = loc_syncTasks[i].getRemoteServiceRequest();
			serviceTaskRequests.push(remoteRequest);
			nosliw.logging.info("Task ID: " + loc_syncTasks[i].id);
		}
		nosliw.logging.info("***************************************************************");
		
		var remoteRequestData = {};
//		remoteRequestData[node_COMMONATRIBUTECONSTANT.REQUESTINFO_COMMAND_CLIENTID] = nosliw.getClientId();
		remoteRequestData[node_COMMONATRIBUTECONSTANT.REQUESTINFO_COMMAND] = loc_setting.getConfigure(node_COMMONATRIBUTECONSTANT.REQUESTINFO_COMMAND);
		remoteRequestData[node_COMMONATRIBUTECONSTANT.REQUESTINFO_PARMS] = node_basicUtility.stringify(serviceTaskRequests);
	
		$.ajax(_.extend({
			data : remoteRequestData,
			success : function(serviceDataResult, status){
				var syncTasks = loc_syncTasks;
				//clear tasks
				loc_syncTasks = [];
				//ready to process new task
				loc_syncReady = true;

				if(node_errorUtility.isSuccess(serviceDataResult)==true){
					var serviceDatas = serviceDataResult.data;
					//processed normally
					for(var j in serviceDatas){
						var serviceData = serviceDatas[j];
						var task = syncTasks[j];
						var taskType = task.type;

						if(taskType==node_COMMONCONSTANT.REMOTESERVICE_TASKTYPE_GROUP){
							//for group task, handle child task first
							for(var k in task.children)		loc_handleServiceResult(task.children[k], serviceData.data[k]);
						}
						//handle task
						loc_handleServiceResult(task, serviceData);
					}
				}
				else{
					nosliw.logging.error(loc_moduleName, loc_name, "System error when processing tasks in snycTask :", serviceDataResult);
					loc_processSyncRequestSystemError(serviceDataResult);
				}
				
				nosliw.logging.info("************************  Remote AJAX Request Finish  ************************");
				nosliw.logging.info("Syntask: " + loc_name);
				nosliw.logging.info("Remote call Id: " + loc_id);
				nosliw.logging.info("Syn status: " + loc_syncReady);
				nosliw.logging.info("***************************************************************");
				
				//process sync task again
				loc_processTasks();

			},
			error: function(obj, textStatus, errorThrown){
				nosliw.logging.error(loc_moduleName, loc_name, "Exception when processing tasks in snycTask ", textStatus, errorThrown);

				//when ajax error happened, which may be caused by network error, server is down or server internal error
				//remote service manager is put into suspend status
				//the service request is not removed
				var serviceData = node_remoteServiceErrorUtility.createRemoteServiceExceptionServiceData(obj, textStatus, errorThrown); 
				
				node_remoteServiceUtility.handleServiceTask(loc_syncTasks, function(serviceTask){
					loc_handleServiceResult(serviceTask, serviceData);
					serviceTask.status = node_CONSTANT.REMOTESERVICE_SERVICESTATUS_FAIL;
				});
				
				//suspend the system
				loc_remoteServiceMan.interfaceObjectLifecycle.suspend();
				//finish processing, so that ready to process again
				loc_syncReady = true;
			},
		}, loc_setting.getConfiguresObject()));
	};
	
	/*
	 * process the system error (invalid client id, ...) 
	 */
	var loc_processSyncRequestSystemError = function(serviceData){
		
	};
	
	/*
	 * call the responding handler according to service data
	 */
	var loc_handleServiceResult = function(serviceTask, serviceData){
		var resultStatus = node_errorUtility.getServiceDataStatus(serviceData);
		switch(resultStatus){
		case node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS:
			nosliw.logging.info(loc_moduleName, loc_name, serviceTask.requestId, "Success handler");
			nosliw.logging.trace(loc_moduleName, loc_name, serviceTask.requestId, "Data", serviceData.data);
			return node_remoteServiceUtility.executeServiceTaskSuccessHandler(serviceTask, serviceData.data, serviceTask);
			break;
		case node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION:
			nosliw.logging.error(loc_moduleName, loc_name, serviceTask.requestId, "Error handler with data", serviceData);
			return node_remoteServiceUtility.executeServiceTaskExceptionHandler(serviceTask, serviceData, serviceTask);
			break;
		case node_CONSTANT.REMOTESERVICE_RESULT_ERROR:
			nosliw.logging.error(loc_moduleName, loc_name, serviceTask.requestId, "Exception handler with data", serviceData);
			return node_remoteServiceUtility.executeServiceTaskErrorHandler(serviceTask, serviceData, serviceTask);
			break;
		}
	};
	
	/*
	 * process all tasks
	 */
	var loc_processTasks = function(){
		if(loc_syncReady==true){
			//if ready to process
			if(loc_isEmpty()==false){
				nosliw.logging.info(loc_moduleName, loc_name, "Start remote processing tasks in snycTask ");
				//if not empty 
				//put all tasks in queue into tasks array and process all tasks in it
				for(var i in loc_syncTaskQueue){
					loc_syncTasks.push(loc_syncTaskQueue[i]);
				}
				loc_syncTaskQueue = [];
				loc_syncRemoteCall();
			}
		}
	};
	
	var loc_isEmpty = function(){
		var out = false;
		if(loc_syncTaskQueue.length==0){
			if(loc_syncTasks.length==0){
				out = true;
			}
		}
		return out;
	};
	
	var loc_out = {
		/*
		 * add a new task
		 */
		addTask : function(task){	
			loc_syncTaskQueue.push(task);	
			nosliw.logging.info(loc_moduleName,  task.requestId, "New remote task is added to sync task ", loc_name, task.id, task.service.command, ":", node_basicUtility.stringify(task.service.parms));
//			this.logSyncTask();
		},
		
		logSyncTask : function(){
			nosliw.logging.trace(loc_moduleName, loc_name, "***********************  info start ***********************" );
			nosliw.logging.trace(loc_moduleName, loc_name, "info", "		"+"task:"+loc_syncTasks.length +"  queue:"+loc_syncTaskQueue.length);
			nosliw.logging.trace(loc_moduleName, loc_name, "tasks", "		");
			nosliw.logging.trace(loc_moduleName, loc_name, "queue", "		");
			for(var i in loc_syncTaskQueue){
				nosliw.logging.trace(loc_moduleName, loc_name, "		", loc_syncTaskQueue[i].requestId, node_basicUtility.stringify(loc_syncTaskQueue[i].service));
			}
			nosliw.logging.trace(loc_moduleName, loc_name, "*********************** info end ***********************" );
		},
	
		/*
		 * process all tasks
		 */
		processTasks : function(){	loc_processTasks();		},
		
		isEmpty : function(){	return loc_isEmpty();	},
		
		isReady : function(){  return loc_syncReady; },
		
		clear : function(){
			loc_syncTaskQueue = [];
			loc_syncTasks = [];
		},
		
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.errorUtility", function(){node_remoteServiceErrorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("remote.utility", function(){node_remoteServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRemoteSyncTask", node_createRemoteSyncTask); 

})(packageObj);

//get/create package
var packageObj = library;    

(function(packageObj){
//get used node
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_createConfigures;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	return {
		/*
		 * data is serviceData.data 
		 */
		executeServiceTaskSuccessHandler : function(serviceTask, data, thisContext){
			var handler = serviceTask.handlers.success;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, data);
			}
		},

		/*
		 * data is service data 
		 */
		executeServiceTaskErrorHandler : function(serviceTask, serviceData, thisContext){
			var handler = serviceTask.handlers.error;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, serviceData);
			}
		},

		/*
		 * data is service data 
		 */
		executeServiceTaskExceptionHandler : function(serviceTask, serviceData, thisContext){
			var handler = serviceTask.handlers.exception;
			if(handler!=undefined){
				return handler.call(thisContext, serviceTask, serviceData);
			}
		},

		/*
		 * check if a service task is a group task
		 */
		isGroupServiceTask : function(serviceTask){
			if(serviceTask.type==node_CONSTANT.REMOTESERVICE_TASKTYPE_GROUP)  return true;
			return false;
		},
		
		/*
		 * process service task by handler function
		 * serviceTask:   a task or array of task
		 */
		handleServiceTask : function(serviceTask, handler){
			var tasks = [];
			if(_.isArray(serviceTask))  tasks = serviceTask;
			else tasks.push(serviceTask);
			
			for(var i in tasks){
				var task = tasks[i];
				handler.call(task, task);
				if(this.isGroupServiceTask(task)){
					for(var j in task.children){
						handleServiceTask(task.children[j], handler);
					}
				}
			}
		},
		
	};
}();

//*******************************************   End Node Definition  ************************************** 	

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});


//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("error");
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
//*******************************************   Start Node Definition  ************************************** 	

var node_ServiceData = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
};

var node_Error = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
	node_makeObjectWithType(this, node_CONSTANT.TYPEDOBJECT_TYPE_ERROR);
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});


//Register Node by Name
packageObj.createChildNode("ServiceData", node_ServiceData); 

})(packageObj);

/**
 * 
 */
var nosliwErrorManager = function(){
	
	var m_createServiceData = function(code, message, data){
		return {
			'code' : code,
			'message' : message,
			'data' : data,
		};
	};

	var m_init = function(){
		_.extend(m_manager, Backbone.Events);
	};
	
	var m_manager = {
		CODE_SUCCESS : 200,
		CODE_FAILURE : 400,

		CODE_ERROR : 800,
		CODE_INVALID_ATTRIBUTE : 8001,

		CODE_ERROR_UNCOVER : 1000,
		CODE_ERROR_NETWORK : 1010,
		CODE_ERROR_SERVER : 1020,

		CODE_ERROR_SERVICECLOSE : 1001,
		
			
		clearErrorMessage : function(reqInfo){
			m_manager.trigger('error', "clear", reqInfo);
			m_manager.trigger('reqId:'+reqInfo.eleId, "clear", reqInfo);
		},
		
		createErrorMessage : function(reqInfo, serviceData){
			var errorHandler = reqInfo.errorHandler;
			var errorCodes = reqInfo.errorCodes;
			var handleByRequester = true;
			
			if(errorHandler==undefined)   handleByRequester = false;
			
			var triggerEvent = true;
			if(handleByRequester==true){
				//have error handler
				if(errorHandler=='none')   triggerEvent = false;
				else triggerEvent = reqInfo.parentResouce.callFunction(errorHandler, reqInfo, serviceData);
			}
			
			if(triggerEvent==true){
				m_manager.trigger('error', "new", reqInfo, serviceData);
				m_manager.trigger('reqId:'+reqInfo.eleId, "new", reqInfo, serviceData);
			}
		},
		
		createValidationFailureServiceData : function(wraper, rule, value){
			var serviceData = {
				code : 	this.CODE_INVALID_ATTRIBUTE,
				message : rule.errorMsg,
				data : {
					wraper : wraper,
					rule : rule,
				},
			};
			return serviceData;
		},
	};
	
	m_init();
	
	return m_manager;
}();
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	var loc_out = {
		
			isSuccess : function(serviceData){
				if(serviceData==undefined)  return true;
				if(node_COMMONCONSTANT.SERVICECODE_FAILURE > serviceData.code)  return true;
				else return false;
			},
		
			isFail : function(serviceData){
				if(this.isSuccess(serviceData))  return false;
				else return true;
			},

			createSuccessServiceData : function(){
				var serviceData = {
					code : 	node_COMMONCONSTANT.SERVICECODE_FAILURE,
					message : "",
					data : {
					},
				};
				return serviceData;
			},

			createErrorServiceData : function(){
				var serviceData = {
					code : 	node_COMMONCONSTANT.SERVICECODE_FAILURE,
					message : "",
					data : {
					},
				};
				return serviceData;
			},
			
			/*
			 * get result status from serviceData: success, exception, error
			 */
			getServiceDataStatus : function(serviceData){
				if(this.isSuccess(serviceData)){
					return node_CONSTANT.REMOTESERVICE_RESULT_SUCCESS;
				}
				else{
					var code = serviceData.code;
					if(code>=node_COMMONCONSTANT.SERVICECODE_EXCEPTION){
						return node_CONSTANT.REMOTESERVICE_RESULT_EXCEPTION;
					}
					else{
						return node_CONSTANT.REMOTESERVICE_RESULT_ERROR;
					}
				}
			},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("runtime");
var library = nosliw.getPackage("runtime");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createConfigures;
	var node_buildServiceProvider;
	var node_requestUtility;
	var node_createServiceRequestInfoExecutor;
	var node_requestServiceProcessor;
	var node_ServiceInfo;
	var node_ServiceRequestExecuteInfo;
	var node_COMMONATRIBUTECONSTANT;
	var node_RemoteServiceTask;
	var node_createServiceRequestInfoRemote;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_createServiceRequestInfoCommon;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createGatewayService = function(){
	
	var loc_configureName = "gateway";
	
	var loc_getGatewayObject = function(){
		return nosliw.getNodeData(node_COMMONATRIBUTECONSTANT.RUNTIME_NODENAME_GATEWAY);
	};
	
	nosliw.registerSetNodeDataEvent("runtime", function(){
		//register remote task configure
		var configure = node_createConfigures({
			url : loc_configureName,
//			contentType: "application/json; charset=utf-8"
		});
		
		nosliw.runtime.getRemoteService().registerSyncTaskConfigure(loc_configureName, configure);
	});
	
	//load file to html page to execute it
	var loc_getLoadFileRequest = function(fileName, handlers, requester_parent){
		var out = node_createServiceRequestInfoCommon(new node_ServiceInfo("LoadResourceFile", {"fileName":fileName}), handlers, requester_parent);		
		out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(requestInfo){
			var script = document.createElement('script');
			script.setAttribute('src', requestInfo.getService().parms.fileName);
			script.setAttribute('type', 'text/javascript');
			script.onload = function(){
				requestInfo.executeSuccessHandler(undefined, requestInfo);
			};
			document.getElementsByTagName("head")[0].appendChild(script);
		}, out));

		return out;
	};
	
	var loc_getLoadScriptRequest = function(dataStr, handlers, requester_parent){
		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("LoadScript", {"dataStr":dataStr}), 
				function(requestInfo){  
					eval(requestInfo.getService().parms.dataStr);  
				}, 
				handlers, requester_parent);
		return out;
	};
	
	var loc_out = {

			getExecuteGatewayCommandRequest : function(gatewayId, command, parms, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RequestGatewayService", {"gatewayId":gatewayId,"command":command,"parms":parms}), handlers, requestInfo);

				var gatewayRemoteServiceRequest = node_createServiceRequestInfoRemote(loc_configureName, new node_ServiceInfo(gatewayId+";"+command, parms), undefined, {
					success : function(requestInfo, gatewayOutput){
						var gatewayOutputData = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_DATA];
						out.setData(gatewayOutputData, "gatewayOutputData");
						var gatewayOutputScripts = gatewayOutput[node_COMMONATRIBUTECONSTANT.GATEWAYOUTPUT_SCRIPTS];
						var requests = [];
						//process script info output 
						_.each(gatewayOutputScripts, function(scriptInfo, i, list){
							var file = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_FILE];
							var script = scriptInfo[node_COMMONATRIBUTECONSTANT.JSSCRIPTINFO_SCRIPT];
							if(file!=undefined)		requests.push(loc_getLoadFileRequest(file));
							if(script!=undefined)		requests.push(loc_getLoadScriptRequest(script));
						});
						
						requests.push( node_createServiceRequestInfoSimple({}, function(request){  return out.getData("gatewayOutputData")})  );
						
						if(requests.length>0)  return requests;
					}
				});
				out.addRequest(gatewayRemoteServiceRequest);
				
//				out.addPostProcessor({
//					success : function(requestInfo){
//						return out.getData("gatewayOutputData");
//					}
//				});
				return out;
			},	
			
		executeExecuteGatewayCommandRequest : function(gatewayId, command, parms, handlers, requester_parent){
			var requestInfo = this.getExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		//execute command directly, no callback needed
		executeGatewayCommand : function(gatewayId, command, parms){
			var gatewayObject = loc_getGatewayObject();
			gatewayObject.executeGateway(gatewayId, command, parms);
		}
		
	};
	
	nosliw.createNode(node_COMMONATRIBUTECONSTANT.RUNTIME_NODENAME_GATEWAY, {
		executeGateway : function(gatewayId, command, parms){
			
		}
	});
	
	loc_out = node_buildServiceProvider(loc_out, "gatewayService");
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.setting.createConfigures", function(){node_createConfigures = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoExecutor", function(){node_createServiceRequestInfoExecutor = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("remote.entity.RemoteServiceTask", function(){node_RemoteServiceTask = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoRemote", function(){node_createServiceRequestInfoRemote = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("createGatewayService", node_createGatewayService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createIdService;
	var node_createLoggingService;
	var node_createResourceManager;
	var node_createResourceService;
	var node_createExpressionService;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_createRemoteService;
	var node_createGatewayService;
	var node_createMiniAppService;
	var node_createProcessRuntimeFactory;
	var node_createDataService;
	var node_createUIPageService;
	var node_createUIModuleService;
	var node_createUIAppService;
	var node_createVariableManager;
	var node_createRequestServiceProcessor;
//*******************************************   Start Node Definition  ************************************** 	

	var loc_mduleName = "runtime";
	
/**
 * 
 */
nosliw.createNode("runtime.name", "browser");

var node_createRuntime = function(name){
	
	var loc_name = name;
	
	var loc_idService;
	
	var loc_resourceService;
	
	var loc_resourceManager;
	
	var loc_expressionService;
	
	var loc_gatewayService;

	var loc_processRuntimeFactory;
	
	var loc_remoteService;
	
	var loc_dataService;
	
	var loc_uiPageService;

	var loc_uiVariableManager;
	
	var loc_uiModuleService;

	var loc_uiAppService;

	var loc_requestProcessor;
	
	var loc_securityService;
	

	var loc_out = {
		
		start : function(){	},
		
		getName(){			return loc_name;		},
		
		getIdService(){		return loc_idService;		},
		
		getResourceService(){			return loc_resourceService;		},
		
		getExpressionService(){			return loc_expressionService;		},
			
		getGatewayService(){		return loc_gatewayService;		},
		
		getRemoteService(){			return loc_remoteService;		},

		getDataService(){   return loc_dataService;   },

		getProcessRuntimeFactory(){   return loc_processRuntimeFactory;  },
		
		getUIPageService(){		return loc_uiPageService;		},
		
		getUIVariableManager(){   return loc_uiVariableManager;   },

		getUIModuleService(){   return loc_uiModuleService; },

		getUIAppService(){   return loc_uiAppService; },

		getRequestProcessor(){   return  loc_requestProcessor;  },

		getSecurityService(){  return  loc_securityService;  },
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = node_createIdService();
		loc_resourceManager = node_createResourceManager();
		loc_resourceService = node_createResourceService(loc_resourceManager);
		loc_expressionService = node_createExpressionService();
		if(node_createRemoteService!=undefined)		loc_remoteService = node_createRemoteService();
		loc_remoteService.interfaceObjectLifecycle.init();
		loc_gatewayService = node_createGatewayService();
		if(node_createUIPageService!=undefined)  loc_uiPageService = node_createUIPageService();
		loc_processRuntimeFactory = node_createProcessRuntimeFactory();
		loc_dataService = node_createDataService();
		loc_securityService = node_createSecurityService();
		
		loc_uiModuleService = node_createUIModuleService();
		loc_uiAppService = node_createUIAppService();
		loc_uiVariableManager = node_createVariableManager();

		loc_requestProcessor = node_createRequestServiceProcessor();
		nosliw.createNode("request.requestServiceProcessor", loc_requestProcessor); 
		
		//set sortcut for object
		 nosliw.runtime = loc_out;
		 nosliw.generateId = loc_out.getIdService().generateId;
		 
		 //create node for runtime object
		 nosliw.createNode("runtime", loc_out);
		 nosliw.triggerNodeEvent("runtime", "active");

		return true;
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("service.idservice.createIdService", function(){node_createIdService = this.getData();});
nosliw.registerSetNodeDataEvent("resource.createResourceManager", function(){node_createResourceManager = this.getData();});
nosliw.registerSetNodeDataEvent("expression.service.createExpressionService", function(){node_createExpressionService = this.getData();});
nosliw.registerSetNodeDataEvent("resource.createResourceService", function(){node_createResourceService = this.getData();});
nosliw.registerSetNodeDataEvent("remote.createRemoteService", function(){node_createRemoteService = this.getData();});
nosliw.registerSetNodeDataEvent("runtime.createGatewayService", function(){node_createGatewayService = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.service.createMiniAppService", function(){node_createMiniAppService = this.getData();});
nosliw.registerSetNodeDataEvent("process.createProcessRuntimeFactory", function(){node_createProcessRuntimeFactory = this.getData();});
nosliw.registerSetNodeDataEvent("dataservice.createDataService", function(){node_createDataService = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIPageService", function(){node_createUIPageService = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.service.createUIModuleService", function(){node_createUIModuleService = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.service.createUIAppService", function(){node_createUIAppService = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableManager", function(){node_createVariableManager = this.getData();});
nosliw.registerSetNodeDataEvent("request.createRequestServiceProcessor", function(){ node_createRequestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("security.createSecurityService", function(){ node_createSecurityService = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRuntime", node_createRuntime); 

})(packageObj);
var library = nosliw.getPackage("uiexpression");

//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createUIResourceScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * put ui script expression together
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceEmbededScriptExpression = function(embededScriptExpression, constants, context, requestInfo){
		
		var loc_scriptExpressions = {};
		
		var loc_scriptFunction;
		
		var loc_dataEventObject = node_createEventObject();
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, constants, context, requestInfo){

			loc_scriptFunction = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTFUNCTION];
			
			_.each(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONS], function(scriptExpression, id){
				var scriptFun = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_SCRIPTEXPRESSIONSCRIPTFUNCTION][id];
				var scriptExprssionObj = node_createUIResourceScriptExpression(scriptExpression, scriptFun, constants, context, requestInfo);
				loc_scriptExpressions[id] = scriptExprssionObj;
				scriptExprssionObj.registerListener(loc_dataEventObject, function(eventName, data){
					switch(eventName){
					case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
						var result = loc_calculateResult();
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, result);
						break;
					case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, data);
						break;
					case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
						loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, data);
						break;
					}
				});
				
			});
		};
		
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){	
			_.each(loc_scriptExpressions, function(scriptExpression, id){
				scriptExpression.destroy();
			});
			loc_dataEventObject.clearup();
			loc_dataEventObject = undefined;
		};

		var loc_calculateResult = function(){
			var scriptExpressionResults = {};
			_.each(loc_scriptExpressions, function(scriptExpression, id){
				scriptExpressionResults[id] = scriptExpression.getResult();
			});
		
			return loc_scriptFunction.call(loc_out, scriptExpressionResults);
		};
		
		
		var loc_out = {

			getExecuteEmbededScriptExpressionRequest : function(handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);

				//calculate multiple script expression
				var executeMutipleScriptExpressionRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("ExecuteMutipleEmbededScriptExpression", {"scriptExpressions":loc_scriptExpressions}), {});
				_.each(loc_scriptExpressions, function(scriptExpression, id){
					var scriptExpressionRequest = scriptExpression.getExecuteScriptExpressionRequest(undefined, requestInfo);
					executeEmbededScriptExpressionRequest.addRequest(id, scriptExpressionRequest);
				});

				var executeEmbededScriptExpressionRequest = node_createServiceRequestInfoService(new node_ServiceInfo("ExecuteEmbedScriptExpression", {"script":script, "expressions":expressions, "variables":variables}), handlers, requestInfo);
				var requestDependency = new node_DependentServiceRequestInfo(executeMutipleScriptExpressionRequest, {
					success : function(requestInfo, scriptExpressionsResult){
						var scriptExpressionsData = scriptExpressionsResult.getResults();
						return loc_scriptFunction.call(loc_out, scriptExpressionsData);
					}
				});
				executeEmbededScriptExpressionRequest.setDependentService(requestDependency);
				return executeEmbededScriptExpressionRequest;
			},
				
			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				_.each(loc_scriptExpressions, function(scriptExpression, id){
					scriptExpression.refresh(requestInfo);
				});
			},
			
			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, constants, context, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceScriptExpression", function(){node_createUIResourceScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceEmbededScriptExpression", node_createUIResourceEmbededScriptExpression); 

	})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_createContextVariablesGroup;
	var node_requestServiceProcessor;
	var node_createContextVariableInfo;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation
	var node_createUIDataOperationRequest
	var node_dataUtility;

//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * script expression unit
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createUIResourceScriptExpression = function(scriptExpression, scriptFun, constants, context, requestInfo){
		
		var loc_constants = {};
		
		var loc_expressions = {};
		
		var loc_contextVarGroup;
		
		var loc_scriptFunction;
		
		//store result from last time calculation
		var loc_result;
		
		var loc_dataEventObject = node_createEventObject();
		
		var loc_contextVarsGroupHandler = function(requestInfo){
			loc_out.executeExecuteScriptExpressionRequest({
				success : function(requestInfo, data){
					loc_result = data;
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, data);
				},
				error : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, serviceData);
				},
				exception : function(requestInfo, serviceData){
					loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, serviceData);
				}
			}, requestInfo);
		}
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(scriptExpression, scriptFun, constants, context, requestInfo){
			loc_constants = constants;
			
			loc_expressions = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_EXPRESSIONS];
			
			loc_scriptFunction = scriptFun; 
//				scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_SCRIPTFUNCTION];

			var varNames = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_VARIABLENAMES];
			var contextVariables = [];
			_.each(varNames, function(varName, index){
				contextVariables.push(node_createContextVariableInfo(varName));
			});
			loc_contextVarGroup = node_createContextVariablesGroup(context, contextVariables, loc_contextVarsGroupHandler, this);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
			loc_contextVarGroup.destroy();
			loc_scriptFunction = undefined;
			loc_constants = undefined;
			loc_expressions = undefined;
		};

		var loc_out = {
				
			getExecuteScriptExpressionRequest : function(handlers, requester_parent){
				
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteScriptExpression", {}), handlers, requester_parent);
				
				var allVarValuesRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("GetAllVariableValues", {}),
					{
						success : function(request, varsValue){
							var variableParms = {};
							_.each(varsValue.getResults(), function(varData, varName){
								variableParms[varName] = node_dataUtility.getValueOfData(varData);
							});
							return nosliw.runtime.getExpressionService().getExecuteScriptRequest(loc_scriptFunction, loc_expressions, variableParms, loc_constants);
						}
					});
				//parepare variable parms
				var hasEmptyVariable = false;  //whether variable is ready
				var variables = loc_contextVarGroup.getVariables();
				_.each(variables, function(variable, varFullName){
					if(!variable.isEmpty())  hasEmptyVariable = true;
					var getVarValueRequest = node_createUIDataOperationRequest(undefined, new node_UIDataOperation(variable, node_uiDataOperationServiceUtility.createGetOperationService("")));
					allVarValuesRequest.addRequest(varFullName, getVarValueRequest);
				});
				
				if(!hasEmptyVariable)	out.addRequest(allVarValuesRequest);
				else out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){return undefined;}));

				return out;
			},

			executeExecuteScriptExpressionRequest : function(handlers, requester_parent){
				var requestInfo = this.getExecuteScriptExpressionRequest(handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			registerListener : function(listenerEventObj, handler){
				loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
			},
			
			refresh : function(requestInfo){
				loc_contextVarGroup.triggerEvent(requestInfo);
			},
			
			getResult : function(){
				return loc_result;
			},

			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(scriptExpression, scriptFun, constants, context, requestInfo);
		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariablesGroup", function(){node_createContextVariablesGroup = this.getData();});
	nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
	nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
	nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
	nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createUIResourceScriptExpression", node_createUIResourceScriptExpression); 

	})(packageObj);
var library = nosliw.getPackage("uipage");

//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface
	var node_createUIResourceEmbededScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInAttribute = function(embededScriptExpression, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_embededScriptExpression = node_createUIResourceEmbededScriptExpression(embededScriptExpression, uiResourceView.getConstants(), uiResourceView.getContext(), uiResourceView, requestInfo);

		//attribute name
		var loc_attribute = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_ATTRIBUTE];
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_UIID]);
		//element
		var loc_ele = loc_uiResourceView.prv_getLocalElementByUIId(loc_uiId);

		var loc_dataEventObject = node_createEventObject();
		
		var loc_scriptExpressionEventHandler = function(eventName, data){
			switch(eventName){
			case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
				loc_ele.attr(loc_attribute, data); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
				loc_ele.attr(loc_attribute, "[Error]"); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
				loc_ele.attr(loc_attribute, "[Exception]"); 
				break;
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){
			loc_embededScriptExpression.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);
			loc_out.refresh(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){		
			loc_dataEventObject.clearup();
			loc_embededScriptExpression.destroy();
			loc_embededScriptExpression = undefined;
			loc_uiResourceView = undefined;
			loc_ele = undefined;
			loc_uiId = undefined;
			loc_attribute = undefined;
		};

		var loc_out = {
				refresh : function(requestInfo){	loc_embededScriptExpression.refresh(requestInfo);			},
				
				destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, loc_uiResourceView, requestInfo);

		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceEmbededScriptExpression", function(){node_createUIResourceEmbededScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createEmbededScriptExpressionInAttribute", node_createEmbededScriptExpressionInAttribute); 

	})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface
	var node_createUIResourceEmbededScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInContent = function(embededScriptExpression, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_embededScriptExpression = node_createUIResourceEmbededScriptExpression(embededScriptExpression, uiResourceView.getConstants(), uiResourceView.getContext(), requestInfo);
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_UIID]);
		//element
		var loc_ele = loc_uiResourceView.prv_getLocalElementByUIId(loc_uiId);

		var loc_dataEventObject = node_createEventObject();
		
		var loc_scriptExpressionEventHandler = function(eventName, data){
			switch(eventName){
			case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
				loc_ele.text(data);
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
				loc_ele.text("[Error]");
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
				loc_ele.text("[Exception]");
				break;
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){
			loc_embededScriptExpression.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);
			loc_out.refresh(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
			loc_dataEventObject.clearup();
			loc_embededScriptExpression.destroy();
			loc_embededScriptExpression = undefined; 
			loc_uiResourceView = undefined;
			loc_uiId = undefined;
			loc_ele = undefined;
		};

		var loc_out = {
			refresh : function(requestInfo){
				loc_embededScriptExpression.refresh(requestInfo);
			},
			
			destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, loc_uiResourceView, requestInfo);

		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceEmbededScriptExpression", function(){node_createUIResourceEmbededScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createEmbededScriptExpressionInContent", node_createEmbededScriptExpressionInContent); 

	})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface
	var node_createUIResourceEmbededScriptExpression;
//*******************************************   Start Node Definition  ************************************** 	

	/*
	 * create expression content object
	 * type: 
	 * 		text, attribute, tagAttribute
	 */
	var node_createEmbededScriptExpressionInTagAttribute = function(embededScriptExpression, uiResourceView, requestInfo){
		
		//script expression definition
		var loc_embededScriptExpression = node_createUIResourceEmbededScriptExpression(embededScriptExpression, uiResourceView.getConstants(), uiResourceView.getContext(), uiResourceView, requestInfo);

		//attribute name
		var loc_attribute = embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_ATTRIBUTE];
		
		//parent resource view
		var loc_uiResourceView = uiResourceView;
		//ui id for content
		var loc_uiId = loc_uiResourceView.prv_getUpdateUIId(embededScriptExpression[node_COMMONATRIBUTECONSTANT.EMBEDEDSCRIPTEXPRESSION_UIID]);
		//tag
		var loc_tag = loc_uiResourceView.prv_getTagByUIId(loc_uiId);

		var loc_dataEventObject = node_createEventObject();
		
		var loc_scriptExpressionEventHandler = function(eventName, data){
			switch(eventName){
			case node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS:
				loc_tag.setAttribute(loc_attribute, data); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_ERROR:
				loc_tag.setAttribute(loc_attribute, "[Error]"); 
				break;
			case node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION:
				loc_tag.setAttribute(loc_attribute, "[Exception]"); 
				break;
			}
		};
		
		var lifecycleCallback = {};
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(embededScriptExpression, uiResourceView, requestInfo){
			loc_embededScriptExpression.registerListener(loc_dataEventObject, loc_scriptExpressionEventHandler);
			loc_out.refresh(requestInfo);
		};
			
		lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){		
			loc_dataEventObject.clearup();
			loc_embededScriptExpression.destroy();
			loc_embededScriptExpression = undefined;
			loc_uiResourceView = undefined;
			loc_tag = undefined;
			loc_uiId = undefined;
			loc_attribute = undefined;
		};

		var loc_out = {
				refresh : function(requestInfo){	loc_embededScriptExpression.refresh(requestInfo);			},
				
				destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		};

		//append resource and object life cycle method to out obj
		loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
		node_getLifecycleInterface(loc_out).init(embededScriptExpression, loc_uiResourceView, requestInfo);

		return loc_out;
	};

	//*******************************************   End Node Definition  ************************************** 	

	//populate dependency node data
	nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
	nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
	nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
	nosliw.registerSetNodeDataEvent("uiexpression.createUIResourceEmbededScriptExpression", function(){node_createUIResourceEmbededScriptExpression = this.getData();});

	//Register Node by Name
	packageObj.createChildNode("createEmbededScriptExpressionInTagAttribute", node_createEmbededScriptExpressionInTagAttribute); 

	})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_destroyUtil;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_contextUtility;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIDecorationsRequest = function(resourceIds, handlers, request){
	var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
	var decs = [];
	if(resourceIds!=undefined){
		var decsRequest = node_createServiceRequestInfoSequence();
		_.each(resourceIds, function(resourceId, index){
			decsRequest.addRequest(nosliw.runtime.getUIPageService().getCreateUIPageRequest(resourceId, undefined, {
				success :function(requestInfo, page){
					var decoration = loc_createDecoration(page.getUIView());
					decs.push(decoration);
				}
			}));
		});
		out.addRequest(decsRequest);
	}
	out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
		return decs;
	}));
	
	return out;
};

var loc_createDecoration = function(uiView){
	var loc_eventSource = node_createEventObject();
	var loc_eventListenerForDec = node_createEventObject();
	var loc_eventListenerForParent = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListenerForDec = node_createEventObject();
	var loc_valueChangeEventListenerForParent = node_createEventObject();
	
	var loc_childUIView;
	var loc_childView;
	
	var loc_uiView = uiView;

	//regular event
	loc_uiView.registerEventListener(loc_eventListenerForDec, function(event, eventData, requestInfo){
		loc_eventSource.triggerEvent(event, eventData, requestInfo);
	});

	//value change event
	loc_uiView.registerValueChangeEventListener(loc_valueChangeEventListenerForDec, function(event, eventData, requestInfo){
		loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
	});

	var loc_getPlaceHolderView = function(){
		var placeHolderAttrValue = loc_uiView.getAttribute(node_COMMONCONSTANT.NOSLIW_RESERVE_ATTRIBUTE_PLACEHOLDER);
		var segs = placeHolderAttrValue.split(":");
		return loc_uiView.getElementByAttributeValue(segs[0], segs[1]);
	};
	
	var loc_out = {
		
		setChild : function(child){
			
			var childDataType = node_getObjectType(child);
			if(childDataType==node_CONSTANT.TYPEDOBJECT_TYPE_UIVIEW){
				loc_childUIView = child;
				
				//insert parent view into decoration view
				loc_childUIView.appendTo(loc_getPlaceHolderView());

				//listener to event from parent
				loc_childUIView.registerEventListener(loc_eventListenerForParent, function(event, eventData, requestInfo){
					//process by decoration first
					var result = loc_uiView.command(node_COMMONCONSTANT.DECORATION_COMMAND_EVENTPROCESS, {}, requestInfo);
					if(result===false)  return false;   //if return false, then no pop up the event
					else loc_eventSource.triggerEvent(event, eventData, requestInfo);  //otherwise, pop up the event
				});

				//value change event, just pop up
				loc_childUIView.registerValueChangeEventListener(loc_valueChangeEventListenerForParent, function(event, eventData, requestInfo){
					loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);  
				});
			}
			else{
				loc_childView = $(child);
				loc_childView.appendTo(loc_getPlaceHolderView());
			}
		},	
		
		appendTo : function(ele){	loc_uiView.appendTo(ele);	},
		insertAfter : function(ele){	loc_uiView.insertAfter(ele);	},
		detachViews : function(){	loc_uiView.detachViews();  },
		getPlaceHolderView : function(){    return loc_getPlaceHolderView();  },
		
		getContextElements : function(){
			var out = {};
			out = _.extend(out, loc_uiView.getContext().prv_elements, loc_childUIView==undefined?undefined:loc_childUIView.getContextElements());
			return out;
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			out.addRequest(loc_uiView.getUpdateContextRequest(parms));
			if(loc_childUIView!=undefined) out.addRequest(loc_childUIView.getUpdateContextRequest(parms));
			return out;
		},
		
		command : function(command, parms, requestInfo){
			//decoration process command first
			var result = loc_uiView.command(node_COMMONCONSTANT.DECORATION_COMMAND_COMMANDPROCESS, 
				{
					commnad : command,
					parms : parms
				}, requestInfo);
			if(result===false)  return false;   //if command return false, then no pass to parent 
			else{
				if(loc_childUIView!=undefined){
					return loc_childUIView.command(command, parms, requestInfo);   //otherwise, let parent process this command
				}
			}
		},
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener);},

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener);},
		
		destroy : function(request){
			loc_eventSource.clearup();
			loc_eventListenerForDec.clearup();
			loc_eventListenerForParent.clearup();

			loc_valueChangeEventSource.clearup();
			loc_valueChangeEventListenerForDec.clearup();
			loc_valueChangeEventListenerForParent.clearup();
			
			node_destroyUtil(loc_uiView);
		}
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIDecorationsRequest", node_createUIDecorationsRequest); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_destroyUtil;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSimple;
	var node_contextUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIPage = function(uiView){
	
	var loc_decorations = [];
	
	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_addElement = function(ele){
		var current = loc_getCurrent();
		if(current!=undefined){
			loc_viewEventListener = loc_unregisterViewListener(current);
			ele.setChild(current);
		}
		loc_decorations.push(ele);
		loc_registerViewListener(loc_getCurrent());
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiView){
		loc_addElement(uiView);
	};	
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_valueChangeEventSource.clearup();
		loc_valueChangeEventListener.clearup();
		loc_eventSource.clearup();
		loc_eventListener.clearup();
		
		_.each(loc_decorations, function(decoration, name){
			node_destroyUtil(decoration, requestInfo);
		});
		loc_decorations = undefined;
	};	

	var loc_unregisterViewListener = function(ele){
		ele.unregisterEventListener(loc_eventListener);
		ele.unregisterValueChangeEventListener(loc_valueChangeEventListener);
	}

	var loc_registerViewListener = function(ele){
		ele.registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});
		ele.registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	}
	
	var loc_getCurrent = function(){
		if(loc_decorations.length==0)  return;
		return loc_decorations[loc_decorations.length-1];	
	};
	
	var loc_out = {
		getUIView :function(){ return loc_decorations[0];  },  
			
		addDecoration : function(decoration){
			if(Array.isArray(decoration)){
				_.each(decoration, function(dec, index){
					loc_addElement(dec);
				});
			}
			else{
				loc_addElement(decoration);
			}
		},	
			
		//append this views to some element as child
		appendTo : function(ele){  loc_getCurrent().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_getCurrent().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_getCurrent().detachViews();		},

		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener);},

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext);},
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener);},

		getUpdateContextRequest : function(parms, handlers, requestInfo){	return loc_getCurrent().getUpdateContextRequest(parms, handlers, requestInfo);	},
		executeUpdateContextRequest : function(parms, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getUpdateContextRequest(parms, handlers, requestInfo));	},

		getExecuteCommandRequest : function(command, parms, handlers, requestInfo){
			if(command==node_CONSTANT.PAGE_COMMAND_REFRESH){
				return loc_getCurrent().getUpdateContextRequest(parms, handlers, requestInfo);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
					return loc_getCurrent().command(command, parms, requestInfo);	
				}, handlers, requestInfo);
			}
		},
		executeExecuteCommandRequest : function(command, data, handlers, requestInfo){		node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(command, data, handlers, requestInfo));	},
		
		getBuildContextGroupRequest : function(handlers, requestInfo){
			return node_contextUtility.buildContextGroupRequest(loc_getCurrent().getContextElements(), handlers, requestInfo);
		},
		
		getContextEleValueAsParmsRequest : function(handlers, requestInfo){
			return node_contextUtility.getContextEleValueAsParmsRequest(loc_getCurrent().getContextElements(), handlers, requestInfo);
		},

		getGetPageStateRequest : function(handlers, requestInfo){
			return node_contextUtility.getContextStateRequest(loc_getCurrent().getContextElements(), handlers, requestInfo);
		},
		
		destroy : function(request){  node_getLifecycleInterface(loc_out).destroy(request);  },
	};

	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIRESOURCEVIEW);

	node_getLifecycleInterface(loc_out).init(uiView);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIPage", node_createUIPage); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createUIViewFactory;
	var node_createServiceRequestInfoSimple;
	var node_createUIPage;
	var node_createServiceRequestInfoSequence;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIPageService = function(){
	
	var loc_uiResourceViewFactory = node_createUIViewFactory();
	
	var loc_getResourceViewId = function(){	return nosliw.generateId();	};
	
	var loc_out = {

			getCreateUIPageRequest : function(name, context, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIResourceView", {"name":name}), handlers, requestInfo);

				out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([name], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE, {
					success : function(requestInfo, uiResources){
						var uiResource = uiResources[name];
						return loc_uiResourceViewFactory.getCreateUIViewRequest(uiResource, loc_getResourceViewId(), undefined, undefined, {
							success : function(requestInfo, uiView){
								return node_createUIPage(uiView);
							}
						});
					}
				}));
				
				return out;
			},	
			executeCreateUIPageRequest : function(name, context, handlers, requester_parent){
				var requestInfo = this.getCreateUIPageRequest(name, context, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
			
			getGenerateUIPageRequest : function(uiResource, context, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIResourceView", {"name":name}), handlers, requestInfo);
				out.addRequest(loc_uiResourceViewFactory.getCreateUIViewRequest(uiResource, loc_getResourceViewId(), undefined, context, {
					success : function(requestInfo, uiView){
						return node_createUIPage(uiView);
					}
				}));
				return out;
			},	
			executeGenerateUIPageRequest : function(uiResource, context, handlers, requester_parent){
				var requestInfo = this.getGenerateUIPageRequest(uiResource, context, handlers, requester_parent);
				node_requestServiceProcessor.processRequest(requestInfo);
			},
	};

	loc_out = node_buildServiceProvider(loc_out, "uiResourceService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIViewFactory", function(){node_createUIViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIPage", function(){node_createUIPage = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createUIPageService", node_createUIPageService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_createContextElement;
	var node_createExtendedContext;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createContextVariableInfo;
	var node_requestServiceProcessor;
	var node_createUIDataOperationRequest;
	var node_UIDataOperation;
	var node_uiDataOperationServiceUtility;
	var node_createBatchUIDataOperationRequest;
	var node_createUIViewFactory;
	var node_createEventObject;
	var node_contextUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUITagRequest = function(id, uiTagResource, parentUIResourceView, handlers, requestInfo){
	var uiTag = node_createUITag(id, uiTagResource, parentUIResourceView);
	
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUITag", {}), handlers, requestInfo);

	var createUITagRequest = node_createServiceRequestInfoSequence(undefined);
	var tagId = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGNAME];
	createUITagRequest.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([tagId], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG, {
		success : function(requestInfo, resources){
			var uiTagResourceObj = resources[tagId];

			var uiTagObj = _.extend({
				findFunctionDown : function(name){},	
				initViews : function(requestInfo){},
				postInit : function(){},
				preInit : function(){},
				destroy : function(){},
			}, uiTagResourceObj[node_COMMONATRIBUTECONSTANT.UITAGDEFINITION_SCRIPT].call(uiTag, uiTag.prv_getEnvObj()));
			uiTag.prv_setUITagObj(uiTagObj);
			
			var uiTagInitRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("UITagInit"));
			
			//overriden method before view is attatched to dom
			if(uiTagObj.preInit!=undefined){
				var initObj = uiTagObj.preInit(requestInfo);
				if(initObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(initObj)){
					uiTagInitRequest.addRequest(initObj);
				}
			}
			
			//overridden method to create init view
			if(uiTagObj.initViews!=undefined){
				var views = uiTagObj.initViews(requestInfo);
				//attach view to resourve view
				if(views!=undefined)  uiTag.prv_getStartElement().after(views);	
			}

			//overridden method to do sth after view is attatched to dom
			if(uiTagObj.postInit!=undefined){
				var postInitObj = uiTagObj.postInit(requestInfo);
				if(postInitObj!=undefined && node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==node_getObjectType(postInitObj)){
					uiTagInitRequest.addRequest(postInitObj);
				}
			}
			
			return uiTagInitRequest;
		}
	}));

	out.addRequest(createUITagRequest);
	out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
		return uiTag;
	}));
	return out;
};
	
	
	
	/**
	 * 
	 * base customer tag object, child tag just provide extendObj which implements its own method 
	 * it is also constructor object for customer tag object  
	 * 		id: 	id for this tag
	 * 		uiTagResource:	ui tag resource 
	 * 		parentUIResourceView: 	parent ui resource view
	 */
var node_createUITag = function(id, uiTagResource, parentUIResourceView){
	//object to implement tag logic, it is from tag library
	var loc_uiTagObj;
	
	//id of this tag object
	var loc_id = id;
	//ui resource definition
	var loc_uiTagResource = uiTagResource;
	//parent resource view
	var loc_parentResourceView = parentUIResourceView;
	//all tag attributes
	var loc_attributes = {};

	var loc_tagName = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGNAME];
	var loc_varNameMapping = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_LOCAL2GLOBAL];
	
	var loc_eventNameMapping = uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_EVENTMAPPING];
	
	var loc_context;
	
	//boundary element for this tag
	var loc_startEle = undefined;
	var loc_endEle = undefined;
	
	var loc_tagEventObject = node_createEventObject();
	var loc_eventObject = node_createEventObject();
	
	//get wraper element
	loc_startEle = loc_parentResourceView.get$EleByUIId(loc_id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX);
	loc_endEle = loc_parentResourceView.get$EleByUIId(loc_id+node_COMMONCONSTANT.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX);
	
	//init all attributes value
	_.each(uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ATTRIBUTES], function(attrValue, attribute, list){
		loc_attributes[attribute] = attrValue;
	});
	
	//create context
	var parentContext;
	if(parentUIResourceView!=undefined)   parentContext = parentUIResourceView.getContext();
	loc_context = node_contextUtility.buildContext(uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGCONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], parentContext);
	
	
	//related name: name, name with categary
	var loc_getRelatedName = function(name){
		var out = [];
		out.push(name);
		var mappedName = loc_varNameMapping[name];
		if(mappedName!=undefined)  out.push(mappedName);
		return out;
	};
	
	//exContext extra context element used when create context for tag resource
	var loc_createContextForTagResource = function(exContext){
		if(exContext==undefined)   exContext = loc_context;
		var context = node_contextUtility.buildContext(loc_uiTagResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], exContext);
		return context;
	};
	
	var loc_processChildUIViewEvent = function(eventName, eventData, requestInfo){
		var en = loc_eventNameMapping[eventName];
		if(en==undefined)  en = eventName;
		loc_eventObject.triggerEvent(en, eventData, requestInfo);
	};
	
	
	//runtime env for uiTagObj
	//include : basic info, utility method
	var loc_envObj = {
		getId : function(){  return loc_id;  },
		getStartElement : function(){  return loc_startEle;  },
		getEneElement : function(){  return loc_endEle;  },
		getContext : function(){   return loc_context;  },
		getAttributeValue : function(name){  return loc_attributes[name];  },
		getAttributes : function(){   return loc_attributes;   },
		getParentResourceView : function(){ return loc_parentResourceView;  },
		getUIResource : function(){  return  loc_uiTagResource; },
		
		//utility methods
		createVariable : function(fullPath){  return loc_context.createVariable(node_createContextVariableInfo(fullPath));  },
		processRequest : function(requestInfo){   node_requestServiceProcessor.processRequest(requestInfo);  },
		
		//---------------------------------ui resource view
		getCreateUIViewWithIdRequest : function(id, context, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIViewWithId", {}), handlers, requestInfo);
			out.addRequest(node_createUIViewFactory().getCreateUIViewRequest(loc_uiTagResource, id, loc_parentResourceView, context, {
				success : function(request, uiView){
					uiView.registerEventListener(loc_eventObject, loc_processChildUIViewEvent, loc_out);
				}
			}, requestInfo));
			return out;
		},

		getCreateDefaultUIViewRequest : function(handlers, requestInfo){
			return this.getCreateUIViewWithIdRequest(loc_id, loc_createContextForTagResource(), handlers, requestInfo);
		},
		
		//---------------------------------build context
		createContextElementInfo : function(name, data1, data2, adapterInfo, info){
			var out = [];
			_.each(loc_getRelatedName(name), function(name, index){
				out.push(node_createContextElementInfo(name, data1, data2, adapterInfo, info));  
			});
			return out;
		},
		createContextElementInfoFromContext : function(name, contextEle, path){	 
			return node_createContextElementInfo(name, loc_context, node_createContextVariableInfo(contextEle, path));	
		},
		
		//create extended ui tag resource context : 
		createExtendedContext : function(extendedEleInfos, requestInfo){
			var extendedVarEles = {};
			_.each(extendedEleInfos, function(eleInfo, index){
				if(!Array.isArray(eleInfo))			extendedVarEles[eleInfo.name] = node_createContextElement(eleInfo);
				else{
					_.each(eleInfo, function(eleInfo){
						extendedVarEles[eleInfo.name] = node_createContextElement(eleInfo);
					});
				}
			});
			var extendedContext = node_createExtendedContext(loc_context, extendedVarEles);
			var context = loc_createContextForTagResource(extendedContext);
			
			
//			var context = loc_createContextForTagResource();
//			_.each(extendedEleInfos, function(eleInfo, index){
//				context.addContextElement(eleInfo);
//			});
			return context;
		},
		
		//---------------------------------operation request
		getDataOperationGet : function(target, path){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createGetOperationService(path)); },
		getDataOperationRequestGet : function(target, path, handler, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationGet(target, path), handler, request);	},
		executeDataOperationRequestGet : function(target, path, handler, request){			return this.processRequest(this.getDataOperationRequestGet(target, path, handler, request));		},

		getDataOperationSet : function(target, path, value){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createSetOperationService(path, value)); },
		getDataOperationRequestSet : function(target, path, value, handler, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationSet(target, path, value), handler, request);	},
		executeDataOperationRequestSet : function(target, path, value, handler, request){	return this.processRequest(this.getDataOperationRequestSet(target, path, value, handler, request));	},

		createHandleEachElementProcessor : function(name, path){  return this.getContext().createHandleEachElementProcessor(name, path);  },
		
		getBatchDataOperationRequest : function(operations){
			var requestInfo = node_createBatchUIDataOperationRequest(loc_context);
			_.each(operations, function(operation, i){
				requestInfo.addUIDataOperation(operation);						
			});
			return requestInfo;
		},
		executeBatchDataOperationRequest : function(operations){		this.processRequest(this.getBatchDataOperationRequest(operations));		},
		
		//---------------------------------
		getExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){  return nosliw.runtime.getExpressionService().getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent)  },
		executeExecuteOperationRequest : function(dataTypeId, operation, parmsArray, handlers, requester_parent){
			this.processRequest(this.getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent));
		},
		
		//---------------------------------other request
		getGatewayCommandRequest : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requestInfo);	},
		executeGatewayCommandRequest : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().executeExecuteGatewayCommandRequest(gatewayId, command, parms, handlers, requestInfo);	},
		executeGatewayCommand : function(gatewayId, command, parms, handlers, requestInfo){	return nosliw.runtime.getGatewayService().executeGatewayCommand(gatewayId, command, parms, handlers, requestInfo);	},
	
		//--------------------------------- event
		trigueEvent : function(event, eventData, requestInfo){   loc_tagEventObject.triggerEvent(event, eventData, requestInfo);  },
	
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(id, uiTagResource, parentUIResourceView){
		
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(){
		loc_uiTagObj.destroy();
		if(loc_context!=undefined)  loc_context.destroy();
	};
	
	var loc_out = {
		prv_getEnvObj : function(){  return loc_envObj;  },
		prv_setUITagObj : function(uiTagObj){  loc_uiTagObj = uiTagObj;   },
		prv_getStartElement : function(){  return loc_startEle;  },

		getId : function(){  return loc_id;   },
		
		getTagName : function(){ return loc_tagName;   },
	
		getTagObject : function(){ return loc_uiTagObj;  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
		
		setAttribute : function(name, value){
			if(loc_uiTagObj.processAttribute!=undefined)  loc_uiTagObj.processAttribute(name, value);  
		},
		
		registerTagEventListener : function(eventName, handler, thisContext){	return loc_tagEventObject.registerListener(eventName, undefined, handler, thisContext);	},
		registerEventListener : function(listener, handler, thisContext){	return loc_eventObject.registerListener(undefined, listener, handler, thisContext);	},
		
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UITAG);

	node_getLifecycleInterface(loc_out).init(id, uiTagResource, parentUIResourceView);
	
	return loc_out;
	
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElement", function(){node_createContextElement = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createExtendedContext", function(){node_createExtendedContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createBatchUIDataOperationRequest", function(){node_createBatchUIDataOperationRequest  = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIViewFactory", function(){node_createUIViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUITagRequest", node_createUITagRequest); 

})(packageObj);

//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_createContext;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_uiResourceUtility;
	var node_createEmbededScriptExpressionInContent;
	var node_createEmbededScriptExpressionInAttribute;
	var node_createEmbededScriptExpressionInTagAttribute;
	var node_getLifecycleInterface;
	var node_basicUtility;
	var node_createUITagRequest;
	var node_createEventObject;
	var node_createUIDataOperationRequest;
	var node_requestServiceProcessor;
	var node_uiDataOperationServiceUtility;
	var node_UIDataOperation;
	var node_contextUtility;
	var node_IOTaskResult;
	var node_ioTaskProcessor;
	var node_createDynamicData;
//*******************************************   Start Node Definition  ************************************** 	

var loc_createUIViewFactory = function(){
	
	var loc_out = {
		getCreateUIViewRequest : function(uiResource, id, parent, context, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateUIView", {}), handlers, requestInfo);

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var uiView = loc_createUIView(uiResource, id, parent, context, requestInfo);
				
				var createUITagRequest = node_createServiceRequestInfoSet(undefined, {
					success: function(requestInfo, tagResults){
						_.each(tagResults.getResults(), function(uiTag, uiTagId){
							uiView.prv_addUITag(uiTagId, uiTag);
						});
						uiView.prv_initCustomTagEvent();
						uiView.prv_initCustomTagExpressionAttribute();
						return uiView;
				}});
				
				//init customer tags
				_.each(uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_UITAGS], function(uiTagResource, tagUiId, list){
					var uiTagId = uiView.prv_getUpdateUIId(tagUiId);
					createUITagRequest.addRequest(uiTagId, node_createUITagRequest(uiTagId, uiTagResource, uiView, {
						success : function(requestInfo, uiTag){
							return uiTag;
						}
					}));
				});
				return createUITagRequest;
			}));
			return out;
		}
	};
	
	return loc_out;
};	
	
	
/*
 * method to create ui resource view according to 
 * 		uiresource object
 * 	 	name space id
 * 		parent uiresource
 */
var loc_createUIView = function(uiResource, id, parent, context, requestInfo){

	//event source used to register and trigger event
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();
	
	//temporately store uiResource
	var loc_uiResource = uiResource;

	//parent ui resource view
	var loc_parentResourveView = parent;
	//name space for this ui resource view
	//every element/customer tag have unique ui id within a web page
	//during compilation, ui id is unique within ui resoure, however, not guarenteed between different ui resource view within same web page
	//name space make sure of it as different ui resource view have different name space
	var loc_idNameSpace = id;

	//all constants defined. they are used in expression
	var loc_constants = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONSTANTS];
	
	//context object for this ui resource view
	var loc_context = context;

	//all content expression objects
	var loc_expressionContents = [];
	
	//all events on regular elements
	var loc_elementEvents = [];
	
	//object store all the functions for js block
	var loc_scriptObject = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPT];
	
	var loc_services = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SERVICES]; 
	var loc_serviceProviders = loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SERVICEPROVIDERS]; 
	
	//all customer tags
	var loc_uiTags = {};
	//all events on customer tag elements
	var loc_tagEvents = [];
	
	//all the attributes on this ui resource
	var loc_attributes = {};
	
	//ui resource view wraper element
	var loc_startEle = undefined;
	var loc_endEle = undefined;

	//temporary object for ui resource view container
	var	loc_fragmentDocument = undefined;
	var loc_parentView = undefined;
	
	//the ui resource view created 
	var loc_out = undefined;

	//if this ui resource is defined within a customer tag, then this object store all the information about that parent tag
	var loc_parentTagInfo = undefined;
	
	/*
	 * init element event object
	 */
	var loc_initElementEvent = function(eleEvent){
		//get element for this event
		var ele = loc_out.prv_getLocalElementByUIId(loc_out.prv_getUpdateUIId(eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID]));
		var subEle = ele;
		//if have sel attribute set, then find sub element according to sel
		var selection = eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_SELECTION];
		if(!node_basicUtility.isStringEmpty(selection))		subEle = ele.find(selection);

		//register event
		var eventValue = eleEvent;
		var eventName = eleEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		subEle.bind(eventName, function(event){
			var info = {
				event : event, 
				source : this,
			};
			loc_out.prv_callScriptFunctionUp(eventValue[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], info);
		});
		
		return {
			source : subEle,
			event :  eventName,
		};
	};

	/*
	 * init ui tag event object
	 */
	var loc_initTagEvent = function(tagEvent){
		var tag = loc_uiTags[loc_out.prv_getUpdateUIId(tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_UIID])];
		var eventName = tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_EVENT];
		
		var listener = tag.registerTagEventListener(eventName, function(event, eventData, requestInfo){
			var info = {
				event : event,
				eventData : eventData,
				source : tag,
				requestInfo: requestInfo,
			};
			loc_out.prv_callScriptFunctionUp(tagEvent[node_COMMONATRIBUTECONSTANT.ELEMENTEVENT_FUNCTION], info);
		});
		
		return {
			source : tag,
			event :  eventName,
			listener: listener,
		};
	};
	
	
	/*
	 * find matched element according to attribute value
	 */
	var loc_getLocalElementByAttributeValue = function(name, value){return loc_findLocalElement("["+name+"='"+value+"']");};
	
	/*
	 * find matched elements according to selection
	 */
	var loc_findLocalElement = function(select){return loc_startEle.nextUntil(loc_endEle.next()).find(select).addBack(select);};
	
	
	/*
	 * update everything again
	 */
	var loc_refresh = function(){
		loc_setContext(loc_context);
	};

	/*
	 * get all views for this resource view
	 */
	var loc_getViews = function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle);  };

	
	//io between module context and page context
	var loc_viewIO = node_createDynamicData(
		function(handlers, request){
			return node_contextUtility.getContextValueAsParmsRequest(loc_context, handlers, request);
		}, 
		function(value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			//update page with data
			out.addRequest(loc_context.getUpdateContextRequest(value));
			return out;
		}
	);

	var loc_getServiceRequest = function(serviceName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {"serviceName":serviceName}), handlers, request);
		var service = loc_services[serviceName];
		out.addRequest(nosliw.runtime.getDataService().getExecuteEmbededDataServiceByNameRequest(service[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_PROVIDER], loc_serviceProviders, service, loc_viewIO));
		return out;
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(uiResource, id, parent, context, requestInfo){

		loc_attributes = uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ATTRIBUTES];
		
		//build context element first
		if(loc_context==undefined){
			//if context not provide, then build context by parent context and current context definition
			var parentContext = parent==undefined?undefined:parent.getContext();
			loc_context = node_contextUtility.buildContext(uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXTFLAT_CONTEXT][node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], parentContext);
		}

		//wrap html by start and end element
		var resourceStartId = "-resource-start";
		var resourceEndId = "-resource-end";
		var html = node_uiResourceUtility.createPlaceHolderWithId(resourceStartId) + _.unescape(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_HTML]) + node_uiResourceUtility.createPlaceHolderWithId(resourceEndId);
		
		//update all uiid within html by adding space name to uiid
		html = node_uiResourceUtility.updateHtmlUIId(html, loc_idNameSpace);
		
		//render html to temporary document fragment
		loc_fragmentDocument = $(document.createDocumentFragment());
		loc_parentView = $("<div></div>");
		loc_fragmentDocument.append(loc_parentView);
		var views = $($.parseHTML(html));
		loc_parentView.append(views);
		
		//get wraper dom element (start and end element)
		loc_startEle = loc_parentView.find("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+loc_out.prv_getUpdateUIId(resourceStartId)+"']");
		loc_endEle = loc_parentView.find("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+loc_out.prv_getUpdateUIId(resourceEndId)+"']");
		

		//init expression content
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPTEXPRESSIONSINCONTENT], function(expressionContent, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInContent(expressionContent, loc_out, requestInfo));
		});

		//init normal expression attribute
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPTEXPRESSIONINATTRIBUTES], function(expressionAttr, key, list){
			loc_expressionContents.push(node_createEmbededScriptExpressionInAttribute(expressionAttr, loc_out, requestInfo));
		});

		//init regular tag event
		_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ELEMENTEVENTS], function(eleEvent, key, list){
			loc_elementEvents.push(loc_initElementEvent(eleEvent));
		});

	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		
		//call destroy funtion in uiresource definition
//		loc_out.prv_getScriptObject().prv_callLocalFunction(NOSLIWCONSTANT.UIRESOURCE_FUNCTION_DESTROY);
		
		//detach view from dom
		loc_out.detachViews();
		
		loc_attributes = {};
		
		_.each(loc_uiTags, function(uiTag, tag, list){
			uiTag.destroy();
		});
		loc_uiTags = undefined;

		_.each(loc_expressionContents, function(expressionContent, key, list){
			expressionContent.destroy();
		});
		loc_expressionContents = undefined;
		
		_.each(loc_elementEvents, function(eleEvent, key, list){
			eleEvent.source.unbind(eleEvent.event);
		});
		loc_elementEvents = undefined;

		loc_eventSource.clearup();
		loc_valueChangeEventSource.clearup();
		
		loc_tagEvents = undefined;

		loc_startEle = undefined;
		loc_endEle = undefined;
		
		loc_parentResourveView = undefined;
		loc_resource = undefined;
		loc_idNameSpace = undefined;
		
		loc_fragmentDocument = undefined;
		loc_parentView = undefined;

		loc_context = undefined;
	};

	
	
	loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		prv_getScriptObject : function(){return loc_scriptObject;},
		//get the parent resource view that contain this resource view, when this resource is within tag
		prv_getParentResourceView : function(){		return loc_parentResourveView;		},
		//get root resource view: the resource view that don't have parent
		prv_getRootResourceView : function(){
			var view = this;
			var parent = view.prv_getParentResourceView();
			while(parent!=undefined){
				view = parent;
				parent = view.prv_getParentResourceView();
			}
			return view;
		},
		
		prv_initCustomTagEvent : function(){
			//init customer tag event
			_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_TAGEVENTS], function(tagEvent, key, list){
				loc_tagEvents.push(loc_initTagEvent(tagEvent));
			});
		},
		
		prv_initCustomTagExpressionAttribute : function(){
			//init tag expression attribute
			_.each(loc_uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_SCRIPTEXPRESSIONINTAGATTRIBUTES], function(expressionAttr, key, list){
				loc_expressionContents.push(node_createEmbededScriptExpressionInTagAttribute(expressionAttr, loc_out, requestInfo));
			});
		},
		
		prv_trigueEvent : function(eventName, data, requestInfo){loc_eventSource.triggerEvent(eventName, data, requestInfo); },

		prv_getTagByUIId : function(uiId){ return loc_uiTags[uiId];  },
		prv_addUITag : function(uiId, uiTag){  
			loc_uiTags[uiId] = uiTag;  
			uiTag.registerEventListener(loc_eventListener, function(eventName, eventData, requestInfo){
				loc_out.prv_trigueEvent(eventName, eventData, requestInfo);
			});
		},
		
		/*
		 * update ui id by adding space name ahead of them
		 */
		prv_getUpdateUIId : function(uiId){	return loc_idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME+uiId;	},

		/*
		 * find matched element according to uiid
		 */
		prv_getLocalElementByUIId : function(id){return loc_findLocalElement("["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+id+"']");},
		
		prv_callScriptFunction : function(funName){   
			var fun = loc_scriptObject[funName];
			var env = {
					context : loc_out.getContext(),
					uiUnit : loc_out,
					trigueEvent : function(eventName, eventData, requestInfo){
						loc_out.prv_trigueEvent(eventName, eventData, requestInfo);
					},
					getServiceRequest : function(serviceName, handlers, request){
						return loc_getServiceRequest(serviceName, handlers, request);
					}
			};
			var args = Array.prototype.slice.call(arguments, 1);
			args.push(env);
			return fun.apply(this, args);
		},
		
		prv_callScriptFunctionUp : function(funName){   
			var find = this.prv_findFunctionUp(funName);
			if(find!=undefined)		return find.uiUnit.prv_callScriptFunction.apply(find.uiUnit, arguments);
			else  nosliw.error("Cannot find function : " + funName);
		},

		prv_callScriptFunctionDown : function(funName){
			var find = this.prv_findFunctionDown(funName);
			if(find!=undefined)		return find.uiUnit.prv_callScriptFunction.apply(find.uiUnit, arguments);
			else nosliw.warning("Cannot find function : " + funName);
		},

		prv_findFunctionDown : function(funName){
			var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[funName];
			if(fun!=undefined){
				return {
					fun : fun,
					uiUnit : loc_out,
				};
			}
			else{
				for (var id in loc_uiTags) {
				    if (!loc_uiTags.hasOwnProperty(id)) continue;
				    var childUITag = loc_uiTags[id];
			    	var funInfo = childUITag.getTagObject().findFunctionDown(funName);
			    	if(funInfo!=undefined)  return funInfo;
				}				
			}
		},

		prv_findFunctionUp : function(funName){
			var fun = loc_scriptObject==undefined?undefined:loc_scriptObject[funName];
			if(fun!=undefined){
				return {
					fun : fun,
					uiUnit : loc_out,
				};
			}
			else{
				return loc_parentResourveView.prv_findFunctionUp(funName);
			}
		},
		
		getContext : function(){return loc_context;},
		getUpdateContextRequest : function(values, handlers, requestInfo){	return loc_context.getUpdateContextRequest(values, handlers, requestInfo);		},
		getContextElements : function(){  return this.getContext().prv_elements; },

		getStartElement : function(){  return loc_startEle;   },
		getEndElement : function(){  return loc_endEle; },
		
		//get all elements of this ui resourve view
		getViews : function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle).get();	},

		//append this views to some element as child
		appendTo : function(ele){  loc_getViews().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_getViews().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_getViews());		},

		
		//return dom element
		getElementByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId)[0];},
		getElementsByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get();},
		getElementByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value).get(0);},
		
		//return jquery object
		get$EleByUIId : function(uiId){return this.prv_getLocalElementByUIId(uiId);},
		get$EleByAttributeValue : function(attr, value){return loc_getLocalElementByAttributeValue(attr, value);},

		//find tag object according to tag name
		getTagsByName : function(name){
			var tagsOut = [];
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					tagsOut.push(uiTag);
				}
			});
			return tagsOut;
		},

		//find tag object that have tag name and particular attribute/value
		getTagsByNameAttribute : function(name, attr, value){
			var tagsOut = [];
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					if(uiTag.getAttribute(attr)==value){
						tagsOut.push(uiTag);
					}
				}
			});
			return tagsOut;
		},
		
		//return map containing value/tag pair for particular tag name and its attribute
		getTagsAttrMapByName : function(name, attr){
			var tagsOut = {};
			_.each(loc_uiTags, function(uiTag, tagId, list){
				var tagName = uiTag.getTagName();
				if(tagName==name){
					tagsOut[uiTag.getAttribute(attr)] = uiTag;
				}
			});
			return tagsOut;
		},
		
		getConstants : function(){   return loc_constants;  },
		
		setAttribute : function(attribute, value){loc_attributes[attribute]=value;},
		getAttribute : function(attribute){return loc_attributes[attribute];},
		
		getIdNameSpace : function(){return loc_idNameSpace;},
		getParentTagInfo : function(){	return loc_parentTagInfo;	},
		setParentTagInfo : function(info){		loc_parentTagInfo = info;	},
		
		destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
		
		getDataOperationSet : function(target, path, value, dataTypeInfo){  return new node_UIDataOperation(target, node_uiDataOperationServiceUtility.createSetOperationService(path, value, dataTypeInfo)); },
		getDataOperationRequestSet : function(target, value, dataTypeInfo, handlers, request){	return node_createUIDataOperationRequest(loc_context, this.getDataOperationSet(target, undefined, value, dataTypeInfo), handlers, request);	},
		executeDataOperationRequestSet : function(target, value, dataTypeInfo, handlers, request){	return node_requestServiceProcessor.processRequest(this.getDataOperationRequestSet(target, value, dataTypeInfo, handlers, request));	},
	
		getDefaultOperationRequestSet : function(value, dataTypeInfo, handlers, request){	return this.getDataOperationRequestSet(this.getContext().getElementsName()[0], value, dataTypeInfo, handlers, request);	},
		executeDefaultDataOperationRequestSet : function(value, dataTypeInfo, handlers, request){	return node_requestServiceProcessor.processRequest(this.getDefaultOperationRequestSet(value, dataTypeInfo, handlers, request));	},
	
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){    return loc_context.registerValueChangeEventListener(listener, handler, thisContext);     },
		unregisterValueChangeEventListener : function(listener){	return loc_context.unregisterValueChangeEventListener(listener); },
		
		command : function(command, data, requestInfo){			return this.prv_callScriptFunctionDown("command_"+command, data, requestInfo);		},
		findFunctionDown : function(funName){  return this.prv_findFunctionDown(funName);  },
		
	};

	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIVIEW);

	node_getLifecycleInterface(loc_out).init(uiResource, id, parent, context, requestInfo);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.utility", function(){node_uiResourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInContent", function(){node_createEmbededScriptExpressionInContent = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInAttribute", function(){node_createEmbededScriptExpressionInAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createEmbededScriptExpressionInTagAttribute", function(){node_createEmbededScriptExpressionInTagAttribute = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUITagRequest", function(){node_createUITagRequest = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskProcessor", function(){node_ioTaskProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIViewFactory", loc_createUIViewFactory); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createUIViewFactory;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_createContext;
	var node_createContextVariableInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = {

		/*
		 * create place holder html with special ui id 
		 */
		createPlaceHolderWithId : function(id){
			return "<nosliw style=\"display:none;\" nosliwid=\"" + id + "\"></nosliw>";
		},
		
		/*
		 * build context
		 * 		1. read context information for this resource from uiResource
		 * 		2. add extra element infos
		 */
		buildUIResourceContext : function(uiResource, contextElementInfoArray){
			var contextElementsInf = [];
			
			//get element info from resource definition
			var resourceAttrs = uiResource[node_COMMONATRIBUTECONSTANT.UIRESOURCEDEFINITION_ATTRIBUTES];
			if(resourceAttrs!=undefined){
				var contextStr = resourceAttrs.contexts;
				var contextSegs = nosliwCreateSegmentParser(contextStr, node_COMMONCONSTANT.SEPERATOR_ELEMENT);
				while(contextSegs.hasNext()){
					var name = undefined;
					var element = undefined;
					var contextSeg = contextSegs.next();
					var index = contextSeg.indexOf("@");
					if(index==-1){
						name = contextSeg;
						info = {};
					}
					else{
						name = contextSeg.substring(0, index);
						var type = contextSeg.substring(index+1);
						info = {wrapperType:type};
					}
					contextElementsInf.push(nosliwCreateContextElementInfo(name, info));
				}
			}

			//add extra element info
			_.each(contextElementInfoArray, function(contextElementInfo, key){
				contextElementsInf.push(contextElementInfo);
			}, this);
			
			return nosliwCreateContext(contextElementsInf);
		},
		
		/*
		 * update all the ui id within html by adding space name ahead of them
		 */
		updateHtmlUIId : function(html, idNameSpace){
			var find = node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\"";
			return html.replace(new RegExp(find, 'g'), node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\""+idNameSpace+node_COMMONCONSTANT.SEPERATOR_FULLNAME);
		},
		
		createTagResourceId : function(name){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = name; 
			out[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UITAG; 
			return out;
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIViewFactory", function(){node_createUIViewFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("dataservice");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_ioTaskProcessor;

//*******************************************   Start Node Definition  ************************************** 	
var node_createDataService = function(){

	var loc_out = {

		getExecuteEmbededDataServiceByNameRequest : function(serviceName, serviceProviders, serviceUse, ioEndpoint, handlers, requester_parent){
			var serviceProvider = serviceProviders[serviceName];
			return loc_out.getExecuteEmbededDataServiceByProviderRequest(serviceProvider, serviceUse, ioEndpoint, handlers, requester_parent);
		},
			
		getExecuteEmbededDataServiceByProviderRequest : function(serviceProvider, serviceUse, ioEndpoint, handlers, requester_parent){
			return loc_out.getExecuteEmbededDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], serviceUse, ioEndpoint, handlers, requester_parent);
		},			
			
		getExecuteEmbededDataServiceRequest : function(serviceId, serviceUse, ioEndpoint, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {}), handlers, requester_parent);
			var serviceMapping = serviceUse[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_SERVICEMAPPING];
			out.addRequest(node_ioTaskProcessor.getExecuteIOTaskRequest(
				ioEndpoint, undefined, serviceMapping,
				function(input, handlers, request){
					var serviceRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
					serviceRequest.addRequest(loc_out.getExecuteDataServiceRequest(serviceId, input, {
						success : function(request, serviceResult){
							return new node_IOTaskResult(serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTNAME], serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_OUTPUT]);
						}
					}));
					return serviceRequest;
				}));
			return out;
		},
		
		getExecuteDataServiceByNameRequest : function(serviceName, serviceProviders, parms, handlers, requester_parent){
			var serviceProvider = serviceProviders[serviceName];
			return this.getExecuteDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], parms, handlers, requester_parent);
		},
			
		getExecuteDataServiceByProviderRequest : function(serviceProvider, parms, handlers, requester_parent){
			return this.getExecuteDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], parms, handlers, requester_parent);
		},
		
		getExecuteDataServiceRequest : function(serviceId, parms, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExectueDataService", {"serviceId":serviceId, "parms":parms}), handlers, requestInfo);

			var gatewayParm = {};
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_QUERY] = {}
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_QUERY][node_COMMONATRIBUTECONSTANT.QUERYSERVICE_SERVICEID] = serviceId;
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_PARMS] = parms;
			
			out.addRequest(nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(
					node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_SERVICE, 
					node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST, 
					gatewayParm,
					{
						success : function(requestInfo, serviceResult){
							return serviceResult;
						}
					}
			));
			
			return out;
		},
			
		executeExecuteDataServiceRequest : function(serviceId, parms, handlers, requester_parent){
			var requestInfo = this.getExecuteDataServiceRequest(serviceId, parms, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskProcessor", function(){node_ioTaskProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataService", node_createDataService); 

})(packageObj);
var library = nosliw.getPackage("component");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createState;
	var node_createConfigure;
	var node_createComponentDecoration;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentComplex = function(configure, envInterface){

	var loc_configure = node_createConfigure(configure);
	
	var loc_state = node_createState();
	var loc_parts = [];
	var loc_interface = _.extend({}, envInterface);

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_getCurrentFacad = function(){   return loc_parts[loc_parts.length-1];  };
	
	var loc_getComponent = function(){  return  loc_parts[0]; };

	var loc_getLifeCycleRequest = function(requestFunName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ComponentComplexLifycycle", {}), handlers, request);
		//start module
		_.each(loc_parts, function(part, i){
			if(part[requestFunName]!=undefined){
				out.addRequest(part[requestFunName](handlers, request));
			}
		});
		return out;
	};

	var loc_updateView = function(view, request){
		for(var i=loc_parts.length-1; i>0; i--){
			var updated = loc_parts[i].updateView(view, request);
			if(updated!=undefined)  view = updated;  
			else break;
		}
	};
	
	var loc_unregisterPartListener = function(){	
		loc_getCurrentFacad().unregisterEventListener(loc_eventListener);	
		loc_getCurrentFacad().unregisterValueChangeEventListener(loc_valueChangeEventListener);	
	};

	var loc_registerPartListener = function(){
		loc_getCurrentFacad().registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});

		loc_getCurrentFacad().registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	};

	var loc_out = {
		
		addComponent : function(component){
			loc_parts.push(component);
			loc_registerPartListener();
		},
		
		addDecorations : function(componentDecorationInfos){
			for(var i in componentDecorationInfos){  loc_out.addDecoration(componentDecorationInfos[i]);	}
		},

		addDecoration : function(componentDecorationInfo){
			var current = loc_getCurrentFacad();
			loc_unregisterPartListener();
			var decName = componentDecorationInfo.name;
			var decoration = node_createComponentDecoration(decName, current, componentDecorationInfo.coreFun, loc_interface, loc_configure.getConfigureData(decName), loc_state);
			loc_parts.push(decoration);
			if(decoration.getInterface!=undefined)	_.extend(loc_interface, decoration.getInterface());
			loc_registerPartListener();
		},
		
		getInterface : function(){  return loc_interface;   },
		
		getComponent : function(){   return loc_getComponent();    },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_getCurrentFacad().getExecuteCommandRequest(command, parms, handlers, request);	},
		
		getAllStateData : function(){   return loc_state.getAllState();   },
		clearState : function(){   loc_state.clear();   },	
		setAllStateData : function(stateData){  loc_state.setAllState(stateData)  },
		
		updateView : function(view, request){  loc_updateView(view, request);  },
		
		getPreDisplayInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getPreDisplayInitRequest", handlers, request);  },
		getInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getInitRequest", handlers, request);  },
		getStartRequest : function(handlers, request){  return loc_getLifeCycleRequest("getStartRequest", handlers, request);  },
		getResumeRequest : function(handlers, request){  return loc_getLifeCycleRequest("getResumeRequest", handlers, request);  },
		getDeactiveRequest : function(handlers, request){  return loc_getLifeCycleRequest("getDeactiveRequest", handlers, request);  },
		getSuspendRequest : function(handlers, request){  return loc_getLifeCycleRequest("getSuspendRequest", handlers, request);  },
		getDestroyRequest : function(handlers, request){  return loc_getLifeCycleRequest("getDestroyRequest", handlers, request);  },

	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.createState", function(){node_createState = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentDecoration", function(){node_createComponentDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentComplex", node_createComponentComplex); 


})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentDecoration = function(id, child, coreGenerator, processEnv, configureData, state){
	
	var loc_id = id;
	var loc_configureData = configureData;
	var loc_state = state;
	var loc_processEnv = processEnv;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_child = child;
	loc_child.registerEventListener(loc_eventListener, function(eventName, eventData, request){
		if(loc_core.processComponentEvent!=undefined){
			var eventResult = loc_core.processComponentEvent(eventName, eventData, request);
			if(eventResult==true || eventResult==undefined){
				//propagate the same event
				loc_trigueEvent(eventName, eventData, request);
			}
			else if(eventResult==false){
				//not propagate
			}
			else{
				//new event 
				loc_trigueEvent(eventResult.eventName, eventResult.eventData, eventResult.request);
			}
		}
		else{
			//propagate the same event
			loc_trigueEvent(eventName, eventData, request);
		}
	});

	loc_child.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
		if(loc_core.processComponentValueChangeEvent!=undefined){
			loc_core.processComponentValueChangeEvent(eventName, eventData, request);
		}
		//propagate the same event
		loc_trigueValueChangeEvent(eventName, eventData, request);
	});

	var loc_component = loc_child.prv_getComponent==undefined? loc_child : loc_child.prv_getComponent();
	
	var loc_core = coreGenerator({
		
		getComponent : function(){   return loc_component;		},
		
		getConfigureData : function(){  return loc_configureData;	},
		
		getStateValue : function(name){  return loc_state.getStateValue(loc_id, name);	},
		
		getState : function(){  return loc_state.getState(loc_id);   },
		
		setStateValue : function(name, value){  loc_state.setStateValue(loc_id, name, value);	},
		
		processRequest : function(request){   	node_requestServiceProcessor.processRequest(request);  },
		
		getExecuteProcessRequest : function(process, extraInput, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessRequest(process, this.getComponent().getIOContext(), extraInput, handlers, request);
		},

		getExecuteProcessResourceRequest : function(processId, input, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessResourceRequest(processId, input, handlers, request);
		},
		
		trigueEvent : function(eventName, eventData, requestInfo){  loc_trigueEvent(eventName, eventData, requestInfo);	}
	});
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		prv_getComponent : function(){
			var childType = node_getObjectType(loc_child);
			if(childType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION)  return loc_child.prv_getComponent();
			else return loc_child;
		},	
			
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){
			if(loc_core.getExecuteCommandRequest!=undefined){
				var commandResult = loc_core.getExecuteCommandRequest(command, parms, undefined, undefined);
				if(commandResult==undefined){
					return loc_child.getExecuteCommandRequest(command, parms, handlers, request);
				}
				else{
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					out.addRequest(commandResult.requestResult);
					if(commandResult.commandInfo!=undefined){
						out.addRequest(loc_child.getExecuteCommandRequest(command, parms));
					}
					return out;
				}
			}
			else{
				return loc_child.getExecuteCommandRequest(command, parms, handlers, request);
			}
		},

		getInterface : function(){   return loc_core.getInterface();	},
		
		updateView : function(view){   
			if(loc_core.updateView==undefined)  return view;
			return loc_core.updateView(view);
		},
		
		getPreDisplayInitRequest : function(handlers, request){  return loc_core.getPreDisplayInitRequest==undefined?undefined:loc_core.getPreDisplayInitRequest(handlers, request);	},
		getInitRequest : function(handlers, request){  return loc_core.getInitRequest==undefined?undefined:loc_core.getInitRequest(handlers, request);	},
		getDeactiveRequest : function(handlers, request){  return loc_core.getDeactiveRequest==undefined?undefined:loc_core.getDeactiveRequest(handlers, request);	},
		getSuspendRequest : function(handlers, request){  return loc_core.getSuspendRequest==undefined?undefined:loc_core.getSuspendRequest(handlers, request);	},
		getResumeRequest : function(handlers, request){  return loc_core.getResumeRequest==undefined?undefined:loc_core.getResumeRequest(handlers, request);	},
		getStartRequest : function(handlers, request){  return loc_core.getStartRequest==undefined?undefined:loc_core.getStartRequest(handlers, request);	},
		getDestroyRequest : function(handlers, request){  return loc_core.getDestroyRequest==undefined?undefined:loc_core.getDestroyRequest(handlers, request);	},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION);
	return loc_out;
	
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentDecoration", node_createComponentDecoration); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	var node_resourceUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_loadComponentResourceRequest = function(componentInfo, decorationInfo, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteComponentResource"), handlers, request);
	
	var resourceIds = [];
	var componentResourceId;
	var component;
	if(componentInfo.componentResourceId != undefined){
		componentResourceId = {};
		componentResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = componentInfo.componentResourceId; 
		componentResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = componentInfo.type; 
		resourceIds.push(componentResourceId);
	}
	else{
		component = componentInfo;
	}

	var decorationFactoryInfos = [];
	if(decorationInfo!=undefined){
		_.each(decorationInfo.decoration, function(decFacDef, i){
			var decFacInfo = {};
			if(typeof decFacDef == "string"){
				decFacInfo.id = decFacDef;
				decFacInfo.name = decFacDef;
			}
			else{
				decFacInfo.id = decFacDef.id;
				decFacInfo.name = decFacDef.name;
				decFacInfo.coreFun = decFacDef.coreFun;
				if(decFacInfo.name==undefined)  decFacInfo.name = decFacInfo.id;
			}
			decorationFactoryInfos.push(decFacInfo);

			if(decFacInfo.coreFun==undefined){
				decFacInfo.resourceId = {};
				decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = decFacInfo.id; 
				decFacInfo.resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = decorationInfo.type; 
				resourceIds.push(decFacInfo.resourceId);
			}
		});
	}

	//load ui module resource and env factory resource
	out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds, {
		success : function(requestInfo, resourceTree){
			var componentDecorationInfos = requestInfo.getData("decorationFactoryInfos");
			_.each(componentDecorationInfos, function(decFacInfo, i){
				if(decFacInfo.resourceId!=undefined){
					decFacInfo.coreFun = node_resourceUtility.getResourceFromTree(resourceTree, decFacInfo.resourceId).resourceData;
				}
			});
			
			var component = requestInfo.getData("component");
			if(componentResourceId!=undefined)  component = node_resourceUtility.getResourceFromTree(resourceTree, componentResourceId).resourceData;
			
			return {
				component :component,
				decoration : componentDecorationInfos
			};
		}
	}).withData(component, "component").withData(decorationFactoryInfos, "decorationFactoryInfos"));
	return out;
};	
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("loadComponentResourceRequest", node_loadComponentResourceRequest); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createStateBackupService = function(componentType, id, version, storeService){
	
	var loc_componentType = componentType;
	var loc_id = id;
	var loc_version = version;
	var loc_storeService = storeService;
	
	var loc_out = {
			
		getBackupData : function(){  
			var storeData = loc_storeService.retrieveData(loc_componentType, loc_id);
			if(storeData==undefined)   return;
			loc_out.clearBackupData();  //clear backup data after retrieve
			if(storeData.version!=loc_version)		return;
			return storeData.data;
		},
		
		saveBackupData : function(stateData){
			var storeData = {
				version : loc_version,
				data : stateData
			};
			loc_storeService.saveData(loc_componentType, loc_id, storeData);  
		},
		
		clearBackupData : function(){  
			loc_storeService.clearData(loc_componentType, loc_id);  
		}
	};
	return loc_out;
};	
	
var node_createState = function(){
	var loc_state = {};
	
	var loc_out = {
		
		getAllState : function(){   return loc_state;   },
		setAllState : function(state){  loc_state = state; },
		
		getState : function(component){
			var out = loc_state[component];
			if(out==undefined){
				out = {};
				loc_state[component] = out;
			}
			return out;
		},
		
		getStateValue : function(component, name){
			return loc_out.getState(component)[name];
		},
		
		setStateValue : function(component, name, value){
			loc_out.getState(component)[name] = value;
		},
		
		clear : function(){
			loc_state = {};
		}
	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createState", node_createState); 
packageObj.createChildNode("createStateBackupService", node_createStateBackupService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_getComponentLifecycleInterface;
	var node_getComponentInterface;
	var node_createServiceRequestInfoSimple;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_requestServiceProcessor;
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentResetView = function(resetCallBack, restartCallBack){
	var loc_view = $('<div>Component Input: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	var loc_submitView = $('<button>Reset</button>')
	loc_view.append(loc_textView);
	loc_view.append(loc_submitView);
	
	loc_submitView.on('click', function(){
		resetCallBack();
	});

	var loc_inputIODataSet = node_createIODataSet();
	var loc_viewIO = node_createDynamicData(
		function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				var content = loc_textView.val();
				if(content=='')  return;
				return JSON.parse(content); 
			}, handlers, request); 
		} 
	);
	loc_inputIODataSet.setData(undefined, loc_viewIO);
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		getInputIODataSet : function(){
			return loc_inputIODataSet;
		}
	}
	
	return loc_out;
};

var node_createComponentDataView = function(){
	var loc_component;
	
	var loc_view = $('<div>Component Data: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_listener = node_createEventObject();

	var loc_clearup = function(){
		if(loc_component!=undefined){
			var comInterface = node_getComponentInterface(loc_component);
			comInterface.unregisterDataChangeEventListener(loc_listener);
			loc_component = undefined;
		}
	};

	var loc_showDataSet = function(dataSet){	loc_textView.val(JSON.stringify(dataSet, null, 4));	};
	
	var loc_setup = function(request){
		var comInterface = node_getComponentInterface(loc_component);
		comInterface.registerDataChangeEventListener(loc_listener, function(eventName, dataSet){
			loc_showDataSet(dataSet);
		});
		node_requestServiceProcessor.processRequest(comInterface.getContextDataSetRequest({
			success : function(request, dataSet){
				loc_showDataSet(dataSet);
			}
		}, request));
	};
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		setComponent : function(component, request){
			loc_clearup();
			loc_component = component;
			loc_setup(request);
		}
	};
	
	return loc_out;
};

var node_createComponentEventView = function(){
	var loc_component;
	var loc_view = $('<div>Component Event: </div>');
	var loc_textView = $('<textarea rows="5" cols="150" style="resize: none;" data-role="none"></textarea>');
	loc_view.append(loc_textView);

	var loc_clearup = function(){};
	
	var loc_setup = function(){
		var comInterface = node_getComponentInterface(loc_component);
		comInterface.registerEventListener(undefined, function(eventName, eventData, request){
			var content = loc_textView.val();
			content = content + "\n\n*****************************************\n\n";
			content = content + JSON.stringify({
				eventName : eventName,
				eventData : eventData
			}, null, 4);
			
			loc_textView.val(content);
		});
	};
	
	var loc_out = {
		getView : function(){  return loc_view;   },
		
		setComponent : function(component){
			loc_clearup();
			loc_component = component;
			loc_setup();
		}
	};
	return loc_out;
};


var node_createComponentLifeCycleDebugView = function(){

	var loc_view = $('<div></div>');
	
	var loc_component;
	
	var loc_stateView = {};
	var loc_commandView = {};
	
	var loc_lifecycle;
	var loc_stateMachine;

	var loc_updateCandidateView = function(all, candidates, views){
		if(candidates==undefined)  candidates = [];
		_.each(all, function(ele, i){
			if(candidates.includes(ele)){
				views[ele].css('color', 'green');
			}
			else{
				views[ele].css('color', 'red');
			}
		});		
	};

	var loc_setup = function(){
		loc_view.empty();
		loc_stateView = {};
		loc_commandView = {};
		
		loc_lifecycle = node_getComponentLifecycleInterface(loc_component);
		loc_stateMachine = loc_lifecycle.getStateMachine();
		
		var allStatesView = $('<div>All States : </div>');
		_.each(loc_stateMachine.getAllStates(), function(state, i){
			var stateView = $('<a>'+state+'</a>');
			allStatesView.append(stateView);
			allStatesView.append($('<span>&nbsp;&nbsp;</span>'));
			stateView.on('click', function(){
				event.preventDefault();
				loc_lifecycle.transit([state]);
			});
			loc_stateView[state] = stateView;
		});
		loc_view.append(allStatesView);

		var allCommandsView = $('<div>All Commands : </div>');
		_.each(loc_stateMachine.getAllCommands(), function(command, i){
			var commandView = $('<br><a>'+command+'</a>');
			allCommandsView.append(commandView);
			allCommandsView.append($('<span>&nbsp;&nbsp;</span>'));
			commandView.on('click', function(){
				event.preventDefault();
//				loc_lifecycle.command(command);
				loc_lifecycle.executeTransitRequest(command, {
					success : function(request){
						console.log('aaa');
					}
				});
			});
			loc_commandView[command] = commandView;
		});
		loc_view.append(allCommandsView);
		
		var stateHistoryBlockView = $('<div>State History : </div>');
		var currentStateBlockView = $('<div>Current State : </div>');
		var stateHistoryView = $('<span></span>');
		stateHistoryBlockView.append(stateHistoryView);
		var currentStateView = $('<span></span>');
		currentStateBlockView.append(currentStateView);
		loc_view.append(stateHistoryBlockView);
		loc_view.append(currentStateBlockView);
		stateHistoryView.text(loc_stateMachine.getCurrentState());
		currentStateView.text(loc_stateMachine.getCurrentState());
		loc_updateCandidateView(loc_stateMachine.getAllStates(), loc_stateMachine.getNextStateCandidates(), loc_stateView);
		loc_updateCandidateView(loc_stateMachine.getAllCommands(), loc_stateMachine.getCommandCandidates(), loc_commandView);
		loc_lifecycle.registerEventListener(undefined, function(eventName, eventData, request){
			if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " -- " + loc_stateMachine.getCurrentState());
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
			}
			else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
				stateHistoryView.text(stateHistoryView.text() + " XX " + loc_stateMachine.getCurrentState());
			}
			currentStateView.text(loc_stateMachine.getCurrentState());
			loc_updateCandidateView(loc_stateMachine.getAllStates(), loc_stateMachine.getNextStateCandidates(), loc_stateView);
			loc_updateCandidateView(loc_stateMachine.getAllCommands(), loc_stateMachine.getCommandCandidates(), loc_commandView);
		});
		
	};
	
	var loc_out = {
		
		getView : function(){   return loc_view;   },
		
		setComponent : function(component){
			loc_component = component;
			loc_setup();
		}
	};
	
	return loc_out;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComponentLifeCycleDebugView", node_createComponentLifeCycleDebugView); 
packageObj.createChildNode("createComponentDataView", node_createComponentDataView); 
packageObj.createChildNode("createComponentEventView", node_createComponentEventView); 
packageObj.createChildNode("createComponentResetView", node_createComponentResetView); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createConfigure = function(value){
	
	if(value!=undefined){
		var valueType = node_getObjectType(value);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE){
			return value;
		}
		else{
			var loc_configure = value;
		}
	}
	
	var loc_out = {
		
		getConfigureData : function(component){
			var out = {};

			if(component!=undefined)	_.extend(out, loc_configure.global, loc_configure.components==undefined?undefined : loc_configure.components[component]);
			else  _.extend(out, loc_configure.global);

			var temp = {};
			_.extend(temp, loc_configure);
			delete temp.global;
			delete temp.components;
			_.extend(out, temp);
			
			return out;
		}
	};
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTCONFIGURE);

	return loc_out;
};

	
var node_commandResult = function(requestResult, commandInfo){
	this.requestResult = requestResult;
	this.commandInfo = commandInfo;
};

var node_commandRequestInfo = function(name, parms, handlers, request){
	this.name = name;
	this.parms = parms;
	this.handlers = handlers;
	this.request = request;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createConfigure", node_createConfigure); 
packageObj.createChildNode("commandResult", node_commandResult); 
packageObj.createChildNode("commandRequestInfo", node_commandRequestInfo); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
	var node_getObjectName;
	var node_getObjectType;
	var node_requestServiceProcessor;
	var node_getComponentLifecycleInterface;

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "componentInterface";

var node_makeObjectWithComponentInterface = function(baseObject, interfaceObj, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createComponentInterfaceObj(thisContext==undefined?baseObject:thisContext, baseObject, interfaceObj));
};
	
var node_getComponentInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var loc_createComponentInterfaceObj = function(thisContext, baseObj, interfaceObj){

	var loc_thisContext = thisContext;

	var loc_interfaceObj = interfaceObj;
	
	var loc_baseObj = baseObj;
	
	var loc_idDataSet = loc_interfaceObj.prv_getIODataSet();
	
	var loc_init = function(){
		
	};

	var loc_dataChangeEventProcessor = function(eventName, eventData, request){
		loc_idDataSet.getGetDataSetValueRequest({
			success : function(request, dataSet){
				
			}
		}, request);
	};
	
	var loc_out = {

		getExecuteCommandRequest : function(command, parms, handlers, request){
			var lifycyclePrefix = 'lifecycle.';
			if(command.startsWith(lifycyclePrefix)){
				var lifecycle = node_getComponentLifecycleInterface(loc_baseObj);
				return lifecycle.getTransitRequest(command.substring(lifycyclePrefix.length), handlers, request);
			}
			else{
				return loc_interfaceObj.prv_getExecuteCommandRequest(command, parms, handlers, request);
			}
		},

		registerDataChangeEventListener : function(listener, handler){	
			return loc_idDataSet.registerEventListener(listener, 
				function(eventName, eventData, request){
					var dataRequest = loc_idDataSet.getGetDataSetValueRequest({
						success : function(request, dataSet){
							handler(eventName, dataSet);
						}
					}, request);
					node_requestServiceProcessor.processRequest(dataRequest);
				}, loc_baseObj);	
		},
		unregisterDataChangeEventListener : function(listener){  loc_idDataSet.unregisterEventListener(listener);  },
		
		getContextDataSetRequest : function(handlers, request){
			return loc_idDataSet.getGetDataSetValueRequest(handlers, request);
		},
		
		getIOContext : function(){  return loc_idDataSet;   },
		
		getComponent : function()  {  return loc_interfaceObj.prv_getComponent(); },
		
		registerEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerEventListener(listener, handler, loc_thisContext);	},
		unregisterEventListener : function(listener){  loc_interfaceObj.prv_unregisterEventListener(listener);  },

		registerValueChangeEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerValueChangeEventListener(listener, handler, loc_thisContext);	},
		unregisterValueChangeEventListener : function(listener){  loc_interfaceObj.prv_unregisterValueChangeEventListener(listener);  },

		registerValueChangeEventListener : function(listener, handler){	return loc_interfaceObj.prv_registerValueChangeEventListener(listener, handler, loc_thisContext);	},
		unregisterValueChangeEventListener : function(listener){  loc_interfaceObj.prv_unregisterValueChangeEventListener(listener);  },
	};

	loc_init();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.getObjectName", function(){node_getObjectName = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentInterface", node_makeObjectWithComponentInterface); 
packageObj.createChildNode("getComponentInterface", node_getComponentInterface); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
	var node_getObjectName;
	var node_getObjectType;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createStateMachine;
	var node_CommandInfo;
	var createStateMachineDef;
	var node_TransitInfo;
	var node_StateTransitPath;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "componentLifecycle";
	
/*
 * utility functions to build lifecycle object
 */
var node_makeObjectWithComponentLifecycle = function(baseObject, lifecycleCallback, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createComponentLifecycle(thisContext==undefined?baseObject:thisContext, lifecycleCallback));
};
	
var node_getComponentLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};

var loc_createComponentLifecycle = function(thisContext, lifecycleCallback){
	var loc_stateMachineDef;
	
	//this context for lifycycle callback method
	var loc_thisContext = thisContext;
	
	var loc_baseObject;
	
	//life cycle call back including all call back method
	var loc_lifecycleCallback = lifecycleCallback==undefined? {}:lifecycleCallback;
	
	var loc_stateMachine;

	var loc_init = function(){
		loc_stateMachineDef = node_createStateMachineDef();
		var loc_validTransits = [
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD),
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED),
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT),
			new node_TransitInfo(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE),
		];

		var loc_commands = [
			new node_CommandInfo("activate", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
			new node_CommandInfo("deactive", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT]),
			new node_CommandInfo("suspend", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED]),
			new node_CommandInfo("resume", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),

			new node_CommandInfo("destroy", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
			new node_CommandInfo("destroy", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
			new node_CommandInfo("destroy", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_SUSPENDED, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD]),
			
			new node_CommandInfo("restart", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
			new node_CommandInfo("restart", node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE]),
		];

		var loc_statePaths = [
			new node_StateTransitPath(node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_DEAD, [node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT])
		];
		
		_.each(loc_validTransits, function(transit, i){		
			var from = transit.from;
			var to = transit.to;
			loc_stateMachineDef.addStateInfo(transit, 
				function(){
					var fun = loc_lifecycleCallback[transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
					else  return true;
				}, 
				function(){
					var fun = loc_lifecycleCallback["_"+transit.from+"_"+transit.to];
					if(fun!=undefined)   return fun.apply(loc_thisContext, arguments);
					else  return true;
				});
		});
		_.each(loc_commands, function(commandInfo, i){      loc_stateMachineDef.addCommand(commandInfo);      });
		_.each(loc_statePaths, function(statePath, index){  loc_stateMachineDef.addTransitPath(statePath);  });
		
		loc_stateMachine = node_createStateMachine(loc_stateMachineDef, node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_INIT, loc_thisContext);
	};

	var loc_out = {

		transit : function(commandName, request){  
			var task = loc_stateMachine.newTask(commandName);
			if(task!=undefined)  	return task.process(request);
		},
		getTransitRequest : function(commandName, handlers, request){
			var task = loc_stateMachine.newTask(commandName);
			if(task!=undefined)  	return task.getProcessRequest(handlers, request);
		},
		executeTransitRequest : function(commandName, handlers, request){
			var request = loc_out.getTransitRequest(commandName, handlers, request);
			node_requestServiceProcessor.processRequest(request);
		},
			
		getStateMachine : function(){  return loc_stateMachine;   },
		getComponentStatus : function(){		return loc_stateMachine.getCurrentState();		},
		isActive : function(){  return this.getComponentStatus()==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },
		
		registerEventListener : function(listener, handler){	return loc_stateMachine.prv_registerEventListener(listener, handler, thisContext);	},
		unregisterEventListener : function(listener){  loc_stateMachine.prv_unregisterEventListener(listener);  },

		bindBaseObject : function(baseObject){		loc_baseObject = baseObject;	}
	};

	loc_init();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.getObjectName", function(){node_getObjectName = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachine", function(){node_createStateMachine = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.CommandInfo", function(){node_CommandInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineDef", function(){node_createStateMachineDef = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.TransitInfo", function(){node_TransitInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("statemachine.StateTransitPath", function(){node_StateTransitPath = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});


//Register Node by Name
packageObj.createChildNode("makeObjectWithComponentLifecycle", node_makeObjectWithComponentLifecycle); 
packageObj.createChildNode("getComponentLifecycleInterface", node_getComponentLifecycleInterface); 

})(packageObj);
var library = nosliw.getPackage("uimodule");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_uiEventData = function(uiName, eventName, eventData){
	this.uiName = uiName;
	this.eventName = eventName;
	this.eventData = eventData;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("uiEventData", node_uiEventData); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_contextUtility;
	var node_requestServiceProcessor;
	var node_getLifecycleInterface;
	var node_makeObjectWithType;
	var node_makeObjectWithLifecycle;
	var node_destroyUtil;
	var node_createDataAssociation;
	var node_createDynamicData;

//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleUIRequest = function(moduleUIDef, moduleIOContext, decorations, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleUI", {"moduleUIDef":moduleUIDef, "moduleContext":moduleIOContext}), handlers, request);
	
	//generate page
	out.addRequest(nosliw.runtime.getUIPageService().getGenerateUIPageRequest(moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_PAGE], undefined, {
		success :function(requestInfo, page){
			var moduleUI = node_createModuleUI(moduleUIDef, page, moduleIOContext);
			
			//append decorations
			if(decorations!=null){
				_.each(decorations, function(decoration, index){
					moduleUI.addDecoration(decoration);
				});
			}
			return moduleUI;
		}
	}));
	return out;
};

var node_createModuleUI = function(moduleUIDef, page, moduleIOContext){
	var loc_moduleUIDef = moduleUIDef;
	var loc_page = page;
	//io between module context and page context
	var loc_inputDataAssociation = node_createDataAssociation(moduleIOContext, loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_INPUTMAPPING], node_createDynamicData(
		function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_page.getContextEleValueAsParmsRequest());
			return out;
		}, 
		function(value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var pageInput = {};
			pageInput = _.extend(pageInput, value);
			//combine data from module with extra data in ui
			_.each(loc_extraContextData, function(data, name){
				pageInput = _.extend(pageInput, data);
			});
			//update page with data
			out.addRequest(loc_page.getUpdateContextRequest(pageInput));
			return out;
		}
	));
	
	var loc_outputDataAssociation = node_createDataAssociation(node_createDynamicData(
		function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_page.getBuildContextGroupRequest());
			return out;
		}
	), loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_OUTPUTMAPPING], moduleIOContext);

	var loc_extraContextData = {};

	var loc_getRefreshRequest = function(handlers, request){
		return loc_getRefreshPageDataRequest(handlers, request);
	};

	var loc_getRefreshPageDataRequest = function(handlers, request){
		return loc_inputDataAssociation.getExecuteRequest(handlers, request);
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(moduleUIDef, page){
		loc_out.setExtraContextData("nosliw_ui_uiInfo", {
			nosliw_uiInfo :{
				id : loc_out.getName(),
				title : loc_out.getName()
			}
		});
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY]  = function(request){
		node_destroyUtil(loc_page, request);
	};

	var loc_out = {
		
		getPage : function(){		return loc_page;		},
		
		getName : function(){	return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];	},
		
		getEventHandler : function(eventName){   return loc_moduleUIDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_EVENTHANDLER][eventName];   },
			
		addDecoration : function(decoration){		loc_page.addDecoration(decoration);	},	

		getGetStateRequest : function(handlers, requestInfo){  return loc_page.getGetPageStateRequest(handlers, requestInfo);  },
		getSetStateRequest : function(stateData, handlers, requestInfo){  return loc_page.getUpdateContextRequest(stateData, handlers, requestInfo);  },
		
		setExtraContextData : function(name, extraContextData){  loc_extraContextData[name] = extraContextData;  },
		getExtraContextData : function(name){  return loc_extraContextData[name];  },
		getUpdateExtraContextDataRequest : function(name, extraContextData){
			this.setExtraContextData(name, extraContextData);
			return loc_page.getUpdateContextRequest(extraContextData);
		},
		
		getUpdateContextRequest : function(parms, handlers, requestInfo){	return loc_page.getUpdateContextRequest(parms, handlers, requestInfo);	},
		executeUpdateContextRequest : function(parms, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getUpdateContextRequest(parms, handlers, requestInfo));	},

		//take command
		getExecuteCommandRequest : function(commandName, parms, handlers, request){		
			if(commandName=="syncInData"){
				return loc_out.getSynInDataRequest(handlers, request);
			}
			else{
				return loc_page.getExecuteCommandRequest(commandName, parms, handlers, request);	
			}
		},
		executeCommandRequest : function(commandName, parms, handlers, request){	node_requestServiceProcessor.processRequest(this.getExecuteCommandRequest(commandName, parms, handlers, request));	},

		registerEventListener : function(listener, handler, thisContext){		return loc_page.registerEventListener(listener, handler, thisContext);	},
		registerValueChangeEventListener : function(listener, handler, thisContext){	return	loc_page.registerValueChangeEventListener(listener, handler, thisContext);	},
		
		getSynInDataRequest : function(handlers, request){  return loc_inputDataAssociation.getExecuteRequest(handlers, request);  },
		
		getSynOutDataRequest : function(name, handlers, request){	return loc_outputDataAssociation.getExecuteRequest(handlers, request);	},
		executeSynOutDataRequest : function(name, handlers, request){	node_requestServiceProcessor.processRequest(this.getSynOutDataRequest(name, handlers, request));	}
		
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULEUI);

	node_getLifecycleInterface(loc_out).init(moduleUIDef, page);

	return loc_out;
	
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleUIRequest", node_createModuleUIRequest); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_createEventObject;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getLifecycleInterface;
	var node_createModuleUIRequest;
	var node_createUIDecorationsRequest;
	var node_createIODataSet;
	var node_objectOperationUtility;
	var node_uiEventData;
	var node_destroyUtil;

//*******************************************   Start Node Definition  ************************************** 	
//module entity store all the status information for module
var node_createUIModuleRequest = function(id, uiModuleDef, decorations, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createUIModule", {"uiModule":uiModuleDef}), handlers, request);

	var module = loc_createUIModule(id, uiModuleDef, ioInput);

	//prepare decoration first
	var decorationInfo = {};
	if(uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_DECORATION]!=null)  decorationInfo = uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_DECORATION];
	if(decorations!=null){
		if(decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL]!=undefined)   decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL]=decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
		if(decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)   decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]=decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI];
	}
	
	//build module ui
	var buildModuleUIsRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("BuildModuleUIs", {}), {
		success : function(request, resultSet){
			_.each(resultSet.getResults(), function(moduleUI, index){
				module.prv_addUI(moduleUI);
			});
			return module;
		}
	});

	// build uis
	_.each(uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_UI], function(ui, index){
		
		var buildModuleUIRequest = node_createServiceRequestInfoSequence();

		var uiName = uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULEUI_ID];
		var decs;
		if(decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI]!=undefined)  decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_UI][uiName]; 
		if(decs==undefined) decs = decorationInfo[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL];
		buildModuleUIRequest.addRequest(node_createUIDecorationsRequest(decs, {
			success : function(request, decorations){
				return node_createModuleUIRequest(ui, module.getIOContext(), decorations);
			}
		}));

		buildModuleUIsRequest.addRequest(index, buildModuleUIRequest);
	});
	out.addRequest(buildModuleUIsRequest);
	
	return out;
};	
	
var loc_createUIModule = function(id, uiModuleDef, ioInput){
	var loc_ioInput = ioInput;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	var loc_valueChangeEventListener = node_createEventObject();
	var loc_valueChangeEventSource = node_createEventObject();
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_updateIOContext = function(input){
		var data = loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_INITSCRIPT](input);
		loc_out.prv_module.ioContext.setData(undefined, data);
	};
	
	var loc_init = function(){
		loc_out.prv_module.ioContext.registerEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
			if(loc_out.prv_module.lifecycle.isActive()==true){
				loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, undefined, request);
			}
		});
	};
	
	var loc_out = {
		prv_module : {
			id : id,
			uiModuleDef : uiModuleDef,
			
			ioContext : node_createIODataSet(),
			
			uiArray : [],
			ui : {},

			lifecycle : undefined    //module's lifecycle obj
		},
		
		prv_addUI : function(ui){
			loc_out.prv_module.uiArray.push(ui);
			loc_out.prv_module.ui[ui.getName()] = ui;
			//register listener for module ui
			ui.registerEventListener(loc_out.prv_module.eventListener, function(eventName, eventData, requestInfo){
				loc_trigueEvent(node_CONSTANT.MODULE_EVENT_UIEVENT, new node_uiEventData(this.getName(), eventName, eventData), requestInfo);
			}, ui);
			ui.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, requestInfo){
				//handle ui value change, update value in module
				this.executeSynOutDataRequest(undefined, undefined, requestInfo);
			}, ui);
		},
	
		setLifecycle : function(lifecycle){  this.prv_module.lifecycle = lifecycle;   },
		
		getId : function(){  return loc_out.prv_module.id;  },
		getVersion : function(){   return "1.0.0";   },
		
		getIOContext : function(){  return loc_out.prv_module.ioContext;  },
		
		getUIs : function(){  return loc_out.prv_module.uiArray;  },
		getUI : function(name) {  return loc_out.prv_module.ui[name];   },
		getRefreshUIRequest : function(uiName, handlers, request){	return this.getUI(uiName).getSynInDataRequest(handlers, request);	},
		
		getEventHandler : function(uiName, eventName){   return this.getUI(uiName).getEventHandler(eventName);   },
		
		getProcess : function(name){  return loc_out.prv_module.uiModuleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEMODULE_PROCESS][name];  },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getInitIOContextRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(loc_ioInput!=undefined){
				out.addRequest(loc_ioInput.getGetDataValueRequest(undefined, {
					success : function(request, data){
						loc_updateIOContext(data);
					}
				}));
			}
			else{
				loc_updateIOContext();
			}
			return out;
		},
		
		getExecuteCommandRequest : function(commandName, parm, handlers, requestInfo){},
		getPart : function(partId){ 	return node_objectOperationUtility.getObjectAttributeByPath(loc_out.prv_module, partId); },
		
		getDestroyRequest : function(handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				node_destroyUtil(loc_out.prv_module.ioContext, request);
				
				_.each(loc_out.prv_module.uiArray, function(ui, i){
					node_destroyUtil(ui, request);
				});
				loc_out.prv_module.uiArray = undefined;
				loc_out.prv_module.ui = undefined;
				loc_out.prv_module.uiModuleDef = undefined;
				loc_out.prv_module.lifecycle = undefined;
			}, handlers, request);
		}
	};

	loc_init();
	
	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_UIMODULE);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleUIRequest", function(){node_createModuleUIRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIDecorationsRequest", function(){node_createUIDecorationsRequest = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.uiEventData", function(){node_uiEventData = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleRequest", node_createUIModuleRequest); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIModuleRequest;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentInterface;
	var node_getComponentInterface;
	var node_createComponentComplex;
	var node_createStateBackupService;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleRuntimeRequest = function(id, uiModuleDef, configure, componentDecorationInfos, rootView, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef}), handlers, request);
	out.addRequest(node_createUIModuleRequest(id, uiModuleDef, undefined, ioInput, {
		success : function(request, uiModule){
			var runtime = loc_createModuleRuntime(uiModule, configure, componentDecorationInfos, rootView, request);
			return runtime.prv_getInitRequest({
				success : function(request){
					return request.getData();
				}
			}).withData(runtime);
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModule, configure, componentDecorationInfos, rootView, request){
	
	var loc_componentComplex = node_createComponentComplex(configure);
	var loc_localStore = configure.getConfigureData().__storeService;
	var loc_stateBackupService = node_createStateBackupService("module", uiModule.getId(), uiModule.getVersion(), loc_localStore);

	var loc_init = function(uiModule, configure, componentDecorationInfos, rootView, request){
		loc_componentComplex.addComponent(uiModule);
		loc_componentComplex.addDecorations(componentDecorationInfos);
	};

	var loc_getIOContext = function(){  return loc_out.prv_getComponent().getIOContext();   };
	
	var loc_getProcessEnv = function(){   return loc_componentComplex.getInterface();    };
	
	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteProcessRequest(process, loc_out.prv_getComponent().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_out.prv_getComponent().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	var loc_getGoActiveRequest = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentComplex.getStartRequest());
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("active"));
		return out;
	};
	
	var loc_getResumeActiveRequest = function(stateData, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ResumeUIModuleRuntime", {}), undefined, request);

		loc_componentComplex.setAllStateData(stateData.state);
		
		var backupContextData = stateData.context;
		_.each(backupContextData, function(contextData, name){
			out.addRequest(loc_getIOContext().getSetDataValueRequest(name, contextData));
		});
		
		out.addRequest(loc_componentComplex.getResumeRequest());
		
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("resume"));
		return out;
	};
	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DestroyUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentComplex.getDestroyRequest());
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out;
		var stateData = loc_stateBackupService.getBackupData();
		if(stateData==undefined)	out = loc_getGoActiveRequest(request);
		else	out = loc_getResumeActiveRequest(stateData, request);
		return out;
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]=
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DeactiveUIModuleRuntime", {}), undefined, request);
		out.addRequest(loc_componentComplex.getDeactiveRequest());
		loc_componentComplex.clearState();
		return out;
	};	

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SuspendUIModuleRuntime", {}), undefined, request);
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("suspend"));
		out.addRequest(loc_componentComplex.getSuspendRequest());
		
		out.addRequest(loc_getIOContext().getGetDataSetValueRequest({
			success : function(request, contextDataSet){
				var backupData = {
						state : loc_componentComplex.getAllStateData(),
						context : contextDataSet,
					};
				loc_stateBackupService.saveBackupData(backupData);
			}
		}));
		
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){
		loc_stateBackupService.clearBackupData();
	};

	var loc_out = {
		
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentComplex.getPreDisplayInitRequest());
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_componentComplex.updateView(rootView, request);
			}));
			out.addRequest(loc_componentComplex.getInitRequest());
			return out;
		},

		prv_getExecuteCommandRequest : function(command, parms, handlers, request){	
			return loc_componentComplex.getExecuteCommandRequest(command, parms, handlers, request);	
		},

		prv_getComponent : function(){  return loc_componentComplex.getComponent();   },
		prv_getIODataSet : function(){  return loc_getIOContext();	},

		prv_registerEventListener : function(listener, handler, thisContext){	return loc_componentComplex.registerEventListener(listener, handler, thisContext);	},
		prv_unregisterEventListener : function(listener){	return loc_componentComplex.unregisterEventListener(listener); },

		prv_registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_componentComplex.registerValueChangeEventListener(listener, handler, thisContext);	},
		prv_unregisterValueChangeEventListener : function(listener){	return loc_componentComplex.unregisterValueChangeEventListener(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){	
			return node_getComponentInterface(loc_out).getExecuteCommandRequest(command, parms, handlers, request);
		},
		
		getInterface : function(){   return node_getComponentInterface(loc_out);  },
		
	};
	
	loc_init(uiModule, configure, componentDecorationInfos, rootView, request);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);
	
	loc_out = node_makeObjectWithComponentInterface(loc_out, loc_out);

	loc_componentComplex.getComponent().setLifecycle(node_getComponentLifecycleInterface(loc_out));
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentInterface", function(){node_makeObjectWithComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentComplex", function(){node_createComponentComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_createConfigure;
	var node_loadComponentResourceRequest;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){
	
	var loc_out = {

		getGetUIModuleRuntimeRequest : function(id, module, configure, ioInput, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource"), handlers, request);

			configure = node_createConfigure(configure);
			var componentDecorationInfo = configure.getConfigureData().moduleDecoration;
			out.addRequest(node_loadComponentResourceRequest(
				typeof module === 'string'? 
					{
						componentResourceId : module,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE
					} : module, 
				componentDecorationInfo==undefined?undefined:
					{
						decoration : componentDecorationInfo,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION
					},
				{
					success : function(request, componentInfo){
						//create ui module runtime
						return node_createModuleRuntimeRequest(id, componentInfo.component, configure, componentInfo.decoration, configure.getConfigureData().root, ioInput, {
							success : function(request, uiModuleRuntime){
								return uiModuleRuntime;
							}
						});
					}
				}));
			
			return out;
		},			
			
		executeGetUIModuleRuntimeRequest : function(id, resourceId, configure, ioInput, handlers, requester_parent){
			var requestInfo = this.getGetUIModuleRuntimeRequest(id, resourceId, configure, ioInput, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleRuntimeRequest", function(){node_createModuleRuntimeRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.loadComponentResourceRequest", function(){node_loadComponentResourceRequest = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
var library = nosliw.getPackage("uiapp");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_appDataService = function(){

	var loc_catchedAppData = {};
	
	var loc_getCachedAppData = function(ownerInfo, dataName){
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined)   return;
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined)  return;
		
		return appDataByName[dataName];
	};
	
	var loc_updateCachedAppData = function(ownerInfo, dataName, appData){
		var ownerType = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE];
		if(ownerType==undefined)  ownerType = node_COMMONCONSTANT.MINIAPP_DATAOWNER_APP;
		var appDataByOwnerId = loc_catchedAppData[ownerType];
		if(appDataByOwnerId==undefined){
			appDataByOwnerId = {};
			loc_catchedAppData[ownerType] = appDataByOwnerId;
		}
		
		var ownerId = ownerInfo[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID];
		var appDataByName = appDataByOwnerId[ownerId];
		if(appDataByName==undefined){
			appDataByName = {};
			appDataByOwnerId[ownerId] = appDataByName;
		}
		
		appDataByName[dataName] = appData;
	};
	
	var loc_getGetAppDataRequest = function(ownerInfo, dataName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var dataNames;
		if(dataName!=undefined){
			if(Array.isArray(dataName))   dataNames = dataName;
			else dataNames = [dataName];
		}
		
		var appDataByName = {};

		if(dataNames!=undefined){
			var notCached = [];
			_.each(dataNames, function(name, index){
				var appData = loc_getCachedAppData(ownerInfo, name);
				if(appData!=undefined){
					appDataByName[name] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
				}
				else{
					notCached.push(name);
				}
			});
			dataNames = notCached;
		}
		
		if(dataNames!=undefined && dataNames.length==0){
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return appDataByName;
			}));
		}
		else{
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_OWNER] = ownerInfo;
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_GETAPPDATA_DATANAME] = dataNames;

			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
				success : function(request, appDataInfo){
					var dataByName = appDataInfo[node_COMMONATRIBUTECONSTANT.MINIAPPSETTINGDATA_DATABYNAME];
					if(dataNames==undefined){
						_.each(dataByName, function(appData, dataName){
							if(appData==undefined)   appData = [];
							loc_updateCachedAppData(ownerInfo, dataName, appData);
							appDataByName[dataName] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
						});
					}
					else{
						_.each(dataNames, function(dataName, index){
							var appData = dataByName[dataName];
							if(appData==undefined){
								appData = {};
								appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = [];
								appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_NAME] = dataName;
							}
							loc_updateCachedAppData(ownerInfo, dataName, appData);
							appDataByName[dataName] = appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
						});
					}
					return appDataByName;
				}
			});
			out.addRequest(gatewayRequest);
		}
		
		return out;
	};

	var loc_getUpdateAppDataRequest = function(ownerInfo, dataByName, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_GATEWAY_APPDATA;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_OWNER] = ownerInfo;
		
		var appDataByName = {};
		_.each(dataByName, function(data, name){
			var appData = loc_getCachedAppData(ownerInfo, name);
			if(appData!=undefined){
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = data;
			}
			else{
				appData = {};
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA] = data;
				appData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_NAME] = name;
			}
			appDataByName[name] = appData;
		});
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYAPPDATA_COMMAND_UPDATEAPPDATA_DATABYNAME] = appDataByName;

		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(request, updatedUserData){
				var out = {};
				var updatedDataByName = updatedUserData[node_COMMONATRIBUTECONSTANT.MINIAPPSETTINGDATA_DATABYNAME];
				_.each(updatedDataByName, function(updatedData, name){
					loc_updateCachedAppData(ownerInfo, name, updatedData);
					out[name] = updatedData[node_COMMONATRIBUTECONSTANT.SETTINGDATA_DATA];
				});
				return out;
			}
		}, request);
		out.addRequest(gatewayRequest);
		return out;
	};
	
	var loc_out = {
			
			getGetAppDataRequest : function(ownerInfo, dataName, handlers, request){
				return loc_getGetAppDataRequest(ownerInfo, dataName, handlers, request);
			},
			
			getGetAppDataSegmentInfoRequest : function(ownerInfo, dataName, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appData){
						var dataNames;
						if(Array.isArray(dataName))   dataNames = dataName;
						else dataNames = [dataName];

						var dataInfos = {};
						_.each(dataNames, function(name, i){
							var dataInfo = [];
							_.each(appData[name], function(dataEle, i){
								dataInfo.push(new node_ApplicationDataSegmentInfo(ownerInfo, name, dataEle.id, dataEle.version));
							});
							dataInfos[name] = dataInfo;
						});
						return dataInfos;
					}
				}));
				return out;
			},	

			getGetAppDataSegmentByIdRequest : function(ownerInfo, dataName, id, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appDataByName){
						var appData = appDataByName[dataName];
						var find = _.find(appData, function(dataSeg, index){
							return dataSeg.id==id;
						});
						return find;
					}
				}));
				return out;
			},	

			getAddAppDataSegmentRequest : function(ownerInfo, dataName, index, id, data, version, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appDataByName){
						var appDataSegment = {
							id : id,
							version : version,
							data : data,
						};
						appDataByName[dataName].splice(index, 0, appDataSegment);
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request, appData){
								return appDataSegment;
							}
						});
					}
				}));
				return out;
			},	
				
			getUpdateAppDataSegmentRequest : function(ownerInfo, dataName, segmentId, value, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appDataByName){
						var appData = appDataByName[dataName];
						var find = _.find(appData, function(dataSeg, index){
							return dataSeg.id==segmentId;
						});
						find.data = value;
						var appDataByName = {};
						appDataByName[dataName] = appData;
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request){
								return value;
							}
						});
					}
				}));
				return out;
			},	

			getDeleteAppDataSegmentRequest : function(ownerInfo, dataName, segmentId, handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(loc_getGetAppDataRequest(ownerInfo, dataName, {
					success : function(request, appDataByName){
						var appData = appDataByName[dataName];
						var findIndex = -1;
						var find = _.find(appData, function(dataSeg, index){
							if(dataSeg.id==segmentId){
								findIndex = index;
								return true;
							}
							else return false;
						});
						appData.splice(findIndex, 1);
						var appDataByName = {};
						appDataByName[dataName] = appData;
						return loc_getUpdateAppDataRequest(ownerInfo, appDataByName, {
							success : function(request){
								return;
							}
						});
					}
				}));
				return out;
			},	

	};

	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});


//Register Node by Name
packageObj.createChildNode("appDataService", node_appDataService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createAppRuntimeRequest;
	var node_resourceUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_appDataService = function(){
	var loc_data = [
		{
			id : "id1",
			version : "version1",
			data : {
				schoolTypeInData : {
					"dataTypeId": "test.options;1.0.0",
					"value": {
						"value" : "Public",
						"optionsId" : "schoolType"
					}
				},
				schoolRatingInData : {
					"dataTypeId": "test.float;1.0.0",
					"value": 9.0
				}
			}
		},
		{
			id : "id2",
			version : "version2",
			data : {
				schoolTypeInData : {
					"dataTypeId": "test.options;1.0.0",
					"value": {
						"value" : "Public",
						"optionsId" : "schoolType"
					}
				},
				schoolRatingInData : {
					"dataTypeId": "test.float;1.0.0",
					"value": 9.0
				}
			}
		},
	];
	
	var loc_out = {
			
		getGetAppDataInfoRequest : function(dataName, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var out = [];
				_.each(loc_data, function(dataEle, i){
					out.push(new node_ApplicationDataInfo(dataName, dataEle.id, dataEle.version));
				});
				return out;
			}, handlers, requester_parent);
		},	

		getGetAppDataByIdRequest : function(dataName, id, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var out;
				_.each(loc_data, function(data, index){
					if(data.id==id){
						out = data;
					}
				});
				return out;
			}, handlers, requester_parent);
		},	

		getAddAppDataRequest : function(dataName, data, version, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				var appData = {
					id : nosliw.generateId(),
					version : version,
					data : data,
				};
				loc_data.push(appData);
				return appData;
			}, handlers, requester_parent);
		},	
			
		getDeleteAppDataRequest : function(dataName, id, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				for(var i in loc_data){
					if(loc_data[i].id==id){
						loc_data.splice(i, 1);
						return;
					}
				}
			}, handlers, requester_parent);
		},	

		getUpdateAppDataRequest : function(dataName, id, appData, handlers, requester_parent){
			return node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				for(var i in loc_data){
					if(loc_data[i].id==id){
						loc_data[i].data = appData;
						return appData;
					}
				}
			}, handlers, requester_parent);
		},	
	};

	return loc_out;
}();


var node_storeService = function(){
	
	var loc_out = {
		saveData : function(categary, id, data){
			localStorage.setItem(categary+"_"+id, JSON.stringify(data));
		},
		
		retrieveData : function(categary, id){
			return JSON.parse(localStorage.getItem(categary+"_"+id));
		},
		
		clearData : function(categary, id){
			return localStorage.removeItem(categary+"_"+id);
		}
	};
	return loc_out;
	
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppRuntimeRequest", function(){node_createAppRuntimeRequest = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("appDataService1", node_appDataService); 
packageObj.createChildNode("storeService", node_storeService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTypicalConfigure = function(mainModuleRoot, settingModuleRoot, dataService, storeService, framework7App){
	var configure = {
		global : {
			appDecoration : [
				{
					coreFun: node_createAppDecoration,
					id : "application"
				}
				],
			__appDataService : dataService,
			__storeService : storeService,
			app : framework7App,
		},
		components : {
			application : {
				"components" : {
					"application" : {
						global : {
							"root" : mainModuleRoot,
							"decoration" : {
								global : ["Decoration_application_framework7"]
							},
							"moduleDecoration" : ["base", "uidecoration", "application_framework7_mobile"]
//							"moduleDecoration" : ["base", "uidecoration", "application_framework7_mobile", "debug"]
						}
					},
					"setting" : {
						global : {
							"root" : settingModuleRoot,
							"decoration" : {
							},
							"moduleDecoration" : ["base", "uidecoration", "setting_framework7_mobile"]
//							"moduleDecoration" : ["base", "uidecoration", "setting_framework7_mobile", "debug"]
						},
						components : {
							"setting_framework7_mobile" : {
								uiResource : "Decoration_setting_framework7"
							}
						}
					}
				}
			}
		}
	};
	return configure;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTypicalConfigure", node_createTypicalConfigure); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_ApplicationDataSegmentInfo = function(ownerInfo, dataName, dataId, dataVersion, persist){
	this.ownerInfo = ownerInfo;
	this.dataName = dataName;
	this.id = dataId;
	this.version = dataVersion;
	this.persist = persist;
	if(this.persist==undefined)   this.persist = true;
};

var node_ApplicationDataSegment = function(data, dataId, dataVersion){
	this.data = data;
	this.id = dataId;
	this.version = dataVersion;
};

var node_ModuleInfo = function(moduleDef){
	this.id = undefined;
	this.name = moduleDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
	this.root = undefined;
	this.module = undefined;
	this.moduleDef = moduleDef;
	this.role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
	this.applicationDataInfo = [];  //application data info for this module
	this.externalIO = undefined; //
	this.inputMapping = {};
	this.inputIO = undefined;
	this.currentInputMapping = undefined;
	this.outputMapping = {};
};
		
var node_ModuleEventData = function(moduleInfo, eventName, eventData){
	this.moduleInfo = moduleInfo;
	this.eventName = eventName;
	this.eventData = eventData;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ModuleInfo", node_ModuleInfo); 
packageObj.createChildNode("ApplicationDataSegmentInfo", node_ApplicationDataSegmentInfo); 
packageObj.createChildNode("ModuleEventData", node_ModuleEventData); 
packageObj.createChildNode("ApplicationDataSegment", node_ApplicationDataSegment); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createPatternMatcher;
	var node_Pattern;
	var node_createEventObject;
	var node_createIODataSet;
	var node_createServiceRequestInfoSequence;
	var node_ModuleEventData;
	var node_ModuleInfo;
	var node_ApplicationDataInfo;
	
//*******************************************   Start Node Definition  **************************************

var node_createApp = function(id, appDef, ioInput){
	var loc_ioInput = ioInput;
	
	var loc_partMatchers = node_createPatternMatcher([
		new node_Pattern(new RegExp("module\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).module;}),
		new node_Pattern(new RegExp("module\.(\\w+)\.outputMapping\.(\\w+)$"), function(result){return loc_out.getCurrentModuleInfo(result[1]).outputMapping[result[2]];}),
		new node_Pattern(new RegExp("module\.(\\w+)\.inputMapping\.(\\w+)$"), function(result){	return loc_out.getCurrentModuleInfo(result[1]).inputMapping[result[2]];	}),
	]);
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_moduleEventProcessor = function(eventName, eventData, request){
		loc_trigueEvent(node_CONSTANT.APP_EVENT_MODULEEVENT, new node_ModuleEventData(this, eventName, eventData), request);
	};

	var loc_moduleValueChangeEventProcessor = function(eventName, eventData, request){
		loc_trigueValueChangeEvent(node_CONSTANT.EVENT_COMPONENT_VALUECHANGE, new node_ModuleEventData(this, eventName, eventData), request);
	};
	
	var loc_updateIOContext = function(input){
		var data = loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_INITSCRIPT](input);
		loc_out.prv_app.ioContext.setData(undefined, data);
	};

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {

		prv_app : {
			id : id,
			version : "1.0.0",
			
			appDef : appDef,
			
			modulesByRole : {},
			currentModuleByRole : {},
			
			ioContext : node_createIODataSet(),
		},
			
		getId : function(){  return loc_out.prv_app.id;  },
		getVersion : function(){   return "1.0.0";   },
		
		getIOContext : function(){  return loc_out.prv_app.ioContext;  },
		
		getPart : function(partId){		return loc_partMatchers.match(partId);	},
		
		getProcess : function(name){  return loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_PROCESS][name];  },

		getEventHandler : function(moduleName, eventName){
			var moduleDef = loc_out.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE][moduleName];
			return moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_EVENTHANDLER][eventName];
		},

		getInitIOContextRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(loc_ioInput!=undefined){
				out.addRequest(loc_ioInput.getGetDataValueRequest(undefined, {
					success : function(request, data){
						loc_updateIOContext(data);
					}
				}));
			}
			else{
				loc_updateIOContext();
			}
			return out;
		},
		
		addModuleInfo : function(moduleInfo){
			var role = moduleInfo.role;
			var module = moduleInfo.module;
			
			var modules = loc_out.prv_app.modulesByRole[role];
			if(modules==undefined){
				modules = [];
				loc_out.prv_app.modulesByRole[role] = modules;
			}
			modules.push(moduleInfo);
			loc_out.setCurrentModuleInfo(role, moduleInfo.id);
			
			module.prv_registerEventListener(loc_eventListener, loc_moduleEventProcessor, moduleInfo);
			module.prv_registerValueChangeEventListener(loc_valueChangeEventListener, loc_moduleValueChangeEventProcessor, moduleInfo);
			return moduleInfo;
		},
		
		removeModuleInfo : function(role, moduleId){
			var modules = loc_out.prv_app.modulesByRole[role];
			var currentId = loc_out.prv_app.currentModuleByRole[role];
			for(var index in modules){
				if(moduleId==modules[index].id){
					break;
				}
			}
			
			if(currentId==moduleId){
				if(index-1>=0)		loc_out.prv_app.currentModuleByRole[role] = modules[index-1].id;
				else loc_out.prv_app.currentModuleByRole[role] = undefined;
			}
			
			modules.splice(index, 1);
		},
		
		getCurrentModuleInfo : function(role){	return loc_out.getModuleInfo(role);	},
		
		setCurrentModuleInfo : function(role, moduleId){	loc_out.prv_app.currentModuleByRole[role] = moduleId;	},
		
		getAllModuleInfo : function(){
			var out = [];
			_.each(loc_out.prv_app.modulesByRole, function(modulesByRole, role){
				_.each(modulesByRole, function(moduleInfo){
					out.push(moduleInfo);
				});
			});
			return out;
		},
		
		getModuleInfo : function(role, id){
			if(id==undefined){
				id = loc_out.prv_app.currentModuleByRole[role];
			}
			var modules = loc_out.prv_app.modulesByRole[role];
			for(var i in modules){
				if(modules[i].id==id)  return modules[i];
			}
		},
		
		clearModuleInfo : function(){
			loc_out.prv_app.modulesByRole = {};
			loc_out.prv_app.currentModuleByRole = {};
		},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.createPatternMatcher", function(){node_createPatternMatcher = this.getData();});
nosliw.registerSetNodeDataEvent("common.patternmatcher.Pattern", function(){node_Pattern = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.ModuleEventData", function(){node_ModuleEventData = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApp", node_createApp); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_createDataAssociation;
	var node_ModuleInfo;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_getComponentLifecycleInterface;
	var node_createEventObject;
	var node_requestServiceProcessor;
	var node_appUtility;
	var node_ApplicationDataSegmentInfo;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppDecoration = function(gate){

	var ROLE_APPLICATION = "application";
	var ROLE_SETTING = "setting";
	
	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponent();
	var loc_uiAppDef = loc_uiApp.prv_app.appDef;
	var loc_configureData = loc_gate.getConfigureData();
	var loc_appDataService = loc_configureData.__appDataService;
	
	var loc_getModuleConfigureData = function(role){
		return node_createConfigure(loc_configureData).getConfigureData(role);
	};
	
	var loc_settingParentView = loc_getModuleConfigureData(ROLE_SETTING).global.root;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_createSettingModuleRequest = function(moduleDef, dataInfo, handlers, request){
		var configureData = loc_getModuleConfigureData(ROLE_SETTING); 
		configureData.root = $('<div></div>').get(0);
		$(configureData.root).appendTo(loc_settingParentView);
		var moduleInfoRequest = node_createServiceRequestInfoSequence();
		moduleInfoRequest.addRequest(node_appUtility.buildModuleInfoRequest(moduleDef, loc_uiApp, dataInfo==undefined?undefined:[dataInfo], configureData, loc_appDataService, {
			success : function(request, moduleInfo){
				return moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
					persist : dataInfo==undefined?false:dataInfo.persist,
					modified : false,
					name : moduleInfo.name
				}, {
					success : function(request){
						return moduleInfo;
					}
				}, request);
			}
		}));
		return moduleInfoRequest;
	};
	
	var loc_createSettingRoleRequest = function(moduleDef, handlers, request){
		var settingRoots = [];
		var settingsRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var appDataName = node_appUtility.getApplicationDataNames(moduleDef)[0];
		settingsRequest.addRequest(loc_appDataService.getGetAppDataSegmentInfoRequest(node_appUtility.getCurrentOwnerInfo(), appDataName, {
			success : function(request, settingDataInfos){
				var settingRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
				settingRequest.addRequest(loc_createSettingModuleRequest(moduleDef, new node_ApplicationDataSegmentInfo(node_appUtility.getCurrentOwnerInfo(), appDataName, node_appUtility.createAppDataSegmentId(), "New Setting", false)));   
				_.each(settingDataInfos[appDataName], function(dataInfo, index){
					settingRequest.addRequest(loc_createSettingModuleRequest(moduleDef, dataInfo));
				});
				return settingRequest;
			}
		}));
		return settingsRequest;
	};
	
	var loc_out = {
		
		processComponentValueChangeEvent : function(eventName, eventData, request){
			var out = eventData.moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
				modified : true
			}, request);
			if(out!=undefined)		node_requestServiceProcessor.processRequest(out);
		},	
			
		processComponentEvent : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.APP_EVENT_MODULEEVENT){
				if(eventData.eventName=="submitSetting"){
					loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, eventData.moduleInfo.id);
					var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(processRequest);
				}
				else if(eventData.eventName=="deleteSetting"){
					var moduleInfo = eventData.moduleInfo;
					var applicationDataInfo = moduleInfo.applicationDataInfo[0];
					
					node_requestServiceProcessor.processRequest(loc_appDataService.getDeleteAppDataSegmentRequest(node_appUtility.getCurrentOwnerInfo(), applicationDataInfo.dataName, applicationDataInfo.id, {
						success : function(request){
							loc_uiApp.removeModuleInfo(ROLE_SETTING, moduleInfo.id);
							moduleInfo.root.remove();
						}
					}, request));
					
				}
				else if(eventData.eventName=="saveSetting"){
					var moduleInfo = eventData.moduleInfo;
					var dataInfo = moduleInfo.applicationDataInfo[0];
					var outRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
					var saveRequest = moduleInfo.outputMapping["persistance"].getExecuteCommandRequest("execute", undefined, {
						success : function(request){
//							dataInfo.persist = true;
							return moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
								persist : dataInfo.persist,
								modified : false,
							}, undefined, request);
						}
					});
					outRequest.addRequest(saveRequest);
					node_requestServiceProcessor.processRequest(outRequest);
				}
				else{
					var eventHandler = loc_gate.getComponent().getEventHandler(eventData.moduleInfo.name, eventData.eventData.eventName);
					//if within module, defined the process for this event
					if(eventHandler!=undefined){
						var extraInput = {
							public : {
								EVENT : {
									event : eventData.eventName,
									data : eventData.eventData
								} 
							}
						};
						loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEEVENTHANDLER_PROCESS], extraInput, undefined, request));
					}
				}
			}
		},
			
		getInitRequest : function(handlers, request){
			
		},

		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var modulesRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){
					var modulesStartRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
					var allModules = loc_uiApp.getAllModuleInfo();
					_.each(allModules, function(moduleInfo){
						modulesStartRequest.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest("activate"));
					});
					return modulesStartRequest;
				}
			});
			var modules = loc_uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE];
			_.each(modules, function(moduleDef, name){
				var role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
				if(role==ROLE_APPLICATION){
					modulesRequest.addRequest(node_appUtility.buildApplicationModuleInfoRequest(moduleDef, loc_uiApp, loc_getModuleConfigureData(role), loc_appDataService));
//					modulesRequest.addRequest(node_appUtility.buildModuleInfoRequest(moduleDef, loc_uiApp, [], loc_getModuleConfigureData(role), loc_appDataService));
				}
				else if(role==ROLE_SETTING){
					modulesRequest.addRequest(loc_createSettingRoleRequest(moduleDef));
				}
			});
			
			out.addRequest(modulesRequest);
			return out;
		},
			
		getDeactiveRequest :function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			var moduleInfos = loc_uiApp.getAllModuleInfo();
			_.each(moduleInfos, function(moduleInfo, index){
				out.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest("destroy"));
			});

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_uiApp.clearModuleInfo();
			}));
			
			return out;
		},
		
		getInterface : function(){	},
	};
	
	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.utility", function(){node_appUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createApp;
	var node_createComponentComplex;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentInterface;
	var node_createStateBackupService;
	var node_createEventObject;

//*******************************************   Start Node Definition  ************************************** 	

var node_createAppRuntimeRequest = function(id, appDef, configure, componentDecorationInfos, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("createUIModule"), function(request){
		var app = node_createApp(id, appDef, ioInput);
		var runtime = node_createAppRuntime(app, configure, componentDecorationInfos);
		return runtime.prv_getInitRequest({
			success : function(request){
				return request.getData();
			}
		}).withData(runtime);
	}, handlers, request);
	return out;
};
	
var node_createAppRuntime = function(uiApp, configure, componentDecorationInfos){
	
	var loc_interface = {
		getPart : function(partId){  
			return loc_componentComplex.getComponent().getPart(partId);
//			return loc_out.getPart(partId);	
		},

		getExecutePartCommandRequest : function(partId, commandName, commandData, handlers, requestInfo){
			return this.getPart(partId).getExecuteCommandRequest(commandName, commandData, handlers, requestInfo);
		},
	};
	
	var loc_componentComplex = node_createComponentComplex(configure, loc_interface);
	var loc_localStore = configure.getConfigureData().__storeService;
	var loc_applicationDataService = configure.getConfigureData().__appDataService;
	var loc_stateBackupService = node_createStateBackupService("app", uiApp.getId(), uiApp.getVersion(), loc_localStore);
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_init = function(uiApp, configure, componentDecorationInfos){
		loc_componentComplex.addComponent(uiApp);
		loc_componentComplex.addDecorations(componentDecorationInfos);
		
		loc_componentComplex.registerEventListener(loc_eventListener, function(eventName, eventData, request){
			if(eventName=="executeProcess"){
				nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).executeProcessResourceRequest(eventData, undefined, undefined, undefined, request);
			}
		}, loc_out);
	};
	
	var loc_getIOContext = function(){  return loc_out.prv_getComponent().getIOContext();   };
	
	var loc_getProcessEnv = function(){   return loc_componentComplex.getInterface();    };
	
	var loc_getExecuteAppProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteProcessRequest(process, loc_out.prv_getComponent().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteAppProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_out.prv_getComponent().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteAppProcessRequest(process, extraInput, handlers, request);
	};

	var loc_getGoActiveRequest = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentComplex.getStartRequest());
		out.addRequest(loc_getExecuteAppProcessByNameRequest("active"));
		return out;
	};

	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		return loc_getGoActiveRequest(request);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]=
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DeactiveUIAppRuntime", {}), undefined, request);
		out.addRequest(loc_componentComplex.getDeactiveRequest());
		loc_componentComplex.clearState();
		return out;
	};	

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DestroyUIAppRuntime", {}), undefined, request);
		out.addRequest(loc_componentComplex.getDestroyRequest());
		return out;
	};

	var loc_out = {
			
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentComplex.getInitRequest());
			out.addRequest(loc_out.prv_getComponent().getInitIOContextRequest());
			return out;
		},
	
		prv_getExecuteCommandRequest : function(command, parms, handlers, request){	
			return loc_componentComplex.getExecuteCommandRequest(command, parms, handlers, request);	
		},
		
		prv_getComponent : function(){  return loc_componentComplex.getComponent();   },
		prv_getIODataSet : function(){  return loc_getIOContext();	},

		prv_registerEventListener : function(listener, handler, thisContext){	return loc_componentComplex.registerEventListener(listener, handler, thisContext);	},
		prv_unregisterEventListener : function(listener){	return loc_componentComplex.unregisterEventListener(listener); },
		
		getInterface : function(){   return node_getComponentInterface(loc_out);  },
	};
	
	loc_init(uiApp, configure, componentDecorationInfos);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithComponentInterface(loc_out, loc_out);

	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.createApp", function(){node_createApp = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createComponentComplex", function(){node_createComponentComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentInterface", function(){node_makeObjectWithComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_loadComponentResourceRequest;
	var node_createConfigure;
	var node_requestServiceProcessor;
	var node_createAppRuntimeRequest;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIAppService = function(){
	
	var loc_out = {

		getGetUIAppEntryRuntimeRequest : function(id, app, configure, ioInput, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIAppResource"), handlers, request);

			configure = node_createConfigure(configure);
			var componentDecorationInfo = configure.getConfigureData().appDecoration;
			out.addRequest(node_loadComponentResourceRequest(
				typeof app === 'string'? 
					{
						componentResourceId : app,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPENTRY
					} : app, 
				componentDecorationInfo==undefined?undefined:
					{
						decoration : componentDecorationInfo,
						type : node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIAPPDECORATION
					},
				{
					success : function(request, componentInfo){
						//create ui module runtime
						return node_createAppRuntimeRequest(id, componentInfo.component, configure, componentInfo.decoration, ioInput, {
							success : function(request, uiAppRuntime){
								return uiAppRuntime;
							}
						});
					}
				}));
			
			return out;
		},			
			
		executeGetUIAppEntryRuntimeRequest : function(id, appEntryId, appConfigureId, appStatelessData, handlers, requester_parent){
			var requestInfo = this.getGetUIAppEntryRuntimeRequest(id, appEntryId, appConfigureId, appStatelessData, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.loadComponentResourceRequest", function(){node_loadComponentResourceRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppRuntimeRequest", function(){node_createAppRuntimeRequest = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUIAppService", node_createUIAppService); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_createServiceRequestInfoSequence;
	var node_getComponentInterface;
	var node_ModuleInfo;
	var node_createServiceRequestInfoSimple;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){

	var loc_appDataPrefex = "applicationData_";

	var loc_out = {
		
		createAppDataSegmentId : function(){
			return new Date().getMilliseconds() + "";
		},
			
		getCurrentOwnerInfo : function(){
			return nosliw.runtime.getSecurityService().getOwnerInfo();
		},
			
		//find which application data this module depend on
		getApplicationDataNames : function(moduleDef){
			var out = [];
			var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
			for(var i in dataDependency){
				var dataName = dataDependency[i];
				if(dataName.startsWith(loc_appDataPrefex)){
					out.push(dataName.substring(loc_appDataPrefex.length));
				}
			}
			return out;
		},
			
		getApplicationDataIOName : function(appDataName){
			return loc_appDataPrefex+appDataName;
		},

		buildApplicationModuleInfoRequest : function(moduleDef, uiApp, configureData, appDataService, handlers, request){
			var appDataNames = loc_out.getApplicationDataNames(moduleDef);
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(appDataNames.length==0){
				out.addRequest(loc_out.buildModuleInfoRequest(moduleDef, uiApp, [], configureData, appDataService));
			}
			else{
				out.addRequest(appDataService.getGetAppDataSegmentInfoRequest(loc_out.getCurrentOwnerInfo(), appDataNames, {
					success : function(request, appDataInfosByName){
						var appDatas = [];
						_.each(appDataNames, function(appDataName, index){
							var appDataInfos = appDataInfosByName[appDataName];
							if(appDataInfos==undefined || appDataInfos.length==0){
								appDatas.push(new node_ApplicationDataSegmentInfo(loc_out.getCurrentOwnerInfo(), appDataName, loc_out.createAppDataSegmentId(), "", false));
							}
							else{
								appDatas.push(appDataInfos[0]);
							}
						});
						return loc_out.buildModuleInfoRequest(moduleDef, uiApp, appDatas, configureData, appDataService);
					}
				}));
			}
			return out;
		},
		
		buildModuleInfoRequest : function(moduleDef, uiApp, applicationDataInfo, configureData, appDataService, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var moduleInfo = new node_ModuleInfo(moduleDef);
			moduleInfo.root = configureData.root;
			moduleInfo.id = uiApp.getId()+"."+nosliw.generateId();
			if(applicationDataInfo!=undefined) moduleInfo.applicationDataInfo = applicationDataInfo;
			
			moduleInfo.externalIO = node_createIODataSet();
			moduleInfo.externalIO.setData(undefined, uiApp.getIOContext().generateDataEle());
			loc_out.buildModuleExternalAppDataIO(uiApp.getIOContext(), moduleInfo, appDataService, uiApp.prv_app.appDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_APPLICATIONDATA]);
			
			loc_out.buildModuleInputMapping(moduleInfo);
			moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
			loc_out.buildMoudleInputIO(moduleInfo);
			
//			if(applicationDataInfo!=undefined && applicationDataInfo.length==1){
//				moduleInfo.name = applicationDataInfo[0].version;
//			}
			
			out.addRequest(nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleInfo.id, moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], configureData, moduleInfo.inputIO, {
				success : function(requestInfo, uiModuleRuntime){
					moduleInfo.module = uiModuleRuntime;
					loc_out.buildModuleOutputMapping(moduleInfo);
					moduleInfo = uiApp.addModuleInfo(moduleInfo);
					return moduleInfo;
				}
			}));
			return out;
		},

		buildModuleExternalAppDataIO : function(appIOContext, moduleInfo, appDataService, appDataDefs){
			_.each(moduleInfo.applicationDataInfo, function(appDataInfo, index){
				loc_out.buildExternalDataIOForAppDataInfo(moduleInfo.externalIO, appDataInfo, appDataService, appDataDefs[appDataInfo.dataName]);
			});
		},
		
		buildExternalDataIOForAppDataInfo : function(externalIO, appDataInfo, appDataService, appDef){
			var dataIOName = loc_out.getApplicationDataIOName(appDataInfo.dataName);
			externalIO.setData(dataIOName, node_createDynamicData(
				function(handlers, request){
					if(appDataInfo.persist==true){
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getGetAppDataSegmentByIdRequest(loc_out.getCurrentOwnerInfo(), appDataInfo.dataName, appDataInfo.id, {
							success : function(request, dataInfo){
								return dataInfo.data;
							}
						}));
						return out;
					}
					else{
						return node_createServiceRequestInfoSimple(undefined, function(request){
							var out = {};
							_.each(appDef[node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], function(ele, name){
								out[name] = ele[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
							});
							return out;
						}, handlers, request);
					}
				},
				function(value, handlers, request){
					if(appDataInfo.persist==true){
						//modify
						return appDataService.getUpdateAppDataSegmentRequest(appDataInfo.ownerInfo, appDataInfo.dataName, appDataInfo.id, value, handlers, request);
					}
					else{
						//new
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getAddAppDataSegmentRequest(appDataInfo.ownerInfo, appDataInfo.dataName, 0, appDataInfo.id, value, appDataInfo.version, {
							success : function(request){
								appDataInfo.persist=true;
							}
						}));
						return out;
					}
				}
			));
		},

		buildMoudleInputIO : function(moduleInfo){
			var out = node_createIODataSet();
			var dynamicData = node_createDynamicData(
				function(handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					if(moduleInfo.currentInputMapping!=undefined){
						out.addRequest(moduleInfo.currentInputMapping.getExecuteRequest({
							success : function(request, dataIo){
								return dataIo.getGetDataValueRequest();
							}
						}));
					}
					return out;
				} 
			);
			out.setData(undefined, dynamicData);
			moduleInfo.inputIO = out;
		},
		
		buildModuleInputMapping : function(moduleInfo){
			var inputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
			var out = {};
			_.each(inputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(moduleInfo.externalIO, mapping);
			});
			moduleInfo.inputMapping = out;
		},
		
		buildModuleOutputMapping : function(moduleInfo){
			var outputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
			var out = {};
			var comInterface = node_getComponentInterface(moduleInfo.module);
			_.each(outputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(comInterface.getIOContext(), mapping, moduleInfo.externalIO);
			});
			moduleInfo.outputMapping = out;
			return moduleInfo;
		},
	};
	
	return loc_out;
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("iotask");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createIODataSet;
	var node_requestServiceProcessor;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_ioTaskUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createDataAssociation = function(inputIO, dataAssociationDef, outputIODataSet){
	
//	if(node_getObjectType(inputIO)==node_CONSTANT.TYPEDOBJECT_TYPE_VALUE)  inputIO = node_createIODataSet(inputIO);
	var loc_inputIO = node_createIODataSet(inputIO);

	var loc_outputIODataSet = node_createIODataSet(outputIODataSet);
	var loc_dataAssociationDef = dataAssociationDef;

	var loc_dyanimicValueBuild = function(output, outputPathSegs, input, intpuPathSegs){
		var inputValue = node_objectOperationUtility.getObjectAttributeByPathSegs(input, intpuPathSegs);
		node_objectOperationUtility.operateObjectByPathSegs(output, outputPathSegs, node_CONSTANT.WRAPPER_OPERATION_SET, inputValue);
		return output;
	};
	
	var loc_executeDataAssociationConvertFun  = function(association, inputDataSet){
		if(association==undefined || association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION]==undefined) return undefined;
		return association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_CONVERTFUNCTION](inputDataSet, loc_dyanimicValueBuild);
	};

	var loc_toTargetRequest = function(value, targetName, isTargetFlat, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(value==undefined){
			//if outputvalue is undefined, then no impact on outputTarget
//			out.addRequest(loc_outputIODataSet.getGetDataValueRequest(targetName));
		}
		else{
			out.addRequest(loc_outputIODataSet.getMergeDataValueRequest(targetName, value, isTargetFlat));
		}
		return out;
	};

	var loc_processMatchersRequest = function(value, matchersByPath, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		if(value!=undefined && matchersByPath!=undefined){
			var matchersByPathRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					_.each(resultSet.getResults(), function(result, path){
						node_objectOperationUtility.operateObject(value, path, node_CONSTANT.WRAPPER_OPERATION_SET, result);
					});
					return value;
				}
			});
			_.each(matchersByPath, function(matchers, path){
				var valueByPath = node_objectOperationUtility.getObjectAttributeByPath(value, path);
				matchersByPathRequest.addRequest(path, node_createExpressionService.getMatchDataRequest(valueByPath, matchers));
			});
			out.addRequest(matchersByPathRequest);
		}
		else{
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(){ return value;  })); 
		}
		return out;
	};
	
	var loc_getExecuteMappingAssociationRequest = function(inputDataSet, association, targetName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteAssociation", {}), handlers, request);

		//use convert function to calculate output
		var output = loc_executeDataAssociationConvertFun(association, inputDataSet); 

		//matchers
		out.addRequest(loc_processMatchersRequest(output, association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_OUTPUTMATCHERS], {
			success :function(request, value){
				//to target
				return loc_toTargetRequest(value, targetName, association[node_COMMONATRIBUTECONSTANT.EXECUTABLEASSOCIATION_FLATOUTPUT]);
			}
		}));

		return out;
	};

	var loc_getExecuteMappingDataAssociationRequest = function(inputDataSet, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteMappingDataAssociation", {}), handlers, request);
		var executeAssociationsRequest = node_createServiceRequestInfoSet(undefined, {
			success : function(request, resultSet){
				return loc_outputIODataSet;
			}
		});
		
		_.each(loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_ASSOCIATION], function(association, targetName){
			executeAssociationsRequest.addRequest(targetName, loc_getExecuteMappingAssociationRequest(inputDataSet, association, targetName));
		});
		out.addRequest(executeAssociationsRequest);

		return out;
	};

	var loc_getExecuteMirrorDataAssociationRequest = function(inputDataSet, handlers, request){
		var service = new node_ServiceInfo("ExecuteMirrorDataAssociation", {});
		var out = node_createServiceRequestInfoSet(undefined, {
			success : function(request, resultSet){
				return loc_outputIODataSet;
			}
		});

		_.each(inputDataSet, function(inputData, name){
			var inputFlat = loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_INPUT][name];
			var outputFlat = loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_OUTPUT][name];
			
			var outputData = inputData;
			if(inputFlat!=outputFlat){
				if(inputFlat==true){
					outputData = {
						public : inputData
					}
				}
				else{
					outputData = {};
					_.each(node_ioTaskUtility.getContextTypes(), function(categary, index){
						var context = inputData[categary];
						if(context!=undefined){
							_.each(context, function(value, name){
								outputData[name] = value;
							});
						}
					});
				}
			}
			
			out.addRequest(name, loc_toTargetRequest(outputData, name, true));
		});
		
		return out;
	};

	var loc_getExecuteNoneDataAssociationRequest = function(intputDataSet, handlers, request){
		return node_createServiceRequestInfoSimple(undefined, function(request){
			return loc_outputIODataSet;
		}, handlers, request);
	};

	var loc_getExecuteDataAssociationRequest = function(inputIO, extraDataSet, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataAssociation", {}), handlers, request);
		out.addRequest(loc_getInputDataSetRequest(inputIO, extraDataSet, {
			success : function(request, intputDataSet){
				if(loc_dataAssociationDef==undefined)  return loc_getExecuteNoneDataAssociationRequest(intputDataSet);
				else{
					var type = loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_TYPE];
					if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_MAPPING)	return loc_getExecuteMappingDataAssociationRequest(intputDataSet);
					else if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_MIRROR)		return loc_getExecuteMirrorDataAssociationRequest(intputDataSet);
					else if(type==node_COMMONCONSTANT.DATAASSOCIATION_TYPE_NONE)	return loc_getExecuteNoneDataAssociationRequest(intputDataSet);
				}
			}
		}));
		return out;
	};

	var loc_getInputDataSetRequest = function(inputIO, extraDataSet, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(inputIO.getGetDataSetValueRequest({
			success : function(request, intputDataSet){
				if(extraDataSet!=undefined){
					_.each(extraDataSet.getDataSet(), function(extraData, setName){
						var inputData = intputDataSet[setName];
						if(inputData==undefined){
							inputData = {};
							intputDataSet[setName] = inputData;
						}
						node_ioTaskUtility.mergeContext(extraData, inputData, loc_dataAssociationDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEDATAASSOCIATION_INPUT][name]);
					})
				}
				return intputDataSet;
			}
		}));
		return out;
	};
	
	
	var loc_out = {
		
		getExecuteDataAssociationRequest : function(extraData, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteDataAssociation", {}), handlers, request);
			if(extraData!=undefined)  extraData = node_createIODataSet(extraData);
			var inputIOType = node_getObjectType(loc_inputIO);
			if(inputIOType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DATASET){
				out.addRequest(loc_getExecuteDataAssociationRequest(loc_inputIO, extraData, {
					success :function(request){
						return loc_outputIODataSet;
					}
				}));
			}
			else if(inputIOType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION){
				out.add(loc_inputIO.getExecuteRequest({
					success : function(request, outputIO){
						out.addRequest(loc_getExecuteDataAssociationRequest(outputIO, extraData, {
							success :function(request){
								return loc_outputIODataSet;
							}
						}));
					}
				}));
			}
			return out;
		},
			
		getExecuteRequest : function(handlers, request){
			return this.getExecuteDataAssociationRequest(undefined, handlers, request);
		},

		executeRequest : function(handlers, request){
			var requestInfo = this.getExecuteRequest(handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteCommandRequest : function(commandName, data, handlers, request){
			if(commandName=="execute"){
				return this.getExecuteDataAssociationRequest(data, handlers, request);
			}
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION);

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskUtility", function(){node_ioTaskUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createDataAssociation", node_createDataAssociation); 

})(packageObj);
//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSet;
	var node_ioTaskUtility;
	var node_createEventObject;
	var node_destroyUtil;
//*******************************************   Start Node Definition  ************************************** 	

//task result 
//  resultName : name of the result
//  result: result value map (value name / value)
var node_IOTaskResult = function(resultName, resultValue){
	this.resultName = resultName;
	this.resultValue = resultValue; 
};

var node_ExternalMapping = function(dataIO, dataAssociationDef){
	this.dataIO = dataIO;
	this.dataAssociationDef = dataAssociationDef;
	node_makeObjectWithType(this, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_EXTERNALMAPPING);
};

var node_createDynamicData = function(getValueRequestFun, setValueRequestFun){
	var loc_getValueRequestFun = getValueRequestFun;
	var loc_setValueRequestFun = setValueRequestFun;
	
	var loc_out = {
		getGetValueRequest : function(handlers, request){
			return loc_getValueRequestFun(handlers, request);
		},
		
		getSetValueRequest : function(value, handlers, request){
			return loc_setValueRequestFun(value, handlers, request);
		}
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA);
	
	return loc_out;
};

var node_createIODataSet = function(value){
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();
	
	if(value!=undefined&&node_getObjectType(value)==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DATASET){
		return value;
	}

	var loc_init = function(value){
		//value is default value
		if(value!=undefined) loc_out.prv_dataSet[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT] = value;
	};
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		prv_dataSet : {},
		prv_id : nosliw.generateId(),
			
		setData : function(name, data, request){  
			if(name==undefined)  name = node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
			loc_out.prv_dataSet[name] = data;   
			loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
		},
		
		getData : function(name){
			if(name==undefined)  name = node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
			var out = loc_out.prv_dataSet[name];
			if(out==undefined){
				out = {};
				loc_out.prv_dataSet[name] = out;
			}
			return out;
		},

		generateDataEle : function(name){
			return node_createDynamicData(
				function(handlers, request){
					return loc_out.getGetDataValueRequest(name, handlers, request);
				},
				function(value, handlers, request){
					return loc_out.getSetDataValueRequest(name, value, handlers, request);
				}
			);
		},
		
		getDataSet : function(){   return loc_out.prv_dataSet;   },
		
		getGetDataValueRequest : function(name, handlers, request){
			var dataEle = this.getData(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA){
				return dataEle.getGetValueRequest(handlers, request);
			}
			else{
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return dataEle;
				}, handlers, request);
			}
		},

		getGetDataSetValueRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var getDataItemRequest = node_createServiceRequestInfoSet(undefined, {
				success : function(request, resultSet){
					var dataSetValue = {};
					_.each(resultSet.getResults(), function(value, name){
						dataSetValue[name] = value;
					});
					return dataSetValue;
				}
			});
			
			_.each(loc_out.prv_dataSet, function(dataSetEle, name){
				getDataItemRequest.addRequest(name, loc_out.getGetDataValueRequest(name));
			});
			out.addRequest(getDataItemRequest);
			return out;
		},

		getSetDataValueRequest : function(name, value, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var dataEle = this.getData(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA){
				return loc_out.getData(name).getSetValueRequest(value, {
					success : function(request, data){
						loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
						return data;
					}
				});
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					loc_out.setData(name, value, request);
					return value;
				}));
			}
			return out;
		},
		
		getMergeDataValueRequest : function(name, value, isDataFlat, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var dataEle = this.getData(name);
			var dataEleType = node_getObjectType(dataEle);
			if(dataEleType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DYNAMICDATA){
				out.addRequest(loc_out.getGetDataValueRequest(name, {
					success : function(request, value){
						var output = node_ioTaskUtility.mergeContext(request.getData('value'), value, isDataFlat);
						return loc_out.getData(request.getData('name')).getSetValueRequest(output, {
							success : function(request, data){
								loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
								return data;
							}
						});
					}
				}).withData(name, 'name').withData(value, 'value'));
			}
			else{
				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					var data = node_ioTaskUtility.mergeContext(request.getData('value'), loc_out.getData(request.getData('name')), isDataFlat);
					loc_trigueEvent(node_CONSTANT.IODATASET_EVENT_CHANGE, undefined, request);
					return data;
				}).withData(name, 'name').withData(value, 'value'));
			}
			return out;
		},
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },
		
		destroy : function(request){
			loc_eventSource.clearup();
			loc_eventListener.clearup();
			_.each(loc_out.prv_dataSet, function(data, name){
				node_destroyUtil(data, request);
			});
			loc_out.prv_dataSet = undefined;
		}
	};
	
	loc_init(value);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_DATASET);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.ioTaskUtility", function(){node_ioTaskUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.destroyUtil", function(){node_destroyUtil = this.getData();});

//Register Node by Name
packageObj.createChildNode("IOTaskResult", node_IOTaskResult); 
packageObj.createChildNode("createIODataSet", node_createIODataSet); 
packageObj.createChildNode("createDynamicData", node_createDynamicData); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createIODataSet;
	var node_createDataAssociation;
//*******************************************   Start Node Definition  ************************************** 	

var node_ioTaskProcessor = function(){
	
	var loc_out = {
			
		getExecuteIOTaskRequest : function(externalIO, extraInputData, ioMapping, getTaskRequest, handlers, request){
			return loc_out.getExecuteIORequest(
					externalIO, 
					extraInputData,
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_INPUTMAPPING], 
					getTaskRequest, 
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_OUTPUTMAPPING], 
					externalIO, 
					handlers, 
					request);
		},
			
		getExecuteIORequest : function(input, extraInputdata, inputDataAssociation, getTaskRequest, outputDataAssociationByResult, outputIO, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			var inputIO = node_createIODataSet(input);
			var taskInputIO = node_createIODataSet();
			var inputDataAssociation = node_createDataAssociation(inputIO, inputDataAssociation, taskInputIO);
			out.addRequest(inputDataAssociation.getExecuteDataAssociationRequest(extraInputdata, {
				success : function(requestInfo, taskInputIO){
					var taskInput = taskInputIO.getData();
					//execute task
					var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTask", {}));
					executeIOTaskRequest.addRequest(getTaskRequest(taskInput, {
						success : function(request, taskResult){
							//process output association according to result name
							var outputDataAssociationDef;
							if(typeof outputDataAssociationByResult === "function")		outputDataAssociationDef = outputDataAssociationByResult(taskResult.resultName);
							else   outputDataAssociationDef = outputDataAssociationByResult[taskResult.resultName];

							var inputDataAssociation = node_createDataAssociation(taskResult.resultValue, outputDataAssociationDef, outputIO);
							return inputDataAssociation.getExecuteRequest({
								success :function(request, taskOutputDataSetIO){
									return new node_IOTaskResult(taskResult.resultName, taskOutputDataSetIO);
								}
							});
						}
					}));
					return executeIOTaskRequest;
				}
			}));
			return out;
		},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskProcessor", node_ioTaskProcessor); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_out = {

		getContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE 
			];
		},

		getReversedContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
			];
		},
		
		mergeContext : function(source, target, isFlat){
			if(target==undefined)   target = {};
			if(isFlat==true){
				_.each(source, function(value, name){
					target[name] = value;
				});
			}
			else{
				_.each(source, function(c, categary){
					var cc = target[categary];
					if(cc==undefined){
						cc = {};
						target[categary] = cc;
					}
					_.each(c, function(ele, name){
						cc[name] = ele;
					});
				});
			}
			return target;
		}
		

	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
var library = nosliw.getPackage("statemachine");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_StateTransitPath = function(from, to, path){
	this.from = from;
	this.to = to;
	this.path = path;
};
	
var node_CommandInfo = function(name, from, nexts){
	this.name = name;
	this.from = from;
	this.nexts = nexts;
};
	
var node_NextStateInfo = function(name, callBack, reverseCallBack){
	this.name = name;
	this.callBack = callBack;
	this.reverseCallBack = reverseCallBack;
};	
	
var node_StateInfo = function(name, nextStates){
	this.name = name;
	this.nextStates = nextStates;
	if(this.nextStates==undefined)  this.nextStates = {};
};

node_StateInfo.prototype = {
	addNextState : function(name, callBack, reverseCallBack){
		this.nextStates[name] = new node_NextStateInfo(name, callBack, reverseCallBack);
	}
};

var node_TransitInfo = function(from, to){
	this.from = from;
	this.to = to;
};

var node_createStateMachineDef = function(){

	var loc_states = {};
	
	var loc_commands = {};

	var loc_nextCommandsByState;

	var loc_transitPath = {};
	
	var loc_addState = function(stateInfo){   loc_states[stateInfo.name] = stateInfo;    };
	
	var loc_getStateInfo = function(state){
		var stateInfo = loc_states[state];
		if(stateInfo==undefined){
			stateInfo = new node_StateInfo(state);
			loc_addState(stateInfo);
		}
		return stateInfo;
	};

	var loc_getAllStates = function(){
		var out = [];
		_.each(loc_states, function(state, name){   out.push(name);   });
		return out;
	};

	var loc_getNextCandidateStates = function(state){
		var out = [];
		_.each(loc_getStateInfo(state).nextStates, function(state, name){   out.push(name);   });
		return out;
	};
	

	var loc_buildNextCommandsByState = function(){
		loc_nextCommandsByState = {};
		
		_.each(loc_getAllStates(), function(state, i){
			var commands = [];
			_.each(loc_commands, function(byName, command){
				_.each(byName, function(commandInfo, from){
					if(from==state){
						commands.push(command);
					}
				});
			});
			loc_nextCommandsByState[state] = commands;
		});
		
		
//		_.each(loc_getAllStates(), function(state, i){
//			var commands = [];
//			var candidates = loc_getNextCandidateStates(state);
//			_.each(loc_commands, function(commandInfo, command){
//				if(candidates.includes(commandInfo.nexts[0])){
//					if(commandInfo.froms==undefined || commandInfo.froms.includes(state)){
//						commands.push(command);
//					}
//				}
//			});
//			loc_nextCommandsByState[state] = commands;
//		});
	};
	
	var loc_discoverTransitPath = function(from, to){
		
	};
	
	var loc_out = {

		getStateInfo : function(state){		return loc_getStateInfo(state);	},	
			
		addStateInfo : function(transitInfo, callBack, reverseCallBack){
			var stateInfoFrom = loc_getStateInfo(transitInfo.from);
			var stateInfoTo = loc_getStateInfo(transitInfo.to);
			stateInfoFrom.addNextState(transitInfo.to, callBack, reverseCallBack);
		},

		addCommand : function(commandInfo){
			var byName = loc_commands[commandInfo.name];
			if(byName==undefined){
				byName = {};
				loc_commands[commandInfo.name] = byName;
			}
			byName[commandInfo.from] = commandInfo;
		},
		
		getCandidateCommands : function(state){
			if(loc_nextCommandsByState==undefined){
				loc_buildNextCommandsByState();
			}
			return loc_nextCommandsByState[state];
		},
		
		getCandidateTransits : function(state){	return loc_getNextCandidateStates(state);	}, 
		
		getAllStates : function(){     
			var out = [];
			_.each(loc_states, function(stateInfo, stateName){ out.push(stateName);  });
			return out;
		},
		
		getAllCommands : function(){   
			var out = [];
			_.each(loc_commands, function(commandInfo, commandName){ out.push(commandName);  });
			return out;
		},
		getCommandInfo : function(command, from){		
			return loc_commands[command][from];	
		},
		
		addTransitPath : function(stateTransitPath){
			var fromTransit = loc_transitPath[stateTransitPath.from];
			if(fromTransit==undefined){
				fromTransit = {};
				loc_transitPath[stateTransitPath.from] = fromTransit;
			}
			fromTransit[stateTransitPath.to] = stateTransitPath;
		},
		
		getTransitPath : function(from, to){
			var fromTransit = loc_transitPath[from];
			if(fromTransit==undefined){
				fromTransit = {};
				loc_transitPath[from] = fromTransit;
			}
			var out = fromTransit[to];
			if(out==undefined){
				var out = loc_discoverTransitPath(from, to);
				if(out!=undefined){
					fromTransit[to] = out;
				}
			}
			return out;
		}
		
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("StateTransitPath", node_StateTransitPath); 
packageObj.createChildNode("TransitInfo", node_TransitInfo); 
packageObj.createChildNode("CommandInfo", node_CommandInfo); 
packageObj.createChildNode("NextStateInfo", node_NextStateInfo); 
packageObj.createChildNode("StateInfo", node_StateInfo);
packageObj.createChildNode("createStateMachineDef", node_createStateMachineDef); 

})(packageObj);
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_createEventObject;
	var node_requestUtility;
	var node_requestServiceProcessor;
	var node_getObjectType;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoCommon;
	var node_ServiceRequestExecuteInfo;
	var node_buildServiceProvider;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createStateMachineTask = function(nexts, stateMachine){

	var loc_stateMachine = stateMachine;
	var loc_from = loc_stateMachine.getCurrentState();
	var loc_nexts = nexts;
	
	var loc_eventObj = node_createEventObject();
	
	var loc_currentNext = -1;

	var loc_processNext = function(request){
		loc_currentNext++;
		if(loc_currentNext>=loc_nexts.length){
			//finish
			loc_stateMachine.prv_finishTask();
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, undefined, request);
		}
		else{
			var listener = loc_stateMachine.prv_registerEventListener(undefined, function(eventName, eventData, request){
				loc_stateMachine.prv_unregisterEventListener(listener);
				if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
					loc_processNext(request);
				}
				else if (eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION || eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION){
					loc_currentNext = loc_currentNext - 2;
					loc_rollBack(request);
					loc_stateMachine.prv_finishTask();
					//finish
					loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, undefined, request);
				}
			});
			loc_stateMachine.prv_startTransit(nexts[loc_currentNext], request);
		}
	};
	
	var loc_rollBack = function(request){
		while(loc_currentNext>=0){
			loc_stateMachine.prv_rollBack(loc_nexts[loc_currentNext], request);
			loc_currentNext--;
		};
	};
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};
	
	var loc_out = {
		
		process : function(request){	
			var request = loc_out.getRequestInfo(request);
			loc_processNext(request);
			return request;
		},
		
		getProcessRequest : function(handlers, request){
			var request = loc_out.getRequestInfo(request);
			var out = node_createServiceRequestInfoCommon(undefined, handlers, request);
			out.setRequestExecuteInfo(new node_ServiceRequestExecuteInfo(function(request){
				var listener = loc_out.registerEventListener(undefined, function(eventName, eventData, request){
					if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
						out.executeSuccessHandler();
					}
					else if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION){
						out.executeErrorHandler();
					}
					loc_out.unregisterEventListener(listener);
				});
				loc_processNext(request);
			}));
			return out;
		},
		
		transitSuccess : function(request){   loc_stateMachine.prv_transitSuccess(request);	},
		transitFail : function(request){   loc_stateMachine.prv_transitFail(request);	},

		registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },
	};
	
	loc_out = node_buildServiceProvider(loc_out, "stateMachineTask");

	return loc_out;
};

var node_createStateMachine = function(stateDef, initState, thisContext){

	var loc_stateDef = stateDef;
	
	var loc_thisContext = thisContext;

	var loc_eventObj = node_createEventObject();

	var loc_currentTask;
	var loc_currentState = initState;
	var loc_inTransit = undefined;
	var loc_finishTransit = true;
	
	var loc_trigueEvent = function(eventName, eventData, request){	loc_eventObj.triggerEvent(eventName, eventData, request);	};

	var loc_startTransit = function(next, request){
		loc_finishTransit = false;
		
		request = node_createServiceRequestInfoCommon(undefined, undefined, request);

		//if in the same state, then just do nothing
		if(next == loc_out.getCurrentState()){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Samestate", request);
			loc_finishTransit = true;
			return;
		}
		//if in transit, do nothing
		if(loc_inTransit!=undefined){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|InTransiting", request);
			loc_finishTransit = true;
			return;
		}
		
		var nextStateInfo = loc_stateDef.getStateInfo(loc_out.getCurrentState()).nextStates[next];
		if(nextStateInfo==undefined){
			loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_NOTRANSITION, next+"|Notvalidtransit", request);
			loc_finishTransit = true;
			return;
		}
		
		loc_inTransit = new node_TransitInfo(loc_currentState, next); 
		var callBack = nextStateInfo.callBack;
		var initResult = true;      
		if(callBack!=undefined)	initResult = callBack.call(loc_thisContext, request);
		
		loc_processStatuesResult(initResult, request);
	};
	
	
	var loc_processStatuesResult = function(result, request){

		if(result==true || result==undefined){
			//success finish
			loc_successTransit(request);
			return;
		}
		if(result==false)   return;           //not finish, wait for finish method get called
		
		var entityType = node_getObjectType(result);
		if(node_CONSTANT.TYPEDOBJECT_TYPE_ERROR==entityType){
			//failed
			loc_failTransit(request);
			return;
		}
		else if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
			var transitRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){			},
				error : function(request){
					loc_thisContext;
					loc_failTransit(request);			
				},
				exception : function(request){	loc_failTransit(request);			}
			});

			//if return request, then build wrapper request
			transitRequest.addRequest(result);

			transitRequest.registerEventListener(undefined, function(eventName, eventData){
				if(loc_finishTransit==false){
					if(eventName==node_CONSTANT.REQUEST_EVENT_DONE){
						loc_successTransit(request);
					}
				}
			});
			
			node_requestServiceProcessor.processRequest(transitRequest, {
				attchedTo : request
			});
			return;
		}
	};

	/*
	 * method called when transition is finished successfully
	 */
	var loc_successTransit = function(request){
		var inTransit = loc_inTransit;
		loc_inTransit = undefined;
		loc_currentState = inTransit.to;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, inTransit, request);
		loc_finishTransit = true;
	};

	var loc_failTransit = function(request){
		var inTransit = loc_inTransit;
		loc_rollBack(inTransit, request);
		loc_inTransit = undefined;
		loc_trigueEvent(node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FAILTRANSITION, inTransit, request);
		loc_finishTransit = true;
	};
	
	var loc_rollBack = function(transitInfo, request){
		var reverseCallBack = loc_stateDef.getStateInfo(inTransit.from).nextStates[inTransit.to].reverseCallBack;
		if(reverseCallBack!=undefined)	reverseCallBack.apply(loc_thisContext, request);
		loc_currentState = transitInfo.from;
	};
	
	var loc_out = {
			
		prv_startTransit : function(next, request){  loc_startTransit(next, request);    },	
		prv_rollBack : function(next, request){  loc_rollBack(new node_TransitInfo(next, loc_currentState), request);    },

		prv_transitSuccess : function(request){   loc_successTransit(request);	},
		prv_transitFail : function(request){   loc_failTransit(request);	},

		prv_registerEventListener : function(listener, handler, thisContext){	return loc_eventObj.registerListener(undefined, listener, handler, thisContext); },
		prv_unregisterEventListener : function(listener){	return loc_eventObj.unregister(listener); },

		prv_finishTask : function(){  loc_currentTask = undefined;  },

		getCurrentState : function(){	return loc_currentState;	},
		getAllStates : function(){  return loc_stateDef.getAllStates();   },
		
		getAllCommands : function(){  return loc_stateDef.getAllCommands();  },
		getNextStateCandidates : function(){  return loc_stateDef.getCandidateTransits(loc_out.getCurrentState());   },
		getCommandCandidates : function(){   return loc_stateDef.getCandidateCommands(loc_out.getCurrentState());   },
		
		newTask : function(nexts){
			if(loc_currentTask!=undefined)  return undefined;
			if(typeof nexts === 'string' || nexts instanceof String){
				//command
				var commandInfo = loc_stateDef.getCommandInfo(nexts, loc_currentState);
				if(commandInfo!=undefined){
					//command
					nexts = commandInfo.nexts;
				}
				else{
					//target state
					var transitPath = loc_stateDef.getTransitPath(loc_currentState, nexts);
					nexts = transitPath.path.slice();
					nexts = nexts.push(transitPath.to);
				}
			}
			loc_currentTask = node_createStateMachineTask(nexts, loc_out);
			return loc_currentTask;
		},
		
	};
	return loc_out;
};	
	
//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){	node_ServiceRequestExecuteInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("statemachine.TransitInfo", function(){node_TransitInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.CommandInfo", function(){node_CommandInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.NextStateInfo", function(){node_NextStateInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.StateInfo", function(){node_StateInfo = this.getData();});
nosliw.registerSetNodeDataEvent("statemachine.createStateMachineDef", function(){node_createStateMachineDef = this.getData();}); 

//Register Node by Name
packageObj.createChildNode("createStateMachine", node_createStateMachine); 

})(packageObj);
	nosliw.registerNodeEvent("runtime", "active111", function(eventName, nodeName){
		
		nosliw.logging.info(nodeName + "    " + eventName );

		var resourceUtility = nosliw.getNodeData("resource.utility");

//		nosliw.runtime.getResourceService().executeDiscoverResourcesRequest([resourceUtility.createOperationResourceId("test.string;1.0.0", "subString")], 
//				{
//					success : function(request, resources){
//						nosliw.logging.info(JSON.stringify(resources));
//					}
//				}, undefined);
		
		
//		nosliw.runtime.getResourceService().executeGetResourcesRequest([resourceUtility.createOperationResourceId("test.string;1.0.0", "subString")], 
//				{
//					success : function(request, resources){
//						nosliw.logging.info(JSON.stringify(resources));
//					}
//				}, undefined);
		
		
		node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
		
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_TESTEXPRESSION;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYLOADTESTEXPRESSION_COMMAND_LOADTESTEXPRESSION;
		var parms = {
				suite : "expression1",
				expressionName : "main",
			};
		var gatewayRequest = nosliw.runtime.getGatewayService().executeExecuteGatewayCommandRequest(gatewayId, command, parms, {
			success : function(requestInfo, expressionResponse){
				nosliw.runtime.getExpressionService().executeExecuteExpressionRequest(expressionResponse.expression, expressionResponse.variablesValue, 
						{
							success : function(requestInfo, result){
								nosliw.logging.info(JSON.stringify(result));
							}	
						}, undefined);
			}	
		});

		/*
		nosliw.runtime.getExpressionService().executeExecuteExpressionRequest(expressionResponse.expression, expressionResponse.variablesValue, 
				{
					success : function(requestInfo, result){
						nosliw.logging.info(JSON.stringify(result));
					}	
				}, undefined);
		
		
		nosliw.runtime.getGateway().getExpressions(
				expressionRequest, 
				{
					success : function(request, expressionResponses){
						var expressionResponse = expressionResponses[0];
						nosliw.runtime.getExpressionService().executeExecuteExpressionRequest(expressionResponse.expression, expressionResponse.variablesValue, 
							{
								success : function(requestInfo, result){
									nosliw.logging.info(JSON.stringify(result));
								}	
							}, undefined);
						
					}
				}
		);
		*/
	});
	nosliw.registerNodeEvent("runtime", "active111", function(eventName, nodeName){
		
		nosliw.runtime.getUIResourceService().executeCreateUIPageRequest("Example1", 
				{
					success : function(requestInfo, uiResourceView){
						nosliw.logging.info(JSON.stringify(uiResourceView));
						
						uiResourceView.appendTo($('#testDiv'));
						
//						var views = uiResourceView.getViews();
//						$('#testDiv').append(views.children());

					}
				}
		);
	});
var library = nosliw.getPackage("security");
//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createSecurityService = function(){

	var loc_token;

	var loc_ownerType;
	var loc_ownerId;
	
	var loc_out = {
	
		getToken : function(){		return loc_token;	},
		
		setToken : function(token){		loc_token = token;		},
		
		getOwnerInfo : function(){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = loc_token || "testUser";
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE] = loc_ownerType || "app";
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID] = loc_ownerId || "testApp";
			return out;
		},
		
		createOwnerInfo : function(ownerType, ownerId){
			var out = {};
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_USERID] = loc_token;
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTTYPE] = ownerType;
			out[node_COMMONATRIBUTECONSTANT.OWNERINFO_COMPONENTID] = ownerId;
			return out;
		},
		
		setOwnerType : function(ownerType){ 	loc_ownerType = ownerType;	},
		
		setOwnerId : function(ownerId){  loc_ownerId = ownerId;   }
	};

	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createSecurityService", node_createSecurityService); 

})(packageObj);
