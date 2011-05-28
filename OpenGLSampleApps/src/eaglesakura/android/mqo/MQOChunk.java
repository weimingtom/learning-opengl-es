/**
 *
 */
package eaglesakura.android.mqo;

import java.util.ArrayList;
import java.util.List;

/**
 * MQO内の１チャンクを扱う。
 */
public class MQOChunk {
    private List<MQOChunk> childs = new ArrayList<MQOChunk>();
    private List<MQOLine> lines = new ArrayList<MQOLine>();
    private MQOLine header = null;

    public MQOChunk() {

    }

    public MQOLine getHeader() {
        return header;
    }

    /**
     * ヘッダの行を設定する。<BR>
     * 最初の単語はチャンク名として認識される。
     * @param header
     */
    public void setHeader(MQOLine header) {
        this.header = header;
    }

    public String getName() {
        return header.get(0);
    }

    /**
     * 解析した中の１行を取得する。
     * @param index
     * @return
     */
    public MQOLine getLine(int index) {
        return lines.get(index);
    }

    /**
     * 内部行数の取得を行う。
     * @return
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * 解析行を追加する。
     * @param line
     */
    public void addLine(MQOLine line) {
        lines.add(line);
    }

    /**
     * 子を追加する。
     * @param child
     */
    public void addChild(MQOChunk child) {
        childs.add(child);
    }

    /**
     * 子を取得する。
     * @param name
     * @return
     */
    public MQOChunk getChild(String name) {
        for (MQOChunk c : childs) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * 特定の名前のチャンクをすべて返す。
     * @param name
     * @return
     */
    public MQOChunk[] getChilds(String name) {
        List<MQOChunk> result = new ArrayList<MQOChunk>();

        for (MQOChunk c : childs) {
            if (c.getName().equals(name)) {
                result.add(c);
            }
        }

        return result.toArray(new MQOChunk[result.size()]);
    }
}
