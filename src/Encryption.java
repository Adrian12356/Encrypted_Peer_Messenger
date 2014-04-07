public class Encryption extends Crypto {
	public String o;

	public Encryption(String m, String k) {
		if (k != "error") {
			char[] ck = fill(k);
			int[] i = fill1(ck);
			char[] cm = fill(m);
			int[] s = fill(cm);
			int[] r = scramble(s, i);
			o = em(r);
		}else{
			//Do Nothing
		}
		return;
	}

	public static int[] scramble(int[] m, int[] c) {
		int x = 0;
		int y = 0;
		while (x < m.length) {
			if (y >= c.length) {
				y = 0;
			}
			m[x] = m[x] * c[y];
			x++;
			y++;
		}
		return m;
	}

	public static String em(int[] r) {
		String m = "";
		for (int x = 0; x < r.length; x++) {
			m = m + Integer.toString(r[x]) + ":";
		}
		return m;
	}
}
