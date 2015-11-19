http://www.oreilly.co.jp/books/9784873114965/

「初めてのOpenGL ES」で解説している項目のサンプルコードです。
内容の詳細については書籍内で触れられています。

update 2011.09.20
各Actiivtyのヘッダに書籍の関連する章を追加しました。


書籍内の正誤

P64　一番下の行列の図

&lt;BR&gt;


※右側の4x4行列内の数値が1になってしまっていますが、正しくは次のとおりです。

&lt;BR&gt;


誤：

&lt;BR&gt;


(1 1 1 1)

&lt;BR&gt;


(1 1 1 1)

&lt;BR&gt;


(1 1 1 1)

&lt;BR&gt;


(0 0 0 0)

&lt;BR&gt;



正：

&lt;BR&gt;


(1 0 0 0)

&lt;BR&gt;


(0 1 0 0)

&lt;BR&gt;


(0 0 1 0)

&lt;BR&gt;


(1 2 3 1)

&lt;BR&gt;



P65 一番上の行列の図（図6-1）

&lt;BR&gt;


※一番右下がw×0になっていますが、w×1が正しい数値です。

&lt;BR&gt;


誤：

&lt;BR&gt;


( x×1 x×0 x×0 x×0 )

&lt;BR&gt;


( y×0 y×1 y×0 y×0 )

&lt;BR&gt;


( z×0 z×0 z×1 z×0 )

&lt;BR&gt;


( w×1 w×2 w×3 w×0 )

&lt;BR&gt;




&lt;BR&gt;


正：

&lt;BR&gt;


( x×1 x×0 x×0 x×0 )

&lt;BR&gt;


( y×0 y×1 y×0 y×0 )

&lt;BR&gt;


( z×0 z×0 z×1 z×0 )

&lt;BR&gt;


( w×1 w×2 w×3 w×1 )

&lt;BR&gt;





質問やバグ等の指摘は@eaglesakuraまで。
http://twitter.com/eaglesakura