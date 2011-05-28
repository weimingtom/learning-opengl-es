/**
 *
 * @author eagle.sakura
 * @version 2010/04/03 : 新規作成
 */
package eaglesakura.android.mqo;

import java.util.ArrayList;
import java.util.List;

/**
 * 一行分の内容を解析・管理する。
 * @version 2010/04/03 : 新規作成
 */
public class MQOLine {
    /**
     * 有効な単語リスト。
     */
    private List<String> words = new ArrayList<String>();

    /**
     *
     */
    private MQOLine() {

    }

    /**
     * チャンクである = 行が "{"で終了している場合trueを返す。
     * @return
     */
    public boolean isChunkHead() {
        return words.get(words.size() - 1).equals("{");
    }

    /**
     * チャンクの終了行の場合、trueを返す。
     * @return
     */
    public boolean isChunkFooter() {
        return words.get(0).equals("}");
    }

    /**
     * 行の単語を取得する。
     * @param index
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public String get(int index) {
        return words.get(index);
    }

    /**
     * 名前・パラメータ用の単語数を取得する。<BR>
     * 末尾が"{"もしくは"}"の場合はそれを無視する。
     * @return
     * @version 2010/04/04 : 新規作成
     */
    public int getWordCount() {
        if (isChunkHead()) {
            return words.size() - 1;
        } else if (isChunkFooter()) {
            return 0;
        } else {
            return words.size();
        }
    }

    /**
     * 文字のセパレータを定義する。
     * @version 2010/04/03 : 新規作成
     */
    private static class Separator {
        private char sep = '\0';

        /**
         * 指定の文字をセパレータとして扱う。
         * @author eagle.sakura
         * @param c
         * @version 2010/04/03 : 新規作成
         */
        public Separator(char c) {
            sep = c;
        }

        /**
         * 区切り文字である場合、trueを返す。
         * @author eagle.sakura
         * @param c
         * @return
         * @version 2010/04/03 : 新規作成
         */
        public boolean isSeparate(char c) {
            return sep == c;
        }
    }

    /**
     * 複数のセパレータを管理し、単語を取得する。。
     * @version 2010/04/03 : 新規作成
     */
    private static class SeparatorList {
        public String line = "";
        private List<Separator> separators = new ArrayList<Separator>();

        /**
         *
         * @author eagle.sakura
         * @param lineText
         * @version 2010/04/03 : 新規作成
         */
        public SeparatorList() {
            separators.add(new Separator('\n'));
            separators.add(new Separator('\r'));
            separators.add(new Separator('\0'));
            separators.add(new Separator('\t'));
            separators.add(new Separator(' '));
        }

        /**
         * セパレータであればtrueを返す。
         * @author eagle.sakura
         * @param c
         * @return
         * @version 2010/04/03 : 新規作成
         */
        public boolean isSeparator(char c) {
            for (Separator sep : separators) {
                if (sep.isSeparate(c)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * セパレータを削除する。
         * @author eagle.sakura
         * @return
         * @version 2010/04/03 : 新規作成
         */
        private char breakSeparator() {
            char[] array = line.toCharArray();
            int head = 0;

            char c = '\0';
            while (head < array.length) {
                c = array[head];
                /**
                 * セパレータ以外が当たった。
                 */
                if (!isSeparator(c)) {
                    line = line.substring(head);
                    return c;
                }

                ++head;
            }

            return '\0';
        }

        /**
         * 次の単語を取得する。
         * @author eagle.sakura
         * @return
         * @version 2010/04/03 : 新規作成
         */
        public String nextWord() {
            if (line.length() == 0) {
                return null;
            }

            String str = "";
            int length = 0;

            //!	セパレータを削除する。
            char head = breakSeparator();
            //!	文字をバッファ化する
            char[] buffer = line.toCharArray();

            //!	文字列囲みの場合
            if (head == '\"') {
                length = 1;
                //!	囲み記号までを検索する。
                while (length < buffer.length && buffer[length] != '\"') {
                    ++length;
                }

                str = line.substring(1, length);
            } else {
                //!	セパレータまでを検索する
                while (length < buffer.length && !isSeparator(buffer[length])) {
                    ++length;
                }

                str = line.substring(0, length);
            }

            if (length < buffer.length) {
                line = line.substring(length + 1);
            } else {
                line = "";
            }

            if (str.length() == 0) {
                return null;
            } else {
                return str;
            }
        }
    }

    private static SeparatorList parser = new SeparatorList();

    /**
     * TSFとして認識するために構文解析した文字列を返す。<BR>
     * ただし、コメントや空行の場合、このメソッドはnullを返す。
     * @author eagle.sakura
     * @param line
     * @return
     * @version 2010/04/03 : 新規作成
     */
    public static MQOLine create(String line) {
        if (line == null) {
            return null;
        }
        MQOLine result = new MQOLine();
        parser.line = line; //!<	新規にラインを指定

        while (true) {
            String str = parser.nextWord();

            //!	コメントである場合、それ以降を無視する。
            if (str == null) {
                //!	単語がない場合、ラインは無効
                if (result.words.size() == 0) {
                    return null;
                } else {
                    return result;
                }
            }

            //!	単語を追加
            result.words.add(str);
        }
    }
}
