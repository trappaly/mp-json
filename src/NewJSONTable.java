package src;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * Creates a new JSONTable 
 * @author Medhashree Adhikari, Alyssa Trapp, and Vivien Yan
 */

public class NewJSONTable<K, V> {

  static final double LOAD_FACTOR = 0.5;

  public static final Iterator<KVPair<JSONString, JSONValue>> iterator = null;

  int size = 0;
  Object[] buckets;
  Random rand;

  public NewJSONTable() {
    this.rand = new Random();
    this.clear();
  }

  public boolean containsKey(JSONString key) {
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public JSONValue get(JSONString key) {
    int index = find(key);
    @SuppressWarnings("unchecked")
    ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[index];

    if (alist == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } else {
      for (Pair<JSONString, JSONValue> target : alist) {
        if (target.key().equals(key)) {
          return target.value();
        }
      }
      throw new IllegalAccessError("Same location " + key); // ChecJSONString if the JSON values have the same HashCode
    }
  }

  public JSONValue set(JSONString key, JSONValue value) {
    JSONValue result = null;

    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    }
    int index = find(key);
    @SuppressWarnings("unchecked")
    ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[index];

    if (alist == null) {
      alist = new ArrayList<Pair<JSONString, JSONValue>>();
      this.buckets[index] = alist;
    } else {
      for (Pair<JSONString, JSONValue> target : alist) {
        if (target.key().equals(key)) {
          Pair<JSONString, JSONValue> pair = new Pair<JSONString, JSONValue>(key, value);
          target = pair;
          return target.value();
        }
      }
      alist.add(new Pair<JSONString, JSONValue>(key, value));
      ++this.size;
    }
    return result;
  }

  public int size() {
    return this.size;
  }


  public void expand() {
    int newSize = 2 * this.buckets.length + rand.nextInt(10);

    Object[] oldBuckets= this.buckets;
    this.buckets = new Object[newSize];
    for(int i = 0; i < oldBuckets.length; i++){
      this.buckets[i] = oldBuckets[i];
    }
  }

  public int find(JSONString key) {
    return Math.abs(key.hashCode()) % this.buckets.length;
  }

  public String toString() {
    String ret = new String();
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[i];
      if (alist != null) {
        for (Pair<JSONString, JSONValue> pair : alist) {
          ret += pair.key() + ":" + pair.value() + "\n";
        }
      }
    }
    return ret;
  }

  public boolean equals(NewJSONTable<JSONString, JSONValue> object) {
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[i];
      @SuppressWarnings("unchecked")
      ArrayList<Pair<JSONString, JSONValue>> alist2 = (ArrayList<Pair<JSONString, JSONValue>>) object.buckets[i];
      
      if(alist==null && alist2==null){
        return true;
      }
      if (!alist.equals(alist2)) {
        return false;
      }
    }
    return true;
  }

  // public Iterator<V> values() {
  //   return MiscUtils.transform(this.iterator(), (pair) -> pair.value());
  // } // values()

  // +------------------+--------------------------------------------
  // | Iterator methods |
  // +------------------+

  /**
   * Iterate the key/value pairs in some order.
   */
  public Iterator<Pair<JSONString, JSONValue>> iterator() {
    return new Iterator<Pair<JSONString, JSONValue>>() {
      int i;
      int pos;

      public boolean hasNext() {
        @SuppressWarnings("unchecked")
        ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[this.i];

        int k = 0;

        Pair<JSONString, JSONValue> next = null;
        if (alist != null && k <= pos+1) {
          for (Pair<JSONString, JSONValue> pair : alist) {
            next = pair;
            k++;
          }
        }
        return(next == null);
      } // hasNext()

      public Pair<JSONString, JSONValue> next() {
        if(hasNext() == false){
          pos = 0;
          i++;
        }
        @SuppressWarnings("unchecked")
        ArrayList<Pair<JSONString, JSONValue>> alist = (ArrayList<Pair<JSONString, JSONValue>>) buckets[this.i];

        int k = 0;

        Pair<JSONString, JSONValue> current = null;
        if (alist != null && k <= pos) {
          for (Pair<JSONString, JSONValue> pair : alist) {
            current = pair;
          }
        }
        pos++;
        return current;
      } // next()
    }; // new Iterator
  } // iterator()

  public void clear() {
    this.buckets = new Object[41];
    this.size = 0;
  }
}
