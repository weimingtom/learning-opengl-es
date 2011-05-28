package eaglesakura.sample.ogles.bmp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 256色パレットBitmapを利用する。
 */
public class PaletteBitmap {
    /**
     * カラーパレット
     */
    private byte[] palette;

    /**
     * パレットインデックス
     */
    private byte[] indices;

    /**
     * 画像幅・高さ
     */
    private int width, height;

    /**
     *
     */
    private PaletteBitmap() {

    }

    /**
     * 画像幅
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * 画像高
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * OpenGL用のバッファに変換して返す。
     *
     * @param result
     * @return
     */
    public ByteBuffer toGLTextureBuffer(ByteBuffer result) {
        //! palette
        result.put(palette);

        //! indices
        result.put(indices);

        //! position reset
        result.position(0);

        return result;
    }

    /**
     * OpenGL用のバッファに変換して返す。
     *
     * @param result
     * @return
     */
    public ByteBuffer toGLTextureBuffer() {
        ByteBuffer bb = ByteBuffer.allocateDirect(getGLTextureBufferSize());
        bb.order(ByteOrder.nativeOrder());
        return toGLTextureBuffer(bb);
    }

    /**
     * OpenGL用テクスチャにした場合のサイズを取得する。
     *
     * @return
     */
    public int getGLTextureBufferSize() {
        return palette.length + indices.length;
    }

    /**
     * Bitmapファイルから情報を読み取って返す。
     *
     * @param bitmapFile
     * @return
     */
    public static PaletteBitmap createInstance(InputStream bitmapFile) throws IOException {
        PaletteBitmap result = new PaletteBitmap();

        {
            char B = (char) bitmapFile.read();
            char M = (char) bitmapFile.read();

            if (B != 'B' || M != 'M') {
                return null;
            }

            bitmapFile.skip(4); //!<    ファイルサイズは必要ない
            bitmapFile.skip(2 + 2); //!<    予約領域は必要ない
        }

        //! ファイル先頭から画素情報までのオフセット
        Util.readIntLE(bitmapFile);

        int headerSize = Util.readIntLE(bitmapFile);

        if (headerSize < 40) {
            //! 小サイズヘッダ
            result.width = Util.readShortLE(bitmapFile);
            result.height = Util.readShortLE(bitmapFile);
        } else {
            //! 大サイズヘッダ
            result.width = Util.readIntLE(bitmapFile);
            result.height = Util.readIntLE(bitmapFile);
        }

        /**
         * テクスチャは2のn乗、つまりtrueのビットが1つでなければならない。
         */
        if (Integer.bitCount(result.width) != 1 || Integer.bitCount(result.height) != 1) {
            return null;
        }

        bitmapFile.skip(2); //!<    プレーン数は必要ない
        int bitCount = Util.readShortLE(bitmapFile); //!<    画素あたりのデータサイズ

        //! 256色BMP以外はエラー
        if (bitCount != 8) {
            return null;
        }

        //! ヘッダを読み飛ばす
        if (headerSize > 12) {
            bitmapFile.skip(headerSize - 16);
        }

        /**
         * 画素情報はBGRA(Aはreservedのため常に0となる)
         * OpenGLで利用するためには画素の順番を変更する必要がある。
         */
        byte[] bgra = new byte[256 * 4];
        bitmapFile.read(bgra);

        //! 要素の順番を入れ替える。
        for (int i = 0; i < 256; ++i) {
            byte b = bgra[4 * i + 0];
            byte g = bgra[4 * i + 1];
            byte r = bgra[4 * i + 2];

            //! rgbとなるよう入れ替える
            bgra[4 * i + 0] = r;
            bgra[4 * i + 1] = g;
            bgra[4 * i + 2] = b;

            if (r == (byte) 0 && g == (byte) 0 && b == (byte) 0) {
                bgra[4 * i + 3] = 0;
            } else {
                bgra[4 * i + 3] = (byte) 255;
            }
        }

        result.palette = bgra;

        //! 画素情報を読み込む
        byte[] tempIndices = new byte[result.width * result.height];
        result.indices = new byte[result.width * result.height];
        bitmapFile.read(tempIndices);

        //! 画素情報は上下反転しているため、これも上下反転する
        for (int i = 0; i < result.height; ++i) {
            //! 書き込むラインは上下反転
            int line = result.height - i - 1;

            for (int k = 0; k < result.width; ++k) {
                result.indices[line * result.width + k] = tempIndices[i * result.width + k];
            }
        }
        return result;
    }
}
