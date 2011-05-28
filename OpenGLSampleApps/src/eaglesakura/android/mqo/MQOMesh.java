package eaglesakura.android.mqo;


/**
 * １つのメッシュ情報を扱う。
 * 今回は１メッシュにつき１マテリアルとする。
 */
public class MQOMesh {
    private MQOVertexBuffer vertices = new MQOVertexBuffer();
    private MQOIndexBuffer indices = null;
    private MQOMaterial material = null;
    private String name = "";


    /**
     * メッシュを生成する。
     * @param root
     * @param object
     */
    public MQOMesh(MQOFigure parent, MQOChunk root, MQOChunk object) {

        name = object.getHeader().get( 1 );

        //! 頂点
        {
            MQOChunk vchunk = object.getChild("vertex");

            if (vchunk != null) {
                int num = vchunk.getLineCount();
                //! 単純な頂点座標を生成し、バッファへと追加する
                for (int i = 0; i < num; ++i) {
                    MQOLine line = vchunk.getLine(i);
                    MQOVertex v = new MQOVertex();
                    v.position.x = Float.parseFloat(line.get(0));
                    v.position.y = Float.parseFloat(line.get(1));
                    v.position.z = Float.parseFloat(line.get(2));

                    //! 頂点を追加する。
                    vertices.addVertex(v);
                }
            }
        }

        //! Indices
        {
            MQOChunk fchunk = object.getChild("face");
            if (fchunk != null) {
                int num = fchunk.getLineCount();
                indices = new MQOIndexBuffer(num);
                int[] vtemp = new int[3];
                Vector2[] uvarray = new Vector2[] { new Vector2(), new Vector2(), new Vector2() };
                Color[] colors = new Color[] { new Color(), new Color(), new Color(), };

                for (int i = 0; i < num; ++i) {
                    MQOLine line = fchunk.getLine(i);
                    for (int k = 0; k < line.getWordCount(); ++k) {
                        String word = line.get(k);
                        if (word.startsWith("V(")) {
                            //! 頂点番号を解析する
                            {
                                String n = word.substring("V(".length());
                                vtemp[0] = Integer.parseInt(n);
                            }
                            ++k;
                            word = line.get(k);
                            vtemp[1] = Integer.parseInt(word);
                            ++k;
                            word = line.get(k);
                            vtemp[2] = Integer.parseInt(word.substring(0, word.length() - 1));
                        } else if (word.startsWith("M(")) {
                            //! マテリアル番号を解析する
                            String n = word.substring("M(".length(), word.length() - 1);
                            indices.setMaterial(Integer.parseInt(n));
                        } else if (word.startsWith("UV(")) {
                            //!   UV情報を解析する
                            {
                                String n = word.substring("UV(".length());
                                uvarray[0].x = Float.parseFloat(n);
                                ++k;
                                uvarray[0].y = Float.parseFloat(line.get(k));
                            }
                            {
                                ++k;
                                uvarray[1].x = Float.parseFloat(line.get(k));
                                ++k;
                                uvarray[1].y = Float.parseFloat(line.get(k));
                            }
                            {
                                ++k;
                                uvarray[2].x = Float.parseFloat(line.get(k));
                                ++k;
                                word = line.get(k);
                                uvarray[2].y = Float.parseFloat(word.substring(0, word.length() - 1));
                            }
                        } else if (word.startsWith("COL(")) {
                            //! 頂点カラーを解析する
                            {
                                String n = word.substring("COL(".length());
                                colors[0].set((int) Long.parseLong(n));
                            }
                            ++k;
                            word = line.get(k);
                            colors[1].set((int) Long.parseLong(word));
                            ++k;
                            word = line.get(k);
                            colors[2].set((int) Long.parseLong(word.substring(0, word.length() - 1)));
                        }
                    }

                    //! 頂点を追加する
                    for (int k = 0; k < 3; ++k) {
                        vtemp[k] = vertices.addVertex(vtemp[k], colors[k], uvarray[k]);
                    }
                    //! インデックスバッファへ登録する
                    indices.add(vtemp[0], vtemp[2], vtemp[1]);
                }

            }
        }
    }

    /**
     * メッシュ名を取得する。
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 描画用のマテリアルを取得する。
     * @return
     */
    public MQOMaterial getMaterial() {
        return material;
    }

    /**
     * 頂点一覧を取得する。
     * @return
     */
    public MQOVertexBuffer getVertices() {
        return vertices;
    }

    /**
     * インデックス一覧を取得する。
     * @return
     */
    public MQOIndexBuffer getIndices() {
        return indices;
    }
}
