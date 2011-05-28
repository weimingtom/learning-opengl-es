package eaglesakura.android.mqo;

import java.util.ArrayList;
import java.util.List;

/**
 * MQOファイルで構築されたフィギュアを管理する。
 * @author eaglesakura
 *
 */
public class MQOFigure {
    private List<MQOMaterial> materials = new ArrayList<MQOMaterial>();
    private List<MQOMesh> meshs = new ArrayList<MQOMesh>();

    /**
     *
     * @param root
     */
    public MQOFigure(MQOChunk root) {
        //! マテリアルを生成する。
        {
            MQOChunk material = root.getChild("Material");
            for (int i = 0; i < material.getLineCount(); ++i) {
                MQOLine mat = material.getLine(i);
                materials.add(new MQOMaterial(mat));
            }
        }

        //! メッシュを生成する。
        {
            MQOChunk[] objects = root.getChilds("Object");
            for (MQOChunk obj : objects) {
                MQOMesh mesh = new MQOMesh(this, root, obj);
                meshs.add(mesh);
            }
        }
    }

    /**
     * メッシュを取得する。
     * @param index
     * @return
     */
    public MQOMesh getMesh(int index) {
        return meshs.get(index);
    }

    /**
     * 管理しているメッシュ数を取得する。
     * @return
     */
    public int getMeshCount() {
        return meshs.size();
    }

    /**
     * 材質情報を取得する。
     *
     * @param index
     * @return
     */
    public MQOMaterial getMaterial(int index) {
        return materials.get(index);
    }

    /**
     * 材質数を取得する。
     *
     * @return
     */
    public int getMaterialCount() {
        return materials.size();
    }
}
