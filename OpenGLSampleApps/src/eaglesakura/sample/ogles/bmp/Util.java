package eaglesakura.sample.ogles.bmp;

import java.io.IOException;
import java.io.InputStream;

public class Util
{
    /**
     * リトルエンディアンからshort型を取得する。
     * @param is
     * @return
     */
    public static short    readShortLE( InputStream is )    throws  IOException
    {
        int n0 = is.read();
        int n1 = is.read();

        return  ( short )( ( n1 << 8 )
                        |  ( n0 << 0 ) );
    }

    /**
     * リトルエンディアンからint型を取得する。
     * @param is
     * @return
     * @throws IOException
     */
    public static int      readIntLE( InputStream is )  throws  IOException
    {
        int n0 = is.read();
        int n1 = is.read();
        int n2 = is.read();
        int n3 = is.read();

        return  ( int )( ( n3 << 24 )
                       | ( n2 << 16 )
                       | ( n1 << 8 )
                       | ( n0 ) );
    }
}
