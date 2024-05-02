package src;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * JSON hashes/objects.
 * @author Medhashree Adhikari, Alyssa Trapp, and Vivien Yan
 */
public class JSONHash implements JSONValue {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
  NewJSONTable<JSONString, JSONValue> value;
  
  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+
  public JSONHash(NewJSONTable<JSONString, JSONValue> value) {
    this.value = value;
  } // JASONHash(Table value)

  public JSONHash() {
    this.value = new NewJSONTable<>();
  } // JASONHash(Table value)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    return this.value.toString();         
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (other instanceof JSONHash){
      return this.value.equals(((JSONHash)other).value);
    }
    return this.value.equals(other);        
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return this.value.hashCode();          
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print(this.value.toString());                  // STUB
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  /* public ArrayListIterator<KVPair<JSONString,JSONValue>> arraylistIterator(){
    return new ArrayListIterator<T>(){
  }*/
  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
    return this.value.get(key);        
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString,JSONValue>> iterator() {
    return NewJSONTable.iterator;     
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  public void set(JSONString key, JSONValue value) {
      this.value.set(key, value);
  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return this.size();           // STUB
  } // size()
  

} // class JSONHash
