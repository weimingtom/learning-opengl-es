package eaglesakura.android.mqo;

/**
 * MQO用のマテリアルを管理する。<BR>
 * ただし、このクラスが管理するのはMQO用マテリアル属性の一部のみ。
 * @author eaglesakura
 *
 */
public class MQOMaterial {
    private String textureName = "";

    /**
     * マテリアル情報を抽出する。
     *
     * @param line
     */
    public MQOMaterial(MQOLine line) {
        for (int i = 0; i < line.getWordCount(); ++i) {
            String word = line.get(i);
            if (word.startsWith("tex(\"")) {
                String n = word.substring("tex(\"".length(), word.length() - 2);
                textureName = n;
            }
        }
    }

    /**
     * テクスチャファイル名を取得する。
     *
     * @return
     */
    public String getTextureName() {
        return textureName;
    }
}
