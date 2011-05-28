package eaglesakura.android.mqo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MQO用のファイルを読み取る。
 */
public class MQOFormatImporter {
    private BufferedReader reader = null;
    private MQOChunk rootChunk = null;
    private MQOFigure object = null;

    /**
     *
     * @param file
     */
    public MQOFormatImporter(InputStream file) throws IOException {
        reader = new BufferedReader(new InputStreamReader(file));
        parse();
        convert();
    }

    /**
     * チャンク情報からGLで使いやすい形式へ変換する。
     */
    private void convert() {
        object = new MQOFigure(rootChunk);

    }

    /**
     * コンバート済みのオブジェクトを取得する。
     * @return
     */
    public MQOFigure getConvertObject() {
        return object;
    }

    private MQOChunk readChunk(String header) throws IOException {
        MQOChunk result = new MQOChunk();
        MQOLine mqHead = MQOLine.create(header);
        if (mqHead == null) {
            return null;
        }

        if (!mqHead.isChunkHead()) {
            return null;
        }

        result.setHeader(mqHead);
        String str = null;
        while ((str = reader.readLine()) != null) {
            //! 最後の文字がヘッダだったら
            char last = str.charAt(str.length() - 1);
            if (last == '{') {
                result.addChild(readChunk(str));
            } else if (last == '}') {
                return result;
            } else {
                MQOLine line = MQOLine.create(str);
                result.addLine(line);
            }
        }
        return result;
    }

    /**
     * MQOを解析し、行ごとに分解する。
     */
    private void parse() throws IOException {
        //! 最初の２行は無視する。
        reader.readLine();
        reader.readLine();

        rootChunk = new MQOChunk();
        String line = null;

        while ((line = reader.readLine()) != null) {
            MQOChunk chunk = readChunk(line);
            if (chunk != null) {
                rootChunk.addChild(chunk);
            }
        }
    }
}
