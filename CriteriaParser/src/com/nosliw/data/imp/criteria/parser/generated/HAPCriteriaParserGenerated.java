/* HAPCriteriaParserGenerated.java */
/* Generated By:JJTree&JavaCC: Do not edit this line. HAPCriteriaParserGenerated.java */
/* define the package name for parser classes */
package com.nosliw.data.imp.criteria.parser.generated;

public class HAPCriteriaParserGenerated/*@bgen(jjtree)*/implements HAPCriteriaParserGeneratedTreeConstants, HAPCriteriaParserGeneratedConstants {/*@bgen(jjtree)*/
  protected JJTHAPCriteriaParserGeneratedState jjtree = new JJTHAPCriteriaParserGeneratedState();
  public static void main(String args[]) throws ParseException, TokenMgrError {
                HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( System.in ) ;
                SimpleNode root = parser.Criteria(0);
                root.dump("");
  }

  final public void anyCriteria() throws ParseException {/*@bgen(jjtree) ANYCRITERIA */
  SimpleNode jjtn000 = new SimpleNode(JJTANYCRITERIA);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(ANY);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;

    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public Token referenceCriteria() throws ParseException {/*@bgen(jjtree) REFERENCECRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTREFERENCECRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t ;
    try {
      jj_consume_token(START_REFERENCE);
      t = jj_consume_token(NAME);
      jj_consume_token(END_REFERENCE);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value = t.image; {if ("" != null) return t;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public Token expressionCriteria() throws ParseException {/*@bgen(jjtree) EXPRESSIONCRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTEXPRESSIONCRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t ;
    try {
      jj_consume_token(START_EXPRESSION);
      t = jj_consume_token(NAME);
      jj_consume_token(END_EXPRESSION);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value = t.image; {if ("" != null) return t;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public SimpleNode idCriteria() throws ParseException {/*@bgen(jjtree) IDCRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTIDCRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t ;
    try {
      t = jj_consume_token(NAME);
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case START_SUBCRITERIA_OPEN:
        case START_SUBCRITERIA_CLOSE:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        subCriteriaGroup();
      }
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value = t.image; {if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public void idsCriteria() throws ParseException {/*@bgen(jjtree) IDSCRITERIA */
  SimpleNode jjtn000 = new SimpleNode(JJTIDSCRITERIA);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(START_IDS);
      idCriteria();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMAR:{
          ;
          break;
          }
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMAR);
        idCriteria();
      }
      jj_consume_token(END_IDS);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;

    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public void rangeCriteria() throws ParseException {/*@bgen(jjtree) RANGECRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTRANGECRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token from = null;
        Token to = null;
    try {
      jj_consume_token(START_RANGE);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NAME:{
          ;
          break;
          }
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        from = jj_consume_token(NAME);
      }
      jj_consume_token(RANGE);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NAME:{
          ;
          break;
          }
        default:
          jj_la1[3] = jj_gen;
          break label_4;
        }
        to = jj_consume_token(NAME);
      }
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case START_SUBCRITERIA_OPEN:
        case START_SUBCRITERIA_CLOSE:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_5;
        }
        subCriteriaGroup();
      }
      jj_consume_token(END_RANGE);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value = new String[]{from==null?null:from.image, to==null?null:to.image};
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  final public SimpleNode subCriteria(int index) throws ParseException {/*@bgen(jjtree) SUBCRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTSUBCRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);Token t ;
    try {
      t = jj_consume_token(NAME);
      jj_consume_token(ASSIGNMENT);
      Criteria(index);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value = t.image; {if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public SimpleNode subCriteriaGroup() throws ParseException {/*@bgen(jjtree) SUBCRITERIAGROUP */
        SimpleNode jjtn000 = new SimpleNode(JJTSUBCRITERIAGROUP);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);int i = 0;
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case START_SUBCRITERIA_OPEN:{
        jj_consume_token(START_SUBCRITERIA_OPEN);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NAME:{
          subCriteria(i);
i++;
          label_6:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case COMMAR:{
              ;
              break;
              }
            default:
              jj_la1[5] = jj_gen;
              break label_6;
            }
            jj_consume_token(COMMAR);
            subCriteria(i);
i++;
          }
          break;
          }
        default:
          jj_la1[6] = jj_gen;
          ;
        }
        jj_consume_token(END_SUBCRITERIA_OPEN);
jjtn000.value=true;
        break;
        }
      case START_SUBCRITERIA_CLOSE:{
        jj_consume_token(START_SUBCRITERIA_CLOSE);
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case NAME:{
          subCriteria(i);
i++;
          label_7:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
            case COMMAR:{
              ;
              break;
              }
            default:
              jj_la1[7] = jj_gen;
              break label_7;
            }
            jj_consume_token(COMMAR);
            subCriteria(i);
i++;
          }
          break;
          }
        default:
          jj_la1[8] = jj_gen;
          ;
        }
        jj_consume_token(END_SUBCRITERIA_CLOSE);
jjtn000.value=false;
        break;
        }
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public SimpleNode orCriteria() throws ParseException {/*@bgen(jjtree) ORCRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTORCRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);int i = 0;
    try {
      jj_consume_token(START_OR);
      Criteria(i);
i++;
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMAR:{
          ;
          break;
          }
        default:
          jj_la1[10] = jj_gen;
          break label_8;
        }
        jj_consume_token(COMMAR);
        Criteria(i);
i++;
      }
      jj_consume_token(END_OR);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public SimpleNode andCriteria() throws ParseException {/*@bgen(jjtree) ANDCRITERIA */
        SimpleNode jjtn000 = new SimpleNode(JJTANDCRITERIA);
        boolean jjtc000 = true;
        jjtree.openNodeScope(jjtn000);int i = 0;
    try {
      jj_consume_token(START_AND);
      Criteria(i);
i++;
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case COMMAR:{
          ;
          break;
          }
        default:
          jj_la1[11] = jj_gen;
          break label_9;
        }
        jj_consume_token(COMMAR);
        Criteria(i);
i++;
      }
      jj_consume_token(END_AND);
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
{if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  final public SimpleNode Criteria(int index) throws ParseException {/*@bgen(jjtree) CRITERIA */
  SimpleNode jjtn000 = new SimpleNode(JJTCRITERIA);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NAME:{
        idCriteria();
        break;
        }
      case START_IDS:{
        idsCriteria();
        break;
        }
      case START_RANGE:{
        rangeCriteria();
        break;
        }
      case START_REFERENCE:{
        referenceCriteria();
        break;
        }
      case START_EXPRESSION:{
        expressionCriteria();
        break;
        }
      case START_OR:{
        orCriteria();
        break;
        }
      case START_AND:{
        andCriteria();
        break;
        }
      case ANY:{
        anyCriteria();
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
jjtn000.value=index+"";   {if ("" != null) return jjtn000;}
    } catch (Throwable jjte000) {
if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public HAPCriteriaParserGeneratedTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x280,0x20000,0x200000,0x200000,0x280,0x20000,0x200000,0x20000,0x200000,0x280,0x20000,0x20000,0x30a82a,};
   }

  /** Constructor with InputStream. */
  public HAPCriteriaParserGenerated(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public HAPCriteriaParserGenerated(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new HAPCriteriaParserGeneratedTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public HAPCriteriaParserGenerated(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new HAPCriteriaParserGeneratedTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public HAPCriteriaParserGenerated(HAPCriteriaParserGeneratedTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(HAPCriteriaParserGeneratedTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[30];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 30; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
