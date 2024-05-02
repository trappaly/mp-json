package src;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * Utilities for our simple implementation of JSON.
 * 
 * @author Sam Rebelskey, Medhashree Adhikari, Alyssa Trapp, and Vivien Yan 
 */
public class JSON {
  // +---------------+-----------------------------------------------
  // | Static fields |
  // +---------------+

  /**
   * The current position in the input.
   */
  static int pos;

  // +----------------+----------------------------------------------
  // | Static methods |
  // +----------------+

  /**
   * Parse a string into JSON.
   */
  public static JSONValue parse(String source) throws ParseException, IOException {
    return parse(new StringReader(source));
  } // parse(String)

  /**
   * Parse a file into JSON.
   */
  public static JSONValue parseFile(String filename) throws ParseException, IOException {
    FileReader reader = new FileReader(filename);
    JSONValue result = parse(reader);
    reader.close();
    return result;
  } // parseFile(String)

  /**
   * Parse JSON from a reader.
   */
  public static JSONValue parse(Reader source) throws ParseException, IOException {
    pos = 0;
    JSONValue result = parseKernel(source);
    if (-1 != skipWhitespace(source)) {
      throw new ParseException("Characters remain at end", pos);
    }
    return result;
  } // parse(Reader)

  // +---------------+-----------------------------------------------
  // | Local helpers |
  // +---------------+

  /**
   * Parse JSON from a reader, keeping track of the current position
   */
  // @SuppressWarnings("null")
  static JSONValue parseKernel(Reader source) throws ParseException, IOException {
    int ch;
    ch = skipWhitespace(source);
    if (-1 == ch) {
      throw new ParseException("Unexpected end of file", pos);
    }

    Character current = (char) ch;
    String key = null;
    String value = null;
    JSONValue value1 = new JSONString(null);
    NewJSONTable<JSONString, JSONValue> table = new NewJSONTable<>();

    while (ch != -1) {
      if (current.equals('"')) {
        while (!current.equals('"')) {
          ch = skipWhitespace(source);
          current = (char) ch;
          key += current;
        }
        JSONString key1 = new JSONString(key);

        ch = skipWhitespace(source);
        current = (char) ch;

        if (current.equals(':')) {
          ch = skipWhitespace(source);
          current = (char) ch;

          if (current.equals('"')) {
            while (!current.equals('"')) {
              ch = skipWhitespace(source);
              current = (char) ch;
              value += current;
            }
            value1 = new JSONString(value);
            table.set(key1, value1);
            ch = skipWhitespace(source);
            current = (char) ch;
          }

          else if (current.equals('-') || Character.isDigit(current)) {
            while (!current.equals(',')) {
              if (current.equals('.') || current.equals('e') || current.equals('E')) {
                value1 = new JSONReal(value);
              } else {
                value1 = new JSONInteger(value);
              }
              ch = skipWhitespace(source);
              current = (char) ch;
              value += current;
            }
            table.set(key1, value1);
          }

          else if (current.equals('[')) {
            JSONArray JArr = new JSONArray(); // new JSON array
            while (!current.equals(']')) {
              JArr = arrayCheck(JArr, ch, current, source);
            }
            table.set(key1, JArr);
            ch = skipWhitespace(source);
            current = (char) ch;
          }

          else if (Character.isAlphabetic(current)) {
            while (!current.equals(',')) {
              value += current;
              ch = skipWhitespace(source);
              current = (char) ch;
            }
            if (value.equals("true") || (value.equals("false")) || (value.equals("null"))) {
              value1 = new JSONConstant(value);
              table.set(key1, value1);
            }
          }

          else if (current.equals('{')) {
            table.set(key1, parseKernel(source));
          }
        }
      }
      ch = skipWhitespace(source);
      current = (char) ch;
    }
    JSONHash hash = new JSONHash(table);
    return hash;
  } // parseKernel

  public static JSONArray arrayCheck(JSONArray JArr, int ch, Character current, Reader source) throws IOException {
    // checks for each thing type
    ch = skipWhitespace(source);
    current = (char) ch;
    JSONValue value1 = new JSONString(null);

    while (!current.equals(']')) {

      if (current.equals('-') || Character.isDigit(current)) {
        String value = null;
        while (!current.equals(',') && !current.equals(']')) {
          value += current;
          if (current.equals('.') || current.equals('e') || current.equals('E')) {
            value1 = new JSONReal(value);
          } else {
            value1 = new JSONInteger(value);
          }
          ch = skipWhitespace(source);
          current = (char) ch;
        }
        JArr.add(value1);
      }

      if (current.equals('"')) {
        String value = null;
        while (!current.equals('"')) {
          value += current;
          ch = skipWhitespace(source);
          current = (char) ch;
        }
        value1 = new JSONString(value);
        JArr.add(value1);
      }

      if (Character.isAlphabetic(current)) {
        String value = null;
        while (!current.equals(',') && !current.equals(']')) {
          value += current;
          ch = skipWhitespace(source);
          current = (char) ch;
        }

        if (value.equals("true") || (value.equals("false")) || (value.equals("null"))) {
          value1 = new JSONConstant(value);
          JArr.add(value1);
        }
      }

      if (current.equals('{')) {
        try {
          JArr.add(parseKernel(source));
        } catch (ParseException | IOException e) {
          e.printStackTrace();
        }
      }
      ch = skipWhitespace(source);
      current = (char) ch;
    }
    return JArr;
  }

  /**
   * Get the next character from source, skipping over whitespace.
   */
  static int skipWhitespace(Reader source) throws IOException {
    int ch;
    do {
      ch = source.read();
      ++pos;
    } while (isWhitespace(ch));
    return ch;
  } // skipWhitespace(Reader)

  /**
   * Determine if a character is JSON whitespace (newline, carriage return,
   * space, or tab).
   */
  static boolean isWhitespace(int ch) {
    return (' ' == ch) || ('\n' == ch) || ('\r' == ch) || ('\t' == ch);
  } // isWhiteSpace(int)

} // class JSON
